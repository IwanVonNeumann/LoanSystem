package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private List<Loan> loanList;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Loan> getLoanList() {
        return loanList;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setLoanList(List<Loan> loanList) {
        this.loanList = loanList;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(
                this, org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .toString();
    }

    // business

    public void addLoan(Loan loan) {
        if (loanList == null)
            loanList = new ArrayList<Loan>();
        loanList.add(loan);
    }


}