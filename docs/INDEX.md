# Documentación del Proyecto

## Archivos Disponibles

### Documentación Principal
- [README.md](../README.md) - Descripción general y guía rápida
- [USAGE_GUIDE.md](USAGE_GUIDE.md) - Guía de uso de la aplicación
- [CSV_FORMAT.md](CSV_FORMAT.md) - Formato del archivo CSV
- [TUTORIAL_PDF.md](TUTORIAL_PDF.md) - Tutorial para generar PDFs
- [LIBRARIES.md](LIBRARIES.md) - Bibliotecas y licencias

### Documentación Javadoc

El Javadoc HTML está en: `target/reports/apidocs/`

Para generarlo:
```bash
mvn javadoc:javadoc
```

Para abrirlo:
```bash
open target/reports/apidocs/index.html  # macOS
xdg-open target/reports/apidocs/index.html  # Linux
start target/reports/apidocs/index.html  # Windows
```

Clases documentadas:
- `Cliente.java` - Modelo de datos
- `ClienteDAO.java` - Acceso a datos
- `PDFExporter.java` - Exportación PDF
- `HelloApplication.java` - Aplicación principal

### Capturas de Pantalla

Las capturas están en: `docs/screenshots/`

Incluye 7 capturas que demuestran:
- Javadoc funcionando (tooltip y HTML)
- Aplicación con datos cargados
- Botón de ayuda con información del autor
- Filtros aplicados
- Exportación a PDF
- PDF generado

### Botón de Ayuda

La aplicación incluye un botón "Ayuda" que muestra:
- Nombre del autor: Álvaro
- Versión: 1.0
- Enlace a GitHub: https://github.com/alvarocrsc/ReportGenerator

## Estructura del Proyecto

```
ReportGenerator/
├── README.md                          # Guía principal
├── pom.xml                            # Configuración Maven
├── mvnw / mvnw.cmd                    # Maven wrapper
├── docs/
│   ├── INDEX.md                       # Este archivo
│   ├── CSV_FORMAT.md                  # Formato CSV
│   ├── USAGE_GUIDE.md                 # Guía de uso
│   ├── LIBRARIES.md                   # Bibliotecas y licencias
│   ├── TUTORIAL_PDF.md                # Tutorial PDF
│   └── screenshots/                   # 7 capturas de pantalla
│       ├── javadoc_tooltip.png
│       ├── javadoc_html.png
│       ├── app_datos_cargados.png
│       ├── app_ayuda.png
│       ├── app_filtros.png
│       ├── app_exportar.png
│       └── pdf_generado.png
├── src/main/java/com/example/reportgenerator/
│   ├── Cliente.java                   # Modelo (Javadoc)
│   ├── ClienteDAO.java                # DAO (Javadoc)
│   ├── PDFExporter.java               # Exportador (Javadoc)
│   ├── HelloApplication.java          # App principal (Javadoc + Ayuda)
│   └── Launcher.java                  # Punto de entrada
├── src/main/resources/
│   └── clientes.csv                   # Datos de ejemplo
└── target/reports/apidocs/            # Javadoc HTML generado
    └── index.html                     # Página principal
```