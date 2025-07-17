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
    private int cantidad;
    private Double subtotal;

    public Compras(int id, int idCliente, LocalDate fechaCompra, int idProducto, int cantidad, Double subtotal) {
        this.id = id;
        this.idCliente = idCliente;
        this.fechaCompra = fechaCompra;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    
    public Compras(int id, int idCliente, int idProducto, int cantidad) {
        this.id = id;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
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
