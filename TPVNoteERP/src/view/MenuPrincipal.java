/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.google.gson.Gson;
import dao.ClienteDAO;
import dao.ProductoDAO;
import dao.UsuarioDAO;
import java.awt.CardLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JFrame;
import javax.swing.UIManager;
import model.Producto;
import model.Usuario;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import model.Cliente;

/**
 *
 * @author DAM2Alu10
 */
public class MenuPrincipal extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MenuPrincipal.class.getName());
    
    private CardLayout cardLayout;
    private Usuario userActual;
    DefaultTableModel dtm;
    DefaultTableModel dtmc;
    DefaultTableModel dtmu;
    ProductoDAO pdao = new ProductoDAO();
    ClienteDAO cdao = new ClienteDAO();
    UsuarioDAO udao = new UsuarioDAO();
    private boolean comboListo = false;
    private java.util.Set<Integer> filasModificadas = new java.util.HashSet<>();
    private java.util.Set<Integer> filasModificadasCliente = new java.util.HashSet<>();
    private java.util.Set<Integer> filasModificadasUsuario = new java.util.HashSet<>();
    private boolean cargandoTablas = false;

    /**
     * Creates new form MenuPrincipal
     */
    public MenuPrincipal(Usuario user) {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        this.userActual = user;
        
        if(!userActual.getRol().equalsIgnoreCase("ADMINISTRADOR")){
            btnArticulos.setEnabled(false);
            btnInformes.setEnabled(false);
            btnUsuarios.setEnabled(false);
        }
        
        cardLayout = (CardLayout) jPanelVentanaUnica.getLayout();
        cardLayout.show(jPanelVentanaUnica, "card2");
        
        jLabelUsuario.setText("Bienvenid@, "+userActual.getUsuario());
        
        dtm = new DefaultTableModel(pdao.obtenerNombresColumnas(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        
        dtmc = new DefaultTableModel(cdao.obtenerNombresColumnas(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        
        dtmu = new DefaultTableModel(udao.obtenerNombresColumnas(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        
        jPanelVentas2 panel = new jPanelVentas2(userActual, cardLayout, jPanelVentanaUnica);
        jPanelVentanaUnica.add(panel, "ventas2");
        
        
        jTableUsuarios.setModel(dtmu);
        jTableClientes.setModel(dtmc);
        jTableProductos.setModel(dtm);
        dtm.addTableModelListener(e -> { //habilita el botón en cuanto haya un cambio en la tabla
            if (cargandoTablas) return;
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int fila = e.getFirstRow();

                filasModificadas.add(fila);

                if (filasModificadas.size() == 1) {
                    jButtonModificar.setEnabled(true);
                } else {
                    jButtonModificar.setEnabled(false);
                    JOptionPane.showMessageDialog(this,
                            "Solo puedes modificar una fila a la vez.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    cargarProductosPorTipo();        
                    }
            }
        });
        dtmc.addTableModelListener(e -> { //habilita el botón en cuanto haya un cambio en la tabla
            if (cargandoTablas) return;
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int fila = e.getFirstRow();

                filasModificadasCliente.add(fila);

                if (filasModificadasCliente.size() == 1) {
                    jButtonModificarClient.setEnabled(true);
                } else {
                    jButtonModificarClient.setEnabled(false);
                    JOptionPane.showMessageDialog(this,
                            "Solo puedes modificar una fila a la vez.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    cargarClientes();        
                    }
            }
        });
        jTableProductos.setAutoCreateRowSorter(false);
        jTableClientes.setAutoCreateRowSorter(false);
        jTableUsuarios.setAutoCreateRowSorter(false);
        dtm.setColumnIdentifiers(pdao.obtenerNombresColumnas());
        dtmc.setColumnIdentifiers(cdao.obtenerNombresColumnas());
        dtmu.setColumnIdentifiers(udao.obtenerNombresColumnas());
        jButtonModificar.setEnabled(false);
        jButtonModificarClient.setEnabled(false);
        comboListo = true;
    }

    public void cargarProductosPorTipo() {
        cargandoTablas = true;
        // limpiar la tabla        
        dtm.setRowCount(0);

        String tipoSeleccionado = (String) jComboBoxTipoProducto.getSelectedItem();

        java.util.List<Producto> productos;
        if ("TODOS".equals(tipoSeleccionado)) {
            productos = pdao.listarTodos();
        } else {
            productos = pdao.listarPorTipo(tipoSeleccionado);
        }

        // agregar productos a la tabla
        for (Producto p : productos) {
            Object[] fila = new Object[]{
                p.getId_producto(),
                p.getNombre(),
                p.getMarca(),
                p.getPrecio(),
                p.getStock(),
                p.getTipo_producto(),
                p.getDescripcion(),
                p.getImagen_url(),
                p.getActivo(),
                p.getFecha_alta()
            };
            dtm.addRow(fila);
        }
        filasModificadas.clear();
        jButtonModificar.setEnabled(false);
        cargandoTablas = false;
    }
    
    public void cargarUsuarios(){
        dtmu.setRowCount(0);
        
        List<Usuario> usuarios = udao.obtenerTodos();
        
        for(Usuario u : usuarios){
            Object[] fila = new Object[] {
              u.getId(),
                u.getUsuario(),
                u.getNombre(),
                u.getApellidos(),
                u.getRol(),
                u.getFecha_alta()
            };
            
            dtmu.addRow(fila);
        }
    }
    
    public void cargarClientes() {
        cargandoTablas = true;
        // limpiar la tabla
        dtmc.setRowCount(0);

        List<Cliente> clientes = cdao.obtenerTodos();

        // agregar clientes a la tabla
        for (Cliente c : clientes) {
            Object[] fila = new Object[]{
                c.getIdCliente(),
                c.getDni(),
                c.getNombre(),
                c.getApellidos(),
                c.getTelefono(),
                c.getEmail(),
                c.getDireccion(),
                c.getFecha_alta()
            };
            dtmc.addRow(fila);
        }
        cargandoTablas = false;

        filasModificadasCliente.clear();
        jButtonModificarClient.setEnabled(false);

        jTableClientes.revalidate();
        jTableClientes.repaint();
    }
    
    public void cargarJson(File f){
        Gson gson = new Gson();
        try {
            String fichero = new String(Files.readAllBytes(Paths.get(f.getPath())));
            List<Producto> productos = Arrays.asList(gson.fromJson(fichero, Producto[].class));
            for(Producto p : productos){
                pdao.crear(p);
            }
            JOptionPane.showMessageDialog(this, "Articulos cargados correctamente", "Insertar Articulos", JOptionPane.INFORMATION_MESSAGE);
            cargarProductosPorTipo();
        } catch (IOException ex) {
            System.getLogger(MenuPrincipal.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelVentanaUnica = new javax.swing.JPanel();
        jPanelPrincipal = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnVentas = new javax.swing.JButton();
        btnArticulos = new javax.swing.JButton();
        btnClientes = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnInformes = new javax.swing.JButton();
        btnUsuarios = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jLabelUsuario = new javax.swing.JLabel();
        jPanelClientes = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnVolverClientes = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableClientes = new javax.swing.JTable();
        jButtonModificarClient = new javax.swing.JButton();
        jButtonNuevoClient = new javax.swing.JButton();
        jButtonBorrarClient = new javax.swing.JButton();
        jPanelArticulos = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnVolverArticulos = new javax.swing.JButton();
        jButtonBorrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProductos = new javax.swing.JTable();
        jComboBoxTipoProducto = new javax.swing.JComboBox<>();
        jButtonModificar = new javax.swing.JButton();
        jButtonNuevo = new javax.swing.JButton();
        btnJson = new javax.swing.JButton();
        jPanelInformes = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnVolverStock = new javax.swing.JButton();
        jPanelUsuarios = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnVolverUsuarios = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableUsuarios = new javax.swing.JTable();
        jButtonBorrarUsuario = new javax.swing.JButton();
        jButtonNuevoUsuario = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TPV NoteERP");

        jPanelVentanaUnica.setLayout(new java.awt.CardLayout());

        jPanel1.setLayout(new java.awt.GridLayout(3, 2, 0, 70));

        btnVentas.setText("VENTAS");
        btnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasActionPerformed(evt);
            }
        });
        jPanel1.add(btnVentas);

        btnArticulos.setText("ARTICULOS");
        btnArticulos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArticulosActionPerformed(evt);
            }
        });
        jPanel1.add(btnArticulos);

        btnClientes.setText("CLIENTES");
        btnClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesActionPerformed(evt);
            }
        });
        jPanel1.add(btnClientes);

        jPanel2.setLayout(new java.awt.GridLayout(3, 2, 0, 70));

        btnInformes.setText("INFORMES");
        btnInformes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInformesActionPerformed(evt);
            }
        });
        jPanel2.add(btnInformes);

        btnUsuarios.setText("USUARIOS");
        btnUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuariosActionPerformed(evt);
            }
        });
        jPanel2.add(btnUsuarios);

        btnSalir.setText("SALIR");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel2.add(btnSalir);

        jLabelUsuario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelUsuario.setForeground(new java.awt.Color(238, 238, 238));
        jLabelUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUsuario.setText("Bienvenid@, ");

        javax.swing.GroupLayout jPanelPrincipalLayout = new javax.swing.GroupLayout(jPanelPrincipal);
        jPanelPrincipal.setLayout(jPanelPrincipalLayout);
        jPanelPrincipalLayout.setHorizontalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPrincipalLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 964, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jLabelUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 771, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 964, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );
        jPanelPrincipalLayout.setVerticalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(jLabelUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 954, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );

        jPanelVentanaUnica.add(jPanelPrincipal, "principal");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("CLIENTES");

        btnVolverClientes.setText("Volver");
        btnVolverClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverClientesActionPerformed(evt);
            }
        });

        jTableClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTableClientes);

        jButtonModificarClient.setText("Modificar");
        jButtonModificarClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarClientActionPerformed(evt);
            }
        });

        jButtonNuevoClient.setText("Nuevo");
        jButtonNuevoClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevoClientActionPerformed(evt);
            }
        });

        jButtonBorrarClient.setText("Borrar");
        jButtonBorrarClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrarClientActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelClientesLayout = new javax.swing.GroupLayout(jPanelClientes);
        jPanelClientes.setLayout(jPanelClientesLayout);
        jPanelClientesLayout.setHorizontalGroup(
            jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientesLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelClientesLayout.createSequentialGroup()
                .addContainerGap(2144, Short.MAX_VALUE)
                .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelClientesLayout.createSequentialGroup()
                        .addComponent(btnVolverClientes)
                        .addGap(68, 68, 68))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelClientesLayout.createSequentialGroup()
                        .addComponent(jButtonNuevoClient)
                        .addGap(55, 55, 55)
                        .addComponent(jButtonModificarClient)
                        .addGap(62, 62, 62)
                        .addComponent(jButtonBorrarClient)
                        .addGap(269, 269, 269))))
            .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelClientesLayout.createSequentialGroup()
                    .addGap(204, 204, 204)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1560, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(991, Short.MAX_VALUE)))
        );
        jPanelClientesLayout.setVerticalGroup(
            jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientesLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 716, Short.MAX_VALUE)
                .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonModificarClient)
                        .addComponent(jButtonNuevoClient))
                    .addComponent(jButtonBorrarClient))
                .addGap(64, 64, 64)
                .addComponent(btnVolverClientes)
                .addGap(47, 47, 47))
            .addGroup(jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelClientesLayout.createSequentialGroup()
                    .addGap(133, 133, 133)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(365, Short.MAX_VALUE)))
        );

        jPanelVentanaUnica.add(jPanelClientes, "clientes");

        jLabel3.setText("Artículos");

        btnVolverArticulos.setText("Volver");
        btnVolverArticulos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverArticulosActionPerformed(evt);
            }
        });

        jButtonBorrar.setText("Borrar");
        jButtonBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrarActionPerformed(evt);
            }
        });

        jTableProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTableProductos);

        jComboBoxTipoProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TODOS", "GUITARRA", "AMPLIFICADOR", "ACCESORIO" }));
        jComboBoxTipoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTipoProductoActionPerformed(evt);
            }
        });

        jButtonModificar.setText("Modificar");
        jButtonModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarActionPerformed(evt);
            }
        });

        jButtonNuevo.setText("Nuevo");
        jButtonNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevoActionPerformed(evt);
            }
        });

        btnJson.setText("Insertar desde JSON");
        btnJson.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJsonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelArticulosLayout = new javax.swing.GroupLayout(jPanelArticulos);
        jPanelArticulos.setLayout(jPanelArticulosLayout);
        jPanelArticulosLayout.setHorizontalGroup(
            jPanelArticulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelArticulosLayout.createSequentialGroup()
                .addGap(268, 268, 268)
                .addGroup(jPanelArticulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelArticulosLayout.createSequentialGroup()
                        .addComponent(btnJson)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonNuevo)
                        .addGap(55, 55, 55)
                        .addComponent(jButtonModificar)
                        .addGap(51, 51, 51)
                        .addComponent(jButtonBorrar)
                        .addGap(76, 76, 76)
                        .addComponent(btnVolverArticulos)
                        .addGap(68, 68, 68))
                    .addGroup(jPanelArticulosLayout.createSequentialGroup()
                        .addGroup(jPanelArticulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1560, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelArticulosLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(510, 510, 510)
                                .addComponent(jComboBoxTipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(927, Short.MAX_VALUE))))
        );
        jPanelArticulosLayout.setVerticalGroup(
            jPanelArticulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelArticulosLayout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addGroup(jPanelArticulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxTipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 208, Short.MAX_VALUE)
                .addGroup(jPanelArticulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVolverArticulos)
                    .addComponent(jButtonBorrar)
                    .addComponent(jButtonModificar)
                    .addComponent(jButtonNuevo)
                    .addComponent(btnJson))
                .addGap(47, 47, 47))
        );

        jPanelVentanaUnica.add(jPanelArticulos, "articulos");

        jLabel4.setText("Stock");

        btnVolverStock.setText("Volver");
        btnVolverStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverStockActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelInformesLayout = new javax.swing.GroupLayout(jPanelInformes);
        jPanelInformes.setLayout(jPanelInformesLayout);
        jPanelInformesLayout.setHorizontalGroup(
            jPanelInformesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInformesLayout.createSequentialGroup()
                .addGap(268, 268, 268)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(2254, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelInformesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVolverStock)
                .addGap(68, 68, 68))
        );
        jPanelInformesLayout.setVerticalGroup(
            jPanelInformesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInformesLayout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 741, Short.MAX_VALUE)
                .addComponent(btnVolverStock)
                .addGap(47, 47, 47))
        );

        jPanelVentanaUnica.add(jPanelInformes, "stock");

        jLabel5.setText("Usuarios");

        btnVolverUsuarios.setText("Volver");
        btnVolverUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverUsuariosActionPerformed(evt);
            }
        });

        jTableUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTableUsuarios);

        jButtonBorrarUsuario.setText("Borrar");
        jButtonBorrarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrarUsuarioActionPerformed(evt);
            }
        });

        jButtonNuevoUsuario.setText("Nuevo");
        jButtonNuevoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevoUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelUsuariosLayout = new javax.swing.GroupLayout(jPanelUsuarios);
        jPanelUsuarios.setLayout(jPanelUsuariosLayout);
        jPanelUsuariosLayout.setHorizontalGroup(
            jPanelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelUsuariosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonNuevoUsuario)
                .addGap(54, 54, 54)
                .addComponent(jButtonBorrarUsuario)
                .addGap(57, 57, 57)
                .addComponent(btnVolverUsuarios)
                .addGap(68, 68, 68))
            .addGroup(jPanelUsuariosLayout.createSequentialGroup()
                .addGap(268, 268, 268)
                .addGroup(jPanelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1536, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(951, Short.MAX_VALUE))
        );
        jPanelUsuariosLayout.setVerticalGroup(
            jPanelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelUsuariosLayout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 187, Short.MAX_VALUE)
                .addGroup(jPanelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVolverUsuarios)
                    .addComponent(jButtonBorrarUsuario)
                    .addComponent(jButtonNuevoUsuario))
                .addGap(47, 47, 47))
        );

        jPanelVentanaUnica.add(jPanelUsuarios, "usuarios");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelVentanaUnica, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelVentanaUnica, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesActionPerformed
        cardLayout.show(jPanelVentanaUnica,"clientes");
        cargarClientes();
    }//GEN-LAST:event_btnClientesActionPerformed

    private void btnVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasActionPerformed
        cardLayout.show(jPanelVentanaUnica, "ventas2");
    }//GEN-LAST:event_btnVentasActionPerformed
    
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnVolverClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverClientesActionPerformed
        cardLayout.show(jPanelVentanaUnica,"principal");
    }//GEN-LAST:event_btnVolverClientesActionPerformed

    private void btnArticulosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArticulosActionPerformed
        cardLayout.show(jPanelVentanaUnica,"articulos");
        cargarProductosPorTipo();
    }//GEN-LAST:event_btnArticulosActionPerformed

    private void btnVolverArticulosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverArticulosActionPerformed
        cardLayout.show(jPanelVentanaUnica,"principal");
    }//GEN-LAST:event_btnVolverArticulosActionPerformed

    private void btnVolverStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverStockActionPerformed
        cardLayout.show(jPanelVentanaUnica,"principal");
    }//GEN-LAST:event_btnVolverStockActionPerformed

    private void btnInformesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInformesActionPerformed
        cardLayout.show(jPanelVentanaUnica,"stock");
    }//GEN-LAST:event_btnInformesActionPerformed

    private void btnVolverUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverUsuariosActionPerformed
        cardLayout.show(jPanelVentanaUnica,"principal");                 
    }//GEN-LAST:event_btnVolverUsuariosActionPerformed

    private void btnUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuariosActionPerformed
        cardLayout.show(jPanelVentanaUnica,"usuarios");
        cargarUsuarios();
    }//GEN-LAST:event_btnUsuariosActionPerformed

    private void jButtonBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBorrarActionPerformed
        int[] filas = jTableProductos.getSelectedRows();

        if (filas.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona al menos un producto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas borrar los productos seleccionados?",
                "Confirmar borrado",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        for (int i = filas.length - 1; i >= 0; i--) {

            int fila = filas[i];
            int idProducto = (int) jTableProductos.getValueAt(fila, 0);

            if (pdao.borrar(idProducto)) {
                dtm.removeRow(fila);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al borrar el producto con ID: " + idProducto,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        JOptionPane.showMessageDialog(this, "Productos borrados correctamente");
    }//GEN-LAST:event_jButtonBorrarActionPerformed

    private void jComboBoxTipoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTipoProductoActionPerformed
        if(!comboListo) return;
        cargarProductosPorTipo();
    }//GEN-LAST:event_jComboBoxTipoProductoActionPerformed

    private void jButtonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarActionPerformed
        int fila = jTableProductos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Producto p = new Producto();
            p.setId_producto((int) dtm.getValueAt(fila, 0));
            p.setNombre((String) dtm.getValueAt(fila, 1));
            p.setMarca((String) dtm.getValueAt(fila, 2));
            p.setPrecio(Double.parseDouble(dtm.getValueAt(fila, 3).toString()));
            p.setStock(Integer.parseInt(dtm.getValueAt(fila, 4).toString()));
            p.setTipo_producto((String) dtm.getValueAt(fila, 5));
            p.setDescripcion((String) dtm.getValueAt(fila, 6));
            p.setImagen_url((String) dtm.getValueAt(fila, 7));
            p.setActivo(Integer.parseInt(dtm.getValueAt(fila, 8).toString()));

            pdao.actualizar(p);

            JOptionPane.showMessageDialog(this, "Producto modificado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al modificar el producto: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        filasModificadas.clear();
        jButtonModificar.setEnabled(false);
    }//GEN-LAST:event_jButtonModificarActionPerformed

    private void jButtonNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoActionPerformed
        JDialogAltaProducto jdp = new JDialogAltaProducto(this, true);
        jdp.setVisible(true);
    }//GEN-LAST:event_jButtonNuevoActionPerformed

    private void jButtonModificarClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModificarClientActionPerformed
        int fila = jTableClientes.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Cliente c = new Cliente();
            c.setIdCliente((int) dtmc.getValueAt(fila, 0));
            c.setDni((String) dtmc.getValueAt(fila, 1));
            c.setNombre((String) dtmc.getValueAt(fila, 2));
            c.setApellidos((String) dtmc.getValueAt(fila, 3));
            c.setTelefono((String)(dtmc.getValueAt(fila, 4)));
            c.setEmail((String) (dtmc.getValueAt(fila, 5)));
            c.setDireccion((String) dtmc.getValueAt(fila, 6));
            c.setFecha_alta((Timestamp) dtmc.getValueAt(fila, 7));
            
            cdao.actualizar(c);

            JOptionPane.showMessageDialog(this, "Cliente modificado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al modificar el cliente: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        filasModificadasCliente.clear();
        jButtonModificarClient.setEnabled(false);
    }//GEN-LAST:event_jButtonModificarClientActionPerformed

    private void jButtonNuevoClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoClientActionPerformed
        JDialogAltaCliente jdc = new JDialogAltaCliente(this, true);
        jdc.setVisible(true);
    }//GEN-LAST:event_jButtonNuevoClientActionPerformed

    private void jButtonBorrarClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBorrarClientActionPerformed
       int[] filas = jTableClientes.getSelectedRows();

        if (filas.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona al menos un cliente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas borrar los clientes seleccionados?",
                "Confirmar borrado",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        for (int i = filas.length - 1; i >= 0; i--) {

            int fila = filas[i];
            int idCliente = (int) jTableClientes.getValueAt(fila, 0);

            if (cdao.eliminar(idCliente)) {
                dtmc.removeRow(fila);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al borrar el cliente con ID: " + idCliente,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        JOptionPane.showMessageDialog(this, "Clientes borrados correctamente");
    }//GEN-LAST:event_jButtonBorrarClientActionPerformed

    private void jButtonBorrarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBorrarUsuarioActionPerformed
        int[] filas = jTableUsuarios.getSelectedRows();

        if (filas.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona al menos un usuario.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas borrar los usuarios seleccionados?",
                "Confirmar borrado",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        for (int i = filas.length - 1; i >= 0; i--) {

            int fila = filas[i];
            int idUsuario = (int) jTableUsuarios.getValueAt(fila, 0);
            
            if(userActual.getId()==idUsuario){
                JOptionPane.showMessageDialog(this,
                        "No es posible borrar el usuario actual",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if(udao.eliminar(idUsuario)) {
                dtmu.removeRow(fila);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al borrar el usuario con ID: " + idUsuario,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Usuarios borrados correctamente");
    }//GEN-LAST:event_jButtonBorrarUsuarioActionPerformed

    private void jButtonNuevoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoUsuarioActionPerformed
        JDialogAltaUsuario jdu = new JDialogAltaUsuario(this, true);
        jdu.setVisible(true);
    }//GEN-LAST:event_jButtonNuevoUsuarioActionPerformed

    private void btnJsonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJsonActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.json", "json"));
        int r= fileChooser.showOpenDialog(this);
        
        if(r== JFileChooser.APPROVE_OPTION){
            File archivo = fileChooser.getSelectedFile();
            cargarJson(archivo);
        }else{
            JOptionPane.showMessageDialog(this,
                            "Ha ocurrido un error, Debe seleccionar un Json válido.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnJsonActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArticulos;
    private javax.swing.JButton btnClientes;
    private javax.swing.JButton btnInformes;
    private javax.swing.JButton btnJson;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnUsuarios;
    private javax.swing.JButton btnVentas;
    private javax.swing.JButton btnVolverArticulos;
    private javax.swing.JButton btnVolverClientes;
    private javax.swing.JButton btnVolverStock;
    private javax.swing.JButton btnVolverUsuarios;
    private javax.swing.JButton jButtonBorrar;
    private javax.swing.JButton jButtonBorrarClient;
    private javax.swing.JButton jButtonBorrarUsuario;
    private javax.swing.JButton jButtonModificar;
    private javax.swing.JButton jButtonModificarClient;
    private javax.swing.JButton jButtonNuevo;
    private javax.swing.JButton jButtonNuevoClient;
    private javax.swing.JButton jButtonNuevoUsuario;
    private javax.swing.JComboBox<String> jComboBoxTipoProducto;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelArticulos;
    private javax.swing.JPanel jPanelClientes;
    private javax.swing.JPanel jPanelInformes;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JPanel jPanelUsuarios;
    private javax.swing.JPanel jPanelVentanaUnica;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableClientes;
    private javax.swing.JTable jTableProductos;
    private javax.swing.JTable jTableUsuarios;
    // End of variables declaration//GEN-END:variables
}
