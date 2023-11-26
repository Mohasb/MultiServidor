package classes.util;

import classes.Bill;
import classes.Client;
import classes.data.DbConnection;
import classes.data.DbOperations;

import java.sql.ResultSet;
import java.util.*;

public class UpdateClient {


    public static void updateClientArrayFromSqLite() {
        Client.clientsList.clear();
        // Get Result of client and send to client to save to arraylist<Clients>
        ResultSet clients = DbOperations.getDataFromDb(DbConnection.sqLiteConnection(),"clientes");
        Client.addClientsToList(clients);
    }
     public static void updateBillsArrayFromSqLite() {
        Bill.billsList.clear();
        // Get Result of Bills and send to Bill to save to arraylist<Bills>
        ResultSet bills = DbOperations.getDataFromDb(DbConnection.sqLiteConnection(),"facturas");
        Bill.addBillsFromSqLiteToList(bills);
    }

    public static void mapClientsAndBills() {
        //Map que contendrá la lista de clientes con sus respectivas facturas
        Map<String, List<Bill>> clientBillsMap = new HashMap<>();

        for (Bill bill : Bill.billsList) {
            String dni = bill.getDniClient();
            List<Bill> clientBills = clientBillsMap.getOrDefault(dni, new ArrayList<>());
            clientBills.add(bill);
            clientBillsMap.put(dni, clientBills);
        }

        // Associate clients with their bills
        for (Client client : Client.clientsList) {
            String dni = client.getDni();
            List<Bill> associatedBills = clientBillsMap.getOrDefault(dni, new ArrayList<>());
            // Assign the list of bills to the client
            client.setBills(associatedBills);
        }
    }

    public static void updateFromMariaDb() {
        Client.clientsList.clear();
        // Get Result of client and send to client to save to arraylist<Clients>
        ResultSet clients = DbOperations.getDataFromDb(Objects.requireNonNull(DbConnection.mySqlConnection()),"clientes");
        Client.addClientsToList(clients);
    }

}
