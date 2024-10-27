package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Peregrino {
	private Long id;
	private String nombre;
	private String nacionalidad;
		
	private Carnet carnet;
	List<Parada> listaParadas = new ArrayList<>();
	List<Estancia> listaEstancias;
		

	public Peregrino() {
		
	}
		
	public Peregrino(Long id, String nombre, String nacionalidad, Carnet carnet,
			List<Parada> listaParadas, List<Estancia> listaEstancias) {
		this.id = id;
		this.nombre = nombre;
		this.nacionalidad = nacionalidad;
		this.carnet = carnet;
		this.listaParadas = listaParadas;
		this.listaEstancias = listaEstancias;
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


	@Override
	public String toString() {
		String salida = "";
		salida += "<< PEREGRINO >>"+"\nID: "+id+"\nNombre: "+nombre+"\nNacionalidad: "+nacionalidad+"\n"+"\n"+carnet.toString();
		
		if(listaParadas != null) {
			salida += "\n<< LISTA DE PARADAS >>";
			for(Parada indParada: listaParadas) {
				salida += "\n"+indParada.toString();
			}
		}
		
		if(listaEstancias != null) {
			salida += "\n<< LISTA DE ESTANCIAS >>";
			for(Estancia indEstancia: listaEstancias) {
				salida += "\n"+indEstancia.toString();
			}
		}
	
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