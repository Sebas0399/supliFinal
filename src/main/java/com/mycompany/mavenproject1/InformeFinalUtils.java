/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

import com.mycompany.mavenproject1.database.model.Cliente;
import com.mycompany.mavenproject1.database.model.Material;
import com.mycompany.mavenproject1.database.model.MaterialReporte;
import com.mycompany.mavenproject1.database.repository.MaterialDAO;
import com.mycompany.mavenproject1.database.repository.MaterialReporteDAO;
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
public class InformeFinalUtils {

    private String reporteVentas;
    private String ruta;
    private Cliente cliente;

    public InformeFinalUtils(String reporteVentas, String ruta,Cliente cliente) {
        this.reporteVentas = reporteVentas;
        this.ruta = ruta;
        this.cliente=cliente;
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

        Map<String, Double> cantidadesPorMaterial = new HashMap<>();
        Map<String, Integer> cantidadItemsPorMaterial = new HashMap<>();
        Map<String, List<String>> cantidades = new HashMap<>();

        for (Map<Integer, List<String>> factura : listaFacturas) {
            var si = false;
            var filtro = false;
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
                            Logger.getLogger(InformeFinalUtils.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (elem.contains("Número DAU")) {
                        si = true;
                        i++;
                        elem = factura.get(i);
                    }
                    if (si && filtro) {
                        if (elem.size() > 7) {
                            var descripcion = elem.get(2);
                            var cantidad = Double.parseDouble(elem.get(3));
                            if (cantidadesPorMaterial.containsKey(descripcion)) {
                                double cantidadActual = cantidadesPorMaterial.get(descripcion);
                                cantidadesPorMaterial.put(descripcion, cantidadActual + cantidad);
                                cantidadItemsPorMaterial.put(descripcion, cantidadItemsPorMaterial.getOrDefault(descripcion, 0) + 1);
                                cantidades.put(descripcion, List.of(String.valueOf(cantidadActual + cantidad), elem.get(1)));
                            } else {
                                cantidadesPorMaterial.put(descripcion, cantidad);
                            }

                        }

                    }

                }

            }

        }
        for (Map.Entry<String, Double> entry : cantidadesPorMaterial.entrySet()) {
            String textoMaterial = entry.getKey();
            double cantidad = entry.getValue();
            int cantidadItems = cantidadItemsPorMaterial.getOrDefault(textoMaterial, 0);
        }
        generarExcel(cantidades, cantidadItemsPorMaterial);
    }

    private void generarExcel(Map<String, List<String>> cantidades, Map<String, Integer> cantidadItemsPorMaterial) {
        MaterialDAO materialDAO = new MaterialDAO();
        MaterialReporteDAO materialReporteDAO = new MaterialReporteDAO(HibernateUtil.getSessionFactory());

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<List<String>> lFinal = new ArrayList<>();
        List<List<String>> cabeceras = List.of(
                List.of("Empresa", "Código", "Supbartida", "Descripción", "Tipo Unidad", "Sald de insumos por regularizar", "Numero de items", "Total Consumo", "Saldo Utilizado en el Periodo", "Saldo Actual por Compensar")
        );

        int rowNumber = 0;
        for (List<String> cabecera : cabeceras) {
            Row header = sheet.createRow(rowNumber++);
            for (int i = 0; i < cabecera.size(); i++) {
                header.createCell(i).setCellValue(cabecera.get(i));
            }
        }
        for (Map.Entry<String, List<String>> entry : cantidades.entrySet()) {
            String textoMaterial = entry.getKey();
            int cantidadItems = cantidadItemsPorMaterial.getOrDefault(textoMaterial, 0);
            MaterialReporte mr = materialReporteDAO.readByCodigo(String.valueOf(Double.valueOf(entry.getValue().get(1)).intValue()));
            if (mr != null) {
                Material m = materialDAO.readByCodigo(mr.getCodigoInsumo(),cliente.getRuc());
                var saldoCompensar = m.getSaldoInsumo().subtract(new BigDecimal(cantidadItems).multiply(new BigDecimal(entry.getValue().get(0))));
                var saldoUsado = m.getSaldoInsumo().subtract(saldoCompensar);
                lFinal.add(List.of(m.getCliente().toString(), m.getCodigo(), m.getSubpartida(), textoMaterial, m.getTipoUnidad(), redondear(m.getSaldoInsumo()), String.valueOf(cantidadItems), redondear(new BigDecimal(entry.getValue().get(0))), redondear(saldoUsado), redondear(saldoCompensar)));
                //System.out.println("Texto Material: " + textoMaterial + ", Cantidad Total: " + cantidad + ", Número de Items: " + cantidadItems);
            } else {
                JOptionPane.showMessageDialog(null, "No existe el material :" + String.valueOf(Double.valueOf(entry.getValue().get(1)).intValue()));
            }

        }
        for (List<String> x : lFinal) {
            Row r = sheet.createRow(rowNumber++);
            int k = 0;
            for (String j : x) {
                r.createCell(k++).setCellValue(j);
            }
        }

        String fileLocation = this.ruta  + ".xls";
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

    public String redondear(BigDecimal num) {
        return num.setScale(3, RoundingMode.HALF_UP).toString().replace(".", ",");
    }
}
