package com.Trabajo1.app.controladores;

import com.Trabajo1.app.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MainControlador {

    @Autowired
    private AsociacionServicio asociacionServicio;
    
    @Autowired
    private ClubServicio clubServicio;
    
    @Autowired
    private JugadorServicio jugadorServicio;
    
    @Autowired
    private EntrenadorServicio entrenadorServicio;
    
    @Autowired
    private CompeticionServicio competicionServicio;

    @GetMapping("/")
    public String index(Model model) {
        // Estadísticas para el dashboard
        Map<String, Long> estadisticas = new HashMap<>();
        estadisticas.put("asociaciones", asociacionServicio.contar());
        estadisticas.put("clubes", clubServicio.contar());
        estadisticas.put("jugadores", jugadorServicio.contar());
        estadisticas.put("entrenadores", entrenadorServicio.contar());
        estadisticas.put("competiciones", competicionServicio.contar());
        
        model.addAttribute("estadisticas", estadisticas);
        model.addAttribute("titulo", "Sistema de Gestión de Fútbol");
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("asociaciones", asociacionServicio.obtenerTodas());
        model.addAttribute("clubes", clubServicio.obtenerTodos());
        model.addAttribute("competicionesActivas", competicionServicio.obtenerCompeticionesActivas());
        model.addAttribute("competicionesPremio", competicionServicio.obtenerOrdenadasPorPremio());
        
        // Estadísticas para el dashboard
        Map<String, Long> estadisticas = new HashMap<>();
        estadisticas.put("asociaciones", asociacionServicio.contar());
        estadisticas.put("clubes", clubServicio.contar());
        estadisticas.put("jugadores", jugadorServicio.contar());
        estadisticas.put("entrenadores", entrenadorServicio.contar());
        estadisticas.put("competiciones", competicionServicio.contar());
        
        model.addAttribute("estadisticas", estadisticas);
        model.addAttribute("titulo", "Dashboard - Estadísticas");
        return "dashboard";
    }
}