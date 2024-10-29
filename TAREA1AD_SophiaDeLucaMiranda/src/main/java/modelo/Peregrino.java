package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Peregrino implements Serializable {
	private Long id;
	private String nombre;
	private String nacionalidad;
		
	private Carnet carnet;
	List<Parada> listaParadas;
	List<Estancia> listaEstancias;
		

	public Peregrino() {
		
	}
		
	public Peregrino(Long id, String nombre, String nacionalidad, Carnet carnet) {
		this.id = id;
		this.nombre = nombre;
		this.nacionalidad = nacionalidad;
		this.carnet = carnet;
	}
		
		

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

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public Carnet getCarnet() {
		return carnet;
	}

	public void setCarnet(Carnet carnet) {
		this.carnet = carnet;
	}

	public List<Parada> getListaParadas() {
		return listaParadas;
	}

	public void setListaParadas(List<Parada> listaParadas) {
		this.listaParadas = listaParadas;
	}

	public List<Estancia> getListaEstancias() {
		return listaEstancias;
	}

	public void setListaEstancias(List<Estancia> listaEstancias) {
		this.listaEstancias = listaEstancias;
	}

	@Override
	public String toString() {
		String salida = "";
		salida += "<< PEREGRINO >>"+"\nID: "+id+"\nNombre: "+nombre+"\nNacionalidad: "+nacionalidad+
				"\nFecha de expedición del carnet: "+carnet.getFechaexp()+"\nNombre de la parada inicial: "+carnet.getParadaInicial().getNombre()+
				"\nRegión de la parada inicial: "+carnet.getParadaInicial().getRegion();
	
		return salida;
	}

	@Override
	public int hashCode() {
		return Objects.hash(carnet, id, listaEstancias, listaParadas, nacionalidad, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Peregrino other = (Peregrino) obj;
		return Objects.equals(carnet, other.carnet) && Objects.equals(id, other.id)
				&& Objects.equals(listaEstancias, other.listaEstancias)
				&& Objects.equals(listaParadas, other.listaParadas) && Objects.equals(nacionalidad, other.nacionalidad)
				&& Objects.equals(nombre, other.nombre);
	}
}