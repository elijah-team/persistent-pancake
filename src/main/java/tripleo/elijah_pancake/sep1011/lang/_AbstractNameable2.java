package tripleo.elijah_pancake.sep1011.lang;

import java.util.Collections;
import java.util.List;

public class _AbstractNameable2 implements Nameable2 {
	private EN_Name      en_name;
//	private List<ENU_Understanding> us;

	@Override
	public EN_Name getEnName() {
		return en_name;
	}

	@Override
	public void addUnderstanding(final ENU_Understanding u) {
//		if (us == null) us = new ArrayList<>();
		en_name.addUnderstanding(u);
	}

	@Override
	public List<ENU_Understanding> getUnderstandings() {
//		if (us == null) return Collections.EMPTY_LIST;
//		return us;
		if (en_name == null)
			return Collections.EMPTY_LIST;
		return en_name.understandings();
	}

	protected void __nameHook(final EN_Name_Q aENNameQ) {
		en_name = aENNameQ;
	}
}
