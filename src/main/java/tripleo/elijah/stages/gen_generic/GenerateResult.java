/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_generic;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.LibraryStatementPart;
import tripleo.elijah.stages.gen_c.OutputFileC;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.gen_fn.GeneratedNamespace;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.util.buffer.Buffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created 4/27/21 1:11 AM
 */
public class GenerateResult {
	private Map<String, OutputFileC> outputFiles;

	private int bufferCounter = 0;

	final List<GenerateResultItem> _res = new ArrayList<GenerateResultItem>();

	public GenerateResult(/*final Map<String, OutputFileC> aOutputFiles, final int aBufferCounter*/) {
/*
		outputFiles   = aOutputFiles;
		bufferCounter = aBufferCounter;
*/
		int y=2;
	}

	public List<GenerateResultItem> results() {
		return _res;
	}

	public void add(Buffer b, GeneratedNode n, TY ty, LibraryStatementPart aLsp, @NotNull Dependency d) {
		final GenerateResultItem item = new GenerateResultItem(ty, b, n, aLsp, d, ++bufferCounter);
		_res.add(item);
//		items.onNext(item);
	}

	public void addFunction(BaseGeneratedFunction aGeneratedFunction, Buffer aBuffer, TY aTY, LibraryStatementPart aLsp) {
		add(aBuffer, aGeneratedFunction, aTY, aLsp, aGeneratedFunction.getDependency());
	}

	public void addConstructor(GeneratedConstructor aGeneratedConstructor, Buffer aBuffer, TY aTY, LibraryStatementPart aLsp) {
		addFunction(aGeneratedConstructor, aBuffer, aTY, aLsp);
	}

	public void addClass(TY ty, GeneratedClass aClass, Buffer aBuf, LibraryStatementPart aLsp) {
		add(aBuf, aClass, ty, aLsp, aClass.getDependency());
	}

	public void addNamespace(TY ty, GeneratedNamespace aNamespace, Buffer aBuf, LibraryStatementPart aLsp) {
		add(aBuf, aNamespace, ty, aLsp, aNamespace.getDependency());
	}

	public void signalDone(final Map<String, OutputFileC> aOutputFiles) {
		outputFiles = aOutputFiles;

		signalDone();
	}

	public void outputFiles(final @NotNull Consumer<Map<String, OutputFileC>> cmso) {
		cmso.accept(outputFiles);
	}

	public void additional(final @NotNull GenerateResult aGenerateResult) {
		// TODO find something better
		//results()
		_res.addAll(aGenerateResult.results());
	}

	public enum TY {
		HEADER, IMPL, PRIVATE_HEADER
	}

	// region REACTIVE

	private final Subject<GenerateResultItem> completedItems = ReplaySubject.<GenerateResultItem>create();

	public void subscribeCompletedItems(Observer<GenerateResultItem> aGenerateResultItemObserver) {
		completedItems.subscribe(aGenerateResultItemObserver);
	}

	public void completeItem(GenerateResultItem aGenerateResultItem) {
		completedItems.onNext(aGenerateResultItem);
	}

	public void signalDone() {
		completedItems.onComplete();
	}

	// endregion
}

//
//
//
