package util;

import classes.Bill;
import classes.Client;
import classes.data.DbConnection;
import classes.data.DbOperations;
import org.w3c.dom.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;

public class FileHandler {

    private static final JFileChooser chooser = new JFileChooser();

    public static void importFileToSqLite(String extension) {
        File archivo = importFile(extension);
        if (archivo != null) {
            readFile(archivo, extension);
            saveToSqLite(Client.clientsList, Bill.billsList);
        } else {
            System.out.println("Error no se ha seleccionado ningún archivo");
        }

    }

    private static void readFile(File archivo, String extension) {
        switch (extension) {
            case "txt" -> FileHandlerTxt.readTXT(archivo);
            case "xml" -> FileHandlerXml.readXML(archivo);
        }
    }

    private static void saveToSqLite(ArrayList<Client> clientes, ArrayList<Bill> bills) {

        // Create tables if not exists in db
        DbOperations.createTablesIfNotExist(DbConnection.sqLiteConnection(), "gestionSqLiteTables");
        // Save client to sqlite database
        int clientesInsertados = DbOperations.saveClientsToDb(clientes, DbConnection.sqLiteConnection(), "sqlite");
        // Save bills to sqlite database
        int facturasInsertadas = DbOperations.saveBillsToDb(bills, DbConnection.sqLiteConnection());
        PrintWithColor.print("\nSe han insertado en la base de datos " + clientesInsertados + " clientes y " + facturasInsertadas + " facturas\n\n", "green");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void exportSqliteToFile(String extension) {
        UpdateClient.updateClientArrayFromSqLite();
        UpdateClient.mapClientsAndBills();
        generateFileFromDataClients(extension);


    }

    private static void generateFileFromDataClients(String extension) {

        String txtContent = FileHandlerTxt.generateTxt();
        Document docXml = FileHandlerXml.generateXml();


        switch (extension) {
            case "txt" -> exportFile(txtContent, "txt");
            case "xml" -> exportFile(docXml, "xml");
        }
    }


    // This function prompt the dialog to choose the file to import
    public static File importFile(String extension) {

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos ." + extension, extension);
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(new File("src/assets/ejemplos"));
        System.out.println("Selecciona un archivo ." + extension + " del diálogo");
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            File selectedFile = chooser.getSelectedFile();


                String extensionSelected;
                String fileName = selectedFile.getName();
                int lastDotIndex = fileName.lastIndexOf('.');
                if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
                    extensionSelected = fileName.substring(lastDotIndex + 1).toLowerCase();
                    if (extension.equals(extensionSelected)) {
                        return chooser.getSelectedFile();
                    }else {
                        PrintWithColor.print("Error: el archivo seleccionado debia ser un ." + extension + " y es un archivo con extensión: \n" + extensionSelected, "red");
                    }
                }
        }
        return null;
    }

    // This function prompt the dialog to choose the folder to export
    public static <T> void exportFile(T content, String extension) {

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos ." + extension, extension);
        chooser.setFileFilter(filter);

        String userHome = System.getProperty("user.home");
        String desktopPath;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            desktopPath = userHome + "\\Desktop";
        } else if (os.contains("mac")) {
            desktopPath = userHome + "/Desktop";
        } else {
            desktopPath = userHome + "/Desktop";
        }
        chooser.setCurrentDirectory(new File(desktopPath));


        String fileName;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("¿Que nombre quieres para el archivo?");
            fileName = sc.nextLine();
        } while (fileName.isEmpty());


        chooser.setSelectedFile(new File(fileName));
        System.out.println("Selecciona una localización para guardar el archivo  ." + extension + " con el  diálogo");

        int seleccion = chooser.showSaveDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String folder = selectedFile.getParent();
            File outputFilePath = new File(folder, fileName + "." + extension);


            switch (extension) {
                case "txt" -> FileHandlerTxt.saveTxt(outputFilePath, (String) content, fileName, extension);
                case "xml" -> FileHandlerXml.saveXml(outputFilePath, (Document) content);
            }
        }

    }

}



