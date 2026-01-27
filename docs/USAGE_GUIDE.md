# Guía de Uso

## Instalación

### Requisitos
- Java JDK 25 o superior
- Maven 3.6+

### Pasos

1. **Compilar el proyecto**
```bash
mvn clean compile
```

2. **Ejecutar la aplicación**
```bash
mvn javafx:run
```

## Funcionalidades

### Cargar Datos desde CSV

1. Clic en **"Seleccionar CSV"**
2. Elegir archivo CSV con formato: `id,nombre,email,ciudad`
3. Los datos se cargan automáticamente en la tabla y gráfico

### Filtros

**Filtro por nombre:**
- Escribir texto en el campo "Nombre contiene"
- Clic en "Aplicar filtros"

**Filtro por ciudad:**
- Seleccionar ciudad del desplegable
- Clic en "Aplicar filtros"

Los filtros se pueden combinar.

### Visualización

- **Tabla**: Muestra todos los clientes (o filtrados)
- **Gráfico circular**: Distribución de clientes por ciudad
- **Contador**: Total de clientes visibles

### Exportar a PDF

1. Clic en **"Exportar a PDF"**
2. Elegir ubicación y nombre
3. Clic en "Guardar"

El PDF incluye:
- Lista completa de clientes
- Gráfico de distribución
- Totales

### Botón Ayuda

Muestra información sobre:
- Autor del proyecto
- Versión de la aplicación
- Enlace al repositorio GitHub

## Formato CSV

Ver [CSV_FORMAT.md](CSV_FORMAT.md) para especificaciones detalladas.

Estructura básica:
```csv
id,nombre,email,ciudad
1,Ana López,ana@email.com,Madrid
```

## Solución de Problemas

**La aplicación no inicia**
- Verificar versión de Java: `java -version`
- Debe ser Java 25 o superior

**Error al cargar CSV**
- Verificar formato del archivo
- Debe tener cabecera en la primera línea
- Separador debe ser coma (,)

**No se genera el PDF**
- Verificar permisos de escritura en la carpeta destino
- Asegurarse de que hay datos cargados

**Caracteres raros en el PDF**
- Guardar el CSV con codificación UTF-8