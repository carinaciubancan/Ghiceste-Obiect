package joc;

import java.io.Serializable;
import java.util.Objects;

public class Joc implements Entity<Integer>, Serializable {
    private int id;
    private int idJoc;
    private String username;
    private int poz1;
    private int poz2;
    private int poz3;

    public Joc(){

    }


    public Joc(int idJoc, String username, int poz1, int poz2, int poz3) {
        this.idJoc = idJoc;
        this.username = username;
        this.poz1 = poz1;
        this.poz2 = poz2;
        this.poz3 = poz3;
    }

    public int getIdJoc() {
        return idJoc;
    }

    public void setIdJoc(int idJoc) {
        this.idJoc = idJoc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoz1() {
        return poz1;
    }

    public void setPoz1(int poz1) {
        this.poz1 = poz1;
    }

    public int getPoz2() {
        return poz2;
    }

    public void setPoz2(int poz2) {
        this.poz2 = poz2;
    }

    public int getPoz3() {
        return poz3;
    }

    public void setPoz3(int poz3) {
        this.poz3 = poz3;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer integer) {
        this.id=integer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joc joc = (Joc) o;
        return id == joc.id &&
                idJoc == joc.idJoc &&
                poz1 == joc.poz1 &&
                poz2 == joc.poz2 &&
                poz3 == joc.poz3 &&
                Objects.equals(username, joc.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idJoc, username, poz1, poz2, poz3);
    }

    @Override
    public String toString() {
        return "Joc{" +
                "id=" + id +
                ", idJoc=" + idJoc +
                ", username='" + username + '\'' +
                ", poz1=" + poz1 +
                ", poz2=" + poz2 +
                ", poz3=" + poz3 +
                '}';
    }

    public String getJocDTO() {
        return " idJoc= " + idJoc + " jucator= " + username + " poz1= " + poz1 + " poz2= " + poz2 + " poz3= " + poz3;
    }

}
