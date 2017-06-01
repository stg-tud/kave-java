package cc.kave.episodes.eventstream;

import java.util.List;
import java.util.Set;

import org.junit.Before;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class FiltersTest {
	
	private Set<IMethodName> decls;
	private List<Event> stream;
	
	private Filters sut;
	
	@Before
	public void setup() {
		decls = Sets.newHashSet();
		decls.add(Events.newContext(m(1, 1)).getMethod());
		decls.add(Events.newContext(m(1, 2)).getMethod());
		decls.add(Events.newContext(mGenericFree(1, 2)).getMethod());
		decls.add(Events.newContext(m(2, 1)).getMethod());
		decls.add(Events.newContext(m(3, 1)).getMethod());
		decls.add(Events.newContext(m(4, 1)).getMethod());
		
		stream = Lists.newLinkedList();
		
		sut = new Filters();
	}
	
	private IMethodName m(int typeNum, int methodNum) {
		return Names.newMethod(String.format("[R,P] [%s].m%d()", t(typeNum),
				methodNum));
	}
	
	private ITypeName t(int typeNum) {
		return Names.newType(String.format("T%d,P", typeNum));
	}
	
	private IMethodName mGenericFree(int typeNum, int methodNum) {
		return Names.newMethod(String.format("[R,P] [%s].m%d`1[[T]]()",
				tGenericFree(typeNum), methodNum));
	}

	private ITypeName tGenericFree(int typeNum) {
		return Names.newType(String.format("T%d`1[[T]],P", typeNum));
	}
}
