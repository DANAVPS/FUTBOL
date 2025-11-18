package com.Trabajo1.app.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "entrenadores")
public class Entrenador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(nullable = false, length = 100)
	private String apellido;

	@Column(nullable = false)
	private Integer edad;

	@Column(nullable = false, length = 100)
	private String nacionalidad;

	@OneToOne(mappedBy = "entrenador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Club club;

	public Entrenador() {
	}

	public Entrenador(Long id, String nombre, String apellido, Integer edad, String nacionalidad) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.nacionalidad = nacionalidad;
	}

	// Getters y Setters ...

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

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	// ✅ NUEVO Getter y Setter para club
	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}
}