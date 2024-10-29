package vista;

import controlador.Controlador;

public class ProgramaPrincipal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*
		 * Sólo es necesario llamar al método login() en el programa principal,
		 * puesto que todos los demás métodos están enlazados con éste.
		 * 
		 * Si se considera necesario, se puede cambiar para que se muestre
		 * algún menú en la clase principal.
         *
         * Se adjunta también el enlace al repositorio remoto donde se pueden ver
         * los commits realizados durante el proyecto:
         * >> https://github.com/SophiaDLM/TAREA1AD_SophiaDeLucaMiranda.git <<
		 */
		
		System.out.println("Iniciando programa...");
		
		Controlador.menuInvitado();
	}

}