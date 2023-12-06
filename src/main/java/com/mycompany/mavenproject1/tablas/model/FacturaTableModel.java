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

    private String[] columnNames = {"No.", "Cantidad", "Descripción insumo", "Producto terminado", "Unidada de medida", "Largo", "Ancho", "Modificar", "Eliminar"};
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
  @Override
    public boolean isCellEditable(int row, int column) {
        return column == 3; // Hacer editable solo la columna del JComboBox
    }
}
