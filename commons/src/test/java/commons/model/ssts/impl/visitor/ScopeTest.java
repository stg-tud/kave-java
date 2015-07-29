package commons.model.ssts.impl.visitor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.impl.visitor.NameGrabber;

public class ScopeTest extends InliningBaseTest {

	/*
	 * Wenn Blöcke abgearbeitet sind werden sie vollständig aus before entfernt.
	 * 
	 */

	@Test
	public void testScoping() {
		IForLoop nestedLoop = forLoop("j", loopHeader(expr(constant("true"))), declareVar("e"));
		IForLoop loop = forLoop("i", loopHeader(expr(constant("true"))), declareVar("d"), nestedLoop,
				assign(refField("f2"), constant("1")));
		List<IStatement> before = Lists.newArrayList(declareVar("a"), declareVar("b"));
		List<IStatement> after = Lists.newArrayList(loop, declareVar("c"), assign(refField("f"), constant("1")));
		List<IStatement> all = Lists.newArrayList(declareVar("a"), declareVar("b"), loop, declareVar("c"),
				assign(refField("f"), constant("1")));
		NameGrabber grabber = new NameGrabber();
		grabber.grabFields(all);
		assertThat(Sets.newHashSet(ref("f2"), ref("f")), equalTo(grabber.getFields()));
		assertThat(Sets.newHashSet(ref("a"), ref("b"), ref("i"), ref("j"), ref("f"), ref("f2"), ref("d"), ref("c"),
				ref("e")), equalTo(grabber.getNames(before, after)));
	}

	@Test
	public void testScoping_inLoop() {
		IForLoop nestedLoop = forLoop("j", loopHeader(expr(constant("true"))), declareVar("e"));
		IForLoop loop = forLoop("i", loopHeader(expr(constant("true"))), declareVar("d"), nestedLoop,
				assign(refField("f2"), constant("1")));
		List<IStatement> before = Lists.newArrayList(declareVar("a"), declareVar("b"), loop, declareVar("d"));
		List<IStatement> after = Lists.newArrayList(assign(refField("f2"), constant("1")), nestedLoop);
		List<IStatement> all = Lists.newArrayList(declareVar("a"), declareVar("b"), loop, declareVar("c"),
				assign(refField("f"), constant("1")));
		NameGrabber grabber = new NameGrabber();
		grabber.grabFields(all);
		assertThat(Sets.newHashSet(ref("f2"), ref("f")), equalTo(grabber.getFields()));
		assertThat(Sets.newHashSet(ref("a"), ref("b"), ref("i"), ref("j"), ref("f"), ref("f2"), ref("d"), ref("e")),
				equalTo(grabber.getNames(before, after)));
	}

	@Test
	public void testScoping_inNestedLoop() {
		IForLoop nestedLoop = forLoop("j", loopHeader(expr(constant("true"))), declareVar("e"));
		IForLoop loop = forLoop("i", loopHeader(expr(constant("true"))), declareVar("d"), nestedLoop,
				assign(refField("f2"), constant("1")));
		List<IStatement> before = Lists.newArrayList(declareVar("a"), declareVar("b"), declareVar("d"),
				assign(refField("f2"), constant("1")), declareVar("e"), loop, nestedLoop);
		List<IStatement> after = Lists.newArrayList();
		List<IStatement> all = Lists.newArrayList(declareVar("a"), declareVar("b"), loop, declareVar("c"),
				assign(refField("f"), constant("1")));
		NameGrabber grabber = new NameGrabber();
		grabber.grabFields(all);
		assertThat(Sets.newHashSet(ref("f2"), ref("f")), equalTo(grabber.getFields()));
		assertThat(Sets.newHashSet(ref("a"), ref("b"), ref("i"), ref("j"), ref("f"), ref("f2"), ref("d"), ref("e")),
				equalTo(grabber.getNames(before, after)));
	}

	@Test
	public void testScoping_afterNestedLoop() {
		IForLoop nestedLoop = forLoop("j", loopHeader(expr(constant("true"))), declareVar("e"));
		IForLoop loop = forLoop("i", loopHeader(expr(constant("true"))), declareVar("d"), nestedLoop,
				assign(refField("f2"), constant("1")));
		List<IStatement> before = Lists.newArrayList(declareVar("a"), declareVar("b"), declareVar("d"), loop);
		List<IStatement> after = Lists.newArrayList(assign(refField("f2"), constant("1")));
		List<IStatement> all = Lists.newArrayList(declareVar("a"), declareVar("b"), loop, declareVar("c"),
				assign(refField("f"), constant("1")));
		NameGrabber grabber = new NameGrabber();
		grabber.grabFields(all);
		assertThat(Sets.newHashSet(ref("f2"), ref("f")), equalTo(grabber.getFields()));
		assertThat(Sets.newHashSet(ref("a"), ref("b"), ref("i"), ref("f"), ref("f2"), ref("d")),
				equalTo(grabber.getNames(before, after)));
	}

	@Test
	public void testScoping_afterNestedLoops() {
		IForLoop nestedLoop = forLoop("j", loopHeader(expr(constant("true"))), declareVar("e"));
		IForLoop loop = forLoop("i", loopHeader(expr(constant("true"))), declareVar("d"), nestedLoop,
				assign(refField("f2"), constant("1")));
		List<IStatement> before = Lists.newArrayList(declareVar("a"), declareVar("b"), declareVar("c"));
		List<IStatement> after = Lists.newArrayList(assign(refField("f"), constant("1")));
		List<IStatement> all = Lists.newArrayList(declareVar("a"), declareVar("b"), loop, declareVar("c"),
				assign(refField("f"), constant("1")));
		NameGrabber grabber = new NameGrabber();
		grabber.grabFields(all);
		assertThat(Sets.newHashSet(ref("f2"), ref("f")), equalTo(grabber.getFields()));
		assertThat(Sets.newHashSet(ref("a"), ref("b"), ref("f"), ref("f2"), ref("c")),
				equalTo(grabber.getNames(before, after)));
	}

}
