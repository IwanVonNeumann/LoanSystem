package dao;

import domain.Loan;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Iwan on 13.09.2014
 */

@Component
public class LoanDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserDAO userDAO;

    public void save(Loan loan) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(loan);
        transaction.commit();
        session.close();
    }

    public List<Loan> getLoanList() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        //List<Loan> list = session.createQuery("from Loan").list();
        Criteria criteria = session.createCriteria(Loan.class);
        List<Loan> list = criteria.list();
        transaction.commit();
        session.close();
        return list;
    }

    public List<Loan> getByUserId(long id) {
        return userDAO.getById(id).getLoanList();
    }


    public List<Loan> getListForTheLastDay(String IPAddress) {
//        System.out.println("Getting list of loans for IP " + IPAddress + "...");
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Criteria criteria = session.createCriteria(Loan.class);
        criteria.add(Restrictions.eq("ipAddress", IPAddress));
        criteria.add(Restrictions.ge("floatedAt",
                new DateTime().minusDays(1).toDate()));

        List<Loan> list = criteria.list();
        transaction.commit();
        session.close();
        System.out.println("Returning " + list.size() + " list items by IP " + IPAddress);
        return list;
    }

    public Loan getById(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Loan loan = (Loan)session.get(Loan.class, id);
        transaction.commit();
        session.close();
        return loan;
    }
}