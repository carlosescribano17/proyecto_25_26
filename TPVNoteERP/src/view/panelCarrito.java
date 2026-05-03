/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import model.Producto;

/**
 *
 * @author jorge
 */
public class panelCarrito extends javax.swing.JPanel {

    /**
     * Creates new form panelCarrito
     */
    
    private List<Producto> carrito = new ArrayList<>();
    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private jPanelVentas2 parent;
    
    public panelCarrito(jPanelVentas2 parent) {
        this.parent = parent;
        initComponents();
        initCarritoUI();
        DarkThemeUtil.apply(this);
        
    }
    
     private void initCarritoUI() {

        modeloCarrito = new DefaultTableModel(new String[]{
            "ID", "Nombre", "Precio", "Cantidad", "Subtotal"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setLayout(new BorderLayout());
        add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

    }
    
    public void agregarProducto(Producto p) {
        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            int idExistente = (int) modeloCarrito.getValueAt(i, 0);

            if (idExistente == p.getId_producto()) {

                int cantidadActual = (int) modeloCarrito.getValueAt(i, 3);
                int nuevaCantidad = cantidadActual + 1;

                modeloCarrito.setValueAt(nuevaCantidad, i, 3);

                double precio = (double) modeloCarrito.getValueAt(i, 2);
                modeloCarrito.setValueAt(precio * nuevaCantidad, i, 4);

                DecimalFormat formato = new DecimalFormat("#,###.00");
                String total = formato.format(calcularTotalPedido());
                parent.cambiarTotal(total);
                
                return;
            }
        }

        // si no esta el producto en el carrito lo añadimos
        double subtotal = p.getPrecio() * 1;

        modeloCarrito.addRow(new Object[]{
            p.getId_producto(),
            p.getNombre(),
            p.getPrecio(),
            1,
            subtotal
        });

        carrito.add(p);
        
        DecimalFormat formato = new DecimalFormat("#,###.00");
        String total = formato.format(calcularTotalPedido());
        parent.cambiarTotal(total);
    }
    
    public void eliminarProducto() {
        int fila = tablaCarrito.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Eliminar este producto del carrito?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            modeloCarrito.removeRow(fila);
            carrito.remove(fila);
        }
        
        DecimalFormat formato = new DecimalFormat("#,###.00");
        String total = formato.format(calcularTotalPedido());
        parent.cambiarTotal(total);
    }
    

    public List<Producto> getCarrito() {
        List<Producto> lista = new ArrayList<>();

        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            
            int id = (int) modeloCarrito.getValueAt(i, 0);
            int cantidad = (int) modeloCarrito.getValueAt(i, 3);

            for (int c = 0; c < cantidad; c++) {
                lista.add(buscarProductoEnCarrito(id));
            }
        }
        return lista;
    }

    private Producto buscarProductoEnCarrito(int id) {
        for (Producto p : carrito) {
            if (p.getId_producto() == id) {
                return p;
            }
        }
        return null;
    }
    
    public double calcularTotalPedido() {
        double total = 0;

        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            double subtotal = (double) modeloCarrito.getValueAt(i, 4);
            total += subtotal;
        }

        return total;
    }
    
    public void vaciarCarrito() {
        modeloCarrito.setRowCount(0);
        carrito.clear();
        parent.cambiarTotal(String.valueOf(calcularTotalPedido()));
    }
    
    public DefaultTableModel getModelo() {
        return modeloCarrito;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
