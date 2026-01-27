package com.example.reportgenerator;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Aplicación principal de gestión de clientes con interfaz gráfica JavaFX.
 * 
 * <p>Esta aplicación permite:
 * <ul>
 *   <li>Cargar datos de clientes desde archivos CSV</li>
 *   <li>Filtrar clientes por nombre y ciudad</li>
 *   <li>Visualizar datos en tabla y gráficos</li>
 *   <li>Exportar informes en formato PDF</li>
 * </ul>
 * 
 * <p>La interfaz está organizada en tres secciones principales:
 * <ol>
 *   <li>Barra superior con botones de acción (cargar CSV, exportar PDF)</li>
 *   <li>Panel de filtros para buscar y filtrar clientes</li>
 *   <li>Área de visualización con tabla de datos y gráfico circular</li>
 * </ol>
 * 
 * @author Álvaro
 * @version 1.0
 * @since 2026-01-26
 */
public class HelloApplication extends Application {
    
    private ClienteDAO clienteDAO;
    private TableView<Cliente> tableView;
    private ObservableList<Cliente> clientesActuales;
    private TextField nombreField;
    private ComboBox<String> ciudadComboBox;
    private Label totalLabel;
    private PieChart pieChart;
    private String csvPath;
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("Gestión de Clientes - Informes");
        
        // Layout principal
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Panel superior con botones
        HBox topPanel = createTopPanel(stage);
        root.setTop(topPanel);
        
        // Panel central con filtros, tabla y gráfico
        VBox centerPanel = createCenterPanel();
        root.setCenter(centerPanel);
        
