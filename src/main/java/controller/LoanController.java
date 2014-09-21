package controller;

import controller.messenger.MessageService;
import dao.LoanDAO;
import dao.UserDAO;
import domain.Loan;
import domain.User;

import domain.risks.RiskAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Iwan on 13.09.2014
 */

@Controller
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanDAO loanDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RiskAnalyzer riskAnalyzerBean;

    @Autowired
    private MessageService messageService;

    @RequestMapping("/list")
    public
    @ResponseBody
    List<Loan> getLoanList() {
        return loanDAO.getLoanList();
    }

    @RequestMapping("/listForUser")
    public
    @ResponseBody
    List<Loan> getLoanList(@RequestParam(value = "userId", required = true) long userId) {
        return loanDAO.getByUserId(userId);
    }

    @RequestMapping("/add")
    public
    @ResponseBody
    String addLoan(
            HttpServletRequest request,
            @RequestParam(value = "userId", required = true) long userId,
            @RequestParam(value = "amount", required = true) double amount,
            @RequestParam(value = "interest", required = true) double interest) {

        Loan loan = new Loan();
        User user = userDAO.getById(userId);

        loan.setAmount(amount);
        loan.setInterest(interest);
        loan.setIpAddress(request.getRemoteAddr());

        if (riskAnalyzerBean.isSafe(loan)) {
            user.addLoan(loan);
            userDAO.save(user);

            messageService.setMessage(
                    new StringBuilder("Loan floated successfully for client ")
                            .append(user.getName()).append("; ")
                            .append("Amount: ").append(loan.getAmount()).append("; ")
                            .append("Interest: ").append(loan.getInterest()).append("; ")
                            .append("To be repaid at: ").append(loan.getRepaidAt()).append(".")
                            .toString());
        }

        return messageService.getMessage();
    }

    @RequestMapping("/extend")
    public
    @ResponseBody
    String extendLoan(
            @RequestParam(value = "loanId", required = true) long loanId) {

        Loan loan = loanDAO.getById(loanId);
        loan.extend();
        loanDAO.save(loan);

        messageService.setMessage(
                new StringBuilder("Loan extended successfully; ")
                        .append("Amount: ").append(loan.getAmount()).append("; ")
                        .append("Interest: ").append(loan.getInterest()).append("; ")
                        .append("To be repaid at: ").append(loan.getRepaidAt()).append(".")
                        .toString());
        return messageService.getMessage();
    }
}