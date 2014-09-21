package domain.risks;

import dao.LoanDAO;
import domain.Loan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * Created by Iwan on 21.09.2014
 */

@RunWith(MockitoJUnitRunner.class)
public class IPRiskTest {

    @Mock
    LoanDAO loanDAO;

    @InjectMocks
    IPRisk ipRisk;

    @Test
    public void test2sameIPs() {

        String ip = "192.168.1.1";

        Loan loan1 = new Loan();
        loan1.setIpAddress(ip);

        Loan loan2 = new Loan();
        loan2.setIpAddress(ip);

        when(loanDAO.getListForTheLastDay(ip)).thenReturn(
                new ArrayList<Loan>(Arrays.asList(loan1, loan2)));

        Loan testLoan = new Loan();
        testLoan.setIpAddress(ip);

        assertFalse(ipRisk.isHigh(testLoan));
    }

}
