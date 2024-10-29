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
import java.util.InputMismatchException;
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
	/*
	 * << NOTA IMPORTANTE >>
	 * Esta clase se dividirá en otras para poder administrarla mejor y hacer
	 * que el código sea más legible, pero por temas de tiempo y errores que
	 * surgieron durante la realización de la tarea, se ha de quedar así.
	 * 
	 * A pesar de ello, se intentó mantener cierta limpieza y coherencia para
	 * facilitar su lectura y comprensión.
	 */
	
	//Atributos de tipo estático que se han utilizado y re-utilizado en los métodos:
	public static Scanner leer = new Scanner(System.in);
	public static Sesion sesionActual = new Sesion(Perfil.INVITADO);
	
	public static boolean sesionIniciada = false;
	
	//Declaración de los diferentes ficheros a utilizar
	public static File ficheroProp = new File("src/main/resources/application.properties");
	public static File ficheroCred = new File("src/main/editable/credenciales.txt");
	public static File ficheroParadas = new File("src/main/editable/paradas.dat");
	public static File ficheroPaisesXML = new File("src/main/resources/paises.xml");
	
	//Declaración de flujos de lectura:
	public static FileInputStream fisFichero;
	public static InputStreamReader isrFichero;
	public static BufferedReader brFichero;
	public static ObjectInputStream oisFichero;
	
	//Declaración de flujos de escritura:
	public static FileOutputStream fosFichero;
	public static OutputStreamWriter oswFichero;
	public static ObjectOutputStream oosFichero;
	public static BufferedWriter bwFichero;
	
	public static Peregrino peregrino;
		
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * << ÍNDICE DE MÉTODOS >>
	 * 
	 * - login() - línea 100
	 * - logout() - línea 163
	 * - verificarCredenciales() - línea 224
	 * - menuInvitado() - línea 272
	 * - menúAdministrador() - línea 312
	 * - menúPeregrino() - línea 350
	 * - menúResponsableParada - línea 383
	 * - nuevaParada() - línea 418
	 * - nuevoPeregrino() - línea 454
	 * - generarID() - línea 549 -> No funciona del todo bien
     * - verificarResponsableParada() - línea 609
     * - leerParada() - línea 658
	 * - escribirParada() - línea 695
	 * - existeParada() - línea 730
	 * - escribirCredenciales() - línea 747
	 * - leerPaisesXML() -línea 781
	 * - mostrarPaises() - línea 838
	 */
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Método login() que se corresponde con el CU2 de la tarea
	public static void login() {
		Properties propiedades = new Properties();
		FileInputStream fisAdmin;
		
		try {
			//Se lee el fichero donde se almacenan las creenciales del admin
			fisAdmin = new FileInputStream(ficheroProp);
			propiedades.load(fisAdmin);
			
			//Se obtiene el nombre y contraseña del admin
			String usuAdmin = propiedades.getProperty("UsuarioAdministrador");
			String conAdmin = propiedades.getProperty("ContraseñaAdministrador");
			
			//Se piden las credenciales por pantalla al usuario
			leer.nextLine();
			System.out.print("Introduzca su nombre de usuario: ");
			String nombre = leer.nextLine();
			System.out.print("Introduzca su contraseña: ");
			String contra = leer.nextLine();
			
			//Se llama a un método que retorna el tipo de usuario
			String tipoUsuario = verificarCredenciales(ficheroCred, nombre, contra);
			
			//Se comprueba el tipo de usuario y en función a ello, se muestran diferentes opciones
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
				//Se muestra si no se encontraron las credenciales
				sesionIniciada = false;
				System.out.println(">> ERROR - No se ha encontrado al usuario -> Usted está accediendo como invitado");
				menuInvitado();
			}
			
			//Se cierra el flujo
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
	
	//Método logout() que se corresponde con el CU2 de la tarea
	public static void logout() {
		//Se pregunta al usuario si está seguro
		System.out.print("¿Está seguro de que desea cerrar sesión? (S/N) - ");
		String respuesta = leer.next();
		
		do {
			//Se comprueba la respuesta
			switch(respuesta.toUpperCase()) {
				//Si el usuario desea cerrar sesión, se le otorga el perfil de invitado de nuevo y se muestra su menú correspondiente
				case "S": sesionActual.setPerfilUsuario(Perfil.INVITADO);
						  System.out.println("Volviendo al menú de invitado...");
						  menuInvitado();
						  return;
				
				//Si el usuario dice que no, se vuelve a su menú según el perfil que tenga asignado
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
					
				//Si el usuario no introduce ni S ni N, se muestra un mensaje de alerta y se vuelve al menú según el perfil
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
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Método que verifica si las credenciales ya existen o no
	public static String verificarCredenciales(File fichero, String nombre, String contra) {
		String linea;
		
		try {
			//Se inicializan los flujos de lectura
			fisFichero = new FileInputStream(fichero);
			isrFichero = new InputStreamReader(fisFichero);
			brFichero = new BufferedReader(isrFichero);
			linea = brFichero.readLine();
			
			//Comprueba que la línea no esté vacía
			while(linea != null) {
				//Divide la línea extraída del fichero en campos separados por espacios en blanco
				String[] campos = linea.split(" ");
				
				//Comprueba que el nombre y la contraseña del fichero coincidan con la que se pasa como parámetro
				if(campos[0].equals(nombre) && campos[1].equals(contra)) {
					//Devuelve el tipo de usuario que es
					return campos[2];
				}
				
				//Lee la siguiente línea
				linea = brFichero.readLine();
			}
			
			//Se cierran los flujos de lectura
			brFichero.close();
			isrFichero.close();
			fisFichero.close();
			
		} catch (FileNotFoundException fnfe) {
			System.out.println(">> ERROR - No se ha encontrado el archivo");
		} catch (Exception e) {
			System.out.println(">> ERROR - "+e.getMessage());
			e.printStackTrace();
		}
		
		//Retorna una cadena vacía si no se han encontrado coincidencias
		return "";
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * Se crean diferentes menús para los diferentes tipo de usuarios, mostrando las opciones
	 * disponibles y llamando los métodos necesarios.
	 */
	
	public static void menuInvitado() {
		int opcion = -1;
		
		do {
			System.out.println("\n<< MENÚ DE INVITADO >>");
			System.out.println("-----------------------");
			System.out.println("1) Registrarse");
			System.out.println("2) Iniciar sesión");
			System.out.println("3) Salir");
			System.out.println("-----------------------");
			System.out.print("Elija una opción: ");
			
			try {
				opcion = leer.nextInt();
				
				switch(opcion) {
					case 1: peregrino = nuevoPeregrino();
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
			} catch(InputMismatchException ime) {
					//Se asegura de que el usuario no introduzca letras
					System.out.println(">> ERROR - Debe ingresar un número");
					leer.nextLine(); //Limpia el buffer
			}
		} while(opcion != 3);
	}
	
	
	public static void menuAdministrador() {
		int opcion = -1;
		
		do {
			System.out.println("\n<< MENÚ DE ADMINISTRADOR >>");
			System.out.println("-----------------------");
			System.out.println("1) Añadir nueva parada");
			System.out.println("2) Ver todas las paradas");
			System.out.println("3) Cerrar sesión");
			System.out.println("-----------------------");
			System.out.print("Elija una opción: ");
			
			try {
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
			} catch(InputMismatchException ime) {
				//Se asegura de que el usuario no introduzca letras
				System.out.println(">> ERROR - Debe ingresar un número");
				leer.nextLine(); //Limpia el buffer
			}
		} while(opcion != 3);
	}
	
	
	public static void menuPeregrino() {
		int opcion = -1;
		
		do {
			System.out.println("\n<< MENÚ DE PEREGRINO >>");
			System.out.println("-----------------------");
			System.out.println("1) Exportar carnet en XML");
			System.out.println("2) Cerrar sesión");
			System.out.println("-----------------------");
			System.out.print("Elija una opción: ");
			
			try {
				opcion = leer.nextInt();
			
				switch(opcion) {
					case 1: ExportarCarnet.exportarCarnetXML(peregrino); //No funciona el método porque peregrino se queda como null
							break;
						
					case 2: logout();
							break;
						
					default: System.out.println(">> ERROR - Opción inválida");
							 break;
				}
			} catch(InputMismatchException ime) {
				//Se asegura de que el usuario no introduzca letras
				System.out.println(">> ERROR - Debe ingresar un número");
				leer.nextLine(); //Limpia el buffer
			}
		} while(opcion != 2);
	}
	
	
	public static void menuResponsableParada() {
		int opcion = -1;
		
		do {
			System.out.println("\n<< MENÚ DEL RESPONSABLE DE PARADA >>");
			System.out.println("--------------------------------------");
			System.out.println("1) Registrar estancia");
			System.out.println("2) Cerrar sesión");
			System.out.println("--------------------------------------");
			System.out.print("Elija una opción: ");
			
			try {
				opcion = leer.nextInt();
			
				switch(opcion) {
					case 1: System.out.println("Seguimos trabajando en ello...");
							break;
						
					case 2: logout();
							break;
						
					default: System.out.println(">> ERROR - Opción inválida");
							 break;
				}
			} catch(InputMismatchException ime) {
				//Se asegura de que el usuario no introduzca letras
				System.out.println(">> ERROR - Debe ingresar un número");
				leer.nextLine(); //Limpia el buffer
			}
		} while(opcion != 2);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Método que nuevaParada() que se corresponde con el CU1 de la tarea
	public static void nuevaParada() {
		try {
			//Se piden los datos de la parada
			System.out.print("Introduzca el nombre de la parada: ");
			leer.nextLine(); //Limpia el buffer. Si no se coloca, salta a la siguiente lectura por pantalla
			String nomParada = leer.nextLine();
			System.out.print("Introduzca la región en la que se encuentra: ");
			char region = leer.next().charAt(0);
			
			//Se piden los datos del responsable de la parada
			System.out.print("Introduzca el nombre del responsable de la parada: ");
			leer.nextLine(); //Limpia el buffer. Si no se coloca, salta a la siguiente lectura por pantalla
			String nomResponsableParada = leer.nextLine();
			System.out.print("Introduzca la contraseña del responsable de la parada: ");
			String contraResponsableParada = leer.nextLine();
						
			String tipoUsuario = verificarCredenciales(ficheroCred, nomResponsableParada, contraResponsableParada);
			
			//Se verifica mediante un método específico si ya existe o es un peregrino
			if(verificarResponsableParada(ficheroCred, nomResponsableParada, contraResponsableParada) == false) {
				//Si no existe, se crea un nuevo objeto parada, se escribe en el archivo paradas.dat y se añaden las credenciales del responsable
				Parada nuevaParada = new Parada(generarID(tipoUsuario), nomParada, region, nomResponsableParada);
				escribirParada(nuevaParada);
				escribirCredenciales(nomResponsableParada, contraResponsableParada, "parada");
			} else {
				//Si existe, se vuelve al menú del administrador
				menuAdministrador();
			}
			
		} catch (Exception e) {
			System.out.println(">> ERROR - "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	//Método nuevoPeregrino() que se corresponde con el CU1 de la tarea
	public static Peregrino nuevoPeregrino() {
		Peregrino nuevoPeregrino = null;
		String respuesta;
		
		try {
			do {
				//Se piden los datos del peregrino
				System.out.print("Introduzca su nombre: ");
				leer.nextLine(); //Limpia el buffer. Si no se coloca, salta a la siguiente lectura por pantalla
				String nombre = leer.nextLine();
				
				System.out.print("Introduzca su contraseña: ");
				String contra = leer.nextLine();
			
				//Se muestra la lista ordenada de países aportado en un documentoXML
				System.out.println("\nSeleccione su nacionalidad según la siguiente lista de países: ");
				TreeMap<String, String> tmPaises = leerPaisesXML(ficheroPaisesXML); //Llama al método para leerlos
				mostrarPaises(tmPaises); //Llama al método para mostrarlos
				System.out.print("Introduzca el ID: ");
				String nacionalidad = leer.next().toUpperCase();
			
				//Se muestran las paradas disponibles y se pide en cuál está el usuario
				System.out.println("\nSeleccione la parada en donde se encuentra: ");
				List<Parada> listaParadas = leerParadas();
				for(Parada indParada: listaParadas) {
					System.out.println(indParada.toString());
				}
				System.out.print("Introduzca el ID: ");
				Long id = leer.nextLong();
			
				//Se pregunta al usuario si los datos introducidos son correctos, mostrándolos por pantalla
				System.out.println("\n¿Son correctos los siguientes datos?");
				System.out.println("Nombre: "+nombre);
				System.out.println("Contraseña: "+contra);
				if (tmPaises.containsKey(nacionalidad)) {
					System.out.println("Nacionalidad: "+tmPaises.get(nacionalidad));
				}
				Parada paradaInicial = null;
				for (Parada paradaIni: listaParadas) {
					if(paradaIni.getId().equals(id)) {
						paradaInicial = paradaIni;
						System.out.println(paradaInicial.toString());
						break;
					}
				}
				System.out.print("Introduzca S o N: ");
				respuesta = leer.next();
			
				//Se llama al método para verificar si existe ese peregrino
				String tipoUsuario = verificarCredenciales(ficheroCred, nombre, contra);
			
				switch(respuesta.toUpperCase()) {
					//Si el usuario confirma que los datos son correctos, se verifica si existe
					case "S": if(tipoUsuario.equals("peregrino")) {
							  	System.out.println(">> ERROR - Ese peregrino ya existe");
							  } else {
								  	//Si no existe, se crea su carnet con los valores por defecto y asignando un ID mediante un método
									Carnet nuevoCarnet = new Carnet(generarID(tipoUsuario), LocalDate.now(), 0.0, 0, paradaInicial);
									//Se crea el peregrino pasándole el resto de datos
									nuevoPeregrino = new Peregrino(generarID(tipoUsuario), nombre, nacionalidad, nuevoCarnet);
									//Se escriben sus credenciales y luego se muestran sus datos
									escribirCredenciales(nombre, contra, "peregrino");
									System.out.println(nuevoPeregrino.toString());
									menuPeregrino(); //Llama al menú correspondiente
									return nuevoPeregrino;	
							  }
							  break;
					
					//Si el usuario dice que no, se volverán a preguntar los datos
					case "N": System.out.println("\nVuelva a introducir los datos\n");
							  break;
					
					default: System.out.println("\n>> ERROR - Opción inválida - Vuelva a introducir los datos\n");
							 break;
				}
			} while(!respuesta.equalsIgnoreCase("S"));
		
		} catch (Exception e) {
			System.out.println(">> ERROR - "+e.getMessage());
			e.printStackTrace();
		}
		//Devuelve un objeto peregrino
		return nuevoPeregrino;
	}
	/*
	 * Es probable que este método no se ejecute correctamente debido a los siguiente errores encontrados:
	 * - El nombre y región de la parada no se muestra por pantalla, por lo que se sospecha que tampoco
	 * se almacena correctamente
	 * 
	 * Evidentemente si este método falla, el de exportarCarnetXML, también fallará
	 */
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Método que se usa para generar un ID automáticamente
	public static Long generarID(String tipoUsuario) {
		Long ultimoParadaID = 0L;
		Long ultimoPeregrinoID = 0L;
		String linea;

		try {
			//Se inicializan los flujos de lectura
			fisFichero = new FileInputStream(ficheroCred);
			isrFichero = new InputStreamReader(fisFichero);
			brFichero = new BufferedReader(isrFichero);
			linea = brFichero.readLine();
			
			//Se comprueba que la línea que lee no esté vacía
			while(linea != null) {
				//Divide los campos con el espacio en blanco y los almacena en un array
				String[] campos = linea.split(" ");
				//El último campo del array lo convierte en un Long para poder operar con él luego
				Long nuevoID = Long.parseLong(campos[3]);
				
				//Comprueba el tipo de usuario y en función de ello, incrementa el número
				if(campos[2].equals("parada")) {
					if (nuevoID > ultimoParadaID) {
						ultimoParadaID = nuevoID;
					}
				} else if (campos[2].equals("peregrino")) {
					if (nuevoID > ultimoPeregrinoID) {
						ultimoPeregrinoID = nuevoID;
					}
				}
				//Lee la siguiente línea del fichero
				linea = brFichero.readLine();
			}
			//Cierra los flujos de lectura
			brFichero.close();
			isrFichero.close();
			fisFichero.close();
			
			//Comprueba el tipo de usuario y lo incrementa en función de los mismos
			if (tipoUsuario.equals("parada")) {
				return ultimoParadaID + 1;
			} else if (tipoUsuario.equals("peregrino")) {
				return ultimoPeregrinoID + 1;
			}
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
		//Retorna el ID generado
		return 1L;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
    //Método que verifica el responsable de parada
	public static boolean verificarResponsableParada(File fichero, String nomResponsableParada, String contraResponsableParada) {
		String linea;
		
		try {
            //Se inicializan los flujos de lectura
			fisFichero = new FileInputStream(ficheroCred);
			isrFichero = new InputStreamReader(fisFichero);
			brFichero = new BufferedReader(isrFichero);
			linea = brFichero.readLine();
			
            //Comprueba que la línea no esté vacía
			while(linea != null) {
                //Divide la línea en campos mediante el split y lo introduce en un array
				String[] campos = linea.split(" ");
				
                //Compueba que no exista ya el responsable de parada
				if(campos[0].equals(nomResponsableParada) && campos[1].equals(contraResponsableParada) && campos[2].equals("parada")) {
                    //Si existe, muestra un mensaje de error y retorna verdadero
					System.out.println(">> ERROR - El responsable de la parada ya existe");
					return true;
				
                //Comprueba que el responsable de parada que se intenta añadir no sea un peregrino
				} else if (campos[0].equals(nomResponsableParada) && campos[1].equals(contraResponsableParada) && campos[2].equals("peregrino")) {
                    //Si es un peregrino, muestra un mensaje de error y retorna verdadero
					System.out.println(">> ERROR - Un peregrino no puede administrar una parada");
					return true;
				}
				//Lee la siguiente línea
				linea = brFichero.readLine();

			}
			//Se cierran los ficheros de lectura
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
        //Retorna falso si no se encontraron coincidencias
		return false;
	}
	
	//Método para leer paradas
	public static List<Parada> leerParadas() {
        //Se crea una lista vacía y un objeto no inicializado que se usará después
		List<Parada> listaParadas = new ArrayList<>();
		Parada paradaActual;
		
		try {
            //Se inicializan los flujos de lectura de objetos
			fisFichero = new FileInputStream(ficheroParadas);
			oisFichero = new ObjectInputStream(fisFichero);
			
			while(true) {
				try {
                    //Lee la parada y la añade a la lista
					paradaActual = (Parada) oisFichero.readObject();
					listaParadas.add(paradaActual);
                //Cuando se llega al final del archivo, sale del bucle
				} catch (EOFException eofe) {
					break;
				}
			}
			//Se cierran los flujos de lectura de objetos
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
        //Retorna la lista de paradas
		return listaParadas;
	}
	
	//Método que escribe una parada en el archivo paradas.dat
	public static void escribirParada(Parada nuevaParada) {
        //Se crea una lista que llama al método leerParadas() para ver las que hay
		List<Parada> listaParadas = leerParadas();
		
        //Se comprueba que no exista ya la parada
		if(existeParada(nuevaParada, listaParadas) == false) {
			try {
                //Si no existe, se añade la nueva parada a la lista
				listaParadas.add(nuevaParada);
				
                //Se inicializan los flujos que escriben objetos
				fosFichero = new FileOutputStream(ficheroParadas);
				oosFichero = new ObjectOutputStream(fosFichero);
				
                //Escribe la parada en el fichero
				for(Parada indParada: listaParadas) {
					oosFichero.writeObject(indParada);
				}
				
				System.out.println("\n>> Se ha añadido la parada");
				
                //Se cierran los flujos que escriben objetos
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
	
	//Método que comprueba si una parada ya existe, evitando que se introduzca la misma parada varias veces
	public static boolean existeParada(Parada nuevaParada, List<Parada> listaParadas) {
		//Recorre la lista de paradas
		for(Parada indParada: listaParadas) {
			//Compara las paradas utilizando el método equals() implementado en la clase parada
			if(indParada.equals(nuevaParada)) {
				//Si se encuentra, muestra un mensaje de error y retorna verdadero
				System.out.println(">> ERROR - La parada ya existe");
				return true;
			}
		}
		//Si no encuentra la parada, retorna falso
		return false;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Método que escribe las credenciales de un nuevo usuario
	public static void escribirCredenciales(String nombre, String contra, String tipoUsuario) {
		String tipo = verificarCredenciales(ficheroCred, nombre, contra);
		String linea;
		
		try {
			//Se inicializan los flujos de escritura
			fosFichero = new FileOutputStream(ficheroCred, true);
			oswFichero = new OutputStreamWriter(fosFichero);
			bwFichero = new BufferedWriter(oswFichero);
			
			//Se escribe el nombre, contraseña y tipo de usuario que se pasa como parámetro, el id se obtiene del método
			linea = nombre+" "+contra+" "+tipoUsuario+" "+generarID(tipo);
			//Se crea una nueva línea y se escribe
			bwFichero.newLine();
			bwFichero.write(linea);
			
			System.out.println("\n>> Se han añadido las credenciales");
			
			//Se cierran los flujos de escritura
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
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Método para leer los paises del fichero XML, devuelve una lista ordenada
	public static TreeMap<String, String> leerPaisesXML(File ficheroPaisesXML) {
		//Se crea una lista vacía
		TreeMap<String, String> tmPaises = new TreeMap<>();
		
		try {
			//Se construye el documento para poder leerlo
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document documento = db.parse(ficheroPaisesXML);
			
			NodeList listaPaises, listaPais;
			Element paises, pais, id, nombre;
			int indPaises, indPais;
			
			//Obtiene todos los elementos en el documento que coincidan con la etiqueta
			listaPaises = documento.getElementsByTagName("paises");
			indPaises = 0;
			
			//Recorre el archivo buscando el elemento
			while(indPaises < listaPaises.getLength()) {
				paises = (Element) listaPaises.item(indPaises);
				
				//Obtiene todos los elementos en el documento que coincidan con la etiqueta
				listaPais = paises.getElementsByTagName("pais");
				indPais = 0;
				
				//Recorre el archivo buscando el elemento
				while(indPais < listaPais.getLength()) {
					pais = (Element) listaPais.item(indPais);
					
					//Obtiene los elementos que coincidan con esas etiquetas
					id = (Element) pais.getElementsByTagName("id").item(0);
					nombre = (Element) pais.getElementsByTagName("nombre").item(0);
					
					//Se comprueba que los elementos no estén vacíos y se introducen en la lista
					if(id != null && nombre != null) {
						String textoID = id.getTextContent();
						String textoNombre = nombre.getTextContent();
						tmPaises.put(textoID, textoNombre);
					}
					//Incrementa el contador para pasar al siguiente elemento
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
		//Retorna la lista de países
		return tmPaises;
	}
	
	//Método que se crea para mostrar los países del fichero XML por pantalla
	public static void mostrarPaises(TreeMap<String, String> tmPaises) {
		//Obtiene cada elemento de la lista ordenada y la muestra
		for(Map.Entry<String, String> indice: tmPaises.entrySet()) {
			//El id es la clave de la lista
			String id = indice.getKey();
			//El nombre es el valor asociado al id
			String nombre = indice.getValue();
			
			System.out.println(id+" - "+nombre);
		}
	}
}