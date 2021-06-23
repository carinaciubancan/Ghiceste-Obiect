package joc.persistance.jdbc;

import joc.Jucator;
import joc.persistance.JucatorRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class JucatorDBRepository implements JucatorRepository {

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();


    public JucatorDBRepository(Properties props){
        logger.info("Initializing ParticipantRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }


    @Override
    public Jucator findUserByUsernameParola(String usernameDat, String parolaDat) {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();

        System.out.println(conn);

       Jucator jucator =null;


        try (PreparedStatement preparedStatement = conn.prepareStatement("select * from \"Jucatori\" where id=? and parola=?")) {
            preparedStatement.setString(1,usernameDat);
            preparedStatement.setString(2,parolaDat);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {


                    String parola = resultSet.getString("parola");
                    String id = resultSet.getString("id");
                    jucator =new Jucator(parola);
                    jucator.setId(id);

                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        logger.traceExit(jucator);
        //conn.close();
        return jucator;
    }

    @Override
    public void add(Jucator elem) {

    }

    @Override
    public Jucator save(Jucator elem) {
        return null;
    }

    @Override
    public List<Jucator> findAll() {
        return null;
    }

    @Override
    public void update(Jucator elem) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public Jucator findById(Integer integer) {
        return null;
    }
}
