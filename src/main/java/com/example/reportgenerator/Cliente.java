package com.example.reportgenerator;

/**
 * Clase que representa un cliente en el sistema de gestión de clientes.
 * Esta clase almacena la información básica de un cliente incluyendo
 * su identificador, nombre, correo electrónico y ciudad de residencia.
 * 
 * @author Álvaro
 * @version 1.0
 * @since 2026-01-26
 */
public class Cliente {

    private int id;
    private String nombre;
    private String email;
    private String ciudad;

    /**
     * Constructor que crea un nuevo cliente con todos sus atributos.
     * 
     * @param id Identificador único del cliente
     * @param nombre Nombre completo del cliente
     * @param email Dirección de correo electrónico del cliente
     * @param ciudad Ciudad donde reside el cliente
     */
    public Cliente(int id, String nombre, String email, String ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.ciudad = ciudad;
    }

    /**
     * Obtiene el identificador único del cliente.
     * 
     * @return El ID del cliente
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre completo del cliente.
     * 
     * @return El nombre del cliente
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la dirección de correo electrónico del cliente.
     * 
     * @return El email del cliente
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtiene la ciudad donde reside el cliente.
     * 
     * @return La ciudad del cliente
     */
    public String getCiudad() {
        return ciudad;
    }
}
