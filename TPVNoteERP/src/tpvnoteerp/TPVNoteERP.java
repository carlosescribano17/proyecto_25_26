/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpvnoteerp;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.UIManager;
import util.ConexionBD;
import util.InstaladorDb;
import view.LoginView1;

/**
 *
 * @author DAM2Alu7
 */
public class TPVNoteERP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(() -> {
            
            try {
                ConexionBD.getConexion();

            } catch (Exception e) {

                int option = javax.swing.JOptionPane.showConfirmDialog(
                    null,
                    "No se encontró la base de datos.\n¿Desea crearla automáticamente?",
                    "Instalación inicial",
                    javax.swing.JOptionPane.YES_NO_OPTION
                );

                if (option == javax.swing.JOptionPane.YES_OPTION) {
                    InstaladorDb.initDatabase();
                } else {
                    System.exit(0);
                }
            }
            
            LoginView1 lv = new LoginView1();
            lv.setVisible(true);
        });
    }
    
}
