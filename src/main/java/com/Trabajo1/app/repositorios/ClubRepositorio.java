package com.Trabajo1.app.repositorios;

import com.Trabajo1.app.entidades.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepositorio extends JpaRepository<Club, Long> {
    
    // Métodos de consulta personalizados
    
    // Buscar club por nombre
    Optional<Club> findByNombre(String nombre);
 // En ClubRepositorio.java
    Optional<Club> findByEntrenadorId(Long entrenadorId);
    
    // Buscar clubes por ciudad
    List<Club> findByCiudad(String ciudad);
    
    // Buscar clubes fundados después de un año
    List<Club> findByAnioFundacionGreaterThan(Integer anio);
    
    // Buscar clubes por asociación
    @Query("SELECT c FROM Club c WHERE c.asociacion.id = :asociacionId")
    List<Club> findByAsociacionId(@Param("asociacionId") Long asociacionId);
    
    // Buscar clubes que participan en una competición específica
    @Query("SELECT c FROM Club c JOIN c.competiciones comp WHERE comp.id = :competicionId")
    List<Club> findByCompeticionId(@Param("competicionId") Long competicionId);
    
    // Buscar clubes con entrenador
    @Query("SELECT c FROM Club c WHERE c.entrenador IS NOT NULL")
    List<Club> findClubesConEntrenador();
    
    // Contar jugadores de un club
    @Query("SELECT COUNT(j) FROM Club c JOIN c.jugadores j WHERE c.id = :clubId")
    Long contarJugadoresPorClub(@Param("clubId") Long clubId);
    
    // Buscar clubes por nombre conteniendo
    List<Club> findByNombreContaining(String palabra);
    
    // ✅ NUEVOS MÉTODOS para relaciones actualizadas
    
    // Buscar clubes sin entrenador
    List<Club> findByEntrenadorIsNull();
    
    // Buscar clubes sin asociación
    List<Club> findByAsociacionIsNull();
    
    
    
    // Buscar clubes por país de asociación
    @Query("SELECT c FROM Club c WHERE c.asociacion.pais = :pais")
    List<Club> findByAsociacionPais(@Param("pais") String pais);
    
    // Buscar clubes por nacionalidad del entrenador
    @Query("SELECT c FROM Club c WHERE c.entrenador.nacionalidad = :nacionalidad")
    List<Club> findByEntrenadorNacionalidad(@Param("nacionalidad") String nacionalidad);
}