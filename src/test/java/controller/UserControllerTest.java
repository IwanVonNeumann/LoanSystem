package controller;

import config.TestSpringConfiguration;
import controller.messenger.MessageService;
import dao.HistoryEntryDAO;
import dao.UserDAO;
import domain.User;
import domain.history.HistoryEntry;
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
public class UserControllerTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private HistoryEntryDAO historyEntryDAO;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private UserController userController;

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
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    public void testGetList() throws Exception {

        User user1 = new User("Jack");
        user1.setId(1);

        User user2 = new User("John");
        user2.setId(2);

        when(userDAO.getUserList()).thenReturn(Arrays.asList(user1, user2));

//      http://www.petrikainulainen.net/programming/spring-framework/unit-testing-of-spring-mvc-controllers-rest-api/

        mockMvc.perform(get("/users/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Jack")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("John")));

        verify(userDAO, times(1)).getUserList();
        verifyNoMoreInteractions(userDAO);
    }

    @Test
    public void testSave() throws Exception {

        String name = "John";

        String message = "Test message";

        when(messageService.getMessage()).thenReturn(message);

        MvcResult result =
                mockMvc.perform(get("/users/save")
                        .param("name", name))
//                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
                        .andReturn();

        assertEquals(result.getResponse().getContentAsString(), message);

        verify(userDAO, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userDAO);
    }

    @Test
    public void testGetHistory() throws Exception {

        String text1 = "text1";
        String text2 = "text2";

        HistoryEntry entry1 = new HistoryEntry(text1);
        entry1.setId(1);
        HistoryEntry entry2 = new HistoryEntry(text2);
        entry2.setId(2);

        int id = 1;

        when(historyEntryDAO.getByUserId(anyLong())).thenReturn(Arrays.asList(entry1, entry2));

        mockMvc.perform(get("/users/getHistory")
                .param("userId", String.valueOf(id)))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].action", is(text1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].action", is(text2)));

        verify(historyEntryDAO, times(1)).getByUserId(id);
        verifyNoMoreInteractions(userDAO);
    }

}