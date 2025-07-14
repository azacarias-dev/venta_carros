package org.ventacarros.model;

/**
 *
 * @author Zacarias
 */
public class Cliente {

    private int id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String nit;
    private String estado;
    private String contraseña;

    public Cliente(String nombre, String apellido, String telefono, String correo, String nit, String contraseña) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.nit = nit;
        this.contraseña = contraseña;
    }
    
    public Cliente(int id, String nombre, String apellido, String telefono, String correo, String nit, String estado, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.nit = nit;
        this.estado = estado;
        this.contraseña = contraseña;
    }
    
    public Cliente(int id, String nombre, String apellido, String telefono, String correo, String nit, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.nit = nit;
        this.contraseña = contraseña;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

}
