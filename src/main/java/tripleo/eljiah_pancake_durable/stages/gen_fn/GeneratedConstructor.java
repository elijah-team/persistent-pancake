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
import tripleo.eljiah_pancake_durable.lang.BaseFunctionDef;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.ConstructorDef;
import tripleo.eljiah_pancake_durable.stages.deduce.ClassInvocation;
import tripleo.eljiah_pancake_durable.stages.deduce.FunctionInvocation;
import tripleo.eljiah_pancake_durable.stages.deduce.NamespaceInvocation;
import tripleo.eljiah_pancake_durable.stages.deduce.percy.DeduceTypeResolve2;

/**
 * Created 6/27/21 9:45 AM
 */
public class GeneratedConstructor extends BaseGeneratedFunction {
	public final @Nullable ConstructorDef     cd;
	private final          DeduceTypeResolve2 resolver;

	public GeneratedConstructor(final @Nullable ConstructorDef aConstructorDef, final DeduceTypeResolve2 aResolver) {
		cd = aConstructorDef;
		resolver = aResolver;
	}

	public void setFunctionInvocation(final FunctionInvocation fi) {
		final GenType genType = new GenType(resolver);

		// TODO will fail on namespace constructors; next line too
		if (genType.getCi() instanceof ClassInvocation) {
//			throw new IllegalStateException("34 Needs class invocation");

			final ClassInvocation classInvocation = (ClassInvocation) genType.getCi();

			genType.setCi(classInvocation);
			genType.setResolved(classInvocation.getKlass().getOS_Type());
		} else if (genType.getCi() instanceof NamespaceInvocation) {
			final NamespaceInvocation namespaceInvocation = (NamespaceInvocation) genType.getCi();

			genType.setCi(namespaceInvocation);
			genType.setResolved(namespaceInvocation.getNamespace().getOS_Type());
		}

		genType.setNode(this);

		resolveTypeDeferred(genType);
	}

	//
	// region toString
	//

	@Override
	public String toString() {
		return String.format("<GeneratedConstructor %s>", cd);
	}

	public String name() {
		if (cd == null)
			throw new IllegalArgumentException("null cd");
		return cd.name();
	}

	// endregion

	@Override
	public @NotNull BaseFunctionDef getFD() {
		if (cd == null) throw new IllegalStateException("No function");
		return cd;
	}

	@Override
	public VariableTableEntry getSelf() {
		if (getFD().getParent() instanceof ClassStatement)
			return getVarTableEntry(0);
		else
			return null;
	}

	@Override
	public String identityString() {
		return ""+cd;
	}

}

//
//
//
