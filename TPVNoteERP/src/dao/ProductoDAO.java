/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Producto;
import util.ConexionBD;

/**
 *
 * @author DAM2Alu10
 */
public class ProductoDAO {
    public Producto actualizar(Producto p){
        String sql = "UPDATE productos SET nombre=?, marca=?, precio=?, stock=?, tipo_producto=?, descripcion=?, imagen_url=?, activo=? WHERE id_producto=?";
        try(Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getMarca());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getTipo_producto());
            ps.setString(6, p.getDescripcion());
            ps.setString(7, p.getImagen_url());
            ps.setInt(8, p.getActivo());
            ps.setInt(9, p.getId_producto());
            
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return p;
    }
}
