package domain.risks;

import controller.messenger.MessageService;
import domain.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iwan on 13.09.2014
 */

@Component
public class RiskAnalyzer {

    @Autowired
    private MessageService messageService;

//    private List<Risk> riskList;

    @Resource(name = "nightRiskBean")
    private Risk nightRisk;

    @Resource(name = "ipRiskBean")
    private Risk ipRisk;

    public RiskAnalyzer() {
    }

    /*public void add(Risk risk) {
        if (riskList == null)
            riskList = new ArrayList<Risk>();
        riskList.add(risk);
    }*/

    public boolean isSafe(Loan loan) {
//        System.out.println("Running risk analyzer containing " + riskList.size() + " risk templates...");
        /*for (Risk risk : riskList) {
            if (risk.isHigh(loan)) {
                messageService.setMessage(risk.getMessage());
                return false;
            }
        }*/
        if (riskIsHigh(loan, nightRisk)) return false;
        if (riskIsHigh(loan, ipRisk)) return false;
        return true;
    }

    private boolean riskIsHigh(Loan loan, Risk risk) {
        if (risk.isHigh(loan)) {
            messageService.setHighRiskMessage(risk);
            return true;
        }
        return false;
    }
}
