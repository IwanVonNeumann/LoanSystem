package domain.history;

import domain.Loan;
import domain.User;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

/**
 * Created by IRuskevich on 25.09.2014
 */

@Component
public class HistoryManager {

    public static void saveFloated(Loan loan, User user) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("[").append(loan.getFloatedAt()).append("]")
                .append(" Loan floated for ").append(user.getName()).append(";")
                .append(" Amount: ").append(loan.getAmount()).append(";")
                .append(" Interest: ").append(loan.getInterest()).append(";")
                .append(" Repayment term: ").append(loan.getRepaidAt()).append(".");

        user.addHistoryEntry(new HistoryEntry(stringBuilder.toString()));
    }

    public static void saveExtended(Loan loan, User user) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("[").append(new DateTime(loan.getRepaidAt()).minus(loan.DEFAULT_EXTENSION_TERM)).append("]")
                .append(" Loan with ID ").append(loan.getId())
                .append(" extended for ").append(user.getName()).append(";")
                .append(" Amount: ").append(loan.getAmount()).append(";")
                .append(" Interest: ").append(loan.getInterest()).append(";")
                .append(" Repayment term: ").append(loan.getRepaidAt()).append(".");

        user.addHistoryEntry(new HistoryEntry(stringBuilder.toString()));
    }
}