package domain;

import junit.framework.TestCase;
import org.joda.time.DateTime;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Iwan on 20.09.2014
 */

public class LoanTest {

    @Test
    public void extendTest() {

        Loan loan = new Loan();

        loan.setAmount(300);
        double interest = 0.05;
        loan.setInterest(interest);

        DateTime repaidAt = new DateTime(loan.getRepaidAt());

        loan.extend();

        assertEquals(loan.getInterest(), interest * Loan.EXTENSION_MULTIPLIER, 2);
        assertEquals(loan.getRepaidAt(), repaidAt.plus(Loan.DEFAULT_EXTENSION_TERM).toDate());

    }


}