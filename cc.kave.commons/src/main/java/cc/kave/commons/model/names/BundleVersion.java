package cc.kave.commons.model.names;

public interface BundleVersion extends Name {
	int getMajor();

	int getMinor();

	int getBuild();

	int getRevision();
}
