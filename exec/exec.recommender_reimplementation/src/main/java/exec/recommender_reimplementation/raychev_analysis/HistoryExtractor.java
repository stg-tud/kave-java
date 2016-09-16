/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.recommender_reimplementation.raychev_analysis;

import static exec.recommender_reimplementation.java_printer.PhantomClassGeneratorUtil.getTransformedType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;
import cc.kave.commons.pointsto.evaluation.StatementCounterVisitor;
import exec.recommender_reimplementation.evaluation.AutomaticEvaluation;
import exec.recommender_reimplementation.java_transformation.JavaTransformationVisitor;

public class HistoryExtractor {

	public static int filteredCount = 0;

	public Set<ConcreteHistory> extractHistories(Context context) {
		ISST sst = context.getSST();
		Integer statementCount = sst.accept(new StatementCounterVisitor(), null);
		if (statementCount > AutomaticEvaluation.STATEMENT_LIMIT) {
			filteredCount++;
			return new HashSet<>();
		}

		JavaTransformationVisitor transformationVisitor = new JavaTransformationVisitor(sst);
		sst = transformationVisitor.transform(sst, ISST.class);

		PointsToContext pointsToContext = performPointsToAnalysis(context);
		context.setSST(sst);

		Set<ConcreteHistory> concreteHistorySet = new HashSet<>();

		for (IMethodDeclaration methodDecl : sst.getMethods()) {
			HistoryMap historyMap = new HistoryMap();
			methodDecl.accept(new RaychevAnalysisVisitor(pointsToContext),
					historyMap);

			for (AbstractHistory abstractHistory : historyMap.values()) {
				concreteHistorySet.addAll(abstractHistory.getHistorySet());
			}

		}

		return concreteHistorySet;
	}

	public PointsToContext performPointsToAnalysis(Context context) {
		PointsToAnalysis pointsToAnalysis = new UnificationAnalysis(FieldSensitivity.FULL);
		return pointsToAnalysis.compute(context);
	}

	public String getHistoryAsString(ConcreteHistory concreteHistory) {
		StringBuilder sb = new StringBuilder();
			for (Iterator<Interaction> iterator = concreteHistory.getHistory()
					.iterator(); iterator.hasNext();) {
				Interaction interaction = iterator.next();
				String interactionString = buildInteractionString(interaction);
				sb.append(interactionString);
				if(iterator.hasNext()) sb.append(" ");	
			}
		sb.append("\n");
		return sb.toString();
	}

	public String buildInteractionString(Interaction interaction) {
		IMethodName methodName = interaction.getMethodName();
		int position = interaction.getPosition();
		StringBuilder sb = new StringBuilder();
		sb.append(getDeclaringType(methodName));
		sb.append(".");
		sb.append(getNameForInteractionType(interaction.getInteractionType(),
				methodName));
		sb.append("(");
		sb.append(getParameterString(methodName));
		sb.append(")");
		sb.append(getReturnString(methodName));
		sb.append(":");
		sb.append(getPositionString(methodName, position));

		return sb.toString();
	}

	public String getDeclaringType(IMethodName methodName) {
		return getTransformedType(methodName.getDeclaringType()).getFullName().replace(" ", "");
	}

	private String getPositionString(IMethodName methodName, int position) {
		if (position == Interaction.RETURN)
			return "R";
		int numberOfParameters = methodName.isStatic() ? methodName
				.getParameters().size() : methodName.getParameters().size() + 1;
		return String.format("%1$s/%2$s", position, numberOfParameters);
	}

	private char getReturnString(IMethodName methodName) {
		ITypeName returnType = methodName.getReturnType();
		if (returnType.isVoidType())
			return 'v';
		char firstChar = returnType.getName().charAt(0);
		if (returnType.isPredefined()) {
			ITypeName fullType = returnType.asPredefinedTypeName().getFullType();
			firstChar = fullType.getName().charAt(0);
		}
		if (returnType.isUnknown()) {
			firstChar = 'O';
		}
		return firstChar;
	}

	private String getParameterString(IMethodName methodName) {
		List<IParameterName> parameters = methodName.getParameters();
		StringBuilder sb = new StringBuilder();
		for (IParameterName parameterName : parameters) {
			char firstChar;
			ITypeName valueType = parameterName.getValueType();
			if (valueType.isPredefined()) {
				ITypeName fullType = valueType.asPredefinedTypeName().getFullType();
				firstChar = fullType.getName().charAt(0);
			} else {
				firstChar = parameterName.getValueType().getName().charAt(0);
			}
			if (valueType.isUnknown()) {
				firstChar = 'O';
			}
			sb.append(firstChar);
		}
		return sb.toString();
	}

	private String getNameForInteractionType(
			InteractionType interactionType, IMethodName methodName) {
		String name = "";
		switch (interactionType) {
		case METHOD_CALL:
			name = methodName.getName();
			break;
		case PROPERTY_GET:
			name = "get" + methodName.getName();
			break;
		case PROPERTY_SET:
			name = "set" + methodName.getName();
			break;
		default:
			break;
		}
		return name;
	}

}
