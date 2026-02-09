package com.example.reportgenerator;

import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para el sistema completo de generación de informes.
 * Estas pruebas verifican el flujo completo desde la carga de datos CSV
 * hasta la generación del archivo PDF final.
 */
@DisplayName("Pruebas de Integración - Sistema Completo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegracionSistemaTest {

    private static final String TEST_CSV = "test_integration_clientes.csv";
    private static final String TEST_PDF = "test_integration_report.pdf";
    private ClienteDAO dao;

    @BeforeEach
    void setUp() throws IOException {
        crearCSVDePruebaCompleto();
        dao = new ClienteDAO(TEST_CSV);
    }

    @AfterEach
    void tearDown() {
        limpiarArchivos();
    }

    /**
     * Crea un archivo CSV completo con datos variados para pruebas de integración
     */
    private void crearCSVDePruebaCompleto() throws IOException {
        try (FileWriter writer = new FileWriter(TEST_CSV)) {
            writer.write("id,nombre,email,ciudad\n");
            writer.write("1,Ana López Silva,ana.lopez@empresa.com,Madrid\n");
            writer.write("2,Carlos Ruiz Martínez,carlos.ruiz@empresa.com,Barcelona\n");
            writer.write("3,María García Pérez,maria.garcia@empresa.com,Madrid\n");
            writer.write("4,Luis Pérez González,luis.perez@empresa.com,Valencia\n");
            writer.write("5,Laura Martínez Rodríguez,laura.martinez@empresa.com,Madrid\n");
            writer.write("6,Pedro Sánchez López,pedro.sanchez@empresa.com,Barcelona\n");
            writer.write("7,Elena Fernández Gil,elena.fernandez@empresa.com,Sevilla\n");
            writer.write("8,Jorge Díaz Moreno,jorge.diaz@empresa.com,Valencia\n");
            writer.write("9,Carmen Muñoz Ruiz,carmen.munoz@empresa.com,Madrid\n");
            writer.write("10,David Romero Torres,david.romero@empresa.com,Barcelona\n");
            writer.write("11,Isabel Navarro Jiménez,isabel.navarro@empresa.com,Sevilla\n");
            writer.write("12,Francisco Iglesias Castro,francisco.iglesias@empresa.com,Valencia\n");
        }
    }

    private void limpiarArchivos() {
        new File(TEST_CSV).delete();
        new File(TEST_PDF).delete();
    }

    @Test
    @Order(1)
    @DisplayName("INTEGRACIÓN: Cargar CSV y generar PDF completo")
    void testIntegracionCompletaCSVaPDF() throws IOException {
        // Arrange
        assertCSVExiste();
        
        // Act - Paso 1: Cargar todos los clientes
        List<Cliente> clientes = dao.obtenerTodos();
        
        // Assert - Verificar carga exitosa
        assertNotNull(clientes, "La lista de clientes no debe ser null");
        assertEquals(12, clientes.size(), "Debe cargar 12 clientes del CSV");
        
        // Act - Paso 2: Generar PDF con todos los clientes
        PDFExporter.exportarAPDF(clientes, TEST_PDF);
        
        // Assert - Verificar que se creó el PDF
        File pdfGenerado = new File(TEST_PDF);
        assertTrue(pdfGenerado.exists(), "El archivo PDF debe haberse creado");
        assertTrue(pdfGenerado.length() > 0, "El PDF debe tener contenido");
        assertTrue(pdfGenerado.length() > 5000, "El PDF debe tener al menos 5KB de tamaño");
        
        // Verificar que es un archivo PDF válido (comienza con %PDF)
        byte[] primerosBytesBytes = Files.readAllBytes(Path.of(TEST_PDF));
        String primeroBytes = new String(primerosBytesBytes, 0, Math.min(4, primerosBytesBytes.length));
        assertEquals("%PDF", primeroBytes, "El archivo debe ser un PDF válido");
    }

    @Test
    @Order(2)
    @DisplayName("INTEGRACIÓN: Filtrar por ciudad y generar PDF filtrado")
    void testIntegracionFiltrarYGenerarPDF() throws IOException {
        // Act - Paso 1: Filtrar clientes por ciudad
        List<Cliente> clientesMadrid = dao.obtenerPorCiudad("Madrid");
        
        // Assert - Verificar filtrado
        assertEquals(4, clientesMadrid.size(), "Debe haber 4 clientes en Madrid");
        clientesMadrid.forEach(c -> 
            assertEquals("Madrid", c.getCiudad(), "Todos deben ser de Madrid")
        );
        
        // Act - Paso 2: Generar PDF solo con clientes de Madrid
        String pdfMadrid = "test_madrid.pdf";
        PDFExporter.exportarAPDF(clientesMadrid, pdfMadrid);
        
        // Assert - Verificar PDF generado
        File pdfFile = new File(pdfMadrid);
        assertTrue(pdfFile.exists(), "El PDF filtrado debe existir");
        assertTrue(pdfFile.length() > 0, "El PDF filtrado debe tener contenido");
        
        // Cleanup
        pdfFile.delete();
    }

    @Test
    @Order(3)
    @DisplayName("INTEGRACIÓN: Buscar por nombre y exportar resultados")
    void testIntegracionBuscarYExportar() throws IOException {
        // Act - Paso 1: Buscar clientes
        List<Cliente> clientesConMaria = dao.obtenerPorNombreContiene("María");
        
        // Assert - Verificar búsqueda
        assertFalse(clientesConMaria.isEmpty(), "Debe encontrar clientes con 'María'");
        assertTrue(clientesConMaria.stream()
                .anyMatch(c -> c.getNombre().contains("María")),
                "Al menos un cliente debe contener 'María' en el nombre");
        
        // Act - Paso 2: Exportar resultados de búsqueda
        String pdfBusqueda = "test_busqueda.pdf";
        PDFExporter.exportarAPDF(clientesConMaria, pdfBusqueda);
        
        // Assert
        File pdfFile = new File(pdfBusqueda);
        assertTrue(pdfFile.exists(), "El PDF de búsqueda debe existir");
        
        // Cleanup
        pdfFile.delete();
    }

    @Test
    @Order(4)
    @DisplayName("INTEGRACIÓN: Contar por ciudad y generar múltiples PDFs")
    void testIntegracionContarYGenerarMultiplesPDFs() throws IOException {
        // Act - Paso 1: Obtener conteo por ciudad
        Map<String, Integer> conteo = dao.contarClientesPorCiudad();
        
        // Assert - Verificar conteo
        assertEquals(4, conteo.size(), "Debe haber 4 ciudades diferentes");
        assertTrue(conteo.containsKey("Madrid"));
        assertTrue(conteo.containsKey("Barcelona"));
        assertTrue(conteo.containsKey("Valencia"));
        assertTrue(conteo.containsKey("Sevilla"));
        
        // Act - Paso 2: Generar un PDF por cada ciudad
        for (String ciudad : conteo.keySet()) {
            List<Cliente> clientesCiudad = dao.obtenerPorCiudad(ciudad);
            String pdfCiudad = "test_" + ciudad.toLowerCase() + ".pdf";
            PDFExporter.exportarAPDF(clientesCiudad, pdfCiudad);
            
            // Assert
            File pdfFile = new File(pdfCiudad);
            assertTrue(pdfFile.exists(), "El PDF de " + ciudad + " debe existir");
            
            // Cleanup
            pdfFile.delete();
        }
    }

    @Test
    @Order(5)
    @DisplayName("INTEGRACIÓN: Flujo completo con validaciones")
    void testIntegracionFlujoCompletoConValidaciones() throws IOException {
        // Paso 1: Verificar que el CSV existe y es válido
        File csvFile = new File(TEST_CSV);
        assertTrue(csvFile.exists(), "El CSV debe existir");
        assertTrue(csvFile.length() > 0, "El CSV debe tener contenido");
        
        // Paso 2: Cargar datos
        List<Cliente> todosClientes = dao.obtenerTodos();
        assertNotNull(todosClientes);
        assertFalse(todosClientes.isEmpty(), "Debe haber clientes cargados");
        
        // Paso 3: Validar integridad de datos
        for (Cliente cliente : todosClientes) {
            assertNotNull(cliente.getNombre(), "El nombre no debe ser null");
            assertNotNull(cliente.getEmail(), "El email no debe ser null");
            assertNotNull(cliente.getCiudad(), "La ciudad no debe ser null");
            assertTrue(cliente.getId() > 0, "El ID debe ser positivo");
            assertTrue(cliente.getEmail().contains("@"), "El email debe contener @");
        }
        
        // Paso 4: Generar PDF
        PDFExporter.exportarAPDF(todosClientes, TEST_PDF);
        
        // Paso 5: Validar PDF generado
        File pdfFile = new File(TEST_PDF);
        assertTrue(pdfFile.exists(), "El PDF debe existir");
        assertTrue(pdfFile.canRead(), "El PDF debe ser legible");
        assertTrue(pdfFile.isFile(), "Debe ser un archivo regular");
        
        // Verificar tamaño mínimo razonable (estructura + contenido)
        long tamaño = pdfFile.length();
        assertTrue(tamaño > 5000, "El PDF debe tener al menos 5KB");
        assertTrue(tamaño < 1000000, "El PDF no debe ser excesivamente grande");
    }

    @Test
    @Order(6)
    @DisplayName("INTEGRACIÓN: Operaciones múltiples secuenciales")
    void testIntegracionOperacionesSecuenciales() throws IOException {
        // Operación 1: Cargar todos
        List<Cliente> todos = dao.obtenerTodos();
        int totalClientes = todos.size();
        
        // Operación 2: Filtrar por diferentes ciudades
        List<Cliente> madrid = dao.obtenerPorCiudad("Madrid");
        List<Cliente> barcelona = dao.obtenerPorCiudad("Barcelona");
        List<Cliente> valencia = dao.obtenerPorCiudad("Valencia");
        
        // Verificar que la suma de filtros coincide con el total
        int sumaFiltros = madrid.size() + barcelona.size() + valencia.size() 
                        + dao.obtenerPorCiudad("Sevilla").size();
        assertEquals(totalClientes, sumaFiltros, 
            "La suma de clientes filtrados debe coincidir con el total");
        
        // Operación 3: Generar PDFs para cada conjunto
        PDFExporter.exportarAPDF(todos, "test_todos.pdf");
        PDFExporter.exportarAPDF(madrid, "test_madrid.pdf");
        PDFExporter.exportarAPDF(barcelona, "test_barcelona.pdf");
        
        // Verificar que todos los PDFs se crearon
        assertTrue(new File("test_todos.pdf").exists());
        assertTrue(new File("test_madrid.pdf").exists());
        assertTrue(new File("test_barcelona.pdf").exists());
        
        // Cleanup
        new File("test_todos.pdf").delete();
        new File("test_madrid.pdf").delete();
        new File("test_barcelona.pdf").delete();
    }

    @Test
    @Order(7)
    @DisplayName("INTEGRACIÓN: Manejo de casos especiales")
    void testIntegracionCasosEspeciales() throws IOException {
        // Caso 1: Lista vacía (ciudad inexistente)
        List<Cliente> inexistentes = dao.obtenerPorCiudad("CiudadInexistente");
        assertTrue(inexistentes.isEmpty());
        
        // Generar PDF con lista vacía
        String pdfVacio = "test_vacio.pdf";
        PDFExporter.exportarAPDF(inexistentes, pdfVacio);
        assertTrue(new File(pdfVacio).exists(), "El PDF vacío debe crearse");
        new File(pdfVacio).delete();
        
        // Caso 2: Un solo cliente
        List<Cliente> unCliente = dao.obtenerPorNombreContiene("Ana López Silva");
        assertEquals(1, unCliente.size());
        
        String pdfUno = "test_uno.pdf";
        PDFExporter.exportarAPDF(unCliente, pdfUno);
        assertTrue(new File(pdfUno).exists(), "El PDF con un cliente debe crearse");
        new File(pdfUno).delete();
    }

    /**
     * Método auxiliar para verificar que el CSV de prueba existe
     */
    private void assertCSVExiste() {
        File csvFile = new File(TEST_CSV);
        assertTrue(csvFile.exists(), "El CSV de prueba debe existir");
        assertTrue(csvFile.canRead(), "El CSV debe ser legible");
    }
}
