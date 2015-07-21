package cc.kave.histories.model;

import cc.kave.histories.model.OUSnapshot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OUHistory extends ArrayList<OUSnapshot> {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany
    public List<OUSnapshot> getSnapshots() {
        return this;
    }

    public void setSnapshots(List<OUSnapshot> snapshots) {
        this.addAll(snapshots);
    }

    public String getEnclosingMethod() {
        return get(0).getEnclosingMethod();
    }

    public OUSnapshot getStart() {
        return get(0);
    }

    public OUSnapshot getEnd() {
        return get(size() - 1);
    }
}
