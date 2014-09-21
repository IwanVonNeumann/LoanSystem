package domain;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertNull;

/**
 * Created by Iwan on 20.09.2014
 */

public class UserTest {

    @Test
    public void addLoanTest() {
        User user = new User();
        assertNull(user.getLoanList());

        Loan loan = new Loan();
        loan.setAmount(300);
        loan.setInterest(0.05);
        user.addLoan(loan);
        assertEquals(user.getLoanList().size(), 1);
        assertSame(user.getLoanList().get(0), loan);
    }

}
