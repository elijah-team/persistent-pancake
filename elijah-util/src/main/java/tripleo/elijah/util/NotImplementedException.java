/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.util;

@SuppressWarnings("unused")
public class NotImplementedException extends RuntimeException {
	public static void raise() {
		Stupidity.println_err_2("Should not be here");
	}

	public static void raise_stop() {
		int y = 2;
	}

	public NotImplementedException() {
		Stupidity.println_err_2("Should not be here 2");
	}

	public NotImplementedException(final String message) {
		super(message);
		Stupidity.println_err_2("Should not be here 2");
	}
}
