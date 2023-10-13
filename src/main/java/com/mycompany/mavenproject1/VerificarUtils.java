/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
public class VerificarUtils {

    private String listadoVentas;
    private String reporteProduccion;

    public VerificarUtils(String listadoVentas, String reporteProduccion) {
        this.listadoVentas = listadoVentas;
        this.reporteProduccion = reporteProduccion;
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

            return archivoGeneral;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cargue los insumos");
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public void getListadoVentas() {

    }

    public void getReportedeVentas() {
        //System.out.println(convertir(reporteProduccion));
        agrupar();

    }

    public void verificarTotales() {

    }

    private static List<String> obtenerNroFactura(Map<Integer, List<String>> factura) {
        var nROFactura = factura.get(4).get(1);
        var total = "0";
        for (Map.Entry<Integer, List<String>> entry : factura.entrySet()) {

            for (var u : entry.getValue()) {
                if (u.contains("TOTAL")) {
                    total = entry.getValue().get(entry.getValue().size() - 1);
                }
            }
        }

        return List.of(nROFactura, total);
    }

    public void agrupar() {
        var l = convertir(reporteProduccion);
        var reporteListado = new ArrayList<List<String>>();
        for (var u : l) {
            reporteListado.add(obtenerNroFactura(u));
        }
        var m = convertir(listadoVentas);
        var reporteListadoVenta = new ArrayList<List<String>>();

        for (var x : m) {

            obtenerNroFacturaListado(x).stream().forEach(q -> reporteListadoVenta.add(q));
        }
        System.out.println("Reporte");
        System.out.println(reporteListado);
        for(var w:reporteListadoVenta){
            var found=false;
            for(var y:reporteListado){
                if(y.get(0)==w.get(0)&&y.get(1)==w.get(1)){
                    found=true;
                }
            }
            if(!found){
                System.out.println("No se encotro "+w);
            }
        }
        System.out.println("Reporte");
        System.out.println(reporteListadoVenta);

    }

    public List<List<String>> obtenerNroFacturaListado(Map<Integer, List<String>> factura) {
        var nROFactura = "";
        var total = "0";
        var contador = 0;
        var listaFinal = new ArrayList<List<String>>();
        for (Map.Entry<Integer, List<String>> entry : factura.entrySet()) {
            contador++;

            nROFactura = entry.getValue().get(1);
            total = entry.getValue().get(entry.getValue().size() - 1);
            listaFinal.add(List.of(nROFactura, total));

        }
        //System.out.println(contador);

        return listaFinal;
    }

}
