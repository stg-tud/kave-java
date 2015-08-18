package cc.kave.commons.utils.exec;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningContext;
import cc.kave.commons.utils.zip.ZipFolder;
import cc.kave.commons.utils.zip.ZipReader;
import cc.kave.commons.utils.zip.ZipUtils;
import cc.kave.commons.utils.zip.ZipWriter;

public class Application {

	public static void main(String[] args) {
		String input = "/Users/jonasschlitzer/Desktop/Context/Github";
		String output = "/Users/jonasschlitzer/Desktop/Context/Output";
		new Application().test(input, output);
	}

	private void test(String input, String output) {
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
						Context newContext = inlineSSTinContext(c);
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

	private Context inlineSSTinContext(Context c) {
		Context newContext = new Context();
		newContext.setTypeShape(c.getTypeShape());
		InliningContext context = new InliningContext();
		ISST sst = c.getSST();
		sst.accept(context.getStatementVisitor(), context);
		newContext.setSST(context.getSST());
		return newContext;
	}

}
