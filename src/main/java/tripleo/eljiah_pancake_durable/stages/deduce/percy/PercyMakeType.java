package tripleo.eljiah_pancake_durable.stages.deduce.percy;

import org.jdeferred2.FailCallback;
import tripleo.elijah.Eventual;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.eljiah_pancake_durable.lang.OS_Type;
import tripleo.eljiah_pancake_durable.stages.deduce.ResolveError;

public class PercyMakeType {
	public OS_Type                 resolved;
	private Eventual<Void> _p_resolveError = new Eventual<>();

	public void resolveError(final ResolveError aE) {
		_p_resolveError.fail(aE);
	}
	public void onResolveError(final FailCallback<ResolveError> cb) {
		_p_resolveError.onFail((Diagnostic result) -> cb.onFail((ResolveError) result));
	}
}
