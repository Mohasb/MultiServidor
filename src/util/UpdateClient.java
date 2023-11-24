package util;

import classes.Bill;
import classes.Client;
import classes.data.DbConnection;
import classes.data.DbOperations;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateClient {


    public static void updateClientArrayFromSqLite() {
        Client.clientsList.clear();
        // Get Resulset of client and send to client to save to arraylist<Clients>
        ResultSet clients = DbOperations.getDataFromDb(DbConnection.sqLiteConnection(),"clientes");
        Client.addClientsToList(clients);
    }

     public static void updateBillsArrayFromSqLite() {
        Bill.billsList.clear();
        // Get Resulset of Biils and send to Bill to save to arraylist<Bills>
        ResultSet bills = DbOperations.getDataFromDb(DbConnection.sqLiteConnection(),"facturas");
        Bill.addBillsFromSqLiteToList(bills);
    }


    public static void updateFromMariaDb() {
        Client.clientsList.clear();
        // Get Resulset of client and send to client to save to arraylist<Clients>
        ResultSet clients = DbOperations.getDataFromDb(DbConnection.mySqlConnection(),"clientes");
        Client.addClientsToList(clients);
    }


    public static void mapClientsAndBills() {
        //Map que contendrá la lsita de clientes con sus respectivas facturas
        Map<String, List<Bill>> clientBillsMap = new HashMap<>();

        // Iterate through bills and associate them with clients
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

}
