package joc;

import java.io.Serializable;
import java.util.Objects;

public class Propunere implements Entity<Integer>, Serializable {
    private int id;
    private int idJoc;
    private int nrRunda;
    private String username;
    private String jucatorPropus;
    private int pozAleasa;
    private  int nrPuncte;

    public Propunere(){}

    public Propunere(int idJoc, int nrRunda, String username, String jucatorPropus, int pozAleasa, int nrPuncte) {
        this.idJoc = idJoc;
        this.nrRunda = nrRunda;
        this.username = username;
        this.jucatorPropus = jucatorPropus;
        this.pozAleasa = pozAleasa;
        this.nrPuncte = nrPuncte;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer integer) {
        this.id=integer;
    }

    public int getIdJoc() {
        return idJoc;
    }

    public void setIdJoc(int idJoc) {
        this.idJoc = idJoc;
    }

    public int getNrRunda() {
        return nrRunda;
    }

    public void setNrRunda(int nrRunda) {
        this.nrRunda = nrRunda;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJucatorPropus() {
        return jucatorPropus;
    }

    public void setJucatorPropus(String jucatorPropus) {
        this.jucatorPropus = jucatorPropus;
    }

    public int getPozAleasa() {
        return pozAleasa;
    }

    public void setPozAleasa(int pozAleasa) {
        this.pozAleasa = pozAleasa;
    }

    public int getNrPuncte() {
        return nrPuncte;
    }

    public void setNrPuncte(int nrPuncte) {
        this.nrPuncte = nrPuncte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Propunere propunere = (Propunere) o;
        return id == propunere.id &&
                idJoc == propunere.idJoc &&
                nrRunda == propunere.nrRunda &&
                pozAleasa == propunere.pozAleasa &&
                nrPuncte == propunere.nrPuncte &&
                Objects.equals(username, propunere.username) &&
                Objects.equals(jucatorPropus, propunere.jucatorPropus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idJoc, nrRunda, username, jucatorPropus, pozAleasa, nrPuncte);
    }

    @Override
    public String toString() {
        return "Propunere{" +
                "id=" + id +
                ", idJoc=" + idJoc +
                ", nrRunda=" + nrRunda +
                ", username='" + username + '\'' +
                ", jucatorPropus='" + jucatorPropus + '\'' +
                ", pozAleasa=" + pozAleasa +
                ", nrPuncte=" + nrPuncte +
                '}';
    }

    public String getPropunereDTO() {
        return "  joc= " + idJoc + "  jucator= " + username + "  jucator propus= " + jucatorPropus + "  pozitie aleasa= " + pozAleasa + "  punctaj= " + nrPuncte;
    }

}
