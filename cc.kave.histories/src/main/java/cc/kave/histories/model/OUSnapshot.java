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

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "OUHistories")
public class OUSnapshot {

    public OUSnapshot() {
    }

    public OUSnapshot(String workPeriod, Date timestamp,
                      String enclosingMethod, String targetType, String objectUsage,
                      boolean isQuery) {
        this.workPeriod = workPeriod;
        this.timestamp = timestamp;
        this.enclosingMethod = enclosingMethod;
        this.targetType = targetType;
        this.objectUsage = objectUsage;
        this.isQuery = isQuery;
    }

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private int id;

    @Column(name = "WorkPeriod")
    private String workPeriod;

    @Column(name = "Timestamp", columnDefinition = "DATETIME")
    private Date timestamp;

    @Column(name = "EnclosingMethod")
    private String enclosingMethod;

    @Column(name = "TargetType")
    private String targetType;

    @Column(name = "ObjectUsage", columnDefinition = "TEXT")
    private String objectUsage;

    @Column(name = "IsQuery", columnDefinition = "BOOLEAN")
    private boolean isQuery;

    public String getWorkPeriod() {
        return workPeriod;
    }

    public String getEnclosingMethod() {
        return enclosingMethod;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getTargetType() {
        return targetType;
    }
}
