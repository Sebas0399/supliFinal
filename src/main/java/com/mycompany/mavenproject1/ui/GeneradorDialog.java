/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.mavenproject1.ui;

import com.mycompany.mavenproject1.Constantes;
import com.mycompany.mavenproject1.FCGenerador;
import com.mycompany.mavenproject1.StringUtils;
import com.mycompany.mavenproject1.database.DAO.FacturaDAO;
import com.mycompany.mavenproject1.database.DAO.MaterialDAO;
import com.mycompany.mavenproject1.database.model.Cliente;
import com.mycompany.mavenproject1.database.model.Factura;
import com.mycompany.mavenproject1.database.model.Material;
import com.mycompany.mavenproject1.utils.FileUtils;
import com.mycompany.mavenproject1.utils.HibernateUtil;
import com.toedter.calendar.JDateChooser;
import java.awt.HeadlessException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Hibernate;

/**
 *
 * @author Administrator
 */
public class GeneradorDialog extends javax.swing.JPanel {

    MaterialDAO materialDAO;
    TableModel datos;
    private JDateChooser dateChooserInicio;
    private JDateChooser dateChooserFin;
    private FacturaDAO facturaDAO;

    /**
     * Creates new form GeneradorDialog
     */
    public GeneradorDialog() {
        initComponents();
        materialDAO = new MaterialDAO();
        datos = FacturaPanel.tableFacturas.getModel();

        dateChooserInicio = new JDateChooser();
        dateChooserInicio.setBounds(300, 20, 150, 30);

        dateChooserFin = new JDateChooser();
        dateChooserFin.setBounds(300, 70, 150, 30);
        this.add(dateChooserInicio);
        this.add(dateChooserFin);
        facturaDAO = new FacturaDAO(HibernateUtil.getSessionFactory());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        generarFacturas = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        generarProductoTerminado = new javax.swing.JToggleButton();
        jLabel4 = new javax.swing.JLabel();
        generarMediosProduccion = new javax.swing.JToggleButton();

        generarFacturas.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        generarFacturas.setText("Generar");
        generarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarFacturasActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Generar Facturas");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Generar Producto Terminado");

        generarProductoTerminado.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        generarProductoTerminado.setText("Generar");
        generarProductoTerminado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarProductoTerminadoActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Generar Material de Producción");

