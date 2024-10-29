package controlador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import modelo.Carnet;
import modelo.Parada;
import modelo.Peregrino;
import modelo.Perfil;
import modelo.Sesion;

public class Controlador {
	public static Scanner leer = new Scanner(System.in);
	public static Sesion sesionActual = new Sesion(Perfil.INVITADO);
	
	public static boolean sesionIniciada = false;
	
	public static File ficheroProp = new File("src/main/resources/application.properties");
	public static File ficheroCred = new File("src/main/editable/credenciales.txt");
	public static File ficheroParadas = new File("src/main/editable/paradas.dat");
	public static File ficheroPaisesXML = new File("src/main/resources/paises.xml");
	
	public static FileInputStream fisFichero;
	public static InputStreamReader isrFichero;
	public static BufferedReader brFichero;
	public static ObjectInputStream oisFichero;
	
	public static FileOutputStream fosFichero;
	public static OutputStreamWriter oswFichero;
	public static ObjectOutputStream oosFichero;
	public static BufferedWriter bwFichero;
	
	
	public static void login() {
		Properties propiedades = new Properties();
		FileInputStream fisAdmin;
		
		try {
			fisAdmin = new FileInputStream(ficheroProp);
			propiedades.load(fisAdmin);
			
			String usuAdmin = propiedades.getProperty("UsuarioAdministrador");
			String conAdmin = propiedades.getProperty("ContraseñaAdministrador");
			
			System.out.print("Introduzca su nombre de usuario: ");
			String nombre = leer.nextLine();
			System.out.print("Introduzca su contraseña: ");
			String contra = leer.nextLine();
			
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
			System.out.println(">> ERROR - "+e.getMessage());
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
		String linea;
		
		try {
			fisFichero = new FileInputStream(fichero);
			isrFichero = new InputStreamReader(fisFichero);
			brFichero = new BufferedReader(isrFichero);
			linea = brFichero.readLine();
			
			while(linea != null) {
				String[] campos = linea.split(" ");
				
				if(campos[0].equals(nombre) && campos[1].equals(contra)) {
					return campos[2];
				}
				
				linea = brFichero.readLine();
			}
			
			brFichero.close();
			isrFichero.close();
			fisFichero.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println(">> ERROR - No se ha encontrado el archivo");
		} catch (Exception e) {
			System.out.println(">> ERROR - "+e.getMessage());
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
				case 1: nuevoPeregrino();
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
			System.out.println("2) Ver todas las paradas");
			System.out.println("3) Cerrar sesión");
			System.out.println("-----------------------");
			System.out.print("Elija una opción: ");
			opcion = leer.nextInt();
			
			switch(opcion) {
				case 1: nuevaParada();
						break;
						
				case 2: System.out.print(leerParadas()+"\n");
						menuAdministrador();
						break;
						
				case 3: logout();
						break;
						
				default: System.out.println(">> ERROR - Opción inválida");
						 break;
			}
		} while(opcion != 3);
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
				case 1: ExportarCarnet.exportarCarnetXML(null); //No testeado
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
	
	
	//////////////////////////////////////////////////////////////////////////////
	public static void nuevaParada() {
		try {
			System.out.print("Introduzca el nombre de la parada: ");
			leer.nextLine(); //Limpia el buffer. Si no se coloca, salta a la siguiente lectura por pantalla
			String nomParada = leer.nextLine();
			System.out.print("Introduzca la región en la que se encuentra: ");
			char region = leer.next().charAt(0);
			
			System.out.print("Introduzca el nombre del responsable de la parada: ");
			leer.nextLine(); //Limpia el buffer. Si no se coloca, salta a la siguiente lectura por pantalla
			String nomResponsableParada = leer.nextLine();
			System.out.print("Introduzca la contraseña del responsable de la parada: ");
			String contraResponsableParada = leer.nextLine();
						
			if(verificarResponsableParada(ficheroCred, nomResponsableParada, contraResponsableParada) == false) {
				Parada nuevaParada = new Parada(generarID(), nomParada, region, nomResponsableParada);
				escribirParada(nuevaParada);
				escribirCredenciales(nomResponsableParada, contraResponsableParada, "parada", generarID());
			} else {
				menuAdministrador();
			}
			
		} catch (Exception e) {
			System.out.println(">> ERROR - "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public static void nuevoPeregrino() {
		try {
			System.out.print("Introduzca su nombre: ");
			leer.nextLine(); //Limpia el buffer. Si no se coloca, salta a la siguiente lectura por pantalla
			String nombre = leer.nextLine();
			System.out.print("Introduzca su contraseña: ");
			String contra = leer.nextLine();
			
			System.out.println("\nSeleccione su nacionalidad según la siguiente lista de países: ");
			TreeMap<String, String> tmPaises = leerPaisesXML(ficheroPaisesXML);
			mostrarPaises(tmPaises);
			System.out.print("Introduzca el ID: ");
			String nacionalidad = leer.next();
			
			System.out.println("\nSeleccione la parada en donde se encuentra: ");
			List<Parada> listaParadas = leerParadas();
			for(Parada indParada: listaParadas) {
				System.out.println(indParada.toString());
			}
			System.out.print("Introduzca el ID: ");
			Long id = leer.nextLong();
			
			System.out.println("\n¿Son correctos los siguientes datos?");
			System.out.println("Nombre: "+nombre);
			System.out.println("Contraseña: "+contra);
			if (tmPaises.containsKey(nacionalidad)) {
				System.out.println("Nacionalidad: "+tmPaises.get(nacionalidad));
			}
			Parada paradaInicial = new Parada();
			for (Parada paradaIni: listaParadas) {
				if(paradaIni.getId() == id) {
					paradaInicial = paradaIni;
					System.out.println("Parada inicial: "+paradaInicial.getNombre());
					break;
				}
			}
			System.out.print("Introduzca S o N: ");
			String respuesta = leer.next();
			
			String tipoUsuario = verificarCredenciales(ficheroCred, nombre, contra);
			
			if(respuesta.equalsIgnoreCase("S")) {
				if(tipoUsuario.equals("peregrino")) {
					System.out.println(">> ERROR - Ese peregrino ya existe");
				} else {
					Carnet nuevoCarnet = new Carnet(generarID(), LocalDate.now(), 0.0, 0, paradaInicial);
					Peregrino nuevoPeregrino = new Peregrino(generarID(), nombre, nacionalidad, nuevoCarnet);
					System.out.println(nuevoPeregrino.toString());
					menuPeregrino();
				}
			} else if(respuesta.equalsIgnoreCase("N")) {
				System.out.println("Vuelva a intentarlo");
			} else {
				System.out.println(">> ERROR - Opción inválida");
			}
		
		} catch (Exception e) {
			System.out.println(">> ERROR - "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	
	public static Long generarID() {
		Long ultimoID = 0L;
		String linea;

		try {
			fisFichero = new FileInputStream(ficheroCred);
			isrFichero = new InputStreamReader(fisFichero);
			brFichero = new BufferedReader(isrFichero);
			linea = brFichero.readLine();
			
			while(linea != null) {
				String[] campos = linea.split(" ");
				Long nuevoID = Long.parseLong(campos[3]);
				
				if(campos[2].equals("parada") && nuevoID > ultimoID) {
					ultimoID = nuevoID;
				} else if (campos[2].equals("peregrino") && nuevoID > ultimoID) {
					ultimoID = nuevoID;
				}
				
				linea = brFichero.readLine();
			}
			
			brFichero.close();
			isrFichero.close();
			fisFichero.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println(">> ERROR - No se ha encontrado el archivo");
		} catch (IOException ioe) {
			System.out.println(">> ERROR - Ha ocurrido un problema de entrada/salida");
		} catch (NumberFormatException nfe) {
			System.out.println(">> ERROR - El formato de número no es correcto");			
		} catch (Exception e) {
			System.out.println(">> ERROR - "+e.getMessage());
			e.printStackTrace();
		}
		return ultimoID+1;
	}
	
	
	public static boolean verificarResponsableParada(File fichero, String nomResponsableParada, String contraResponsableParada) {
		String linea;
		
		try {
			fisFichero = new FileInputStream(ficheroCred);
			isrFichero = new InputStreamReader(fisFichero);
			brFichero = new BufferedReader(isrFichero);
			linea = brFichero.readLine();
			
			while(linea != null) {
				String[] campos = linea.split(" ");
				
				if(campos[0].equals(nomResponsableParada) && campos[1].equals(contraResponsableParada) && campos[2].equals("parada")) {
					System.out.println(">> ERROR - El responsable de la parada ya existe");
					return true;
					
				} else if (campos[0].equals(nomResponsableParada) && campos[1].equals(contraResponsableParada) && campos[2].equals("peregrino")) {
					System.out.println(">> ERROR - Un peregrino no puede administrar una parada");
					return true;
				}
				
				linea = brFichero.readLine();
			}
			
			brFichero.close();
			isrFichero.close();
			fisFichero.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println(">> ERROR - No se ha encontrado el archivo");
		} catch (IOException ioe) {
			System.out.println(">> ERROR - Ha ocurrido un problema de entrada/salida");
		} catch (Exception e) {
			System.out.println(">> ERROR - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static List<Parada> leerParadas() {
		List<Parada> listaParadas = new ArrayList<>();
		Parada paradaActual;
		
		try {
			fisFichero = new FileInputStream(ficheroParadas);
			oisFichero = new ObjectInputStream(fisFichero);
			
			while(true) {
				try {
					paradaActual = (Parada) oisFichero.readObject();
					listaParadas.add(paradaActual);
				} catch (EOFException eofe) {
					break;
				}
			}
			
			oisFichero.close();
			fisFichero.close();
		} catch (FileNotFoundException fnfe) {
	        System.out.println(">> ERROR - No se ha encontrado el archivo");
	    } catch (ClassNotFoundException cnfe) {
	        System.out.println(">> ERROR - No se ha encontrado la clase");
	    } catch (Exception e) {
			System.out.println(">> ERROR - "+e.getMessage());
			e.printStackTrace();
		}
		return listaParadas;
	}
	
	
	public static void escribirParada(Parada nuevaParada) {
		List<Parada> listaParadas = leerParadas();
		
		if(!existeParada(nuevaParada, listaParadas)) {
			try {
				listaParadas.add(nuevaParada);
				
				fosFichero = new FileOutputStream(ficheroParadas);
				oosFichero = new ObjectOutputStream(fosFichero);
				
				for(Parada indParada: listaParadas) {
					oosFichero.writeObject(indParada);
				}
				
				System.out.println("\n>> Se ha añadido la parada");
				
				oosFichero.close();
				fosFichero.close();
			} catch (FileNotFoundException fnfe) {
				System.out.println(">> ERROR - No se ha encontrado el archivo");
			} catch (Exception e) {
				System.out.println(">> ERROR - "+e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public static boolean existeParada(Parada nuevaParada, List<Parada> listaParadas) {
		for(Parada indParada: listaParadas) {
			if(indParada.equals(nuevaParada)) {
				System.out.println(">> ERROR - La parada ya existe");
				return true;
			}
		}
		return false;
	}
	
	
	public static void escribirCredenciales(String nombre, String contra, String tipoUsuario, Long id) {
		String linea;
		
		try {
			fosFichero = new FileOutputStream(ficheroCred, true);
			oswFichero = new OutputStreamWriter(fosFichero);
			bwFichero = new BufferedWriter(oswFichero);
			
			linea = nombre+" "+contra+" "+tipoUsuario+" "+id;
			bwFichero.newLine();
			bwFichero.write(linea);
			
			System.out.println("\n>> Se han añadido las credenciales");
			
			bwFichero.close();
			oswFichero.close();
			fosFichero.close();
			
		} catch (FileNotFoundException fnfe) {
			System.out.println(">> ERROR - No se ha encontrado el archivo");
		} catch (Exception e) {
			System.out.println(">> ERROR - "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	public static TreeMap<String, String> leerPaisesXML(File ficheroPaisesXML) {
		TreeMap<String, String> tmPaises = new TreeMap<>();
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document documento = db.parse(ficheroPaisesXML);
			
			NodeList listaPaises, listaPais;
			Element paises, pais, id, nombre;
			int indPaises, indPais;
			
			listaPaises = documento.getElementsByTagName("paises");
			indPaises = 0;
			
			while(indPaises < listaPaises.getLength()) {
				paises = (Element) listaPaises.item(indPaises);
				
				listaPais = paises.getElementsByTagName("pais");
				indPais = 0;
				
				while(indPais < listaPais.getLength()) {
					pais = (Element) listaPais.item(indPais);
					
					id = (Element) pais.getElementsByTagName("id").item(0);
					nombre = (Element) pais.getElementsByTagName("nombre").item(0);
					
					if(id != null && nombre != null) {
						String textoID = id.getTextContent();
						String textoNombre = nombre.getTextContent();
						tmPaises.put(textoID, textoNombre);
					}
					
					indPais++;
				}
				indPaises++;
			}
		} catch (ParserConfigurationException pce) {
			System.out.println(">> ERROR - "+pce.getMessage());
		} catch (IOException ioe) {
			System.out.println(">> ERROR - Ha ocurrido un problema de entrada/salida");
		} catch (Exception e) {
			System.out.println();
		}
		return tmPaises;
	}
	
	
	public static void mostrarPaises(TreeMap<String, String> tmPaises) {
		for(Map.Entry<String, String> indice: tmPaises.entrySet()) {
			String id = indice.getKey();
			String nombre = indice.getValue();
			
			System.out.println(id+" - "+nombre);
		}
	}
	
	
}
