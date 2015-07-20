package cc.kave.histories.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "OUHistories")
@IdClass(SnapshotKey.class)
public class OUSnapshot {

	@Id
	@Column(name = "WorkPeriod")
	private String workPeriod;

	@Id
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
}
