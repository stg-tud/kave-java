package cc.kave.commons.model.groum.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ActionNodeTest.class, ControlNodeTest.class, GroumTest.class, GroumQueryTest.class,
		NaivSubgraphStrategyTest.class, PattUtilsTest.class, PattExplorerTest.class, GroumMultisetTest.class,
		GroumMultimapTest.class })
public class AllTests {

}
