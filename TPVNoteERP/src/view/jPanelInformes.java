/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import java.sql.Connection;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JComponent;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.ConexionBD;
import dao.ProductoDAO;
import java.util.List;
import model.Producto;

/**
 *
 * @author Jorge Moncada
 */
public class jPanelInformes extends javax.swing.JPanel {
    /**
     * Creates new form jPanelInformes
     */
    private JPanel panelVentana;
    private CardLayout cardLayout;
    private Connection con;
    private List<Producto> listaProductos;
    
    public jPanelInformes(CardLayout cardLayout, JPanel panelVentana) {
        initComponents();
        cargarProductos();
        configurarVistaUserFriendly();
        DarkThemeUtil.apply(this);
        
        this.cardLayout = cardLayout;
        this.panelVentana = panelVentana;
        try{
            this.con = ConexionBD.getConexion();
        }catch(Exception E){
            System.out.println(E);
        }
    }
    
    private void configurarVistaUserFriendly() {
        setLayout(new BorderLayout(24, 24));
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        
        JLabel titulo = new JLabel("Centro de Informes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        
        JLabel subtitulo = new JLabel("Genera informes de ventas y stock con un clic.");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        
        JPanel cabecera = new JPanel();
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
        cabecera.add(titulo);
        cabecera.add(Box.createRigidArea(new Dimension(0, 8)));
        cabecera.add(subtitulo);
        
        JPanel gridAcciones = new JPanel(new GridLayout(4, 2, 14, 14));
        gridAcciones.setPreferredSize(new Dimension(940, 280));
        
        btnVentasSemanales.setText("Ventas por semana");
        btnVentasProducto.setText("Ventas por producto");
        btnVentasClientes.setText("Ventas por clientes");
        btnVentasAnuales.setText("Ventas anuales");
        btnVentasCategoria.setText("Ventas por categoria");
        btnStockTotal.setText("Stock total");
        
        btnVentasSemanales.setToolTipText("Muestra la evolucion semanal de ventas.");
        btnVentasProducto.setToolTipText("Informe de ventas del producto seleccionado.");
        btnVentasClientes.setToolTipText("Resumen de compras por cliente.");
        btnVentasAnuales.setToolTipText("Totales de ventas del ano en curso.");
        btnVentasCategoria.setToolTipText("Comparativa de ventas por categoria.");
        btnStockTotal.setToolTipText("Inventario total disponible.");
        
        gridAcciones.add(btnVentasSemanales);
        gridAcciones.add(btnVentasClientes);
        
        JPanel panelProducto = new JPanel();
        panelProducto.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 6));
        
        btnVentasProducto.setPreferredSize(new Dimension(220, 52));
        btnVentasProducto.setFont(new Font("Segoe UI", Font.BOLD, 17));
        
        Dimension comboSize = new Dimension(260, 44);
        jComboBoxProductos.setPreferredSize(comboSize);
        jComboBoxProductos.setMinimumSize(comboSize);
        jComboBoxProductos.setMaximumSize(comboSize);
        jComboBoxProductos.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        jComboBoxProductos.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        
        panelProducto.add(btnVentasProducto);
        panelProducto.add(jComboBoxProductos);
        
        gridAcciones.add(panelProducto);
        gridAcciones.add(btnVentasAnuales);
        gridAcciones.add(btnVentasCategoria);
        gridAcciones.add(btnStockTotal);
        gridAcciones.add(new JLabel(""));
        gridAcciones.add(jButton1);
        
        JPanel izquierda = new JPanel(new BorderLayout(0, 18));
        izquierda.add(cabecera, BorderLayout.NORTH);
        izquierda.add(gridAcciones, BorderLayout.CENTER);
        
        JLabel tituloDerecha = new JLabel("Consejos Rapidos");
        tituloDerecha.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        JTextArea ayuda = new JTextArea();
        ayuda.setEditable(false);
        ayuda.setLineWrap(true);
        ayuda.setWrapStyleWord(true);
        ayuda.setOpaque(false);
        ayuda.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        ayuda.setText(
                "- Usa \"Ventas por producto\" para analizar un articulo concreto.\n\n"
                + "- \"Ventas por categoria\" te ayuda a decidir reposicion de stock.\n\n"
                + "- \"Stock total\" es ideal para revisiones rapidas antes de cerrar caja.\n\n"
                + "- Puedes abrir varios informes seguidos sin salir de esta pantalla."
        );
        
        JPanel derecha = new JPanel(new BorderLayout(0, 12));
        derecha.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        derecha.setPreferredSize(new Dimension(420, 0));
        derecha.add(tituloDerecha, BorderLayout.NORTH);
        derecha.add(ayuda, BorderLayout.CENTER);

        removeAll();
        add(izquierda, BorderLayout.CENTER);
        add(derecha, BorderLayout.EAST);
        
