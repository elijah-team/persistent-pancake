/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.generate;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.nextgen.outputtree.EOT_OutputFile;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNamespace;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.gen_generic.GenerateResultItem;
import tripleo.elijah.util.SimplePrintLoggerToRemoveSoon;

import java.util.HashMap;
import java.util.Map;

/**
 * Created 1/8/21 11:02 PM
 */
public class ElSystem {
	private OutputStrategy outputStrategy;
	private Compilation compilation;
	private final Map<GeneratedFunction, String> gfm_map = new HashMap<GeneratedFunction, String>();
	public boolean verbose = true;

	public void generateOutputs(@NotNull final GenerateResult gr) {
		final @NotNull OutputStrategyC outputStrategyC = new OutputStrategyC(this.outputStrategy);

		for (final GenerateResultItem ab : gr.results()) {
			final EOT_OutputFile.FileNameProvider fn = generateOutputs_Internal2(ab.node, ab.ty, outputStrategyC);

			assert fn.getFilename() != null;
			ab.output = fn.getFilename();
		}

		if (verbose) {
			for (final GenerateResultItem ab : gr.results()) {
				if (ab.node instanceof GeneratedFunction) continue;
				SimplePrintLoggerToRemoveSoon.println2("** " + ab.node + " " + ab.output);
			}
		}
	}

	private EOT_OutputFile.FileNameProvider generateOutputs_Internal2(final GeneratedNode node, final GenerateResult.TY ty, final OutputStrategyC outputStrategyC) {
		final EOT_OutputFile.FileNameProvider s;
		String                                ss;
		if (node instanceof GeneratedNamespace) {
			final GeneratedNamespace generatedNamespace = (GeneratedNamespace) node;
			s = outputStrategyC.nameForNamespace2(generatedNamespace, ty);
//			tripleo.elijah.util.Stupidity.println2("41 "+generatedNamespace+" "+s);
			for (final GeneratedFunction gf : generatedNamespace.functionMap.values()) {
				ss = generateOutputs_Internal(gf, ty, outputStrategyC);
				gfm_map.put(gf, ss);
			}
		} else if (node instanceof GeneratedClass) {
			final GeneratedClass generatedClass = (GeneratedClass) node;
			s = outputStrategyC.nameForClass2(generatedClass, ty);
//			tripleo.elijah.util.Stupidity.println2("48 "+generatedClass+" "+s);
			for (final GeneratedFunction gf : generatedClass.functionMap.values()) {
				ss = generateOutputs_Internal(gf, ty, outputStrategyC);
				gfm_map.put(gf, ss);
			}
		} else if (node instanceof GeneratedFunction) {
			final GeneratedFunction generatedFunction = (GeneratedFunction) node;
			s = outputStrategyC.nameForFunction2(generatedFunction, ty);
//			tripleo.elijah.util.Stupidity.println2("55 "+generatedFunction+" "+s);
		} else if (node instanceof GeneratedConstructor) {
			final GeneratedConstructor generatedConstructor = (GeneratedConstructor) node;
			s = outputStrategyC.nameForConstructor2(generatedConstructor, ty);
//			tripleo.elijah.util.Stupidity.println2("55 "+generatedConstructor+" "+s);
		} else {
			throw new IllegalStateException("Can't be here.");
		}
		return s;
	}

	String generateOutputs_Internal(final GeneratedNode node, final GenerateResult.TY ty, final OutputStrategyC outputStrategyC) {
		return generateOutputs_Internal2(node, ty, outputStrategyC).getFilename();
	}

	public void setOutputStrategy(final OutputStrategy outputStrategy) {
		this.outputStrategy = outputStrategy;
	}

	public void setCompilation(final Compilation compilation) {
		this.compilation = compilation;
	}
}

//
//
//
