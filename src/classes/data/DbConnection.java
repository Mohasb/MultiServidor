package classes.data;

import classes.util.PrintWithColor;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static Connection conn = null;

    //Retorna una conexión a mysql
    public static Connection mySqlConnection(String... args) {

        String url = "jdbc:mysql://localhost:3306/";
        String bd = "gestion";
        String user = (args.length > 0 ? args[0] : "root");
        String password = (args.length > 0 ? args[1] : "");

        try {
            conn = DriverManager.getConnection(url + bd, user, password);
            return conn;

        } catch (SQLException e) {
            PrintWithColor.printError("Usuario o password incorrectos !!!\n");
            return null;
        }
    }

    //Retorna una conexión a sqlite
    public static Connection sqLiteConnection(String... args) {
        File sqliteDb = new File("src/assets/database/gestion.db");
        String url = (args.length > 0 ? args[0] : "jdbc:sqlite:" + sqliteDb.getAbsolutePath());
        try {
            conn = DriverManager.getConnection(url);
            return conn;
        } catch (SQLException e) {
            PrintWithColor.printError("Error al conectar a SqLite");
            throw new RuntimeException(e);
        }
    }
}
