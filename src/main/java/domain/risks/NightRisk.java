package domain.risks;

import domain.Loan;
import org.joda.time.DateTime;

import java.sql.Timestamp;

/**
 * Created by Iwan on 07.09.2014
 */

public class NightRisk implements Risk {

    private String message;

    public NightRisk() {
        message = "Cannot float maximal loan at night.";
    }

    @Override
    public boolean isHigh(Loan loan) {
        return isNight() && (loan.getAmount() >= Loan.MAX_LOAN );
    }

    private boolean isNight() {
        DateTime now = new DateTime();
        int hours = now.getHourOfDay();
        System.out.println("hours: " + hours);
        return hours < 8;
    }

    @Override
    public String getMessage() {
        return message;
    }
}