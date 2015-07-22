package cc.kave.histories.model;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "ContextHistories")
public class SSTSnapshot {

	@Id
	@GeneratedValue
	@Column(name = "Id")
	private int id;

	@Column(name = "WorkPeriod")
	private String workPeriod;

	@Column(name = "Timestamp", columnDefinition = "DATETIME")
	private Date timestamp;

	@Column(name = "TargetType")
	private String targetType;

	@Column(name = "Context", columnDefinition = "TEXT")
	private String context;
}
