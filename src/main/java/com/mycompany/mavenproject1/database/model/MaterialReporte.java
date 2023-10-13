/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.database.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 *
 * @author Sebas
 */
@Entity
@Table(name = "material_reportes")
public class MaterialReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mare_id")
    private int id;
    @Column(name = "mare_codigo")
    private String codigo;
    @Column(name = "mare_codigo_insumo")
    private String codigoInsumo;
    @Column(name = "mare_descripcion")
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public MaterialReporte(String codigo, String codigoInsumo, String descripcion, Cliente cliente) {
        this.codigo = codigo;
        this.codigoInsumo = codigoInsumo;
        this.descripcion = descripcion;
        this.cliente = cliente;
    }

    public MaterialReporte() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoInsumo() {
        return codigoInsumo;
    }

    public void setCodigoInsumo(String codigoInsumo) {
        this.codigoInsumo = codigoInsumo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "MaterialReporte{" + "id=" + id + ", codigo=" + codigo + ", codigoInsumo=" + codigoInsumo + ", descripcion=" + descripcion + ", cliente=" + cliente + '}';
    }
    
    
    

}
