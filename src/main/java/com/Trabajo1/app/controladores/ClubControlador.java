package com.Trabajo1.app.controladores;

import com.Trabajo1.app.entidades.*;
import com.Trabajo1.app.servicios.*; // ✅ Import correcto
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clubes")
public class ClubControlador {

    @Autowired // ✅ Usa @Autowired en lugar de constructor
    private JugadorServicio jugadorServicio;

    @Autowired
    private ClubServicio clubServicio;

    @Autowired
    private AsociacionServicio asociacionServicio;

    @Autowired
    private EntrenadorServicio entrenadorServicio;

    @Autowired
    private CompeticionServicio competicionServicio;

    // ✅ Elimina el constructor que causa problemas

    @GetMapping
    public String listarClubes(Model model) {
        List<Club> clubes = clubServicio.obtenerTodos();
        model.addAttribute("clubes", clubes);
        model.addAttribute("titulo", "Lista de Clubes");
        return "clubes/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        Club club = new Club();
        club.setJugadores(new ArrayList<>());
        club.setCompeticiones(new ArrayList<>());
        
        model.addAttribute("club", club);
        model.addAttribute("asociaciones", asociacionServicio.obtenerTodas());
        model.addAttribute("entrenadores", entrenadorServicio.obtenerTodos());
        model.addAttribute("competiciones", competicionServicio.obtenerTodas());
        model.addAttribute("titulo", "Nuevo Club");
        return "clubes/formulario";
    }

    @PostMapping("/guardar")
    public String guardarClub(@ModelAttribute Club club,
                             @RequestParam(required = false) Long entrenadorId,
                             @RequestParam(required = false) Long asociacionId,
                             @RequestParam(required = false) List<Long> competicionesIds) {
        
        try {
            // ✅ SOLUCIÓN 1: Verificar si el nombre ya existe (para INSERT)
            if (club.getId() == null) { // Es un nuevo club
                Optional<Club> clubExistente = clubServicio.buscarPorNombre(club.getNombre());
                if (clubExistente.isPresent()) {
                    throw new RuntimeException("Ya existe un club con el nombre: " + club.getNombre());
                }
            } else { // Es una actualización
                // Verificar que no haya otro club con el mismo nombre
                Optional<Club> clubExistente = clubServicio.buscarPorNombre(club.getNombre());
                if (clubExistente.isPresent() && !clubExistente.get().getId().equals(club.getId())) {
                    throw new RuntimeException("Ya existe otro club con el nombre: " + club.getNombre());
                }
            }
            
            // ✅ SOLUCIÓN 2: Si es edición, obtener el club existente de la BD
            Club clubAGuardar;
            if (club.getId() != null) {
                // Obtener club existente y actualizar sus campos
                clubAGuardar = clubServicio.obtenerPorId(club.getId())
                    .orElseThrow(() -> new RuntimeException("Club no encontrado"));
                
                // Actualizar campos básicos
                clubAGuardar.setNombre(club.getNombre());
                clubAGuardar.setCiudad(club.getCiudad());
                clubAGuardar.setAnioFundacion(club.getAnioFundacion());
            } else {
                // Es un nuevo club
                clubAGuardar = club;
            }
            
            // Manejar entrenador
            if (entrenadorId != null) {
                Entrenador entrenador = entrenadorServicio.obtenerPorId(entrenadorId)
                    .orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));
                
                // Verificar si el entrenador ya está en otro club
                Optional<Club> clubConEsteEntrenador = clubServicio.buscarPorEntrenadorId(entrenadorId);
                if (clubConEsteEntrenador.isPresent() && 
                    !clubConEsteEntrenador.get().getId().equals(clubAGuardar.getId())) {
                    throw new RuntimeException("Este entrenador ya está asignado al club: " + 
                        clubConEsteEntrenador.get().getNombre());
                }
                
                clubAGuardar.setEntrenador(entrenador);
            } else {
                clubAGuardar.setEntrenador(null);
            }
            
            // ✅ MANEJAR ASOCIACIÓN (obtener entidad managed)
            if (asociacionId != null) {
                Asociacion asociacion = asociacionServicio.obtenerPorId(asociacionId)
                    .orElseThrow(() -> new RuntimeException("Asociación no encontrada"));
                clubAGuardar.setAsociacion(asociacion);
            } else {
                clubAGuardar.setAsociacion(null);
            }
            
            // ✅ MANEJAR COMPETICIONES (obtener entidades managed)
            if (competicionesIds != null && !competicionesIds.isEmpty()) {
                List<Competicion> competicionesManaged = competicionServicio.obtenerPorIds(competicionesIds);
                clubAGuardar.setCompeticiones(competicionesManaged);
            } else {
                clubAGuardar.setCompeticiones(new ArrayList<>());
            }
            
