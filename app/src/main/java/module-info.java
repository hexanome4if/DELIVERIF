module deliverif.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;
    requires java.base;
    
    opens deliverif.app to javafx.fxml;
    exports deliverif.app.view;
}