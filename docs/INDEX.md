# Documentación del Proyecto

## Archivos Disponibles

### Documentación Principal
- [README.md](../README.md) - Descripción general y guía rápida
- [USAGE_GUIDE.md](USAGE_GUIDE.md) - Guía de uso de la aplicación
- [CSV_FORMAT.md](CSV_FORMAT.md) - Formato del archivo CSV
- [TUTORIAL_PDF.md](TUTORIAL_PDF.md) - Tutorial para generar PDFs
- [LIBRARIES.md](LIBRARIES.md) - Bibliotecas y licencias

### Documentación Javadoc

El Javadoc HTML está en: `target/site/apidocs/`

Para generarlo:
```bash
mvn javadoc:javadoc
```

Para abrirlo:
```bash
open target/site/apidocs/index.html  # macOS
```

Clases documentadas:
- `Cliente.java` - Modelo de datos
- `ClienteDAO.java` - Acceso a datos
- `PDFExporter.java` - Exportación PDF
- `HelloApplication.java` - Aplicación principal

### Botón de Ayuda

La aplicación incluye un botón "Ayuda" que muestra:
- Nombre del autor: Álvaro
- Versión: 1.0
- Enlace a GitHub

## Cumplimiento de Requisitos RA6

| Requisito | Archivo | Estado |
|-----------|---------|--------|
| 1. Javadoc | `*.java` + `target/site/apidocs/` | Completado |
| 2. Guía rápida | `README.md` | Completado |
| 3. Botón ayuda | `HelloApplication.java` | Completado |
| 4. Formato CSV | `CSV_FORMAT.md` | Completado |
| 5. Guía de uso | `USAGE_GUIDE.md` | Completado |
| 6. Bibliotecas y licencias | `LIBRARIES.md` | Completado |
| 7. Tutorial PDF | `TUTORIAL_PDF.md` | Completado |

## Estructura del Proyecto

```
ReportGenerator/
├── README.md
├── docs/
│   ├── CSV_FORMAT.md
│   ├── USAGE_GUIDE.md
│   ├── LIBRARIES.md
│   └── TUTORIAL_PDF.md
├── src/main/java/
│   └── com/example/reportgenerator/
│       ├── Cliente.java (Javadoc)
│       ├── ClienteDAO.java (Javadoc)
│       ├── PDFExporter.java (Javadoc)
│       └── HelloApplication.java (Javadoc + Ayuda)
└── target/site/apidocs/ (Javadoc HTML)
```