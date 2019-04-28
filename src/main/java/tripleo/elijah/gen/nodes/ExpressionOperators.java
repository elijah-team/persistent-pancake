/**
 * Created Mar 13, 2019 at 10:34:57 AM
 *
 */
package tripleo.elijah.gen.nodes;

import org.jetbrains.annotations.NotNull;

/**
 * @author SBUSER
 *
 */
public enum ExpressionOperators {
	OP_MINUS, OP_MULT;
	
	@NotNull
	public String getSymbol() {
		String middle1;
		switch (this) {
			case OP_MINUS: middle1 = "-"; break;
			case OP_MULT:  middle1 = "*"; break;
			default: throw new IllegalStateException("no such symbol");//NotImplementedException();
		}
		return middle1;
	}
	
	
}
