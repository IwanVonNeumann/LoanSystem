package domain;

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
    private long id;

    @Column
    private double amount;

    public static final double MAX_LOAN = 1000d;

    @Column
    private double interest;

    @Column
    private Date floatedAt;

    @Column
    private Date repaidAt;

    private static final Period DEFAULT_TERM =
            new Period(0, 1, 0, 0, 0, 0, 0, 0); // 1 month

    @Column
    private String ipAddress;

    @Column
    private boolean active;


    public Loan() {
        floatedAt = new Date();
        repaidAt = new DateTime().plus(DEFAULT_TERM).toDate();
        /*System.out.println("New loan:");
        System.out.println("Floated at " + floatedAt);
        System.out.println("With period of " + DEFAULT_TERM);
        System.out.println("To be repaid at " + repaidAt);
        System.out.println();*/
        active = true;
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

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    // business

    public void extend() {
        interest *= 1.5;
        repaidAt = new DateTime(repaidAt).plus(DEFAULT_TERM).toDate();
        /*System.out.println("Extending Loan with ID " + id);
        System.out.println("Interest: " + interest);
        System.out.println("To be repaid at " + repaidAt);*/
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(
                this, org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("amount", amount)
                .append("interest", interest)
//                .append("user", user)
                .toString();
    }
}