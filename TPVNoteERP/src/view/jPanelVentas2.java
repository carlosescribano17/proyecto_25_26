/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import dao.ClienteDAO;
import dao.LineaVentaDAO;
import dao.ProductoDAO;
import dao.VentaDAO;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.Cliente;
import model.LineaVenta;
import model.Usuario;
import model.Venta;

/**
 *
 * @author jorge
 */
public class jPanelVentas2 extends javax.swing.JPanel {

    /**
     * Creates new form jPanelVentas2
     */
    private CardLayout cardLayout;
    private JPanel panelVentana;
    private panelProductos pProductos;
    private panelCarrito pCarrito;
    private ClienteDAO cdao;
    private Usuario user;
    private ProductoDAO pdao;
    
    public jPanelVentas2(Usuario user, CardLayout cardLayout,JPanel panelVentana) {
        initComponents();
        
        this.user = user;
        this.cardLayout = cardLayout;
        this.panelVentana = panelVentana;
        
        pProductos = new panelProductos(this);
        jPanelProductos2.add(pProductos, BorderLayout.CENTER);
        
        pCarrito = new panelCarrito(this);
        jPanelCarrito2.add(pCarrito, BorderLayout.CENTER);
        
        cdao = new ClienteDAO();
        
//        List<Cliente> listaC = new ArrayList<>();
//        listaC = cdao.obtenerTodos();
        
        List<Cliente> listaC = cdao.obtenerTodos();
        
        for(Cliente c : listaC){
            jComboBoxClientes.addItem(c.getIdCliente() + "-" + c.getNombre() + " " +  c.getApellidos());
        }
        DarkThemeUtil.apply(this);
        revalidate();
        repaint();
    }
    
    public panelCarrito getCarritoPanel() {
        return pCarrito;
    }
    
    public void mostrarProductoSeleccionado(model.Producto producto) {
        if (producto == null) {
            limpiarImagenProducto();
            return;
        }
        mostrarImagenProducto(producto.getImagen_url(), producto.getNombre());
    }
    
    public void limpiarImagenProducto() {
        jLabelPreview.setIcon(null);
        jLabelPreview.setText("Selecciona un producto para ver su imagen");
    }
    
    private void mostrarImagenProducto(String rutaImagen, String nombreProducto) {
        jLabelPreview.setIcon(null);
        if (rutaImagen == null || rutaImagen.trim().isEmpty()) {
            jLabelPreview.setText("Sin imagen disponible");
            return;
        }
        try {
            ImageIcon icon;
            String ruta = rutaImagen.trim();
            if (ruta.startsWith("http://") || ruta.startsWith("https://")) {
                icon = new ImageIcon(new URL(ruta));
            } else {
                icon = new ImageIcon(new File(ruta).getAbsolutePath());
            }
            if (icon.getIconWidth() <= 0) {
                jLabelPreview.setText("No se pudo cargar la imagen");
                return;
            }
            Image escalada = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
            jLabelPreview.setIcon(new ImageIcon(escalada));
            jLabelPreview.setText("<html><div style='text-align:center;'>" + nombreProducto + "</div></html>");
            jLabelPreview.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jLabelPreview.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        } catch (Exception ex) {
            jLabelPreview.setText("No se pudo cargar la imagen");
        }
    }
    
    public void cambiarTotal(String total){
        jLabelTotal.setText(total+" €");
    }
    
    public String getClienteSeleccionado() {
        return jComboBoxClientes.getSelectedItem().toString();
    }
    
    public Usuario getUser(){
        return user;
    }
    
