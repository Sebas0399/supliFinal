/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

import com.mycompany.mavenproject1.database.model.Cliente;
import com.mycompany.mavenproject1.database.model.Material;
import com.mycompany.mavenproject1.database.repository.MaterialDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
/**
 *
 * @author Sebas
 */
public class InsumosUtils {

    MaterialDAO materialDAO;
    Map<String, String> paths;
    Cliente cliente;

    public InsumosUtils(Map<String, String> paths,Cliente cliente) {
        materialDAO = new MaterialDAO();
        this.paths = paths;
        this.cliente=cliente;
    }

    public List<Map<Integer, List<String>>> convertirInsumos() {

        try {
            List<Map<Integer, List<String>>> archivoGeneral = new ArrayList<>();
            FileInputStream file = new FileInputStream(new File(this.paths.get("IN")));
            Workbook workbook = new HSSFWorkbook(file);
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

    public Boolean saveAllInsumos() {
        List<Map<Integer, List<String>>> archivoGeneral = this.convertirInsumos();
        materialDAO.deleteAll();
        for (var hoja : archivoGeneral) {
            for (int i = 2; i < hoja.size(); i++) {
                var fila = hoja.get(i);
                Material material = new Material();
               
                material.setCliente(cliente);
                material.setCodigo(fila.get(0));
                material.setSubpartida(fila.get(1));
                material.setDescripcion(fila.get(2));
                material.setSaldoInsumo(new BigDecimal(convertir(fila.get(6))));
                material.setTipoUnidad(fila.get(3));
                material.setCalculaDesperdicio(false);
                material.setAplicaFormula(false);
               
                materialDAO.create(material);
            }
        }
        return null;
    }

    public String convertir(String numero) {
        var numeros = numero.toCharArray();
        StringBuilder nuevoNumero=new StringBuilder();
                
        for (var num : numeros) {
            switch (num) {
                case '.' -> {
                }
                case ',' -> nuevoNumero.append('.');
                default -> nuevoNumero.append(num);
            }
        }
        return nuevoNumero.toString();
    }

}
