/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.DeduceLookupUtils;
import tripleo.elijah.stages.gen_c.GenerateC;
import tripleo.elijah.stages.gen_generic.CodeGenerator;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created 10/29/20 4:26 AM
 */
public class GeneratedClass extends GeneratedContainerNC {
	private final OS_Module module;
	private final ClassStatement klass;
	public Map<ConstructorDef, GeneratedFunction> constructors = new HashMap<ConstructorDef, GeneratedFunction>();

	public GeneratedClass(ClassStatement klass, OS_Module module) {
		this.klass = klass;
		this.module = module;
	}

	public void addAccessNotation(AccessNotation an) {
		throw new NotImplementedException();
	}

	public void createCtor0() {
		// TODO implement me
		FunctionDef fd = new FunctionDef(klass, klass.getContext());
		fd.setName(Helpers.string_to_ident("<ctor$0>"));
		Scope3 scope3 = new Scope3(fd);
		fd.scope(scope3);
		for (VarTableEntry varTableEntry : varTable) {
			if (varTableEntry.initialValue != IExpression.UNASSIGNED) {
				IExpression left = varTableEntry.nameToken;
				IExpression right = varTableEntry.initialValue;

				IExpression e = ExpressionBuilder.build(left, ExpressionKind.ASSIGNMENT, right);
				scope3.add(new StatementWrapper(e, fd.getContext(), fd));
			} else {
				if (getPragma("auto_construct")) {
					scope3.add(new ConstructStatement(fd, fd.getContext(), varTableEntry.nameToken, null, null));
				}
			}
		}
	}

	private boolean getPragma(String auto_construct) { // TODO this should be part of Context
		return false;
	}

	public String getName() {
		return klass.getName();
	}

	public void addConstructor(ConstructorDef aConstructorDef, GeneratedFunction aGeneratedFunction) {
		constructors.put(aConstructorDef, aGeneratedFunction);
	}

	public ClassStatement getKlass() {
		return this.klass;
	}

    @Override
    public String identityString() {
        return ""+klass;
    }

    @Override
    public OS_Module module() {
        return module;
    }

	public void resolve_var_table_entries() {
		for (VarTableEntry varTableEntry : varTable) {
			int y=2;
			if (varTableEntry.potentialTypes.size() == 0 && varTableEntry.varType == null) {
				final TypeName tn = varTableEntry.typeName;
				if (tn != null) {
					if (tn instanceof NormalTypeName) {
						final NormalTypeName tn2 = (NormalTypeName) tn;
						LookupResultList lrl = tn.getContext().lookup(tn2.getName());
						OS_Element best = lrl.chooseBest(null);
						if (best != null) {
							if (best instanceof AliasStatement)
								best = DeduceLookupUtils._resolveAlias((AliasStatement) best);
							assert best instanceof ClassStatement;
							varTableEntry.varType = new OS_Type((ClassStatement) best);
						} else {
							// TODO shouldn't this already be calculated?
						}
					}
				} else {
					// must be unknown
				}
			} else {

			}
		}
	}

	@Override
	public OS_Element getElement() {
		return getKlass();
	}

	@Override
	public void generateCode(CodeGenerator aCodeGenerator, GenerateC.GenerateResult aGr) {
		aCodeGenerator.generate_class(this, aGr);
	}
}

//
//
//
