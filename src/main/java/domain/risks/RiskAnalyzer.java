package domain.risks;

import controller.messenger.MessageService;
import domain.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iwan on 13.09.2014
 */

@Component
public class RiskAnalyzer {

    @Autowired
    MessageService messageService;

    private List<Risk> riskList;

    public RiskAnalyzer() {
        riskList = new ArrayList<Risk>();
    }

    public void add(Risk risk) {
        riskList.add(risk);
    }

    public boolean isSafe(Loan loan) {
        for (Risk risk : riskList) {
            if (risk.isHigh(loan)) {
                messageService.setMessage(risk.getMessage());
                return false;
            }
        }
        return true;
    }
}
