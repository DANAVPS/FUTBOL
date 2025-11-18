package com.Trabajo1.app.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "competiciones")
public class Competicion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String nombre;
    
    @Column(nullable = false)
    private Integer montoPremio;
    
    @Column(nullable = false)
    private LocalDate fechaInicio;
    
    @Column(nullable = false)
    private LocalDate fechaFin;
    
    // RELACIÓN ManyToMany con Club (Página 22 del PDF)
    // mappedBy = "competiciones" significa que Club es el dueño de la relación
    // FetchType.LAZY para mejor rendimiento (Página 25 del PDF)
    @ManyToMany(mappedBy = "competiciones", fetch = FetchType.LAZY)
    private List<Club> clubes = new ArrayList<>();
    
    // Constructores
    public Competicion() {
    }
    
    // Constructor sin la lista de clubes (para creación inicial)
    public Competicion(String nombre, Integer montoPremio, LocalDate fechaInicio, LocalDate fechaFin) {
        this.nombre = nombre;
        this.montoPremio = montoPremio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    
    // Constructor completo
    public Competicion(Long id, String nombre, Integer montoPremio, LocalDate fechaInicio, LocalDate fechaFin) {
        this.id = id;
        this.nombre = nombre;
        this.montoPremio = montoPremio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Integer getMontoPremio() {
        return montoPremio;
    }
    
    public void setMontoPremio(Integer montoPremio) {
        this.montoPremio = montoPremio;
    }
    
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public List<Club> getClubes() {
        return clubes;
    }
    
    public void setClubes(List<Club> clubes) {
        this.clubes = clubes;
    }
    
    // Método helper para agregar club
    public void agregarClub(Club club) {
        this.clubes.add(club);
    }
    
    // Método helper para remover club
    public void removerClub(Club club) {
        this.clubes.remove(club);
    }
    
    // toString para debugging
    @Override
    public String toString() {
        return "Competicion{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", montoPremio=" + montoPremio +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", numeroClubes=" + (clubes != null ? clubes.size() : 0) +
                '}';
    }
}