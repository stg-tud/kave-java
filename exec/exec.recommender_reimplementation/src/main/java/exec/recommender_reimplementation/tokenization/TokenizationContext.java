package exec.recommender_reimplementation.tokenization;

import java.util.Iterator;
import java.util.List;

import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.typeshapes.ITypeShape;

public class TokenizationContext {

	List<String> tokenStream;
	
	boolean useFullyQualifiedTypes;
	
	ITypeShape typeShape;
	
	public TokenizationContext pushToken(String token) {
		tokenStream.add(token);
		return this;
	}

	public TokenizationContext pushType(ITypeName type) {
		if(useFullyQualifiedTypes) {
			pushToken(type.getFullName());
		}
		else {
			pushToken(type.getName());
		}
		
		if(type.hasTypeParameters()) {
			pushTypeParameters(type.getTypeParameters());
		}
		
		return this;
	}

	public TokenizationContext pushTypeParameters(List<ITypeName> typeParameters) {
		for (Iterator<ITypeName> iterator = typeParameters.iterator(); iterator.hasNext();) {
			ITypeName typeParameter= (ITypeName) iterator.next();
			
			if(typeParameter.isUnknown() || typeParameter.getTypeParameterType().isUnknownType()) {
				pushToken(TypeName.UNKNOWN_NAME.getIdentifier());
			}
			else {
				pushType(typeParameter.getTypeParameterType());
			}
			
			if(iterator.hasNext()) {
				pushToken(",");
			}
		}
		
		return this;
	}
	
	public TokenizationContext pushParameters(List<IParameterName> parameters) {
		pushOpeningBracket();
		
		 for (Iterator<IParameterName> iterator = parameters.iterator(); iterator.hasNext();) {
			IParameterName parameter = (IParameterName) iterator.next();
			
            if (parameter.isPassedByReference() && parameter.getValueType().isValueType())
            {
                pushToken("ref");
            }

            if (parameter.isOutput())
            {
                pushToken("out");
            }

            if (parameter.isOptional())
            {
                pushToken("opt");
            }

            if (parameter.isParameterArray())
            {
                pushToken("params");
            }

            if (parameter.isExtensionMethodParameter())
            {
                pushToken("this");
            }

            pushType(parameter.getValueType());
            
            pushToken(parameter.getName());
            
            if(iterator.hasNext()) {
            	pushToken(",");
            }
		}
		 
		pushClosingBracket(); 
		
		return this;
	}
	
	public TokenizationContext pushLeftAngleBracket() {
		return pushToken("<");
	}
	
	public TokenizationContext pushRightAngleBracket() {
		return pushToken(">");
	}
	
	public TokenizationContext pushOpeningBracket() {
		return pushToken("(");
	}
	
	public TokenizationContext pushClosingBracket() {
		return pushToken(")");
	}
	
	public TokenizationContext pushOpeningCurlyBracket() {
		return pushToken("{");
	}

	public TokenizationContext pushClosingCurlyBracket() {
		return pushToken("}");
	}
	
	public TokenizationContext pushSemicolon() {
		return pushToken(";");
	}
	
	public TokenizationContext pushUnknown() {
		return pushToken("???");
	}
}
