package com.Trabajo1.app.servicios;

import com.Trabajo1.app.entidades.Jugador;
import com.Trabajo1.app.repositorios.JugadorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JugadorServicio {
    
    @Autowired
    private JugadorRepositorio jugadorRepositorio;
    
    // Crear o actualizar jugador
    public Jugador guardar(Jugador jugador) {
        return jugadorRepositorio.save(jugador);
    }
    
    // Obtener todos los jugadores
    public List<Jugador> obtenerTodos() {
        return jugadorRepositorio.findAll();
    }
    
    // Obtener jugador por ID
    public Optional<Jugador> obtenerPorId(Long id) {
        return jugadorRepositorio.findById(id);
    }
    
    // Buscar por número de camiseta
    public Optional<Jugador> buscarPorNumero(Integer numero) {
        return jugadorRepositorio.findByNumero(numero);
    }
    
    // Buscar por posición
    public List<Jugador> buscarPorPosicion(String posicion) {
        return jugadorRepositorio.findByPosicion(posicion);
    }
    
    // Buscar por nombre y apellido
    public Optional<Jugador> buscarPorNombreYApellido(String nombre, String apellido) {
        return jugadorRepositorio.findByNombreAndApellido(nombre, apellido);
    }
    
    // Buscar por apellido conteniendo
    public List<Jugador> buscarPorApellido(String apellido) {
        return jugadorRepositorio.findByApellidoContaining(apellido);
    }
    
    // ✅ CORREGIDO: Buscar jugadores por club
    public List<Jugador> buscarPorClub(Long clubId) {
        return jugadorRepositorio.findByClub(clubId);
    }
    
    // ✅ CORREGIDO: Buscar jugadores sin club
    public List<Jugador> buscarJugadoresSinClub() {
        return jugadorRepositorio.findByClubIsNull();
    }
    
    // ✅ CORREGIDO: Buscar por posición y club
    public List<Jugador> buscarPorPosicionYClub(String posicion, Long clubId) {
        return jugadorRepositorio.findByPosicionAndClub(posicion, clubId);
    }
    
    // Eliminar jugador
    public void eliminar(Long id) {
        jugadorRepositorio.deleteById(id);
    }
    
    // Verificar si existe
    public boolean existe(Long id) {
        return jugadorRepositorio.existsById(id);
    }
    
    public List<Jugador> obtenerPorId(List<Long> ids) {
        return jugadorRepositorio.findAllById(ids);
    }
    
    // Contar total de jugadores
    public long contar() {
        return jugadorRepositorio.count();
    }
    
    // ✅ CORREGIDO: Contar jugadores por club
    public Long contarPorClub(Long clubId) {
        return jugadorRepositorio.countByClub(clubId);
    }
}