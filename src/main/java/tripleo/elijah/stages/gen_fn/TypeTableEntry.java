/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.lang.GenericTypeName;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.TypeName;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.deduce.percy.DeduceTypeResolve2;
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

	public Type getLifetime() {
		return lifetime;
	}

	public TableEntryIV getTableEntry() {
		return tableEntry;
	}

	public GenType getGenType() {
		if (genType == null) {
			if (deduceTypes2 != null) {
				genType = new GenType(deduceTypes2.resolver());
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
		switch (aAttached.getType()) {
			case USER:
				if (__genType().getTypeName() != null) {
					final TypeName typeName = aAttached.getTypeName();
					if (!(typeName instanceof GenericTypeName))
						__genType().setNonGenericTypeName(typeName);
				} else
					__genType().setTypeName(aAttached)/*.getTypeName()*/;
				break;
			case USER_CLASS:
//			ClassStatement c = attached.getClassOf();
				__genType().setResolved(aAttached)/*attached*/; // c
				break;
			case UNIT_TYPE:
				__genType().setResolved(aAttached);
			case BUILT_IN:
				if (__genType().getTypeName() != null)
					__genType().setResolved(aAttached);
				else
					__genType().setTypeName(aAttached);
				break;
			case FUNCTION:
				assert __genType().getResolved() == null || __genType().getResolved() == aAttached || /*HACK*/ aAttached.getType() == OS_Type.Type.FUNCTION;
				__genType().setResolved(aAttached);
				break;
			case FUNC_EXPR:
				assert __genType().getResolved() == null || __genType().getResolved() == aAttached;// || /*HACK*/ aAttached.getType() == OS_Type.Type.FUNCTION;
				__genType().setResolved(aAttached);
				break;
			default:
//			throw new NotImplementedException();
				SimplePrintLoggerToRemoveSoon.println_err2("73 " + aAttached);
				break;
		}
	}

	private GenType __genType() {
		assert deduceTypes2 != null;
		genType = new GenType(deduceTypes2.resolver());
		return genType;
	}

	@Override @NotNull
	public String toString() {
		return "TypeTableEntry{" +
				"index=" + index +
				", lifetime=" + lifetime +
				", attached=" + attached +
				", expression=" + expression +
				'}';
	}

	public int getIndex() {
		return index;
	}

	public void resolve(final GeneratedNode aResolved) {
		genType.setNode(aResolved);
	}

	public GeneratedNode resolved() {
		return genType.getNode();
	}

	public boolean isResolved() {
		return genType.getNode() != null;
	}

	public OS_Type getAttached() {
		return attached;
	}

	public void setAttached(final OS_Type aAttached) {
		attached = aAttached;
		if (aAttached != null) {
			_settingAttached(aAttached);

			for (final OnSetAttached cb : osacbs) {
				cb.onSetAttached(this);
			}
		}
	}

	public void setAttached(final GenType aGenType) {
		genType.copy(aGenType);
//		if (aGenType.resolved != null) genType.resolved = aGenType.resolved;
//		if (aGenType.ci != null) genType.ci = aGenType.ci;
//		if (aGenType.node != null) genType.node = aGenType.node;

		setAttached(genType.getResolved());
	}

	public void addSetAttached(final OnSetAttached osa) {
		osacbs.add(osa);
	}

	public void genTypeCI(final ClassInvocation aClsinv) {
		genType.setCi(aClsinv);
	}

	public enum Type {
		SPECIFIED, TRANSIENT
	}

}

//
//
//
