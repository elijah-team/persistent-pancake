package tripleo.elijah.persistent_pancake.javaparser.ast.expr;

public class SimpleName {
	private String s;

	public SimpleName(final String aName) {
		s = aName;
	}

	public String asString() {
		return s;
	}
}
