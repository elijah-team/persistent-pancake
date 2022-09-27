/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.Contract;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.util.NotImplementedException;

/**
 * Created 8/21/21 10:16 PM
 */
public class GeneratePipeline implements PipelineMember, Consumer<Supplier<GenerateResult>> {
	private final Compilation    c;
	private final DeducePipeline dpl;

	@Contract(pure = true)
	public GeneratePipeline(Compilation aCompilation, DeducePipeline aDpl) {
		c = aCompilation;
		dpl = aDpl;
	}

	@Override
	public void run() {
		c.pipelineLogic.generate(dpl.lgc());
	}

	@Override
	public void accept(Supplier<GenerateResult> t) {
		NotImplementedException.raise();
	}
}

//
//
//
