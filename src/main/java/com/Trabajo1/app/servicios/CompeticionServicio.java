package com.Trabajo1.app.servicios;

import com.Trabajo1.app.entidades.Competicion;
import com.Trabajo1.app.entidades.Club;
import com.Trabajo1.app.repositorios.CompeticionRepositorio;
import com.Trabajo1.app.repositorios.ClubRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompeticionServicio {
    
    @Autowired
    private CompeticionRepositorio competicionRepositorio;
    
    @Autowired
    private ClubRepositorio clubRepositorio;
    
    // ========== MÉTODOS PRINCIPALES ==========
    
    public Competicion guardar(Competicion competicion) {
        return competicionRepositorio.save(competicion);
    }
    public List<Competicion> obtenerPorIds(List<Long> ids) {
        return competicionRepositorio.findAllById(ids);
    }
    
    @Transactional(readOnly = true)
    public List<Competicion> obtenerTodas() {
        List<Competicion> competiciones = competicionRepositorio.findAll();
        // Forzar inicialización de clubes dentro de la transacción
        competiciones.forEach(competicion -> {
            // Esto fuerza a Hibernate a cargar la colección
            competicion.getClubes().size();
        });
        return competiciones;
    }
    
    
    @Transactional(readOnly = true)
    public Optional<Competicion> obtenerPorId(Long id) {
        Optional<Competicion> competicion = competicionRepositorio.findById(id);
        // Inicializar clubes si existe
        competicion.ifPresent(c -> c.getClubes().size());
        return competicion;
    }
    
    public Optional<Competicion> buscarPorNombre(String nombre) {
        return competicionRepositorio.findByNombre(nombre);
    }
    
    public List<Competicion> buscarPorMontoPremioMayorA(Integer monto) {
        return competicionRepositorio.findByMontoPremioGreaterThan(monto);
    }
    
    @Transactional(readOnly = true)
    public List<Competicion> obtenerCompeticionesActivas() {
        List<Competicion> competiciones = competicionRepositorio.findCompeticionesActivas(LocalDate.now());
        // Inicializar clubes
        competiciones.forEach(c -> c.getClubes().size());
        return competiciones;
    }
    
    public List<Competicion> buscarCompeticionesFuturas() {
        return competicionRepositorio.findByFechaInicioAfter(LocalDate.now());
    }
    
    @Transactional(readOnly = true)
    public List<Competicion> obtenerOrdenadasPorPremio() {
        List<Competicion> competiciones = competicionRepositorio.findAllByOrderByMontoPremioDesc();
        // Inicializar clubes
        competiciones.forEach(c -> c.getClubes().size());
        return competiciones;
    }
    
    public void eliminar(Long id) {
        competicionRepositorio.deleteById(id);
    }
    
    public boolean existe(Long id) {
        return competicionRepositorio.existsById(id);
    }
    
    public long contar() {
        return competicionRepositorio.count();
    }
    
    // ========== MÉTODOS PARA MANEJAR CLUBES ==========
    
    public void agregarClubACompeticion(Long competicionId, Long clubId) {
        Competicion competicion = competicionRepositorio.findById(competicionId)
            .orElseThrow(() -> new RuntimeException("Competición no encontrada"));
        Club club = clubRepositorio.findById(clubId)
            .orElseThrow(() -> new RuntimeException("Club no encontrado"));
        
        // Agregar a la relación (desde el lado del club que es el owner)
        if (!club.getCompeticiones().contains(competicion)) {
            club.getCompeticiones().add(competicion);
            clubRepositorio.save(club);
        }
    }
    
    public void removerClubDeCompeticion(Long competicionId, Long clubId) {
        Competicion competicion = competicionRepositorio.findById(competicionId)
            .orElseThrow(() -> new RuntimeException("Competición no encontrada"));
        Club club = clubRepositorio.findById(clubId)
            .orElseThrow(() -> new RuntimeException("Club no encontrado"));
        
        // Remover de la relación (desde el lado del club)
        club.getCompeticiones().remove(competicion);
        clubRepositorio.save(club);
    }
    
    @Transactional(readOnly = true)
    public List<Club> obtenerClubesDeCompeticion(Long competicionId) {
        Competicion competicion = competicionRepositorio.findById(competicionId)
            .orElseThrow(() -> new RuntimeException("Competición no encontrada"));
        // Forzar carga de clubes
        List<Club> clubes = competicion.getClubes();
        clubes.size(); // Inicializar la colección
        return clubes;
    }
    
    @Transactional(readOnly = true)
    public Integer calcularTotalPremios() {
        return competicionRepositorio.findAll().stream()
                .mapToInt(Competicion::getMontoPremio)
                .sum();
    }
}