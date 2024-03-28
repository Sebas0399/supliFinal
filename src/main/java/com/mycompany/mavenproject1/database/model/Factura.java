/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fact_id")
    private Integer id;
    @Column(name = "fact_nro_factura")
    private String nroFactura;
    
    @Column(name = "fact_ruc_cliente")
    private String clienteFactura;
    
    @Column(name = "fact_cliente")
    private String rucCliente;
    
    @Column(name = "fact_cantidad")
    private Integer cantidad;
    
    @Column(name = "fact_insumo")
    private String insumo;
     @Column(name = "fact_descripcion")
    private String insumoDescripcion;
    @Column(name = "fact_unidad")
    private String unidadMedida;
    
    @Column(name = "fact_largo")
    private Double largo;
    @Column(name = "fact_ancho")
    private Double ancho;
    
    @Column(name = "fact_fecha")
    private LocalDate fecha;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public String getRucCliente() {
        return rucCliente;
    }

    public void setRucCliente(String rucCliente) {
        this.rucCliente = rucCliente;
    }

    public String getClienteFactura() {
        return clienteFactura;
    }

    public void setClienteFactura(String clienteFactura) {
        this.clienteFactura = clienteFactura;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getInsumo() {
        return insumo;
    }

    public void setInsumo(String insumo) {
        this.insumo = insumo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Double getLargo() {
        return largo;
    }

    public void setLargo(Double largo) {
        this.largo = largo;
    }

    public Double getAncho() {
        return ancho;
    }

    public void setAncho(Double ancho) {
        this.ancho = ancho;
    }

  

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getInsumoDescripcion() {
        return insumoDescripcion;
    }

    public void setInsumoDescripcion(String insumoDescripcion) {
        this.insumoDescripcion = insumoDescripcion;
    }

    @Override
    public String toString() {
        return "Factura{" + "id=" + id + ", nroFactura=" + nroFactura + ", clienteFactura=" + clienteFactura + ", rucCliente=" + rucCliente + ", cantidad=" + cantidad + ", insumo=" + insumo + ", insumoDescripcion=" + insumoDescripcion + ", unidadMedida=" + unidadMedida + ", largo=" + largo + ", ancho=" + ancho + ", fecha=" + fecha + '}';
    }

   
  

}
