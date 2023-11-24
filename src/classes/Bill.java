package classes;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Bill {

    public static ArrayList<Bill> billsList = new ArrayList<>();
    private String concept;
    private Double price;
    private Date date;
    private String dniClient;
    static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");


    public Bill() {

    }

    public Bill(String concept, Double price, Date date, String dniClient) {
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

        Date date = null;
        try {
            date = dateFormatter.parse(datos[2]);
        } catch (ParseException e) {
            System.out.println("Se ha insertado una factura con la fecha mal: " + fechaFactura);
        }


        Bill b = new Bill(concepto, price, date, dniClient);
        Bill.billsList.add(b);
    }
    public static void addBillsXmlToList(NodeList facturas) {

        for (int i = 0; i < facturas.getLength(); i++) {
            Element facturaElement = (Element) facturas.item(i);

            String concepto = facturaElement.getElementsByTagName("concepto").item(0).getTextContent();
            String importeString = facturaElement.getElementsByTagName("importe").item(0).getTextContent();
            String fechaFactura = facturaElement.getElementsByTagName("fechaFactura").item(0).getTextContent();

            // Parse date
            Date date = null;
            try {
                date = dateFormatter.parse(fechaFactura);
            } catch (ParseException e) {
                System.out.println("Se ha insertado una factura con la fecha mal: " + fechaFactura);
            }
            // Parse double
            double importe = 0;
            try {
                importe = Double.parseDouble(importeString.replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Se ha insertado una factura con el precio mal: " + importeString);
            }
            Bill b = new Bill(concepto, importe, date, null);
            Bill.billsList.add(b);

        }

    }

    public static void addBillsFromSqLiteToList(ResultSet bills) {
        try {
            while (bills.next()) {
                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                Date date = null;
                try {
                    date = format.parse(bills.getString("fechaFactura"));
                } catch (ParseException e) {
                    System.out.println("Fecha obtenida de sqlite no válida");
                    e.printStackTrace();
                }

                Bill bi = new Bill(bills.getString("concepto"), bills.getDouble("importe"),  date, bills.getString("dni_cliente"));
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
