/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: t; c-basic-offset: 4 -*- */
/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.slir;

import tripleo.elijah.lang.OS_Element;

/**
 * Represents a SlirElement with no underlying OS_Element
 *
 * Created 11/6/21 8:42 AM
 */
public class SlirAbstractElement implements SlirElement {
	private final String name;
	private final SlirPos partOfSpeech;

	public SlirAbstractElement(final String aName, final SlirPos aPartOfSpeech) {
		name = aName;
		partOfSpeech = aPartOfSpeech;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public SlirPos partOfSpeech() {
		return partOfSpeech;
	}

	@Override
	public OS_Element element() {
		return null;
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
