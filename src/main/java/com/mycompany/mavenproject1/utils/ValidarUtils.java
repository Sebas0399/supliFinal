/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.utils;

import com.mycompany.mavenproject1.FCGenerador;
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
 * @author sebas
 */
public class ValidarUtils {

    private String ruta;

    public ValidarUtils(String ruta) {
        this.ruta = ruta;
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

    public Boolean validar() {

        List<Map<Integer, List<String>>> listaFacturas = convertir(ruta);
        var valido=true;
        for (Map<Integer, List<String>> factura : listaFacturas) {
            var si = false;
            Map<String, Integer> facturaNro = new HashMap<>();

            for (int i = 0; i < factura.size(); i++) {

                var elem = factura.get(i);

                if (elem != null) {

                    if (elem.contains("Fecha Fabricación")) {
                        si = true;
                        i++;
                        elem = factura.get(i);
                    }

                    if (si) {

                        if (elem.contains("TOTAL") && elem.size() < 4) {

                            break;
                        } else if (elem.contains("TOTAL") && elem.size() > 4) {
                            facturaNro.put(elem.get(1), 0);
                            break;

                        } else {

                            facturaNro.put(elem.get(1), 0);

                        }

                    }

                }

            }
            if (facturaNro.size() >= 2) {
                JOptionPane.showMessageDialog(null, "Error en la factura Nro " + facturaNro.keySet().toString());
                valido=false;
            }

        }
        return valido;
    }

}
