package controlador;

import java.io.File;
import java.time.LocalDate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

import modelo.Estancia;
import modelo.Parada;
import modelo.Peregrino;

public class ExportarCarnet {
	/*
	 * << NOTA IMPORTANTE >>
	 * Este método no ha sido testeado, por lo que se pueden producir errores.
	 * Después de la entrega se realizarán pruebas y se modificará el código si se considera necesario.
	 * 
	 * Este código fue hecho con ayuda de los proyectos proporcionados por el profesor, que mostraban 
	 * ejemplos de cómo leer y escribir archivos XML.
	 */
	
	public static void exportarCarnetXML(Peregrino peregrino) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			DOMImplementation implementacion = db.getDOMImplementation();
			
			//Se crea el documento, pasando como elemento raíz carnet
			Document documento = implementacion.createDocument(null, "carnet", null);
			Element carnet = documento.getDocumentElement();
			
			//Se coloca la versión del XML (esto es opcional, si se desea, en próximas entregas se puede omitir)
			documento.setXmlVersion("1.0");
			ProcessingInstruction pi = documento.createProcessingInstruction("xml-stylesheet", "type=\"text/xml\" href=\"test.xsl\"");
			documento.insertBefore(pi, carnet);
			
			//Se crea el elemento id del carnet, que será el mismo que el del peregrino, y se obtiene de la clase peregrino
			Element idCarnet = documento.createElement("id");
			idCarnet.setTextContent(String.valueOf(peregrino.getId()));
			carnet.appendChild(idCarnet);
			
			//Se crea el elemento fechaexp con el getter de la clase carnet, que por defecto pone la fecha actual
			Element fechaExpedicion = documento.createElement("fechaexp");
			fechaExpedicion.setTextContent(String.valueOf(peregrino.getCarnet().getFechaexp()));
			carnet.appendChild(fechaExpedicion);
			
			//Se crea el elemento expedidoen, que indica el nombre de la parada inicial
			Element expedidoEn = documento.createElement("expedidoen");
			expedidoEn.setTextContent(peregrino.getCarnet().getParadaInicial().getNombre());
			carnet.appendChild(expedidoEn);
			
			//Se crea el elemento peregrino para luego crear sus elementos hijos
			Element pereg = documento.createElement("peregrino");
			
			//Se crea el elemento nombre, que se saca de la clase peregrino
			Element nombre = documento.createElement("nombre");
			nombre.setTextContent(peregrino.getNombre());
			pereg.appendChild(nombre);
			
			//Se crea el elemento nacionalidad, que se saca de la clase peregrino
			Element nacionalidad = documento.createElement("nacionalidad");
			nacionalidad.setTextContent(peregrino.getNacionalidad());
			pereg.appendChild(nacionalidad);
			
			//Se crea el elemento con la fecha de hoy, utilizando el método .now() de LocalDate
			Element hoy = documento.createElement("hoy");
			hoy.setTextContent(LocalDate.now().toString());
			carnet.appendChild(hoy);
			
			//Se crea el elemento distancia total, que se obtiene del carnet del peregrino
			Element distanciaTotal = documento.createElement("distanciatotal");
			distanciaTotal.setTextContent(String.valueOf(peregrino.getCarnet().getDistancia()));
			carnet.appendChild(distanciaTotal);
			
			//Se crea el elemento paradas al que luego se le añadiran sus hijos
			Element paradas = documento.createElement("paradas");
			//Se obtiene la lista de paradas de peregrino para recorrerla
			for(int i = 0; i < peregrino.getListaParadas().size(); i++) {
				//Se obtiene el objeto parada en el que se encuentra el índice
				Parada paradaObj = peregrino.getListaParadas().get(i);
				
				//Se crea el elemento parada al que luego se le pasaran sus hijos
				Element parada = documento.createElement("parada");
				
				//Se crea el elemento orden a partir del índice
				Element orden = documento.createElement("orden");
				orden.setTextContent(String.valueOf(i+1));
				parada.appendChild(orden);
				
				//Se crea el elemento nombre, que se obtiene de la parada
				Element nomParada = documento.createElement("nombre");
				nomParada.setTextContent(paradaObj.getNombre());
				parada.appendChild(nomParada);
				
				//Se crea el elemento region, que se obtiene de parada
				Element regParada = documento.createElement("region");
				regParada.setTextContent(String.valueOf(paradaObj.getRegion()));
				parada.appendChild(regParada);
				
				//Se añade el elemento parada con todos sus hijos al elemento padre paradas
				paradas.appendChild(parada);
			}
			//Se añade el elemento paradas a carnet
			carnet.appendChild(paradas);
			
			//Se crea el elemento estancias
			Element estancias = documento.createElement("estancias");
			for(int i = 0; i < peregrino.getListaEstancias().size(); i++) {
				//Se obtiene el objeto parada en el que se encuentra el índice
				Estancia estanciaObj = peregrino.getListaEstancias().get(i);
				
				//Se crea el elemento estancia al que luego se le pasaran sus hijos
				Element estancia = documento.createElement("estancia");
				
				//Se crea el elemento id, que se obtiene de la clase estancia
				Element idEstancia = documento.createElement("id");
				idEstancia.setTextContent(String.valueOf(estanciaObj.getId()));
				estancia.appendChild(idEstancia);
				
				//Se crea el elemento fecha, que se obtiene de la clase estancia 
				Element fecha = documento.createElement("fecha");
				fecha.setTextContent(String.valueOf(estanciaObj.getFecha()));
				estancia.appendChild(fecha);
				
				//Se crea el elemento parada, que pasa muestra su nombre solamente
				Element paradaNombre = documento.createElement("parada");
				paradaNombre.setTextContent(estanciaObj.getParada().getNombre());
				estancia.appendChild(paradaNombre);
				
				//Se crea el elemento vip, que se obtiene de estancia
				Element vip = documento.createElement("vip");
				if (estanciaObj.isVip()) {
					estancia.appendChild(vip);
				}
				//Se añade el elemento estancia con todos sus hijos al elemento padre estancias
				estancias.appendChild(estancia);
			}
            //Se añade el elemento estancias a carnet
            carnet.appendChild(estancias);
			
			//Se crea el documento
			DOMSource source = new DOMSource(documento);
			File fichero = new File("src/main/exportable/"+peregrino.getNombre()+".xml");
			StreamResult sr = new StreamResult(fichero);
			
			//Se transforma el documento en un XML
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.transform(source, sr);
			
			System.out.println(">> Carnet exportado con éxito");

		} catch (ParserConfigurationException pce) {
			System.out.println(">> ERROR - "+pce.getMessage());
		} catch (TransformerConfigurationException tce) {
			System.out.println(">> ERROR - "+tce.getMessage());
		} catch (TransformerException te) {
			System.out.println(">> ERROR - "+te.getMessage());
		} catch (Exception e) {
			System.out.println(">> ERROR - "+e.getMessage());
		}
	}
}