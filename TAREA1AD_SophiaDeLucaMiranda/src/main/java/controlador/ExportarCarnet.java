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

import modelo.Parada;
import modelo.Peregrino;

public class ExportarCarnet {
	/*
	 * NOTA IMPORTANTE - Este método no ha sido testeado, por lo que se pueden producir errores.
	 * Después de la entrega se realizarán pruebas y se modificará el código si se considera necesario.
	 * Este código fue hecho con ayuda de los proyectos proporcionados por el profesor, que mostraban 
	 * ejemplos de cómo leer y escribir archivos XML.
	 */
	
	public static void exportarCarnetXML(Peregrino peregrino) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			DOMImplementation implementacion = db.getDOMImplementation();
			
			Document documento = implementacion.createDocument(null, "carnet", null);
			Element carnet = documento.getDocumentElement();
			
			documento.setXmlVersion("1.0");
			ProcessingInstruction pi = documento.createProcessingInstruction("xml-stylesheet", "type=\"text/xml\" href=\"test.xsl\"");
			documento.insertBefore(pi, carnet);
			
			Element idCarnet = documento.createElement("id");
			idCarnet.setTextContent(String.valueOf(peregrino.getId()));
			carnet.appendChild(idCarnet);
			
			Element fechaExpedicion = documento.createElement("fechaexp");
			fechaExpedicion.setTextContent(String.valueOf(peregrino.getCarnet().getFechaexp()));
			carnet.appendChild(fechaExpedicion);
			
			Element expedidoEn = documento.createElement("expedidoen");
			expedidoEn.setTextContent(peregrino.getCarnet().getParadaInicial().getNombre());
			carnet.appendChild(expedidoEn);
			
			Element pereg = documento.createElement("peregrino");
			
			Element nombre = documento.createElement("nombre");
			nombre.setTextContent(peregrino.getNombre());
			pereg.appendChild(nombre);
			
			Element nacionalidad = documento.createElement("nacionalidad");
			nacionalidad.setTextContent(peregrino.getNacionalidad());
			pereg.appendChild(nacionalidad);
			
			Element hoy = documento.createElement("hoy");
			hoy.setTextContent(LocalDate.now().toString());
			carnet.appendChild(hoy);
			
			Element distanciaTotal = documento.createElement("distanciatotal");
			distanciaTotal.setTextContent(String.valueOf(peregrino.getCarnet().getDistancia()));
			carnet.appendChild(distanciaTotal);
			
			Element paradas = documento.createElement("paradas");
			for(int i = 0; i < peregrino.getListaParadas().size(); i++) {
				Parada paradaObj = peregrino.getListaParadas().get(i);
				
				Element parada = documento.createElement("parada");
				
				Element orden = documento.createElement("orden");
				orden.setTextContent(String.valueOf(i+1));
				parada.appendChild(orden);
				
				Element nomParada = documento.createElement("nombre");
				nomParada.setTextContent(paradaObj.getNombre());
				parada.appendChild(nomParada);
				
				Element regParada = documento.createElement("region");
				regParada.setTextContent(String.valueOf(paradaObj.getRegion()));
				parada.appendChild(regParada);
				
				paradas.appendChild(parada);
			}
			carnet.appendChild(paradas);
			
			Element estancias = documento.createElement("estancias");
			
			DOMSource source = new DOMSource(documento);
			File fichero = new File("src/main/exportable/"+peregrino.getNombre()+".xml");
			StreamResult sr = new StreamResult(fichero);
			
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
