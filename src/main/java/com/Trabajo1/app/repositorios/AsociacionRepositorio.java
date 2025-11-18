package com.Trabajo1.app.repositorios;

import com.Trabajo1.app.entidades.Asociacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsociacionRepositorio extends JpaRepository<Asociacion, Long> {
    
    // Métodos de consulta personalizados
    
    // Buscar asociación por nombre
    Optional<Asociacion> findByNombre(String nombre);
    
    // Buscar asociaciones por país
    List<Asociacion> findByPais(String pais);
    
    // Buscar asociación por presidente
    Optional<Asociacion> findByPresidente(String presidente);
    
    // Buscar asociaciones cuyo nombre contenga una palabra
    List<Asociacion> findByNombreContaining(String palabra);
    
    // ✅ NUEVO MÉTODO para contar clubes por asociación
    @Query("SELECT COUNT(c) FROM Club c WHERE c.asociacion.id = :asociacionId")
    Long contarClubesPorAsociacion(@Param("asociacionId") Long asociacionId);
    
    // ✅ NUEVOS MÉTODOS ADICIONALES
    
    // Buscar asociaciones con más de X clubes
    @Query("SELECT a FROM Asociacion a WHERE SIZE(a.clubes) > :minClubes")
    List<Asociacion> findByCantidadClubesMayorA(@Param("minClubes") int minClubes);
    
    // Buscar asociaciones por país conteniendo
    List<Asociacion> findByPaisContaining(String pais);
    
    // Contar total de clubes por país de asociación
    @Query("SELECT a.pais, COUNT(c) FROM Asociacion a LEFT JOIN a.clubes c GROUP BY a.pais")
    List<Object[]> contarClubesPorPaisAsociacion();
}