package dao;

import domain.Loan;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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

    // TODO: делай
    public List<Loan> getByUserId(long id) {
        return userDAO.getById(id).getLoanList();
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