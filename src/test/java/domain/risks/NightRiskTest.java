package domain.risks;

import domain.Loan;
import domain.time.DateTimeSource;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import static org.mockito.Mockito.*;

/**
 * Created by Iwan on 20.09.2014
 */

@RunWith(MockitoJUnitRunner.class)
public class NightRiskTest {

    @Mock
    DateTimeSource dateTimeSource;

    @InjectMocks
    NightRisk nightRisk;

    @Test
    public void testNightLoan() {

        when(dateTimeSource.getCurrentTime()).thenReturn(new DateTime(0, 1, 1, 0, 0, 0)); // midnight

        Loan loan = new Loan();
        loan.setAmount(300);

        assertFalse(nightRisk.isHigh(loan));
    }

    @Test
    public void testMaxLoan() {

        when(dateTimeSource.getCurrentTime()).thenReturn(new DateTime(0, 1, 1, 12, 0, 0)); // midday

        Loan loan = new Loan();
        loan.setAmount(loan.MAX_LOAN);

        assertFalse(nightRisk.isHigh(loan));
    }

    @Test
    public void testMaxLoanAtNight() {

        when(dateTimeSource.getCurrentTime()).thenReturn(new DateTime(0, 1, 1, 0, 0, 0)); // midnight

        Loan loan = new Loan();
        loan.setAmount(loan.MAX_LOAN);

        assertTrue(nightRisk.isHigh(loan));

        when(dateTimeSource.getCurrentTime()).thenReturn(new DateTime(0, 1, 1, 7, 59, 59)); // almost day

        assertTrue(nightRisk.isHigh(loan));
    }
}
