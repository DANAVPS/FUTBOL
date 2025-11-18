package com.Trabajo1.app.controladores;

import com.Trabajo1.app.entidades.Competicion;
import com.Trabajo1.app.entidades.Club;
import com.Trabajo1.app.servicios.CompeticionServicio;
import com.Trabajo1.app.servicios.ClubServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/competiciones")
public class CompeticionControlador {

    @Autowired
    private CompeticionServicio competicionServicio;
    
    @Autowired
    private ClubServicio clubServicio; // NUEVO: Para listar clubes disponibles

    // --- MÉTODOS EXISTENTES (los mantienes) ---
    @GetMapping
    public String listarCompeticiones(Model model) {
    	List<Competicion> competiciones = competicionServicio.obtenerTodas();
        model.addAttribute("titulo", "Lista de Competiciones");
        model.addAttribute("competiciones", competiciones);
        model.addAttribute("totalPremios", competicionServicio.calcularTotalPremios());
        return "competiciones/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("competicion", new Competicion());
        model.addAttribute("titulo", "Nueva Competición");
        return "competiciones/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCompeticion(@ModelAttribute Competicion competicion) {
        competicionServicio.guardar(competicion);
        return "redirect:/competiciones";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Competicion> competicion = competicionServicio.obtenerPorId(id);
        if (competicion.isPresent()) {
            model.addAttribute("competicion", competicion.get());
            model.addAttribute("titulo", "Editar Competición");
            return "competiciones/formulario";
        }
        return "redirect:/competiciones";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCompeticion(@PathVariable Long id) {
        competicionServicio.eliminar(id);
        return "redirect:/competiciones";
    }

    @GetMapping("/activas")
    public String competicionesActivas(Model model) {
        List<Competicion> competiciones = competicionServicio.obtenerCompeticionesActivas();
        model.addAttribute("competiciones", competiciones);
        model.addAttribute("titulo", "Competiciones Activas");
        return "competiciones/lista";
    }

    @GetMapping("/premios")
    public String competicionesPorPremio(Model model) {
        List<Competicion> competiciones = competicionServicio.obtenerOrdenadasPorPremio();
        model.addAttribute("competiciones", competiciones);
        model.addAttribute("titulo", "Competiciones Ordenadas por Premio");
        return "competiciones/lista";
    }

    // --- NUEVOS ENDPOINTS PARA MANEJAR CLUBES ---
    
    @GetMapping("/{id}/clubes")
    public String mostrarClubesDeCompeticion(@PathVariable Long id, Model model) {
        Optional<Competicion> competicion = competicionServicio.obtenerPorId(id);
        if (competicion.isPresent()) {
            List<Club> clubesEnCompeticion = competicionServicio.obtenerClubesDeCompeticion(id);
            List<Club> clubesDisponibles = clubServicio.obtenerTodos(); // Necesitas este método en ClubServicio
            
            model.addAttribute("competicion", competicion.get());
            model.addAttribute("clubes", clubesEnCompeticion);
            model.addAttribute("clubesDisponibles", clubesDisponibles);
            model.addAttribute("titulo", "Clubes en " + competicion.get().getNombre());
            return "competiciones/clubes";
        }
        return "redirect:/competiciones";
    }

    @PostMapping("/{id}/agregar-club")
    public String agregarClubACompeticion(@PathVariable Long id, @RequestParam Long clubId) {
        competicionServicio.agregarClubACompeticion(id, clubId);
        return "redirect:/competiciones/" + id + "/clubes";
    }

    @PostMapping("/{id}/remover-club")
    public String removerClubDeCompeticion(@PathVariable Long id, @RequestParam Long clubId) {
        competicionServicio.removerClubDeCompeticion(id, clubId);
        return "redirect:/competiciones/" + id + "/clubes";
    }
}