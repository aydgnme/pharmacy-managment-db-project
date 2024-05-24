module aydgn.me.pharmacymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.kordamp.bootstrapfx.core;

    opens aydgn.me.pharmacymanagementsystem to javafx.fxml;
    exports aydgn.me.pharmacymanagementsystem;
    exports aydgn.me.pharmacymanagementsystem.model;
    opens aydgn.me.pharmacymanagementsystem.model to javafx.fxml;
}