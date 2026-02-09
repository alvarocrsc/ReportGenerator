# 🧪 Documentación de Pruebas

## 📋 Pruebas Disponibles

El proyecto incluye **53 pruebas activas** distribuidas en 5 clases:

### 1. ClienteTest (8 pruebas)
Pruebas unitarias del modelo `Cliente`.

### 2. ClienteDAOTest (13 pruebas)
Pruebas de acceso a datos desde CSV.

### 3. PDFExporterTest (10 pruebas)
Pruebas de generación de archivos PDF.

### 4. IntegracionSistemaTest (7 pruebas)
Pruebas del flujo completo: CSV → filtrado → PDF.

### 5. RendimientoYVolumenTest (8 pruebas)
Pruebas de rendimiento con grandes volúmenes de datos (1000-5000 clientes).

### 6. SeguridadTest (7 pruebas activas)
Pruebas de seguridad: inyección de código, path traversal, DoS, límites.

## 🚀 Cómo Ejecutar las Pruebas

### Ejecutar todas las pruebas:
```bash
mvn test
```

### Ejecutar una clase específica:
```bash
mvn test -Dtest=ClienteTest
mvn test -Dtest=IntegracionSistemaTest
```

### Ejecutar con informe detallado:
```bash
mvn clean test
```

## 📊 Resultado Esperado

```
Tests run: 57
Failures: 0
Errors: 0
Skipped: 4
BUILD SUCCESS
```

## 📁 Ubicación

Las pruebas están en: `src/test/java/com/example/reportgenerator/`

---

**Repositorio:** https://github.com/alvarocrsc/ReportGenerator
