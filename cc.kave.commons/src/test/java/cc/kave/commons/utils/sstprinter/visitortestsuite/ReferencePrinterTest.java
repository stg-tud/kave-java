package cc.kave.commons.utils.sstprinter.visitortestsuite;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.names.csharp.EventName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

@Ignore
public class ReferencePrinterTest extends SSTPrintingVisitorBaseTest {
	@Test
	public void VariableReference() {
		IVariableReference sst = SSTUtil.variableReference("variable");
		AssertPrint(sst, "variable");
	}

	@Test
	public void EventReference() {
		EventReference sst = new EventReference();
		sst.setEventName(EventName.newEventName("[EventType,P] [DeclaringType,P].E"));
		sst.setReference(SSTUtil.variableReference("o"));

		AssertPrint(sst, "o.E");
	}

	@Test
	public void FieldReference() {
		FieldReference sst = new FieldReference();
		sst.setFieldName(FieldName.newFieldName("[FieldType,P] [DeclaringType,P].F"));
		sst.setReference(SSTUtil.variableReference("o"));

		AssertPrint(sst, "o.F");
	}

	@Test
	public void MethodReference() {
		MethodReference sst = new MethodReference();
		sst.setMethodName(MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));
		sst.setReference(SSTUtil.variableReference("o"));

		AssertPrint(sst, "o.M");
	}

	@Test
	public void PropertyReference() {
		PropertyReference sst = new PropertyReference();
		sst.setPropertyName(PropertyName.newPropertyName("get set [PropertyType,P] [DeclaringType,P].P"));
		sst.setReference(SSTUtil.variableReference("o"));

		AssertPrint(sst, "o.P");
	}

	@Test
	public void UnknownReference() {
		UnknownReference sst = new UnknownReference();
		AssertPrint(sst, "???");
	}
}
