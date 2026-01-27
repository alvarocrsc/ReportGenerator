module com.example.reportgenerator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    
    // PDF library
    requires org.apache.pdfbox;
    requires java.desktop;
    
    // JFreeChart for charts
    requires org.jfree.jfreechart;

    opens com.example.reportgenerator to javafx.fxml;
    exports com.example.reportgenerator;
}