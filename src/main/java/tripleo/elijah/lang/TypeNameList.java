/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: t; c-basic-offset: 4 -*- */
/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.util.Helpers;
import tripleo.elijah_pancake.sep1011.lang.ENU_Understanding;
import tripleo.elijah_pancake.sep1011.lang.EN_TypeNameListMember;

import java.util.ArrayList;
import java.util.List;

public class TypeNameList {
	public TypeNameList() {
		int y = 2;
	}

	final   List<TypeName>          __internal_TypeNameList = new ArrayList<>();
	private List<ENU_Understanding> us; // monotonic, once, by lazy; reactive guys may or may not be smart abt this

	public void add(final TypeName tn) {
		__internal_TypeNameList.add(tn);

		tn.getEnName().addUnderstanding(new EN_TypeNameListMember(this));
	}

	public TypeName get(final int index) {
		return __internal_TypeNameList.get(index);
	}

	public int size() {
		return __internal_TypeNameList.size();
	}

	@Override
	public String toString() {
		return Helpers.String_join(", ", Collections2.transform(__internal_TypeNameList, new Function<TypeName, String>() {
			@Nullable
			@Override
			public String apply(@Nullable final TypeName input) {
				assert input != null;
				return input.toString();
			}
		}));
	}

	public void addUnderstanding(final ENU_Understanding aUnderstanding) {
		if (us == null) us = new ArrayList<>();
		us.add(aUnderstanding);
	}

	public void addTypeNamesInto(final OS_Module m, final ClassStatement cs) {
		for (TypeName typeName : __internal_TypeNameList) {
			m.addTypeName(typeName, cs);
		}
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
