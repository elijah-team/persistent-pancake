/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.util.NotImplementedException;

/**
 * Created 8/21/21 10:16 PM
 */
public class GeneratePipeline implements PipelineMember, Consumer<Supplier<GenerateResult>> {
	private final Compilation    c;

	@Contract(pure = true)
	public GeneratePipeline(Compilation aCompilation, @NotNull DeducePipeline aDpl) {
		c = aCompilation;

		aDpl.lgcp(new DoneCallback<List<GeneratedNode>>() {
			@Override
			public void onDone(final List<GeneratedNode> result) {
				latch.set(result);
				latch.run();
			}
		});
	}

	final private GPL latch = new GPL();

	@Override
	public void run() {
		NotImplementedException.raise();
	}

	@Override
	public void accept(Supplier<GenerateResult> t) {
		NotImplementedException.raise();
	}

	private class GPL implements Runnable {
		private List<GeneratedNode> result;

		@Contract(mutates = "this")
		public void set(final List<GeneratedNode> aResult) {
			result = aResult;
		}

		@Override
		public void run() {
			c.pipelineLogic.generate(result);
		}
	}
}

//
//
//
