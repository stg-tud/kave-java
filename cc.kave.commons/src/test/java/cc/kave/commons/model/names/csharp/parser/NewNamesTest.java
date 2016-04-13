package cc.kave.commons.model.names.csharp.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsTypeName;

public class NewNamesTest {

	@Test
	public void validRegularType(){
		CsTypeName uut = new CsTypeName("n.T,P");
		assertEquals("n.", uut.getNamespace());
	}
	
	@Test
	public void validMethodName(){
		CsMethodName uut = new CsMethodName("[T] [?].M([n.T,P] p)");
		CsTypeName type = new CsTypeName("n.T,P");
		assertEquals(type.getNamespace(), uut.getType().getNamespace());
	}
}
