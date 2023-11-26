package classes;

import org.w3c.dom.Element;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Client {

    public static ArrayList<Client> clientsList = new ArrayList<>();
    private String dni;
    private String name;
    private String lastname;
    private String fechaNac;
    private List<Bill> bills;


    public Client() {
    }

    public Client(String dni, String name, String lastname, String fechaNac) {
        this.dni = dni;
        this.name = name;
        this.lastname = lastname;
        this.fechaNac = fechaNac;
    }

    public static void addClientsTxtToList(String[] datos) {
        String dni = datos[0];
        String name = datos[1];
        String lastName = datos[2];
        String fechaNac = datos[3];
        Client c = new Client(dni, name, lastName, fechaNac);
        Client.clientsList.add(c);
    }

    public static void addClientsXmlToList(Element clientElement) {

        String dni = clientElement.getElementsByTagName("dni").item(0).getTextContent();
        String nombre = clientElement.getElementsByTagName("nombre").item(0).getTextContent();
        String apellido = clientElement.getElementsByTagName("apellido").item(0).getTextContent();
        String fechaNac = clientElement.getElementsByTagName("fechaNacimiento").item(0).getTextContent();

        Client c = new Client(dni, nombre, apellido, fechaNac);
        Client.clientsList.add(c);
    }

    public static void addClientsToList(ResultSet clients) {
        try {
            while (clients.next()) {
                Client cli = new Client(clients.getString("dni"), clients.getString("nombre"), clients.getString("apellido"),  clients.getString("fechaNacimiento"));
                Client.clientsList.add(cli);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

    public String getLastname() {
        return lastname;
    }

    public void setApellido(String apellido) {
        this.lastname = apellido;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    @Override
    public String toString() {
        return "Cliente{" + "dni='" + dni + '\'' + ", nombre='" + name + '\'' + ", apellido='" + lastname + '\'' + ", fechaNac=" + fechaNac + '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Client otherClient = (Client) obj;
        return this.dni.equals(otherClient.getDni()) && Objects.equals(this.name, otherClient.name);
    }
}
