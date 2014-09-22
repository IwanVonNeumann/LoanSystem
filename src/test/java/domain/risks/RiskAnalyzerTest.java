package domain.risks;

import controller.messenger.MessageService;
import domain.Loan;
import domain.time.DateTimeSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Iwan on 21.09.2014
 */

@RunWith(MockitoJUnitRunner.class)
public class RiskAnalyzerTest {

    @Mock
    Risk nightRisk;

    @Mock
    Risk ipRisk;

    @Mock
    MessageService messageService;

    @InjectMocks
    RiskAnalyzer riskAnalyzer;

    @Test
    public void testBothRisks() {

        when(nightRisk.isHigh(any(Loan.class))).thenReturn(true);
        when(ipRisk.isHigh(any(Loan.class))).thenReturn(true);

        assertFalse(riskAnalyzer.isSafe(new Loan()));
    }

    @Test
    public void testNightRisk() {

        when(nightRisk.isHigh(any(Loan.class))).thenReturn(true);
        when(ipRisk.isHigh(any(Loan.class))).thenReturn(false);

        assertFalse(riskAnalyzer.isSafe(new Loan()));
    }

    @Test
    public void testIPRisk() {

        when(nightRisk.isHigh(any(Loan.class))).thenReturn(false);
        when(ipRisk.isHigh(any(Loan.class))).thenReturn(true);

        assertFalse(riskAnalyzer.isSafe(new Loan()));
    }

    @Test
    public void testNoneOfRisks() {

        when(nightRisk.isHigh(any(Loan.class))).thenReturn(false);
        when(ipRisk.isHigh(any(Loan.class))).thenReturn(false);

        assertTrue(riskAnalyzer.isSafe(new Loan()));
    }
}
