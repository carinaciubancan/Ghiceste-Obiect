package services;

import joc.Jucator;

public interface IServices {

    public Jucator login(String username, String parola, IObserver obs) throws MyException ;
    public void logout(Jucator jucator, IObserver obs) throws MyException ;

    void start(Jucator jucatorConectat, int pozitie1, int pozitie2, int pozitie3);

    void joacaRunda(String jucator, String jucatorAles, int pozInt)throws MyException;
}
