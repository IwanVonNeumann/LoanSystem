package controller;

import controller.messenger.MessageService;
import dao.LoanDAO;
import dao.UserDAO;
import domain.Loan;
import domain.User;

import domain.history.HistoryManager;
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

//    /*@Autowired
//    private HistoryManager historyManager;*/

    @Autowired
    private MessageService messageService;

    @RequestMapping("/list")
    public
    @ResponseBody
    List<Loan> getLoanList() {
        return loanDAO.getLoanList();
    }

    @RequestMapping("/add")
    public
    @ResponseBody
    String addLoan(
            HttpServletRequest request,
            @RequestParam(value = "userId", required = true) long userId,
            @RequestParam(value = "amount", required = true) double amount,
            @RequestParam(value = "days", required = true) int days) {

        Loan loan = new Loan(amount, days, request.getRemoteAddr());

        if (riskAnalyzerBean.isSafe(loan)) {
            User user = userDAO.getById(userId);
            user.addLoan(loan);
            HistoryManager.saveFloated(loan, user);
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
            @RequestParam(value = "userId", required = true) long userId,
            @RequestParam(value = "loanId", required = true) long loanId)
    {

        Loan loan = loanDAO.getById(loanId);
        loan.extend();
        loanDAO.save(loan);

        User user = userDAO.getById(userId);
        HistoryManager.saveExtended(loan, user);
        userDAO.save(user);

        messageService.setMessage(
                new StringBuilder("Loan extended successfully; ")
                        .append("Amount: ").append(loan.getAmount()).append("; ")
                        .append("Interest: ").append(loan.getInterest()).append("; ")
                        .append("To be repaid at: ").append(loan.getRepaidAt()).append(".")
                        .toString());
        return messageService.getMessage();
    }
}