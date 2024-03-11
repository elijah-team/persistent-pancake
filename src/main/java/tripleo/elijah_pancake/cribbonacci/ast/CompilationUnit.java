package tripleo.elijah_pancake.cribbonacci.ast;

import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.ModuleItem;
import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.elijah_pancake.cribbonacci.ast.body.ClassOrInterfaceDeclaration;

/**
 * TODO When you finish misbehaving, call this Module
 */
public interface CompilationUnit {
	static CompilationUnit of(final OS_Module aModule) {
		final CompilationUnit compilationUnit = new CompilationUnit() {
			private final NodeList<TypeDeclaration<?>> types = new NodeList<>();

			@Override
			public String getFileName() {
				return aModule.getFileName();
			}

			@Override
			public void ___addType(final ClassOrInterfaceDeclaration aDeclaration) {
				types.___add(aDeclaration);
			}

			public NodeList<TypeDeclaration<?>> getTypes() {
				return types;
			}
		};
		for (final ModuleItem item : aModule.getItems()) {
			if (item instanceof final ClassStatement cs) {
				compilationUnit.___addType(ClassOrInterfaceDeclaration.of(cs));
			}
		}
		return compilationUnit;
	}

	void ___addType(ClassOrInterfaceDeclaration aOf);

	NodeList<TypeDeclaration<?>> getTypes();

	String getFileName();
}
