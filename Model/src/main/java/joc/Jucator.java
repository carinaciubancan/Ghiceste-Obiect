package joc;

import java.io.Serializable;
import java.util.Objects;

public class Jucator implements Entity<String>, Serializable {

    private String id;
    private String parola;


    public Jucator() {

    }

    public Jucator(String parola) {
        this.parola = parola;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jucator jucator = (Jucator) o;
        return Objects.equals(id, jucator.id) &&
                Objects.equals(parola, jucator.parola);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parola);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }
}
