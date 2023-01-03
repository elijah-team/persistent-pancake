/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 8/21/21 10:09 PM
 */
public class Pipeline {
	final   List<PipelineMember> pls = new ArrayList<>();
	private boolean              _runAlready; // TODO remove need for this

	public void add(final PipelineMember aPipelineMember) {
		pls.add(aPipelineMember);
	}


	public void run() throws Exception {
		if (_runAlready) return;

		for (final PipelineMember pl : pls) {
			pl.run();
		}

		_runAlready = true;
	}
}

//
//
//
