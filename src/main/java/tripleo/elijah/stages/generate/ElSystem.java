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
import tripleo.elijah.UnintendedUseException;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.modeltransition.ElSystemSink;
import tripleo.elijah.modeltransition.EventualProvider;
import tripleo.elijah.nextgen.outputtree.EOT_FileNameProvider;
import tripleo.elijah.stages.deduce.percy.Provided;
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
import java.util.function.Supplier;

/**
 * Created 1/8/21 11:02 PM
 */
public class ElSystem {
	private final Provided<ElSystemSink> cmElSystemSink = new EventualProvider<>();
	private       OutputStrategy         outputStrategy;
	private Compilation compilation;
	private final Map<GeneratedFunction, String> gfm_map = new HashMap<GeneratedFunction, String>();
	public boolean verbose = true;
	private GenerateResult gr;

	public ElSystem() {
		cmElSystemSink.on(result -> result.provide(ElSystem.this));
	}

	public void generateOutputs(@NotNull final GenerateResult gr, final ElSystemSink sink) {
		cmElSystemSink.provide(sink);
		generateOutputs(gr);
	}

	public void generateOutputs(@NotNull final GenerateResult gr) {
		// ling my place here
		final ElSystemSink s;
		if (cmElSystemSink.has()) {
			s = cmElSystemSink.get();
		} else {
			s = new ElSystemSink() {
				@Override
				public void provide(final ElSystem aSystem) {
					throw new UnintendedUseException();
				}

				@Override
				public OutputStrategyC strategy(final OutputStrategy aOutputStrategy) {
//					return new OutputStrategyC(aOutputStrategy);
					throw new UnintendedUseException();
				}

				@Override
				public void addGenerateResultItem(final GenerateResultItem ab, final Supplier<EOT_FileNameProvider> aSupplier) {
					throw new UnintendedUseException();
				}
			};
		}

		resultInto(s);
	}

	private EOT_FileNameProvider generateOutputs_Internal2(final GeneratedNode node, final GenerateResult.TY ty, final OutputStrategyC outputStrategyC) {
		final EOT_FileNameProvider s;
		String                     ss;
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

	public void resultInto(final ElSystemSink sink) {
		final @NotNull OutputStrategyC outputStrategyC = sink.strategy(this.outputStrategy);

		assert gr != null;

		for (final GenerateResultItem ab : gr.results()) {
			sink.addGenerateResultItem(ab, ()->generateOutputs_Internal2(ab.node, ab.ty, outputStrategyC));
		}

		if (verbose) {
			for (final GenerateResultItem ab : gr.results()) {
				if (ab.node instanceof GeneratedFunction) continue;
				SimplePrintLoggerToRemoveSoon.println2("** " + ab.node + " " + ab.output);
			}
		}
	}

	public void __gr_slot(final GenerateResult aGr) {
		gr = aGr;
	}
}

//
//
//
