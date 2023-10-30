package tripleo.elijah.comp;

import java.util.HashMap;
import java.util.Map;

public abstract class __Extensionable {
	private final Map<Object, Object> exts = new HashMap<>();

	public Object getExt(Class<?> aClass) {
		if (exts.containsKey(aClass)) {
			return exts.get(aClass);
		}
		return null;
	}

	public void putExt(Class<?> aClass, Object o) {
		exts.put(aClass, o);
	}
}
