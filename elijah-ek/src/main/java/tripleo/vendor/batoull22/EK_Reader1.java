package tripleo.vendor.batoull22;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author tripleo
 */
final class EK_Reader1 implements EK_Reader {
	private final           EK_ExpertSystem _system;
	private final @Nullable Scanner         input_;

	@Contract(pure = true)
	EK_Reader1(final EK_ExpertSystem aExpertSystem, final InputStream aStream) {
		_system = aExpertSystem;
		if (aStream != null) {
			input_ = new Scanner(Objects.requireNonNull(aStream));
		} else {
			input_ = null;
		}
	}

	@Override
	public void closefile() {
		if (input_ != null) {
			input_.close();
		}
	}

	@Override
	public void print() {
		System.out.println("factlist:" + _system.getListfacts());
		System.out.println("rulelist:" + _system.getListrule());
		System.out.println("goal:" + _system.getGoal());
		System.out.println(" ");
		// System.out.println( c);
		// System.out.println( j);
	}

	@Override
	public void readfile() {
		// Read the line
		if (input_ != null) {
			while (input_.hasNext()) {
				String a = input_.nextLine();

				_system.proof(a);
			}
		}
		// System.out.println("factlist:"+ Listfacts);
		// System.out.println("goal:"+ goal);
		// System.out.println( "rulelist:"+Listrule);
		// System.out.println( " ");
	}
}
