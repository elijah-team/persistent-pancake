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
import tripleo.elijah.stages.gen_c.OutputFileC;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNamespace;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_c.CDependencyRef;
import tripleo.elijah.stages.gen_generic.Dependency;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.gen_generic.GenerateResultItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created 1/8/21 11:02 PM
 */
public class ElSystem {
	private final Supplier<OutputStrategy>       outputStrategyCreator;
	//private       OutputStrategy                 outputStrategy;
	private final Map<GeneratedFunction, String> gfm_map = new HashMap<GeneratedFunction, String>();
	public        boolean                        verbose;
	//private       Compilation                    compilation;

	public ElSystem(final boolean aB, final Compilation aC, final Supplier<OutputStrategy> aCreateOutputStratgy) {
		verbose = aB;
		//compilation = aC;
		outputStrategyCreator = aCreateOutputStratgy;
	}

	public void generateOutputs(@NotNull GenerateResult gr) {
		//final OutputStrategy  outputStrategy1 = this.outputStrategy;
		final OutputStrategy  outputStrategy1 = outputStrategyCreator.get();
		// TODO hard coded
		final OutputStrategyC outputStrategyC = new OutputStrategyC(outputStrategy1);

		for (final GenerateResultItem ab : gr.results()) {
			final String filename = getFilenameForNode(ab.node, ab.ty, outputStrategyC);

			assert filename != null;

			ab.output = filename;

			final Dependency dependency1 = ab.getDependency();

			if (ab.ty == GenerateResult.TY.HEADER)
				dependency1.setRef(new CDependencyRef(filename));

			for (final Dependency dependency : dependency1.getNotedDeps()) {
				if (dependency.referent != null) {
					final String filename1 = getFilenameForNode((GeneratedNode) dependency.referent, GenerateResult.TY.HEADER, outputStrategyC);
					dependency.setRef(new CDependencyRef(filename1));
				} else {
					int y=2;
					assert false;
				}
			}

			gr.completeItem(ab);
		}

		if (verbose) {
			for (GenerateResultItem ab : gr.results()) {
				if (ab.node instanceof GeneratedFunction) continue;
				System.out.println("** "+ab.node+" "+ ab.output/*((CDependencyRef)ab.getDependency().getRef()).getHeaderFile()*/);
			}
		}

		Map<String, OutputFileC> outputFiles = new HashMap<>();

		for (GenerateResultItem ab : gr.results()) {
			OutputFileC outputFileC = new OutputFileC(ab.output);
			outputFiles.put(ab.output, outputFileC);
		}

		for (GenerateResultItem ab : gr.results()) {
			final OutputFileC outputFileC = outputFiles.get(ab.output);
			outputFileC.putDependencies(ab.getDependency().getNotedDeps/*dependencies*/());
		}

		for (GenerateResultItem ab : gr.results()) {
			final OutputFileC outputFileC = outputFiles.get(ab.output);
			outputFileC.putBuffer(ab.buffer);
		}

		for (GenerateResultItem ab : gr.results()) {
			final OutputFileC outputFileC = outputFiles.get(ab.output);
			ab.outputFile = outputFileC;
		}

		gr.signalDone(outputFiles);
	}

	String getFilenameForNode(GeneratedNode node, GenerateResult.TY ty, OutputStrategyC outputStrategyC) {
		String s, ss;
		//GeneratedNode // todo: find stupid iterator
		if (node instanceof GeneratedNamespace) {
			final GeneratedNamespace generatedNamespace = (GeneratedNamespace) node;
			s = outputStrategyC.nameForNamespace(generatedNamespace, ty);
//			System.out.println("41 "+generatedNamespace+" "+s);
			for (GeneratedFunction gf : generatedNamespace.functionMap.values()) {
				ss = getFilenameForNode(gf, ty, outputStrategyC);
				gfm_map.put(gf, ss);
			}
		} else if (node instanceof GeneratedClass) {
			final GeneratedClass generatedClass = (GeneratedClass) node;
			s = outputStrategyC.nameForClass(generatedClass, ty);
//			System.out.println("48 "+generatedClass+" "+s);
			for (GeneratedFunction gf : generatedClass.functionMap.values()) {
				ss = getFilenameForNode(gf, ty, outputStrategyC);
				gfm_map.put(gf, ss);
			}
		} else if (node instanceof GeneratedFunction) {
			final GeneratedFunction generatedFunction = (GeneratedFunction) node;
			s = outputStrategyC.nameForFunction(generatedFunction, ty);
//			System.out.println("55 "+generatedFunction+" "+s);
		} else if (node instanceof GeneratedConstructor) {
			final GeneratedConstructor generatedConstructor = (GeneratedConstructor) node;
			s = outputStrategyC.nameForConstructor(generatedConstructor, ty);
//			System.out.println("55 "+generatedConstructor+" "+s);
		} else
			throw new IllegalStateException("Can't be here.");
		return s;
	}

	//public void setOutputStrategy(OutputStrategy aOutputStrategy) {
	//	outputStrategy = aOutputStrategy;
	//}

	//public void setCompilation(Compilation aCompilation) {
	//	compilation = aCompilation;
	//}
}

//
//
//
