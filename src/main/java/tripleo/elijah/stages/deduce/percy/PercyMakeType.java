package tripleo.elijah.stages.deduce.percy;

import org.jdeferred2.FailCallback;
import tripleo.elijah.util.Eventual;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.stages.deduce.ResolveError;

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
