package vista;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.Carnet;
import modelo.Estancia;
import modelo.Parada;
import modelo.Peregrino;

public class ProgramaPrincipal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		 * Antes de continuar con la tarea, se hace un breve test de las clases y sus métodos
		 * toString() para comprobar que funcionen bien
		 */
		
		Parada par1 = new Parada(1L, "Gijón", 'A', "Administrador Gijón", null);
		System.out.println(par1.toString());
		
		Parada par2 = new Parada(2L, "Avilés", 'A', "Administrador Avilés", null);
		System.out.println(par2.toString());
		
		List<Parada> listaParadas = new ArrayList<>();
		listaParadas.add(par1);
		listaParadas.add(par2);
		
		Estancia est1 = new Estancia(1L, LocalDate.now(), false, par1);
		System.out.println(est1.toString());
		
		Estancia est2 = new Estancia(1L, LocalDate.now(), false, par2);
		System.out.println(est2.toString());
		
		List<Estancia> listaEstancias = new ArrayList<>();
		listaEstancias.add(est1);
		listaEstancias.add(est2);

		Carnet c1 = new Carnet();
		c1.setId(1L);
		c1.setFechaexp(LocalDate.now());
		c1.setDistancia(15.4);
		c1.setNvips(0);
		c1.setParadaInicial(par1);
		
		System.out.println(c1.toString());
		
		Peregrino p1 = new Peregrino(1L, "Sophia", "Española", c1, listaParadas, listaEstancias);
		System.out.println(p1.toString());
	}

}