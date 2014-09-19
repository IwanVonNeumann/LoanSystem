package risks;

import controller.messenger.MessageService;
import domain.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Iwan on 13.09.2014
 */

@Component
public class RiskAnalyzer {

    @Autowired
    MessageService messageService;

    @Resource(name = "riskList")
//    @Autowired
    private List<Risk> riskList;

    public RiskAnalyzer() {
    }

    public void add(Risk risk) {
        riskList.add(risk);
    }

    public boolean isSafe(Loan loan) {
//        System.out.println("Running risk analyzer containing " + riskList.size() + " risk templates...");
        for (Risk risk : riskList) {
            if (risk.isHigh(loan)) {
                messageService.setMessage(risk.getMessage());
                return false;
            }
        }
        return true;
    }
}
