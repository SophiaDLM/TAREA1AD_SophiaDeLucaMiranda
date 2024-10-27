package controlador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Scanner;

import modelo.Perfil;
import modelo.Sesion;

public class Controlador {
	public static Scanner leer = new Scanner(System.in);
	public static Sesion sesionActual = new Sesion(Perfil.INVITADO);
	public static boolean sesionIniciada = false;
	
	public static void login() {
		Properties propiedades = new Properties();
		File ficheroProp = new File("src/main/resources/application.properties");
		File ficheroCred = new File("src/main/editable/credenciales.txt");
		FileInputStream fisAdmin;
		
		try {
			fisAdmin = new FileInputStream(ficheroProp);
			propiedades.load(fisAdmin);
			
			String usuAdmin = propiedades.getProperty("UsuarioAdministrador");
			String conAdmin = propiedades.getProperty("ContraseñaAdministrador");
			
			System.out.print("Introduzca su nombre de usuario: ");
			String nombre = leer.next();
			System.out.print("Introduzca su contraseña: ");
			String contra = leer.next();
			
			String tipoUsuario = verificarCredenciales(ficheroCred, nombre, contra);
			
			if(nombre.equals(usuAdmin) && contra.equals(conAdmin)) {
				sesionIniciada = true;
				sesionActual.setPerfilUsuario(Perfil.ADMINISTRADOR);
				System.out.println("<< ¡BIENVENIDO "+nombre.toUpperCase()+"! >>");
				menuAdministrador();
				
			} else if (tipoUsuario.equals("peregrino")){
				sesionIniciada = true;
				sesionActual.setPerfilUsuario(Perfil.PEREGRINO);
				System.out.println("<< ¡BIENVENIDO/A "+nombre.toUpperCase()+"! >>");
				menuPeregrino();
			
			} else if (tipoUsuario.equals("parada")) {
				sesionIniciada = true;
				sesionActual.setPerfilUsuario(Perfil.RESPONSABLE_PARADA);
				System.out.println("<< ¡BIENVENIDO "+nombre.toUpperCase()+"! >>");
				menuResponsableParada();
			
			} else {
				sesionIniciada = false;
				System.out.println(">> ERROR - No se ha encontrado al usuario");
				System.out.println(">> Usted está accediendo como invitado");
				menuInvitado();
			}
			
			fisAdmin.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println(">> ERROR - No se ha encontrado el archivo");
		} catch (IOException ioe) {
			System.out.println(">> ERROR - Ha ocurrido un problema de entrada/salida");
		} catch (Exception e) {
			System.out.println(">> ERROR - "+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public static void logout() {
		System.out.print("¿Está seguro de que desea cerrar sesión? (S/N) - ");
		String respuesta = leer.next();
		
		do {
			switch(respuesta.toUpperCase()) {
				case "S": sesionActual.setPerfilUsuario(Perfil.INVITADO);
						  System.out.println("Volviendo al menú de invitado...");
						  menuInvitado();
						  return;
				
				case "N": 
					switch(sesionActual.getPerfilUsuario()) {
						case ADMINISTRADOR: System.out.println("Volviendo al menú de administrador...");
											menuAdministrador();
											return;

						case PEREGRINO: System.out.println("Volviendo al menú de peregrino...");
										menuPeregrino();
										return;
			
						case RESPONSABLE_PARADA: System.out.println("Volviendo al menú del responsable de parada...");
												 menuResponsableParada();
												 return;
					 
						default: System.out.println(">> ERROR - Perfil no encontrado");
								 menuInvitado();
								 return;
					}
					
				default:
					System.out.println(">> ERROR - Opción inválida");
						switch(sesionActual.getPerfilUsuario()) {
							case ADMINISTRADOR: System.out.println("Volviendo al menú de administrador...");
												menuAdministrador();
												return;

							case PEREGRINO: System.out.println("Volviendo al menú de peregrino...");
											menuPeregrino();
											return;
	
							case RESPONSABLE_PARADA: System.out.println("Volviendo al menú del responsable de parada...");
													 menuResponsableParada();
										 			 return;
			 
							default: System.out.println(">> ERROR - Perfil no encontrado");
									 menuInvitado();
									 return;
						}
			}
		} while(!respuesta.equals("S"));
	}
	
	//////////////////////////////////////////////////////////////////////////////
	public static String verificarCredenciales(File fichero, String nombre, String contra) {
		FileInputStream fisCred;
		InputStreamReader isrCred;
		BufferedReader brCred;
		String linea;
		
		try {
			fisCred = new FileInputStream(fichero);
			isrCred = new InputStreamReader(fisCred);
			brCred = new BufferedReader(isrCred);
			linea = brCred.readLine();
			
			while(linea != null) {
				String[] campos = linea.split(" ");
				
				if(campos[0].equals(nombre) && campos[1].equals(contra)) {
					return campos[2];
				}
				
				linea = brCred.readLine();
			}
			
			brCred.close();
			isrCred.close();
			fisCred.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println(">> ERROR - No se ha encontrado el archivo");
		} catch (IOException ioe) {
			System.out.println(">> ERROR - Ha ocurrido un problema de entrada/salida");
		} catch (Exception e) {
			System.out.println(">> ERROR - "+e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return "";
	}
	
	//////////////////////////////////////////////////////////////////////////////
	public static void menuInvitado() {
		int opcion;
		
		do {
			System.out.println("\n<< MENÚ DE INVITADO >>");
			System.out.println("-----------------------");
			System.out.println("1) Registrarse");
			System.out.println("2) Iniciar sesión");
			System.out.println("3) Salir");
			System.out.println("-----------------------");
			System.out.print("Elija una opción: ");
			opcion = leer.nextInt();
			
			switch(opcion) {
				case 1: System.out.println("Seguimos trabajando en ello...");
						break;
						
				case 2: login();
						if (sesionIniciada = true) {
							return;
						}
						break;
						
				case 3: System.out.println("<< ¡HASTA PRONTO! >>");
						break;
						
				default: System.out.println(">> ERROR - Opción inválida");
						 break;
			}
		} while(opcion != 3);
	}
	
	
	public static void menuAdministrador() {
		int opcion;
		
		do {
			System.out.println("\n<< MENÚ DE ADMINISTRADOR >>");
			System.out.println("-----------------------");
			System.out.println("1) Añadir nueva parada");
			System.out.println("2) Cerrar sesión");
			System.out.println("-----------------------");
			System.out.print("Elija una opción: ");
			opcion = leer.nextInt();
			
			switch(opcion) {
				case 1: System.out.println("Seguimos trabajando en ello...");
						break;
						
				case 2: logout();
						break;
						
				default: System.out.println(">> ERROR - Opción inválida");
						 break;
			}
		} while(opcion != 2);
	}
	
	
	public static void menuPeregrino() {
		int opcion;
		
		do {
			System.out.println("\n<< MENÚ DE PEREGRINO >>");
			System.out.println("-----------------------");
			System.out.println("1) Exportar carnet en XML");
			System.out.println("2) Cerrar sesión");
			System.out.println("-----------------------");
			System.out.print("Elija una opción: ");
			opcion = leer.nextInt();
			
			switch(opcion) {
				case 1: System.out.println("Seguimos trabajando en ello...");
						break;
						
				case 2: logout();
						break;
						
				default: System.out.println(">> ERROR - Opción inválida");
						 break;
			}
		} while(opcion != 2);
	}
	
	
	public static void menuResponsableParada() {
		int opcion;
		
		do {
			System.out.println("\n<< MENÚ DEL RESPONSABLE DE PARADA >>");
			System.out.println("--------------------------------------");
			System.out.println("1) Registrar estancia");
			System.out.println("2) Cerrar sesión");
			System.out.println("--------------------------------------");
			System.out.print("Elija una opción: ");
			opcion = leer.nextInt();
			
			switch(opcion) {
				case 1: System.out.println("Seguimos trabajando en ello...");
						break;
						
				case 2: logout();
						break;
						
				default: System.out.println(">> ERROR - Opción inválida");
						 break;
			}
		} while(opcion != 2);
	}
}
