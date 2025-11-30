/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.Producto;
import util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DAM2Alu10
 */
public class ProductoDAO {
    //actualizar un producto
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
    //sacar los nombres de las columnas
    public String[] obtenerNombresColumnas() {
        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS "
                   + "WHERE TABLE_SCHEMA = 'tpv_guitarras' "
                   + "AND TABLE_NAME = 'productos' "
                   + "ORDER BY ORDINAL_POSITION";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            java.util.List<String> columnas = new java.util.ArrayList<>();

            while (rs.next()) {
                columnas.add(rs.getString("COLUMN_NAME"));
            }

            return columnas.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
    //listar todos los productos de un tipo en concreto
    public List<Producto> listarPorTipo(String tipo) {
    List<Producto> productos = new ArrayList<>();
    String sql = "SELECT * FROM productos WHERE tipo_producto = ? AND activo = TRUE ORDER BY id_producto";
    
    try (Connection conn = ConexionBD.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, tipo);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            Producto p = new Producto();
            p.setId_producto(rs.getInt("id_producto"));
            p.setNombre(rs.getString("nombre"));
            p.setMarca(rs.getString("marca"));
            p.setPrecio(rs.getDouble("precio"));
            p.setStock(rs.getInt("stock"));
            p.setTipo_producto(rs.getString("tipo_producto"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setImagen_url(rs.getString("imagen_url"));
            p.setActivo(rs.getInt("activo"));
            p.setFecha_alta(rs.getTimestamp("fecha_alta"));
            
            productos.add(p);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, 
            "Error al listar productos por tipo: " + e.getMessage(),
            "Error BD", JOptionPane.ERROR_MESSAGE);
    }
    
    return productos;
}
    //listar todos los productos
    public List<Producto> listarTodos() {
    List<Producto> productos = new ArrayList<>();
    String sql = "SELECT * FROM productos WHERE activo = TRUE ORDER BY id_producto";
    
    try (Connection conn = ConexionBD.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            Producto p = new Producto();
            p.setId_producto(rs.getInt("id_producto"));
            p.setNombre(rs.getString("nombre"));
            p.setMarca(rs.getString("marca"));
            p.setPrecio(rs.getDouble("precio"));
            p.setStock(rs.getInt("stock"));
            p.setTipo_producto(rs.getString("tipo_producto"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setImagen_url(rs.getString("imagen_url"));
            p.setActivo(rs.getInt("activo"));
            p.setFecha_alta(rs.getTimestamp("fecha_alta"));
            productos.add(p);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, 
            "Error al listar productos: " + e.getMessage(),
            "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    
    return productos;
    }
    //borrar un producto
    public boolean borrar(int idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //crear nuevo producto
    public boolean crear(Producto p) {
        String sql = "INSERT INTO productos (nombre, marca, precio, stock, tipo_producto, descripcion, imagen_url, activo, fecha_alta) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, 1, NOW())";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getMarca());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getTipo_producto());
            ps.setString(6, p.getDescripcion());
            ps.setString(7, p.getImagen_url());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error al crear producto: " + e.getMessage(),
                "Error BD",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    
}
