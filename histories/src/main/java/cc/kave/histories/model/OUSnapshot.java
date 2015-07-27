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
