package tripleo.elijah.ci_impl;

import antlr.Token;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tripleo.elijah.ci.CiExpression;

import tripleo.elijah.lang.DotExpression;
import tripleo.elijah.lang.ExpressionKind;

import tripleo.elijah.lang.types.OS_BuiltinType;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.UnintendedUseException;
import tripleo.elijah.xlang.LocatableString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CiQualident implements  CiExpression {

	/** Look into creating a {@link DotExpression} from here */
	public void append(final CiIdentExpression r1) {
		if (r1.getText().contains("."))
			throw new IllegalArgumentException("trying to create a CiQualident with a dot from a user created Token");
		parts.add(r1);
	}

	public void appendDot(final LocatableString d1) {
//		_syntax.appendDot(d1, parts.size());//parts.add(d1);
	}

	private final List<CiIdentExpression> parts = new ArrayList<CiIdentExpression>();

	@Override
	public String toString() {
		return asSimpleString();
	}

	@NotNull
	public String asSimpleString() {
		return Helpers.String_join(".", Collections2.transform(parts, new Function<CiIdentExpression, String>() {
			@Nullable
			@Override
			public String apply(@Nullable final CiIdentExpression input) {
				assert input != null;
				return input.getText();
			}
		}));
	}

	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.QIDENT;
	}

	@Override
	public void setKind(final ExpressionKind aIncrement) {
		throw new IllegalArgumentException(); // TODO is this right?
	}

	@Override
	public CiExpression getLeft() {
		return this;
	}

	/** Not sure what this should do */
	@Override
	public void setLeft(final CiExpression CiExpression) {
		throw new IllegalArgumentException(); // TODO is this right?
	}

	@Override
	public String repr_() {
		return String.format("CiQualident (%s)", this);
	}

	@Override
	public boolean is_simple() {
		return true;  // TODO is this true?
	}

	public List<CiIdentExpression> parts() {
		return parts;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof CiQualident)) return false;
		final CiQualident CiQualident = (CiQualident) o;
		if (CiQualident.parts.size() != parts.size()) return false;
		for (int i=0; i< parts.size();i++) {
			final CiIdentExpression ppart = CiQualident.parts.get(i);
			final CiIdentExpression part  = parts.get(i);
//			if (!equivalentTokens(ppart.token(), part.token()))
			if (!part.getText().equals(ppart.getText()))
				return false;
//			if (!CiQualident.parts.contains(token))
//				return false;
		}
//		if (Objects.equals(parts, CiQualident.parts))
		return true;//Objects.equals(_type, CiQualident._type);
	}

	private static boolean equivalentTokens(final Token token1, final Token token2) {
		return token2.getText().equals(token1.getText()) &&
		  token2.getLine() == token1.getLine() &&
		  token2.getColumn() == token1.getColumn() &&
		  token2.getType() == token1.getType();
	}

	@Override
	public int hashCode() {
		return Objects.hash(parts);
	}

	@Override
	public void setType(final OS_BuiltinType aOSBuiltinType) {
		throw new UnintendedUseException();
	}
}
