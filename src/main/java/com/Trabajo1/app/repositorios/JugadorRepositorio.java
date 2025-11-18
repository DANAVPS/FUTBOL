package com.Trabajo1.app.repositorios;

import com.Trabajo1.app.entidades.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JugadorRepositorio extends JpaRepository<Jugador, Long> {
    
    // ✅ MÉTODOS CORREGIDOS - USANDO SOLO "club"
    @Query("SELECT j FROM Jugador j WHERE j.club.id = :clubId")
    List<Jugador> findByClub(@Param("clubId") Long clubId);
    
    List<Jugador> findByClubIsNull();
    
    @Query("SELECT j FROM Jugador j WHERE j.posicion = :posicion AND j.club.id = :clubId")
    List<Jugador> findByPosicionAndClub(@Param("posicion") String posicion, @Param("clubId") Long clubId);
    
    @Query("SELECT COUNT(j) FROM Jugador j WHERE j.club.id = :clubId")
    Long countByClub(@Param("clubId") Long clubId);
    
    
    // Buscar jugador por número de camiseta
    Optional<Jugador> findByNumero(Integer numero);
    
    // Buscar jugadores por posición
    List<Jugador> findByPosicion(String posicion);
    
    // Buscar jugador por nombre y apellido
    Optional<Jugador> findByNombreAndApellido(String nombre, String apellido);
    
    // Buscar jugadores por apellido
    List<Jugador> findByApellidoContaining(String apellido);
    
    // ✅ NUEVOS MÉTODOS ADICIONALES
    
    // Buscar jugadores por club y posición
    @Query("SELECT j FROM Jugador j WHERE j.club.id = :clubId AND j.posicion = :posicion")
    List<Jugador> buscarPorClubYPosicion(@Param("clubId") Long clubId, @Param("posicion") String posicion);
    
    // Contar jugadores por posición en un club
    @Query("SELECT COUNT(j) FROM Jugador j WHERE j.club.id = :clubId AND j.posicion = :posicion")
    Long contarPorPosicionYClub(@Param("clubId") Long clubId, @Param("posicion") String posicion);
    
    // Buscar jugadores por rango de número
    List<Jugador> findByNumeroBetween(Integer numeroInicio, Integer numeroFin);
}