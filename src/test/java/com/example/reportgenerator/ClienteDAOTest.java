package com.example.reportgenerator;

import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase ClienteDAO.
 * Verifica el correcto funcionamiento de la lectura y filtrado de datos desde CSV.
 */
@DisplayName("Pruebas unitarias de ClienteDAO")
class ClienteDAOTest {

    private static final String TEST_CSV_PATH = "test_clientes.csv";
    private ClienteDAO dao;

    @BeforeEach
    void setUp() throws IOException {
        // Crear CSV de prueba con datos conocidos
        crearCSVDePrueba();
        dao = new ClienteDAO(TEST_CSV_PATH);
    }

    @AfterEach
    void tearDown() {
        // Limpiar archivo de prueba
        File testFile = new File(TEST_CSV_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    /**
     * Crea un archivo CSV de prueba con datos conocidos
     */
    private void crearCSVDePrueba() throws IOException {
        try (FileWriter writer = new FileWriter(TEST_CSV_PATH)) {
            writer.write("id,nombre,email,ciudad\n");
            writer.write("1,Ana López,ana@email.com,Madrid\n");
            writer.write("2,Carlos Ruiz,carlos@email.com,Barcelona\n");
            writer.write("3,María García,maria@email.com,Madrid\n");
            writer.write("4,Luis Pérez,luis@email.com,Valencia\n");
            writer.write("5,Laura Martínez,laura@email.com,Madrid\n");
            writer.write("6,Pedro Sánchez,pedro@email.com,Barcelona\n");
            writer.write("7,Elena Fernández,elena@email.com,Sevilla\n");
        }
    }

    @Test
    @DisplayName("obtenerTodos() devuelve todos los clientes del CSV")
    void testObtenerTodos() {
        // Act
        List<Cliente> clientes = dao.obtenerTodos();
        
        // Assert
        assertNotNull(clientes, "La lista no debe ser null");
        assertEquals(7, clientes.size(), "Debe haber 7 clientes en el CSV");
        
        // Verificar primer cliente
        Cliente primero = clientes.get(0);
        assertEquals(1, primero.getId());
        assertEquals("Ana López", primero.getNombre());
        assertEquals("ana@email.com", primero.getEmail());
        assertEquals("Madrid", primero.getCiudad());
    }

    @Test
    @DisplayName("obtenerPorCiudad() filtra correctamente por ciudad")
    void testObtenerPorCiudad() {
        // Act
        List<Cliente> clientesMadrid = dao.obtenerPorCiudad("Madrid");
        List<Cliente> clientesBarcelona = dao.obtenerPorCiudad("Barcelona");
        List<Cliente> clientesValencia = dao.obtenerPorCiudad("Valencia");
        
        // Assert
        assertEquals(3, clientesMadrid.size(), "Debe haber 3 clientes en Madrid");
        assertEquals(2, clientesBarcelona.size(), "Debe haber 2 clientes en Barcelona");
        assertEquals(1, clientesValencia.size(), "Debe haber 1 cliente en Valencia");
        
        // Verificar que todos los clientes filtrados son de la ciudad correcta
        assertTrue(clientesMadrid.stream().allMatch(c -> c.getCiudad().equals("Madrid")));
        assertTrue(clientesBarcelona.stream().allMatch(c -> c.getCiudad().equals("Barcelona")));
    }

    @Test
    @DisplayName("obtenerPorCiudad() es case-insensitive")
    void testObtenerPorCiudadCaseInsensitive() {
        // Act
        List<Cliente> clientesMadrid1 = dao.obtenerPorCiudad("madrid");
        List<Cliente> clientesMadrid2 = dao.obtenerPorCiudad("MADRID");
        List<Cliente> clientesMadrid3 = dao.obtenerPorCiudad("MaDrId");
        
        // Assert
        assertEquals(3, clientesMadrid1.size());
        assertEquals(3, clientesMadrid2.size());
        assertEquals(3, clientesMadrid3.size());
    }

    @Test
    @DisplayName("obtenerPorCiudad() con ciudad inexistente devuelve lista vacía")
    void testObtenerPorCiudadInexistente() {
        // Act
        List<Cliente> clientes = dao.obtenerPorCiudad("Zaragoza");
        
        // Assert
        assertNotNull(clientes, "La lista no debe ser null");
        assertTrue(clientes.isEmpty(), "La lista debe estar vacía");
    }

    @Test
    @DisplayName("obtenerPorNombreContiene() encuentra clientes que contienen el texto")
    void testBuscarPorNombre() {
        // Act
        List<Cliente> conAna = dao.obtenerPorNombreContiene("Ana");
        List<Cliente> conMaria = dao.obtenerPorNombreContiene("María");
        List<Cliente> conez = dao.obtenerPorNombreContiene("ez"); // Sufijo común
        
        // Assert
        assertEquals(1, conAna.size(), "Debe encontrar 1 cliente con 'Ana'");
        assertEquals(1, conMaria.size(), "Debe encontrar 1 cliente con 'María'");
        assertTrue(conez.size() >= 3, "Debe encontrar al menos 3 clientes con 'ez'");
    }

    @Test
    @DisplayName("obtenerPorNombreContiene() es case-insensitive")
    void testBuscarPorNombreCaseInsensitive() {
        // Act
        List<Cliente> resultado1 = dao.obtenerPorNombreContiene("ana");
        List<Cliente> resultado2 = dao.obtenerPorNombreContiene("ANA");
        List<Cliente> resultado3 = dao.obtenerPorNombreContiene("AnA");
        
        // Assert
        assertEquals(1, resultado1.size());
        assertEquals(1, resultado2.size());
        assertEquals(1, resultado3.size());
    }

    @Test
    @DisplayName("obtenerPorNombreContiene() con texto vacío devuelve todos los clientes")
    void testBuscarPorNombreTextoVacio() {
        // Act
        List<Cliente> resultado = dao.obtenerPorNombreContiene("");
        
        // Assert
        assertEquals(7, resultado.size(), "Debe devolver todos los clientes");
    }

    @Test
    @DisplayName("obtenerPorNombreContiene() con texto inexistente devuelve lista vacía")
    void testBuscarPorNombreInexistente() {
        // Act
        List<Cliente> resultado = dao.obtenerPorNombreContiene("XXXXXXXXX");
        
        // Assert
        assertTrue(resultado.isEmpty(), "La lista debe estar vacía");
    }

    @Test
    @DisplayName("contarClientesPorCiudad() agrupa correctamente los clientes")
    void testContarPorCiudad() {
        // Act
        Map<String, Integer> conteo = dao.contarClientesPorCiudad();
        
        // Assert
        assertNotNull(conteo, "El mapa no debe ser null");
        assertEquals(4, conteo.size(), "Debe haber 4 ciudades diferentes");
        assertEquals(3, conteo.get("Madrid"), "Madrid debe tener 3 clientes");
        assertEquals(2, conteo.get("Barcelona"), "Barcelona debe tener 2 clientes");
        assertEquals(1, conteo.get("Valencia"), "Valencia debe tener 1 cliente");
        assertEquals(1, conteo.get("Sevilla"), "Sevilla debe tener 1 cliente");
    }

    @Test
    @DisplayName("contarClientesPorCiudad() con CSV vacío devuelve mapa vacío")
    void testContarPorCiudadCSVVacio() throws IOException {
        // Arrange - Crear CSV vacío
        try (FileWriter writer = new FileWriter(TEST_CSV_PATH)) {
            writer.write("id,nombre,email,ciudad\n");
        }
        ClienteDAO daoVacio = new ClienteDAO(TEST_CSV_PATH);
        
        // Act
        Map<String, Integer> conteo = daoVacio.contarClientesPorCiudad();
        
        // Assert
        assertNotNull(conteo);
        assertTrue(conteo.isEmpty(), "El mapa debe estar vacío");
    }

    @Test
    @DisplayName("ClienteDAO funciona con CSV que tiene un solo cliente")
    void testCSVConUnSoloCliente() throws IOException {
        // Arrange
        try (FileWriter writer = new FileWriter(TEST_CSV_PATH)) {
            writer.write("id,nombre,email,ciudad\n");
            writer.write("1,Solo Cliente,solo@test.com,TestCity\n");
        }
        ClienteDAO daoUnico = new ClienteDAO(TEST_CSV_PATH);
        
        // Act
        List<Cliente> todos = daoUnico.obtenerTodos();
        
        // Assert
        assertEquals(1, todos.size());
        assertEquals("Solo Cliente", todos.get(0).getNombre());
    }

    @Test
    @DisplayName("Múltiples operaciones consecutivas devuelven resultados consistentes")
    void testOperacionesConsecutivasConsistentes() {
        // Act - Ejecutar la misma operación múltiples veces
        List<Cliente> resultado1 = dao.obtenerTodos();
        List<Cliente> resultado2 = dao.obtenerTodos();
        List<Cliente> resultado3 = dao.obtenerTodos();
        
        // Assert - Los resultados deben ser iguales
        assertEquals(resultado1.size(), resultado2.size());
        assertEquals(resultado1.size(), resultado3.size());
        assertEquals(7, resultado1.size());
    }

    @Test
    @DisplayName("Filtros combinados funcionan correctamente")
    void testFiltrosCombinados() {
        // Act - Obtener clientes de Madrid y luego buscar por nombre
        List<Cliente> clientesMadrid = dao.obtenerPorCiudad("Madrid");
        long clientesMadridConAna = clientesMadrid.stream()
                .filter(c -> c.getNombre().contains("Ana"))
                .count();
        
        // Assert
        assertEquals(1, clientesMadridConAna, "Debe haber 1 Ana en Madrid");
    }
}
