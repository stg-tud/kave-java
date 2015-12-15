package commons.model.ssts.transformation.loops;

import org.junit.Before;

import cc.kave.commons.model.ssts.impl.transformation.loops.LoopNormalizationContext;
import cc.kave.commons.model.ssts.impl.transformation.loops.LoopNormalizationVisitor;

public abstract class LoopNormalizationTest {
	protected LoopNormalizationVisitor visitor;
	protected LoopNormalizationContext context;
	
	@Before
	public void setup() {
		visitor = new LoopNormalizationVisitor();
		context = new LoopNormalizationContext();
	}
}
