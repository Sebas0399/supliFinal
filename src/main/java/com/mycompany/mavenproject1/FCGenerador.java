/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

import com.mycompany.mavenproject1.database.DAO.ReporteDAO;
import com.mycompany.mavenproject1.utils.FileUtils;
import com.mycompany.mavenproject1.database.model.Cliente;
import com.mycompany.mavenproject1.database.model.Reporte;
import com.mycompany.mavenproject1.utils.HibernateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

/**
 *
 * @author Sebas
 */
public class FCGenerador {

   
    private String savePath;
    private Cliente cliente;
    ReporteDAO reporteDAO;

    public FCGenerador( Cliente cliente) {
        
        this.cliente = cliente;
        reporteDAO=new ReporteDAO(HibernateUtil.getSessionFactory());
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
        var listadoRUC = archivoGeneral.stream().map(x -> (x.get(2).get(0))).distinct().toList();
        var listRuc = obtenerRucs(listadoRUC);

        var listadoTotal = listRuc.stream().map(x -> getFacturasPorRuc(x, archivoGeneral)).toList();

        listadoTotal.forEach(x -> {

            var lFinal = new ArrayList<List<String>>();
            x.forEach(y -> {
                var l = y.get(4);
                var p = List.of(Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, StringUtils.agregarGuiones(l.get(1)), l.get(3));
                lFinal.add(p);
            });
            generarExcel(lFinal, x.get(0).get(2).get(0));

        });
        JOptionPane.showMessageDialog(null, "Facturas Generadas");
    }

    private void generarExcel(List<List<String>> lFinal, String nombreRuc) {

        Set<List<String>> lFinalSet = new HashSet<>(lFinal);
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

        for (List<String> x : lFinalSet) {
            Row r = sheet.createRow(rowNumber++);
            int k = 0;
            for (String j : x) {
                r.createCell(k++).setCellValue(j);
            }
        }

        String fileLocation = this.savePath + "\\" + StringUtils.tranformarNombre(cliente.getNombre()) + "_FC_" + nombreRuc + ".xls";

        try (FileOutputStream outputStream = new FileOutputStream(fileLocation)) {
            workbook.write(outputStream);

        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Ocurrio un errror");

        }
    }

    public void cargarFacturas() {
        this.savePath = FileUtils.saveData("Guardar Factura");
        if (this.savePath != null) {
            try {
                List<Map<Integer, List<String>>> archivoGeneral = new ArrayList<>();
                Reporte reporte=this.reporteDAO.read();
                FileInputStream file = new FileInputStream(new File(reporte.getRuta()));

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
                                        data.get(j).add(cell.getNumericCellValue() + "");
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
                JOptionPane.showMessageDialog(null, "Cargue el reporte de produccion");
                Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
