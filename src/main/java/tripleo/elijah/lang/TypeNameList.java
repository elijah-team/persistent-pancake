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
import tripleo.elijah.lang.nextgen.names2.ENU_Understanding;
import tripleo.elijah.lang.nextgen.names2.EN_TypeNameListMember;
import tripleo.elijah.util.Helpers;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

public class TypeNameList {
	public TypeNameList() {
		int y=2;
	}

	final   List<TypeName>          p = new ArrayList<>();
	private List<ENU_Understanding> us;

	public void add(final TypeName tn) {
		p.add(tn);

		tn.getEnName().addUnderstanding(new EN_TypeNameListMember(this));
	}

	public TypeName get(final int index) {
		return p.get(index);
	}

	public int size() {
		return p.size();
	}

	@Override
	public String toString() {
		return Helpers.String_join(", ", Collections2.transform(p, new Function<TypeName, String>() {
			@Nullable
			@Override
			public String apply(@Nullable final TypeName input) {
				assert input != null;
				return input.toString();
			}
		}));
	}

	public void addUnderstanding(final ENU_Understanding aUnderstanding) {
		if (us==null) us = new ArrayList<>();
		us.add(aUnderstanding);
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
