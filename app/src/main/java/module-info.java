module deliverif.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;
    requires java.base;
    requires gs.core;
    requires gs.ui.javafx;
    
    opens deliverif.app to javafx.graphics;
    exports deliverif.app.view;
}