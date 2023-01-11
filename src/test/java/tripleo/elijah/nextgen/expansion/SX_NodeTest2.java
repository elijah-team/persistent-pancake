package tripleo.elijah.nextgen.expansion;

import junit.framework.TestCase;
import tripleo.elijah.comp.AccessBus;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.PipelineLogic;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.comp.internal.CompilationImpl;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.nextgen.model.SM_ClassBody;
import tripleo.elijah.nextgen.model.SM_ClassDeclaration;
import tripleo.elijah.nextgen.model.SM_ClassInheritance;
import tripleo.elijah.nextgen.model.SM_ClassSubtype;
import tripleo.elijah.nextgen.model.SM_Name;
import tripleo.elijah.nextgen.outputstatement.EG_SyntheticStatement;
import tripleo.elijah.stages.gen_c.GenerateC;
import tripleo.elijah.stages.gen_generic.GenerateFiles;
import tripleo.elijah.stages.gen_generic.OutputFileFactoryParams;
import tripleo.elijah.stages.logging.ElLog;

import java.util.List;

import static tripleo.elijah.util.Helpers.List_of;

public class SX_NodeTest2 extends TestCase {

	public void testFullText() {
		final StdErrSink      errSink       = new StdErrSink();
		final IO              io            = new IO();
		final CompilationImpl comp          = new CompilationImpl(errSink, io);
		final AccessBus       ab            = new AccessBus(comp);
		final PipelineLogic   pipelineLogic = new PipelineLogic(ab);
		final OS_Module mod = comp.moduleBuilder()
		                          .withFileName("filename.elijah")
		                          .addToCompilation()
		                          .build();
		final OutputFileFactoryParams p    = new OutputFileFactoryParams(mod, errSink, ElLog.Verbosity.SILENT, pipelineLogic);
		final GenerateFiles           fgen = new GenerateC(p);

		final SM_ClassDeclaration node = new SM_ClassDeclaration() {
			@Override
			public SM_Name name() {
				return new SM_Name() {
					@Override
					public String getText() {
						return "Main";
					}
				};
			}

			@Override
			public SM_ClassSubtype subType() {
				return SM_ClassSubtype.NORMAL;
			}

			@Override
			public SM_ClassInheritance inheritance() {
				return new SM_ClassInheritance() {
					@Override
					public List<SM_Name> names() {
						return List_of(new SM_Name() {
							@Override
							public String getText() {
								return "Arguments";
							}
						});
					}
				};
			}

			@Override
			public SM_ClassBody classBody() {
				return null;
			}
		};

		fgen.forNode(node);

		// (syn include local "main.h" :rule c-interface-default)
		final EG_SyntheticStatement emh = new EG_SyntheticStatement();
		// (syn include system (?) :Prelude :rule c-interface-prelude-default)
		// (syn linebreak :rule c-break-after-includes)
		// (comp enc {8} BRACES {9++})
		// 8 -> (comp seq c-fn-hdr '("void" :tag fn Main::main/<rt-type>)
		//    '("z100main" :tag <class Main> :rule c-fn-code-main))
		// 9++ -> (comp enc {9+} BRACES ...???)
		// 9+ -> (comp seq '({9.1} {9.2}))
		// 9.1 -> (sing c-simple-decl-assign
		//   ("int" :tag [i/c-type] :rule el-std-c-types)
		//   ("vvi" :tag [i] :rule el-std-var-name)
		//   ("=" :rule c-assignment)
		//   ("0" :rule el-c-inot-iterate-with-initial-increment)
		//   '(list '( ("Z100*" :rule c-class-code-main :rule c-class-ptr :rule c-class-z-prefix :tag <class Main>)
		//             ("C" :rule c-arg-c-for-current)))
		//   (";" :rule c-close-statement) // auto??
		// 9.2 -> (comp enc {9.2.1} BRACES {9.2.2})
		// 9.2.1 -> (comp seq while-loop-range-monotonic ("while") (LPAREN) (ENC??)
		//            ("vvi" :tag [with i])
		//            ("<=" :rule el-c-integer-iterator-pre-begin-test)
		//            ("100" :tag [to 100] :rule el-c-integer-iterator-to)
		//            (RPAREN))
		// 9.2.2 -> (comp seq {9.2.2.1} {9.2.2.2})
		// 9.2.2.1 -> (comp sing "vvi++" (postinc "vvi")?? :rule el-integer-iterate-monotonic
		//              :tag [with i])
		// 9.2.2.2 -> (comp sing fn-call (("println_int" [:ref Prelude/println @( Prelude/SystemInteger )]) // list vs list
		//                                ("vvi" :tag [with i]))
	}
}
