import util.FileHandler;
import util.PrintWithColor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PrintWithColor.print("Bienvenido al multiservidor de datos", "blue");
        menu();
    }


    //First menu
    public static void menu() {

        String option;
        Scanner scanner = new Scanner(System.in);

        System.out.print("""
                ¿Que operación quiere realizar?
                1. Importar datos
                2. Exportar datos
                3. Realizar backup de la base de datos
                4. Exportación hacia MongoDB(JSON)
                5. Salir
                """);
        option = scanner.nextLine();

        switch (option) {
            case "1" -> subMenu("Importar");
            case "2" -> subMenu("Exportar");
            case "3" -> System.out.println("backup bbdd");
            case "4" -> System.out.println("exportar a mongodb");
            case "5" -> {
                System.out.println("Saliendo...");
                System.exit(0);
            }
            default -> System.out.println("No existe la opción: " + option);
        }

    }

    //Submenu of option 1 & 2
    public static void subMenu(String operation) {
        String option;
        Scanner scanner = new Scanner(System.in);

            System.out.print("""
                    ¿Que operación quiere realizar?
                    1.\040""" + operation + """
                     desde archivo de texto
                    2.\040""" + operation + """
                     desde archivo XML
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
                    case "1" -> FileHandler.exportFile("txt");
                    case "2" -> FileHandler.exportFile("xml");
                    case "3" -> menu();
                    default -> System.out.println("No existe la opción: " + option);
                }
            }
            // Al acabar una operación sale el menu de nuevo
            menu();
    }
}