package cc.kave.commons.utils.exec;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningContext;
import cc.kave.commons.utils.zip.ZipFolder;
import cc.kave.commons.utils.zip.ZipReader;
import cc.kave.commons.utils.zip.ZipUtils;
import cc.kave.commons.utils.zip.ZipWriter;

public class BatchInlining {

	public void run(String input, String output) {
		Set<ZipFolder> zips = ZipUtils.getAllZips(input, input, output);

		for (ZipFolder zip : zips) {
			System.out.println(zip.getName());
			List<ZipReader> readers = zip.openAll();
			List<ZipWriter> writers = zip.createNewArchives();
			int index = 0;
			for (ZipReader reader : readers) {
				int counter = 0;
				for (Context c : reader.getAll(Context.class)) {
					System.out.print("\r" + counter + "      ");
					if (c != null) {
						Context newContext = inline(c);
						writers.get(index).add(newContext);
					}
					counter++;
				}
				System.out.println("");
				writers.get(index).dispose();
				index++;
			}
		}
		System.out.println("Finished");
	}

	private void inline(CompletionEvent orig) {
		orig.Context = inline(orig.Context);
	}

	private Context inline(Context orig) {
		Context inlined = new Context();
		inlined.setTypeShape(orig.getTypeShape());
		inlined.setSST(inline(orig.getSST()));
		return inlined;
	}

	private ISST inline(ISST sst) {
		InliningContext context = new InliningContext();
		sst.accept(context.getStatementVisitor(), context);
		return context.getSST();
	}
}