/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.util.Eventual;
import tripleo.elijah.lang.GenericTypeName;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.TypeName;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.deduce.percy.DeduceTypeResolve2;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.SimplePrintLoggerToRemoveSoon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 9/12/20 10:26 PM
 */
public class TypeTableEntry {
	@NotNull
	private final Type                lifetime;
	@Nullable
	private final TableEntryIV tableEntry;
	private       GenType      genType;
	private final IExpression  expression;
	private final int                 index;
	private final List<OnSetAttached> osacbs  = new ArrayList<OnSetAttached>();
	@Nullable
	private OS_Type      attached;
	private @Nullable DeduceTypes2 deduceTypes2;
	private DeduceTypeResolve2 resolver;
	private Eventual<GenType> _p_genTypeSet = new Eventual<>();

	public Type getLifetime() {
		return lifetime;
	}

	public TableEntryIV getTableEntry() {
		return tableEntry;
	}

	public GenType getGenType(final @Nullable DeduceTypes2 aO) {
		if (genType == null) {
			if (deduceTypes2 == null && aO != null) {deduceTypes2=aO;}
			if (deduceTypes2 != null) {
				genType = new GenType(deduceTypes2.resolver());
				_p_genTypeSet.resolve(genType);
			}
		}
		return genType;
	}

	public IExpression getExpression() {
		return expression;
	}

	public List<OnSetAttached> getOsacbs() {
		return osacbs;
	}

	public void setDeduceTypes2(final DeduceTypes2 aDeduceTypes2) {
		deduceTypes2 = aDeduceTypes2;
		provide(aDeduceTypes2);
	}

	public void onGenType(final DoneCallback<? super GenType> aO) {
		_p_genTypeSet.then(aO);
	}

	public void provide(final DeduceTypes2 aDeduceTypes2) {
		deduceTypes2 = aDeduceTypes2;
	}

	public interface OnSetAttached {
		void onSetAttached(TypeTableEntry aTypeTableEntry);
	}

	public TypeTableEntry(final int index,
	                      @NotNull final Type lifetime,
	                      @Nullable final OS_Type aAttached,
	                      final IExpression expression,
	                      @Nullable final TableEntryIV aTableEntryIV) {
		this.index    = index;
		this.lifetime = lifetime;
		if (aAttached == null || (aAttached.getType() == OS_Type.Type.USER && aAttached.getTypeName() == null)) {
			attached = null;
			// do nothing with genType
		} else {
			attached = aAttached;
			_settingAttached(aAttached);
		}
		this.expression = expression;
		this.tableEntry = aTableEntryIV;
//		genType         = new GenType(aResolver);
	}

	private void _settingAttached(@NotNull final OS_Type aAttached) {
		final GenType genType1 = __genType();

		if (genType1 != null) {
			switch (aAttached.getType()) {
				case USER:
					if (genType1.getTypeName() != null) {
						final TypeName typeName = aAttached.getTypeName();
						if (!(typeName instanceof GenericTypeName))
							genType1.setNonGenericTypeName(typeName);
					} else
						genType1.setTypeName(aAttached)/*.getTypeName()*/;
					break;
				case USER_CLASS:
	//			ClassStatement c = attached.getClassOf();
					genType1.setResolved(aAttached)/*attached*/; // c
					break;
				case UNIT_TYPE:
					genType1.setResolved(aAttached);
				case BUILT_IN:
					if (genType1.getTypeName() != null)
						genType1.setResolved(aAttached);
					else
						genType1.setTypeName(aAttached);
					break;
				case FUNCTION:
					assert genType1.getResolved() == null || genType1.getResolved() == aAttached || /*HACK*/ aAttached.getType() == OS_Type.Type.FUNCTION;
					genType1.setResolved(aAttached);
					break;
				case FUNC_EXPR:
					assert genType1.getResolved() == null || genType1.getResolved() == aAttached;// || /*HACK*/ aAttached.getType() == OS_Type.Type.FUNCTION;
					genType1.setResolved(aAttached);
					break;
				default:
	//			throw new NotImplementedException();
					SimplePrintLoggerToRemoveSoon.println_err2("73 " + aAttached);
					break;
			}
		} else {
			NotImplementedException.raise_stop();
		}
	}

	private @Nullable GenType __genType() {
		if (resolver != null && genType == null) {
			genType = new GenType(resolver);
			_p_genTypeSet.resolve(genType);
		} else if (deduceTypes2 != null && genType == null) {
//			assert false;
			genType = new GenType(deduceTypes2.resolver());
			_p_genTypeSet.resolve(genType);
		} else {
			if (genType == null) {
				// FIZME 11/19 resolver is never used anyway. this may be a small
				genType = new GenType(null);
				_p_genTypeSet.resolve(genType);
//				System.err.println("FAIL 135");
			}
//			throw new AssertionError();
		}
		return genType;
	}

	@Override @NotNull
	public String toString() {
		final String attachedString = attached != null ? attached.asString() : "<<null>>";
		return "TypeTableEntry{" +
				"index=" + index +
				", lifetime=" + lifetime +
				", attached=" + attachedString +
				", expression=" + expression +
				'}';
	}

	public int getIndex() {
		return index;
	}

	public void resolve(final GeneratedNode aResolved) {
		if (__genType() != null) {
			genType.setNode(aResolved);
		}
	}
	public GeneratedNode resolved() {
		if (__genType() != null) {
			return genType.getNode();
		}
		return null;
	}
	public boolean isResolved() {
		if (__genType() != null) {
			return genType.getNode() != null;
		}
		return false;
	}
	public OS_Type getAttached() {
		return attached;
	}

	public void setAttached(final OS_Type aAttached, DeduceTypeResolve2 resolver) {
		this.resolver = resolver;
		attached = aAttached;
		if (aAttached != null) {
			_settingAttached(aAttached);

			for (final OnSetAttached cb : osacbs) {
				cb.onSetAttached(this);
			}
		}
	}

	public void setAttached(final GenType aGenType) {
		_p_genTypeSet.then(gt -> {
			assert gt == genType;

			genType.copy(aGenType);

			setAttached(genType.getResolved(), resolver);
		});
	}

	public void addSetAttached(final OnSetAttached osa) {
		osacbs.add(osa);
	}

	public void genTypeCI(final ClassInvocation aClsinv) {
		if (__genType() != null) {
			genType.setCi(aClsinv);
		}
	}

	public enum Type {
		SPECIFIED, TRANSIENT
	}

}

//
//
//
