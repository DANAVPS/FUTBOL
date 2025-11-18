package com.Trabajo1.app.controladores;

import com.Trabajo1.app.entidades.Asociacion;
import com.Trabajo1.app.entidades.Club;
import com.Trabajo1.app.servicios.AsociacionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/asociaciones")
public class AsociacionControlador {

    @Autowired
    private AsociacionServicio asociacionServicio;

    @GetMapping
    public String listarAsociaciones(Model model) {
        List<Asociacion> asociaciones = asociacionServicio.obtenerTodas();
        model.addAttribute("asociaciones", asociaciones);
        model.addAttribute("titulo", "Lista de Asociaciones");
        return "asociaciones/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("asociacion", new Asociacion());
        model.addAttribute("titulo", "Nueva Asociación");
        return "asociaciones/formulario";
    }

    @PostMapping("/guardar")
    public String guardarAsociacion(@ModelAttribute Asociacion asociacion) {
        asociacionServicio.guardar(asociacion);
        return "redirect:/asociaciones";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Asociacion> asociacion = asociacionServicio.obtenerPorId(id);
        if (asociacion.isPresent()) {
            model.addAttribute("asociacion", asociacion.get());
            model.addAttribute("titulo", "Editar Asociación");
            return "asociaciones/formulario";
        }
        return "redirect:/asociaciones";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAsociacion(@PathVariable Long id) {
        asociacionServicio.eliminar(id);
        return "redirect:/asociaciones";
    }

    @GetMapping("/buscar")
    public String buscarAsociaciones(@RequestParam String palabra, Model model) {
        List<Asociacion> asociaciones = asociacionServicio.buscarPorNombreConteniendo(palabra);
        model.addAttribute("asociaciones", asociaciones);
        model.addAttribute("titulo", "Resultados de Búsqueda: " + palabra);
        return "asociaciones/lista";
    }

    @GetMapping("/{id}/clubes")
    public String mostrarClubesDeAsociacion(@PathVariable Long id, Model model) {
        Optional<Asociacion> asociacion = asociacionServicio.obtenerPorId(id);
        if (asociacion.isPresent()) {
            List<Club> clubes = asociacionServicio.obtenerClubesDeAsociacion(id);
            model.addAttribute("asociacion", asociacion.get());
            model.addAttribute("clubes", clubes);
            model.addAttribute("titulo", "Clubes de " + asociacion.get().getNombre());
            return "asociaciones/clubes";
        }
        return "redirect:/asociaciones";
    }
}