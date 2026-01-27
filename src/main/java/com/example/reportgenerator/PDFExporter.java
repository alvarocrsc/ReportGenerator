package com.example.reportgenerator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Clase responsable de exportar los datos de clientes a formato PDF.
 * Genera un informe profesional que incluye:
 * <ul>
 *   <li>Listado detallado de todos los clientes</li>
 *   <li>Gráfico de distribución de clientes por ciudad</li>
 *   <li>Totales y estadísticas</li>
 *   <li>Cabecera y pie de página personalizados</li>
 * </ul>
 * 
 * <p>Utiliza Apache PDFBox para la generación del PDF y JFreeChart
 * para la creación de gráficos estadísticos.
 * 
 * @author Álvaro
 * @version 1.0
 * @since 2026-01-26
 */
public class PDFExporter {
    
    private static final float MARGIN = 50;
    private static final float FONT_SIZE_TITLE = 20;
    private static final float FONT_SIZE_SUBTITLE = 14;
    private static final float FONT_SIZE_NORMAL = 10;
    private static final float LINE_HEIGHT = 15;
    
    /**
     * Exporta una lista de clientes a un archivo PDF con formato profesional.
     * Este método genera un documento PDF completo que incluye:
     * <ul>
     *   <li>Cabecera con título del informe</li>
     *   <li>Listado completo de clientes con sus ciudades</li>
     *   <li>Total de clientes registrados</li>
     *   <li>Gráfico circular (pie chart) mostrando la distribución por ciudad</li>
     *   <li>Pie de página con información de copyright</li>
     * </ul>
     * 
     * <p>El método maneja automáticamente el salto de página cuando el contenido
     * excede el espacio disponible.
     * 
     * @param clientes Lista de clientes a incluir en el informe PDF
     * @param rutaPDF Ruta completa donde se guardará el archivo PDF generado
     * @throws IOException Si ocurre un error durante la creación o escritura del PDF
     */
    public static void exportarAPDF(List<Cliente> clientes, String rutaPDF) throws IOException {
        PDDocument document = new PDDocument();
        
        try {
            // Crear página
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
            float yPosition = page.getMediaBox().getHeight() - MARGIN;
            
            // Cabecera
            yPosition = dibujarCabecera(contentStream, page, yPosition);
            
            // Línea separadora
            yPosition -= 10;
            dibujarLinea(contentStream, MARGIN, yPosition, page.getMediaBox().getWidth() - MARGIN, yPosition);
            yPosition -= 20;
            
            // Contenido - Descripción
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("Este documento presenta un resumen de los clientes registrados en la aplicación.");
            contentStream.endText();
            yPosition -= 30;
            
            // Subtítulo - Listado de clientes
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("Listado de clientes:");
            contentStream.endText();
            yPosition -= 25;
            
            // Lista de clientes
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), FONT_SIZE_NORMAL);
            for (Cliente cliente : clientes) {
                if (yPosition < 150) {
                    // Si no hay espacio, crear nueva página
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = page.getMediaBox().getHeight() - MARGIN;
                }
                
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("- " + cliente.getNombre() + " (" + cliente.getCiudad() + ")");
                contentStream.endText();
                yPosition -= LINE_HEIGHT;
            }
            
            yPosition -= 20;
            
