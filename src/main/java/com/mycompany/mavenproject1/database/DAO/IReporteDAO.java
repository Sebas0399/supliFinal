/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.database.DAO;

import com.mycompany.mavenproject1.database.model.Reporte;

/**
 *
 * @author Sebas
 */
public interface IReporteDAO {
    public void create(Reporte reporte);
    public void deleteAll();
    public Reporte read();
    
}

