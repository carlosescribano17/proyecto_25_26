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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Cliente;
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
                    rs.getString("rol"),
                               rs.getString("nombre"),
                               rs.getString("apellidos"),
                               rs.getTimestamp("fecha_alta")
                );
                }
                //hola buenas
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return usuario;
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM empleados WHERE id_empleado = ?";

        try (Connection conexion = ConexionBD.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public String[] obtenerNombresColumnas() {
        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS "
                   + "WHERE TABLE_SCHEMA = 'tpv_guitarras' "
                   + "AND TABLE_NAME = 'empleados' "
                   + "ORDER BY ORDINAL_POSITION";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            java.util.List<String> columnas = new java.util.ArrayList<>();

            while (rs.next()) {
                if(!rs.getString("COLUMN_NAME").equals("contrasena")&&!rs.getString("COLUMN_NAME").equals("activo")){
                    columnas.add(rs.getString("COLUMN_NAME"));
                }
                
            }

            return columnas.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
    
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id_empleado"));
        u.setUsuario(rs.getString("usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setApellidos(rs.getString("apellidos"));
        u.setRol(rs.getString("rol"));
        u.setFecha_alta(rs.getTimestamp("fecha_alta"));

        return u;
    }
    
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleados ORDER BY fecha_alta DESC";

        try (Connection conexion = ConexionBD.getConexion();
            Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
