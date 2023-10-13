/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.database.model;

/**
 *
 * @author Sebas
 */
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clie_id")
    private int id;
    @Column(name = "clie_nombre")
    private String nombre;
    @Column(name = "clie_ruc")
    private String ruc;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Material> materiales;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<MaterialReporte> materialesReporte;

    public Cliente() {
        // Constructor vacío requerido por Hibernate
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Material> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
    }

    public void addMaterial(Material material) {
        material.setCliente(this);
        this.materiales.add(material);
    }

    public Cliente(String ruc, String nombre) {
        this.ruc = ruc;
        this.nombre = nombre;
    }

    public Cliente(String ruc, List<Material> materiales) {
        this.ruc = ruc;
        this.materiales = materiales;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    @Override
    public String toString() {
        return nombre + "[" + ruc + ']';
    }

    public List<MaterialReporte> getMaterialesReporte() {
        return materialesReporte;
    }

    public void setMaterialesReporte(List<MaterialReporte> materialesReporte) {
        this.materialesReporte = materialesReporte;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Cliente cliente = (Cliente) obj;

        return ruc != null ? ruc.equals(cliente.ruc) : cliente.ruc == null;
    }

    @Override
    public int hashCode() {
        return ruc != null ? ruc.hashCode() : 0;
    }

}
