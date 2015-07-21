package cc.kave.histories.model;

import cc.kave.histories.model.OUSnapshot;

import java.util.ArrayList;

public class OUHistory extends ArrayList<OUSnapshot> {
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
