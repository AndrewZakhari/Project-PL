module com.inventory.pl_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires com.google.gson;
    requires jakarta.mail;

    opens com.inventory.pl_project to javafx.fxml;
    opens com.inventory.pl_project.controllers to javafx.fxml;
    opens com.inventory.pl_project.models to com.google.gson;
    exports com.inventory.pl_project;
    exports com.inventory.pl_project.controllers;
}