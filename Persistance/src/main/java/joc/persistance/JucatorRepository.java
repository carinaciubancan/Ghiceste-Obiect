package joc.persistance;

import joc.Jucator;

public interface JucatorRepository extends CrudRepository<Integer, Jucator> {
    public Jucator findUserByUsernameParola(String username, String parola);

}
