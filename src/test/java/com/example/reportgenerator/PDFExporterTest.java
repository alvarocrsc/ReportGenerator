package com.example.reportgenerator;

import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase PDFExporter.
 * Verifica que se genera correctamente el archivo PDF con diferentes conjuntos de datos.
 */
@DisplayName("Pruebas unitarias de PDFExporter")
class PDFExporterTest {

    private static final String TEST_PDF_PATH = "test_output.pdf";
    private List<Cliente> clientesPrueba;

    @BeforeEach
    void setUp() {
        clientesPrueba = new ArrayList<>();
        clientesPrueba.add(new Cliente(1, "Ana López", "ana@email.com", "Madrid"));
        clientesPrueba.add(new Cliente(2, "Carlos Ruiz", "carlos@email.com", "Barcelona"));
        clientesPrueba.add(new Cliente(3, "María García", "maria@email.com", "Madrid"));
    }

    @AfterEach
    void tearDown() {
        // Limpiar archivo PDF de prueba
        File pdfFile = new File(TEST_PDF_PATH);
        if (pdfFile.exists()) {
            pdfFile.delete();
        }
    }

    @Test
    @DisplayName("exportarAPDF() crea un archivo PDF válido")
    void testExportarAPDFCreaArchivo() throws IOException {
        // Act
        PDFExporter.exportarAPDF(clientesPrueba, TEST_PDF_PATH);
        
        // Assert
        File pdfFile = new File(TEST_PDF_PATH);
        assertTrue(pdfFile.exists(), "El archivo PDF debe existir");
        assertTrue(pdfFile.length() > 0, "El archivo PDF debe tener contenido");
    }

    @Test
    @DisplayName("exportarAPDF() funciona con lista vacía")
    void testExportarAPDFListaVacia() throws IOException {
        // Arrange
        List<Cliente> listaVacia = new ArrayList<>();
        
        // Act
        PDFExporter.exportarAPDF(listaVacia, TEST_PDF_PATH);
        
        // Assert
        File pdfFile = new File(TEST_PDF_PATH);
        assertTrue(pdfFile.exists(), "El archivo PDF debe existir incluso con lista vacía");
        assertTrue(pdfFile.length() > 0, "El PDF debe tener al menos la estructura básica");
    }

    @Test
    @DisplayName("exportarAPDF() funciona con un solo cliente")
    void testExportarAPDFUnSoloCliente() throws IOException {
        // Arrange
        List<Cliente> unCliente = new ArrayList<>();
        unCliente.add(new Cliente(1, "Cliente Único", "unico@test.com", "TestCity"));
        
        // Act
        PDFExporter.exportarAPDF(unCliente, TEST_PDF_PATH);
        
        // Assert
        File pdfFile = new File(TEST_PDF_PATH);
        assertTrue(pdfFile.exists());
        assertTrue(pdfFile.length() > 0);
    }

    @Test
    @DisplayName("exportarAPDF() funciona con muchos clientes")
    void testExportarAPDFMuchosClientes() throws IOException {
        // Arrange - Crear 100 clientes
        List<Cliente> muchosClientes = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            muchosClientes.add(new Cliente(i, "Cliente" + i, "cliente" + i + "@test.com", 
                                          i % 3 == 0 ? "Madrid" : i % 3 == 1 ? "Barcelona" : "Valencia"));
        }
        
        // Act
        PDFExporter.exportarAPDF(muchosClientes, TEST_PDF_PATH);
        
