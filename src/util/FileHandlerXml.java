package util;

import classes.Bill;
import classes.Client;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class FileHandlerXml {

    // This function reads a xml and save to local arraylist of clients and bills in his classes
    static void readXML(File archivo) {
        Client.clientsList.clear();
        Bill.billsList.clear();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            //obtener XML
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(archivo.getAbsoluteFile());
            document.getDocumentElement().normalize();
            //  Get a Nodelist of clients
            NodeList client = document.getElementsByTagName("cliente");
            Client.clientsList.clear();
            for (int i = 0; i < client.getLength(); i++) {
                Node clientNode = client.item(i);
                Element clientElement = (Element) clientNode;
                if (clientElement.getNodeType() == Node.ELEMENT_NODE) {
                    //Añade al cliente  al arraylist de clientes(Client.clientList)
                    Client.addClientsXmlToList(clientElement);
                    // Añade las facturas del cliente al arraylist de facturas(Bill.billsList)
                    NodeList facturas = clientElement.getElementsByTagName("factura");

                    String dni = clientElement.getElementsByTagName("dni").item(0).getTextContent();

                    Bill.addBillsXmlToList(facturas, dni);
                }
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
    static Document generateXml() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        DOMImplementation implementation = builder.getDOMImplementation();
        Document document = implementation.createDocument(null, "clientes", null);
        document.setXmlVersion("1.0");

        for (Client cliente : Client.clientsList) {
            Element clienteElement = document.createElement("cliente");

            Element dni = document.createElement("dni");
            dni.setTextContent(cliente.getDni());

            Element nombre = document.createElement("nombre");
            nombre.setTextContent(cliente.getName());

            Element apellido = document.createElement("apellido");
            apellido.setTextContent(cliente.getName());

            Element fechaNacimiento = document.createElement("fecha_nacimineto");
            fechaNacimiento.setTextContent(cliente.getFechaNac());


            clienteElement.appendChild(dni);
            clienteElement.appendChild(nombre);
            clienteElement.appendChild(apellido);
            clienteElement.appendChild(fechaNacimiento);

            Element facturas = document.createElement("facturas");

            for (Bill fa : cliente.getBills()) {

                Element factura = document.createElement("factura");


                Element concepto = document.createElement("concepto");
                concepto.setTextContent(fa.getConcept());

                Element importe = document.createElement("importe");
                importe.setTextContent(fa.getPrice().toString());

                Element fechaFactura = document.createElement("fechaFactura");
                fechaFactura.setTextContent(fa.getDate());
                System.out.println(fechaFactura.getTextContent());

                Element dni_cliente = document.createElement("dniCliente");
                dni_cliente.setTextContent(cliente.getDni());

                factura.appendChild(concepto);
                factura.appendChild(importe);
                factura.appendChild(fechaFactura);


                facturas.appendChild(factura);


            }
            clienteElement.appendChild(facturas);
            document.getDocumentElement().appendChild(clienteElement);
        }
        return document;
    }

    static void saveXml(File outputFilePath, Document docXml) {
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Source source = new DOMSource(docXml);
            Result result = new StreamResult(new File(outputFilePath.toURI()));
            transformer.transform(source, result);
            System.out.println("Documento XML creado correctamente a partir de los datos de la base de datos gestión en SqLite" + "\n");
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
