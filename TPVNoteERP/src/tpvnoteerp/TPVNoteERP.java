/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpvnoteerp;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.UIManager;
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
        // TODO code application logic here
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear y mostrar el login en el Event Dispatch Thread
        java.awt.EventQueue.invokeLater(() -> {
            LoginView1 lv = new LoginView1();
            lv.setVisible(true);
        });
    }
    
}
