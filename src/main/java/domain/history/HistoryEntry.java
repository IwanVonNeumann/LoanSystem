package domain.history;

import javax.persistence.*;

/**
 * Created by IRuskevich on 25.09.2014
 */

@Entity
@Table(name="History")
public class HistoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EntryID")
    private long id;

    @Column
    private String action;


    public HistoryEntry() {
    }

    public HistoryEntry(String action) {
        this.action = action;
    }


    public long getId() {
        return id;
    }

    public String getAction() {
        return action;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoryEntry)) return false;

        HistoryEntry entry = (HistoryEntry) o;

        if (!action.equals(entry.action)) return false;

        return true;
    }
}