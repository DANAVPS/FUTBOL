package com.Trabajo1.app.repositorios;


import com.Trabajo1.app.entidades.Competicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompeticionRepositorio extends JpaRepository<Competicion, Long> {
    
    // Métodos de consulta personalizados
    
    // Buscar competición por nombre
    Optional<Competicion> findByNombre(String nombre);
    
    // Buscar competiciones con monto de premio mayor a
    List<Competicion> findByMontoPremioGreaterThan(Integer monto);
    
    // Buscar competiciones que están activas (fecha actual entre inicio y fin)
    @Query("SELECT c FROM Competicion c WHERE :fecha BETWEEN c.fechaInicio AND c.fechaFin")
    List<Competicion> findCompeticionesActivas(LocalDate fecha);
    
    // Buscar competiciones que inicien después de una fecha
    List<Competicion> findByFechaInicioAfter(LocalDate fecha);
    
    // Buscar competiciones ordenadas por monto de premio descendente
    List<Competicion> findAllByOrderByMontoPremioDesc();
}