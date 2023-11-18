package tripleo.elijah.lang;

import org.jetbrains.annotations.NotNull;

import tripleo.elijah.util.Helpers;

public class LangGlobals {

	public final static IdentExpression emptyConstructorName = Helpers.string_to_ident("<>");

	// TODO override name() ??
	public final static  ConstructorDef defaultVirtualCtor = new ConstructorDef(null, null, null);
	private static final OS_Package     _dp                = new OS_Package(null, 0);

	public static @NotNull OS_Package defaultPackage() {
		return _dp;
	}

	@NotNull
	static RegularTypeName getUnitType() {
		if (unitType == null) {
			unitType = new RegularTypeName();
			unitType.setName(Helpers.string_to_qualident("Unit"));
		}
		return unitType;
	}

	private static RegularTypeName unitType;
}
