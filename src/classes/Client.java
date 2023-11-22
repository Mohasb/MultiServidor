package classes;

import org.w3c.dom.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Client {

    private String dni;
    private String name;
    private String lastname;
    private Date fechaNac;

    public static ArrayList<Client> clientsList = new ArrayList<>();
    static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");


    public Client() {
    }

    public Client(String dni, String name, String lastname, Date fechaNac) {
        this.dni = dni;
        this.name = name;
        this.lastname = lastname;
        this.fechaNac = fechaNac;
    }

    public static void AddClientsTxtToList(String[] datos){
        String dni = datos[0];
        String name = datos[1];
        String lastName = datos[2];
        String fechaNac = datos[3];

        Date date = null;
        try {
            date = dateFormatter.parse(datos[3]);
        } catch (ParseException e) {
            System.out.println("Se ha insertado un cliente con dni: " + dni + " con la fecha mal: " + fechaNac);
        }

        Client c = new Client(dni, name, lastName, date);
        Client.clientsList.add(c);
    }
    public static void AddClientsXmlToList(Element clientElement) {

        String dni = clientElement.getElementsByTagName("dni").item(0).getTextContent();
        String nombre = clientElement.getElementsByTagName("nombre").item(0).getTextContent();
        String apellido = clientElement.getElementsByTagName("apellido").item(0).getTextContent();
        String fechaNac = clientElement.getElementsByTagName("fechaNacimiento").item(0).getTextContent();

        Date date = null;
        try {
            date = dateFormatter.parse(fechaNac);
        } catch (ParseException e) {
            System.out.println("Se ha insertado un cliente con dni: " + dni + " con la fecha mal: " + fechaNac);
        }

        Client c = new Client(dni,nombre, apellido, date);
        Client.clientsList.add(c);
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

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "dni='" + dni + '\'' +
                ", nombre='" + name + '\'' +
                ", apellido='" + lastname + '\'' +
                ", fechaNac=" + fechaNac +
                '}';
    }
}
