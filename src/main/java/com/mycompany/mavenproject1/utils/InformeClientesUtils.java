/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.utils;

import com.mycompany.mavenproject1.Constantes;
import com.mycompany.mavenproject1.FCGenerador;
import com.mycompany.mavenproject1.database.model.Cliente;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class InformeClientesUtils {

    private String reporteVentas;
    private String ruta;
    private Cliente cliente;

    public InformeClientesUtils(String reporteVentas, String ruta, Cliente cliente) {
        this.reporteVentas = reporteVentas;
        this.ruta = ruta;
        this.cliente = cliente;
    }

    public List<Map<Integer, List<String>>> convertir(String path) {

        try {
            List<Map<Integer, List<String>>> archivoGeneral = new ArrayList<>();
            FileInputStream file = new FileInputStream(new File(path));
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
            return archivoGeneral;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cargue los insumos");
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public void generarInforme(Date inicio, Date fin) {
        List<Map<Integer, List<String>>> listaFacturas = convertir(reporteVentas);

        List<List<String>> lFinal = new ArrayList<>();

        for (Map<Integer, List<String>> factura : listaFacturas) {
            var si = false;
            var filtro = false;
            Map<String, List<String>> producto = new HashMap<>();
            Map<String, Integer> facturaNro = new HashMap<>();
            System.err.println(factura.get(2));
            var cliente = factura.get(2);
            for (int i = 0; i < factura.size(); i++) {

                var elem = factura.get(i);

                if (elem != null) {

                    if (elem.contains("Fecha Fabricación")) {

                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            if (formato.parse(factura.get(i + 1).get(3)).after(inicio) && formato.parse(factura.get(i + 1).get(3)).before(fin)) {
                                filtro = true;

                            }
                            if (formato.parse(factura.get(i + 1).get(3)).equals(inicio)
                                    || formato.parse(factura.get(i + 1).get(3)).equals(fin)) {
                                filtro = true;

                            }
                        } catch (ParseException ex) {
                            Logger.getLogger(InformeInsumosUtils.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    if (elem.contains("Fecha Fabricación")) {
                        si = true;
                        i++;
                        elem = factura.get(i);
                    }

                    if (si && filtro) {

                        //System.err.println(elem);
                        if (elem.contains("TOTAL")) {
                            break;
                        } else {
                            facturaNro.put(factura.get(4).get(1), 0);
                            producto.put(elem.get(2), List.of(cliente.get(0), elem.get(1), elem.get(3), Constantes.SUBPARTIDA_FC, "U", extraerEnteros(elem.get(10))));
                        }

                    }

                }

            }
            //System.out.println(producto);
            for (Map.Entry<String, List<String>> entry : producto.entrySet()) {
                String textoMaterial = entry.getKey();
                List<String> items = entry.getValue();
                List<String> item = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {

                    if (i == 4) {
                        item.add(textoMaterial);
                        item.add(items.get(i));
                    } else {
                        item.add(items.get(i));

                    }
                }

                lFinal.add(item);

            }

        }

        // generarExcel(lFinal);
        generarExcelFinal(lFinal);
    }

    private void generarExcel(List<List<String>> lFinal) {

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        //List<List<String>> lFinal = new ArrayList<>();
        List<List<String>> cabeceras = List.of(
                List.of("CLIENTE", "No. FACTURA", "FECHA FACTURA", "SUBPARTIDA ARANCELARIA P. TERMINADO", "DESCRIPCIÓN P. TERMINADO", "TIPO UNIDAD", "CANTIDAD ELABORADA")
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

        String fileLocation = this.ruta + ".xls";
        System.out.println(fileLocation);
        try (FileOutputStream outputStream = new FileOutputStream(fileLocation)) {
            workbook.write(outputStream);
            JOptionPane.showMessageDialog(null, "Informe Final Generado");
        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void generarExcelFinal(List<List<String>> lFinal) {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Map<String, List<String>> listaFactura = new HashMap<>();
        //List<List<String>> lFinal = new ArrayList<>();
        List<List<String>> cabeceras = List.of(
                List.of("CLIENTE", "No. FACTURA", "FECHA FACTURA", "SUBPARTIDA ARANCELARIA P. TERMINADO", "DESCRIPCIÓN P. TERMINADO", "TIPO UNIDAD", "CANTIDAD ELABORADA")
        );

        int rowNumber = 0;
        for (List<String> cabecera : cabeceras) {
            Row header = sheet.createRow(rowNumber++);
            for (int i = 0; i < cabecera.size(); i++) {
                header.createCell(i).setCellValue(cabecera.get(i));
            }
        }

        for (List<String> x : lFinal) {
            //Double valorActual=Double.valueOf(x.get(5));
            if (listaFactura.get(x.get(1)) != null) {
                Integer sumaActual = Integer.valueOf(listaFactura.get(x.get(1)).get(6)); // Cambiado a índice 5 para obtener el sexto elemento (x.get(5)).
                Integer nuevaSuma = sumaActual + Integer.valueOf(x.get(6));
                listaFactura.put(x.get(1), List.of(x.get(0), x.get(1), x.get(2), x.get(3), x.get(4), x.get(5), nuevaSuma.toString())); // Convertido nuevaSuma a String antes de ponerlo en la lista.
            } else {
                listaFactura.put(x.get(1), List.of(x.get(0), x.get(1), x.get(2), x.get(3), x.get(4), x.get(5), x.get(6)));
            }

            //listaFactura.put(x.get(1), x);
            /* Row r = sheet.createRow(rowNumber++);
            int k = 0;
            for (String j : x) {
                r.createCell(k++).setCellValue(j);
            }*/
        }
        System.out.println(listaFactura);
        listaFactura.entrySet().stream()
                // ... some other Stream processings
                .forEach(e -> System.out.println(e.getKey() + ":" + e.getValue()));

        /* String fileLocation = this.ruta + ".xls";
        System.out.println(fileLocation);
        try (FileOutputStream outputStream = new FileOutputStream(fileLocation)) {
            workbook.write(outputStream);
            JOptionPane.showMessageDialog(null, "Informe Final Generado");
        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    public String redondear(BigDecimal num) {
        return num.setScale(3, RoundingMode.HALF_UP).toString().replace(".", ",");
    }

    public String extraerEnteros(String num) {
        double numeroComoDouble = Double.parseDouble(num);
        int parteEntera = (int) numeroComoDouble;
        return String.valueOf(parteEntera);
    }
}