        // Assert
        File pdfFile = new File(TEST_PDF_PATH);
        assertTrue(pdfFile.exists());
        assertTrue(pdfFile.length() > 10000, "El PDF con 100 clientes debe ser > 10KB");
    }

    @Test
    @DisplayName("exportarAPDF() maneja clientes con nombres largos")
    void testExportarAPDFNombresLargos() throws IOException {
        // Arrange
        List<Cliente> clientesNombresLargos = new ArrayList<>();
        clientesNombresLargos.add(new Cliente(1, 
            "Juan Sebastián Alejandro Maximiano de la Rosa García López", 
            "juan.sebastian.largo@dominio-muy-largo.com", 
            "Madrid"));
        
        // Act
        PDFExporter.exportarAPDF(clientesNombresLargos, TEST_PDF_PATH);
        
        // Assert
        File pdfFile = new File(TEST_PDF_PATH);
        assertTrue(pdfFile.exists());
    }

    @Test
    @DisplayName("exportarAPDF() maneja caracteres especiales")
    void testExportarAPDFCaracteresEspeciales() throws IOException {
        // Arrange
        List<Cliente> clientesEspeciales = new ArrayList<>();
        clientesEspeciales.add(new Cliente(1, "José María Ñoño", "josé@ñoño.es", "Barcelona"));
        clientesEspeciales.add(new Cliente(2, "François Müller", "françois@test.de", "München"));
        
        // Act
        PDFExporter.exportarAPDF(clientesEspeciales, TEST_PDF_PATH);
        
        // Assert
        File pdfFile = new File(TEST_PDF_PATH);
        assertTrue(pdfFile.exists());
    }

    @Test
    @DisplayName("exportarAPDF() crea PDFs con diferentes tamaños")
    void testExportarAPDFDiferentesTamaños() throws IOException {
        // Act & Assert - PDF pequeño (1 cliente)
        List<Cliente> unCliente = new ArrayList<>();
        unCliente.add(new Cliente(1, "Cliente1", "test1@test.com", "Madrid"));
        PDFExporter.exportarAPDF(unCliente, "test_small.pdf");
        File smallPdf = new File("test_small.pdf");
        assertTrue(smallPdf.exists());
        long smallSize = smallPdf.length();
        
        // Act & Assert - PDF grande (100 clientes)
        List<Cliente> muchosClientes = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            muchosClientes.add(new Cliente(i, "Cliente" + i, "test" + i + "@test.com", "Ciudad" + (i % 5)));
        }
        PDFExporter.exportarAPDF(muchosClientes, "test_large.pdf");
        File largePdf = new File("test_large.pdf");
        assertTrue(largePdf.exists());
        long largeSize = largePdf.length();
        
        // El PDF grande debe ser igual o mayor (gráficos pueden dominar el tamaño)
        assertTrue(largeSize >= smallSize, "El PDF con más clientes debe ser igual o más grande");
        
        // Cleanup
        smallPdf.delete();
        largePdf.delete();
    }

    @Test
    @DisplayName("exportarAPDF() puede sobrescribir archivo existente")
    void testExportarAPDFSobrescribeArchivo() throws IOException {
        // Arrange - Crear primer PDF
        PDFExporter.exportarAPDF(clientesPrueba, TEST_PDF_PATH);
        File pdfFile = new File(TEST_PDF_PATH);
        long primerTamaño = pdfFile.length();
        
        // Act - Crear segundo PDF con más datos
        List<Cliente> masClientes = new ArrayList<>(clientesPrueba);
        for (int i = 4; i <= 10; i++) {
            masClientes.add(new Cliente(i, "Cliente" + i, "test" + i + "@test.com", "Valencia"));
        }
        PDFExporter.exportarAPDF(masClientes, TEST_PDF_PATH);
        
        // Assert
        assertTrue(pdfFile.exists());
        long segundoTamaño = pdfFile.length();
        assertTrue(segundoTamaño > primerTamaño, "El segundo PDF debe ser más grande");
    }

    @Test
    @DisplayName("exportarAPDF() funciona con diferentes extensiones de archivo")
    void testExportarAPDFDiferentesExtensiones() throws IOException {
        // Act
        String[] rutas = {"test.pdf", "test.PDF", "output/test.pdf", "test_file.pdf"};
        
        for (String ruta : rutas) {
            if (ruta.contains("/")) {
                new File("output").mkdirs();
            }
            PDFExporter.exportarAPDF(clientesPrueba, ruta);
            
            // Assert
            File pdf = new File(ruta);
            assertTrue(pdf.exists(), "El archivo " + ruta + " debe existir");
            
            // Cleanup
            pdf.delete();
        }
        
        // Cleanup directorio
        new File("output").delete();
    }

    @Test
    @DisplayName("exportarAPDF() lanza IOException con ruta inválida")
    void testExportarAPDFRutaInvalida() {
        // Arrange - Ruta con caracteres inválidos en algunos sistemas
        String rutaInvalida = "/ruta/inexistente/no/existe/test.pdf";
        
        // Act & Assert
        assertThrows(IOException.class, () -> {
            PDFExporter.exportarAPDF(clientesPrueba, rutaInvalida);
        }, "Debe lanzar IOException con ruta inválida");
    }
}
