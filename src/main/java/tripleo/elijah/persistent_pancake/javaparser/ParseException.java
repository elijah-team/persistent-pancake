package tripleo.elijah.persistent_pancake.javaparser;

import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.util.Operation;

public class ParseException extends Throwable {
	private Operation<OS_Module> moduleOperation;

	public ParseException(final String aS, final Operation<OS_Module> aModuleOperation) {
		super(aS);
		moduleOperation = aModuleOperation;
	}
}
