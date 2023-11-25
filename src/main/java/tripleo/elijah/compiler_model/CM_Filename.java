package tripleo.elijah.compiler_model;

//import tripleo.elijah.comp.CompFactory;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.InputRequest;
import tripleo.elijah.util.Mode;
import tripleo.elijah.util.Operation;

import java.io.File;
import java.io.IOException;

public interface CM_Filename {
	static CM_Filename fromFileToString(File file) {
		return new CM_Filename() {
			@Override
			public String getString() {
				return file.toString();
			}
		};
	}

	static CM_Filename fromParams(String aF) {
		return new CM_Filename() {
			@Override
			public String getString() {
				return aF;
			}
		};
	}

	static CM_Filename of(String aFn) {
		return new CM_Filename() {
			@Override
			public String getString() {
				return aFn;
			}
		};
	}

	static CM_Filename fromInputRequestCanonicalFileToString(InputRequest aInputRequest) {
		var r = fromInputRequestCanonicalFileToStringOperation(aInputRequest);
		if (r.mode() == Mode.SUCCESS) {
			return r.success();
		}
		throw new RuntimeException(r.failure());
	}

	static Operation<CM_Filename> fromInputRequestCanonicalFileToStringOperation(@NotNull InputRequest aInputRequest) {
		var file = aInputRequest.file();
		var f    = aInputRequest.file().toString();

		final String absolutePath1;
		try {
			absolutePath1 = file.getCanonicalFile().toString();
		} catch (IOException aE) {
			return Operation.failure(aE);
		}

		var r = new CM_Filename() {
			@Override
			public String getString() {
				return absolutePath1;
			}
		};

		return Operation.success(r);
	}

	String getString();

	default File fileOf() {
		return new File(getString());
	}

	default String printableString() {
		return getString();
	}
}
