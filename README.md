# ReportGenerator - Gestor de Clientes con Informes PDF

![Java](https://img.shields.io/badge/Java-25-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-blue)
![Maven](https://img.shields.io/badge/Maven-Project-red)
![License](https://img.shields.io/badge/License-MIT-green)

Aplicación de escritorio desarrollada en JavaFX para la gestión de clientes con capacidad de generar informes profesionales en formato PDF.

## Características Principales

- **Carga de datos desde CSV**: Importa fácilmente tus datos de clientes desde archivos CSV
- **Filtros avanzados**: Busca clientes por nombre o filtra por ciudad
- **Visualización en tabla**: Vista organizada de todos los clientes
- **Gráficos estadísticos**: Gráfico circular mostrando la distribución de clientes por ciudad
- **Exportación a PDF**: Genera informes profesionales con un solo clic
- **Interfaz intuitiva**: Diseño limpio y fácil de usar

## Capturas de Pantalla

### Pantalla Principal
```
┌─────────────────────────────────────────────────────────────────┐
│  [Seleccionar CSV] [Exportar a PDF]            [Ayuda]          │
├─────────────────────────────────────────────────────────────────┤
│  Nombre contiene: [________] [Ciudad] [Aplicar filtros]         │
├──────────────────────────────┬──────────────────────────────────┤
│                              │                                  │
│  TABLA DE CLIENTES           │    GRÁFICO CIRCULAR              │
│  ┌─────────────────────────┐ │   Clientes por ciudad            │
│  │ Nombre  Email  Ciudad   │ │                                  │
│  │ Ana L.  ana@..  Madrid  │ │        Madrid: 45%               │
│  │ Carlos  car@..  Valencia│ │        Barcelona: 30%            │
│  │ ...                     │ │        Valencia: 15%             │
│  └─────────────────────────┘ │        Sevilla: 10%              │
│  Total clientes: 150         │                                  │
└──────────────────────────────┴──────────────────────────────────┘
```

## Inicio Rápido

### Requisitos Previos

- **Java JDK 25** o superior
- **Maven 3.6+**
- Sistema operativo: Windows, macOS o Linux

### Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/usuario/ReportGenerator.git
cd ReportGenerator
```

2. **Compilar el proyecto**
```bash
mvn clean compile
```

3. **Ejecutar la aplicación**
```bash
mvn javafx:run
```

## Cómo Usar la Aplicación

### 1. Cargar Datos
- Haz clic en el botón **"Seleccionar CSV"**
- Navega hasta tu archivo CSV con los datos de clientes
- El formato debe ser: `id,nombre,email,ciudad`

### 2. Filtrar Clientes
- **Por nombre**: Escribe parte del nombre en el campo de texto
- **Por ciudad**: Selecciona una ciudad del desplegable
- Haz clic en **"Aplicar filtros"**
- Puedes combinar ambos filtros

### 3. Visualizar Datos
- La tabla muestra todos los clientes filtrados
- El gráfico circular se actualiza automáticamente
- El contador muestra el total de clientes visibles

### 4. Exportar a PDF
- Haz clic en **"Exportar a PDF"**
- Elige la ubicación y nombre del archivo
- El PDF incluirá:
  - Listado completo de clientes filtrados
  - Gráfico de distribución por ciudad
  - Totales y estadísticas

### 5. Ver Información de Ayuda
- Haz clic en el botón **"Ayuda"**
- Verás información del autor y enlace al código fuente

## Formato del Archivo CSV

El archivo CSV debe seguir esta estructura:

```csv
id,nombre,email,ciudad
1,Ana López,ana.lopez@email.com,Madrid
2,Carlos Ruiz,carlos.ruiz@email.com,Valencia
3,Lucía Pérez,lucia.perez@email.com,Sevilla
```

**Requisitos:**
- Primera línea: cabecera con los nombres de las columnas
- Separador: coma (`,`)
- Codificación: UTF-8
- Campos obligatorios: `id`, `nombre`, `email`, `ciudad`

**Para más detalles**, consulta [CSV_FORMAT.md](docs/CSV_FORMAT.md)

## Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 25 | Lenguaje de programación principal |
| **JavaFX** | 21.0.6 | Framework para la interfaz gráfica |
| **Apache PDFBox** | 3.0.6 | Generación de documentos PDF |
| **JFreeChart** | 1.5.6 | Creación de gráficos estadísticos |
| **Maven** | 3.x | Gestión de dependencias y build |

**Licencias de bibliotecas**: Ver [LIBRARIES.md](docs/LIBRARIES.md)

## Estructura del Proyecto

```
ReportGenerator/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/reportgenerator/
│   │   │       ├── HelloApplication.java    # Aplicación principal
│   │   │       ├── Cliente.java             # Modelo de datos
│   │   │       ├── ClienteDAO.java          # Acceso a datos
│   │   │       ├── PDFExporter.java         # Exportación PDF
│   │   │       └── Launcher.java            # Punto de entrada
│   │   └── resources/
│   │       ├── clientes.csv                 # Datos de ejemplo
│   │       └── com/example/reportgenerator/
│   │           └── hello-view.fxml          # Vista (si se usa)
│   └── test/
├── docs/                                    # Documentación adicional
├── pom.xml                                  # Configuración Maven
└── README.md                                # Este archivo
```

## Documentación Adicional

- [Guía de Uso Completa](docs/USAGE_GUIDE.md) - Instrucciones detalladas
- [Formato CSV](docs/CSV_FORMAT.md) - Especificación del formato de datos
- [Bibliotecas y Licencias](docs/LIBRARIES.md) - Información de dependencias
- [Tutorial PDF](docs/TUTORIAL_PDF.md) - Tutorial paso a paso para generar PDFs

## Autor

**Álvaro**
- GitHub: [@usuario](https://github.com/usuario)
- Proyecto: [ReportGenerator](https://github.com/usuario/ReportGenerator)

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Haz un Fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## Soporte

Si encuentras algún problema o tienes sugerencias:
- Abre un [Issue](https://github.com/usuario/ReportGenerator/issues)
- Contacta al autor a través de GitHub

---

**¡Si te ha sido útil este proyecto, dale una estrella!**

© 2026 Álvaro - Desarrollado para DAM (Desarrollo de Aplicaciones Multiplataforma)
