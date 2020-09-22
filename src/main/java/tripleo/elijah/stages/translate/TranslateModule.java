package tripleo.elijah.stages.translate;

import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created 9/3/20 12:38 PM
 */
public class TranslateModule {
	private final OS_Module module;

	public TranslateModule(OS_Module module_) {
		module = module_;
	}

	public void translate() {
//		TabbedOutputStream w = null;
		try {
//			w = getFile();

			for (ModuleItem item : module.getItems()) {
				try {
					if (item instanceof ClassStatement) {
						put_class_statement((ClassStatement) item);
					} else if (item instanceof NamespaceStatement) {
						put_namespace_statement((NamespaceStatement) item);
					} else
						System.out.println("8000 "+item);
				} catch (IOException e) {
					module.parent.eee.exception(e);
				}
			}
		} finally {
//			if (w != null) {
//				try {
//					w.close();
//				} catch (IOException e) {
//					module.parent.eee.exception(e);
//				}
//			}
		}
	}

	private void put_class_statement(ClassStatement item) throws IOException {
		final String cls_name = "C"+item.name();
		final TabbedOutputStream w = stream_for(item.getPackageName().getName(), cls_name);
		try {
			{
				final OS_Package packageName = item.getPackageName();
				if (!packageName.equals("")) {
					String pkg_decl = "package " + packageName.getName()+";";
					w.put_string_ln(pkg_decl);
					w.put_string_ln("");
				}
			}
			w.put_string_ln("class "+ cls_name + " {");
			w.incr_tabs();
			put_class_statement_internal(item, w);
			w.dec_tabs();
			w.put_string_ln("}");
		} finally {
			w.close();
		}
	}

	private void put_namespace_statement(NamespaceStatement item) throws IOException {
		final StringBuilder ns_name_sb = new StringBuilder("NS_");
		switch(item.getKind()) {
			case NAMED:
				ns_name_sb.append(item.name());
				break;
			case PRIVATE:
				ns_name_sb.append("__private__");
				break;
			case MODULE:
				ns_name_sb.append("__module__");
				// TODO get a number for this, even if its %n_%n
				break;
			case PACKAGE:
				ns_name_sb.append("__package__");
				break;
		}
		final String ns_name = ns_name_sb.toString();
		final TabbedOutputStream w = stream_for(item.getPackageName().getName(), ns_name);
		try {
			{
				final OS_Package packageName = item.getPackageName();
				if (!packageName.equals("")) {
					String pkg_decl = "package " + packageName.getName()+";";
					w.put_string_ln(pkg_decl);
					w.put_string_ln("");
				}
			}
			w.put_string_ln("class "+ ns_name + " {");
			w.incr_tabs();
			put_namespace_statement_internal(item, w);
			w.dec_tabs();
			w.put_string_ln("}");
		} finally {
			w.close();
		}
	}

	private static TabbedOutputStream stream_for(String packageName, String name) throws FileNotFoundException {
		String packageDirName = packageName.replace('.', '/');
		if (packageDirName.equals(""))
			packageDirName = ".";
		final File dir = new File("output", packageDirName);
		dir.mkdirs();
		final File file = new File(dir, name+".java");
		final FileOutputStream os = new FileOutputStream(file);
		TabbedOutputStream R = new TabbedOutputStream(os);
		return R;
	}

	private void put_class_statement_internal(ClassStatement classStatement, TabbedOutputStream w) throws IOException {
		for (ClassItem item : classStatement.getItems()) {
			if (item instanceof FunctionDef) {
				w.put_string("public void "+((FunctionDef) item).name()+"(");
				put_formal_arg_list(((FunctionDef) item).fal(), w);
				w.put_string_ln(") {");
				w.incr_tabs();
				put_function_def((FunctionDef) item, w);
				w.dec_tabs();
				w.put_string_ln("}");
			} else
				System.out.println("8001 "+item);
		}
	}

	private void put_formal_arg_list(FormalArgList fal, TabbedOutputStream w) throws IOException {
		for (FormalArgListItem fali : fal.falis) {
			w.put_string(fali.typeName().toString());
			w.put_string(" ");
			w.put_string(fali.name());
			w.put_string(",");
		}
	}

	private void put_function_def(FunctionDef functionDef, TabbedOutputStream w) throws IOException {
		for (FunctionItem item : functionDef.getItems()) {
			System.out.println("8003 "+item);
			if (item instanceof AliasStatement) {

			} else if (item instanceof CaseConditional) {

			} else if (item instanceof ClassStatement) {

			} else if (item instanceof StatementWrapper) {

			} else if (item instanceof IfConditional) {

			} else if (item instanceof Loop) {

			} else if (item instanceof MatchConditional) {

			} else if (item instanceof NamespaceStatement) {

			} else if (item instanceof VariableSequence) {
				for (VariableStatement vs : ((VariableSequence) item).items()) {
					System.out.println("8004 " + vs);
					final OS_Type type = vs.initialValue().getType();
					final String stype = type == null ? "Unknown" : getTypeString(type);
					System.out.println("8004-1 " + type);
					w.put_string_ln(String.format("%s %s;", stype, vs.getName()));
				}
			} else if (item instanceof WithStatement) {

			} else if (item instanceof SyntacticBlock) {

			} else {
				throw new IllegalStateException("cant be here");
			}

		}
	}

	private String getTypeString(OS_Type type) {
		switch (type.getType()) {
			case BUILT_IN:
				BuiltInTypes bt = type.getBType();
				return bt.name();
//				if (type.resolve())
//					return type.getClassOf().getName();
//				else
//					return "Unknown";
			case USER:
				return type.getTypeName().toString();
			case USER_CLASS:
				return type.getClassOf().getName();
			case FUNCTION:
				return "Function<>";
			default:
				throw new IllegalStateException("cant be here");
		}
//		return type.toString();
	}

	private void put_namespace_statement_internal(NamespaceStatement namespaceStatement, TabbedOutputStream w) {
		for (ClassItem item : namespaceStatement.getItems()) {
			System.out.println("8002 "+item);
		}
	}

	private TabbedOutputStream getFile() {
		final String fn = module.getFileName();
		final String fn2 = fn.substring(0, fn.lastIndexOf('.'));
		final String fn3 = fn2 + ".java";
		final Path p = Path.of("output", fn3);
		p.getParent().toFile().mkdirs();
		System.err.println("PATH "+p.toString());
		TabbedOutputStream w = null;
		try {
			w = new TabbedOutputStream(Files.newOutputStream(p));
			return w;
		} catch (IOException e) {
			module.parent.eee.exception(e);
		}
		return null;
	}
}
