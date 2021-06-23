package services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface IObserver extends Remote {
    void notifyNewGame(List<String> readyToPlay)throws MyException, RemoteException;
    void notifyWinner(String textMesaj) throws MyException, RemoteException;
    void notifyWinner(String textMesaj, Map<String,String> cuvinteSelectate) throws MyException, RemoteException;

}
