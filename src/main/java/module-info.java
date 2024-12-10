module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.desktop;
    requires fop;
    requires org.apache.logging.log4j;
    requires java.sql;
    requires xmlgraphics.commons;

    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
    opens org.example.demo.page_controllers to javafx.fxml;
    exports org.example.demo.page_controllers;
    exports org.example.demo.objectclasses;
    opens org.example.demo.objectclasses to javafx.fxml;
    exports org.example.demo.exceptions;
    opens org.example.demo.exceptions to javafx.fxml;
    exports org.example.demo.database;
    opens org.example.demo.database to javafx.fxml;
    exports org.example.demo.page_controllers.concerts_buttons;
    opens org.example.demo.page_controllers.concerts_buttons to javafx.fxml;
    exports org.example.demo.page_controllers.music_groups_buttons;
    opens org.example.demo.page_controllers.music_groups_buttons to javafx.fxml;
    exports org.example.demo.page_controllers.repertoire_buttons;
    opens org.example.demo.page_controllers.repertoire_buttons to javafx.fxml;
    exports org.example.demo.page_controllers.artists_buttons;
    opens org.example.demo.page_controllers.artists_buttons to javafx.fxml;
}