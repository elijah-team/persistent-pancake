/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: t; c-basic-offset: 4 -*- */
/*
 *Elijjah compiler,copyright Tripleo<oluoluolu+elijah@gmail.com>
 *
 *The contents of this library are released under the LGPL licence v3,
 *the GNU Lesser General Public License text was downloaded from
 *http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 */
package tripleo.elijah.ci;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.xlang.LocatableString;

public interface LibraryStatementPart {
	void addDirective(@NotNull LocatableString aName, CiExpression aExpression);

	// TODO PossiblyQuotedString + getFileName + filenamepolicy.apply...
	String getDirName();

	CompilerInstructions getInstructions();

	String getName();

	void setDirName(LocatableString dirName);

	void setInstructions(CompilerInstructions instructions);

	void setName(LocatableString i1);
}
