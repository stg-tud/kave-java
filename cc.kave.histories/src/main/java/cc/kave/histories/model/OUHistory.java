package cc.kave.histories.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class OUHistory {

    @Id
    @GeneratedValue
    private int id;

    private List<OUSnapshot> snapshots = new ArrayList<>();

    @OneToMany
    public List<OUSnapshot> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<OUSnapshot> snapshots) {
        this.snapshots = snapshots;
    }

    public void addSnapshot(OUSnapshot snapshot) {
        this.snapshots.add(snapshot);
    }

    public int size() {
        return snapshots.size();
    }

    public String getEnclosingMethod() {
        return getStart().getEnclosingMethod();
    }

    public OUSnapshot getStart() {
        return snapshots.get(0);
    }

    public OUSnapshot getEnd() {
        return snapshots.get(snapshots.size() - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OUHistory ouHistory = (OUHistory) o;
        return Objects.equals(snapshots, ouHistory.snapshots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snapshots);
    }
}
