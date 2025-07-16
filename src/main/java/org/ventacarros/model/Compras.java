/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ventacarros.model;

import java.time.LocalDate;

/**
 *
 * @author Zacarias
 */
public class Compras {

    private int id;
    private int idCliente;
    private LocalDate fechaCompra;
    private int idProducto;
    private Double subtotal;

    public Compras(int id, int idCliente, LocalDate fechaCompra, int idProducto, Double subtotal) {
        this.id = id;
        this.idCliente = idCliente;
        this.fechaCompra = fechaCompra;
        this.idProducto = idProducto;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

}
