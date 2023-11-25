package tripleo.elijah.ci_impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.diagnostic.Locatable;
import tripleo.elijah.lang.Attached;
import tripleo.elijah.lang.ExpressionKind;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.types.OS_BuiltinType;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.SimplePrintLoggerToRemoveSoon;
import tripleo.elijah.util.UnintendedUseException;
import tripleo.elijah.xlang.LocatableString;

import java.io.File;

/**
 * @author Tripleo(sb)
 *
 */
public class CiIdentExpression implements CiExpression, Locatable {
	private final LocatableString text;

	public CiIdentExpression(final LocatableString r1) {
		this.text = r1;
	}

//	public IdentExpression(final LocatableString r1, final Context cur) {
//		this.text = r1;
//		this._a   = new Attached();
//		setContext(cur);
//	}

	public CiIdentExpression(final LocatableString aT, final String aS) {
		this(aT);
	}

	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.IDENT;
	}

	/**
	 * same as getText()
	 */
	@Override
	public String toString() {
		return getText();
	}

	@Override
	public void setKind(final ExpressionKind aIncrement) {
		// log and ignore
		SimplePrintLoggerToRemoveSoon.println_err2("Trying to set ExpressionType of IdentExpression to " + aIncrement.toString());
	}

	@Override
	public CiExpression getLeft() {
		return this;
	}

	@Override
	public void setLeft(final @NotNull CiExpression CiExpression) {
//		if (CiExpression instanceof IdentExpression) {
//			text = ((IdentExpression) CiExpression).text;
//		} else {
//			// NOTE was tripleo.elijah.util.Stupidity.println_err2
		throw new IllegalArgumentException("Trying to set left-side of IdentExpression to " + CiExpression);
//		}
	}

	@Override
	public String repr_() {
		return String.format("CiIdentExpression(%s)", text.getString());
	}

	public String getText() {
		return text.getString();
	}

	@Override
	public boolean is_simple() {
		return true;
	}

//	@Override
//	public void visitGen(final ElElementVisitor visit) {
//		visit.visitIdentExpression(this);
//	}

//	@Override
//	public Context getContext() {
//		return _a.getContext();
//	}

//	public void setContext(final Context cur) {
//		_a.setContext(cur);
//	}

	@Contract("_ -> new")
	public static @NotNull CiIdentExpression forString(final String string) {
		return new CiIdentExpression(LocatableString.of(string));
	}

	public LocatableString LocatableString() {
		return text;
	}

	@Override
	public int getLine() {
		return -1;//LocatableString().getLine();
	}

	@Override
	public int getColumn() {
		return -1;//LocatableString().getColumn();
	}

	@Override
	public int getLineEnd() {
		return -1;//LocatableString().getLine();
	}

	@Override
	public int getColumnEnd() {
		return -1;//LocatableString().getColumn();
	}

	@Override
	public @Nullable File getFile() {
		return null;
	}

//	@Override
//	public File getFile() {
//		final String filename = LocatableString().getFilename();
//		if (filename == null)
//			return null;
//		return new File(filename);
//	}

	@Override
	public void setType(final OS_BuiltinType aOSBuiltinType) {
		throw new UnintendedUseException();
	}
}
