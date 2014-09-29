package controller;

import controller.messenger.MessageService;
import dao.HistoryEntryDAO;
import dao.UserDAO;
import domain.User;
import domain.history.HistoryEntry;
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
    private HistoryEntryDAO historyEntryDAO;

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
        messageService.setUserCreatedMessage(user);

        return messageService.getMessage();
    }

    @RequestMapping("/getHistory")
    public
    @ResponseBody
    List<HistoryEntry> getHistory(@RequestParam(value = "userId", required = true) long userId) {
        return historyEntryDAO.getByUserId(userId);
    }

}