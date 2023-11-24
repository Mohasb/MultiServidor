package util;

import classes.Bill;
import classes.Client;

import java.io.*;
import java.text.DecimalFormat;

public class FileHandlerTxt {


    // This function reads a txt and save to local arraylist of clients and bills in his classes
    static void readTXT(File archivo) {
        if (archivo.exists()) {
            try {
                FileReader fr = new FileReader(archivo.getAbsoluteFile());
                BufferedReader br = new BufferedReader(fr);
                String linea;
                Client.clientsList.clear();
                Bill.billsList.clear();
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
                        Client.addClientsTxtToList(datos);
                    } else {
                        Bill.addBillsTxtToList(datos);
                    }
                }
                br.close();

            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("ERROR: El documento " + archivo.getName() + " no existe");
        }
    }
    static String generateTxt() {

        StringBuilder contentBuilder = new StringBuilder();

        for (Client client : Client.clientsList) {
            contentBuilder.append(client.getDni()).append(";").append(client.getName()).append(";").append(client.getLastname()).append(";").append(client.getFechaNac()).append("\n");

            for (Bill bill : client.getBills()) {
                contentBuilder.append(bill.getConcept()).append(";").append(bill.getPrice()).append(";").append(bill.getDate()).append(";").append(bill.getDniClient()).append("\n");
            }
        }

        return contentBuilder.toString();

    }

    static void saveTxtOrJson(File outputFilePath, String txtContent, String fileName, String extension) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(txtContent);

            PrintWithColor.print("\nNombre del archivo de la exportación: " + fileName, "green");
            PrintWithColor.print("Se ha creado el archivo "+ fileName+"[." + extension + "]", "green");
            PrintWithColor.print("Se han exportado un total de " + Client.clientsList.size() + " clientes y " + Bill.billsList.size() + " facturas", "green");

            double totalFacturas = 0d;
            DecimalFormat df = new DecimalFormat("0.00");
            for (Bill bill : Bill.billsList) {
                totalFacturas += bill.getPrice();
            }
            PrintWithColor.print("El coste total de las facturas asciende a " + df.format(totalFacturas) + "\n", "green");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
