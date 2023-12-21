/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.tablas.model;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author anton
 */
public class FacturaTableModel extends AbstractTableModel {

    private String[] columnNames = {"Id", "Cliente", "No.", "Fecha", "Cantidad", "Descripción insumo", "Producto terminado", "Unidad de medida", "Largo", "Ancho", "Modificar", "Eliminar"};
    private Object[][] data;

    public FacturaTableModel(Object[][] data) {
        this.data = data;

    }

    public FacturaTableModel() {
        this.data = new Object[0][columnNames.length];
    }

    @Override
    public int getRowCount() {
        if (data != null) {
            return data.length;

        } else {
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
        System.arraycopy(rowData, 0, data[row], 0, getColumnCount());
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

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1 || columnIndex == 2 || columnIndex == 3 || columnIndex == 4;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    
}
