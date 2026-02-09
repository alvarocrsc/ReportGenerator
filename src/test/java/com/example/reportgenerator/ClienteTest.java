package com.example.reportgenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Cliente.
 * Verifica el correcto funcionamiento del constructor y los métodos getter.
 */
@DisplayName("Pruebas unitarias de Cliente")
class ClienteTest {

    @Test
    @DisplayName("Constructor crea cliente con todos los atributos correctos")
    void testConstructorConValoresValidos() {
        // Arrange & Act
        Cliente cliente = new Cliente(1, "Ana López", "ana@email.com", "Madrid");
        
        // Assert
        assertEquals(1, cliente.getId(), "El ID debe ser 1");
        assertEquals("Ana López", cliente.getNombre(), "El nombre debe ser 'Ana López'");
        assertEquals("ana@email.com", cliente.getEmail(), "El email debe ser 'ana@email.com'");
        assertEquals("Madrid", cliente.getCiudad(), "La ciudad debe ser 'Madrid'");
    }

    @Test
    @DisplayName("Constructor funciona con diferentes valores numéricos de ID")
    void testConstructorConDiferentesIDs() {
        // Arrange & Act
        Cliente cliente1 = new Cliente(0, "Test", "test@test.com", "Test");
        Cliente cliente2 = new Cliente(999, "Test", "test@test.com", "Test");
        Cliente cliente3 = new Cliente(-1, "Test", "test@test.com", "Test");
        
        // Assert
        assertEquals(0, cliente1.getId(), "El ID debe ser 0");
        assertEquals(999, cliente2.getId(), "El ID debe ser 999");
        assertEquals(-1, cliente3.getId(), "El ID puede ser negativo");
    }

    @Test
    @DisplayName("Constructor funciona con cadenas vacías")
    void testConstructorConCadenasVacias() {
        // Arrange & Act
        Cliente cliente = new Cliente(1, "", "", "");
        
        // Assert
        assertEquals("", cliente.getNombre(), "El nombre debe ser cadena vacía");
        assertEquals("", cliente.getEmail(), "El email debe ser cadena vacía");
        assertEquals("", cliente.getCiudad(), "La ciudad debe ser cadena vacía");
    }

    @Test
    @DisplayName("Constructor funciona con valores nulos")
    void testConstructorConValoresNulos() {
        // Arrange & Act
        Cliente cliente = new Cliente(1, null, null, null);
        
        // Assert
        assertNull(cliente.getNombre(), "El nombre debe ser null");
        assertNull(cliente.getEmail(), "El email debe ser null");
        assertNull(cliente.getCiudad(), "La ciudad debe ser null");
    }

    @Test
    @DisplayName("Constructor funciona con caracteres especiales")
    void testConstructorConCaracteresEspeciales() {
        // Arrange & Act
        Cliente cliente = new Cliente(1, "José María Ñoño", "josé@ñoño.es", "São Paulo");
        
        // Assert
        assertEquals("José María Ñoño", cliente.getNombre());
        assertEquals("josé@ñoño.es", cliente.getEmail());
        assertEquals("São Paulo", cliente.getCiudad());
    }

    @Test
    @DisplayName("Constructor funciona con nombres largos")
    void testConstructorConNombresLargos() {
        // Arrange
        String nombreLargo = "a".repeat(100);
        String emailLargo = "test@" + "domain".repeat(20) + ".com";
        String ciudadLarga = "Ciudad" + "Nombre".repeat(20);
        
        // Act
        Cliente cliente = new Cliente(1, nombreLargo, emailLargo, ciudadLarga);
        
        // Assert
        assertEquals(nombreLargo, cliente.getNombre());
        assertEquals(emailLargo, cliente.getEmail());
        assertEquals(ciudadLarga, cliente.getCiudad());
    }

    @Test
    @DisplayName("Getters devuelven los mismos valores asignados en el constructor")
    void testGettersDevuelvenValoresCorrectos() {
        // Arrange
        Cliente cliente1 = new Cliente(50, "Carlos Ruiz", "carlos@test.com", "Valencia");
        Cliente cliente2 = new Cliente(100, "María García", "maria@test.com", "Sevilla");
        Cliente cliente3 = new Cliente(200, "Luis Pérez", "luis@test.com", "Barcelona");
        
        // Assert - Cliente 1
        assertEquals(50, cliente1.getId());
        assertEquals("Carlos Ruiz", cliente1.getNombre());
        assertEquals("carlos@test.com", cliente1.getEmail());
        assertEquals("Valencia", cliente1.getCiudad());
        
        // Assert - Cliente 2
        assertEquals(100, cliente2.getId());
        assertEquals("María García", cliente2.getNombre());
        assertEquals("maria@test.com", cliente2.getEmail());
        assertEquals("Sevilla", cliente2.getCiudad());
        
        // Assert - Cliente 3
        assertEquals(200, cliente3.getId());
        assertEquals("Luis Pérez", cliente3.getNombre());
        assertEquals("luis@test.com", cliente3.getEmail());
        assertEquals("Barcelona", cliente3.getCiudad());
    }

    @Test
    @DisplayName("Múltiples instancias mantienen sus valores independientes")
    void testMultiplesInstanciasIndependientes() {
        // Arrange & Act
        Cliente c1 = new Cliente(1, "Cliente1", "c1@test.com", "Madrid");
        Cliente c2 = new Cliente(2, "Cliente2", "c2@test.com", "Barcelona");
        
        // Assert - Los valores no se mezclan entre instancias
        assertNotEquals(c1.getId(), c2.getId());
        assertNotEquals(c1.getNombre(), c2.getNombre());
        assertNotEquals(c1.getEmail(), c2.getEmail());
        assertNotEquals(c1.getCiudad(), c2.getCiudad());
    }
}
