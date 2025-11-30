package dao;

import java.sql.*;
import util.ConexionBD;
import model.Venta;

public class VentaDAO {
    //crear una venta nueva
    public int crearVenta(Venta v) {
        String sql = "INSERT INTO ventas (id_cliente, id_empleado, total, metodo_pago, descuento, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setObject(1, v.getIdCliente() == 0 ? null : v.getIdCliente(), java.sql.Types.INTEGER);
            ps.setInt(2, v.getIdEmpleado());
            ps.setDouble(3, v.getTotal());
            ps.setString(4, v.getMetodoPago());
            ps.setDouble(5, v.getDescuento());
            ps.setString(6, v.getObservaciones());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}

