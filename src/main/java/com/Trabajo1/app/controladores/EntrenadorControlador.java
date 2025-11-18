package com.Trabajo1.app.controladores;

import com.Trabajo1.app.entidades.Entrenador;
import com.Trabajo1.app.entidades.Club;
import com.Trabajo1.app.servicios.EntrenadorServicio;
import com.Trabajo1.app.servicios.ClubServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/entrenadores")
public class EntrenadorControlador {

	@Autowired
	private EntrenadorServicio entrenadorServicio;

	@Autowired
	private ClubServicio clubServicio;

	@GetMapping
	public String listarEntrenadores(Model model) {
		try {
			List<Entrenador> entrenadores = entrenadorServicio.obtenerTodos();

			// ✅ PREVENIR RECURSIÓN: Limpiar relaciones circulares para Thymeleaf
			for (Entrenador entrenador : entrenadores) {
				if (entrenador.getClub() != null) {
					// Crear un club "ligero" sin relaciones circulares
					Club clubLigero = new Club();
					clubLigero.setId(entrenador.getClub().getId());
					clubLigero.setNombre(entrenador.getClub().getNombre());
					// No copiar otras relaciones para evitar recursión
					entrenador.setClub(clubLigero);
				}
			}

			model.addAttribute("entrenadores", entrenadores);
			model.addAttribute("titulo", "Lista de Entrenadores");
			return "entrenadores/lista";
		} catch (Exception e) {
			// Log del error
			System.err.println("Error al listar entrenadores: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("error", "Error al cargar los entrenadores");
			return "error";
		}
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("entrenador", new Entrenador());
		model.addAttribute("clubes", clubServicio.obtenerTodos());
		model.addAttribute("titulo", "Nuevo Entrenador");
		return "entrenadores/formulario";
	}

	@PostMapping("/guardar")
	public String guardarEntrenador(@ModelAttribute Entrenador entrenador,
			@RequestParam(required = false) Long clubId) {

		// Manejar la relación bidireccional
		if (clubId != null) {
			Club club = clubServicio.obtenerPorId(clubId).orElseThrow(() -> new RuntimeException("Club no encontrado"));

			// Establecer relación bidireccional
			entrenador.setClub(club);
			club.setEntrenador(entrenador);

			// Guardar ambos para mantener consistencia
			clubServicio.guardar(club);
		} else {
			// Si no hay club, asegurarse de que la relación sea nula
			entrenador.setClub(null);
		}

		entrenadorServicio.guardar(entrenador);
		return "redirect:/entrenadores";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
		Optional<Entrenador> entrenador = entrenadorServicio.obtenerPorId(id);
		if (entrenador.isPresent()) {
			model.addAttribute("entrenador", entrenador.get());
			model.addAttribute("clubes", clubServicio.obtenerTodos());
			model.addAttribute("titulo", "Editar Entrenador");
			return "entrenadores/formulario";
		}
		return "redirect:/entrenadores";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarEntrenador(@PathVariable Long id) {
		Optional<Entrenador> entrenadorOpt = entrenadorServicio.obtenerPorId(id);

		if (entrenadorOpt.isPresent()) {
			Entrenador entrenador = entrenadorOpt.get();

			// Manejar relación bidireccional al eliminar
			if (entrenador.getClub() != null) {
				Club club = entrenador.getClub();
				club.setEntrenador(null); // Romper la relación
				clubServicio.guardar(club);
			}

			entrenadorServicio.eliminar(id);
		}

		return "redirect:/entrenadores";
	}

	@GetMapping("/buscar")
	public String buscarEntrenadores(@RequestParam String nacionalidad, Model model) {
		model.addAttribute("entrenadores", entrenadorServicio.buscarPorNacionalidad(nacionalidad));
		model.addAttribute("titulo", "Entrenadores de: " + nacionalidad);
		return "entrenadores/lista";
	}

	@GetMapping("/sin-club")
	public String entrenadoresSinClub(Model model) {
		model.addAttribute("entrenadores", entrenadorServicio.buscarEntrenadoresSinClub());
		model.addAttribute("titulo", "Entrenadores Sin Club");
		return "entrenadores/lista";
	}
}
