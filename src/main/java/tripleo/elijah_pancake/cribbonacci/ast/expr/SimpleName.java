package tripleo.elijah_pancake.cribbonacci.ast.expr;

public class SimpleName {
	private String s;

	public SimpleName(final String aName) {
		s = aName;
	}

	public String asString() {
		return s;
	}
}
