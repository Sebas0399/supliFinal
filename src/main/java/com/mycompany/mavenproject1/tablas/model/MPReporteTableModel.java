/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.tablas.model;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Sebas
 */
public class MPReporteTableModel extends AbstractTableModel{

   private String[] columnNames = {"Material","Cód. Insumo","Descripción","Empresa","Modificar","Eliminar"};
    private Object[][] data ;
    public MPReporteTableModel(Object[][]data){
        this.data=data;
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

   @Override
    public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < getRowCount(); i++) {
        for (int j = 0; j < getColumnCount(); j++) {
            stringBuilder.append(getValueAt(i, j)).append("\t");
        }
        stringBuilder.append("\n");
    }
    return stringBuilder.toString();
}
    
}
