package tripleo.elijah.xlang;

import antlr.Token;
import tripleo.elijah.diagnostic.Locatable;

public interface LocatableString {
	public static LocatableString of(Token aToken) {
		return new LocatableString() {
			@Override
			public Locatable getLocatable() {
				return null;
			}

			@Override
			public String getString() {
				return aToken.getText();
			}
		};
	}

	static LocatableString of(String aS) {
		return new LocatableString() {
			@Override
			public Locatable getLocatable() {
				return null;
			}

			@Override
			public String getString() {
				return aS;
			}
		};
	}

	Locatable getLocatable();

	String getString();
}
