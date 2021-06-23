package controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import joc.Jucator;
import services.IServices;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LoginController extends UnicastRemoteObject implements  Controller, Serializable {

    private Parent prt;
    ControllerPrincipal ctrlPrincipal;
    @FXML
    TextField idTextFieldUsername;
    @FXML
    PasswordField idTextFieldParola;

    @FXML
    Label idLabelParola;
    @FXML
    Label idLabelUsername;

    @FXML
    Button idButtonLogin;

    IServices service;
    Stage loginStage;

    public LoginController() throws RemoteException {


    }
    @Override
    public void initialize() {

    }

    @Override
    public void setStage(Stage loginStage) {
        this.loginStage=loginStage;
    }


    public void handleLogin(ActionEvent actionEvent) {
        String username=idTextFieldUsername.getText();
        String password=idTextFieldParola.getText();

        try{

           Jucator jucator = service.login(username,password, ctrlPrincipal);
            System.out.println(jucator);
            Stage stage=new Stage();
            stage.setScene(new Scene(prt));

            stage.setOnCloseRequest(event -> {
                ctrlPrincipal.logout();
                System.exit(0);
            });
            ctrlPrincipal.setUserConnectat(jucator);


            stage.show();

            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }catch(Exception e){
            MessageBox.showErrorMessage(null,e.getMessage());
        }

    }
    public void setContext(IServices service) throws  RemoteException{
        this.service=service;

    }
    public void setControllerPrincipal(ControllerPrincipal crt){
        ctrlPrincipal=crt;
    }
    public void setParent(Parent p) {
        this.prt = p;
    }


}
