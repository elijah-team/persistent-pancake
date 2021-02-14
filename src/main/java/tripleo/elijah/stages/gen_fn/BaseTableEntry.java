/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import tripleo.elijah.lang.OS_Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 2/4/21 10:11 PM
 */
public abstract class BaseTableEntry {
	protected Status status = Status.UNCHECKED;
	private final List<StatusListener> statusListenerList = new ArrayList<StatusListener>();

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status newStatus, OS_Element el) {
		status = newStatus;
		for (StatusListener statusListener : statusListenerList) {
			statusListener.onChange(el, newStatus);
		}
	}

	public void addStatusListener(StatusListener sl) {
		statusListenerList.add(sl);
	}

	public enum Status {
		UNKNOWN, UNCHECKED, KNOWN
	}

	public interface StatusListener {
		void onChange(OS_Element el, Status newStatus);
	}
}

//
//
//