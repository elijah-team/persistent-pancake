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
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.ci.LibraryStatementPart;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.DecideElObjectType;
import tripleo.elijah.lang.ElObjectType;
import tripleo.elijah.lang.NamespaceStatement;
import tripleo.elijah.lang.NamespaceTypes;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Package;
import tripleo.elijah.nextgen.outputtree.EOT_OutputFile;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNamespace;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_generic.GenerateResult;

import java.io.File;

/**
 * Created 1/13/21 5:54 AM
 */
public class OutputStrategyC {
	private final OutputStrategy outputStrategy;

	public OutputStrategyC(final OutputStrategy outputStrategy) {
		this.outputStrategy = outputStrategy;
	}

	public static @NotNull String nameForClass(final GeneratedClass generatedClass, final GenerateResult.TY aTy, final OutputStrategy outputStrategy1) {
		return new OSC_C(generatedClass, aTy, outputStrategy1).getFilename();
	}

	public static String nameForNamespace(final GeneratedNamespace generatedNamespace, final GenerateResult.TY aTy) {
		return new OSC_NS(generatedNamespace, aTy).getFilename();
	}

	public static void appendExtension(final GenerateResult.@NotNull TY aTy, final StringBuilder aSb) {
		switch (aTy) {
		case IMPL:
			aSb.append(".c");
			break;
		case PRIVATE_HEADER:
			aSb.append("_Priv.h");
		case HEADER:
			aSb.append(".h");
			break;
		}
	}

	private static OS_Package findPackage(OS_Element e) {
		while (e != null) {
			e = e.getParent();
			assert e != null;
			if (e.getContext().getParent() == e.getContext()) {
				e = null;
			} else {
				@NotNull final ElObjectType t = DecideElObjectType.getElObjectType(e);
				switch (t) {
				case NAMESPACE:
					final NamespaceStatement namespaceStatement = (NamespaceStatement) e;
					if (namespaceStatement.getPackageName() != null) {
						return namespaceStatement.getPackageName();
					}
				case CLASS:
					final ClassStatement classStatement = (ClassStatement) e;
					if (classStatement.getPackageName() != null) {
						return classStatement.getPackageName();
					}
				case FUNCTION:
					continue;
				default:
					// datatype, enum, alias
					continue;
				}
			}
		}
		return null;
	}

	static @NotNull String strip_elijah_extension(@NotNull String aFilename) {
		if (aFilename.endsWith(".elijah")) {
			aFilename = aFilename.substring(0, aFilename.length() - 7);
		} else if (aFilename.endsWith(".elijjah")) {
			aFilename = aFilename.substring(0, aFilename.length() - 8);
		}
		return aFilename;
	}

	public EOT_OutputFile.FileNameProvider nameForNamespace2(final GeneratedNamespace aGeneratedNamespace, final GenerateResult.TY aTy) {
		return new OSC_NS(aGeneratedNamespace, aTy);
	}

	public EOT_OutputFile.FileNameProvider nameForClass2(final GeneratedClass aGeneratedClass, final GenerateResult.TY aTy) {
		return new OSC_C(aGeneratedClass, aTy, outputStrategy);
	}

	public EOT_OutputFile.FileNameProvider nameForFunction2(final GeneratedFunction aGeneratedFunction, final GenerateResult.TY aTy) {
		return new OSC_F(aGeneratedFunction, aTy, outputStrategy);
	}

	public EOT_OutputFile.FileNameProvider nameForConstructor2(final GeneratedConstructor aGeneratedConstructor, final GenerateResult.TY aTy) {
		return new OSC_CTOR(aGeneratedConstructor, aTy, outputStrategy);
	}

	static class OSC_NS implements EOT_OutputFile.FileNameProvider {
		private final GeneratedNamespace generatedNamespace;
		private final GenerateResult.TY  ty;

		public OSC_NS(final GeneratedNamespace aGeneratedNamespace, final GenerateResult.TY aTy) {
			generatedNamespace = aGeneratedNamespace;
			ty                 = aTy;
		}

		@Override
		public String getFilename() {
			if (generatedNamespace.module().isPrelude()) {
				// We are dealing with the Prelude
				final StringBuilder sb = new StringBuilder();
				sb.append("/Prelude/");
				sb.append("Prelude");
				appendExtension(ty, sb);
				return sb.toString();
			}
			final String filename;
			if (generatedNamespace.getNamespaceStatement().getKind() == NamespaceTypes.MODULE) {
				final String moduleFileName = generatedNamespace.module().getFileName();
				final File   moduleFile     = new File(moduleFileName);
				final String filename1      = moduleFile.getName();
				filename = strip_elijah_extension(filename1);
			} else {
				filename = generatedNamespace.getName();
			}
			final StringBuilder sb = new StringBuilder();
			sb.append("/");
			final LibraryStatementPart lsp = generatedNamespace.module().getLsp();
			if (lsp == null || lsp.getInstructions() == null) {
				sb.append("___________________");
			} else {
				sb.append(lsp.getInstructions().getName());
			}
			sb.append("/");
			OS_Package pkg = generatedNamespace.getNamespaceStatement().getPackageName();
			if (pkg != OS_Package.default_package) {
				if (pkg == null) {
					pkg = findPackage(generatedNamespace.getNamespaceStatement());
				}
				sb.append(pkg.getName());
				sb.append("/");
			}
			sb.append(filename);
			appendExtension(ty, sb);
			return sb.toString();
		}
	}

