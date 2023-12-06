/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.tablas.model;

/**
 *
 * @author Administrator
 */
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ComboBoxEditor extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> comboBox;

    public ComboBoxEditor(List<String> opciones) {
        comboBox = new JComboBox<>(opciones.toArray(new String[0]));

        // Agregar un ActionListener opcional para manejar eventos de selección
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped(); // Informar que la edición ha terminado
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        comboBox.setSelectedItem(value);
        return comboBox;
    }
}
