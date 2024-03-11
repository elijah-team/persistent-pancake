/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/**
 *
 */
package tripleo.eljiah_pancake_durable.lang;

public enum NamespaceTypes {
	/**
	 * has a name (IDENT)
	 */
	NAMED,
	/**
	 * name is underscore
	 */
	PRIVATE,
	/**
	 * no name specified
	 */
	MODULE,
	/**
	 * named __package__
	 */
	PACKAGE
}

//
//
//