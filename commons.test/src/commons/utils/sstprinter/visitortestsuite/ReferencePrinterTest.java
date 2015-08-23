package commons.utils.sstprinter.visitortestsuite;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsEventName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsPropertyName;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class ReferencePrinterTest extends SSTPrintingVisitorBaseTest {
	@Test
	public void VariableReference() {
		IVariableReference sst = SSTUtil.variableReference("variable");
		AssertPrint(sst, "variable");
	}

	@Test
	public void EventReference() {
		EventReference sst = new EventReference();
		sst.setEventName(CsEventName.newEventName("[EventType,P] [DeclaringType,P].E"));
		sst.setReference(SSTUtil.variableReference("e"));

		AssertPrint(sst, "e");
	}

	@Test
	public void FieldReference() {
		FieldReference sst = new FieldReference();
		sst.setFieldName(CsFieldName.newFieldName("[FieldType,P] [DeclaringType,P].F"));
		sst.setReference(SSTUtil.variableReference("f"));

		AssertPrint(sst, "f");
	}

	@Test
	public void MethodReference() {
		MethodReference sst = new MethodReference();
		sst.setMethodName(CsMethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));
		sst.setReference(SSTUtil.variableReference("m"));

		AssertPrint(sst, "m");
	}

	@Test
	public void PropertyReference() {
		PropertyReference sst = new PropertyReference();
		sst.setPropertyName(CsPropertyName.newPropertyName("get set [PropertyType,P] [DeclaringType,P].P"));
		sst.setReference(SSTUtil.variableReference("p"));

		AssertPrint(sst, "p");
	}

	@Test
	public void UnknownReference() {
		UnknownReference sst = new UnknownReference();
		AssertPrint(sst, "???");
	}
}
