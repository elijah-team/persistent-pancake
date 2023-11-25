/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.ci_impl;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.ci.CiIndexingStatement;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.ci.GenerateStatement;
import tripleo.elijah.ci.LibraryStatementPart;
import tripleo.elijah.comp.CompilerInput;
import tripleo.elijah.compiler_model.CM_Filename;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.xlang.LocatableString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created 9/6/20 11:20 AM
 */
public class CompilerInstructionsImpl implements CompilerInstructions {
	@NotNull
	private List<LibraryStatementPart> lsps = new ArrayList<>();
	private CiIndexingStatement        _idx;
	private CM_Filename                filename;
	private GenerateStatement          gen;
	private @NotNull LocatableString name;
	private CompilerInput              advised;

	private static Optional<String> __genLangHelper(final GenerateStatementImpl.Directive gin) {
		final CiExpression lang_raw = gin.getExpression();
		if (lang_raw instanceof CiStringExpression) {
			final CiStringExpression langRaw = (CiStringExpression) lang_raw;
			final String           s       = Helpers.remove_single_quotes_from_string(langRaw.getText());
			return Optional.of(s);
		} else {
			return Optional.empty();
		}
	}

//	@Override
//	public File makeFile() {
//		if (advised != null) {
//			return new File(advised.makeFile(), getFilename().getString());
//		} else {
//			return getFilename().fileOf();
//		}
//	}

	@Override
	public @NotNull CiIndexingStatement indexingStatement() {
		if (_idx == null)
			_idx = new CiIndexingStatementImpl(this);

		return _idx;
	}

	@Override
	public void add(final GenerateStatement generateStatement) {
		assert gen == null;
		gen = generateStatement;
	}

	@Override
	public void add(final LibraryStatementPartImpl libraryStatementPart) {
		lsps.add(libraryStatementPart);
	}

	@Override
	public CM_Filename getFilename() {
		return filename;
	}

	@Override
	public void setFilename(final String filename) {
		this.filename = CM_Filename.of(filename);
	}

	@Override
	public void setFilename(final CM_Filename filename) {
		this.filename = filename;
	}

	@Override
	public void add(final @NotNull LibraryStatementPart libraryStatementPart) {
		libraryStatementPart.setInstructions(this);
		lsps.add(libraryStatementPart);
	}

	@Override
	public @Nullable String genLang() {
		final Optional<String> genLang = ((GenerateStatementImpl) gen).dirs.stream()
		                                                                   .filter(input -> input.getName().equals("gen"))
		                                                                   .findAny() // README if you need more than one, comment this out
		                                                                   .stream().map(CompilerInstructionsImpl::__genLangHelper)
		                                                                   .findFirst() // README here too
		                                                                   .orElse(null);

		if (genLang == null) return null;
		return genLang.get();
	}

	@Override
	public String getName() {
		return name.getString();
	}

	@Override
	public void setName(String name) {
		this.name = LocatableString.of(name);
	}

	@Override
	public void setName(@NotNull LocatableString name) {
		this.name = name;
	}

	@Override
	public List<LibraryStatementPart> getLibraryStatementParts() {
		return lsps;
	}

	@Override
	public void advise(final CompilerInput aCompilerInput) {
		advised = aCompilerInput;
	}

	@Override
	public String toString() {
		return String.format("CompilerInstructionsImpl{name='%s', filename='%s'}", name, filename);
	}
}
