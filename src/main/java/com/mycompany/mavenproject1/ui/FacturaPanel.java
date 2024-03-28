/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.mavenproject1.ui;

import com.mycompany.mavenproject1.Constantes;
import com.mycompany.mavenproject1.StringUtils;
import com.mycompany.mavenproject1.database.DAO.ClienteDAO;
import com.mycompany.mavenproject1.database.DAO.FacturaDAO;
import com.mycompany.mavenproject1.database.DAO.MaterialDAO;
import com.mycompany.mavenproject1.database.model.Cliente;
import com.mycompany.mavenproject1.database.model.Factura;
import com.mycompany.mavenproject1.database.model.Material;
import com.mycompany.mavenproject1.tablas.model.FacturaTableModel;
import com.mycompany.mavenproject1.tablas.model.RenderTable;
import com.mycompany.mavenproject1.utils.HibernateUtil;
import com.mycompany.mavenproject1.utils.PdfUtils;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author anton
 */
public class FacturaPanel extends javax.swing.JPanel {

    /**
     * Creates new form FacturaPanel
     */
    private int numeroItems;
    public static Cliente cliente;
    private JDateChooser dateChooser;
    public FacturaDAO facturaDAO;

    public FacturaPanel() {
        initComponents();
        this.setBounds(0, 0, 800, 600);

        this.setBackground(Color.LIGHT_GRAY);
        FacturaTableModel facturaTableModel = new FacturaTableModel();
        facturaDAO = new FacturaDAO(HibernateUtil.getSessionFactory());
        tableFacturas.setModel(facturaTableModel);
        // Configuración del ordenamiento por columnas

        tableFacturas.setDefaultRenderer(Object.class, new RenderTable());

        cargarClientes();

        cliente = (Cliente) comboEmpresa.getSelectedItem();
        cargarInsumos();
        dateChooser = new JDateChooser();
        dateChooser.setBounds(0, 0, 150, 30);
        contenedor.add(dateChooser);

    }

    private void cargarClientes() {
        ClienteDAO clienteDAO = new ClienteDAO();
        var clientes = clienteDAO.readAll();
        clientes.stream().forEach(x -> this.comboEmpresa.addItem(x));

    }

    private void cargarInsumos() {
        MaterialDAO materialDAO = new MaterialDAO();
        var insumos = materialDAO.readByRuc(cliente.getRuc());
        if (this.comboInsumos.getItemCount() == 0) {
            insumos.stream().forEach(x -> this.comboInsumos.addItem(x));

        } else {
            comboInsumos.removeAllItems();
            insumos.stream().forEach(x -> this.comboInsumos.addItem(x));

        }
    }

    private static int contarTotalValores(Map<List<List<String>>, List<List<String>>> items) {
        int total = 0;

        // Iterar a través de los valores del mapa y contar
        for (List<List<String>> value : items.values()) {
            total += value.size();
        }

        return total;
    }

