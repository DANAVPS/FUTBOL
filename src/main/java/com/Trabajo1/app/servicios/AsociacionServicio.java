package com.Trabajo1.app.servicios;

import com.Trabajo1.app.entidades.Asociacion;
import com.Trabajo1.app.entidades.Club;
import com.Trabajo1.app.repositorios.AsociacionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AsociacionServicio {
    
    @Autowired
    private AsociacionRepositorio asociacionRepositorio;
    
    // Crear o actualizar asociación
    public Asociacion guardar(Asociacion asociacion) {
        return asociacionRepositorio.save(asociacion);
    }
    
    // Obtener todas las asociaciones
    public List<Asociacion> obtenerTodas() {
        return asociacionRepositorio.findAll();
    }
    
    // Obtener asociación por ID
    public Optional<Asociacion> obtenerPorId(Long id) {
        return asociacionRepositorio.findById(id);
    }
    
    // Buscar por nombre
    public Optional<Asociacion> buscarPorNombre(String nombre) {
        return asociacionRepositorio.findByNombre(nombre);
    }
    
    // Buscar por país
    public List<Asociacion> buscarPorPais(String pais) {
        return asociacionRepositorio.findByPais(pais);
    }
    
    // Buscar por presidente
    public Optional<Asociacion> buscarPorPresidente(String presidente) {
        return asociacionRepositorio.findByPresidente(presidente);
    }
    
    // Buscar por nombre conteniendo
    public List<Asociacion> buscarPorNombreConteniendo(String palabra) {
        return asociacionRepositorio.findByNombreContaining(palabra);
    }
    
    // Eliminar asociación
    public void eliminar(Long id) {
        asociacionRepositorio.deleteById(id);
    }
    
    // Verificar si existe
    public boolean existe(Long id) {
        return asociacionRepositorio.existsById(id);
    }
    
    // Contar total de asociaciones
    public long contar() {
        return asociacionRepositorio.count();
    }
    
    // ✅ NUEVO: Obtener clubes de una asociación
    @Transactional(readOnly = true)
    public List<Club> obtenerClubesDeAsociacion(Long asociacionId) {
        Optional<Asociacion> asociacion = asociacionRepositorio.findById(asociacionId);
        if (asociacion.isPresent()) {
            // Forzar carga de la colección dentro de la transacción
            List<Club> clubes = asociacion.get().getClubes();
            clubes.size(); // Esto inicializa la colección lazy
            return clubes;
        }
        throw new RuntimeException("Asociación no encontrada con ID: " + asociacionId);
    }
    
    // ✅ NUEVO: Contar clubes de una asociación
    public Long contarClubesPorAsociacion(Long asociacionId) {
        return asociacionRepositorio.contarClubesPorAsociacion(asociacionId);
    }
}
