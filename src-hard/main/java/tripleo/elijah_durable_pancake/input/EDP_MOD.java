package tripleo.elijah_durable_pancake.input;

import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.elijah_durable_pancake.comp.Compilation0101;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EDP_MOD {
	public final List<OS_Module>        modules = new ArrayList<OS_Module>();
	public final Map<String, OS_Module> fn2m    = new HashMap<String, OS_Module>();
//		private final Compilation            c;

	public EDP_MOD(final Compilation0101 aCompilation) {
//			c = aCompilation;
	}

	public void addModule(final OS_Module module, final String fn) {
		modules.add(module);
		fn2m.put(fn, module);
	}

	public int size() {
		return modules.size();
	}

	public List<OS_Module> modules() {
		return modules;
	}
}
