package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import joc.Jucator;
import services.IObserver;
import services.IServices;
import services.MyException;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControllerPrincipal extends UnicastRemoteObject implements Controller, IObserver, Serializable {
     Jucator jucatorConectat;
     IServices service;

    @FXML
    TextField poz1;
    @FXML
    TextField poz2;
    @FXML
    TextField poz3;
    @FXML
    Button btnStart;
    @FXML
    Label cuvant1;
    @FXML
    Label cuvant2;
    @FXML
    Label cuvant3;
    @FXML
    Label jucator1;
    @FXML
    Label jucator2;
    @FXML
    Label jucator3;
    @FXML
    ListView<String> listView;
    @FXML
    TextField propunere;
    @FXML
    Button btnTrimite;
    @FXML
    TextArea textArea;


    public ControllerPrincipal() throws RemoteException { }
    public void setContext(IServices server) {
        this.service=server;
    }
    private void initModel() { }
    @Override
    public void setStage(Stage probeStage) { }
    public void setUserConnectat(Jucator jucatorConectat) throws MyException   {
        this.jucatorConectat = jucatorConectat;
    }
    @Override
    public void initialize() {

    }


    public void logout() {
        try {
            service.logout(jucatorConectat,this);
            System.exit(0);
        } catch (MyException e) {
            System.out.println("Logout error " + e);
        }
    }

    public void handleLogout(ActionEvent actionEvent) {

        logout();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

    }


    public void handleStart(ActionEvent actionEvent) {
        try {
            int pozitie1 = Integer.parseInt(poz1.getText());
            int pozitie2 = Integer.parseInt(poz2.getText());
            int pozitie3 = Integer.parseInt(poz3.getText());
            if(pozitie1 >= 1 && pozitie1 <= 9){
                if(pozitie2 >= 1 && pozitie2 <= 9){
                    if(pozitie3 >= 1 && pozitie3 <= 9) {
                        service.start(this.jucatorConectat, pozitie1, pozitie2, pozitie3);

                        btnStart.setDisable(true);
                        poz1.setDisable(true);
                        poz2.setDisable(true);
                        poz3.setDisable(true);
                    }
                }
            }
        }
        catch(NumberFormatException e){
            MessageBox.showErrorMessage(null, e.getMessage());
        }
    }


    //List<String> readyToPlay = lista de useri dati in service
    @Override
    public void notifyNewGame(List<String> readyToPlay) throws MyException, RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                jucator1.setText(readyToPlay.get(0));
                jucator2.setText(readyToPlay.get(1));
                jucator3.setText(readyToPlay.get(2));

                cuvant1.setText("_ _ _ _ _ _ _ _ _");
                cuvant2.setText("_ _ _ _ _ _ _ _ _");
                cuvant3.setText("_ _ _ _ _ _ _ _ _");

                btnTrimite.setDisable(false);
                propunere.setDisable(false);
                listView.setDisable(false);

                if(listView.getItems().size()>0) {
                    listView.getItems().clear();
                }

                listView.getItems().addAll(readyToPlay);
                listView.getItems().remove(jucatorConectat.getId());
            }
        });
    }

    @Override
    public void notifyWinner(String textMesaj) throws MyException, RemoteException {
        Platform.runLater((new Runnable(){

            @Override
            public void run() {
                textArea.clear();
                textArea.setText(textMesaj);
                jucator1.setText("");
                jucator2.setText("");
                jucator3.setText("");
                cuvant1.setText("");
                cuvant2.setText("");
                cuvant3.setText("");
            }
        }));
    }

    @Override
    public void notifyWinner(String textMesaj, Map<String, String> cuvinteSelectate) throws MyException, RemoteException {
        Platform.runLater((new Runnable(){

            @Override
            public void run() {
                textArea.clear();
                textArea.setText(textMesaj);
                List<String> keys = new ArrayList<>(cuvinteSelectate.keySet());

                jucator1.setText(keys.get(0));
                cuvant1.setText(cuvinteSelectate.get(keys.get(0)));

                jucator2.setText(keys.get(1));
                cuvant2.setText(cuvinteSelectate.get(keys.get(1)));

                jucator3.setText(keys.get(2));
                cuvant3.setText(cuvinteSelectate.get(keys.get(2)));

                listView.setDisable(false);
                btnTrimite.setDisable(false);
                propunere.setDisable(false);
            }
        }));
    }



    public void handleTrimitePropunere(ActionEvent actionEvent) {
        try{
            String username = listView.getSelectionModel().getSelectedItem();
            String pozitiePropusa = propunere.getText();
            int pozInt = Integer.parseInt(pozitiePropusa);
            if(username != null ){
                service.joacaRunda(jucatorConectat.getId(),username,pozInt);
                btnTrimite.setDisable(true);
            }
            else MessageBox.showErrorMessage(null,"Alegti un jucator!");

        } catch (Exception e) {
            MessageBox.showErrorMessage(null,e.getMessage());
        }
    }
}
