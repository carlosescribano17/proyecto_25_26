/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import com.formdev.flatlaf.FlatLightLaf;
import dao.UsuarioDAO;

import javax.swing.*;
import model.Usuario;

/**
 *
 * @author DAM2Alu7
 */
public class LoginView extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnLogin;

    public LoginView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Login - NoteERP");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Inicio de sesión");
        lblTitulo.setBounds(110, 20, 150, 30);
        add(lblTitulo);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(50, 70, 80, 25);
        add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(130, 70, 150, 25);
        add(txtUsuario);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(50, 110, 80, 25);
        add(lblContrasena);

        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(130, 110, 150, 25);
        add(txtContrasena);

        btnLogin = new JButton("Entrar");
        btnLogin.setBounds(130, 160, 100, 30);
        add(btnLogin);

        btnLogin.addActionListener(e -> autenticar());
    }

    private void autenticar() {
        String user = txtUsuario.getText();
        String pass = new String(txtContrasena.getPassword());

        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = dao.autenticar(user, pass);

        if (u != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + u.getUsuario() + " (" + u.getRol() + ")");
            dispose(); // Cierra login
            // Aquí abrirías el menú principal (por ahora lo dejamos así)
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
