/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.mavenproject1.tablas;

import com.mycompany.mavenproject1.utils.HibernateUtil;
import com.mycompany.mavenproject1.database.model.MaterialReporte;
import com.mycompany.mavenproject1.database.DAO.MaterialReporteDAO;
import com.mycompany.mavenproject1.database.DAO.ReporteDAO;
import com.mycompany.mavenproject1.tablas.model.MPReporteTableModel;
import com.mycompany.mavenproject1.tablas.model.RenderTable;
import com.mycompany.mavenproject1.ui.FormMaterialReporte;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.awt.image.ImageObserver.WIDTH;

import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author Sebas
 */
public class TodosMaterialReporte extends javax.swing.JPanel {

    /**
     * Creates neigw form TodosMaterialReporte
     */
    List<String> codigos;
    private ReporteDAO reporteDAO;

    public TodosMaterialReporte() {
        initComponents();
        this.reporteDAO=new ReporteDAO(HibernateUtil.getSessionFactory());
        jTable1.setRowHeight(30);
        this.setBounds(0, 0, 800, 600);
        this.setBackground(Color.LIGHT_GRAY);
        cargarDatos();

        JMenuItem menuIActualizar = new JMenuItem("Actualizar");

        jPopupMenu1.add(menuIActualizar);
        menuIActualizar.addActionListener((ActionEvent e) -> {
            cargarDatos();
        });
        jPanel1.add(new FormMaterialReporte());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnNuevo = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseReleased(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        add(btnNuevo, java.awt.BorderLayout.PAGE_END);
        add(jPanel1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        FormMaterialReporte fr = new FormMaterialReporte();
        JDialog dialog = new JDialog();
        dialog.setTitle("Insertar");

        dialog.getContentPane().add(fr);
        dialog.setSize(900, 150);
        dialog.setModal(true);
        dialog.setResizable(false);
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


    }//GEN-LAST:event_btnNuevoActionPerformed
    public static void regargarPanel() {
        jPanel1.removeAll();
        jPanel1.add(new FormMaterialReporte());
    }
    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        var col = jTable1.getSelectedColumn();
        var row = jTable1.getSelectedRow();
        MaterialReporteDAO mr = new MaterialReporteDAO(HibernateUtil.getSessionFactory());
        if (col == 5) {

            var option = JOptionPane.showConfirmDialog(null, "Se va a eliminar el material");
            if (option == JOptionPane.YES_OPTION) {

                String codigo
                        = jTable1.getModel().getValueAt(row, 0).toString();
                System.out.println(mr.delete(codigo));
                cargarDatos();
            }

        } else if (col == 4) {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            FormMaterialReporte fr = new FormMaterialReporte(jTable1.getModel().getValueAt(row, 0).toString());
            jPanel1.removeAll();
            jPanel1.add(fr);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        // TODO add your handling code here:
        if (evt.isPopupTrigger()) {
            this.jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable1MouseReleased

    private void jScrollPane1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseReleased
        // TODO add your handling code here:
        if (evt.isPopupTrigger()) {
            this.jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jScrollPane1MouseReleased

    public static void cargarDatos() {
        MaterialReporteDAO mr = new MaterialReporteDAO(HibernateUtil.getSessionFactory());

        JButton mod = new JButton();
        mod.setText("Modificar");
        mod.addActionListener((ActionEvent e) -> {
            System.out.println("Modificar");
        });
        JButton delete = new JButton();
        delete.setText("Eliminar");
        var materiales = mr.readAll();
        
        Object[][] datos = new Object[materiales.size()][WIDTH];
        for (int i = 0; i < materiales.size(); i++) {
            MaterialReporte materiaPrima = materiales.get(i);
            datos[i] = new Object[]{
                materiaPrima.getCodigo(),
                materiaPrima.getCodigoInsumo(),
                materiaPrima.getDescripcion(),
                materiaPrima.getCliente(),
                mod,
                delete
            };
        }
        MPReporteTableModel modelo = new MPReporteTableModel(datos);
        jTable1.setModel(modelo);
        jTable1.setDefaultRenderer(Object.class, new RenderTable());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnNuevo;
    protected static javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    protected static javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
