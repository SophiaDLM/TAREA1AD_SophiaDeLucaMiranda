package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Estancia implements Serializable { //Implementa serializable para poder trabajar con ficheros en la próxima entrega
	//Atributos de la clase:
	private Long id;
	private LocalDate fecha;
	private boolean vip = false;

	//Representa la relación entre Estancia y Parada del diagrama UML:
	private Parada parada;
	
	//Constructores de la clase:
	public Estancia() {
		
	}

	public Estancia(Long id, LocalDate fecha, boolean vip, Parada parada) {
		this.id = id;
		this.fecha = fecha;
		this.vip = vip;
		this.parada = parada;
	}
		
	//Métodos Getter y Setter de cada atributo:
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public Parada getParada() {
		return parada;
	}

	public void setParada(Parada parada) {
		this.parada = parada;
	}

	//Método toString que muestra la información de la clase:
	@Override
	public String toString() {
		String salida = "";
		salida += "<< ESTANCIA >>"+"\nID: "+id+"\nFecha: "+fecha+"\n¿Es V.I.P.? - "+vip+"\nParada en la que se realizó: \n"+parada;

		return salida;
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(fecha, id, parada, vip);
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
		Estancia other = (Estancia) obj;
		return Objects.equals(fecha, other.fecha) && Objects.equals(id, other.id)
				&& Objects.equals(parada, other.parada) && vip == other.vip;
	}
}