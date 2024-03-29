/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.mavenproject1.tablas;

import com.mycompany.mavenproject1.database.model.Cliente;
import com.mycompany.mavenproject1.database.model.Material;
import com.mycompany.mavenproject1.database.DAO.MaterialDAO;
import com.mycompany.mavenproject1.tablas.model.MPTableModel;
import com.mycompany.mavenproject1.tablas.model.RenderTable;
import com.mycompany.mavenproject1.ui.FormMaterial;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Sebas
 */
public class TodosMaterial extends javax.swing.JPanel {

    /**
     * Creates new form TodosMaterial
     */
    public TodosMaterial() {
        initComponents();
        this.setBackground(Color.LIGHT_GRAY);
        this.cargarDatos();
        this.jTable3.getTableHeader().setPreferredSize(new Dimension(10, 30));
        JMenuItem menuIActualizar = new JMenuItem("Actualizar");
        jPopupMenu1.add(menuIActualizar);
        menuIActualizar.addActionListener((ActionEvent e) -> {
            cargarDatos();
        });
    }

    public static void cargarDatos() {

        JButton mod = new JButton();
        mod.setText("Modificar");
        JButton delete = new JButton();
        delete.setText("Eliminar");
        MaterialDAO mr = new MaterialDAO();
        var materiales = mr.readAll();
        Object[][] datos = new Object[materiales.size()][WIDTH];
        for (int i = 0; i < materiales.size(); i++) {
            Material materiaPrima = materiales.get(i);

            var box1 = new JCheckBox();
            if (materiaPrima.getCalculaDesperdicio() != null) {
                box1.setSelected(materiaPrima.getCalculaDesperdicio());

            } else {
                box1.setSelected(false);

            }
            var box2 = new JCheckBox();
            if (materiaPrima.getAplicaFormula() != null) {
                box2.setSelected(materiaPrima.getAplicaFormula());

            } else {
                box2.setSelected(false);

            }

            datos[i] = new Object[]{
                materiaPrima.getCliente(),
                materiaPrima.getCodigo(),
                materiaPrima.getSubpartida(),
                materiaPrima.getDescripcion(),
                materiaPrima.getTipoUnidad(),
                materiaPrima.getSaldoInsumo(),
                materiaPrima.getPorcentajeDesperdicio(),
                materiaPrima.getPorcentajeMerma(),
                (materiaPrima.getCoeficienteConsumo() == null) ? "" : materiaPrima.getCoeficienteConsumo().doubleValue(),
                box1,
                box2,
                mod,
                delete
            };
        }

        MPTableModel modelo = new MPTableModel(datos);
        jTable3.setModel(modelo);

        jTable3.setRowHeight(30);
        jTable3.setDefaultRenderer(Object.class, new RenderTable());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable2);

        setLayout(new java.awt.BorderLayout());

        jPanel1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanel1FocusGained(evt);
            }
        });
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jScrollPane3MouseReleased(evt);
            }
        });
        jScrollPane3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jScrollPane3KeyReleased(evt);
            }
        });

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable3.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable3.getTableHeader().setReorderingAllowed(false);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable3MouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jPanel1.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jButton1.setText("Nuevo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, java.awt.BorderLayout.PAGE_END);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel1FocusGained
        // TODO add your handling code here:
        this.cargarDatos();
    }//GEN-LAST:event_jPanel1FocusGained

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        FormMaterial fr = new FormMaterial();
        JDialog dialog = new JDialog();
        dialog.setTitle("Insertar");

        dialog.getContentPane().add(fr);
        dialog.setSize(800, 600);
        dialog.setModal(true);

        dialog.setVisible(true);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cargarDatos();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                cargarDatos();
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                cargarDatos();
            }

        });
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:

        var col = jTable3.getSelectedColumn();
        int viewRow = jTable3.getSelectedRow();

        var row = jTable3.convertRowIndexToModel(viewRow);

        MaterialDAO mr = new MaterialDAO();

        if (col == 12) {

            var option = JOptionPane.showConfirmDialog(null, "Se va a eliminar el material ");
            if (option == JOptionPane.YES_OPTION) {

                String codigo = jTable3.getModel().getValueAt(row, 1).toString();
                Cliente clienteTabla = (Cliente) jTable3.getModel().getValueAt(row, 0);

                mr.delete(codigo, clienteTabla.getRuc());
                cargarDatos();
            }

        } else if (col == 11) {
            Cliente cliente = (Cliente) jTable3.getValueAt(row, 0);
            FormMaterial fr = new FormMaterial(jTable3.getValueAt(row, 1).toString(), cliente);
            JDialog dialog = new JDialog();
            dialog.setTitle("Actualizar");
            dialog.getContentPane().add(fr);
            dialog.setSize(800, 600);
            dialog.setModal(true);
            dialog.setVisible(true);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowDeactivated(WindowEvent e) {
                    cargarDatos();
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    cargarDatos();
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    cargarDatos();
                }

            });
        }
    }//GEN-LAST:event_jTable3MouseClicked

    private void jTable3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_jTable3MouseReleased

    private void jScrollPane3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane3KeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_jScrollPane3KeyReleased

    private void jScrollPane3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane3MouseReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_jScrollPane3MouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
    protected static javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables
}
