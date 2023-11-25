/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 1, 2005 8:16:32 PM
 */
package tripleo.elijah.ci_impl;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.diagnostic.Locatable;
import tripleo.elijah.lang.ExpressionKind;
import tripleo.elijah.lang.types.OS_BuiltinType;
import tripleo.elijah.util.SimplePrintLoggerToRemoveSoon;
import tripleo.elijah.util.UnintendedUseException;
import tripleo.elijah.xlang.LocatableString;

import java.io.File;

public class CiNumericExpression implements CiExpression, Locatable {
	final   int   carrier;
	private @NotNull LocatableString n;

	public CiNumericExpression(final int aCarrier) {
		carrier = aCarrier;
	}

	public CiNumericExpression(final @NotNull LocatableString n) {
		this.n  = n;
		carrier = Integer.parseInt(n.getString());
	}

	@Override
	public CiExpression getLeft() {
		return this;
	}

	@Override
	public void setLeft(final CiExpression iexpression) {
		throw new UnintendedUseException();
	}

	// region kind

	@Override  // CiExpression
	public ExpressionKind getKind() {
		return ExpressionKind.NUMERIC; // TODO
	}

	@Override  // CiExpression
	public void setKind(final ExpressionKind aType) {
		// log and ignore
		SimplePrintLoggerToRemoveSoon.println_err2("Trying to set ExpressionType of NumericExpression to " + aType.toString());
	}

	// endregion

	// region representation

	@Override
	public String repr_() {
		return toString();
	}

	@Override
	public String toString() {
		return String.format("NumericExpression (%d)", carrier);
	}

	@Override
	public boolean is_simple() {
		return true;
	}

	public int getValue() {
		return carrier;
	}

	private @NotNull LocatableString token() {
		return n;
	}

	@Override
	public int getLine() {
//		if (token() != null)
//			return token().getLine();
		return 0;
	}

	@Override
	public int getColumn() {
//		if (token() != null)
//			return token().getColumn();
		return 0;
	}

	@Override
	public int getLineEnd() {
//		if (token() != null)
//			return token().getLine();
		return 0;
	}

	@Override
	public int getColumnEnd() {
//		if (token() != null)
//			return token().getColumn();
		return 0;
	}

	@Override
	public File getFile() {
		if (token() != null) {
//			final String filename = token().getFilename();
//			if (filename != null)
//				return new File(filename);
		}
		return null;
	}

	@Override
	public void setType(final OS_BuiltinType aOSBuiltinType) {
		throw new UnintendedUseException();
	}
}

//
//
//
