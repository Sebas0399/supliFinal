/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
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
public class PTGenerador {

    private Map<String, String> paths;
    private String savePath;

    public PTGenerador(Map<String, String> paths) {
        this.paths = paths;
    }

    private List<String> extraerNumeros(String input) {
        List<String> numeros = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d{13}");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            numeros.add(matcher.group());
        }

        return numeros;
    }

    private List<String> obtenerRucs(List<String> rucs) {
        List<String> numeros = new ArrayList<>();
        for (var input : rucs) {
            numeros.addAll(extraerNumeros(input));
        }
        return numeros;
    }

    private List<Map<Integer, List<String>>> getFacturasPorRuc(String ruc, List<Map<Integer, List<String>>> archivoGeneral) {
        List<Map<Integer, List<String>>> res = new ArrayList<>();
        for (var factura : archivoGeneral) {
            var r = factura.get(2).get(0).split("-");

            if (r[r.length - 1].trim().equals(ruc.trim())) {
                res.add(factura);
            }
        }

        return res;
    }

    private void generarFacturaExcel(List<Map<Integer, List<String>>> archivoGeneral) {
        var listadoRUC = archivoGeneral.parallelStream().map(x -> (x.get(2).get(0))).distinct().toList();
        var listRuc = obtenerRucs(listadoRUC);

        var listadoTotal = listRuc.parallelStream().map(x -> getFacturasPorRuc(x, archivoGeneral)).toList();

        

        listadoTotal.forEach(x -> {
            var lFinal = new ArrayList<List<String>>();
            var contadorSerie = 1;

            for (var y : x) {
                var seguir = true;
                var contadorFila = 4;
                var contadorItem = 1;

                while (seguir) {
                    var fila = y.get(contadorFila);
                    if (fila.contains("TOTAL")) {
                        seguir = false;
                        contadorSerie++;
                    } else {
                        var p = List.of(StringUtils.agregarGuiones(fila.get(1)), String.valueOf(contadorSerie), String.valueOf(contadorItem), Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, fila.get(6), "U", fila.get(10));
                        lFinal.add(p);
                        contadorItem++;
                        contadorFila++;
                    }
                }
                generarExcel(lFinal, x.get(0).get(2).get(0));
              

               
            }
        });

        JOptionPane.showMessageDialog(null, "Producto Terminado Generadas");
    }

    private void generarExcel(List<List<String>> lFinal, String nombreRuc) {
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

        String fileLocation = this.savePath + "\\" + "CRA_PT_" + nombreRuc + ".xls";
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
        this.savePath = FileUtils.saveData();
        if(this.savePath!=null){
            try {
            List<Map<Integer, List<String>>> archivoGeneral = new ArrayList<>();
            FileInputStream file = new FileInputStream(new File(this.paths.get("RP")));
            Workbook workbook = new XSSFWorkbook(file);
            int numHojas = workbook.getNumberOfSheets();

            for (int i = 0; i <= numHojas - 1; i++) {
                Sheet sheet = workbook.getSheetAt(i);

                Map<Integer, List<String>> data = new HashMap<>();
                int j = 0;
                for (Row row : sheet) {
                    data.put(j, new ArrayList<>());
                    for (Cell cell : row) {
                        switch (cell.getCellType()) {
                            case STRING ->
                                data.get(j).add(cell.getRichStringCellValue().getString());
                            case NUMERIC -> {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    data.get(j).add(sdf.format(cell.getDateCellValue()) + "");
                                } else {
                                    data.get(j).add((int) cell.getNumericCellValue() + "");
                                }
                            }
                            case BOOLEAN ->
                                data.get(j).add(cell.getBooleanCellValue() + "");
                            case FORMULA ->
                                data.get(j).add(cell.getCellFormula() + "");
                            default ->
                                data.get(j).add(" ");
                        }
                    }
                    j++;
                }
                archivoGeneral.add(data);
            }

           

            Thread generarFacturasThread = new Thread(() -> {
                generarFacturaExcel(archivoGeneral);
            });
            generarFacturasThread.start();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cargue el reporte de producción");
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
    }
}
