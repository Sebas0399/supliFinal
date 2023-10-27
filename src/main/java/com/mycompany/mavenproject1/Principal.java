/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.mavenproject1;

import com.mycompany.mavenproject1.ui.Cargando;
import com.mycompany.mavenproject1.ui.GeneradorPanel;
import com.mycompany.mavenproject1.ui.EntradaPanel;
import com.mycompany.mavenproject1.tablas.TodosMaterial;
import com.mycompany.mavenproject1.tablas.TodosMaterialReporte;
import com.mycompany.mavenproject1.ui.AdminPanel;

public class Principal extends javax.swing.JFrame {

    /**
     * Creates new form Principal
     */
    public Principal() {

        Runnable mRunnable = () -> {
            Cargando c = new Cargando("Bienvenido");
            c.setVisible(true);
            initComponents();
            
            this.setVisible(false);
          
            EntradaPanel pan1 = new EntradaPanel();
            GeneradorPanel pan2 = new GeneradorPanel(pan1.getPaths());
            jInternalFrame1.add(pan1);
            jInternalFrame2.add(pan2);

            TodosMaterialReporte pan5 = new TodosMaterialReporte();
            jInternalFrame3.add(pan5);
            TodosMaterial pan4 = new TodosMaterial();
            jInternalFrame4.add(pan4);
            AdminPanel adminPanel=new AdminPanel();
            jInternalFrame5.add(adminPanel);
            c.dispose();
            this.setVisible(true);

        };
        Thread miHilo = new Thread(mRunnable);
        miHilo.start();
        // this.setVisible(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jInternalFrame2 = new javax.swing.JInternalFrame();
        jInternalFrame4 = new javax.swing.JInternalFrame();
        jInternalFrame3 = new javax.swing.JInternalFrame();
        jInternalFrame5 = new javax.swing.JInternalFrame();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1280, 720));

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jInternalFrame1.setVisible(true);
        jTabbedPane1.addTab("Entrada", jInternalFrame1);

        jInternalFrame2.setVisible(true);
        jTabbedPane1.addTab("Generador", jInternalFrame2);

        jInternalFrame4.setVisible(true);
        jTabbedPane1.addTab("Insumos", jInternalFrame4);

        jInternalFrame3.setVisible(true);
        jTabbedPane1.addTab("Material", jInternalFrame3);

        jInternalFrame5.setVisible(true);
        jTabbedPane1.addTab("Administrar", jInternalFrame5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JInternalFrame jInternalFrame2;
    private javax.swing.JInternalFrame jInternalFrame3;
    private javax.swing.JInternalFrame jInternalFrame4;
    private javax.swing.JInternalFrame jInternalFrame5;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
