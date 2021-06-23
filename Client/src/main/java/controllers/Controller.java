package controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public interface Controller {

    @FXML
    public void initialize();

    void setStage(Stage probeStage);
}
