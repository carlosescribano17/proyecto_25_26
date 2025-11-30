/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;
/**
 *
 * @author DAM2Alu10
 */
public class Producto {
    //variables
    private int id_producto;
    private String nombre;
    private String marca;
    private Double precio;
    private int stock;
    private String tipo_producto;
    private String descripcion;
    private String imagen_url;
    private int activo;
    private Timestamp fecha_alta;
    //constructor vacio
    public Producto(){
    }
    //constructor entero
    public Producto(int id_producto, String nombre, String marca, Double precio, int stock, String tipo_producto, String descripcion, String imagen_url, int activo, Timestamp fecha_alta) {
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.stock = stock;
        this.tipo_producto = tipo_producto;
        this.descripcion = descripcion;
        this.imagen_url = imagen_url;
        this.activo = activo;
        this.fecha_alta = fecha_alta;
    }
    // getters y setters
    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getTipo_producto() {
        return tipo_producto;
    }

    public void setTipo_producto(String tipo_producto) {
        this.tipo_producto = tipo_producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public Timestamp getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(Timestamp fecha_alta) {
        this.fecha_alta = fecha_alta;
    }
    //to string para hacer pruebas
    @Override
    public String toString() {
        return "Producto{" + "id_producto=" + id_producto + ", nombre=" + nombre + ", marca=" + marca + ", precio=" + precio + ", stock=" + stock + ", tipo_producto=" + tipo_producto + ", descripcion=" + descripcion + ", imagen_url=" + imagen_url + ", activo=" + activo + ", fecha_alta=" + fecha_alta + '}';
    }
}
