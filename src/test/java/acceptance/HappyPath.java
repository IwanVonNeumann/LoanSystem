package acceptance;

import config.TestSpringConfiguration;
import controller.LoanController;
import controller.UserController;
import controller.messenger.MessageService;
import dao.LoanDAO;
import dao.UserDAO;
import domain.Loan;
import domain.User;
import domain.history.HistoryManager;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Iwan on 28.09.2014
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestSpringConfiguration.class})
@WebAppConfiguration
public class HappyPath {

    @Autowired
    UserDAO userDAO;

    @Autowired
    LoanDAO loanDAO;

    @Autowired
    UserController userController;

    @Autowired
    LoanController loanController;

    @Autowired
    MessageService messageService;

    private MockMvc userMockMvc;
    private MockMvc loanMockMvc;

    private static ApplicationContext applicationContext;

    @BeforeClass
    public static void runContext() {
        applicationContext = SpringApplication.run(TestSpringConfiguration.class);
    }

    @AfterClass
    public static void shutDownContext() {
        SpringApplication.exit(applicationContext);
    }

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        userMockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        loanMockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
    }

    @Test
    public void testHappyPath() throws Exception {

        // create user

        String name = "John";

        MvcResult result =
                userMockMvc.perform(get("/users/save")
                        .param("name", name))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
                        .andReturn();

        int userID = 1;

        User user1 = userDAO.getById(userID);

        assertEquals(result.getResponse().getContentAsString(), messageService.getUserCreatedMessage(user1));

        assertEquals(user1.getName(), name);

        // add loan

        double amount = 300.0;
        int days = 25;

        result =
                loanMockMvc.perform(get("/loans/add")
                        .param("userId", String.valueOf(userID))
                        .param("amount", String.valueOf(amount))
                        .param("days", String.valueOf(days)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
                        .andReturn();

        Loan loan1 = loanDAO.getById(1);

        assertEquals(result.getResponse().getContentAsString(),
                new StringBuffer()
                        .append("Loan floated successfully for client ")
                        .append(name)
                        .append("; Amount: ")
                        .append(amount)
                        .append("; Interest: ")
                        .append(Loan.DEFAULT_INTEREST)
                        .append("; To be repaid at: ")
                        .append(new DateTime(loan1.getRepaidAt()).toDate())
                        .append(".")
                        .toString());

        User user2 = userDAO.getById(userID);

        assertEquals(user2.getLoanList().size(), 1);
        assertEquals(user2.getLoanList().get(0), loan1);

        String messageBeforeExtension = HistoryManager.getSaveMessage(user2, loan1);

        // extend loan

        int loanID = 1;

        result = loanMockMvc.perform(get("/loans/extend")
                .param("userId", String.valueOf(userID))
                .param("loanId", String.valueOf(loanID)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(),
                new StringBuffer()
                        .append("Loan extended successfully; Amount: ")
                        .append(amount)
                        .append("; Interest: ")
                        .append(Loan.DEFAULT_INTEREST * Loan.EXTENSION_MULTIPLIER)
                        .append("; To be repaid at: ")
                        .append(new DateTime(loan1.getRepaidAt()).plus(Loan.DEFAULT_EXTENSION_TERM).toDate())
                        .append(".")
                        .toString());

        User user3 = userDAO.getById(userID);
        Loan loan2 = loanDAO.getById(loanID);


        assertEquals(user3.getLoanList().size(), 1);
        assertEquals(user3.getLoanList().get(0), loan2);

        // view history

        userMockMvc.perform(get("/users/getHistory")
                .param("userId", String.valueOf(userID)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].action", is(messageBeforeExtension)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].action", is(
                        HistoryManager.getExtendMessage(user3, loan2))));
    }
}