package tripleo.eljiah_pancake_durable.compiler_model;

import java.io.File;
import java.util.Objects;

public interface CM_Filename {
	static CM_Filename fromFileToString(final File file) {
		return new CM_Filename() {
			@Override
			public String getString() {
				return file.toString();
			}
		};
	}

	static CM_Filename fromParams(final String aF) {
		return new CM_Filename() {
			@Override
			public String getString() {
				return aF;
			}
		};
	}

	static CM_Filename of(final String aFn) {
		return new CM_Filename() {
			@Override
			public String getString() {
				return aFn;
			}
		};
	}

//	static CM_Filename fromInputRequestCanonicalFileToString(InputRequest aInputRequest) {
//		var r = fromInputRequestCanonicalFileToStringOperation(aInputRequest);
//		if (r.mode() == Mode.SUCCESS) {
//			return r.success();
//		}
//		throw new RuntimeException(r.failure());
//	}
//
//	static Operation<CM_Filename> fromInputRequestCanonicalFileToStringOperation(CompFactory.InputRequest aInputRequest) {
//		var file = aInputRequest.file();
//		var f = aInputRequest.file().toString();
//		var do_out = aInputRequest.do_out();
//
//		final String absolutePath1;
//		try {
//			absolutePath1 = file.getCanonicalFile().toString();
//		} catch (IOException aE) {
//			return Operation.failure(aE);
//		}
//
//		var r = new CM_Filename() {
//			@Override
//			public String getString() {
//				return absolutePath1;
//			}
//		};
//
//		return Operation.success(r);
//	}

	String getString();

	default File fileOf() {
		return new File(getString());
	}

	default String printableString() {
		return getString();
	}

	default boolean sameString(final String string) {
		return Objects.equals(string, getString());
	}
}