        Scene scene = new Scene(root, 1100, 650);
        stage.setScene(scene);
        stage.show();
    }
    
    private HBox createTopPanel(Stage stage) {
        HBox panel = new HBox(10);
        panel.setPadding(new Insets(10));
        panel.setAlignment(Pos.CENTER_LEFT);
        
        Button seleccionarCSVBtn = new Button("Seleccionar CSV");
        seleccionarCSVBtn.setOnAction(e -> seleccionarCSV(stage));
        
        Button exportarPDFBtn = new Button("Exportar a PDF");
        exportarPDFBtn.setOnAction(e -> exportarAPDF(stage));
        
        Button ayudaBtn = new Button("Ayuda");
        ayudaBtn.setOnAction(e -> mostrarAyuda());
        
        // Espaciador para empujar el botón de ayuda a la derecha
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        panel.getChildren().addAll(seleccionarCSVBtn, exportarPDFBtn, spacer, ayudaBtn);
        return panel;
    }
    
    private VBox createCenterPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        
        // Panel de filtros
        HBox filtrosPanel = createFiltrosPanel();
        
        // Panel con tabla y gráfico
        HBox contenidoPanel = new HBox(10);
        
        // Panel izquierdo con tabla
        VBox tablaPanel = createTablaPanel();
        
        // Panel derecho con gráfico
        VBox graficoPanel = createGraficoPanel();
        
        contenidoPanel.getChildren().addAll(tablaPanel, graficoPanel);
        HBox.setHgrow(tablaPanel, Priority.SOMETIMES);
        HBox.setHgrow(graficoPanel, Priority.SOMETIMES);
        
        panel.getChildren().addAll(filtrosPanel, contenidoPanel);
        return panel;
    }
    
    private HBox createFiltrosPanel() {
        HBox panel = new HBox(10);
        panel.setPadding(new Insets(10));
        panel.setAlignment(Pos.CENTER_LEFT);
        
        Label nombreLabel = new Label("Nombre contiene...");
        nombreField = new TextField();
        nombreField.setPrefWidth(150);
        
        ciudadComboBox = new ComboBox<>();
        ciudadComboBox.getItems().add("Ciudad");
        ciudadComboBox.setValue("Ciudad");
        ciudadComboBox.setPrefWidth(120);
        
        Button aplicarFiltrosBtn = new Button("Aplicar filtros");
        aplicarFiltrosBtn.setOnAction(e -> aplicarFiltros());
        
        panel.getChildren().addAll(nombreLabel, nombreField, ciudadComboBox, aplicarFiltrosBtn);
        return panel;
    }
    
    private VBox createTablaPanel() {
        VBox panel = new VBox(10);
        panel.setPrefWidth(450);
        
        // Tabla
        tableView = new TableView<>();
        clientesActuales = FXCollections.observableArrayList();
        tableView.setItems(clientesActuales);
        tableView.setPlaceholder(new Label("Tabla sin contenido"));
        
        TableColumn<Cliente, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombreCol.setPrefWidth(150);
        
        TableColumn<Cliente, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(150);
        
        TableColumn<Cliente, String> ciudadCol = new TableColumn<>("Ciudad");
        ciudadCol.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        ciudadCol.setPrefWidth(120);
        
        tableView.getColumns().addAll(nombreCol, emailCol, ciudadCol);
        tableView.setPrefHeight(450);
        
        // Label total
        totalLabel = new Label("Total clientes: 0");
        totalLabel.setStyle("-fx-font-weight: bold;");
        
        panel.getChildren().addAll(tableView, totalLabel);
        return panel;
    }
    
    private VBox createGraficoPanel() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.TOP_CENTER);
        
        Label tituloGrafico = new Label("Clientes por ciudad");
        tituloGrafico.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        pieChart = new PieChart();
        pieChart.setTitle("");
        pieChart.setLegendVisible(true);
        pieChart.setPrefSize(500, 500);
        
        panel.getChildren().addAll(tituloGrafico, pieChart);
        return panel;
    }
    
    private void seleccionarCSV(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo CSV");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos CSV", "*.csv")
        );
        
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            csvPath = file.getAbsolutePath();
            clienteDAO = new ClienteDAO(csvPath);
            cargarDatos();
            actualizarCiudadesComboBox();
        }
    }
    
    private void cargarDatos() {
        if (clienteDAO == null) {
            mostrarAlerta("Error", "Primero debe seleccionar un archivo CSV");
            return;
        }
        
        List<Cliente> clientes = clienteDAO.obtenerTodos();
        clientesActuales.clear();
        clientesActuales.addAll(clientes);
        
        totalLabel.setText("Total clientes: " + clientes.size());
        
        actualizarGrafico(clientes);
    }
    
    private void actualizarCiudadesComboBox() {
        if (clienteDAO == null) return;
        
        Map<String, Integer> ciudades = clienteDAO.contarClientesPorCiudad();
        ciudadComboBox.getItems().clear();
        ciudadComboBox.getItems().add("Ciudad");
        ciudadComboBox.getItems().addAll(ciudades.keySet());
        ciudadComboBox.setValue("Ciudad");
    }
    
    private void aplicarFiltros() {
        if (clienteDAO == null) {
            mostrarAlerta("Error", "Primero debe seleccionar un archivo CSV");
            return;
        }
        
        List<Cliente> clientesFiltrados = clienteDAO.obtenerTodos();
        
        // Filtro por nombre
        String nombreFiltro = nombreField.getText().trim();
        if (!nombreFiltro.isEmpty()) {
            clientesFiltrados = clienteDAO.obtenerPorNombreContiene(nombreFiltro);
        }
        
        // Filtro por ciudad
        String ciudadFiltro = ciudadComboBox.getValue();
        if (ciudadFiltro != null && !ciudadFiltro.equals("Ciudad")) {
            if (!nombreFiltro.isEmpty()) {
                // Aplicar ambos filtros
                clientesFiltrados.removeIf(c -> !c.getCiudad().equalsIgnoreCase(ciudadFiltro));
            } else {
                clientesFiltrados = clienteDAO.obtenerPorCiudad(ciudadFiltro);
            }
        }
        
        clientesActuales.clear();
        clientesActuales.addAll(clientesFiltrados);
        
        totalLabel.setText("Total clientes: " + clientesFiltrados.size());
        
        actualizarGrafico(clientesFiltrados);
    }
    
    private void actualizarGrafico(List<Cliente> clientes) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        Map<String, Integer> ciudadCount = new java.util.HashMap<>();
        for (Cliente c : clientes) {
            ciudadCount.put(c.getCiudad(), ciudadCount.getOrDefault(c.getCiudad(), 0) + 1);
        }
        
        for (Map.Entry<String, Integer> entry : ciudadCount.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        
        pieChart.setData(pieChartData);
    }
    
    private void exportarAPDF(Stage stage) {
        if (clienteDAO == null || clientesActuales.isEmpty()) {
            mostrarAlerta("Error", "No hay datos para exportar");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar PDF");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        fileChooser.setInitialFileName("informe_clientes.pdf");
        
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                PDFExporter.exportarAPDF(clientesActuales, file.getAbsolutePath());
                mostrarAlerta("Éxito", "PDF exportado correctamente");
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al exportar PDF: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(titulo.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    /**
     * Muestra un cuadro de diálogo con información sobre el autor y el código fuente.
     * Incluye enlaces al repositorio de GitHub donde se aloja el proyecto.
     */
    private void mostrarAyuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de la Aplicación");
        alert.setHeaderText("Gestor de Clientes - Informes PDF");
        
        // Crear contenido con formato
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        Label autorLabel = new Label("Autor: Álvaro");
        autorLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        Label versionLabel = new Label("Versión: 1.0");
        Label fechaLabel = new Label("Fecha: Enero 2026");
        
        Label descripcionLabel = new Label(
            "Aplicación de gestión de clientes con generación de informes PDF.\n" +
            "Desarrollada con JavaFX y Apache PDFBox."
        );
        descripcionLabel.setWrapText(true);
        
        Separator separator = new Separator();
        
        Label repoLabel = new Label("Código fuente:");
        repoLabel.setStyle("-fx-font-weight: bold;");
        
        // Crear un Hyperlink para el repositorio de GitHub
        Hyperlink githubLink = new Hyperlink("https://github.com/alvarocrsc/ReportGenerator");
        githubLink.setOnAction(e -> {
            try {
                // Intentar abrir el enlace en el navegador
                java.awt.Desktop.getDesktop().browse(
                    new java.net.URI("https://github.com/alvarocrsc/ReportGenerator")
                );
            } catch (Exception ex) {
                System.err.println("No se pudo abrir el enlace: " + ex.getMessage());
            }
        });
        
        Label licenciaLabel = new Label("Licencia: MIT");
        licenciaLabel.setStyle("-fx-font-style: italic; -fx-font-size: 10px;");
        
        content.getChildren().addAll(
            autorLabel,
            versionLabel,
            fechaLabel,
            new Label(""),
            descripcionLabel,
            separator,
            repoLabel,
            githubLink,
            new Label(""),
            licenciaLabel
        );
        
        alert.getDialogPane().setContent(content);
        alert.showAndWait();
    }
}

