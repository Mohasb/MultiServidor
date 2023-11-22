package util;

import classes.Bill;
import classes.Client;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {

    private static final JFileChooser chooser = new JFileChooser();

    public static void importFileToSqLite(String extension) {
        File archivo = importFile(extension);
        if (archivo != null) {
            readFile(archivo, extension);
            saveToSqLite();
        } else {
            System.out.println("Error no se ha seleccionado archivo");
        }

    }

    private static void saveToSqLite() {
    }

    private static void readFile(File archivo, String extension) {
        switch (extension) {
            case "txt" -> readTXT(archivo);
            case "xml" -> readXML(archivo);
        }
    }

    private static void readXML(File archivo) {
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
                    Client.AddClientsXmlToList(clientElement);
                    // Añade las facturas del cliente al arraylist de facturas(Bill.billsList)
                    NodeList facturas = clientElement.getElementsByTagName("factura");
                    Bill.AddBillsXmlToList(facturas);

                }
            }

            System.out.println(Client.clientsList);
            System.out.println(Bill.billsList);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    //This function reads a txt and save to local arraylist of clients and bills in his classes
    private static void readTXT(File archivo) {
        if (archivo.exists()) {

            try {
                FileReader fr = new FileReader(archivo.getAbsoluteFile());
                BufferedReader br = new BufferedReader(fr);
                String linea;
                Client.clientsList.clear();
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split((";"));

                    boolean isClient;
                    try {
                        Double.parseDouble(datos[1].replace(',', '.'));
                        isClient = false;
                    } catch (NumberFormatException e) {
                        isClient = true;
                    }

                    if (isClient) {
                        Client.AddClientsTxtToList(datos);
                    } else {
                        Bill.AddBillsTxtToList(datos);
                    }
                }
                br.close();
                System.out.println(Client.clientsList);
                System.out.println(Bill.billsList);

            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("ERROR: El documento " + archivo.getName() + " no existe");
        }
    }

    public static File importFile(String extension) {

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos ." + extension, extension);
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(new File("src/assets/"));
        System.out.println("Selecciona un archivo ." + extension + " del diálogo");
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    public static void exportFile(String extension) {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos ." + extension, extension);
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(new File("src/assets/"));
        int seleccion = chooser.showSaveDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File fichero = chooser.getSelectedFile();
            System.out.println(fichero.getName());
        }
    }
}
