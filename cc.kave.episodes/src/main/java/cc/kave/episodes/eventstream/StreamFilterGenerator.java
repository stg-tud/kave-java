package cc.kave.episodes.eventstream;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;

public class StreamFilterGenerator extends StreamRepoGenerator{

	private int numbGeneratedMethods = 0;

	public void add(Context ctx) {
		ISST sst = ctx.getSST();
		if (isGenerated(sst)) {
			numbGeneratedMethods += sst.getMethods().size();
		} else {
			sst.accept(new EventStreamGenerationVisitor(), ctx.getTypeShape());
		}
	}
	
	public int getNumbGeneratedMethods() {
		return numbGeneratedMethods;
	}
	
	private boolean isGenerated(ISST sst) {
		if (sst.isPartialClass()) {
			String fileName = sst.getPartialClassIdentifier();
			if (fileName.contains(".designer")
					|| fileName.contains(".Designer")) {
				return true;
			}
		}
		return false;
	}
}
