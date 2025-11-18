package com.Trabajo1.app.entidades;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clubes")
public class Club {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 200, unique = true)
	private String nombre;

	@Column(length = 100)
	private String ciudad;

	@Column(name = "anio_fundacion")
	private Integer anioFundacion;

	// ✅ CORRECTO: OneToOne con Entrenador (EAGER según PDF página 27)
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "entrenador_id")
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Entrenador entrenador;

	// ✅ CORRECTO: OneToMany con Jugadores (LAZY por defecto - correcto)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_club") // ✅ Esto evita la tabla intermedia (página 13-14 del PDF)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Jugador> jugadores = new ArrayList<>();

	// ✅ CORRECTO: ManyToOne con Asociación (EAGER según PDF página 27)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "asociacion_id", referencedColumnName = "id")
	private Asociacion asociacion;

	// ✅ CORRECTO: ManyToMany con Competiciones (LAZY por defecto - correcto)
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "clubes_competiciones", joinColumns = @JoinColumn(name = "club_id"), inverseJoinColumns = @JoinColumn(name = "competicion_id"))
	private List<Competicion> competiciones = new ArrayList<>();

	// Constructores
	public Club() {
		this.jugadores = new ArrayList<>();
		this.competiciones = new ArrayList<>();
	}

	public Club(Long id, String nombre, String ciudad, Integer anioFundacion) {
		this.id = id;
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.anioFundacion = anioFundacion;
		this.jugadores = new ArrayList<>();
		this.competiciones = new ArrayList<>();
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

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public Integer getAnioFundacion() {
		return anioFundacion;
	}

	public void setAnioFundacion(Integer anioFundacion) {
		this.anioFundacion = anioFundacion;
	}

	public Entrenador getEntrenador() {
		return entrenador;
	}

	public void setEntrenador(Entrenador entrenador) {
		this.entrenador = entrenador;
	}

	public List<Jugador> getJugadores() {
		return jugadores;
	}

	public void setJugadores(List<Jugador> jugadores) {
		this.jugadores = jugadores;
	}

	public Asociacion getAsociacion() {
		return asociacion;
	}

	public void setAsociacion(Asociacion asociacion) {
		this.asociacion = asociacion;
	}

	public List<Competicion> getCompeticiones() {
		return competiciones;
	}

	public void setCompeticiones(List<Competicion> competiciones) {
		this.competiciones = competiciones;
	}

	// ✅ MÉTODOS HELPER para manejar relaciones
	public void agregarJugador(Jugador jugador) {
        if (jugadores == null) {
            jugadores = new ArrayList<>();
        }
        jugadores.add(jugador);
        // No establecemos club en jugador porque usamos unidireccional
    }

    public void removerJugador(Jugador jugador) {
        if (jugadores != null) {
            jugadores.remove(jugador);
        }
    }

	public void agregarCompeticion(Competicion competicion) {
		this.competiciones.add(competicion);
		competicion.getClubes().add(this);
	}

	public void removerCompeticion(Competicion competicion) {
		this.competiciones.remove(competicion);
		competicion.getClubes().remove(this);
	}
}