package controller;

import config.TestConfig;
import controller.messenger.MessageService;
import dao.UserDAO;
import domain.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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

import static junit.framework.Assert.assertNull;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by IRuskevich on 22.09.2014
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
public class UserControllerTest {

    @Mock
    UserDAO userDAO;

    @Mock
    MessageService messageService;

    @InjectMocks
    UserController userController;

    private MockMvc mockMvc;

    @BeforeClass
    public static void runContext() {
        SpringApplication.run(TestConfig.class);
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

        mockMvc.perform(get("users/save?&name={name}", "John"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("save", hasProperty("name", is("John"))));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDAO, times(1)).save(userCaptor.capture());
        verifyNoMoreInteractions(userDAO);

        User user = userCaptor.getValue();
        assertNull(user.getId());
        assertThat(user.getName(), is("John"));
    }

}