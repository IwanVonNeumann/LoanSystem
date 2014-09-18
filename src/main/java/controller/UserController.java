package controller;

import controller.messenger.MessageService;
import dao.UserDAO;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private MessageService messageService;

    @RequestMapping("/list")
    public
    @ResponseBody
    List<User> getUserList() {
        return userDAO.getUserList();
    }

    @RequestMapping("/save")
    public
    @ResponseBody
    String saveUser(
            @RequestParam(value = "name", required = true) String name) {

        User user = new User(name);
        userDAO.save(user);
        messageService.setMessage(
                new StringBuilder("User with name ")
                        .append(user.getName())
                        .append(" created successfully.")
                        .toString());
        return messageService.getMessage();
    }

}
