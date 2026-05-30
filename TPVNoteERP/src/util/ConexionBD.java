/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author DAM2Alu7
 */
public class ConexionBD {
    private static final String URL_BASE = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "tpv_guitarras";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(
            URL_BASE + DB_NAME,
            USER,
            PASSWORD
        );
    }
    
    public static Connection getConexionSinBD() throws SQLException {
        return DriverManager.getConnection(
            URL_BASE,
            USER,
            PASSWORD
        );
    }
    
    public static Connection getConexionMultiQuery() throws SQLException {
    return DriverManager.getConnection(
        URL_BASE + DB_NAME + "?allowMultiQueries=true",
        USER,
        PASSWORD
    );
}
}
