package domain;

import domain.history.HistoryEntry;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UserID")
    private long id;

    @Column
    private String name;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="UserID")
    @Fetch(FetchMode.SELECT)
    private List<Loan> loanList;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="UserID")
    @Fetch(FetchMode.SELECT)
    private List<HistoryEntry> history;

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

    public List<HistoryEntry> getHistory() {
        return history;
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

    public void setHistory(List<HistoryEntry> history) {
        this.history = history;
    }


    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(
                this, org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("loans", loanList.size())
                .append("history", history.size())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        //if (id != user.id) return false;
        if (!name.equals(user.name)) return false;

        return true;
    }


    // business

    public void addLoan(Loan loan) {
        if (loanList == null)
            loanList = new ArrayList<Loan>();
        loanList.add(loan);
    }

    public void addHistoryEntry(HistoryEntry entry) {
        if (history == null)
            history = new ArrayList<HistoryEntry>();
        history.add(entry);
    }

}