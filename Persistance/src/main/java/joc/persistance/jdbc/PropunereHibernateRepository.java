package joc.persistance.jdbc;

import joc.Propunere;
import joc.persistance.PropunereRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class PropunereHibernateRepository implements PropunereRepository {
    static SessionFactory sessionFactory;
    public PropunereHibernateRepository() {
        sessionFactory=HibernateUtility.getSessionFactory();
        System.out.println("JocHibernateRepository" + sessionFactory);
    }

    @Override
    public void add(Propunere elem) {

    }

    @Override
    public Propunere save(Propunere entity) {
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
    public List<Propunere> findAll() {
        List<Propunere> result = null;
        try(Session session = sessionFactory.openSession()){
            Transaction transaction = null;
            try{
                transaction = session.beginTransaction();
                result = session.createQuery("from Propunere",Propunere.class).list();
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
    public void update(Propunere elem) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public Propunere findById(Integer integer) {
        return null;
    }
}
