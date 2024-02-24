module com.example.projectakhirasd {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.projectakhirasd to javafx.fxml;
    exports com.example.projectakhirasd;
}