/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import tripleo.elijah.util.UnintendedUseException;

/*
 * Created on 5/19/2019 at 02:09
 */
public class Attached {
	Context _context;

//	public Attached(final Context aContext) {
//		_context = aContext;
//	}

//	public Attached() {
//	}

	public int getCode() {
		return -100000;
	}
	
	public void setCode(final int aCode) {
		throw new UnintendedUseException();
	}
	
	public Context getContext() {
		return _context;
	}
	
	public void setContext(final Context aContext) {
		_context = aContext;
	}

}

//
//
//