        generarMediosProduccion.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        generarMediosProduccion.setText("Generar");
        generarMediosProduccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarMediosProduccionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generarMediosProduccion)
                    .addComponent(generarProductoTerminado)
                    .addComponent(generarFacturas)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addContainerGap(297, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generarFacturas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generarProductoTerminado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generarMediosProduccion)
                .addContainerGap(18, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void generarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarFacturasActionPerformed
        // TODO add your handling code here:
        var ruta = FileUtils.saveData("Guardar Factura");
        Map<String, List<String>> mapLista = new HashMap<>();

        LocalDate fechaInicio = LocalDate.ofInstant(dateChooserInicio.getCalendar().getTime().toInstant(), java.time.ZoneId.systemDefault());
        LocalDate fechaFin = LocalDate.ofInstant(dateChooserFin.getCalendar().getTime().toInstant(), java.time.ZoneId.systemDefault());
        var facturas = facturaDAO.filterByFecha(fechaInicio, fechaFin);
        for (Factura factura : facturas) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Convertir LocalDate a String con el formato especificado
            String fechaFormateada = factura.getFecha().format(formatter);
            mapLista.put(factura.getNroFactura(), List.of(factura.getClienteFactura(), factura.getNroFactura(), fechaFormateada, String.valueOf(factura.getCantidad()), factura.getInsumo(), String.valueOf(factura.getLargo()), String.valueOf(factura.getAncho())));
        }
        var nombres = obtenerNombresClientes(mapLista);
        try {
            for (var nombre : nombres) {
                List<List<String>> lFinal = new ArrayList<>();
                for (var ent : mapLista.entrySet()) {

                    var listaSub = ent.getValue();
                    var nameCliente = listaSub.get(0);

                    if (nombre.contains(nameCliente)) {
                        lFinal.add(List.of(Constantes.SUBPARTIDA_FC_MEGA, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, listaSub.get(1), listaSub.get(2)));
                    }

                }
                Collections.sort(lFinal, Comparator
                        .<List<Object>, Comparable>comparing(list -> (Comparable) ((List<Object>) list).get(4))
                        .thenComparing(list -> ((List<Object>) list).get(6)));
                generarExcel(lFinal, FacturaPanel.cliente, nombre, ruta);

            }
            JOptionPane.showMessageDialog(null, "Facturas generadas");

        } catch (Exception e) {

        }


    }//GEN-LAST:event_generarFacturasActionPerformed
    public List<String> obtenerNombresClientes(Map<String, List<String>> mapLista) {
        List<String> nombres = new ArrayList<>();
        for (var lista : mapLista.entrySet()) {
            if (!nombres.contains(lista.getValue().get(0))) {
                nombres.add(lista.getValue().get(0));
            }
        }

        return nombres;
    }

    public void generarExcel(List<List<String>> lFinal, Cliente empresa, String cliente, String path) {

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<List<String>> cabeceras = List.of(
                List.of("Subpartida", "Complementario", "Suplementario", "Numero de factura", "Numero de item"),
                List.of("prdt_hs_part_cd", "prdt_hs_cpmt_cd", "prdt_hs_spmt_cd", "ntfc_no", "ntfc_de")
        );

        int rowNumber = 0;
        for (List<String> cabecera : cabeceras) {
            Row header = sheet.createRow(rowNumber++);
            for (int i = 0; i < cabecera.size(); i++) {
                header.createCell(i).setCellValue(cabecera.get(i));
            }
        }

        for (List<String> x : lFinal) {
            Row r = sheet.createRow(rowNumber++);
            int k = 0;
            for (String j : x) {

                r.createCell(k++).setCellValue(j);

            }
        }

        String fileLocation = path + "\\" + StringUtils.tranformarNombre(empresa.getNombre()) + "_FC_" + cliente + ".xls";

        try (FileOutputStream outputStream = new FileOutputStream(fileLocation)) {
            workbook.write(outputStream);

        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        try {
            workbook.close();

        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class
                    .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Ocurrio un errror");

        }
    }
    private void generarProductoTerminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarProductoTerminadoActionPerformed
        // TODO add your handling code here:
        var ruta = FileUtils.saveData("Guardar Producto Terminado");

        Map<String, List<String>> mapLista = new HashMap<>();

        var nameCliente = "";

        LocalDate fechaInicio = LocalDate.ofInstant(dateChooserInicio.getCalendar().getTime().toInstant(), java.time.ZoneId.systemDefault());
        LocalDate fechaFin = LocalDate.ofInstant(dateChooserFin.getCalendar().getTime().toInstant(), java.time.ZoneId.systemDefault());
        var facturas = facturaDAO.filterByFecha(fechaInicio, fechaFin);

        for (Factura factura : facturas) {
            if (mapLista.containsKey(factura.getNroFactura())) {
                var sumaVal = Double.valueOf(factura.getCantidad()) + Double.valueOf(mapLista.get(factura.getNroFactura()).get(3));
                mapLista.put(factura.getNroFactura(), List.of(
                        factura.getClienteFactura(),
                        factura.getNroFactura(),
                        String.valueOf(factura.getFecha()),
                        String.valueOf((int) sumaVal),
                        factura.getInsumoDescripcion(),
                        String.valueOf(factura.getLargo()),
                        String.valueOf(factura.getAncho())
                ));

            } else {
                mapLista.put(factura.getNroFactura(), List.of(
                        factura.getClienteFactura(),
                        factura.getNroFactura(),
                        String.valueOf(factura.getFecha()),
                        String.valueOf(factura.getCantidad()),
                        factura.getInsumoDescripcion(),
                        String.valueOf(factura.getLargo()),
                        String.valueOf(factura.getAncho())
                ));

            }
        }
        var nombres = obtenerNombresClientes(mapLista);
        try {

            for (var nombre : nombres) {
                List<List<String>> lFinal = new ArrayList<>();
                var numSerie = 1;
                for (var e : mapLista.entrySet()) {
                    var lPeq = e.getValue();
                    nameCliente = lPeq.get(0);

                    if (nameCliente.contains(nombre)) {
                        lFinal.add(List.of(lPeq.get(1), String.valueOf(numSerie), "1", Constantes.SUBPARTIDA_FC_MEGA, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, lPeq.get(4), "U", String.valueOf(lPeq.get(3))));
                        numSerie++;
                    }

                }

                generarExcelPT(lFinal, FacturaPanel.cliente, nombre, ruta);

            }
            JOptionPane.showMessageDialog(null, "Producto Terminado generado");

        } catch (Exception e) {

        }


    }//GEN-LAST:event_generarProductoTerminadoActionPerformed
    public void generarExcelPT(List<List<String>> lFinal, Cliente empresa, String cliente, String path) {

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<List<String>> cabeceras = List.of(
                List.of("No. Factura asociada", "el número de serie", "Numero de item", "Subpartida", "Complementario", "Suplementario", "Descripción", "Tipo Unidad", "Cantidad Transformado"),
                List.of("ntfc_prdt_cl_cd", "sn", "prdt_item_sn", "prdt_hs_part_cd", "prdt_hs_cpmt_cd", "prdt_hs_spmt_cd", "prdt_prdt_desc", "prdt_ut_tp_cd", "prdt_trsm_use_qt")
        );

        int rowNumber = 0;
        for (List<String> cabecera : cabeceras) {
            Row header = sheet.createRow(rowNumber++);
            for (int i = 0; i < cabecera.size(); i++) {
                header.createCell(i).setCellValue(cabecera.get(i));
            }
        }

        for (List<String> x : lFinal) {
            Row r = sheet.createRow(rowNumber++);
            int k = 0;
            for (String j : x) {

                r.createCell(k++).setCellValue(j);

            }
        }

        String fileLocation = path + "\\" + StringUtils.tranformarNombre(empresa.getNombre()) + "_PT_" + cliente + ".xls";

        try (FileOutputStream outputStream = new FileOutputStream(fileLocation)) {
            workbook.write(outputStream);

        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        try {
            workbook.close();

        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class
                    .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Ocurrio un errror");

        }
    }

    public void generarExcelMP(List<List<String>> lFinal, Cliente empresa, String cliente, String path) {

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<List<String>> cabeceras = List.of(
                List.of("No. Factura asociada", "Supbartida", "Complementario", "Suplementario", "el número de serie", "Numero de item", "Código", "Subpartida", "Complementario", "Suplementario", "Descripción", "Tipo Unidad", "Cantidad Transformado", "Cantidad de Desperdicio", "Cantidad de Merma"
                ),
                List.of("csgd_ntfc_no", "prdt_hs_part_cd", "prdt_hs_cpmt_cd", "prdt_hs_spmt_cd", "prdt_sn", "csgd_item_sn", "csgd_cmdt_cd", "csgd_hs_part_cd", "csgd_hs_cpmt_cd", "csgd_hs_spmt_cd", "csgd_prdt_desc", "csgd_ut_tp_cd", "csgd_trsm_use_qt", "csgd_duse_qt", "csgd_use_ips_duse_qt"
                )
        );

        int rowNumber = 0;
        for (List<String> cabecera : cabeceras) {
            Row header = sheet.createRow(rowNumber++);
            for (int i = 0; i < cabecera.size(); i++) {
                header.createCell(i).setCellValue(cabecera.get(i));
            }
        }

        for (List<String> x : lFinal) {
            Row r = sheet.createRow(rowNumber++);
            int k = 0;
            for (String j : x) {
                if (k == 12 || k == 14) {
                    r.createCell(k++).setCellValue(j.replace('.', ','));
                } else {
                    r.createCell(k++).setCellValue(j);

                }

            }
        }

        String fileLocation = path + "\\" + StringUtils.tranformarNombre(empresa.getNombre()) + "_MP_" + cliente + ".xls";

        try (FileOutputStream outputStream = new FileOutputStream(fileLocation)) {
            workbook.write(outputStream);

        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        try {
            workbook.close();

        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class
                    .getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Ocurrio un errror");

        }
    }
    private void generarMediosProduccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarMediosProduccionActionPerformed
        // TODO add your handling code here:
        var ruta = FileUtils.saveData("Guardar Medios de Produccion");

        Map<String, List<String>> mapLista = new HashMap<>();

        var nameCliente = "";

        LocalDate fechaInicio = LocalDate.ofInstant(dateChooserInicio.getCalendar().getTime().toInstant(), java.time.ZoneId.systemDefault());
        LocalDate fechaFin = LocalDate.ofInstant(dateChooserFin.getCalendar().getTime().toInstant(), java.time.ZoneId.systemDefault());
        var facturas = facturaDAO.filterByFecha(fechaInicio, fechaFin);
        for (Factura factura : facturas) {
            if (mapLista.containsKey(factura.getNroFactura())) {
                var largo = new BigDecimal(factura.getLargo());
                var ancho = new BigDecimal(factura.getAncho());
                var cantidad = new BigDecimal(factura.getCantidad());
                var tot = largo.multiply(ancho);
                var totCant = tot.multiply(cantidad).multiply(Constantes.COEFICIENTE_MEGA);
                var ant = new BigDecimal(mapLista.get(factura.getNroFactura()).get(3));

                var sumaVal = totCant.add(ant);

                mapLista.put(factura.getNroFactura(), List.of(
                        factura.getClienteFactura(),
                        factura.getNroFactura(),
                        String.valueOf(factura.getFecha()),
                        String.valueOf(sumaVal.round(new MathContext(3))),
                        factura.getInsumoDescripcion(),
                        factura.getInsumo(),
                        factura.getUnidadMedida()
                ));

            } else {

                var largo = new BigDecimal(factura.getLargo());
                var ancho = new BigDecimal(factura.getAncho());
                var cantidad = new BigDecimal(factura.getCantidad());
                var tot = largo.multiply(ancho);
                var totCant = tot.multiply(cantidad).multiply(Constantes.COEFICIENTE_MEGA);
                mapLista.put(factura.getNroFactura(),
                        List.of(
                                factura.getClienteFactura(),
                                factura.getNroFactura(),
                                String.valueOf(factura.getFecha()),
                                totCant.round(new MathContext(3)).toString(),
                                factura.getInsumoDescripcion(),
                                factura.getInsumo(),
                                factura.getUnidadMedida()
                        )
                );
            }
        }
        var nombres = obtenerNombresClientes(mapLista);
        try {

            for (var nombre : nombres) {
                List<List<String>> lFinal = new ArrayList<>();

                var numSerie = 1;
                for (var e : mapLista.entrySet()) {
                    var lPeq = e.getValue();
                    nameCliente = lPeq.get(0);
                    if (nameCliente.contains(nombre)) {
                        Material m = materialDAO.readByCodigo(lPeq.get(5), FacturaPanel.cliente.getRuc());
                        lFinal.add(List.of(lPeq.get(1),
                                Constantes.SUBPARTIDA_FC_MEGA,
                                Constantes.COMPLEMENTARIO_FC,
                                Constantes.SUPLEMENTARIO_FC,
                                String.valueOf(numSerie),
                                "1", m.getCodigo(),
                                m.getSubpartida().split("-")[0],
                                "0000", "0000",
                                m.getDescripcion(),
                                m.getTipoUnidad(),
                                String.valueOf(lPeq.get(3)),
                                "0",
                                String.valueOf(new BigDecimal(lPeq.get(3)).multiply(m.getPorcentajeMerma() != null ? m.getPorcentajeMerma() : new BigDecimal(0)).round(new MathContext(3)))
                        ));

                        numSerie++;
                    }

                }

                generarExcelMP(lFinal, FacturaPanel.cliente, nombre, ruta);

            }
            JOptionPane.showMessageDialog(null, "Material de Produccion generado ");
        }
        catch(Exception e){
            

    }


    }//GEN-LAST:event_generarMediosProduccionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton generarFacturas;
    private javax.swing.JToggleButton generarMediosProduccion;
    private javax.swing.JToggleButton generarProductoTerminado;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
