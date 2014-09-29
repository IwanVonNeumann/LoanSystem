package controller.messenger;

import domain.Loan;
import domain.User;
import domain.risks.Risk;
import org.springframework.stereotype.Component;

/**
 * Created by Iwan on 14.09.2014
 */

@Component
public class MessageService {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setLoanFloatedMessage(User user, Loan loan) {
        message = getLoanFloatedMessage(user, loan);
    }

    public void setLoanExtendedMessage(Loan loan) {
        message = getLoanExtendedMessage(loan);
    }

    public void setUserCreatedMessage(User user) {
        message = getUserCreatedMessage(user);
    }

    public void setHighRiskMessage(Risk risk) {
        message = risk.getMessage();
    }

    private String getLoanFloatedMessage(User user, Loan loan) {

        String result = new StringBuilder()
                .append("Loan floated successfully for client ")
                .append(user.getName()).append("; ")
                .append("Amount: ").append(loan.getAmount()).append("; ")
                .append("Interest: ").append(loan.getInterest()).append("; ")
                .append("To be repaid at: ").append(loan.getRepaidAt()).append(".")
                .toString();

        return result;
    }

    private String getLoanExtendedMessage(Loan loan) {

        String result = new StringBuilder()
                .append("Loan extended successfully; ")
                .append("Amount: ").append(loan.getAmount()).append("; ")
                .append("Interest: ").append(loan.getInterest()).append("; ")
                .append("To be repaid at: ").append(loan.getRepaidAt()).append(".")
                .toString();

        return result;
    }

    private String getUserCreatedMessage(User user) {

        String result = new StringBuilder()
                .append("User with name ")
                .append(user.getName())
                .append(" created successfully.")
                .toString();

        return result;
    }
}
