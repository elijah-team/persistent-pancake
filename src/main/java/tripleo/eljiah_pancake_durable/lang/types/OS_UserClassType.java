package tripleo.eljiah_pancake_durable.lang.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.eljiah_pancake_durable.comp.ErrSink;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.NormalTypeName;
import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.lang.OS_Type;
import tripleo.eljiah_pancake_durable.lang.TypeName;
import tripleo.eljiah_pancake_durable.stages.deduce.ClassInvocation;
import tripleo.eljiah_pancake_durable.stages.deduce.DeducePhase;
import tripleo.eljiah_pancake_durable.stages.deduce.DeduceTypes2;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenType;

import java.text.MessageFormat;
import java.util.List;

public class OS_UserClassType extends __Abstract_OS_Type {
	private final ClassStatement _classStatement;

	public OS_UserClassType(final ClassStatement aClassStatement) {
		_classStatement = aClassStatement;
	}

	@Override
	public OS_Element getElement() {
		return _classStatement;
	}

	@Override
	public Type getType() {
		return Type.USER_CLASS;
	}

	@NotNull
	public ClassInvocation resolvedUserClass(final @NotNull GenType genType, final TypeName aGenericTypeName, final DeducePhase phase, final DeduceTypes2 deduceTypes2, final ErrSink errSink) {
		final ClassStatement   best            = _classStatement;
		@Nullable final String constructorName = null; // TODO what to do about this, nothing I guess

		@NotNull final List<TypeName> gp = best.getGenericPart();
		@Nullable ClassInvocation     clsinv;
		if (genType.getCi() == null) {
			clsinv = DeduceTypes2.ClassInvocationMake.withGenericPart(best, constructorName, (NormalTypeName) aGenericTypeName, deduceTypes2, errSink);
			if (clsinv == null) return null;
			clsinv     = phase.registerClassInvocation(clsinv);
			genType.setCi(clsinv);
		} else
			clsinv = (ClassInvocation) genType.getCi();
		return clsinv;
	}

	@Override
	public ClassStatement getClassOf() {
		return _classStatement;
	}

	@Override
	public String asString() {
		return MessageFormat.format("<OS_UserClassType {0}>", _classStatement);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("{ User_Class ");
		sb.append(_classStatement);
		sb.append('}');
		return sb.toString();
	}

	protected boolean _isEqual(final OS_Type aType) {
		return aType.getType() == Type.USER_CLASS && _classStatement.equals(((OS_UserClassType) aType)._classStatement);
	}
}
