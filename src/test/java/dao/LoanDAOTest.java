package dao;

import config.TestSpringConfiguration;
import domain.Loan;
import domain.User;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Iwan on 28.09.2014
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestSpringConfiguration.class})
public class LoanDAOTest {

    @Autowired
    LoanDAO loanDAO;

    @Autowired
    UserDAO userDAO;

    private static ApplicationContext applicationContext;

    @BeforeClass
    public static void runContext() {
        applicationContext = SpringApplication.run(TestSpringConfiguration.class);
    }

    @AfterClass
    public static void shutDownContext() {
        SpringApplication.exit(applicationContext);
    }

    @Test
    public void createGetUpdateTest() {

        User user1 = new User("Jack");
        User user2 = new User("John");

        String ip1 = "192.168.1.3";
        String ip2 = "192.168.1.6";

        Loan loan1 = new Loan(300, 20, ip1);
        Loan loan2 = new Loan(200, 15, ip2);
        Loan loan3 = new Loan(400, 30, ip1);

        user1.addLoan(loan1);
        user1.addLoan(loan2);
        user2.addLoan(loan3);

        userDAO.save(user1);
        userDAO.save(user2);

        // getList

        List<Loan> loanList1 = loanDAO.getLoanList();

        assertEquals(loanList1.size(), 3);
        assertEquals(loanList1.get(0), loan1);
        assertEquals(loanList1.get(1), loan2);
        assertEquals(loanList1.get(2), loan3);

        // getById

        assertEquals(loanDAO.getById(1), loan1);
        assertEquals(loanDAO.getById(2), loan2);
        assertEquals(loanDAO.getById(3), loan3);

        // update

        loan1.setAmount(150);
        loanDAO.save(loan1);

        assertEquals(loanDAO.getById(1), loan1);

        // getByUserId

        List<Loan> loanList2 = loanDAO.getByUserId(1);

        assertEquals(loanList2.size(), 2);
        assertEquals(loanList2.get(0), loan1);
        assertEquals(loanList2.get(1), loan2);

        // getList for IP

        List<Loan> loanList3 = loanDAO.getListForTheLastDay(ip1);

        assertEquals(loanList3.size(), 2);
        assertEquals(loanList3.get(0), loan1);
        assertEquals(loanList3.get(1), loan3);

        Date earlier = new DateTime(loan3.getFloatedAt()).minusDays(1).toDate();

        loan3.setFloatedAt(earlier);

        loanDAO.save(loan3);

        List<Loan> loanList4 = loanDAO.getListForTheLastDay(ip1);

        assertEquals(loanList4.size(), 1);
        assertEquals(loanList4.get(0), loan1);

    }
}