    public void hacerCompra(String s){
        double total = pCarrito.calcularTotalPedido();

        if (total <= 0) {
            JOptionPane.showMessageDialog(this,
                    "No se puede completar una venta de 0 €. Añade productos al carrito.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        // el empleado logeado que se pasa desde el main
        int idEmpleado = user.getId();

        // se recoge el cliente que se haya seleccionado
        int idCliente = 0;
        String cliente = getClienteSeleccionado();

        if (!cliente.equals("INVITADO")) {
            idCliente = Integer.parseInt(cliente.split("-")[0]);
        }

        Venta venta = new Venta();
        venta.setIdCliente(idCliente);
        venta.setIdEmpleado(idEmpleado);
        venta.setMetodoPago(s);
        venta.setDescuento(0);
        venta.setObservaciones(null);
        venta.setTotal(total);

        //se guarda la venta
        VentaDAO vdao = new VentaDAO();
        int idVentaGenerada = vdao.crearVenta(venta);

        if (idVentaGenerada <= 0) {
            JOptionPane.showMessageDialog(this, "Error guardando la venta en BD.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //se guardan las lineas de ventas por cada producto en el carrito
        LineaVentaDAO lvdao = new LineaVentaDAO();
        
        int error = 0;

        for (int i = 0; i < pCarrito.getModelo().getRowCount(); i++) {

            LineaVenta lv = new LineaVenta();
            lv.setIdVenta(idVentaGenerada);

            lv.setIdProducto((int) pCarrito.getModelo().getValueAt(i, 0));
            lv.setCantidad((int) pCarrito.getModelo().getValueAt(i, 3));
            lv.setPrecioUnitario((double) pCarrito.getModelo().getValueAt(i, 2));
            lv.setSubtotal((double) pCarrito.getModelo().getValueAt(i, 4));

            lvdao.crearLineaVenta(lv);
            
            pdao = new ProductoDAO();
            int id = (int) pCarrito.getModelo().getValueAt(i, 0);
            int stock  = pdao.getStock(id);
            int cantidad = (int) pCarrito.getModelo().getValueAt(i, 3);
            //System.out.println(id + "-" +  stock + "-" + cantidad);
            if (pdao.restarStock(id, cantidad, stock)){
                error = 1;
            }else {
                error = 2;
            }
        }
        if (error == 1) {
            JOptionPane.showMessageDialog(this, "Stock actualizado.", "CONFIRMACIÓN", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(error == 2) {
            JOptionPane.showMessageDialog(this, "Error al actualizar stock.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        pProductos.mostrarTipos();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelProductos2 = new javax.swing.JPanel();
        jPanelCarrito2 = new javax.swing.JPanel();
        btnVolver = new javax.swing.JButton();
        btnAnterior = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jPanelPreview = new javax.swing.JPanel();
        jLabelPreview = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jComboBoxClientes = new javax.swing.JComboBox<>();
        jButtonPagar = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));

        jPanelProductos2.setBackground(new java.awt.Color(102, 102, 102));
        jPanelProductos2.setLayout(new java.awt.BorderLayout());

        jPanelCarrito2.setLayout(new java.awt.BorderLayout());

        btnVolver.setText("Volver");
        btnVolver.addActionListener(this::btnVolverActionPerformed);

        btnAnterior.setText("<--");
        btnAnterior.addActionListener(this::btnAnteriorActionPerformed);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        jPanelPreview.setLayout(new java.awt.BorderLayout());

        jLabelPreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPreview.setText("Selecciona un producto para ver su imagen");
        jPanelPreview.add(jLabelPreview, java.awt.BorderLayout.CENTER);

        jLabelTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTotal.setText("0,00");

        jComboBoxClientes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "INVITADO" }));

        jButtonPagar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonPagar.setText("PAGAR");
        jButtonPagar.addActionListener(this::jButtonPagarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBoxClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(620, 620, 620)
                                .addComponent(jLabelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(77, 77, 77))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelProductos2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(142, 142, 142)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanelPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(139, 139, 139)
                        .addComponent(jPanelCarrito2, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(206, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnVolver)
                .addGap(128, 128, 128))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(210, 210, 210)
                        .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jPanelPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanelProductos2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanelCarrito2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(80, 80, 80)))))
                .addComponent(jComboBoxClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(177, 177, 177))
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(btnVolver)
                .addGap(50, 50, 50))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        cardLayout.show(panelVentana, "principal");
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        pProductos.mostrarTipos();
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        pCarrito.eliminarProducto();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void jButtonPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPagarActionPerformed
        double total = pCarrito.calcularTotalPedido();
        if (total <= 0) {
            JOptionPane.showMessageDialog(this,
                    "No se puede completar una venta de 0 €. Añade productos al carrito.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        JDialogPago jdp = new JDialogPago((Frame) SwingUtilities.getWindowAncestor(this), true, pCarrito , this);
        jdp.setVisible(true);
    }//GEN-LAST:event_jButtonPagarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JButton jButtonPagar;
    private javax.swing.JComboBox<String> jComboBoxClientes;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JLabel jLabelPreview;
    private javax.swing.JPanel jPanelCarrito2;
    private javax.swing.JPanel jPanelPreview;
    private javax.swing.JPanel jPanelProductos2;
    // End of variables declaration//GEN-END:variables
}
