package tripleo.elijah_pancake.sep1011.lang;

import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.ClassStatement;

import java.util.ArrayList;
import java.util.List;

public class EN_Name_CS implements EN_Name {
	private       String         text;
	private final ClassStatement principal;
	private       List<ENU_Understanding> us;

	public EN_Name_CS(final ClassStatement aClassStatement) {
//		text      = aS;
		principal = aClassStatement;
	}

	@Override
	public OS_Element principal() {
		return principal;
	}

//	public ClassStatement principalType() {
//		return this.principal;
//	}

	@Override
	public List<ENU_Understanding> understandings() {
		return us;
	}

	@Override
	public void addUnderstanding(final ENU_Understanding u) {
		if (us == null) us = new ArrayList<>();
		us.add(u);
	}

	public void nameTrigger() {
		text = principal.getName();
	}
}
