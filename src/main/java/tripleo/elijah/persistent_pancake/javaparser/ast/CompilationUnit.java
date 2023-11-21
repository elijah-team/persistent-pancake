package tripleo.elijah.persistent_pancake.javaparser.ast;

import tripleo.elijah.lang.OS_Module;

public interface CompilationUnit {
	static CompilationUnit of(OS_Module aModule) {
		return new CompilationUnit() {
			private NodeList<TypeDeclaration<?>> types = new NodeList<>();

			@Override
			public String getFileName() {
				return aModule.getFileName();
			}

			public NodeList<TypeDeclaration<?>> getTypes() {
				return types;
			}
		};
	}

	NodeList<TypeDeclaration<?>> getTypes();

	String getFileName();
}