            clubServicio.guardar(clubAGuardar);
            return "redirect:/clubes";
            
        } catch (RuntimeException e) {
            // Manejar errores y mostrar mensaje al usuario
            // Aquí deberías agregar el manejo de errores apropiado
            throw e; // Por ahora re-lanzamos la excepción
        }
    }

    @PostMapping("/{clubId}/jugadores/asignar")
    public String asignarJugadoresAClub(@PathVariable Long clubId, 
                                      @RequestParam List<Long> jugadoresIds) {
        
        // ✅ SOLUCIÓN: Usar el servicio que maneja la transacción correctamente
        Optional<Club> clubOpt = clubServicio.obtenerPorId(clubId);
        
        if (clubOpt.isPresent() && jugadoresIds != null && !jugadoresIds.isEmpty()) {
            Club club = clubOpt.get();
            
            // Obtener jugadores managed de la base de datos
            for (Long jugadorId : jugadoresIds) {
                Optional<Jugador> jugadorOpt = jugadorServicio.obtenerPorId(jugadorId);
                if (jugadorOpt.isPresent()) {
                    Jugador jugador = jugadorOpt.get();
                    // Solo agregar si no está ya en la lista
                    if (!club.getJugadores().contains(jugador)) {
                        club.agregarJugador(jugador);
                    }
                }
            }
            
            clubServicio.guardar(club);
        }
        
        return "redirect:/clubes/detalle/" + clubId;
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Club> club = clubServicio.obtenerPorId(id);
        if (club.isPresent()) {
            model.addAttribute("club", club.get());
            model.addAttribute("asociaciones", asociacionServicio.obtenerTodas());
            model.addAttribute("entrenadores", entrenadorServicio.obtenerTodos());
            model.addAttribute("competiciones", competicionServicio.obtenerTodas());
            model.addAttribute("titulo", "Editar Club");
            return "clubes/formulario";
        }
        return "redirect:/clubes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarClub(@PathVariable Long id) {
        clubServicio.eliminar(id);
        return "redirect:/clubes";
    }

//    @GetMapping("/detalle/{id}")
//    public String verDetalle(@PathVariable Long id, Model model) {
//        Optional<Club> club = clubServicio.obtenerPorId(id);
//        if (club.isPresent()) {
//            Long totalJugadores = clubServicio.contarJugadores(id);
//            model.addAttribute("club", club.get());
//            model.addAttribute("totalJugadores", totalJugadores);
//            model.addAttribute("titulo", "Detalle del Club: " + club.get().getNombre());
//            return "clubes/detalle";
//        }
//        return "redirect:/clubes";
//    }
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Optional<Club> club = clubServicio.obtenerClubConRelaciones(id);
        if (club.isPresent()) {
            // ✅ CORREGIDO: Usar buscarPorClub en lugar de buscarPorClubId
            List<Jugador> jugadoresDelClub = jugadorServicio.buscarPorClub(id);
            
            model.addAttribute("club", club.get());
            model.addAttribute("totalJugadores", jugadoresDelClub.size());
            model.addAttribute("titulo", "Detalle del Club: " + club.get().getNombre());
            return "clubes/detalle";
        }
        return "redirect:/clubes";
    }

    @GetMapping("/buscar")
    public String buscarClubes(@RequestParam String palabra, Model model) {
        List<Club> clubes = clubServicio.buscarPorNombreConteniendo(palabra);
        model.addAttribute("clubes", clubes);
        model.addAttribute("titulo", "Resultados de Búsqueda: " + palabra);
        return "clubes/lista";
    }
    
//    @PostMapping("/{clubId}/jugadores/asignar")
//    public String asignarJugadorAClub(@PathVariable Long clubId, 
//                                      @RequestParam Long jugadorId) {
//        Optional<Club> clubOpt = clubServicio.obtenerPorId(clubId);
//        Optional<Jugador> jugadorOpt = jugadorServicio.obtenerPorId(jugadorId); // ✅ Corregido: jugadorServicio (minúscula)
//        
//        if (clubOpt.isPresent() && jugadorOpt.isPresent()) {
//            Club club = clubOpt.get();
//            Jugador jugador = jugadorOpt.get();
//            
//            club.agregarJugador(jugador);
//            clubServicio.guardar(club);
//        }
//        
//        return "redirect:/clubes/detalle/" + clubId;
//    }
    @GetMapping("/{id}/agregar-jugadores")
    public String mostrarFormularioAgregarJugadores(@PathVariable Long id, Model model) {
        Optional<Club> club = clubServicio.obtenerPorId(id);
        if (club.isPresent()) {
            // Obtener jugadores sin club
            List<Jugador> jugadoresSinClub = jugadorServicio.buscarJugadoresSinClub();
            // Obtener todos los jugadores para asignar
            List<Jugador> todosJugadores = jugadorServicio.obtenerTodos();
            
            model.addAttribute("club", club.get());
            model.addAttribute("jugadoresSinClub", jugadoresSinClub);
            model.addAttribute("todosJugadores", todosJugadores);
            model.addAttribute("titulo", "Agregar Jugadores al Club");
            return "clubes/agregar-jugadores";
        }
        return "redirect:/clubes";
    }

//    @PostMapping("/{clubId}/jugadores/asignar")
//    public String asignarJugadoresAClub(@PathVariable Long clubId, 
//                                      @RequestParam List<Long> jugadoresIds) {
//        Optional<Club> clubOpt = clubServicio.obtenerPorId(clubId);
//        
//        if (clubOpt.isPresent() && jugadoresIds != null && !jugadoresIds.isEmpty()) {
//            Club club = clubOpt.get();
//            List<Jugador> jugadores = jugadorServicio.obtenerPorId(jugadoresIds);
//            
//            for (Jugador jugador : jugadores) {
//                club.agregarJugador(jugador);
//            }
//            
//            clubServicio.guardar(club);
//        }
//        
//        return "redirect:/clubes/detalle/" + clubId;
//    }
}