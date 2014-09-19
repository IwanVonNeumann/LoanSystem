package risks;

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
    private LoanDAO loanDAO;

    private String message;

    public IPRisk() {
        message = "Maximal loans count from this IP address reached.";
    }

    @Override
    public boolean isHigh(Loan loan) {
        System.out.println("Checking IP risk...");
        System.out.println("LoanDAO is null: " + (loanDAO == null));
        List<Loan> loanList =
                loanDAO.getListForTheLastDay(loan.getIpAddress());
        return countIPs(loanList, loan.getIpAddress()) == MAX_LOANS_FROM_IP;
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