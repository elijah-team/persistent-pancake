/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import org.jdeferred2.DoneCallback;
import org.jdeferred2.impl.DeferredObject;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.stages.deduce.pipeline_impl.DeducePipelineImpl;
import tripleo.elijah.stages.gen_fn.GeneratedNode;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.util.NotImplementedException;

/**
 * Created 8/21/21 10:10 PM
 */
public class DeducePipeline implements PipelineMember, Consumer<Supplier<GenerateResult>> {
	protected void logProgress(final String g) {
		System.err.println(g);
	}

	private final DeducePipelineImpl impl;

	public DeducePipeline(final @NotNull ICompilationAccess aCa) {
		logProgress("***** Hit DeducePipeline constructor");
		impl = new DeducePipelineImpl(aCa.getCompilation());
	}

	@Override
	public void run() {
		logProgress("***** Hit DeducePipeline #run");
		impl.run();
	}

	//public void setPipelineLogic(final PipelineLogic aPipelineLogic) {
	//	logProgress("***** Hit DeducePipeline #setPipeline");
	//	impl.setPipelineLogic(aPipelineLogic);
	//}

	//public @NotNull List<GeneratedNode> lgc() {
	//	return impl.lgc; // almost caught myself java'ing and returning a Supplier (but how is this *not* correct?)
	//}

	@Override
	public void accept(final Supplier<GenerateResult> t) {
		NotImplementedException.raise();
	}

	public void lgcp(final DoneCallback<List<GeneratedNode>> aListDoneCallback) {
		_lgcp.then(aListDoneCallback);
	}

	public void lgcpdd(final List<GeneratedNode> lgn) {
		_lgcp.resolve(lgn);
	}

	private final DeferredObject<List<GeneratedNode>, Void, Void> _lgcp = new DeferredObject<>();
}

//
//
//
