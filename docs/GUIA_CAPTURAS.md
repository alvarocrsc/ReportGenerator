# Guía de Capturas de Pantalla

## Capturas Necesarias

### 1. Javadoc en el IDE (3-4 capturas)

**Captura 1: Tooltip de Javadoc**
1. Abrir una clase en el IDE
2. Escribir código que use un método documentado
3. Hacer hover sobre el método
4. Capturar el tooltip con la descripción Javadoc
5. Guardar como: `javadoc_tooltip.png`

**Captura 2: Javadoc HTML**
1. Ejecutar: `mvn javadoc:javadoc`
2. Abrir: `target/site/apidocs/index.html`
3. Navegar a una clase (ej: Cliente)
4. Capturar la página
5. Guardar como: `javadoc_html.png`

### 2. Aplicación Funcionando (4-5 capturas)

**Captura 3: Pantalla Principal**
- Aplicación abierta sin datos
- Guardar como: `app_principal.png`

**Captura 4: Datos Cargados**
- Tabla llena con clientes
- Gráfico mostrando distribución
- Guardar como: `app_datos_cargados.png`

**Captura 5: Botón de Ayuda**
- Diálogo de ayuda abierto mostrando autor y GitHub
- Guardar como: `app_ayuda.png`

**Captura 6: Filtros Aplicados**
- Filtro por ciudad aplicado
- Tabla mostrando solo clientes filtrados
- Guardar como: `app_filtros.png`

**Captura 7: Exportación PDF**
- Diálogo "Guardar PDF" abierto
- Guardar como: `app_exportar.png`

### 3. PDF Generado (1-2 capturas)

**Captura 8: PDF Abierto**
- Abrir el PDF generado
- Mostrar contenido: tabla, gráfico, totales
- Guardar como: `pdf_generado.png`

## Organización

Crear carpeta para las capturas:
```bash
mkdir -p docs/screenshots
```

Guardar todas las capturas en esa carpeta.

## Consejos

- Usar tamaño de ventana adecuado (no demasiado grande ni pequeño)
- Asegurarse de que el texto sea legible
- Capturar en buena resolución
- No incluir información personal en las capturas

## Total Requerido

Mínimo 8 capturas de pantalla que demuestren:
1. Javadoc funcionando
2. Aplicación completa con todas sus funciones
3. PDF generado correctamente