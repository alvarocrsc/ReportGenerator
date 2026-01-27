package com.example.reportgenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Clase DAO (Data Access Object) para gestionar el acceso a los datos de clientes.
 * Esta clase se encarga de leer y filtrar los datos de clientes desde un archivo CSV,
 * simulando las operaciones típicas de una base de datos SQL.
 * 
 * <p>Proporciona métodos para:
 * <ul>
 *   <li>Obtener todos los clientes</li>
 *   <li>Filtrar clientes por ciudad</li>
 *   <li>Buscar clientes por nombre</li>
 *   <li>Contar clientes y agrupar por ciudad</li>
 * </ul>
 * 
 * @author Álvaro
 * @version 1.0
 * @since 2026-01-26
 */
public class ClienteDAO {

    private String rutaCSV;

    /**
     * Constructor que inicializa el DAO con la ruta del archivo CSV.
     * 
     * @param rutaCSV Ruta absoluta al archivo CSV que contiene los datos de clientes
     */
    public ClienteDAO(String rutaCSV) {
        this.rutaCSV = rutaCSV;
    }

    /**
     * Obtiene todos los clientes del archivo CSV.
     * Simula la consulta SQL: SELECT * FROM clientes
     * 
     * @return Lista completa de todos los clientes registrados
     */
    public List<Cliente> obtenerTodos() {
        return leerCSV();
    }

    /**
     * Obtiene todos los clientes que residen en una ciudad específica.
     * Simula la consulta SQL: SELECT * FROM clientes WHERE ciudad = ?
     * 
     * <p>La búsqueda ignora mayúsculas/minúsculas para mayor flexibilidad.
     * 
     * @param ciudadFiltro Nombre de la ciudad por la cual filtrar los clientes
     * @return Lista de clientes que residen en la ciudad especificada
     */
    public List<Cliente> obtenerPorCiudad(String ciudadFiltro) {

        List<Cliente> resultado = new ArrayList<>();

        for (Cliente c : leerCSV()) {
            if (c.getCiudad().equalsIgnoreCase(ciudadFiltro)) {
                resultado.add(c);
            }
        }

        return resultado;
    }

    /**
     * Busca clientes cuyo nombre contenga el texto especificado.
     * Simula la consulta SQL: SELECT * FROM clientes WHERE nombre LIKE '%texto%'
     * 
     * <p>La búsqueda es case-insensitive (ignora mayúsculas/minúsculas).
     * 
     * @param texto Texto a buscar dentro del nombre del cliente
     * @return Lista de clientes cuyos nombres contienen el texto especificado
     */
    public List<Cliente> obtenerPorNombreContiene(String texto) {

        List<Cliente> resultado = new ArrayList<>();

        for (Cliente c : leerCSV()) {
            if (c.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(c);
            }
        }

        return resultado;
    }

    /**
     * Cuenta el número total de clientes registrados.
     * Simula la consulta SQL: SELECT COUNT(*) FROM clientes
     * 
     * @return Número total de clientes
     */
    public int contarClientes() {
        return leerCSV().size();
    }

    /**
     * Agrupa y cuenta los clientes por ciudad.
     * Simula la consulta SQL: SELECT ciudad, COUNT(*) FROM clientes GROUP BY ciudad
     * 
     * @return Mapa donde la clave es el nombre de la ciudad y el valor es el número de clientes
     */
    public Map<String, Integer> contarClientesPorCiudad() {

        Map<String, Integer> totales = new HashMap<>();

        for (Cliente c : leerCSV()) {
            String ciudad = c.getCiudad();
            totales.put(ciudad, totales.getOrDefault(ciudad, 0) + 1);
        }

        return totales;
    }

    /**
     * Método interno que lee y parsea el archivo CSV.
     * Lee todas las líneas del archivo CSV, omitiendo la cabecera,
     * y crea objetos Cliente con los datos.
     * 
     * <p>Formato esperado del CSV: id,nombre,email,ciudad
     * 
     * @return Lista de todos los clientes leídos del archivo CSV
     */
    private List<Cliente> leerCSV() {

        List<Cliente> clientes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaCSV))) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {

                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] campos = linea.split(",");

                int id = Integer.parseInt(campos[0]);
                String nombre = campos[1];
                String email = campos[2];
                String ciudad = campos[3];

                clientes.add(new Cliente(id, nombre, email, ciudad));
            }

        } catch (IOException e) {
            System.err.println("Error leyendo el CSV");
            e.printStackTrace();
        }

        return clientes;
    }
}
