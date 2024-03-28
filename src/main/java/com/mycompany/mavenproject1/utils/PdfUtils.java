package com.mycompany.mavenproject1.utils;

import com.mycompany.mavenproject1.Constantes;
import com.mycompany.mavenproject1.FCGenerador;
import com.mycompany.mavenproject1.StringUtils;
import com.mycompany.mavenproject1.database.DAO.FacturaDAO;
import com.mycompany.mavenproject1.database.model.Cliente;
import com.mycompany.mavenproject1.database.model.Factura;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Administrator
 */
public class PdfUtils {

    FacturaDAO facturaDAO;
    private Cliente cliente;

    public PdfUtils(Cliente cliente) {
        this.cliente = cliente;
        facturaDAO = new FacturaDAO(HibernateUtil.getSessionFactory());
    }

    public Map<String, List<String>> cargarPDFs() throws IOException {
        var files = FileUtils.cargarDataPdf("Cargar pdf");
        Map<String, List<String>> facturas = new HashMap<>();
        for (var file : files) {

            PDDocument document = Loader.loadPDF(file);
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            facturas.put(file.getAbsolutePath(), List.of(text.split("\\r?\\n")));
            document.close();
        }
       // guardarFacturas(facturas);
        return facturas;
    }

    public void guardarFacturas(Map<String, List<String>> facturas) {
        List<List<String>> elementosFactura = new ArrayList<>();
        var initContador = 34;
        for (var factura : facturas.entrySet()) {

            List<String> elementos=new ArrayList<>();
            var fecha = StringUtils.formatearFecha(factura.getValue().get(1));
            var numeroFactura = factura.getValue().get(17);
            var clienteRuc = cliente.getRuc();
            var ruta = factura.getKey();
            Factura f = new Factura();
            f.setNroFactura(numeroFactura);
            f.setRucCliente(clienteRuc);
            facturaDAO.create(f);

            elementosFactura.add(List.of(Constantes.SUBPARTIDA_FC, Constantes.COMPLEMENTARIO_FC, Constantes.SUPLEMENTARIO_FC, numeroFactura, fecha));
            for (int i = initContador; i < factura.getValue().size(); i++) {
                if (factura.getValue().get(i).trim().contains("SUBTOTAL")) {
                    break;
                } else {
                    elementos.add(factura.getValue().get(i));
                }
            }
           // String[] elementos = factura.getValue().get(34).split("\\s+");

            System.out.println(factura);
                        

            // Imprimir los elementos
           
            for (var elemento : elementos) {
                System.out.println(elemento);
                
            }
        }
        // generarExcel(elementosFactura, cliente, FileUtils.saveData("Guardar Facturas"));
    }

    public void generarExcelFactura(List<List<String>> lFinal, Cliente cliente, String path) {

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

        for (List<String> x : lFinal) {
            Row r = sheet.createRow(rowNumber++);
            int k = 0;
            for (String j : x) {

                r.createCell(k++).setCellValue(j);

            }
        }

        String fileLocation = path + "\\" + StringUtils.tranformarNombre(cliente.getNombre()) + "_FC_" + cliente.getRuc() + ".xls";

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
public void generarExcel(List<List<String>> lFinal, Cliente cliente, String path) {

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

        for (List<String> x : lFinal) {
            Row r = sheet.createRow(rowNumber++);
            int k = 0;
            for (String j : x) {

                r.createCell(k++).setCellValue(j);

            }
        }

        String fileLocation = path + "\\" + StringUtils.tranformarNombre(cliente.getNombre()) + "_FC_" + cliente.getRuc() + ".xls";

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
}
