/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.eljiah_pancake_durable.stages.gen_fn;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.lang.*;
import tripleo.eljiah_pancake_durable.lang.AccessNotation;
import tripleo.eljiah_pancake_durable.lang.ConstructStatement;
import tripleo.eljiah_pancake_durable.lang.ExpressionBuilder;
import tripleo.eljiah_pancake_durable.lang.ExpressionKind;
import tripleo.eljiah_pancake_durable.lang.FunctionDef;
import tripleo.eljiah_pancake_durable.lang.IExpression;
import tripleo.eljiah_pancake_durable.lang.NamespaceStatement;
import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.eljiah_pancake_durable.lang.Scope3;
import tripleo.eljiah_pancake_durable.lang.StatementWrapper;
import tripleo.eljiah_pancake_durable.stages.gen_generic.CodeGenerator;
import tripleo.eljiah_pancake_durable.stages.gen_generic.GenerateResult;
import tripleo.eljiah_pancake_durable.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

/**
 * Created 12/22/20 5:39 PM
 */
public class GeneratedNamespace extends GeneratedContainerNC implements GNCoded {
	public GeneratedNamespace(final NamespaceStatement namespace1, final OS_Module module) {
		this.namespaceStatement = namespace1;
		this.module             = module;
	}

    private final OS_Module module;
    private final NamespaceStatement namespaceStatement;

	public void addAccessNotation(final AccessNotation an) {
		throw new NotImplementedException();
	}

	public void createCtor0() {
		// TODO implement me
		final FunctionDef fd = new FunctionDef(namespaceStatement, namespaceStatement.getContext());
		fd.setName(Helpers.string_to_ident("<ctor$0>"));
		final Scope3 scope3 = new Scope3(fd);
		fd.scope(scope3);
		for (final VarTableEntry varTableEntry : varTable) {
			if (varTableEntry.initialValue != IExpression.UNASSIGNED) {
				final IExpression left  = varTableEntry.nameToken;
				final IExpression right = varTableEntry.initialValue;

				final @NotNull IExpression e = ExpressionBuilder.build(left, ExpressionKind.ASSIGNMENT, right);
				scope3.add(new StatementWrapper(e, fd.getContext(), fd));
			} else {
				if (getPragma("auto_construct")) {
					scope3.add(new ConstructStatement(fd, fd.getContext(), varTableEntry.nameToken, null, null));
				}
			}
		}
	}

	private boolean getPragma(final String auto_construct) { // TODO this should be part of Context
		return false;
	}

	public String getName() {
		return namespaceStatement.getName();
	}

	public NamespaceStatement getNamespaceStatement() {
		return this.namespaceStatement;
	}

	@Override
	@Nullable
	public VarTableEntry getVariable(final String aVarName) {
		for (final VarTableEntry varTableEntry : varTable) {
			if (varTableEntry.nameToken.getText().equals(aVarName))
				return varTableEntry;
		}
		return null;
	}

	@Override
	public void generateCode(final CodeGenerator aCodeGenerator, final GenerateResult aGr) {
		aCodeGenerator.generate_namespace(this, aGr);
	}

	@Override
	public OS_Element getElement() {
		return getNamespaceStatement();
	}

	@Override
	public OS_Module module() {
		return module;
	}

	@Override
	public @NotNull String identityString() {
		return "" + namespaceStatement;
	}

	@Override
	public Role getRole() {
		return Role.NAMESPACE;
	}
}

//
//
//
