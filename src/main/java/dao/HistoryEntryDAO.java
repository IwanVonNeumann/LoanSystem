package dao;

import domain.history.HistoryEntry;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IRuskevich on 25.09.2014
 */

@Component
public class HistoryEntryDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserDAO userDAO;

    public void save(HistoryEntry entry) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entry);
        session.refresh(entry);
        transaction.commit();
        session.close();
    }

    public List<HistoryEntry> getByUserId(long id) {
        return userDAO.getById(id).getHistory();
    }

}