import classes.Bill;
import classes.Client;
import classes.data.DbOperations;
import util.FileHandler;
import util.PrintWithColor;
import util.UpdateClient;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PrintWithColor.print("Bienvenido al multiservidor de datos\n", "blue");
        menu();
    }


    //First menu
    public static void menu() {

        String option;
        Scanner scanner = new Scanner(System.in);

        System.out.print("""
                ¿Que operación quiere realizar?
                1. Importar datos(archivo a sqlite)
                2. Exportar datos(sqlite a archivo)
                3. Realizar backup de la base de datos(sqlite a MariaDb)
                4. Exportación hacia MongoDB(JSON)
                5. Salir
                """);
        option = scanner.nextLine();

        switch (option) {
            case "1" -> subMenu("Importar");
            case "2" -> subMenu("Exportar");
            case "3" -> backUpSqliteToMariaDb();
            case "4" -> System.out.println("exportar a mongodb");
            case "5" -> {
                System.out.println("Saliendo...");
                System.exit(0);
            }
            default -> System.out.println("No existe la opción: " + option);
        }
        // Al acabar una operación sale el menu de nuevo
        menu();
    }

    private static void backUpSqliteToMariaDb() {


        UpdateClient.updateClientArrayFromSqLite();
        UpdateClient.updateBillsArrayFromSqLite();
        UpdateClient.mapClientsAndBills();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduce el usuario de MariaDb");
        String user = scanner.nextLine();
        System.out.println("Introduce el password de MariaDb");
        String password = scanner.nextLine();

        DbOperations.insertDataToMariaDb(user, password, Client.clientsList, Bill.billsList);
    }

    //Submenu of option 1 & 2
    public static void subMenu(String operation) {
        String option;
        Scanner scanner = new Scanner(System.in);

            System.out.print("""
                    ¿Que operación quiere realizar?
                    1.\040""" + operation + """
                     archivo de texto
                    2.\040""" + operation + """
                     archivo XML
                    3. Volver
                    """);
            option = scanner.next();


            if (operation.equals("Importar")) {
                switch (option) {
                    case "1" -> FileHandler.importFileToSqLite("txt");
                    case "2" -> FileHandler.importFileToSqLite("xml");
                    case "3" -> menu();
                    default -> System.out.println("No existe la opción: " + option);
                }
            }else {
                switch (option) {
                    case "1" -> FileHandler.exportSqliteToFile("txt");
                    case "2" -> FileHandler.exportSqliteToFile("xml");
                    case "3" -> menu();
                    default -> System.out.println("No existe la opción: " + option);
                }
            }
            // Al acabar una operación sale el menu de nuevo
            menu();
    }
}