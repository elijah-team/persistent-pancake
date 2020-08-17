/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import java.util.ArrayList;
import java.util.List;

public class TypeNameList {

	List<TypeName> p = new ArrayList<TypeName>();

	public NormalTypeName next() {
		RegularTypeName t = new RegularTypeName();
		p.add(t);
		return t;
	}

	public void add(TypeName tn) {
		p.add(tn);
	}
}

//
//
//
