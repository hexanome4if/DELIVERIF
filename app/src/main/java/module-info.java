module deliverif.app {
    requires javafx.controls;
    requires javafx.fxml;

    opens deliverif.app to javafx.fxml;
    exports deliverif.app.view;
}