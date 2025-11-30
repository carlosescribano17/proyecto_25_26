package dao;

import java.sql.*;
import model.LineaVenta;
import util.ConexionBD;

public class LineaVentaDAO {
    //crear linea de venta por cada producto de una venta
    public boolean crearLineaVenta(LineaVenta lv) {
        String sql = "INSERT INTO lineas_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, lv.getIdVenta());
            ps.setInt(2, lv.getIdProducto());
            ps.setInt(3, lv.getCantidad());
            ps.setDouble(4, lv.getPrecioUnitario());
            ps.setDouble(5, lv.getSubtotal());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

