package tripleo.vendor.batoull22;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tripleo.elijah.util.Mode;
import tripleo.elijah.util.Operation;

public class Expert__SystemTest {

	@Test
	@Disabled
	public void testOpenfile() {
		final EK_ExpertSystem i = new EK_ExpertSystem();

		final Operation<EK_Reader> ovo2 = i.openfile_2();
		Assertions.assertNotSame(ovo2.mode(), Mode.FAILURE);

		final EK_Reader reader = ovo2.success();

		reader.readfile();
		// reader.print();
		reader.closefile();

		// System.out.println("------------------------");
		boolean f = i.Forwardchaining();
		// System.out.println(" ");
		System.out.println("Result of Forwardchaining: " + f);

		// System.out.println(" ");
		// i.print();

		// System.out.println("------------------------");
		boolean b = i.Backwardchaining();
		System.out.println("Result of Backwardchaining: " + b);
		System.out.println(" ");
	}
}
