package cc.kave.commons.utils.sstprinter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.names.INamespaceName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.model.typeshapes.ITypeShape;

public class SSTPrintingContext {
	/// <summary>
	/// Base indentation level to use while printing SST nodes.
	/// </summary>
	public int indentationLevel;

	/// <summary>
	/// Type shape (supertype information) of the SST. If the SST is a type and
	/// a type shape is
	/// provided, the supertypes will be included :the print result.
	/// </summary>
	public ITypeShape typeShape;

	/// <summary>
	/// Collection of namespaces that have been seen by the context while
	/// processing an SST.
	/// </summary>
	public Iterator<INamespaceName> SeenNamespaces;

	private StringBuilder _sb;
	private Set<INamespaceName> _seenNamespaces;

	public SSTPrintingContext() {
		_sb = new StringBuilder();
		_seenNamespaces = new HashSet<INamespaceName>();
	}

	/// <summary>
	/// appends a String to the context.
	/// </summary>
	/// <param name="text">The String to append.</param>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext text(String text) {
		_sb.append(text);
		return this;
	}

	public int getIndentationLevel() {
		return indentationLevel;
	}

	public void setIndentationLevel(int indentationLevel) {
		indentationLevel = indentationLevel;
	}

	public ITypeShape getTypeShape() {
		return typeShape;
	}

	public void setTypeShape(ITypeShape typeShape) {
		this.typeShape = typeShape;
	}

	public Iterator<INamespaceName> getSeenNamespaces() {
		return _seenNamespaces.iterator();
	}

	public void setSeenNamespaces(Iterator<INamespaceName> seenNamespaces) {
		SeenNamespaces = seenNamespaces;
	}

	/// <summary>
	/// appends a comment to the context. Delimiters must be provided.
	/// </summary>
	/// <param name="commentText">The comment to append.</param>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext comment(String commentText) {
		return text(commentText);
	}

	/// <summary>
	/// appends a line break to the context.
	/// </summary>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext newLine() {
		_sb.append("\n");
		return this;
	}

	/// <summary>
	/// appends a space to the context.
	/// </summary>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext space() {
		_sb.append(" ");
		return this;
	}

	/// <summary>
	/// appends the appropriate amount of indentation to the context according
	/// to the current indentation level. Should
	/// always be used after appending a line break.
	/// </summary>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext indentation() {
		for (int i = 0; i < indentationLevel; i++) {
			_sb.append("    ");
		}
		return this;
	}

	/// <summary>
	/// appends a keyword (e.g. "null", "class", "static") to the context.
	/// </summary>
	/// <param name="keyword">The keyword to append.</param>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext keyword(String keyword) {
		return text(keyword);
	}

	/// <summary>
	/// appends a marker for the current cursor position to the context.
	/// </summary>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext cursorPosition() {
		return text("$");
	}

	/// <summary>
	/// appends a marker for an unknown entity to the context.
	/// </summary>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext unknownMarker() {
		return text("???");
	}

	/// <summary>
	/// appends a left angle bracket ("<![CDATA[<]]>") to the context.
	/// </summary>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext leftAngleBracket() {
		return text("<");
	}

	/// <summary>
	/// appends a right angle bracket ("("<![CDATA[>]]>") to the context.
	/// </summary>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext rightAngleBracket() {
		return text(">");
	}

	/// <summary>
	/// appends a String literal to the context. Quotation marks must not be
	/// provided.
	/// </summary>
	/// <param name="value">The String to append.</param>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext stringLiteral(String value) {
		return text("\"").text(value).text("\"");
	}

	/// <summary>
	/// appends the name (and only the name!) of a type to the context.
	/// </summary>
	/// <param name="typeName">The type name to append.</param>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext typeNameOnly(ITypeName typeName) {
		if (typeName != null)
			return text(typeName.getName());
		return this;
	}

	protected SSTPrintingContext typeParameterShortName(String typeParameterShortName) {
		return text(typeParameterShortName);
	}

	/// <summary>
	/// Formats and appends a type name together with its generic types to the
	/// context.
	/// </summary>
	/// <param name="typeName">The type name to append.</param>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext type(ITypeName typeName) {

		typeNameOnly(typeName);

		if (typeName != null && typeName.hasTypeParameters()) {
			typeParameters(typeName.getTypeParameters());
		}

		return this;
	}

	public SSTPrintingContext typeParameters(List<ITypeName> typeParameters) {
		leftAngleBracket();

		for (ITypeName p : typeParameters) {
			if (p.isUnknownType() || (p.getTypeParameterType() != null && p.getTypeParameterType().isUnknownType())) {
				typeParameterShortName(TypeName.UNKNOWN_NAME.getIdentifier());
			} else {
				type(p.getTypeParameterType());
			}

			if (!p.equals(typeParameters.get(typeParameters.size() - 1))) {
				_sb.append(", ");
			}
		}

		rightAngleBracket();

		return this;
	}

	/// <summary>
	/// Formats and appends a parameter list to the context.
	/// </summary>
	/// <param name="parameters">The list of parameters to append.</param>
	/// <returns>The context after appending.</returns>
	public SSTPrintingContext parameterList(List<IParameterName> list) {
		text("(");

		for (IParameterName parameter : list) {
			if (parameter.isPassedByReference() && parameter.getValueType().isValueType()) {
				keyword("ref").space();
			}

			if (parameter.isOutput()) {
				keyword("out").space();
			}

			if (parameter.isOptional()) {
				keyword("opt").space();
			}

			if (parameter.isParameterArray()) {
				keyword("params").space();
			}

			type(parameter.getValueType()).space().text(parameter.getName());

			if (!parameter.equals(list.get(list.size() - 1))) {
				text(",").space();
			}
		}

		_sb.append(")");

		return this;
	}

	/// <summary>
	/// appends a statement block to the context with correct indentation.
	/// </summary>
	/// <param name="block">The block to append.</param>
	/// <param name="visitor">The visitor to use for printing each
	/// statement.</param>
	/// <param name="withBrackets">If false, opening and closing brackets will
	/// be omitted.</param>
	public SSTPrintingContext statementBlock(List<IStatement> block, ISSTNodeVisitor<SSTPrintingContext, Void> visitor,
			boolean withBrackets) {
		if (block.isEmpty()) {
			if (withBrackets) {
				text(" { }");
			}

			return this;
		}

		if (withBrackets) {
			newLine().indentation().text("{");
		}

		indentationLevel++;

		for (IStatement statement : block) {
			newLine();
			statement.accept(visitor, this);
		}

		indentationLevel--;

		if (withBrackets) {
			newLine().indentation().text("}");
		}

		return this;
	}

	// TODO: rename method and adapt all usages
	public String toString() {
		return _sb.toString();
	}
}
