package tripleo.elijah_pancake.cribbonacci;

import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.elijah.util.Operation;

public class ParseException extends Throwable {
	private Operation<OS_Module> moduleOperation;

	public ParseException(final String aS, final Operation<OS_Module> aModuleOperation) {
		super(aS);
		moduleOperation = aModuleOperation;
	}
}
