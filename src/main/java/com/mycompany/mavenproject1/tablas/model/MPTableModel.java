/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.tablas.model;

import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Sebas
 */
public class MPTableModel extends AbstractTableModel {

    private String[] columnNames = {"Cliente", "Codigo", "Subpartida", "Descripcion", "<HTML>Tipo <br> Unidad<HTML>", "<HTML>Saldo <br> Insumo<HTML>", "<HTML>Porcentaje <br> Desperdicio<HTML>", "<HTML>Porcentaje <br> Merma<HTML>", "<HTML>Coeficiente  <br> de Consumo<HTML>", "<HTML>No calcula <br> Desperdicio<HTML>", "<HTML>Aplica <br> Formula<HTML>", "Modificar", "Eliminar"};
    private Object[][] data;

    public MPTableModel(Object[][] data) {
        this.data = data;

    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

  
}
