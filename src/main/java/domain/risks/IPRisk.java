package domain.risks;

import dao.LoanDAO;
import domain.Loan;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Iwan on 13.09.2014
 */
public class IPRisk implements Risk {

    public static final int MAX_LOANS_FROM_IP = 3;

    @Autowired
    LoanDAO loanDAO;

    private String message;

    public IPRisk() {
        message = "Maximal loans count from this IP address reached.";
    }

    @Override
    public boolean isHigh(Loan loan) {
        /*List<Loan> loanList = loanDAO.getLoanList(); //TODO: заменить
        return countIPs(loanList, loan.getIpAddress()) == MAX_LOANS_FROM_IP;*/
        return false;
    }

    private int countIPs(List<Loan> loanList, String IPAddress) {
        int c = 0;
        for (Loan loan : loanList) {
            if (loan.getIpAddress().equals(IPAddress)) c++;
            if (c == MAX_LOANS_FROM_IP) break;
        }
        return c;
    }

    @Override
    public String getMessage() {
        return message;
    }
}