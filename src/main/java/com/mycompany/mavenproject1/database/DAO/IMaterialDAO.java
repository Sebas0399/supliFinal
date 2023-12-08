/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.database.DAO;

import com.mycompany.mavenproject1.database.model.Material;
import java.util.List;

/**
 *
 * @author Sebas
 */
public interface IMaterialDAO {
    public Boolean create(Material material);
    public List<Material> readAll();
    public Boolean delete(String subpartida,String ruc);
    public Material readByCodigo(String codigo,String ruc);
    public Boolean update(Material material);
    public Boolean deleteAll();
    public Material readLike(String codigo,String ruc);
    public List<Material> readByRuc(String ruc);
}
