/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.database.repository;

import com.mycompany.mavenproject1.database.model.Material;
import com.mycompany.mavenproject1.database.model.MaterialReporte;
import java.util.List;

/**
 *
 * @author Sebas
 */
public interface IMaterialReporteDAO {

    public Boolean create(MaterialReporte material);

    public Boolean delete(String codigo);

    public List<MaterialReporte> readAll();

    public MaterialReporte readByCodigo(String codigo);

    public Boolean update(MaterialReporte materialReporte, String codigo);

    public Boolean deleteAll();

}
