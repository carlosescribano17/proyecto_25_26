/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.Timestamp;
/**
 *
 * @author DAM2Alu7
 */
public class Usuario {
    //variables
    private int id;
    private String usuario;
    private String contrasena;
    private String rol;
    private String nombre;
    private String apellidos;
    private Timestamp fecha_alta;
    //constructor vacio
    public Usuario() {}
    //constructor lleno
    public Usuario(int id, String usuario, String contrasena, String rol, String nombre, String apellidos, Timestamp fecha_alta) {
        this.id = id;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.nombre=nombre;
        this.apellidos=apellidos;
        this.fecha_alta=fecha_alta;
    }
    //getters y setters
    public int getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getContrasena() { return contrasena; }
    public String getRol() { return rol; }

    public void setId(int id) { this.id = id; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setRol(String rol) { this.rol = rol; }

    public String getNombre() {
        return nombre;
    }

    public Timestamp getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(Timestamp fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
}
