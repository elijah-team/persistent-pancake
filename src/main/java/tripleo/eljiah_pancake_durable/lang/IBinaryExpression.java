/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.eljiah_pancake_durable.lang;

public interface IBinaryExpression extends IExpression {

	IExpression getRight();

	void setRight(IExpression iexpression);

	@Deprecated void shift(ExpressionKind aType);

	@Deprecated void set(IBinaryExpression aEx); // TODO what is this for?

}