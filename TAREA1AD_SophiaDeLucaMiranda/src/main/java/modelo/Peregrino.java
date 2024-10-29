package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Peregrino implements Serializable { //Implementa serializable para poder trabajar con ficheros
	//Atributos de la clase:
	private Long id;
	private String nombre;
	private String nacionalidad;
	
	//Representa las relaciones entre Peregrino y las demás clases del diagrama UML:
	private Carnet carnet;
	List<Parada> listaParadas;
	List<Estancia> listaEstancias;
		
	//Constructores de la clase:
	public Peregrino() {
		
	}
		
	public Peregrino(Long id, String nombre, String nacionalidad, Carnet carnet) {
		this.id = id;
		this.nombre = nombre;
		this.nacionalidad = nacionalidad;
		this.carnet = carnet;
	} //Se ha evitado pasar las listas como parámetros para facilitar su uso
		
	//Métodos Getter y Setter de cada atributo:	
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

	//Método toString que muestra la información de la clase:
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

	//Método equals que sirve para comparar dos objetos de la misma clase mediante sus atributos:
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