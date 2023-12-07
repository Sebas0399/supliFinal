/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.mavenproject1.ui;

import com.mycompany.mavenproject1.Constantes;
import com.mycompany.mavenproject1.database.DAO.MaterialReporteDAO;
import com.mycompany.mavenproject1.database.model.Cliente;
import com.mycompany.mavenproject1.database.model.Factura;
import static com.mycompany.mavenproject1.tablas.TodosMaterialReporte.cargarDatos;
import com.mycompany.mavenproject1.tablas.model.ComboBoxEditor;
import com.mycompany.mavenproject1.tablas.model.FacturaTableModel;
import com.mycompany.mavenproject1.tablas.model.RenderTable;
import com.mycompany.mavenproject1.utils.HibernateUtil;
import com.mycompany.mavenproject1.utils.PdfUtils;
import java.awt.Color;
import java.awt.Cursor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;

/**
 *
 * @author anton
 */
public class FacturaPanel extends javax.swing.JPanel {

    /**
     * Creates new form FacturaPanel
     */
    private int numeroItems;
    JComboBox combo;

    public FacturaPanel() {
        initComponents();
        this.setBounds(0, 0, 800, 600);

        this.setBackground(Color.LIGHT_GRAY);
        FacturaTableModel facturaTableModel = new FacturaTableModel();

        tableFacturas.setModel(facturaTableModel);
        tableFacturas.setDefaultRenderer(Object.class, new RenderTable());
        combo = new JComboBox();

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
        var totalItems
                = (contarTotalValores(items));

        Object[][] datos = new Object[totalItems][9];

        combo.addItem("SApo");

        var contadorItems = 0;
        for (var item : items.entrySet()) {
            System.out.println(item.getValue());

            for (var itemPeq : item.getValue()) {
                var lenItems = itemPeq.size();
                System.out.println(itemPeq.size());
                if (lenItems == 10) {

                    datos[contadorItems] = new Object[]{
                        item.getKey().get(0).get(3),
                        itemPeq.get(1), "", combo, "", "", "", "", ""
                    };
                } else {
                    var data = itemPeq.get(0).split(" ");
                    System.out.println(Arrays.toString(data));
                    datos[contadorItems] = new Object[]{
                        item.getKey().get(0).get(3),
                        data[data.length - 1], "", combo, "", "", "", "", ""
                    };
                }

                contadorItems++;
            }

        }

        FacturaTableModel facturaTableModel = new FacturaTableModel(datos);
        tableFacturas.setModel(facturaTableModel);

    }
 
    public Map< List<List<String>>, List<List<String>>> extraerItems(Map<String, List<String>> facturas) {

        Map< List<List<String>>, List<List<String>>> map = new HashMap<>();
        var initContador = 34;
        for (var factura : facturas.entrySet()) {
            List<List<String>> elementosFactura = new ArrayList<>();
            List<List<String>> cabeceraFactura = new ArrayList<>();
            List<String> elementos = new ArrayList<>();
            var fecha = factura.getValue().get(1);
            var numeroFactura = factura.getValue().get(17);
            //var clienteRuc = cliente.getRuc();
            //var ruta = factura.getKey();

            cabeceraFactura.add(List.of(Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, numeroFactura, fecha));
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
        comboEmpresa = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
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
        comboEmpresa1 = new javax.swing.JComboBox<>();
        comboEmpresa2 = new javax.swing.JComboBox<>();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();

        jLabel1.setText("Empresa");

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

        jButton2.setText("Cargar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setText("Unidad de Medida");

        jLabel7.setText("Largo");

        jLabel8.setText("Ancho");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(270, 270, 270))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 864, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(35, 35, 35))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(comboEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(comboEmpresa2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(272, 272, 272)
                    .addComponent(comboEmpresa1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(289, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6))
                            .addComponent(comboEmpresa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton1))
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboEmpresa2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addComponent(comboEmpresa1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(478, Short.MAX_VALUE)))
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
        System.out.println();
    }//GEN-LAST:event_tableFacturasKeyReleased

    private void tableFacturasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableFacturasMouseClicked
        // TODO add your handling code here:
        var col = tableFacturas.getSelectedColumn();
        var row = tableFacturas.getSelectedRow();
        if (col == 5) {
            combo.setPopupVisible(true);

        } else if (col == 4) {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        }
    }//GEN-LAST:event_tableFacturasMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<Cliente> comboEmpresa;
    private javax.swing.JComboBox<Cliente> comboEmpresa1;
    private javax.swing.JComboBox<Cliente> comboEmpresa2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTable tableFacturas;
    // End of variables declaration//GEN-END:variables
}