        revalidate();
        repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        btnVentasSemanales = new javax.swing.JButton();
        btnStockTotal = new javax.swing.JButton();
        btnVentasProducto = new javax.swing.JButton();
        btnVentasClientes = new javax.swing.JButton();
        btnVentasAnuales = new javax.swing.JButton();
        btnVentasCategoria = new javax.swing.JButton();
        jComboBoxProductos = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(153, 204, 255));
        setMaximumSize(null);

        jButton1.setText("Volver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnVentasSemanales.setText("Ventas Semanales");
        btnVentasSemanales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasSemanalesActionPerformed(evt);
            }
        });

        btnStockTotal.setText("Stock Total");
        btnStockTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockTotalActionPerformed(evt);
            }
        });

        btnVentasProducto.setText("Ventas Producto");
        btnVentasProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasProductoActionPerformed(evt);
            }
        });

        btnVentasClientes.setText("Ventas Clientes");
        btnVentasClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasClientesActionPerformed(evt);
            }
        });

        btnVentasAnuales.setText("Ventas Anuales");
        btnVentasAnuales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasAnualesActionPerformed(evt);
            }
        });

        btnVentasCategoria.setText("Ventas Categoria");
        btnVentasCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasCategoriaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnStockTotal)
                    .addComponent(btnVentasCategoria)
                    .addComponent(btnVentasAnuales)
                    .addComponent(btnVentasClientes)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnVentasProducto)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnVentasSemanales))
                .addContainerGap(773, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addComponent(btnVentasSemanales)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVentasProducto)
                    .addComponent(jComboBoxProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnVentasClientes)
                .addGap(18, 18, 18)
                .addComponent(btnVentasAnuales)
                .addGap(18, 18, 18)
                .addComponent(btnVentasCategoria)
                .addGap(18, 18, 18)
                .addComponent(btnStockTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 334, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cargarProductos() {
        try {
            ProductoDAO productoDAO = new ProductoDAO();
            listaProductos = productoDAO.listarTodos();
            jComboBoxProductos.removeAllItems();
            for (Producto p : listaProductos) {
                jComboBoxProductos.addItem(p.getNombre());
            }
            if (!listaProductos.isEmpty()) {
                jComboBoxProductos.setSelectedIndex(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }
    }
    
    public static void abrirInforme(String nombre, Map<String,Object> params, Connection con) {
        try {
            String rutaInforme = "Informes/" + nombre + ".jasper";
            File file = new File(rutaInforme);

            if (!file.exists()) {
                throw new RuntimeException("No se encontró el informe: " + rutaInforme);
            }

            InputStream is = new FileInputStream(file);

            if (params == null) {
                params = new java.util.HashMap<>();
            }

            JasperPrint jp = JasperFillManager.fillReport(is, params, con);
            JasperViewer.viewReport(jp, false);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al abrir el informe:\n" + e.getMessage());
        }
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        cardLayout.show(panelVentana, "principal");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnVentasSemanalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasSemanalesActionPerformed
        abrirInforme("ventas_por_semana",null, con);
    }//GEN-LAST:event_btnVentasSemanalesActionPerformed

    private void btnVentasProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasProductoActionPerformed
        int index = jComboBoxProductos.getSelectedIndex();
        if (index >= 0 && index < listaProductos.size()) {
            Producto productoSeleccionado = listaProductos.get(index);

            Map<String, Object> params = new HashMap<>();
            params.put("idProducto", productoSeleccionado.getId_producto());

            abrirInforme("ventas_por_producto", params, con);
        } else {
            JOptionPane.showMessageDialog(this, "No hay producto seleccionado.");
        }
    }//GEN-LAST:event_btnVentasProductoActionPerformed

    private void btnVentasClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasClientesActionPerformed
        abrirInforme("ventas_por_clientes",null, con);
    }//GEN-LAST:event_btnVentasClientesActionPerformed

    private void btnVentasAnualesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasAnualesActionPerformed
        Map<String, Object> params = new HashMap<>();
        params.put("anio", new java.sql.Timestamp(System.currentTimeMillis()));
        
        abrirInforme("ventas_anuales",params, con);
    }//GEN-LAST:event_btnVentasAnualesActionPerformed

    private void btnVentasCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasCategoriaActionPerformed
        abrirInforme("ventas_por_categoria",null, con);
    }//GEN-LAST:event_btnVentasCategoriaActionPerformed

    private void btnStockTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockTotalActionPerformed
        abrirInforme("stock_total",null, con);
    }//GEN-LAST:event_btnStockTotalActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnStockTotal;
    private javax.swing.JButton btnVentasAnuales;
    private javax.swing.JButton btnVentasCategoria;
    private javax.swing.JButton btnVentasClientes;
    private javax.swing.JButton btnVentasProducto;
    private javax.swing.JButton btnVentasSemanales;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBoxProductos;
    // End of variables declaration//GEN-END:variables
}
