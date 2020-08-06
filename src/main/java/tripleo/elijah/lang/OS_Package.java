/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

/*
 * Created on 5/3/2019 at 21:41
 *
 * $Id$
 *
 */
public class OS_Package implements OS_Element {
	public static OS_Package default_package = new OS_Package(null, 0);
	private OS_Module _module;

	@Override
	public void visitGen(ICodeGen visit) {
		throw new NotImplementedException();
	}

	// TODO packages, elements
	
	public OS_Package(Qualident aName, int aCode) {
		_code = aCode;
		_name = aName;
	}
	
	public OS_Package(Qualident aName, int aCode, OS_Module module) {
		_code = aCode;
		_name = aName;
		_module = module;
	}
	
	int _code;
	Qualident _name;

	@Override
	public OS_Element getParent() {
		return _module;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null; //_a._context;
	}

	
}
