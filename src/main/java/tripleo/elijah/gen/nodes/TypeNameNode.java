/**
 * 
 */
package tripleo.elijah.gen.nodes;

import org.eclipse.jdt.annotation.NonNull;
import tripleo.elijah.lang.IdentExpression;

/**
 * @author olu
 *
 */
public class TypeNameNode {

	public TypeNameNode(@NonNull IdentExpression return_type) {
		// TODO Auto-generated constructor stub
		genType=return_type.getText(); // TODO wrong prolly
	}

	public String genType;
	
	public String getText() {
		return null;
	}
}
