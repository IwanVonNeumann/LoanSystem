package dao;

import config.TestConfig;
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
 * Created by Iwan on 28.09.2014
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class HistoryEntryDAOTest {

    @Autowired
    HistoryEntryDAO historyEntryDAO;

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

        HistoryEntry entry1 = new HistoryEntry("entry1");
        HistoryEntry entry2 = new HistoryEntry("entry2");
        HistoryEntry entry3 = new HistoryEntry("entry3");

        user1.addHistoryEntry(entry1);
        user1.addHistoryEntry(entry2);
        user2.addHistoryEntry(entry3);

        userDAO.save(user1);
        userDAO.save(user2);

        // get by UserID

        List<HistoryEntry> entryList1 = historyEntryDAO.getByUserId(1);

        assertEquals(entryList1.size(), 2);
        assertEquals(entryList1.get(0), entry1);
        assertEquals(entryList1.get(1), entry2);

        // update

        HistoryEntry entry4 = historyEntryDAO.getByUserId(2).get(0);

        entry4.setAction("entry4");

        historyEntryDAO.save(entry4);

        assertEquals(historyEntryDAO.getByUserId(2).get(0), entry4);

    }
}
