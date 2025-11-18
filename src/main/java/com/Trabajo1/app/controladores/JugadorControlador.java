package com.Trabajo1.app.controladores;

import com.Trabajo1.app.entidades.Jugador;
import com.Trabajo1.app.servicios.ClubServicio;
import com.Trabajo1.app.servicios.JugadorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/jugadores")
public class JugadorControlador {

	@Autowired
	private JugadorServicio jugadorServicio;

	@Autowired
	private ClubServicio clubServicio;

	@GetMapping
	public String listarJugadores(Model model) {
		List<Jugador> jugadores = jugadorServicio.obtenerTodos();

		// ✅ CALCULAR ESTADÍSTICAS EN EL CONTROLADOR
		long totalDelanteros = jugadores.stream().filter(j -> "Delantero".equals(j.getPosicion())).count();

		long totalMediocampistas = jugadores.stream().filter(j -> "Mediocampista".equals(j.getPosicion())).count();

		long totalDefensas = jugadores.stream().filter(j -> "Defensa".equals(j.getPosicion())).count();

		long totalPorteros = jugadores.stream().filter(j -> "Portero".equals(j.getPosicion())).count();

		model.addAttribute("jugadores", jugadores);
		model.addAttribute("totalDelanteros", totalDelanteros);
		model.addAttribute("totalMediocampistas", totalMediocampistas);
		model.addAttribute("totalDefensas", totalDefensas);
		model.addAttribute("totalPorteros", totalPorteros);
		model.addAttribute("titulo", "Lista de Jugadores");
		return "jugadores/lista";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("jugador", new Jugador());
		model.addAttribute("clubes", clubServicio.obtenerTodos()); // ✅ AGREGAR ESTO
		model.addAttribute("titulo", "Nuevo Jugador");
		return "jugadores/formulario";
	}

	@PostMapping("/guardar")
	public String guardarJugador(@ModelAttribute Jugador jugador) {
		jugadorServicio.guardar(jugador);
		return "redirect:/jugadores";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
		Optional<Jugador> jugador = jugadorServicio.obtenerPorId(id);
		if (jugador.isPresent()) {
			model.addAttribute("jugador", jugador.get());
			model.addAttribute("clubes", clubServicio.obtenerTodos()); // ✅ AGREGAR ESTO
			model.addAttribute("titulo", "Editar Jugador");
			return "jugadores/formulario";
		}
		return "redirect:/jugadores";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarJugador(@PathVariable Long id) {
		jugadorServicio.eliminar(id);
		return "redirect:/jugadores";
	}

	@GetMapping("/buscar")
	public String buscarJugadores(@RequestParam(required = false) String posicion,
			@RequestParam(required = false) String apellido, Model model) {
		List<Jugador> jugadores;

		if (posicion != null && !posicion.isEmpty()) {
			jugadores = jugadorServicio.buscarPorPosicion(posicion);
			model.addAttribute("titulo", "Jugadores - " + posicion);
		} else if (apellido != null && !apellido.isEmpty()) {
			jugadores = jugadorServicio.buscarPorApellido(apellido);
			model.addAttribute("titulo", "Jugadores - Apellido: " + apellido);
		} else {
			jugadores = jugadorServicio.obtenerTodos();
			model.addAttribute("titulo", "Lista de Jugadores");
		}

		model.addAttribute("jugadores", jugadores);
		return "jugadores/lista";
	}
}