	static class OSC_C implements EOT_OutputFile.FileNameProvider {
		private final GeneratedClass generatedClass;
		private final GenerateResult.TY ty;
		private final OutputStrategy outputStrategy;

		public OSC_C(final GeneratedClass aGeneratedClass, final GenerateResult.TY aTy, final OutputStrategy aOutputStrategy) {
			generatedClass = aGeneratedClass;
			ty             = aTy;
			outputStrategy = aOutputStrategy;
		}

		@Override
		public String getFilename() {
			if (generatedClass.module().isPrelude()) {
				// We are dealing with the Prelude
				final @NotNull StringBuilder sb = new StringBuilder();
				sb.append("/Prelude/");
				sb.append("Prelude");
				appendExtension(ty, sb);
				return sb.toString();
			}
			final StringBuilder sb = new StringBuilder();
			sb.append("/");
			final LibraryStatementPart lsp = generatedClass.module().getLsp();
			if (lsp == null || lsp.getInstructions() == null) {
				if ((lsp.getInstructions() == null)) {
					throw new AssertionError();
				}

				sb.append("______________");
			} else {
				final String name = lsp.getInstructions().getName();
				assert name != null;
				sb.append(name);
			}
			sb.append("/");
			OS_Package pkg = generatedClass.getKlass().getPackageName();
			if (pkg != OS_Package.default_package) {
				if (pkg == null) {
					pkg = findPackage(generatedClass.getKlass());
				}
				sb.append(pkg.getName());
				sb.append("/");
			}
			switch (outputStrategy.per()) {
			case PER_CLASS: {
				if (generatedClass.isGeneric()) {
					sb.append(generatedClass.getNumberedName());
				} else {
					sb.append(generatedClass.getName());
				}
			}
			break;
			case PER_MODULE: {
//					mod = generatedClass.getKlass().getContext().module();
				final OS_Module mod = generatedClass.module();
				final File      f   = new File(mod.getFileName());
				String          ff  = f.getName();
				final int       y   = 2;
				ff = strip_elijah_extension(ff);
				sb.append(ff);
//					sb.append('/');
			}
			break;
			case PER_PACKAGE: {
				final OS_Package pkg2 = generatedClass.getKlass().getPackageName();
				final String     pkgName;
				if (pkg2 != OS_Package.default_package) {
					pkgName = "$default_package";
				} else {
					pkgName = pkg2.getName();
				}
				sb.append(pkgName);
			}
			break;
			case PER_PROGRAM: {
				assert lsp != null;
				final CompilerInstructions xx = lsp.getInstructions();
				final String               n  = xx.getName();
				sb.append(n);
			}
			break;
			default:
				throw new IllegalStateException("Unexpected value: " + outputStrategy.per());
			}
			appendExtension(ty, sb);
			return sb.toString();
		}
	}

	static class OSC_F implements EOT_OutputFile.FileNameProvider {
		private final GeneratedFunction generatedFunction;
		private final GenerateResult.TY ty;
		private final OutputStrategy outputStrategy;

		public OSC_F(final GeneratedFunction aGeneratedFunction, final GenerateResult.TY aTy, final OutputStrategy aOutputStrategy) {
			generatedFunction = aGeneratedFunction;
			ty                = aTy;
			outputStrategy    = aOutputStrategy;
		}

		@Override
		public String getFilename() {
			GeneratedNode c = generatedFunction.getGenClass();
			if (c == null) {
				c = generatedFunction.getParent(); // TODO fixme
			}
			if (c instanceof GeneratedClass) {
				return new OSC_C((GeneratedClass) c, ty, outputStrategy).getFilename();
			} else if (c instanceof GeneratedNamespace) {
				return new OSC_NS((GeneratedNamespace) c, ty).getFilename();
			}
			return null;
		}
	}

	static class OSC_CTOR implements EOT_OutputFile.FileNameProvider {
		private final BaseGeneratedFunction generatedConstructor;
		private final GenerateResult.TY ty;
		private final OutputStrategy outputStrategy;

		OSC_CTOR(final BaseGeneratedFunction aGeneratedConstructor, final GenerateResult.TY aTy, final OutputStrategy aOutputStrategy) {
			generatedConstructor = aGeneratedConstructor;
			ty                   = aTy;
			outputStrategy       = aOutputStrategy;
		}

		@Override
		public String getFilename() {
			GeneratedNode c = generatedConstructor.getGenClass();
			if (c == null) {
				c = generatedConstructor.getParent(); // TODO fixme
			}
			if (c instanceof GeneratedClass) {
				return nameForClass((GeneratedClass) c, ty, outputStrategy);
			} else if (c instanceof GeneratedNamespace) {
				return nameForNamespace((GeneratedNamespace) c, ty);
			}
			return null;
		}
	}
}

//
//
//
