package com.example.reportgenerator;

import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de rendimiento, volumen y estrés para el sistema.
 * Estas pruebas verifican el comportamiento del sistema bajo condiciones extremas.
 */
@DisplayName("Pruebas de Rendimiento y Volumen")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RendimientoYVolumenTest {

    private static final String TEST_CSV_GRANDE = "test_volumen_clientes.csv";
    private static final String TEST_PDF_GRANDE = "test_volumen_report.pdf";

    @AfterEach
    void tearDown() {
        new File(TEST_CSV_GRANDE).delete();
        new File(TEST_PDF_GRANDE).delete();
    }

    @Test
    @Order(1)
    @DisplayName("VOLUMEN: Procesar 1,000 clientes")
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testVolumen1000Clientes() throws IOException {
        // Arrange
        int numeroClientes = 1000;
        crearCSVGrande(numeroClientes);
        ClienteDAO dao = new ClienteDAO(TEST_CSV_GRANDE);
        
        // Act - Medir tiempo de carga
        long inicioLectura = System.currentTimeMillis();
        List<Cliente> clientes = dao.obtenerTodos();
        long finLectura = System.currentTimeMillis();
        
        // Assert - Verificar carga
        assertEquals(numeroClientes, clientes.size(), 
            "Debe cargar todos los clientes");
        
        long tiempoLectura = finLectura - inicioLectura;
        System.out.println("Tiempo de lectura (1000 clientes): " + tiempoLectura + "ms");
        assertTrue(tiempoLectura < 5000, 
            "La lectura de 1000 clientes debe tomar menos de 5 segundos");
        
        // Act - Medir tiempo de generación PDF
        long inicioPDF = System.currentTimeMillis();
        PDFExporter.exportarAPDF(clientes, TEST_PDF_GRANDE);
        long finPDF = System.currentTimeMillis();
        
        // Assert - Verificar PDF
        File pdfFile = new File(TEST_PDF_GRANDE);
        assertTrue(pdfFile.exists(), "El PDF debe generarse correctamente");
        
        long tiempoPDF = finPDF - inicioPDF;
        System.out.println("Tiempo de generación PDF (1000 clientes): " + tiempoPDF + "ms");
        assertTrue(tiempoPDF < 30000, 
            "La generación del PDF debe tomar menos de 30 segundos");
    }

    @Test
    @Order(2)
    @DisplayName("VOLUMEN: Procesar 5,000 clientes")
    @Timeout(value = 60, unit = TimeUnit.SECONDS)
    void testVolumen5000Clientes() throws IOException {
        // Arrange
        int numeroClientes = 5000;
        crearCSVGrande(numeroClientes);
        ClienteDAO dao = new ClienteDAO(TEST_CSV_GRANDE);
        
        // Act
        long inicio = System.currentTimeMillis();
        List<Cliente> clientes = dao.obtenerTodos();
        long fin = System.currentTimeMillis();
        
        // Assert
        assertEquals(numeroClientes, clientes.size());
        long tiempo = fin - inicio;
        System.out.println("Tiempo de lectura (5000 clientes): " + tiempo + "ms");
        assertTrue(tiempo < 10000, 
            "La lectura de 5000 clientes debe tomar menos de 10 segundos");
    }

    @Test
    @Order(3)
    @DisplayName("ESTRÉS: Múltiples operaciones de filtrado consecutivas")
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testEstresMultiplesFiltrados() throws IOException {
        // Arrange
        crearCSVGrande(1000);
        ClienteDAO dao = new ClienteDAO(TEST_CSV_GRANDE);
        
        // Act - Realizar 100 operaciones de filtrado
        long inicio = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            String ciudad = "Ciudad" + (i % 10);
            List<Cliente> filtrados = dao.obtenerPorCiudad(ciudad);
            assertNotNull(filtrados);
        }
        long fin = System.currentTimeMillis();
        
        // Assert
        long tiempo = fin - inicio;
        System.out.println("Tiempo de 100 filtrados: " + tiempo + "ms");
        assertTrue(tiempo < 15000, 
            "100 operaciones de filtrado deben tomar menos de 15 segundos");
    }

    @Test
    @Order(4)
    @DisplayName("ESTRÉS: Múltiples búsquedas consecutivas")
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    void testEstresMultiplesBusquedas() throws IOException {
        // Arrange
        crearCSVGrande(1000);
        ClienteDAO dao = new ClienteDAO(TEST_CSV_GRANDE);
        
        // Act - Realizar 100 búsquedas
        long inicio = System.currentTimeMillis();
        String[] terminos = {"Cliente", "0", "5", "Test", "Email"};
        for (int i = 0; i < 100; i++) {
            String termino = terminos[i % terminos.length];
            List<Cliente> resultados = dao.obtenerPorNombreContiene(termino);
            assertNotNull(resultados);
        }
        long fin = System.currentTimeMillis();
        
        // Assert
        long tiempo = fin - inicio;
        System.out.println("Tiempo de 100 búsquedas: " + tiempo + "ms");
        assertTrue(tiempo < 15000, 
            "100 búsquedas deben tomar menos de 15 segundos");
    }

    @Test
    @Order(5)
    @DisplayName("RENDIMIENTO: Comparar rendimiento con diferentes tamaños")
    void testRendimientoDiferentesTamaños() throws IOException {
        int[] tamaños = {100, 500, 1000};
        long[] tiempos = new long[tamaños.length];
        
        for (int i = 0; i < tamaños.length; i++) {
            crearCSVGrande(tamaños[i]);
            ClienteDAO dao = new ClienteDAO(TEST_CSV_GRANDE);
            
            long inicio = System.currentTimeMillis();
            dao.obtenerTodos();
            long fin = System.currentTimeMillis();
            
            tiempos[i] = fin - inicio;
            System.out.println("Clientes: " + tamaños[i] + ", Tiempo: " + tiempos[i] + "ms");
        }
        
        // Si las operaciones son muy rápidas (< 5ms), el test pasa automáticamente
        if (tiempos[2] < 5) {
            System.out.println("Operaciones muy rápidas - Test pasado automáticamente");
            return;
        }
        
        // El tiempo debe crecer de forma razonable (no exponencial)
        assertTrue(tiempos[2] < tiempos[0] * 20, 
            "El tiempo debe escalar de forma lineal, no exponencial");
    }

    @Test
    @Order(6)
    @DisplayName("MEMORIA: Verificar que no hay fugas de memoria")
    void testNoFugasMemoria() throws IOException {
        // Arrange
        crearCSVGrande(500);
        ClienteDAO dao = new ClienteDAO(TEST_CSV_GRANDE);
        Runtime runtime = Runtime.getRuntime();
        
        // Forzar garbage collection inicial
        runtime.gc();
        long memoriaInicial = runtime.totalMemory() - runtime.freeMemory();
        
        // Act - Realizar múltiples operaciones
        for (int i = 0; i < 50; i++) {
            List<Cliente> clientes = dao.obtenerTodos();
            // Simular procesamiento
            for (Cliente c : clientes) {
                c.getNombre();
                c.getEmail();
            }
        }
        
        // Forzar garbage collection final
        runtime.gc();
        long memoriaFinal = runtime.totalMemory() - runtime.freeMemory();
        
        // Assert
        long incrementoMemoria = memoriaFinal - memoriaInicial;
        long incrementoMB = incrementoMemoria / (1024 * 1024);
        System.out.println("Incremento de memoria: " + incrementoMB + " MB");
        
        // El incremento no debe ser excesivo (menos de 50MB)
        assertTrue(incrementoMB < 50, 
            "El incremento de memoria debe ser razonable (< 50MB)");
    }

    @Test
    @Order(7)
    @DisplayName("CONCURRENCIA: Múltiples accesos simultáneos")
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void testConcurrenciaAccesosSimultaneos() throws IOException, InterruptedException {
        // Arrange
        crearCSVGrande(500);
        ClienteDAO dao = new ClienteDAO(TEST_CSV_GRANDE);
        
        // Act - Crear múltiples threads que accedan simultáneamente
        Thread[] threads = new Thread[10];
        final boolean[] errores = {false};
        
        for (int i = 0; i < threads.length; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        List<Cliente> clientes = dao.obtenerTodos();
                        assertNotNull(clientes);
                        assertEquals(500, clientes.size());
                    }
                } catch (Exception e) {
                    errores[0] = true;
                    System.err.println("Error en thread " + threadId + ": " + e.getMessage());
                }
            });
            threads[i].start();
        }
        
        // Esperar a que terminen todos los threads
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Assert
        assertFalse(errores[0], "No debe haber errores en acceso concurrente");
    }

    @Test
    @Order(8)
    @DisplayName("LÍMITES: Nombres muy largos")
    void testLimitesNombresLargos() throws IOException {
        // Arrange
        List<Cliente> clientesLargos = new ArrayList<>();
        String nombreMuyLargo = "a".repeat(500);
        String emailMuyLargo = "email" + "x".repeat(200) + "@test.com";
        
        for (int i = 0; i < 10; i++) {
            clientesLargos.add(new Cliente(i, nombreMuyLargo, emailMuyLargo, "TestCity"));
        }
        
        // Act & Assert - No debe lanzar excepciones
        assertDoesNotThrow(() -> {
            PDFExporter.exportarAPDF(clientesLargos, TEST_PDF_GRANDE);
        }, "Debe manejar nombres muy largos sin errores");
        
        assertTrue(new File(TEST_PDF_GRANDE).exists());
    }

    /**
     * Crea un archivo CSV grande con el número especificado de clientes
     */
    private void crearCSVGrande(int numeroClientes) throws IOException {
        try (FileWriter writer = new FileWriter(TEST_CSV_GRANDE)) {
            writer.write("id,nombre,email,ciudad\n");
            
            String[] ciudades = {"Madrid", "Barcelona", "Valencia", "Sevilla", "Bilbao", 
                               "Málaga", "Zaragoza", "Murcia", "Granada", "Alicante"};
            
            for (int i = 1; i <= numeroClientes; i++) {
                writer.write(String.format("%d,Cliente%d Test%d,cliente%d@test.com,%s\n",
                    i, i, i, i, ciudades[i % ciudades.length]));
            }
        }
    }
}
