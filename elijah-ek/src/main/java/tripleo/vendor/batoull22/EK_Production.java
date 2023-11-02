package tripleo.vendor.batoull22;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tripleo
 */
class EK_Production {

	private final EK_Fact[] ch;
	private final String st;

	@Contract(pure = true)
	public EK_Production(final String aSt) {
		st = aSt;
		ch = getEkFactarray().ch();
	}

	@NotNull
	private EK_Factarray getEkFactarray() { // FIXME 10/18 too complicated
		String st = this.getString();

		List<EK_Fact> ch1 = new ArrayList<>();
		for (char c : st.toCharArray()) {
			ch1.add(new EK_Fact(c));
		}

		EK_Fact[] ch = Arrays.asList(ch1.toArray())
				.toArray(new EK_Fact[0]);
		
		logProgress(3737, "" + Arrays.asList(ch));
		EK_Factarray result = new EK_Factarray(st, ch);
		return result;
	}

	public @NotNull EK_Merge getMerge() {
		return new EK_Merge(ch[0], ch[2], ch[4]);
	}

	public @NotNull EK_Push getPush() {
		final EK_Fact predicating = ch[0];
		final EK_Fact resultant = ch[3];
		return new EK_Push(predicating, resultant);
	}

	public String getString() {
		return st;
	}

	public boolean isMerge() {
		return ch.length >= 5; // example A.B-->C;
	}

	public boolean isPush() {
		return ch.length == 4; // example A-->C;
	}

	@Override
	public String toString() {
		return st;
	}

	private void logProgress(final int aI, final String aList) {
		System.out.println(aI + " " + aList);
	}
}
