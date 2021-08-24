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
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.gen_fn.GeneratedNamespace;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.util.buffer.Buffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 4/27/21 1:11 AM
 */
public class GenerateResult {
	private int bufferCounter = 0;

	final List<GenerateResultItem> res = new ArrayList<GenerateResultItem>();

	public List<GenerateResultItem> results() {
		return res;
	}

	public void add(Buffer b, GeneratedNode n, TY ty) {
		final GenerateResultItem item = new GenerateResultItem(ty, b, n, ++bufferCounter);
		res.add(item);
//		items.onNext(item);
	}

	public void addFunction(BaseGeneratedFunction aGeneratedFunction, Buffer aBuffer, TY aTY) {
		add(aBuffer, aGeneratedFunction, aTY);
	}

	public void addConstructor(GeneratedConstructor aGeneratedFunction, Buffer aBuffer, TY aTY) {
		addFunction(aGeneratedFunction, aBuffer, aTY);
	}

	public void addClass(TY ty, GeneratedClass aClass, Buffer aBuf) {
		add(aBuf, aClass, ty);
	}

	public void addNamespace(TY ty, GeneratedNamespace aNamespace, Buffer aBuf) {
		add(aBuf, aNamespace, ty);
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
