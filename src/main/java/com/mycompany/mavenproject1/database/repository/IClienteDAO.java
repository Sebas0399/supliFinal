/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.database.repository;

import com.mycompany.mavenproject1.database.model.Cliente;
import java.util.List;

/**
 *
 * @author Sebas
 */
public interface IClienteDAO {
    public void create(Cliente cliente);
    public List<Cliente>readAll();
    public Cliente readByRuc(String ruc);
}
