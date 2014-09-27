package dao;

import domain.User;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;
import java.util.List;

@Component
public class UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(user);
        transaction.commit();
        session.close();
    }

    public List<User> getUserList() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class);
        List<User> list = criteria.list();
        transaction.commit();
        session.close();
        return list;
    }

    public User getById(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        User user = (User) session.get(User.class, id);
        transaction.commit();
        session.close();
        return user;
    }
}