package com.Trabajo1.app.entidades;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "asociaciones")
public class Asociacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String nombre;
    
    @Column(nullable = false, length = 100)
    private String pais;
    
    @Column(nullable = false, length = 150)
    private String presidente;
    
    // ✅ RELACIÓN FALTANTE: OneToMany con Clubes
    @OneToMany(mappedBy = "asociacion", fetch = FetchType.LAZY)
    private List<Club> clubes = new ArrayList<>();
    
    // Constructores
    public Asociacion() {
    }
    
    public Asociacion(Long id, String nombre, String pais, String presidente) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.presidente = presidente;
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
    
    public String getPais() {
        return pais;
    }
    
    public void setPais(String pais) {
        this.pais = pais;
    }
    
    public String getPresidente() {
        return presidente;
    }
    
    public void setPresidente(String presidente) {
        this.presidente = presidente;
    }
    
    // ✅ NUEVO Getter y Setter para clubes
    public List<Club> getClubes() {
        return clubes;
    }
    
    public void setClubes(List<Club> clubes) {
        this.clubes = clubes;
    }
}