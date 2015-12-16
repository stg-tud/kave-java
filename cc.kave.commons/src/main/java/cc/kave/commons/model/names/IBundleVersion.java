package cc.kave.commons.model.names;

public interface IBundleVersion extends IName {
	int getMajor();

	int getMinor();

	int getBuild();

	int getRevision();
}
