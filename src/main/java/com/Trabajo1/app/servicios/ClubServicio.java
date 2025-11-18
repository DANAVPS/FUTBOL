package com.Trabajo1.app.servicios;

import com.Trabajo1.app.entidades.*;
import com.Trabajo1.app.repositorios.ClubRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClubServicio {
    
    @Autowired
    private ClubRepositorio clubRepositorio;
    
    // ✅ MEJORADO: Crear o actualizar club con validación
    public Club guardar(Club club) {
        // Si el club tiene ID, verificar que existe
        if (club.getId() != null) {
            Optional<Club> existente = clubRepositorio.findById(club.getId());
            if (!existente.isPresent()) {
                throw new RuntimeException("Club con ID " + club.getId() + " no existe");
            }
        }
        
        // Verificar unicidad del nombre
        Optional<Club> clubConMismoNombre = clubRepositorio.findByNombre(club.getNombre());
        if (clubConMismoNombre.isPresent() && 
            !clubConMismoNombre.get().getId().equals(club.getId())) {
            throw new RuntimeException("Ya existe un club con el nombre: " + club.getNombre());
        }
        
        return clubRepositorio.save(club);
    }
    
    // ✅ NUEVO: Método para actualizar club existente sin problemas de merge
    @Transactional
    public Club actualizar(Long id, Club clubActualizado) {
        Club clubExistente = clubRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Club no encontrado"));
        
        // Actualizar solo campos básicos
        clubExistente.setNombre(clubActualizado.getNombre());
        clubExistente.setCiudad(clubActualizado.getCiudad());
        clubExistente.setAnioFundacion(clubActualizado.getAnioFundacion());
        
        // Las relaciones se manejan por separado
        if (clubActualizado.getEntrenador() != null) {
            clubExistente.setEntrenador(clubActualizado.getEntrenador());
        }
        
        if (clubActualizado.getAsociacion() != null) {
            clubExistente.setAsociacion(clubActualizado.getAsociacion());
        }
        
        return clubRepositorio.save(clubExistente);
    }
    
    // Obtener todos los clubes
    @Transactional(readOnly = true)
    public List<Club> obtenerTodos() {
        return clubRepositorio.findAll();
    }
    
    // Obtener club por ID
    @Transactional(readOnly = true)
    public Optional<Club> obtenerPorId(Long id) {
        return clubRepositorio.findById(id);
    }
    
    // Buscar por nombre
    @Transactional(readOnly = true)
    public Optional<Club> buscarPorNombre(String nombre) {
        return clubRepositorio.findByNombre(nombre);
    }
    
    // Buscar por ciudad
    @Transactional(readOnly = true)
    public List<Club> buscarPorCiudad(String ciudad) {
        return clubRepositorio.findByCiudad(ciudad);
    }
    
    // Buscar por año de fundación mayor a
    @Transactional(readOnly = true)
    public List<Club> buscarPorAnioFundacionMayorA(Integer anio) {
        return clubRepositorio.findByAnioFundacionGreaterThan(anio);
    }
    
    // Buscar clubes de una asociación
    @Transactional(readOnly = true)
    public List<Club> buscarPorAsociacion(Long asociacionId) {
        return clubRepositorio.findByAsociacionId(asociacionId);
    }
    
    // Buscar clubes en una competición
    @Transactional(readOnly = true)
    public List<Club> buscarPorCompeticion(Long competicionId) {
        return clubRepositorio.findByCompeticionId(competicionId);
    }
    
    // Obtener clubes con entrenador
    @Transactional(readOnly = true)
    public List<Club> obtenerClubesConEntrenador() {
        return clubRepositorio.findClubesConEntrenador();
    }
    
    // Contar jugadores de un club
    @Transactional(readOnly = true)
    public Long contarJugadores(Long clubId) {
        return clubRepositorio.contarJugadoresPorClub(clubId);
    }
    
    // Buscar por nombre conteniendo
    @Transactional(readOnly = true)
    public List<Club> buscarPorNombreConteniendo(String palabra) {
        return clubRepositorio.findByNombreContaining(palabra);
    }
    
    // Buscar clubes sin entrenador
    @Transactional(readOnly = true)
    public List<Club> buscarClubesSinEntrenador() {
        return clubRepositorio.findByEntrenadorIsNull();
    }
    
    // Buscar clubes sin asociación
    @Transactional(readOnly = true)
    public List<Club> buscarClubesSinAsociacion() {
        return clubRepositorio.findByAsociacionIsNull();
    }
    
    // Buscar clubes por país de asociación
    @Transactional(readOnly = true)
    public List<Club> buscarPorPaisAsociacion(String pais) {
        return clubRepositorio.findByAsociacionPais(pais);
    }
    
    // ✅ MEJORADO: Obtener club con relaciones cargadas
    @Transactional(readOnly = true)
    public Optional<Club> obtenerClubConRelaciones(Long id) {
        Optional<Club> club = clubRepositorio.findById(id);
        club.ifPresent(c -> {
            // Forzar carga de colecciones LAZY
            c.getJugadores().size();
            c.getCompeticiones().size();
        });
        return club;
    }
    
    // ✅ MEJORADO: Asignar entrenador a un club
    @Transactional
    public Club asignarEntrenador(Long clubId, Entrenador entrenador) {
        Club club = clubRepositorio.findById(clubId)
            .orElseThrow(() -> new RuntimeException("Club no encontrado"));
        
        club.setEntrenador(entrenador);
        return clubRepositorio.save(club);
    }
    
    // ✅ MEJORADO: Agregar jugador a un club
    @Transactional
    public Club agregarJugador(Long clubId, Jugador jugador) {
        Club club = clubRepositorio.findById(clubId)
            .orElseThrow(() -> new RuntimeException("Club no encontrado"));
        
        if (!club.getJugadores().contains(jugador)) {
            club.agregarJugador(jugador);
            return clubRepositorio.save(club);
        }
        
        return club;
    }
    
    // ✅ MEJORADO: Asignar asociación a un club
    @Transactional
    public Club asignarAsociacion(Long clubId, Asociacion asociacion) {
        Club club = clubRepositorio.findById(clubId)
            .orElseThrow(() -> new RuntimeException("Club no encontrado"));
        
        club.setAsociacion(asociacion);
        return clubRepositorio.save(club);
    }
    
    // Buscar por entrenador ID
    @Transactional(readOnly = true)
    public Optional<Club> buscarPorEntrenadorId(Long entrenadorId) {
        return clubRepositorio.findByEntrenadorId(entrenadorId);
    }
    
    // ✅ MEJORADO: Agregar competición a un club
    @Transactional
    public Club agregarCompeticion(Long clubId, Competicion competicion) {
        Club club = clubRepositorio.findById(clubId)
            .orElseThrow(() -> new RuntimeException("Club no encontrado"));
        
        if (!club.getCompeticiones().contains(competicion)) {
            club.getCompeticiones().add(competicion);
            return clubRepositorio.save(club);
        }
        
        return club;
    }
    
    // Eliminar club
    @Transactional
    public void eliminar(Long id) {
        if (!clubRepositorio.existsById(id)) {
            throw new RuntimeException("Club no encontrado");
        }
        clubRepositorio.deleteById(id);
    }
    
    // Verificar si existe
    @Transactional(readOnly = true)
    public boolean existe(Long id) {
        return clubRepositorio.existsById(id);
    }
    
    // Contar total de clubes
    @Transactional(readOnly = true)
    public long contar() {
        return clubRepositorio.count();
    }
}