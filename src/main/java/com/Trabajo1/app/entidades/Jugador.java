package com.Trabajo1.app.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "jugadores")
public class Jugador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, length = 100)
    private String apellido;
    
    @Column(nullable = false, unique = true)
    private Integer numero;
    
    @Column(nullable = false, length = 50)
    private String posicion;
    
    // ✅ AGREGAR ESTA RELACIÓN FALTANTE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_club") // Mismo nombre que en la base de datos
    private Club club;
    
    // Constructores
    public Jugador() {
    }
    
    public Jugador(Long id, String nombre, String apellido, Integer numero, String posicion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numero = numero;
        this.posicion = posicion;
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
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public Integer getNumero() {
        return numero;
    }
    
    public void setNumero(Integer numero) {
        this.numero = numero;
    }
    
    public String getPosicion() {
        return posicion;
    }
    
    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }
    
    // ✅ AGREGAR Getter y Setter para club
    public Club getClub() {
        return club;
    }
    
    public void setClub(Club club) {
        this.club = club;
    }
}