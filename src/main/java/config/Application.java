package config;

import dao.LoanDAO;
import dao.UserDAO;
import domain.Loan;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Application {

    @Autowired
    UserDAO userDAO;

    @Autowired
    LoanDAO loanDAO;

    public static void main(String[] args) {
        SpringApplication.run(SpringConfiguration.class, args);
    }

//    @PostConstruct
    public void init() {

        System.out.println("PostConstruct method call...");

        User ulan = new User();
        ulan.setName("Ulan");

        userDAO.save(ulan);

        User carl = new User("Carl");
        userDAO.save(carl);

//        System.out.println(ulan);

        List<User> userList = userDAO.getUserList();

        System.out.println("UserList:");

        for (User u : userList) {
            System.out.println(u);
        }

        Loan loan1 = new Loan();
        loan1.setAmount(300);
        loan1.setTerm(15);

        Loan loan2 = new Loan();
        loan2.setAmount(200);
        loan2.setTerm(10);

        Loan loan3 = new Loan();
        loan3.setAmount(400);
        loan3.setTerm(20);

        User user1 = userList.get(0);

        user1.addLoan(loan1);
        user1.addLoan(loan2);

        User user2 = userList.get(1);

        user2.addLoan(loan3);

        userDAO.save(user1);
        userDAO.save(user2);

        List<Loan> loanList = loanDAO.getLoanList();
        System.out.println("Printing loanList:");
        for (Loan loan : loanList) {
            System.out.println(loan);
        }

        System.out.println("Printing whole DB:");
        userList = userDAO.getUserList();
        for (User u : userList) {
            System.out.println(u);
            for (Loan loan : u.getLoanList()) {
                System.out.println(loan);
            }
        }

        System.out.println("Printing list for User with ID 1");
        user1 = userDAO.getById(1);
        for (Loan loan : user1.getLoanList()) {
            System.out.println(loan);
        }

        System.out.println("Printing list for User with ID 2");
        user2 = userDAO.getById(2);
        for (Loan loan : user2.getLoanList()) {
            System.out.println(loan);
        }

    }

}