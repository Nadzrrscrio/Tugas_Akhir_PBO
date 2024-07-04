module org.example.tugasakhir_pbo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.mail;
    requires java.activation;

    opens org.example.tugasakhir_pbo to javafx.fxml;
    exports org.example.tugasakhir_pbo;
    exports main;
    opens main to javafx.fxml;
}