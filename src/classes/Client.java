package classes;

import org.w3c.dom.Element;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Client {

    public static ArrayList<Client> clientsList = new ArrayList<>();
    static DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private String dni;
    private String name;
    private String lastname;
    private LocalDate fechaNac;
    private List<Bill> bills;


    public Client() {
    }

    public Client(String dni, String name, String lastname, LocalDate fechaNac) {
        this.dni = dni;
        this.name = name;
        this.lastname = lastname;
        this.fechaNac = fechaNac;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public static void addClientsTxtToList(String[] datos) {
        String dni = datos[0];
        String name = datos[1];
        String lastName = datos[2];
        String fechaNac = datos[3];
        LocalDate fecha = LocalDate.parse(fechaNac, formato);
        Client c = new Client(dni, name, lastName, fecha);
        Client.clientsList.add(c);
    }

    public static void addClientsXmlToList(Element clientElement) {

        String dni = clientElement.getElementsByTagName("dni").item(0).getTextContent();
        String nombre = clientElement.getElementsByTagName("nombre").item(0).getTextContent();
        String apellido = clientElement.getElementsByTagName("apellido").item(0).getTextContent();
        String fechaNac = clientElement.getElementsByTagName("fechaNacimiento").item(0).getTextContent();
        LocalDate fecha = LocalDate.parse(fechaNac, formato);

        Client c = new Client(dni, nombre, apellido, fecha);
        Client.clientsList.add(c);
    }

    public static void addClientsToList(ResultSet clients) {
        try {
            while (clients.next()) {
                LocalDate fecha = LocalDate.parse(clients.getString("fechaNacimiento"));
                Client cli = new Client(clients.getString("dni"), clients.getString("nombre"), clients.getString("apellido"), fecha);
                Client.clientsList.add(cli);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public LocalDate getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(LocalDate fechaNac) {
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
