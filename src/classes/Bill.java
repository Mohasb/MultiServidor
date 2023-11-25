package classes;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Bill {

    public static ArrayList<Bill> billsList = new ArrayList<>();
    private String concept;
    private Double price;
    private String date;
    private String dniClient;


    public Bill() {

    }

    public Bill(String concept, Double price, String date, String dniClient) {
        this.concept = concept;
        this.price = price;
        this.date = date;
        this.dniClient = dniClient;
    }

    public static void addBillsTxtToList(String[] datos) {
        String concepto = datos[0];
        Double price = Double.parseDouble(datos[1].replace(',', '.'));
        String fechaFactura = datos[2];
        String dniClient = datos[3];


        Bill b = new Bill(concepto, price, fechaFactura, dniClient);
        Bill.billsList.add(b);
    }
    public static void addBillsXmlToList(NodeList facturas, String dniFactura) {

        for (int i = 0; i < facturas.getLength(); i++) {
            Element facturaElement = (Element) facturas.item(i);

            String concepto = facturaElement.getElementsByTagName("concepto").item(0).getTextContent();
            String importeString = facturaElement.getElementsByTagName("importe").item(0).getTextContent();
            String fechaFactura = facturaElement.getElementsByTagName("fechaFactura").item(0).getTextContent();

            // Parse double
            double importe = 0;
            try {
                importe = Double.parseDouble(importeString.replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Se ha insertado una factura con el precio mal: " + importeString);
            }
            Bill b = new Bill(concepto, importe, fechaFactura, dniFactura);
            Bill.billsList.add(b);

        }

    }

    public static void addBillsFromSqLiteToList(ResultSet bills) {
        try {
            while (bills.next()) {
                Bill bi = new Bill(bills.getString("concepto"), bills.getDouble("importe"),  bills.getString("fechaFactura"), bills.getString("dni_cliente"));
                Bill.billsList.add(bi);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public String getConcept() {
        return concept;
    }

    public void setConcept(String name) {
        this.concept = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDniClient() {
        return dniClient;
    }

    public void setDniClient(String dniClient) {
        this.dniClient = dniClient;
    }

    @Override
    public String toString() {
        return "Bill{" + "name='" + concept + '\'' + ", price=" + price + ", date=" + date + ", dniClient='" + dniClient + '\'' + '}';
    }
}
