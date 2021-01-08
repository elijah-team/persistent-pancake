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
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created 12/22/20 5:39 PM
 */
public class GeneratedNamespace implements GeneratedNode {
	public GeneratedNamespace(NamespaceStatement namespace1, OS_Module module) {
		this.namespaceStatement = namespace1;
		this.module = module;
	}

	private final OS_Module module;
	private final NamespaceStatement namespaceStatement;
	public List<VarTableEntry> varTable = new ArrayList<VarTableEntry>();
	public Map<FunctionDef, GeneratedFunction> functionMap = new HashMap<FunctionDef, GeneratedFunction>();

	public void addVarTableEntry(AccessNotation an, VariableStatement vs) {
		// TODO dont ignore AccessNotation
		varTable.add(new VarTableEntry(vs.getNameToken(), vs.initialValue()));
	}

	public void addAccessNotation(AccessNotation an) {
		throw new NotImplementedException();
	}

	public void createCtor0() {
		// TODO implement me
		FunctionDef fd = new FunctionDef(namespaceStatement, namespaceStatement.getContext());
		fd.setName(Helpers.string_to_ident("<ctor$0>"));
		Scope3 scope3 = new Scope3(fd);
		for (VarTableEntry varTableEntry : varTable) {
			if (varTableEntry.initialValue != IExpression.UNASSIGNED) {
				IExpression left = varTableEntry.nameToken;
				IExpression right = varTableEntry.initialValue;

				IExpression e = ExpressionBuilder.build(left, ExpressionKind.ASSIGNMENT, right);
				scope3.add(new StatementWrapper(e, fd.getContext(), fd));
			} else {
				if (getPragma("auto_construct")) {
					scope3.add(new ConstructExpression(fd, fd.getContext(), varTableEntry.nameToken, null));
				}
			}
		}
	}

	private boolean getPragma(String auto_construct) { // TODO this should be part of Context
		return false;
	}

	public String getName() {
		return namespaceStatement.getName();
	}

	public void addFunction(FunctionDef functionDef, GeneratedFunction generatedFunction) {
		if (functionMap.containsKey(functionDef))
			throw new IllegalStateException("Function already generated"); // TODO do better than this
		functionMap.put(functionDef, generatedFunction);
	}

	/**
	 * Get a {@link GeneratedFunction}
	 *
	 * @param fd the function searching for
	 *
	 * @return null if no such key exists
	 */
	public GeneratedFunction getFunction(FunctionDef fd) {
		return functionMap.get(fd);
	}

	public NamespaceStatement getNamespaceStatement() {
		return this.namespaceStatement;
	}

	public class VarTableEntry {
		public final IdentExpression nameToken;
		public final IExpression initialValue;
		public OS_Type varType;

		public VarTableEntry(IdentExpression nameToken, IExpression initialValue) {
			this.nameToken = nameToken;
			this.initialValue = initialValue;
		}
	}

}

//
//
//
