package controller;

import config.TestConfig;
import controller.messenger.MessageService;
import dao.LoanDAO;
import dao.UserDAO;
import domain.Loan;
import domain.User;
import domain.risks.RiskAnalyzer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.SpringApplication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by IRuskevich on 22.09.2014
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
public class LoanControllerTest {

    @Mock
    UserDAO userDAO;

    @Mock
    LoanDAO loanDAO;

    @Mock
    RiskAnalyzer riskAnalyzerBean;

    @Mock
    MessageService messageService;

    @InjectMocks
    LoanController loanController;

    MockMvc mockMvc;

    @BeforeClass
    public static void runContext() {
        SpringApplication.run(TestConfig.class);
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
}