# Tutorial: Generar Informe PDF

## Pasos Básicos

### 1. Iniciar la Aplicación

```bash
mvn javafx:run
```

### 2. Cargar Datos

1. Clic en **"Seleccionar CSV"**
2. Navegar hasta `src/main/resources/clientes.csv`
3. Clic en **"Abrir"**

**Resultado**: La tabla y el gráfico se llenan con los datos.

### 3. Exportar a PDF

1. Clic en **"Exportar a PDF"**
2. Elegir ubicación y nombre del archivo
3. Clic en **"Guardar"**

**Resultado**: Se genera el PDF con la lista de clientes y el gráfico.

## Usar Filtros (Opcional)

### Filtrar por Ciudad

1. Seleccionar una ciudad del desplegable
2. Clic en **"Aplicar filtros"**
3. El PDF solo incluirá clientes de esa ciudad

### Filtrar por Nombre

1. Escribir parte del nombre en el campo de texto
2. Clic en **"Aplicar filtros"**
3. El PDF solo incluirá clientes que coincidan

## Generar Múltiples Informes

Para crear un informe por ciudad:

1. Filtrar por "Madrid" y exportar como `informe_madrid.pdf`
2. Cambiar filtro a "Barcelona" y exportar como `informe_barcelona.pdf`
3. Repetir para cada ciudad

## Contenido del PDF

El informe incluye:
- Título "Informe de Clientes"
- Tabla con todos los clientes (filtrados o no)
- Total de clientes
- Gráfico circular de distribución por ciudad
- Pie de página con fecha y copyright

## Solución de Problemas

**No se abre el PDF**
- Verificar que tienes un lector de PDF instalado

**Error al cargar CSV**
- Verificar que el archivo tiene el formato correcto
- Ver [CSV_FORMAT.md](CSV_FORMAT.md)

**Gráfico no aparece**
- Asegurarse de que hay datos cargados antes de exportar