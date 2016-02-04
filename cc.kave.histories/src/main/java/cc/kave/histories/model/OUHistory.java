/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
