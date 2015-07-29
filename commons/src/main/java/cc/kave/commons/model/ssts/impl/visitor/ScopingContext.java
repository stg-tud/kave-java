package cc.kave.commons.model.ssts.impl.visitor;

public class ScopingContext {
	private boolean allFields;
	private boolean returnRef;
	private boolean withBlocks;

	public ScopingContext() {
		this.allFields = false;
		this.returnRef = false;
		this.withBlocks = false;
	}

	public void setAllFields(boolean b) {
		this.allFields = b;
	}

	public boolean isAllFields() {
		return allFields;
	}

	public boolean isReturnRef() {
		return returnRef;
	}

	public void setReturnRef(boolean returnRef) {
		this.returnRef = returnRef;
	}

	public boolean isWithBlocks() {
		return withBlocks;
	}

	public void setWithBlocks(boolean withBlocks) {
		this.withBlocks = withBlocks;
	}

}
