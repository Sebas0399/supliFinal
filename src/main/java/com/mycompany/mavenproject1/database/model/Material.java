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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import org.hibernate.annotations.Type;

/**
 *
 * @author Sebas
 */
@Entity
@Table(name = "materiales")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mate_id")
    private int id;
    @Column(name = "mate_codigo")
    private String codigo;

    @Column(name = "mate_subpartida")
    private String subpartida;

    @Column(name = "mate_descripcion")
    private String descripcion;
    @Column(name = "mate_tipo_unidad")
    private String tipoUnidad;
    @Column(name = "mate_saldo_insumo", precision = 20, scale = 7)
    //@Type(value = BigDecimal.class)
    private BigDecimal saldoInsumo;

    @Column(name = "mate_porcentaje_merma", precision = 20, scale = 7)
    private BigDecimal porcentajeMerma;

    @Column(name = "mate_coeficiente_consumo", precision = 20, scale = 7)
    private BigDecimal coeficienteConsumo;

    @Column(name = "mate_porcentaje_desperdicio", precision = 20, scale = 7)
    private BigDecimal porcentajeDesperdicio;

    @Column(name = "mate_calculaDesperdicio")
    private Boolean calculaDesperdicio;

    @Column(name = "mate_aplicaFormula")
    private Boolean aplicaFormula;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Material() {
        // Constructor vacío requerido por Hibernate
    }

    public Material(String codigo, String subpartida, String descripcion, String tipoUnidad, BigDecimal saldoInsumo, BigDecimal porcentajeMerma, BigDecimal coeficienteConsumo, BigDecimal porcentajeDesperdicio, Boolean calculaDesperdicio, Boolean aplicaFormula, Cliente cliente) {
        this.codigo = codigo;
        this.subpartida = subpartida;
        this.descripcion = descripcion;
        this.tipoUnidad = tipoUnidad;
        this.saldoInsumo = saldoInsumo;
        this.porcentajeMerma = porcentajeMerma;
        this.coeficienteConsumo = coeficienteConsumo;
        this.porcentajeDesperdicio = porcentajeDesperdicio;
        this.calculaDesperdicio = calculaDesperdicio;
        this.aplicaFormula = aplicaFormula;
        this.cliente = cliente;
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

    public String getSubpartida() {
        return subpartida;
    }

    public void setSubpartida(String subpartida) {
        this.subpartida = subpartida;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public BigDecimal getSaldoInsumo() {
        return saldoInsumo;
    }

    public void setSaldoInsumo(BigDecimal saldoInsumo) {
        this.saldoInsumo = saldoInsumo;
    }

    public BigDecimal getPorcentajeMerma() {
        return porcentajeMerma;
    }

    public void setPorcentajeMerma(BigDecimal porcentajeMerma) {
        this.porcentajeMerma = porcentajeMerma;
    }

    public BigDecimal getCoeficienteConsumo() {
        return coeficienteConsumo;
    }

    public void setCoeficienteConsumo(BigDecimal coeficienteConsumo) {
        this.coeficienteConsumo = coeficienteConsumo;
    }

    public BigDecimal getPorcentajeDesperdicio() {
        return porcentajeDesperdicio;
    }

    public void setPorcentajeDesperdicio(BigDecimal porcentajeDesperdicio) {
        this.porcentajeDesperdicio = porcentajeDesperdicio;
    }

    public Boolean getCalculaDesperdicio() {
        return calculaDesperdicio;
    }

    public void setCalculaDesperdicio(Boolean calculaDesperdicio) {
        this.calculaDesperdicio = calculaDesperdicio;
    }

    public Boolean getAplicaFormula() {
        return aplicaFormula;
    }

    public void setAplicaFormula(Boolean aplicaFormula) {
        this.aplicaFormula = aplicaFormula;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return codigo + ": " + descripcion + " (" + cliente.getNombre() + " [" + cliente.getRuc() + "])";
    }

}
