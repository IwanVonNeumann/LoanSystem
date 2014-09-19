package risks;

import domain.Loan;

/**
 * Created by Iwan on 07.09.2014
 */
public interface Risk {

    public boolean isHigh(Loan loan);

    public String getMessage();
}
