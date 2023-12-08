/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.tablas.model;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author anton
 */
public class FacturaTableModel extends AbstractTableModel {

    private String[] columnNames = {"Id","Cliente","No.","Fecha", "Cantidad", "Descripción insumo", "Producto terminado", "Unidad de medida", "Largo", "Ancho", "Modificar", "Eliminar"};
    private Object[][] data;

    public FacturaTableModel(Object[][] data) {
        this.data = data;

    }

    public FacturaTableModel() {

    }

    @Override
    public int getRowCount() {
        if (data != null) {
            return data.length;

        }
        else{
            return 0;
        }
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
    public void setData(Object[][] data) {
        this.data = data;
        fireTableDataChanged();
    }

    public void updateRow(int row, Object[] rowData) {
        for (int col = 0; col < getColumnCount(); col++) {
            data[row][col] = rowData[col];
        }
        fireTableRowsUpdated(row, row);
    }

    public void addRow(Object[] rowData) {
        Object[][] newData = new Object[getRowCount() + 1][getColumnCount()];
        System.arraycopy(data, 0, newData, 0, getRowCount());
        newData[getRowCount()] = rowData;
        data = newData;
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    public void removeRow(int row) {
        Object[][] newData = new Object[getRowCount() - 1][getColumnCount()];
        System.arraycopy(data, 0, newData, 0, row);
        System.arraycopy(data, row + 1, newData, row, getRowCount() - row - 1);
        data = newData;
        fireTableRowsDeleted(row, row);
    }
   
}
