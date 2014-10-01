package domain.risks;

import dao.LoanDAO;
import domain.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Iwan on 13.09.2014
 */

@Component
public class IPRisk implements Risk {

    @Autowired
    private LoanDAO loanDAO;


    public static final int MAX_LOANS_FROM_IP = 3;

    public static final String MESSAGE = "Maximal loans count from this IP address reached.";


    @Override
    public boolean isHigh(Loan loan) {
        List<Loan> loanList =
                loanDAO.getListForTheLastDay(loan.getIpAddress());
        return loanList.size() >= MAX_LOANS_FROM_IP;
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}