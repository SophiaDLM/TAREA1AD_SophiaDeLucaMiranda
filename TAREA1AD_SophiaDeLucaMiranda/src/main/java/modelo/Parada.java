package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Parada {
	private Long id;
	private String nombre;
	private char region;
	private String responsable;
		
	List<Peregrino> listaPeregrinos = new ArrayList<>();
			

	public Parada() {
		
	}

	public Parada(Long id, String nombre, char region, String responsable, List<Peregrino> listaPeregrinos) {
		this.id = id;
		this.nombre = nombre;
		this.region = region;
		this.responsable = responsable;
		this.listaPeregrinos = listaPeregrinos;
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

	public char getRegion() {
		return region;
	}

	public void setRegion(char region) {
		this.region = region;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public List<Peregrino> getListaPeregrinos() {
		return listaPeregrinos;
	}

	public void setListaPeregrinos(List<Peregrino> listaPeregrinos) {
		this.listaPeregrinos = listaPeregrinos;
	}

	
	@Override
	public String toString() {
		String salida ="";
		salida += "<< PARADA >>"+"\nID: "+id+"\nNombre: "+nombre+"\nRegión: "+region+"\nResponsable de la parada: "+responsable+"\n";
		
		if(listaPeregrinos != null) {
			salida += "<< LISTA DE PEREGRINOS >>";
			for(Peregrino indPeregrino: listaPeregrinos) {
				salida += indPeregrino.toString();
			}
		}
		
		return salida;
	}
		
	@Override
	public int hashCode() {
		return Objects.hash(id, listaPeregrinos, nombre, region, responsable);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parada other = (Parada) obj;
		return Objects.equals(id, other.id) && Objects.equals(listaPeregrinos, other.listaPeregrinos)
				&& Objects.equals(nombre, other.nombre) && region == other.region
				&& Objects.equals(responsable, other.responsable);
	}
}