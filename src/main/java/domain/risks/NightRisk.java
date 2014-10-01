package domain.risks;

import domain.Loan;
import domain.time.DateTimeSource;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Iwan on 07.09.2014
 */

@Component
public class NightRisk implements Risk {

    @Autowired
    private DateTimeSource dateTimeSource;

    public static String MESSAGE = "Cannot float maximal loan at night.";


    @Override
    public boolean isHigh(Loan loan) {
        return isNight() && (loan.getAmount() >= Loan.MAX_LOAN);
    }

    private boolean isNight() {
        DateTime now = dateTimeSource.getCurrentTime();
        int hours = now.getHourOfDay();
        return hours < 8;
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}