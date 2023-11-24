package classes.data;

import util.PrintWithColor;

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
            System.out.println("Login correcto !!!");
            return conn;

        } catch (SQLException e) {
            PrintWithColor.print("Usuario o password incorrectos !!!\n", "red");
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
            System.out.println("Error al conectar a SqLite");
            throw new RuntimeException(e);
        }
    }

    // Cierra la conexión
    public static void cerrarConexion() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
