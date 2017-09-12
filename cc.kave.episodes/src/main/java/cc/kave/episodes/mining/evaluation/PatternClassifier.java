package cc.kave.episodes.mining.evaluation;

public class PatternClassifier {

	private boolean general = false;
	private boolean  multApis = false;
	private boolean partial = false;
	
	public boolean isGeneral() {
		return general;
	}
	
	public void setGeneral(boolean general) {
		this.general = general;
	}
	
	public boolean isMultApis() {
		return multApis;
	}
	
	public void setMultApis(boolean multApis) {
		this.multApis = multApis;
	}
	
	public boolean isPartial() {
		return partial;
	}
	
	public void setPartial(boolean partial) {
		this.partial = partial;
	}
}
