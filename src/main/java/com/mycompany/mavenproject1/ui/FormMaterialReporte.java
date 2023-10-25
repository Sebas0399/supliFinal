/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.mavenproject1.ui;

import com.mycompany.mavenproject1.FCGenerador;
import com.mycompany.mavenproject1.HibernateUtil;
import com.mycompany.mavenproject1.database.model.Cliente;
import com.mycompany.mavenproject1.database.model.Material;
import com.mycompany.mavenproject1.database.model.MaterialReporte;
import com.mycompany.mavenproject1.database.repository.ClienteDAO;
import com.mycompany.mavenproject1.database.repository.MaterialDAO;
import com.mycompany.mavenproject1.database.repository.MaterialReporteDAO;
import com.mycompany.mavenproject1.tablas.TodosMaterialReporte;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.poi.ss.usermodel.Cell;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Sebas
 */
public class FormMaterialReporte extends javax.swing.JPanel {

    private Map<String, String> paths;
    List<Material> insumos;
    MaterialReporteDAO mr = new MaterialReporteDAO(HibernateUtil.getSessionFactory());
    MaterialDAO md = new MaterialDAO();
    MaterialReporte materialReporte;
    List<String> codigos;

    /**
     * Creates new form FormMaterialReporte
     */
    public FormMaterialReporte() {

        initComponents();
        cargarDatos();
        this.paths = EntradaPanel.paths;
        if (this.paths.isEmpty()) {
            //JOptionPane.showMessageDialog(null, "Cargue el reporte de produccion");
            return;

        }

        this.codigos = generarFacturaExcel(cargarFacturas());
        this.codigos.stream().forEach(x -> this.comboMaterial.addItem(x));
        this.codigos.stream().forEach(x -> mr.create(new MaterialReporte(x)));

    }

    public FormMaterialReporte(String codigo) {

        initComponents();
        cargarDatos();
        this.paths = EntradaPanel.paths;
        if (this.paths.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Cargue el reporte de produccion");
            return;
        }
        this.codigos = generarFacturaExcel(cargarFacturas());
        //Guardar todos los codigos

        this.btnGuardar.setText("Actualizar");
        materialReporte = mr.readByCodigo(codigo);
       
        Supplier<Stream<Material>>  itemSelected = ()->this.insumos.stream().filter(x -> x.getDescripcion().equals(materialReporte.getDescripcion()));
        if (itemSelected.get().findAny().isPresent()) {
            this.comboInsumos.setSelectedItem(itemSelected.get().findAny().get());

        }
        this.codigos.stream().forEach(x -> this.comboMaterial.addItem(x));
        this.comboMaterial.setSelectedItem(codigo);

    }

    private void cargarDatos() {

        insumos = md.readAll();
        insumos.stream().forEach(x -> this.comboInsumos.addItem(x));

    }

    private static String obtenerRuc(Map<Integer, List<String>> factura) {
        return factura.get(2).get(0).split(" - ")[1];
    }

    private List<String> generarFacturaExcel(List<Map<Integer, List<String>>> archivoGeneral) {
        Map<String, List<Map<Integer, List<String>>>> rucFacturas = new HashMap<>();
        var c = 0;
        for (Map<Integer, List<String>> factura : archivoGeneral) {
            //c++;
            if (factura.containsKey(2)) {
                String ruc = obtenerRuc(factura);
                if (!rucFacturas.containsKey(ruc)) {
                    rucFacturas.put(ruc, new ArrayList<>());
                }
                rucFacturas.get(ruc).add(factura);
            }
        }
        //generarMP(rucFacturas);
        Map<String, Integer> codigoSumaMap = new HashMap<>();
        for (Map.Entry<String, List<Map<Integer, List<String>>>> entry : rucFacturas.entrySet()) {
            c++;
            String ruc = entry.getKey();
            List<Map<Integer, List<String>>> facturas = entry.getValue();

            var contadorSerie = 1;
            for (Map<Integer, List<String>> factura : facturas) {

                var si = false;

                for (int i = 0; i <= factura.size() + 1; i++) {
                    var elem = factura.get(i);

                    if (elem != null) {
                        if (elem.contains("Número DAU")) {
                            si = true;
                            i++;
                            elem = factura.get(i);
                        }
                        if (si) {

                            if (elem.size() > 7) {
                                var codigo = String.valueOf((int) Double.parseDouble(elem.get(1)));

                                codigoSumaMap.put(codigo, 0);

                            }

                        }
                    }
                }

                contadorSerie++;
            }

        }

        Set<String> keys = codigoSumaMap.keySet();

        // Convertir el conjunto de claves a una lista
        //this.codigos = new ArrayList<>(keys);
        return new ArrayList<>(keys);
    }

    public List<Map<Integer, List<String>>> cargarFacturas() {

        try {

            List<Map<Integer, List<String>>> archivoGeneral = new ArrayList<>();
            FileInputStream file = new FileInputStream(new File(this.paths.get("RP")));
            Workbook workbook = new XSSFWorkbook(file);
            int numHojas = workbook.getNumberOfSheets();

            for (int i = 0; i <= numHojas - 1; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Map<Integer, List<String>> hojaData = new HashMap<>();  // Crear un Map para cada hoja

                for (Row row : sheet) {
                    List<String> rowData = new ArrayList<>();

                    for (Cell cell : row) {
                        switch (cell.getCellType()) {
                            case STRING ->
                                rowData.add(cell.getRichStringCellValue().getString());
                            case NUMERIC -> {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    rowData.add(sdf.format(cell.getDateCellValue()));
                                } else {
                                    rowData.add(String.valueOf(cell.getNumericCellValue()));
                                }
                            }
                            case BOOLEAN ->
                                rowData.add(String.valueOf(cell.getBooleanCellValue()));
                            case FORMULA ->
                                rowData.add(cell.getCellFormula());

                            default ->
                                System.out.print("");
                        }
                    }

                    hojaData.put(row.getRowNum(), rowData);  // Agregar la lista a la posición del mapa
                }

                archivoGeneral.add(hojaData); // Agregar el mapa de la hoja al listado general
            }

            return (archivoGeneral);

        } catch (IOException ex) {
            JOptionPane.showConfirmDialog(null, "Cargue el reporte de producción");
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public String redondear(BigDecimal num) {
        return num.setScale(3, RoundingMode.HALF_UP).toString().replace(".", ",");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGuardar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        comboInsumos = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        comboMaterial = new javax.swing.JComboBox<>();

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel1.setText("Insumo");

        jLabel2.setText("Material");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboInsumos, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(comboMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGuardar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboInsumos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar))
                .addContainerGap(10, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        MaterialReporte material = new MaterialReporte();

        ClienteDAO cd = new ClienteDAO();
        Cliente client = cd.readByRuc(insumos.get(this.comboInsumos.getSelectedIndex()).getCliente().getRuc());
        material.setCliente(client);
        material.setCodigo(this.comboMaterial.getSelectedItem().toString());
        material.setCodigoInsumo(insumos.get(this.comboInsumos.getSelectedIndex()).getCodigo());
        material.setDescripcion(insumos.get(this.comboInsumos.getSelectedIndex()).getDescripcion());
        if (btnGuardar.getText().contains("Actualizar")) {
            mr.update(material, materialReporte.getCodigo());
            JOptionPane.showMessageDialog(null, "Se actualizo correctamente");
        } else {
            mr.create(material);
            JOptionPane.showMessageDialog(null, "Se creo correctamente");

        }
        TodosMaterialReporte.cargarDatos();

    }//GEN-LAST:event_btnGuardarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<Material> comboInsumos;
    private javax.swing.JComboBox<String> comboMaterial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
