/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import model.Usuario;
import util.ConexionBD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author DAM2Alu7
 */
public class UsuarioDAO {
    public Usuario autenticar(String nombreUsuario, String contrasena) {
        Usuario usuario = null;
        String sql = "SELECT * FROM empleados WHERE usuario=?";

        try (Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nombreUsuario);
            //ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                if(BCrypt.checkpw(contrasena,rs.getString("contrasena"))){
                       usuario = new Usuario(
                    rs.getInt("id_empleado"),
                    rs.getString("usuario"),
                    rs.getString("contrasena"),
                    rs.getString("rol")
                );
                }
                //hola buenas
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }
}
