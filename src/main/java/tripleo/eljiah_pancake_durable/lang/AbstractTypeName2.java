/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.eljiah_pancake_durable.lang;

import java.util.Collection;
import java.util.LinkedHashSet;

import tripleo.elijah_pancake.sep1011.lang.EN_Name_Q;
import tripleo.elijah_pancake.sep1011.lang.__TypeName__EnName__Mixin;

/**
 * Created on 5/4/2019 at 12:25
 */
public abstract class AbstractTypeName2 extends __TypeName__EnName__Mixin implements NormalTypeName {
	private final Collection<TypeModifiers> _ltm = new LinkedHashSet<>();
	private       Qualident                 text;
//	private EN_Name_Q en_name;

	@Override
	public boolean isNull() {
		boolean res;
		if (text == null) {
			//		if (text == null) return false;
			res = true;
		} else {
			res    = _ltm.isEmpty();
		}
		return res;
	}

	@Override public boolean getConstant() {
		return _ltm.contains(TypeModifiers.CONST);
	}

	@Override public void setConstant(final boolean aFlag) {
		_ltm.add(TypeModifiers.CONST);
	}

	@Override public boolean getReference() {
		return _ltm.contains(TypeModifiers.REFPAR);
	}
	
	@Override public void setReference(final boolean aFlag) {
		_ltm.add(TypeModifiers.REFPAR);
	}
	
	@Override public boolean getOut() {
		return _ltm.contains(TypeModifiers.OUTPAR);
	}
	
	@Override public void setOut(final boolean aFlag) {
		_ltm.add(TypeModifiers.OUTPAR);
	}
	
	@Override public boolean getIn() {
		return _ltm.contains(TypeModifiers.INPAR);
	}
	
	@Override public void setIn(final boolean aFlag) {
		_ltm.add(TypeModifiers.INPAR);
	}

	@Override public void setNullable() {
		_ltm.add(TypeModifiers.NULLABLE);
	}

	@Override public Collection<TypeModifiers> getModifiers() {
		return _ltm;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof NormalTypeName)) return false;
		final NormalTypeName that = (NormalTypeName) o;
		return getConstant() == that.getConstant() &&
				getReference() == that.getReference() &&
				getOut() == that.getOut() &&
				getIn() == that.getIn() &&
//				type == that.type &&
				getModifiers().containsAll(that.getModifiers()) &&
				getName().equals(that.getName());
	}

	public Qualident getText() {
		return text;
	}

	public void setText(Qualident aText) {
		text = aText;
	}

	protected void _setText(final Qualident aS, final RegularTypeName aRegularTypeName) {
		this.setText(aS);
		__nameHook(new EN_Name_Q(aS, aRegularTypeName));
	}
}

//
//
//
