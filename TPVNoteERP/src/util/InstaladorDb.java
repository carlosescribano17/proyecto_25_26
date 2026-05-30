package util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.io.ByteArrayOutputStream;

public class InstaladorDb {

    public static void initDatabase() {
        try (Connection con = ConexionBD.getConexionSinBD();
             Statement st = con.createStatement()) {

            st.executeUpdate("CREATE DATABASE IF NOT EXISTS tpv_guitarras "
                    + "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar a MySQL o crear la base de datos.\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
        try (Connection con = ConexionBD.getConexionMultiQuery();
            Statement st = con.createStatement()) {

           InputStream is = InstaladorDb.class.getResourceAsStream("/database/tpv_guitarras.sql");
           if (is == null) {
               JOptionPane.showMessageDialog(null,
                   "No se encuentra el script SQL dentro del JAR.",
                   "Error", JOptionPane.ERROR_MESSAGE);
               return;
           }

           ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String sql = result.toString(StandardCharsets.UTF_8.name());
            st.execute(sql);

            JOptionPane.showMessageDialog(null,
               "Base de datos creada correctamente.",
               "OK", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
           JOptionPane.showMessageDialog(null,
               "Error al ejecutar el script SQL.\n" + e.getMessage(),
               "Error", JOptionPane.ERROR_MESSAGE);
           e.printStackTrace();
        }
    }
}