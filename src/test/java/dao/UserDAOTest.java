package dao;

import config.TestConfig;
import domain.Loan;
import domain.User;
import domain.history.HistoryEntry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IRuskevich on 23.09.2014
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class UserDAOTest {

    @Autowired
    UserDAO userDAO;

    private static ApplicationContext applicationContext;

    @BeforeClass
    public static void runContext() {
        applicationContext = SpringApplication.run(TestConfig.class);
    }

    @AfterClass
    public static void shutDownContext() {
        SpringApplication.exit(applicationContext);
    }

    @Test
    public void createGetUpdateTest() {

        User user1 = new User("Jack");
        User user2 = new User("John");

        Loan loan1 = new Loan(300, 20, "192.168.1.2");
        user1.addLoan(loan1);

        HistoryEntry entry = new HistoryEntry("Text");
        user1.addHistoryEntry(entry);

        userDAO.save(user1);
        userDAO.save(user2);

        // save-getList

        List<User> userList = userDAO.getUserList();

        assertEquals(userList.size(), 2);
        assertEquals(userList.get(0), user1);
        assertEquals(userList.get(0).getLoanList().size(), 1);
        assertEquals(userList.get(0).getLoanList().get(0), loan1);
        assertEquals(userList.get(0).getHistory().size(), 1);
        assertEquals(userList.get(0).getHistory().get(0), entry);
        assertEquals(userList.get(1), user2);

        // getById

        assertEquals(userDAO.getById(1), user1);
        assertEquals(userDAO.getById(2), user2);

        // update

        User user3 = userDAO.getById(1);

        user3.setName("Rajesh");
        Loan loan2 = new Loan(200, 20, "192.168.1.3");
        user3.addLoan(loan2);

        userDAO.save(user3);

        userList = userDAO.getUserList();

        assertEquals(userList.size(), 2);
        assertEquals(userList.get(0), user3);
        assertEquals(userList.get(0).getLoanList().size(), 2);
        assertEquals(userList.get(0).getLoanList().get(1), loan2);
    }
}