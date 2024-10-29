package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Carnet implements Serializable {
	private Long id;
	private LocalDate fechaexp = LocalDate.now();
	private double distancia = 0.0;
	private int nvips = 0;
	
	private Parada paradaInicial = new Parada();
		

	public Carnet() {
			
	}

	public Carnet(Long id, LocalDate fechaexp, double distancia, int nvips,	Parada paradaInicial) {
		this.id = id;
		this.fechaexp = fechaexp;
		this.distancia = distancia;
		this.nvips = nvips;
		this.paradaInicial = paradaInicial;
	}

			
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaexp() {
		return fechaexp;
	}

	public void setFechaexp(LocalDate fechaexp) {
		this.fechaexp = fechaexp;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	public int getNvips() {
		return nvips;
	}

	public void setNvips(int nvips) {
		this.nvips = nvips;
	}

	public Parada getParadaInicial() {
		return paradaInicial;
	}

	public void setParadaInicial(Parada paradaInicial) {
		this.paradaInicial = paradaInicial;
	}

		
	@Override
	public String toString() {
		String salida = "";
		salida += "<< CARNET >>"+"\nID: "+id+"\nFecha de expedición: "+fechaexp+"\nDistancia recorrida: "+
		distancia+"km"+"\nNúmero de estancias V.I.P.: "+nvips+"\nParada inicial: \n"+paradaInicial.toString();
	
		return salida;
	}

	@Override
	public int hashCode() {
		return Objects.hash(distancia, fechaexp, id, nvips, paradaInicial);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Carnet other = (Carnet) obj;
		return Double.doubleToLongBits(distancia) == Double.doubleToLongBits(other.distancia)
				&& Objects.equals(fechaexp, other.fechaexp) && Objects.equals(id, other.id) && nvips == other.nvips
				&& Objects.equals(paradaInicial, other.paradaInicial);
	}
}