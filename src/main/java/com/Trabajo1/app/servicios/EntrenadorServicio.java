package com.Trabajo1.app.servicios;

import com.Trabajo1.app.entidades.Club; // Importación asumida
import com.Trabajo1.app.entidades.Entrenador;
import com.Trabajo1.app.repositorios.EntrenadorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de la entidad Entrenador.
 * Utiliza @Transactional a nivel de clase para transacciones de solo lectura por defecto,
 * y se anota explícitamente en los métodos de escritura (ej. guardar, asignarClub).
 */
@Service
@Transactional(readOnly = true) // Transacciones de solo lectura por defecto
public class EntrenadorServicio {

    @Autowired
    private EntrenadorRepositorio entrenadorRepositorio;

    // Asumimos que ClubServicio es necesario para manejar la bidireccionalidad correctamente
    @Autowired
    private ClubServicio clubServicio;

    /**
     * Guarda o actualiza un Entrenador y maneja la bidireccionalidad con Club.
     * @param entrenador La entidad Entrenador a guardar.
     * @return El Entrenador guardado.
     */
    @Transactional // Sobreescribe readOnly = true
    public Entrenador guardar(Entrenador entrenador) {
        // Si el entrenador tiene un club, asegura la bidireccionalidad.
        if (entrenador.getClub() != null) {
            Club club = entrenador.getClub();
            // Asigna el entrenador al club.
            club.setEntrenador(entrenador);
            // Nota: Es posible que no necesitemos guardar el club aquí si ClubServicio ya
            // maneja su persistencia o si la relación está correctamente mapeada.
            // Dependerá de las reglas de Cascade y el diseño de la entidad Club/Servicio.
            // Para simplificar, confiamos en que guardar el Entrenador persista la relación.
        }
        
        return entrenadorRepositorio.save(entrenador);
    }

    /**
     * Asigna un Club a un Entrenador específico, manejando la desasignación del club anterior
     * y la bidireccionalidad.
     * @param entrenadorId ID del entrenador.
     * @param club El Club a asignar (puede ser null para desasignar).
     * @return El Entrenador actualizado o null si no se encuentra.
     */
    @Transactional // Sobreescribe readOnly = true
    public Entrenador asignarClub(Long entrenadorId, Club club) {
        Optional<Entrenador> entrenadorOpt = entrenadorRepositorio.findById(entrenadorId);
        
        if (entrenadorOpt.isPresent()) {
            Entrenador entrenador = entrenadorOpt.get();
            
            // 1. Desasignar club anterior si existe
            if (entrenador.getClub() != null) {
                Club clubAnterior = entrenador.getClub();
                clubAnterior.setEntrenador(null);
                // Guardamos explícitamente el club anterior para reflejar la desasignación
                clubServicio.guardar(clubAnterior); 
            }
            
            // 2. Asignar nuevo club
            entrenador.setClub(club);
            if (club != null) {
                club.setEntrenador(entrenador);
                // Guardamos explícitamente el nuevo club para reflejar la asignación
                clubServicio.guardar(club);
            }
            
            // 3. Guardar el entrenador actualizado
            return entrenadorRepositorio.save(entrenador);
        }
        
        return null;
    }

    public List<Entrenador> obtenerTodos() {
        return entrenadorRepositorio.findAll();
    }

    public Optional<Entrenador> obtenerPorId(Long id) {
        return entrenadorRepositorio.findById(id);
    }

    public Optional<Entrenador> buscarPorNombre(String nombre) {
        // Se asume que este método existe en el repositorio
        return entrenadorRepositorio.findByNombre(nombre); 
    }

    public List<Entrenador> buscarPorNacionalidad(String nacionalidad) {
        // Se asume que este método existe en el repositorio
        return entrenadorRepositorio.findByNacionalidad(nacionalidad);
    }

    public Optional<Entrenador> buscarPorNombreYApellido(String nombre, String apellido) {
        // Se asume que este método existe en el repositorio
        return entrenadorRepositorio.findByNombreAndApellido(nombre, apellido);
    }

    public List<Entrenador> buscarPorEdadMayorA(Integer edad) {
        // Se asume que este método existe en el repositorio
        return entrenadorRepositorio.findByEdadGreaterThan(edad);
    }

    public List<Entrenador> buscarEntrenadoresSinClub() {
        // Se asume que este método existe en el repositorio
        return entrenadorRepositorio.findByClubIsNull();
    }

    public Optional<Entrenador> buscarPorClubId(Long clubId) {
        // Se asume que este método existe en el repositorio
        return entrenadorRepositorio.findByClubId(clubId);
    }

    public List<Entrenador> buscarPorNacionalidadYSinClub(String nacionalidad) {
        // Se asume que este método existe en el repositorio
        return entrenadorRepositorio.findByNacionalidadAndClubIsNull(nacionalidad);
    }

    @Transactional
    public void eliminar(Long id) {
        entrenadorRepositorio.deleteById(id);
    }

    public boolean existe(Long id) {
        return entrenadorRepositorio.existsById(id);
    }

    public long contar() {
        return entrenadorRepositorio.count();
    }

    public Long contarEntrenadoresSinClub() {
        // Se asume que este método existe en el repositorio
        return entrenadorRepositorio.countByClubIsNull();
    }
}