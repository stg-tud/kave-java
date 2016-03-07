/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.recommenders.evaluation.distribution.calc;

import static cc.recommenders.io.Logger.append;
import static cc.recommenders.io.Logger.log;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.recommenders.datastructures.Map2D;
import cc.recommenders.datastructures.Map3D;
import cc.recommenders.evaluation.OptionsUtils;
import cc.recommenders.evaluation.OutputUtils;
import cc.recommenders.evaluation.data.Boxplot;
import cc.recommenders.evaluation.data.BoxplotData;
import cc.recommenders.evaluation.io.ProjectFoldedUsageStore;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.names.CoReTypeName;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class F1ForInputSeveralProvider extends F1ForInputProvider {

	public static final List<ICoReTypeName> TYPES = Lists.<ICoReTypeName> newArrayList(
			// 20000 ---------------------------------
			CoReTypeName.get("Lorg/eclipse/swt/widgets/Button"), // 47014
			CoReTypeName.get("Lorg/eclipse/swt/widgets/Composite"), // 26631
			CoReTypeName.get("Lorg/eclipse/swt/widgets/Text"), // 24289
			// 9000 ---------------------------------
			CoReTypeName.get("Lorg/eclipse/swt/widgets/Label"), // 16593
			CoReTypeName.get("Lorg/eclipse/swt/widgets/Display"), // 10585
			CoReTypeName.get("Lorg/eclipse/swt/widgets/Table"), // 10526
			CoReTypeName.get("Lorg/eclipse/swt/widgets/Combo"), // 10127
			// 3000 ---------------------------------
			CoReTypeName.get("Lorg/eclipse/swt/widgets/Control"), // 8649
			CoReTypeName.get("Lorg/eclipse/swt/widgets/Shell"), // 7452
			CoReTypeName.get("Lorg/eclipse/swt/widgets/Tree"), // 4791
			// 1000 ---------------------------------
			CoReTypeName.get("Lorg/eclipse/swt/widgets/Menu"), // ??
			CoReTypeName.get("Lorg/eclipse/swt/widgets/Group"), // ??
			CoReTypeName.get("Lorg/eclipse/swt/widgets/TableColumn"), // ??
			CoReTypeName.get("Lorg/eclipse/swt/widgets/ToolItem"), // ??
			CoReTypeName.get("Lorg/eclipse/swt/widgets/MenuItem"), // ??
			CoReTypeName.get("Lorg/eclipse/swt/widgets/ScrollBar"), // ??
			CoReTypeName.get("Lorg/eclipse/swt/widgets/Canvas"), // ??
			CoReTypeName.get("Lorg/eclipse/swt/widgets/List"), // ??
			CoReTypeName.get("Lorg/eclipse/swt/widgets/TableItem"), // ??
			CoReTypeName.get("Lorg/eclipse/swt/widgets/TreeItem") // ??
			);

	private final Set<Integer> usedSizes = Sets.newTreeSet();
	private final Map3D<String, ICoReTypeName, Integer, BoxplotData> results = Map3D.create();

	@Inject
	public F1ForInputSeveralProvider(ProjectFoldedUsageStore store, OutputUtils output) {
		super(store, output);
	}

	@Override
	protected Map<String, String> getOptions() {
		Map<String, String> options = Maps.newLinkedHashMap();
		options.put("BMN+DEF", OptionsUtils.bmn().c(false).d(true).p(false).useFloat().ignore(false).min(30).get());
		options.put("PBN0+DEF", OptionsUtils.pbn(0).c(false).d(true).p(false).useFloat().ignore(false).min(30).get());
		options.put("PBN25+DEF", OptionsUtils.pbn(25).c(false).d(true).p(false).useFloat().ignore(false).min(30).get());
		options.put("PBN40+DEF", OptionsUtils.pbn(40).c(false).d(true).p(false).useFloat().ignore(false).min(30).get());
		options.put("PBN60+DEF", OptionsUtils.pbn(60).c(false).d(true).p(false).useFloat().ignore(false).min(30).get());
		return options;
	}

	@Override
	protected boolean useType(ICoReTypeName type) {
		return TYPES.contains(type);
	}

	@Override
	protected void addResult2(F1ForInputTask r) {
		usedSizes.add(r.inputSize);
		ICoReTypeName type = CoReTypeName.get(r.typeName);
		BoxplotData bpd = results.getOrAdd(r.app, type, r.inputSize, new BoxplotData());
		bpd.addAll(r.f1s);

		log("f1: %s", BoxplotData.from(r.f1s).getBoxplot());
	}

	@Override
	protected void logResults() {
		for (String app : results.keySet()) {
			append("%%%%%% app: %s %%%%%%\n\n", app);
			logResults(results.get(app), "all", TYPES);
			logResults(results.get(app), "20000", TYPES.subList(0, 3)); // 3
			logResults(results.get(app), "9000", TYPES.subList(3, 7)); // 4
			logResults(results.get(app), "3000", TYPES.subList(7, 10)); // 3
			logResults(results.get(app), "1000", TYPES.subList(10, 20)); // 10
		}
	}

	protected void logResults(Map2D<ICoReTypeName, Integer, BoxplotData> results, String filter, List<ICoReTypeName> types) {
		append("%% «num» == %s\n", filter);
		append("input");
		for (ICoReTypeName type : types) {
			if (results.containsKey(type)) {
				append("\t%s", type.getClassName());
			}
		}
		append("\n");

		for (int size : usedSizes) {
			append("%d", size);
			for (ICoReTypeName type : types) {
				if (results.containsKey(type)) {
					BoxplotData bpd = results.getOrAdd(type, size, BoxplotData.from(new double[] { 0.0 }));
					Boxplot bp = bpd.getBoxplot();
					append("\t%.5f", bp.getMean());
				}
			}
			append("\n");
		}
		append("\n");
	}

	@Override
	protected String getFileHint() {
		return "plots/data/f1-for-input-several-«num».txt";
	}
}