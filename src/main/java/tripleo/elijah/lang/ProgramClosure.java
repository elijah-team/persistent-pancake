/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

public abstract class ProgramClosure {
	public ClassStatement classStatement(OS_Element aParent) {
		final ClassStatement classStatement = new ClassStatement(aParent);
		return classStatement;
	}
	
//	public ImportStatement importStatement(OS_Element aParent) {
//		final ImportStatement importStatement = new ImportStatement(aParent);
//		return importStatement;
//	}
	
	public NamespaceStatement namespaceStatement(OS_Element aParent) {
		final NamespaceStatement namespaceStatement = new NamespaceStatement(aParent);
		return namespaceStatement;
	}
	
	public AliasStatement aliasStatement(OS_Element aParent) {
		final AliasStatement aliasStatement = new AliasStatement(aParent);
		return aliasStatement;
	}

}

//
//
//