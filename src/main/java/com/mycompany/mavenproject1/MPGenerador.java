/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author Sebas
 */
import com.mycompany.mavenproject1.utils.FileUtils;
import com.mycompany.mavenproject1.utils.HibernateUtil;
import com.mycompany.mavenproject1.database.model.Cliente;
import com.mycompany.mavenproject1.database.DAO.MaterialDAO;
import com.mycompany.mavenproject1.database.DAO.MaterialReporteDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
public class MPGenerador {

    private Map<String, String> paths;
    private MaterialReporteDAO materialReporteDAO = new MaterialReporteDAO(HibernateUtil.getSessionFactory());
    private MaterialDAO materialDAO = new MaterialDAO();
    private String savePath;
    private Cliente cliente;

    public MPGenerador(Map<String, String> paths, Cliente cliente) {
        this.paths = paths;
        this.cliente = cliente;
    }

    private static String obtenerRuc(Map<Integer, List<String>> factura) {
        return factura.get(2).get(0).split(" - ")[1];
    }

    private void generarFacturaExcel(List<Map<Integer, List<String>>> archivoGeneral) {
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

        for (Map.Entry<String, List<Map<Integer, List<String>>>> entry : rucFacturas.entrySet()) {
            c++;
            String ruc = entry.getKey();
            List<Map<Integer, List<String>>> facturas = entry.getValue();
            List<List<String>> valore = new ArrayList<>();
            var contadorSerie = 1;
            for (Map<Integer, List<String>> factura : facturas) {
                var contadorFila = 1;
                var si = false;
                var suma = 0.0;
                Map<String, Double> codigoSumaMap = new HashMap<>();

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
                                var materialReporte = this.materialReporteDAO.readByCodigo(codigo);
                                if (materialReporte != null) {
                                    var sumaActual = codigoSumaMap.getOrDefault(codigo, 0.0);
                                    codigoSumaMap.put(codigo, sumaActual + Double.valueOf(elem.get(3)));

                                }

                                suma = suma + Double.parseDouble(elem.get(3));
                            }

                        }
                    }
                }
                if(suma==0){
                    JOptionPane.showMessageDialog(null, "La factura "+factura.get(5).get(1)+"no tiene materiales");
                    break;
                }
                var almidon = this.materialDAO.readLike("ALMIDON", cliente.getRuc());
                if (almidon != null) {
                    String almidonTotal = (almidon.getCoeficienteConsumo() != null
                            ? redondear(new BigDecimal(suma).multiply(almidon.getCoeficienteConsumo()))
                            : "");
                    var almidonFila = List.of(String.valueOf(StringUtils.agregarGuiones(factura.get(5).get(1))), Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, String.valueOf(contadorSerie), String.valueOf(contadorFila), almidon.getCodigo(), almidon.getSubpartida(), "0000", "0000", almidon.getDescripcion(), almidon.getTipoUnidad(), almidonTotal, "0", "0");
                    contadorFila++;
                    valore.add(almidonFila);
                }

                var cera = this.materialDAO.readLike("CERA", cliente.getRuc());
                if (cera != null) {
                    String ceraTotal = (cera.getCoeficienteConsumo() != null
                            ? redondear(new BigDecimal(suma).multiply(cera.getCoeficienteConsumo()))
                            : "");
                    var ceraFila = List.of(String.valueOf(StringUtils.agregarGuiones(factura.get(5).get(1))), Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, String.valueOf(contadorSerie), String.valueOf(contadorFila), cera.getCodigo(), cera.getSubpartida(), "0000", "0000", cera.getDescripcion(), cera.getTipoUnidad(), ceraTotal, "0", "0");
                    contadorFila++;
                    valore.add(ceraFila);

                }

                for (Map.Entry<String, Double> entrada : codigoSumaMap.entrySet()) {
                    var codigo = entrada.getKey();
                    var sumaProd = entrada.getValue();

                    var materialReporte = this.materialReporteDAO.readByCodigo(codigo);
                    if (materialReporte == null) {
                        valore.add(List.of(StringUtils.agregarGuiones(factura.get(5).get(1)), Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, String.valueOf(contadorSerie), String.valueOf(contadorFila), "null", "null", "0000", "0000", "null", "null", String.valueOf(sumaProd), "0", "0"));
                        JOptionPane.showMessageDialog(null, "Error no exite el codigo de material: " + codigo);
                        break;
                    } else {

                        var material = this.materialDAO.readByCodigo(materialReporte.getCodigoInsumo(), cliente.getRuc());
                        System.out.println(material);
                        valore.add(List.of(StringUtils.agregarGuiones(factura.get(5).get(1)), Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, String.valueOf(contadorSerie), String.valueOf(contadorFila), material.getCodigo(), material.getSubpartida(), "0000", "0000", material.getDescripcion(), material.getTipoUnidad(), redondear(new BigDecimal(sumaProd)), redondear(new BigDecimal(sumaProd).divide(new BigDecimal(10))), "0"));

                    }
                    contadorFila++;
                }
                generarExcel(valore, factura.get(2));

                contadorSerie++;
            }

        }
        JOptionPane.showMessageDialog(null, "Medio de Produccion Generadas");

    }

    private void generarExcel(List<List<String>> lFinal, List<String> ruc) {

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
                r.createCell(k++).setCellValue(j);
            }
        }

        String fileLocation = this.savePath + "\\" + "CRA_MP_" + ruc.get(0) + ".xls";
        try (FileOutputStream outputStream = new FileOutputStream(fileLocation)) {
            workbook.write(outputStream);
        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarFacturas() {
        this.savePath = FileUtils.saveData("Guardar Material De Produccion");
        if (this.savePath != null) {
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

                generarFacturaExcel(archivoGeneral);

            } catch (IOException ex) {
                JOptionPane.showConfirmDialog(null, "Cargue el reporte de producción");
                Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public String redondear(BigDecimal num) {
        return num.setScale(3, RoundingMode.HALF_UP).toString().replace(".", ",");
    }

}
