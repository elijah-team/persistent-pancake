package tripleo.elijah.util;

import java.util.*;

public class DeferredActionList<T> {
	private List<DeferredAction<T>> backing = new ArrayList<>();

	public DeferredAction<T> get(int index) {
		return backing.get(index);
	}

	public void add(DeferredAction<T> item) {
		backing.add(item);
	}

	public int size() {
		return backing.size();
	}
}