    public void actualizarTabla(Map< List<List<String>>, List<List<String>>> items) {
        var totalItems = (contarTotalValores(items));
        Object[][] datos = new Object[totalItems][9];
        JButton mod = new JButton();
        mod.setText("Modificar");
        JButton delete = new JButton();
        delete.setText("Eliminar");
        var contadorItems = 0;
        for (var item : items.entrySet()) {
            for (var itemPeq : item.getValue()) {
                var lenItems = itemPeq.size();
                if (lenItems != 10) {

                    datos[contadorItems] = new Object[]{contadorItems,
                        item.getKey().get(0).get(3),
                        item.getKey().get(0).get(4),
                        item.getKey().get(0).get(5),
                        "", "", "", "", "", "", mod, delete
                    };
                } else {
                    datos[contadorItems] = new Object[]{contadorItems,
                        item.getKey().get(0).get(3),
                        item.getKey().get(0).get(4),
                        item.getKey().get(0).get(5),
                        "", "", "", "", "", "", mod, delete
                    };
                }

                contadorItems++;
            }
        }
        Arrays.sort(datos, Comparator.comparing(o -> ((Comparable) o[1])));
        FacturaTableModel facturaTableModel = new FacturaTableModel(datos);
        tableFacturas.setModel(facturaTableModel);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableFacturas.getModel());
        tableFacturas.setRowSorter(sorter);
        sorter.setSortsOnUpdates(true);

    }

    public Map< List<List<String>>, List<List<String>>> extraerItems(Map<String, List<String>> facturas) {

        Map< List<List<String>>, List<List<String>>> map = new HashMap<>();
        var initContador = 34;

        for (var factura : facturas.entrySet()) {
            List<List<String>> elementosFactura = new ArrayList<>();
            List<List<String>> cabeceraFactura = new ArrayList<>();
            List<String> elementos = new ArrayList<>();
            if (!factura.getValue().isEmpty()) {
                var fecha = StringUtils.formatearFecha(factura.getValue().get(1));
                var numeroFactura = factura.getValue().get(17);
                var clienteFactura = factura.getValue().get(0);
                cabeceraFactura.add(List.of(Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, clienteFactura, numeroFactura, fecha));
                for (int i = initContador; i < factura.getValue().size(); i++) {
                    if (factura.getValue().get(i).trim().contains("SUBTOTAL")) {
                        break;
                    } else {
                        elementos.add(factura.getValue().get(i));
                    }
                }

                // Imprimir los elementos
                for (int i = 0; i < elementos.size() - 4; i++) {
                    elementosFactura.add(List.of((elementos.get(i).split(" "))));

                }
                map.put(cabeceraFactura, elementosFactura);
            } else {
                JOptionPane.showMessageDialog(null, "Factura " + factura.getKey() + " no se puede leer");
            }

        }
        return map;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        comboInsumos = new javax.swing.JComboBox<>();
        noFacturaTxt = new javax.swing.JTextField();
        descripcionTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableFacturas = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        comboEmpresa = new javax.swing.JComboBox<>();
        comboMedida = new javax.swing.JComboBox<>();
        largoTxt = new javax.swing.JTextField();
        anchoTxt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cantidadTxt = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel10 = new javax.swing.JLabel();
        idTxt = new javax.swing.JLabel();
        contenedor = new javax.swing.JPanel();
        clienteNombre = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(800, 600));

        jLabel1.setText("Empresa");

        comboInsumos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboInsumosActionPerformed(evt);
            }
        });

        descripcionTxt.setText("LÁMINA MICROCORRUGADA");

        jLabel3.setText("No.");

        jLabel4.setText("Cantidad");

        jLabel5.setText("Insumo");

        tableFacturas.setModel(new javax.swing.table.DefaultTableModel(
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
        tableFacturas.setPreferredSize(new java.awt.Dimension(800, 600));
        tableFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableFacturasMouseClicked(evt);
            }
        });
        tableFacturas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableFacturasKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableFacturas);

        jButton1.setText("Generar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cargar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setText("Unidad de Medida");

        jLabel7.setText("Largo");

        jLabel8.setText("Ancho");

        comboEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboEmpresaActionPerformed(evt);
            }
        });

        comboMedida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tonelada (TM)", "Kilogramo (KG)", " " }));

        jLabel9.setText("Descripción producto terminado");

        jToggleButton1.setText("Guardar");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jLabel10.setText("Id");

        contenedor.setPreferredSize(new java.awt.Dimension(150, 30));

        javax.swing.GroupLayout contenedorLayout = new javax.swing.GroupLayout(contenedor);
        contenedor.setLayout(contenedorLayout);
        contenedorLayout.setHorizontalGroup(
            contenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );
        contenedorLayout.setVerticalGroup(
            contenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jLabel11.setText("Cliente");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1275, Short.MAX_VALUE)
                        .addGap(16, 16, 16))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(298, 298, 298)
                                .addComponent(jButton2)
                                .addGap(342, 342, 342)
                                .addComponent(jButton1))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(descripcionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(comboMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(largoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(anchoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(5, 5, 5)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(clienteNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel3)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(noFacturaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(contenedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(comboEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cantidadTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5)
                                        .addComponent(comboInsumos, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(noFacturaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(clienteNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(contenedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cantidadTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboInsumos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descripcionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(largoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(anchoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        PdfUtils pdfUtils = new PdfUtils((Cliente) comboEmpresa.getSelectedItem());
        try {
            var facturas = pdfUtils.cargarPDFs();
            var items = extraerItems(facturas);
            actualizarTabla(items);
        } catch (IOException ex) {
            Logger.getLogger(FacturaPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void tableFacturasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableFacturasKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tableFacturasKeyReleased

    private void tableFacturasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableFacturasMouseClicked
        // TODO add your handling code here:
        var col = tableFacturas.getSelectedColumn();
        var row = tableFacturas.getSelectedRow();
        FacturaTableModel model = (FacturaTableModel) tableFacturas.getModel();
        if (col == 10) {
            if (cantidadTxt.getText() != null) {
                cantidadTxt.setText((String) tableFacturas.getValueAt(row, 4));

            }
            if (clienteNombre.getText() != null) {
                clienteNombre.setText((String) tableFacturas.getValueAt(row, 1));
            }
            if (noFacturaTxt.getText() != null) {
                noFacturaTxt.setText((String) tableFacturas.getValueAt(row, 2));

            }
            if (idTxt.getText() != null) {
                idTxt.setText(String.valueOf(tableFacturas.getValueAt(row, 0)));

            }
            if (largoTxt.getText() != null) {
                largoTxt.setText(String.valueOf(tableFacturas.getValueAt(row, 8)));

            }
            if (anchoTxt.getText() != null) {
                anchoTxt.setText(String.valueOf(tableFacturas.getValueAt(row, 9)));

            }

            SimpleDateFormat formatoDeseado = new SimpleDateFormat("dd/MM/yy");
            try {
                // Formatear la fecha en el nuevo formato
                var fd = formatoDeseado.parse(String.valueOf(tableFacturas.getValueAt(row, 3)));
                dateChooser.setDate(fd);
            } catch (ParseException ex) {
                Logger.getLogger(FacturaPanel.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (col == 11) {
            model.removeRow(row);
        }
    }//GEN-LAST:event_tableFacturasMouseClicked

    private void comboInsumosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboInsumosActionPerformed
        // TODO add your handling code here:
        System.out.println(evt);
    }//GEN-LAST:event_comboInsumosActionPerformed

    private void comboEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboEmpresaActionPerformed
        // TODO add your handling code here:
        cliente = (Cliente) comboEmpresa.getSelectedItem();
        cargarInsumos();
    }//GEN-LAST:event_comboEmpresaActionPerformed
    private int buscarElemento(String nroFactura, String id) {
        var datos = tableFacturas.getModel();
        int rowCount = datos.getRowCount();
        int fin = -1;
        for (int row = 0; row < rowCount; row++) {
            String nroFacturaAct = datos.getValueAt(row, 2).toString();
            String idAct = datos.getValueAt(row, 0).toString();

            if (nroFactura.equals(nroFacturaAct) && id.equals(idAct)) {
                fin = row;
            }

        }
        return fin;
    }
    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        FacturaTableModel datos = (FacturaTableModel) tableFacturas.getModel();
        int rowCount = datos.getRowCount();
        int encontrado = buscarElemento(noFacturaTxt.getText(), String.valueOf(idTxt.getText()));
        if (validarTexto()) {
            if (encontrado >= 0) {
                Object[] rowData = {
                    idTxt.getText(),
                    datos.getValueAt(encontrado, 1),
                    noFacturaTxt.getText(),
                    StringUtils.transformarFecha(dateChooser.getCalendar().getTime()),
                    cantidadTxt.getText(),
                    ((Material) comboInsumos.getSelectedItem()).getCodigo(),
                    descripcionTxt.getText(),
                    comboMedida.getSelectedItem(),
                    largoTxt.getText(),
                    anchoTxt.getText(),
                    datos.getValueAt(encontrado, 10),
                    datos.getValueAt(encontrado, 11)
                };

                datos.updateRow(encontrado, rowData);
                Factura f = new Factura();
                f.setCantidad(Integer.valueOf(cantidadTxt.getText()));

                f.setAncho(Double.valueOf(anchoTxt.getText()));
                f.setLargo(Double.valueOf(largoTxt.getText()));
                f.setClienteFactura(clienteNombre.getText());
                LocalDate localDate = LocalDate.ofInstant(dateChooser.getCalendar().getTime().toInstant(), java.time.ZoneId.systemDefault());
                f.setFecha(localDate);
                f.setClienteFactura(clienteNombre.getText());
                f.setInsumo(((Material) comboInsumos.getSelectedItem()).getCodigo());
                f.setNroFactura(noFacturaTxt.getText());
                f.setInsumoDescripcion(descripcionTxt.getText());
                f.setUnidadMedida(String.valueOf(comboMedida.getSelectedItem()));
                facturaDAO.create(f);
                limpiarInput();

            } else {
                JButton mod = new JButton();
                mod.setText("Modificar");
                JButton delete = new JButton();
                delete.setText("Eliminar");
                Object[] rowData = {
                    (rowCount - 1) + 1,
                    clienteNombre.getText(),
                    noFacturaTxt.getText(),
                    StringUtils.transformarFecha(dateChooser.getCalendar().getTime()),
                    cantidadTxt.getText(),
                    ((Material) comboInsumos.getSelectedItem()).getCodigo(),
                    descripcionTxt.getText(),
                    comboMedida.getSelectedItem(),
                    largoTxt.getText(),
                    anchoTxt.getText(),
                    mod,
                    delete
                };
                datos.addRow(rowData);
                //insertar en la base de datos
                Factura f = new Factura();
                f.setCantidad(Integer.valueOf(cantidadTxt.getText()));

                f.setAncho(Double.valueOf(anchoTxt.getText()));
                f.setLargo(Double.valueOf(largoTxt.getText()));
                f.setClienteFactura(clienteNombre.getText());
                LocalDate localDate = LocalDate.ofInstant(dateChooser.getCalendar().getTime().toInstant(), java.time.ZoneId.systemDefault());
                f.setFecha(localDate);
                f.setClienteFactura(clienteNombre.getText());
                f.setInsumo(((Material) comboInsumos.getSelectedItem()).getCodigo());
                f.setNroFactura(noFacturaTxt.getText());
                f.setInsumoDescripcion(descripcionTxt.getText());
                f.setUnidadMedida(String.valueOf(comboMedida.getSelectedItem()));
                facturaDAO.create(f);
                limpiarInput();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Rellene todos los campos");
        }


    }//GEN-LAST:event_jToggleButton1ActionPerformed
    private boolean validarTexto() {
        return !(cantidadTxt.getText().isEmpty()
                || clienteNombre.getText().isEmpty()
                || noFacturaTxt.getText().isEmpty()
                || largoTxt.getText().isEmpty()
                || anchoTxt.getText().isEmpty()
                || dateChooser.getCalendar() == null);
    }

    private void limpiarInput() {
        clienteNombre.setText(null);
        cantidadTxt.setText(null);
        noFacturaTxt.setText(null);
        largoTxt.setText(null);
        anchoTxt.setText(null);
        dateChooser.setCalendar(null);
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JDialog dialog = new JDialog();
        dialog.setTitle("Generar Reportes");
        dialog.getContentPane().add(new GeneradorDialog());
        dialog.setSize(600, 300);
        dialog.setModal(true);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField anchoTxt;
    private javax.swing.JTextField cantidadTxt;
    private javax.swing.JTextField clienteNombre;
    private javax.swing.JComboBox<Cliente> comboEmpresa;
    private javax.swing.JComboBox<Material> comboInsumos;
    private javax.swing.JComboBox<String> comboMedida;
    private javax.swing.JPanel contenedor;
    private javax.swing.JTextField descripcionTxt;
    private javax.swing.JLabel idTxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextField largoTxt;
    private javax.swing.JTextField noFacturaTxt;
    public static javax.swing.JTable tableFacturas;
    // End of variables declaration//GEN-END:variables
}
