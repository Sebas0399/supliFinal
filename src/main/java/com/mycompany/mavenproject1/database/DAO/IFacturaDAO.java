/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.database.DAO;

import com.mycompany.mavenproject1.database.model.Factura;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface IFacturaDAO {
    public Boolean create(Factura factura);
    public void delete(String numero,String rucCliente);
    public Factura readByNumeroAndRuc(String numero,String rucCliente );
    public List<Factura> filterByFecha(LocalDate fechaInicio,LocalDate fechaFin);
}
