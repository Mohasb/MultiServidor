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
import java.util.ArrayList;
import java.util.Scanner;

public class DbOperations {

    public static int saveClientsToDb(ArrayList<Client> clientesList, Connection connection, String bdType) {
        int clientesInsertados = 0;
        StringBuilder values = new StringBuilder();
        //Copia del arraylist local para iterar
        ArrayList<Client> clientesFromFileCopy = new ArrayList<>(clientesList);
        //Borro el arraylist local lo relleno con lo que haya en la bbdd

        if (bdType.equals("sqlite")) {
            UpdateClient.updateClientArrayFromSqLite();
        }else {
            UpdateClient.updateFromMariaDb();
        }

        //Creación del string con la query
        for (Client client : clientesFromFileCopy) {
            String dni = client.getDni();
            String nombre = client.getName();
            String apellido = client.getLastname();
            String fechaNac = client.getFechaNac();
            //Aqui se comprueba si existe el cliente en la basse de datos
            if (!Client.clientsList.contains(client)) {
                String clientValues = "('" + dni + "','" + nombre + "','" + apellido + "','" + fechaNac + "'),";
                values.append(clientValues);
                clientesInsertados++;
            } else {
                PrintWithColor.print("El cliente con dni:" + dni + " ya se encuentra en nuestra base de datos\n", "red");
            }

        }

        // Se insertan solo los clientes que no existen
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
            String fechaFactura = bill.getDate();
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
                PrintWithColor.print("Las facturas del cliente con dni: "+ dni_cliente +" no corresponden a ningún cliente.\n", "red");

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

        if (conn != null) {
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

                //Este codigo ejecuta el sql en formato para sqlite o en líneas para MariaDb
                if (sqlFileName.equals("gestionSqLiteTables")) {
                    stmt.execute(String.valueOf(sql));
                }else {
                    String[] lines =  String.valueOf(sql).trim().split(";");
                    for (String line : lines) {
                        if (line.length() > 0) {
                            stmt.execute(line);
                        }
                    }
                }

                // Cerrar la conexión
                conn.close();
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error al ejecutar las sentencias SQL desde el archivo: " + e.getMessage());
            }
        }
    }

    public static ResultSet getDataFromDb(Connection conn, String tableName) {

        Statement st;
        ResultSet rs;
        try {
            st = conn.createStatement();
            String query = "SELECT * from " + tableName;
            rs = st.executeQuery(query);

        } catch (SQLException e) {
            System.out.println("Error al obtener los datos de: " + tableName);
            throw new RuntimeException(e.getMessage());
        }
        return rs;
    }

    public static void insertDataToMariaDb() {


        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el usuario de MariaDb");
        String user = sc.nextLine();
        System.out.println("Introduce el password de MariaDb");
        String password = sc.nextLine();

        try (Connection conn = DbConnection.mySqlConnection(user, password)) {
            if (conn != null) {
                System.out.println("Login correcto !!!");

                createTablesIfNotExist(conn, "gestionMariaDbTablas");
                UpdateClient.updateClientArrayFromSqLite();
                UpdateClient.updateBillsArrayFromSqLite();

                if (Client.clientsList.size() > 0 || Bill.billsList.size() > 0) {
                    int clientesInsertados = saveClientsToDb(Client.clientsList, DbConnection.mySqlConnection(user, password), "mariadb");
                    int facturasInsertadas = saveBillsToDb(Bill.billsList, DbConnection.mySqlConnection(user, password));
                    PrintWithColor.print("\nSe han insertado en la base de datos " + clientesInsertados + " clientes y " + facturasInsertadas + " facturas\n\n", "green");
                }else {
                    System.out.println("No hay clientes o facturas en la base de datos de sqlite");
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
