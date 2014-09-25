package dao;

import config.TestConfig;
import domain.User;
import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * Created by IRuskevich on 23.09.2014
 */

//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class UserDAOTest {

    @Autowired
    UserDAO userDAO;

    @BeforeClass
    public static void runContext() {
        SpringApplication.run(TestConfig.class);
    }

    @Test
    public void testInsertGet() {

        User user1 = new User("Jack");
        user1.setId(1);

        User user2 = new User("John");
        user2.setId(2);

        userDAO.save(user1);
        userDAO.save(user2);


        List<User> userList = userDAO.getUserList();

        for (User user : userList) {
            System.out.println(user);
        }


    }
}