            // Total de clientes
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_SUBTITLE);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("Total de clientes: " + clientes.size());
            contentStream.endText();
            yPosition -= 40;
            
            // Verificar si hay espacio para el gráfico
            if (yPosition < 350) {
                contentStream.close();
                page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = page.getMediaBox().getHeight() - MARGIN;
            }
            
            // Generar y añadir gráfico
            File chartFile = generarGrafico(clientes);
            if (chartFile != null && chartFile.exists()) {
                PDImageXObject chartImage = PDImageXObject.createFromFile(chartFile.getAbsolutePath(), document);
                
                float chartWidth = 300;
                float chartHeight = 200;
                float xPosition = (page.getMediaBox().getWidth() - chartWidth) / 2;
                
                contentStream.drawImage(chartImage, xPosition, yPosition - chartHeight, chartWidth, chartHeight);
                yPosition -= (chartHeight + 30);
                
                // Eliminar archivo temporal
                chartFile.delete();
            }
            
            // Pie de página
            dibujarPieDePagina(contentStream, page);
            
            contentStream.close();
            
            // Guardar documento
            document.save(rutaPDF);
            
        } finally {
            document.close();
        }
    }
    
    /**
     * Dibuja la cabecera del documento PDF con el título principal.
     * 
     * @param contentStream Stream de contenido donde se dibujará la cabecera
     * @param page Página actual del documento
     * @param yPosition Posición vertical inicial para dibujar
     * @return Nueva posición vertical después de dibujar la cabecera
     * @throws IOException Si ocurre un error al escribir en el stream
     */
    private static float dibujarCabecera(PDPageContentStream contentStream, PDPage page, float yPosition) 
            throws IOException {
        // Título
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), FONT_SIZE_TITLE);
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Informe de Clientes");
        contentStream.endText();
        
        return yPosition - 30;
    }
    
    /**
     * Dibuja el pie de página con información de copyright y año actual.
     * El texto se centra automáticamente en la página.
     * 
     * @param contentStream Stream de contenido donde se dibujará el pie de página
     * @param page Página actual del documento
     * @throws IOException Si ocurre un error al escribir en el stream
     */
    private static void dibujarPieDePagina(PDPageContentStream contentStream, PDPage page) 
            throws IOException {
        float yPosition = 30;
        
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year = sdf.format(new Date());
        
        String footer = "© " + year + " Informe generado por la aplicación DAM - Todos los derechos reservados.";
        
        float textWidth = new PDType1Font(Standard14Fonts.FontName.HELVETICA).getStringWidth(footer) / 1000 * 8;
        float xPosition = (page.getMediaBox().getWidth() - textWidth) / 2;
        
        contentStream.beginText();
        contentStream.newLineAtOffset(xPosition, yPosition);
        contentStream.showText(footer);
        contentStream.endText();
    }
    
    /**
     * Dibuja una línea recta entre dos puntos en el documento PDF.
     * 
     * @param contentStream Stream de contenido donde se dibujará la línea
     * @param x1 Coordenada X del punto inicial
     * @param y1 Coordenada Y del punto inicial
     * @param x2 Coordenada X del punto final
     * @param y2 Coordenada Y del punto final
     * @throws IOException Si ocurre un error al escribir en el stream
     */
    private static void dibujarLinea(PDPageContentStream contentStream, float x1, float y1, float x2, float y2) 
            throws IOException {
        contentStream.moveTo(x1, y1);
        contentStream.lineTo(x2, y2);
        contentStream.stroke();
    }
    
    /**
     * Genera un gráfico circular (pie chart) que muestra la distribución de clientes por ciudad.
     * El gráfico se crea usando JFreeChart y se guarda como archivo temporal PNG.
     * 
     * <p>El gráfico incluye:
     * <ul>
     *   <li>Título descriptivo</li>
     *   <li>Leyenda con los nombres de las ciudades</li>
     *   <li>Colores personalizados para cada segmento</li>
     *   <li>Tooltips informativos</li>
     * </ul>
     * 
     * @param clientes Lista de clientes para generar las estadísticas del gráfico
     * @return Archivo temporal que contiene el gráfico en formato PNG, o null si ocurre un error
     */
    private static File generarGrafico(List<Cliente> clientes) {
        try {
            // Crear dataset
            DefaultPieDataset dataset = new DefaultPieDataset();
            
            Map<String, Integer> ciudadCount = new HashMap<>();
            for (Cliente c : clientes) {
                ciudadCount.put(c.getCiudad(), ciudadCount.getOrDefault(c.getCiudad(), 0) + 1);
            }
            
            for (Map.Entry<String, Integer> entry : ciudadCount.entrySet()) {
                dataset.setValue(entry.getKey(), entry.getValue());
            }
            
            // Crear gráfico
            JFreeChart chart = ChartFactory.createPieChart(
                "Clientes por ciudad",
                dataset,
                true,  // legend
                true,  // tooltips
                false  // urls
            );
            
            // Personalizar colores
            org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) chart.getPlot();
            plot.setBackgroundPaint(Color.WHITE);
            
            // Colores personalizados para las ciudades
            Color[] colors = {
                new Color(231, 76, 60),    // Rojo
                new Color(241, 196, 15),   // Amarillo
                new Color(46, 204, 113),   // Verde
                new Color(52, 152, 219)    // Azul
            };
            
            int colorIndex = 0;
            for (Object key : dataset.getKeys()) {
                plot.setSectionPaint((Comparable) key, colors[colorIndex % colors.length]);
                colorIndex++;
            }
            
            // Renderizar a imagen
            BufferedImage image = chart.createBufferedImage(600, 400);
            
            // Guardar en archivo temporal
            File tempFile = File.createTempFile("chart", ".png");
            ImageIO.write(image, "png", tempFile);
            
            return tempFile;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
