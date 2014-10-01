package domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.Period;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Iwan on 06.09.2014
 */

@Entity
@Table(name="Loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LoanID")
    private long id;

    @Column
    private double amount;

    @Column
    private double interest;

    @Column
    private Date floatedAt;

    @Column
    private Date repaidAt;

    @Column
    private String ipAddress;

    @Column
    private boolean active;


    public static final double MAX_LOAN = 1000d;

    public static final double DEFAULT_INTEREST = 0.5d;

    public static final double EXTENSION_MULTIPLIER = 1.5d;

    public static final Period DEFAULT_EXTENSION_TERM =
            new Period(0, 0, 1, 0, 0, 0, 0, 0); // 1 week


    public Loan() {
        interest = DEFAULT_INTEREST;
        floatedAt = new Date();
        active = true;
    }

    // for Controller
    public Loan(double amount, int days, String ipAddress) {
        this();
        this.amount = amount;
        this.ipAddress = ipAddress;
        setTerm(days);
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public double getInterest() {
        return interest;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Date getFloatedAt() {
        return floatedAt;
    }

    public Date getRepaidAt() {
        return repaidAt;
    }

    public boolean isActive() {
        return active;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setAmount(double amount) {
        this.amount = amount > MAX_LOAN ? MAX_LOAN : amount;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public void setFloatedAt(Date floatedAt) {
        this.floatedAt = floatedAt;
    }

    public void setRepaidAt(Date repaidAt) {
        this.repaidAt = repaidAt;
    }

    public void setTerm(int days) {
        repaidAt = new DateTime().plusDays(days).toDate();
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan)) return false;

        Loan loan = (Loan) o;

        //if (id != loan.id) return false;
        if (active != loan.active) return false;
        if (Double.compare(loan.amount, amount) != 0) return false;
        if (Double.compare(loan.interest, interest) != 0) return false;
        if (!new DateTime(floatedAt).equals(new DateTime(loan.floatedAt))) return false;
        if (ipAddress != null ? !ipAddress.equals(loan.ipAddress) : loan.ipAddress != null) return false;
        if (!new DateTime(repaidAt).equals(new DateTime(loan.repaidAt))) return false;

        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("amount", amount)
                .append("interest", interest)
                .append("floated at", floatedAt)
                .append("repaid at", repaidAt)
                .append("ip address", ipAddress)
                .append("is active", active)
                .toString();
    }


    // business

    public void extend() {
        interest *= EXTENSION_MULTIPLIER;
        repaidAt = new DateTime(repaidAt).plus(DEFAULT_EXTENSION_TERM).toDate();
    }
}