package tripleo.elijah.comp;

import tripleo.elijah.diagnostic.Diagnostic;

public interface ErrSink {

	void exception(Throwable exception);

	/*@ ensures errorCount() == \old errorCount + 1*/
	void reportError(String s);

	void reportWarning(String s);

	int errorCount();

	void info(String format);

	void reportDiagnostic(Diagnostic diagnostic);

	enum Errors {
		ERROR, WARNING, INFO
	}
}
