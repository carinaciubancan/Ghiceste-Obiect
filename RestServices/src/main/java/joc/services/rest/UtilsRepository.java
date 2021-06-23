package joc.services.rest;


import joc.Joc;
import joc.Jucator;

import joc.Propunere;
import joc.persistance.jdbc.JdbcUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UtilsRepository {
    private JdbcUtils jdbcUtils;
    public UtilsRepository() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("C:\\Users\\dell\\Desktop\\UBB_sem4\\MPP\\SubiecteEXAMEN\\Ultimul\\modelMpp\\RestServices\\db.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        jdbcUtils = new JdbcUtils(properties);
    }

    public Jucator findUserByUsername(String us1){
        Connection conn = jdbcUtils.getConnection();
        System.out.println(conn);
        Jucator jucator=null;
        try (PreparedStatement preparedStatement = conn.prepareStatement("select * from \"Jucatori\" where id=?")) {
            preparedStatement.setString(1,us1);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String username2 = resultSet.getString("id");
                    String parola= resultSet.getString("parola");
                    jucator=new Jucator(parola);
                    jucator.setId(username2);
                }
            }
        } catch (SQLException e) {

            System.err.print("Error DB "+e);
        }

        return jucator;
    }


    public List<Jucator> findAll() {
        Connection con=jdbcUtils.getConnection();
        List<Jucator> jucatori = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from \"Jucatori\"")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    String username=result.getString("id");
                    String password=result.getString("parola");
                    Jucator jucator=new Jucator(password);
                    jucator.setId(username);
                    jucatori.add(jucator);
                }
            }
        }catch(SQLException e){
            System.err.println("Error DB"+e);
        }
        return jucatori;
    }

    public List<Joc> findByJoc(int idJoc) {
        Connection con = jdbcUtils.getConnection();
        List<Joc> jocuri = new ArrayList<>();
        try {
            String select = "select * from Joc where idJoc=?";
            PreparedStatement preStm = con.prepareStatement(select);
            preStm.setInt(1, idJoc);
            ResultSet result = preStm.executeQuery();

            while (result.next()) {
                Joc joc = new Joc();
                joc.setId(result.getInt("id"));
                joc.setIdJoc(result.getInt("idJoc"));
                joc.setUsername(result.getString("username"));
                joc.setPoz1(result.getInt("poz1"));
                joc.setPoz2(result.getInt("poz2"));
                joc.setPoz3(result.getInt("poz3"));
                jocuri.add(joc);
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return jocuri;
    }

    public List<Propunere> findByJocJucator(int idJoc, String username) {
        Connection con = jdbcUtils.getConnection();
        List<Propunere> propuneri = new ArrayList<>();
        try {
            String select = "select * from Propunere where idJoc=? and username=?";
            PreparedStatement preStm = con.prepareStatement(select);
            preStm.setInt(1, idJoc);
            preStm.setString(2, username);
            ResultSet result = preStm.executeQuery();

            while (result.next()) {
                Propunere propunere = new Propunere();
                propunere.setId(result.getInt("id"));
                propunere.setIdJoc(result.getInt("idJoc"));
                propunere.setNrRunda(result.getInt("nrRunda"));
                propunere.setUsername(result.getString("username"));
                propunere.setPozAleasa(result.getInt("pozAleasa"));
                propunere.setNrPuncte(result.getInt("nrPuncte"));
                propunere.setJucatorPropus(result.getString("jucatorPropus"));
                propuneri.add(propunere);
            }
        } catch (SQLException e) {
            System.err.println("Error DB " + e);
        }
        return propuneri;
    }

}
