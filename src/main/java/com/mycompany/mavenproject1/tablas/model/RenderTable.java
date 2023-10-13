/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.tablas.model;

import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Sebas
 */
public class RenderTable extends DefaultTableCellRenderer {

    private DecimalFormat decimalFormat;

    public RenderTable() {
        decimalFormat = new DecimalFormat("#.####");
        setHorizontalAlignment(SwingConstants.RIGHT);

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof JButton boton) {
            return boton;
        }
        else if(value instanceof JCheckBox box){
            return box;
        }

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    protected void setValue(Object value) {
        setText((value == null) ? "" : value.toString());

    }

}
