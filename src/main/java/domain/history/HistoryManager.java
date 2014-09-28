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

    public static void saveFloated(User user, Loan loan) {
        user.addHistoryEntry(new HistoryEntry(getSaveMessage(user, loan)));
    }

    public static void saveExtended(User user, Loan loan) {
        user.addHistoryEntry(new HistoryEntry(getExtendMessage(user, loan)));
    }

    public static String getSaveMessage(User user, Loan loan) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("[").append(
                        new DateTime(loan.getFloatedAt())).append("]")
                .append(" Loan floated for ").append(user.getName()).append(";")
                .append(" Amount: ").append(loan.getAmount()).append(";")
                .append(" Interest: ").append(loan.getInterest()).append(";")
                .append(" Repayment term: ").append(
                        new DateTime(loan.getRepaidAt())).append(".");

        return stringBuilder.toString();
    }

    public static String getExtendMessage(User user, Loan loan) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("[").append(
                        new DateTime(loan.getRepaidAt()).minus(loan.DEFAULT_EXTENSION_TERM)).append("]")
                .append(" Loan with ID ").append(loan.getId())
                .append(" extended for ").append(user.getName()).append(";")
                .append(" Amount: ").append(loan.getAmount()).append(";")
                .append(" Interest: ").append(loan.getInterest()).append(";")
                .append(" Repayment term: ").append(
                        new DateTime(loan.getRepaidAt())).append(".");

        return stringBuilder.toString();
    }
}