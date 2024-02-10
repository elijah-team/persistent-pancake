package tripleo.elijah_pancake.sep1011.lang;

import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.lang.TypeName;

import java.util.ArrayList;
import java.util.List;

public class EN_Name_Q implements EN_Name {
	private final Qualident text;
	private final TypeName          principal;
	private List<ENU_Understanding> us;

	public EN_Name_Q(final Qualident aS, final TypeName aTypeName) {
		text      = aS;
		principal = aTypeName;
	}

	/**
	 * This doesn't do anything
	 * But what is it supposed to do?
	 */
	@Override
	public OS_Element principal() {
		return null;
	}
	public TypeName principalType() {
		return this.principal;
	}

	@Override
	public List<ENU_Understanding> understandings() {
		return us;
	}

	@Override
	public void addUnderstanding(final ENU_Understanding u) {
		if (us==null) us=new ArrayList<>();
		us.add(u);
	}
}
