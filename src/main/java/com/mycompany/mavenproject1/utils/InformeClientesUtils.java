/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.utils;

import com.mycompany.mavenproject1.Constantes;
import com.mycompany.mavenproject1.FCGenerador;
import com.mycompany.mavenproject1.StringUtils;
import com.mycompany.mavenproject1.database.DAO.MaterialDAO;
import com.mycompany.mavenproject1.database.DAO.MaterialReporteDAO;
import com.mycompany.mavenproject1.database.DAO.ReporteDAO;
import com.mycompany.mavenproject1.database.model.Cliente;
import com.mycompany.mavenproject1.database.model.Reporte;
import com.mycompany.mavenproject1.ui.FacturaPanel;
import java.awt.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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
import org.hibernate.engine.spi.ExecutableList;

/**
 *
 * @author Sebas
 */
public class InformeClientesUtils {

    private String ruta;
    private Cliente cliente;
    private ReporteDAO reporteDAO;
    private MaterialReporteDAO materialReporteDAO;

    public InformeClientesUtils(String ruta, Cliente cliente) {
        this.ruta = ruta;
        this.cliente = cliente;
        this.reporteDAO = new ReporteDAO(HibernateUtil.getSessionFactory());
        this.materialReporteDAO = new MaterialReporteDAO(HibernateUtil.getSessionFactory());

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

    private Boolean verificarFactura(Map<Integer, List<String>> factura) {

        List<List<String>> valore = new ArrayList<>();

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
                if (si && elem != null) {

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
        return suma != 0; // JOptionPane.showMessageDialog(null, "La factura " + factura.get(4).get(1) + "no tiene materiales");
        // contadorFila++;
    }

    public void generarInforme(Date inicio, Date fin) {
        Reporte reporte = reporteDAO.read();
        List<Map<Integer, List<String>>> listaFacturas = convertir(reporte.getRuta());

        List<List<String>> lFinal = new ArrayList<>();

        for (Map<Integer, List<String>> factura : listaFacturas) {

            var si = false;
            var filtro = false;
            Map<String, List<String>> producto = new HashMap<>();
            Map<String, Integer> facturaNro = new HashMap<>();
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
                            facturaNro.put(factura.get(4).get(1), 0);
                            var elemAux = factura.get(i - 1);
                            var contieneElementos = verificarFactura(factura);
                            var contieneElementosAux = contieneElementos ? "" : "No contiene";
                            producto.put(elemAux.get(2), List.of(cliente.get(0), elemAux.get(1), elemAux.get(3), Constantes.SUBPARTIDA_FC, "U", extraerEnteros(elem.get(2)), contieneElementosAux));
                            break;
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

    private void generarExcelFinal(List<List<String>> lFinal) {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Map<String, List<String>> listaFactura = new HashMap<>();
        //List<List<String>> lFinal = new ArrayList<>();
        List<List<String>> cabeceras = List.of(
                List.of("CLIENTE", "No. FACTURA", "FECHA FACTURA", "SUBPARTIDA ARANCELARIA P. TERMINADO", "DESCRIPCIÓN P. TERMINADO", "TIPO UNIDAD", "CANTIDAD ELABORADA", "CONTIENE MATERIAL")
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
                listaFactura.put(x.get(1), List.of(x.get(0), x.get(1), x.get(2), x.get(3), x.get(4), x.get(5), nuevaSuma.toString(), x.get(6))); // Convertido nuevaSuma a String antes de ponerlo en la lista.
            } else {
                listaFactura.put(x.get(1), List.of(x.get(0), x.get(1), x.get(2), x.get(3), x.get(4), x.get(5), x.get(6), x.get(7)));
            }

            //listaFactura.put(x.get(1), x);
            /* Row r = sheet.createRow(rowNumber++);
            int k = 0;
            for (String j : x) {
                r.createCell(k++).setCellValue(j);
            }*/
        }
        System.out.println(listaFactura);
        var listaOrdenada = new ArrayList<>(listaFactura.entrySet());
        listaOrdenada.sort(Comparator.comparing(entry -> entry.getValue().get(0)));
        Map<String, List<String>> listaFinalOrdenada = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : listaOrdenada) {
            listaFinalOrdenada.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, List<String>> x : listaFinalOrdenada.entrySet()) {
            Row r = sheet.createRow(rowNumber++);
            int k = 0;
            for (String j : x.getValue()) {
                r.createCell(k++).setCellValue(j);
            }
        }

        String fileLocation = this.ruta + ".xls";
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

    public String redondear(BigDecimal num) {
        return num.setScale(3, RoundingMode.HALF_UP).toString().replace(".", ",");
    }

    public String extraerEnteros(String num) {
        double numeroComoDouble = Double.parseDouble(num);
        int parteEntera = (int) numeroComoDouble;
        return String.valueOf(parteEntera);
    }

    //MEGASTOCK
    public void generarInforme(Date inicio, Date fin, boolean m) {
        var tabla = FacturaPanel.tableFacturas.getModel();
        var rowCount = tabla.getRowCount();
        var colCount = tabla.getColumnCount();
        System.out.println("Filas");
        System.out.println(rowCount);
        System.out.println("Columnas");
        System.out.println(colCount);
        //CLIENTE,No. FACTURA,FECHA 
        //FACTURA,SUBPARTIDA ARANCELARIA P. TERMINADO,DESCRIPCIÓN P. 
        //TERMINADO,TIPO UNIDAD,CANTIDAD ELABORADA

        List<List<String>> data = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            List<String> peq = new ArrayList<>();
            peq.add((String) tabla.getValueAt(i, 1));
            peq.add((String) tabla.getValueAt(i, 2));
            peq.add((String) tabla.getValueAt(i, 3));
            
            peq.add("Subapartida");
            peq.add("Descripcion");
            peq.add((String) tabla.getValueAt(i, 7));
            peq.add((String) tabla.getValueAt(i, 4));
            peq.add("");
           
            data.add(peq);
        }
        generarExcelFinal(data);
    }
}
