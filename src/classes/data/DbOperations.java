package classes.data;

import classes.Bill;
import classes.Client;
import util.PrintWithColor;
import util.UpdateClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class DbOperations {

    public static int saveClientsToDb(ArrayList<Client> clientesList, Connection connection) {
        int clientesInsertados = 0;
        StringBuilder values = new StringBuilder();
        //Copia del arraylist local para iterar
        ArrayList<Client> clientesFromFileCopy = new ArrayList<>(Client.clientsList);
        //Borro el arraylist localy lo relleno con lo que haya en la bbdd
        UpdateClient.updateClientArrayFromSqLite();
        //Creación del string con la query
        for (Client client : clientesFromFileCopy) {
            String dni = client.getDni();
            String nombre = client.getName();
            String apellido = client.getLastname();
            LocalDate fechaNac = client.getFechaNac();

            //Aqui se comprueba si existe el cliente en la basse de datos
            if (!Client.clientsList.contains(client)) {
                String clientValues = "('" + dni + "','" + nombre + "','" + apellido + "','" + fechaNac + "'),";
                values.append(clientValues);
                clientesInsertados++;
            } else {
                PrintWithColor.print("El cliente con dni:" + dni + " ya se encuentra en nuestra base de datos", "red");
                System.out.println("");
            }

        }

        // Se insertan solo los clientes qye no existen
        if (values.length() > 0) {
            String finalValues = values.substring(0, values.length() - 1);
            Statement st;
            try (connection) {
                st = connection.createStatement();
                String query = "INSERT INTO clientes VALUES " + finalValues;
                st.executeUpdate(query);
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return clientesInsertados;
    }

    public static int saveBillsToDb(ArrayList<Bill> billsList, Connection conn) {
        int facturasInsertadas = 0;
        StringBuilder values = new StringBuilder();

        UpdateClient.updateClientArrayFromSqLite();


        for (Bill bill : billsList) {
            String concepto = bill.getConcept();
            Double importe = bill.getPrice();
            Date fechaFactura = bill.getDate();
            String dni_cliente = bill.getDniClient();


            boolean dniExists = false;
            for (Client client : Client.clientsList) {
                //si dni del cliente == al dni del la factura ó el dni_cliente es null(por el xml)
                if (client.getDni().equals(dni_cliente) || dni_cliente == null) {
                    dniExists = true;
                    break;
                }
            }


            if (dniExists) {
                String billValues = "('" + concepto + "'," + importe + ",'" + fechaFactura + "','" + dni_cliente + "'),";
                values.append(billValues);
                facturasInsertadas++;
            } else {
                PrintWithColor.print("Las facturas del cliente con dni: "+ dni_cliente +" no corresponden a ningún cliente.", "red");

            }

        }

        if (values.length() > 0) {
            String finalValues = values.substring(0, values.length() - 1);
            Statement st;
            try (conn) {
                st = conn.createStatement();
                String query = "INSERT INTO facturas VALUES " + finalValues;
                st.executeUpdate(query);

            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return facturasInsertadas;
    }

    public static void createTablesIfNotExist(Connection conn, String sqlFileName) {
        try (conn) {
            Statement stmt = conn.createStatement();
            // Leer el archivo SQL
            StringBuilder sql = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader("src/assets/database/" + sqlFileName + ".sql"));
                String linea;
                while ((linea = br.readLine()) != null) {
                    // Agregar cada línea al StringBuilder
                    sql.append(linea).append("\n");
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo SQL: " + e.getMessage());
                throw new RuntimeException(e);
            }


            if (sqlFileName.equals("gestion")) {
                stmt.execute(String.valueOf(sql));
            }else {

            }


            // Cerrar la conexión
            conn.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error al ejecutar las sentencias SQL desde el archivo: " + e.getMessage());
        }
    }

    public static ResultSet getDataFromSqLite(String tableName) {

        Statement st;
        Connection connection = DbConnection.sqLiteConnection();
        ResultSet rs;
        try {
            st = connection.createStatement();
            String query = "SELECT * from " + tableName;
            rs = st.executeQuery(query);

        } catch (SQLException e) {
            System.out.println("Error al obtener los datos de: " + tableName);
            throw new RuntimeException(e.getMessage());
        }
        return rs;
    }

    public static void insertDataToMariaDb(String user, String password, ArrayList<Client> clientes, ArrayList<Bill> facturas) {
        //Tengo pasar una instancia a cada uno porque cuando cierro conexion luego para abrirla tendria que crear mas objetos y lo veo inecesareo
        createTablesIfNotExist(DbConnection.mySqlConnection(user, password), "gestionMariaDbTablas");

        //TODO
        //saveClientsToDb(clientes, DbConnection.mySqlConnection(user, password));
        //saveBillsToDb(facturas, DbConnection.mySqlConnection(user, password));
    }
}
