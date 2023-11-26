import classes.data.DbOperations;
import classes.util.MongoDbHandler;
import classes.util.FileHandler;
import classes.util.PrintWithColor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PrintWithColor.printMessage("Bienvenido al multiservidor de datos");
        menu();
    }

    //First menu
    public static void menu() {

        String option;
        Scanner scanner = new Scanner(System.in);

        System.out.print("""
                ¿Que operación quiere realizar?
                1. Importar datos (archivo.[txt|xml] a sqlite)
                2. Exportar datos (sqlite a archivo.[txt|xml])
                3. Realizar backup de la base de datos (sqlite a MariaDb)
                4. Exportación hacia MongoDB (sqlite a JSON)
                5. Salir
                """);
        option = scanner.nextLine().trim();

        switch (option) {
            case "1" -> subMenu("Importar");
            case "2" -> subMenu("Exportar");
            case "3" -> DbOperations.insertDataToMariaDb();
            case "4" -> MongoDbHandler.exportToJson();
            case "5" -> {
                System.out.println("Saliendo...");
                System.exit(0);
            }
            default -> System.out.println("No existe la opción: " + option);
        }
        // Al acabar una operación sale el menu de nuevo
        menu();
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
            option = scanner.next().trim();


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