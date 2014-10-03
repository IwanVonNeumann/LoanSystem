package controller;

import config.TestSpringConfiguration;
import controller.messenger.MessageService;

import dao.LoanDAO;
import dao.UserDAO;

import domain.Loan;
import domain.User;
import domain.risks.RiskAnalyzer;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by IRuskevich on 22.09.2014
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestSpringConfiguration.class})
@WebAppConfiguration
public class LoanControllerTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private LoanDAO loanDAO;

    @Mock
    private RiskAnalyzer riskAnalyzerBean;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private LoanController loanController;

    private MockMvc mockMvc;

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
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
    }


    @Test
    public void testGetList() throws Exception {

        Loan loan1 = new Loan();
        loan1.setId(1);
        loan1.setAmount(300);
        loan1.setInterest(0.05);
        loan1.setIpAddress("192.168.1.1");

        Loan loan2 = new Loan();
        loan2.setId(2);
        loan2.setAmount(200);
        loan2.setInterest(0.06);
        loan2.setIpAddress("192.168.1.2");

        when(loanDAO.getLoanList()).thenReturn(Arrays.asList(loan1, loan2));

        mockMvc.perform(get("/loans/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].amount", is(300.0)))
                .andExpect(jsonPath("$[0].interest", is(0.05)))
                .andExpect(jsonPath("$[0].ipAddress", is("192.168.1.1")))
                .andExpect(jsonPath("$[0].active", is(true)))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].amount", is(200.0)))
                .andExpect(jsonPath("$[1].interest", is(0.06)))
                .andExpect(jsonPath("$[1].ipAddress", is("192.168.1.2")))
                .andExpect(jsonPath("$[1].active", is(true)));

        verify(loanDAO, times(1)).getLoanList();
        verifyNoMoreInteractions(loanDAO);
    }

    @Test
    public void additionTest() throws Exception {

        User user = new User("John");

        int id = 1;
        int amount = 200;
        int days = 25;

        String message = "Test message";

        when(riskAnalyzerBean.isSafe(any(Loan.class))).thenReturn(true);
        when(userDAO.getById(id)).thenReturn(user);
        when(messageService.getMessage()).thenReturn(message);

        MvcResult result =
                mockMvc.perform(get("/loans/add")
                        .param("userId", String.valueOf(id))
                        .param("amount", String.valueOf(amount))
                        .param("days", String.valueOf(days)))
//                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
                        .andReturn();

        assertEquals(result.getResponse().getContentAsString(), message);

        verify(userDAO, times(1)).getById(id);
        verify(userDAO, times(1)).save(user);
        verifyNoMoreInteractions(userDAO);

    }

    @Test
    public void additionFailTest() throws Exception{

        User user = new User("John");

        int id = 1;
        int amount = 200;
        int days = 25;

        String message = "Test message";

        when(riskAnalyzerBean.isSafe(any(Loan.class))).thenReturn(false);
        when(userDAO.getById(id)).thenReturn(user);
        when(messageService.getMessage()).thenReturn(message);

        MvcResult result =
                mockMvc.perform(get("/loans/add")
                        .param("userId", String.valueOf(id))
                        .param("amount", String.valueOf(amount))
                        .param("days", String.valueOf(days)))
//                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
                        .andReturn();

        assertEquals(result.getResponse().getContentAsString(), message);

        verify(userDAO, times(0)).getById(id);
        verify(userDAO, times(0)).save(user);
        verifyNoMoreInteractions(userDAO);
    }

    @Test
    public void extensionTest() throws Exception {

        User user = new User("John");
        Loan loan = new Loan(300, 30, "192.168.1.5");

        user.addLoan(loan);

        int userID = 1;
        int loanID = 1;

        String message = "Test message";

        when(userDAO.getById(userID)).thenReturn(user);
        when(loanDAO.getById(loanID)).thenReturn(loan);
        when(messageService.getMessage()).thenReturn(message);

        MvcResult result =
                mockMvc.perform(get("/loans/extend")
                        .param("userId", String.valueOf(userID))
                        .param("loanId", String.valueOf(loanID)))
//                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
                        .andReturn();

        assertEquals(result.getResponse().getContentAsString(), message);

        verify(loanDAO, times(1)).getById(loanID);
        verify(loanDAO, times(1)).save(loan);
        verify(userDAO, times(1)).getById(userID);
        verify(userDAO, times(1)).save(user);
        verifyNoMoreInteractions(loanDAO);
        verifyNoMoreInteractions(userDAO);
    }
}