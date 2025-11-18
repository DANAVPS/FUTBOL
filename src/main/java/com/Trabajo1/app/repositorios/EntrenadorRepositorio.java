package com.Trabajo1.app.repositorios;

import com.Trabajo1.app.entidades.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntrenadorRepositorio extends JpaRepository<Entrenador, Long> {

    Optional<Entrenador> findByNombre(String nombre);
    List<Entrenador> findByNacionalidad(String nacionalidad);
    List<Entrenador> findByEdadGreaterThan(Integer edad);
    Optional<Entrenador> findByNombreAndApellido(String nombre, String apellido);

    List<Entrenador> findByClubIsNull();

    @Query("SELECT e FROM Entrenador e WHERE e.club.id = :clubId")
    Optional<Entrenador> findByClubId(@Param("clubId") Long clubId);

    List<Entrenador> findByNacionalidadAndClubIsNull(String nacionalidad);
    Long countByClubIsNull();

    @Query("SELECT e FROM Entrenador e WHERE e.edad > :edad AND e.club IS NULL")
    List<Entrenador> findByEdadGreaterThanAndSinClub(@Param("edad") Integer edad);
}
