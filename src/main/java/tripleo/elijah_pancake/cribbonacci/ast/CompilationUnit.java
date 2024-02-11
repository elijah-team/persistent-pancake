package tripleo.elijah_pancake.cribbonacci.ast;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.ModuleItem;
import tripleo.elijah.lang.OS_Module;
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
