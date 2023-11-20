/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.utils;

import com.mycompany.mavenproject1.FCGenerador;
import com.mycompany.mavenproject1.database.DAO.ReporteDAO;
import com.mycompany.mavenproject1.database.model.Reporte;
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
 * @author Administrator
 */
public class ValidarFacturaUtils {

    private String path;
    private ReporteDAO reporteDAO;
    private List<Map<Integer, List<String>>> facturas;
    private List<Map<Integer, List<String>>> facturasValido;

    public ValidarFacturaUtils(String path) {
        this.reporteDAO = new ReporteDAO(HibernateUtil.getSessionFactory());
        this.path = path;
        this.facturas = new ArrayList<>();
        this.facturasValido = new ArrayList<>();
    }

    public Boolean validar() {
        cargarFacturas();
        cargarListadoFacturas();
        extraerFacturas();
        return null;

    }

    public void cargarListadoFacturas() {

        try {

            FileInputStream file = new FileInputStream(new File(this.path));

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
                facturasValido.add(data);
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cargue el reporte de produccion");
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void extraerFacturas() {
        var listadoFacturas = new HashMap<String, Integer>();
        var listadoFacturasValidas = new HashMap<String, Integer>();

        for (var factura : facturas) {
            var capturarFactura = false;
            for (var contenido : factura.entrySet()) {
                if (capturarFactura) {
                    listadoFacturas.put(contenido.getValue().get(1), 0);
                }
                if (contenido.getValue().contains("Pedido")) {
                    capturarFactura = true;

                }

            }
        }
         for (var factura : facturasValido) {
            var capturarFactura = false;
            for (var contenido : factura.entrySet()) {
                if (capturarFactura) {
                    listadoFacturasValidas.put(contenido.getValue().get(1), 0);
                }
                    if (contenido.getValue().contains("SRI")) {
                    capturarFactura = true;

                }

            }
        }
        System.out.println(listadoFacturas);
                System.out.println(lista);

        
    }

    public void cargarFacturas() {

        try {
            Reporte reporte = this.reporteDAO.read();
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
                facturas.add(data);
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cargue el reporte de produccion");
            Logger.getLogger(FCGenerador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
