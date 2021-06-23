package joc.persistance.jdbc;

import joc.Joc;
import joc.persistance.JocRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class JocHibernateRepository implements JocRepository {
    static SessionFactory sessionFactory;
    public JocHibernateRepository() {
        sessionFactory=HibernateUtility.getSessionFactory();
        System.out.println("JocHibernateRepository" + sessionFactory);
    }
    @Override
    public void add(Joc elem) {

    }

    @Override
    public Joc save(Joc entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
                return entity;
            }catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }

    @Override
    public List<Joc> findAll() {
        List<Joc> result = null;
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = null;
            try{
                transaction = session.beginTransaction();
                result = session.createQuery("from Joc",Joc.class).list();
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
                if(transaction !=null)
                    transaction.rollback();
            }
        }
        return result;
    }

    @Override
    public void update(Joc elem) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public Joc findById(Integer integer) {
        return null;
    }
}
