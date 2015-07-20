package cc.kave.histories.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="ContextHistories")
@IdClass(SnapshotKey.class)
public class SSTSnapshot {

	@Id
	@Column(name="WorkPeriod")
	private String workPeriod;
	
	@Id
	@Column(name="Timestamp", columnDefinition="DATETIME")
	private Date timestamp;
	
	@Column(name="TargetType")
	private String targetType;
	
	@Column(name="Context", columnDefinition="TEXT")
	private String context;
}
