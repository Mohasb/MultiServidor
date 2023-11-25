package classes.util;

import classes.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDbHandler {

    public static void exportToJson() {
        UpdateClient.updateClientArrayFromSqLite();
        UpdateClient.updateBillsArrayFromSqLite();
        UpdateClient.mapClientsAndBills();

        if (Client.clientsList.size() > 0) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setPrettyPrinting().create();
            String JSONObject = gson.toJson(Client.clientsList);
            FileHandler.exportFile(JSONObject, "json");
            exportToMongoDb(JSONObject);
        }else {
            PrintWithColor.printError("No hay clientes en la base de datos gestion de SqLite");
        }
    }

    //TODO:Cuando haya tiempo
    public static void exportToMongoDb(String JsonObject) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://localhost:27017/"))
                .build();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase("gestion");
            MongoCollection<Document> collection = database.getCollection("clientes");
            /*Document bsonDocument  = Document.parse(JsonObject);
            collection.insertOne(bsonDocument);*/

            System.out.println("JSON document inserted successfully!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
