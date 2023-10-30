package tripleo.elijah.nextgen.inputtree;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.ci.LibraryStatementPart;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.comp.PipelineLogic;
import tripleo.elijah.lang.ModuleItem;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_generic.GenerateFiles;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.gen_generic.OutputFileFactory;
import tripleo.elijah.stages.gen_generic.OutputFileFactoryParams;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.work.WorkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EIT_ModuleInput implements EIT_Input {
	private final Compilation c;
	private final OS_Module   module;

	@Contract(pure = true)
	public EIT_ModuleInput(final OS_Module aModule, final Compilation aC) {
		module = aModule;
		c = aC;
	}

//	public @NotNull SM_Module computeSourceModel() {
//		final SM_Module sm = new SM_Module() {
//			@Override
//			public @NotNull List<SM_ModuleItem> items() {
//				final List<SM_ModuleItem> items = new ArrayList<>();
//				for (final ModuleItem item : module.getItems()) {
//					items.add(new SM_ModuleItem() {
//						@Override
//						public ModuleItem _carrier() {
//							return item;
//						}
//					});
//				}
//				return items;
//			}
//		};
//		return sm;
//	}

	@Override
	public @NotNull EIT_InputType getType() {
		return EIT_InputType.ELIJAH_SOURCE;
	}

	@NotNull
	private String langOfModule() {
		final LibraryStatementPart lsp  = module.getLsp();
		final CompilerInstructions ci   = lsp.getInstructions();

		if (ci == null) return "\'c\'";

		final String               lang0    = ci.genLang();
		final String               lang = lang0 == null ? Compilation.CompilationAlways.defaultPrelude() : lang0;
		// DEFAULT(compiler-default), SPECIFIED(gen-clause: codePoint), INHERITED(cp) //
		// CodePoint??
		return lang;
	}


    public void doGenerate(final List<GeneratedNode> nodes,
                           final ErrSink aErrSink,
                           final ElLog.Verbosity verbosity,
                           final PipelineLogic pipelineLogic,
                           final WorkManager wm,
                           final @NotNull Consumer<GenerateResult> resultConsumer) {
        // 0. get lang
        final String lang = langOfModule();

        // 1. find Generator (GenerateFiles) eg. GenerateC
        final OutputFileFactoryParams p             = new OutputFileFactoryParams(module, aErrSink, verbosity, pipelineLogic);
        final GenerateFiles           generateFiles = OutputFileFactory.create(lang, p);

        // 2. query results
        final GenerateResult gr2 = generateFiles.resultsFromNodes(nodes, wm);

        // 3. #drain workManager -> README part of workflow. may change later as appropriate
        wm.drain();

        // 4. tail process results
        resultConsumer.accept(gr2);
    }


}
