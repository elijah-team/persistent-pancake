/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_c;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.IdentTableEntry;
import tripleo.elijah.stages.gen_fn.ProcTableEntry;
import tripleo.elijah.stages.gen_fn.VariableTableEntry;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.ProcIA;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created 1/9/21 7:12 AM
 */
public class CReference {
	String rtext = null;
	private List<String> args;
	List<Reference> refs = new ArrayList<Reference>();

	static class Reference {
		String text;
		Ref    type;

		public Reference(String text, Ref type) {
			this.text = text;
			this.type = type;
		}
	}

	enum Ref {
		LOCAL, MEMBER, PROPERTY, INLINE_MEMBER, CONSTRUCTOR, DIRECT_MEMBER, FUNCTION
	}

	void addRef(String text, Ref type) {
		refs.add(new Reference(text, type));
	}

	public String getIdentIAPath(final IdentIA ia2, GeneratedFunction generatedFunction) {
		assert ia2.gf == generatedFunction;
		final List<InstructionArgument> s = _getIdentIAPathList(ia2, generatedFunction);

		//
		// TODO NOT LOOKING UP THINGS, IE PROPERTIES, MEMBERS
		//
		String text = "";
		List<String> sl = new ArrayList<String>();
		for (int i = 0, sSize = s.size(); i < sSize; i++) {
			InstructionArgument ia = s.get(i);
			if (ia instanceof IntegerIA) { // should only be the first element if at all
				final VariableTableEntry vte = generatedFunction.getVarTableEntry(DeduceTypes2.to_int(ia));
				text = "vv" + vte.getName();
				addRef(vte.getName(), Ref.LOCAL);
			} else if (ia instanceof IdentIA) {
				final IdentTableEntry idte = generatedFunction.getIdentTableEntry(((IdentIA) ia).getIndex());
				OS_Element resolved_element = idte.resolved_element;
				if (resolved_element != null) {
					if (i + 1 >= sSize)
						_getIdentIAPath_IdentIAHelper(null, sl, i, sSize, resolved_element, generatedFunction);
					else {
						boolean b = _getIdentIAPath_IdentIAHelper(s.get(i + 1), sl, i, sSize, resolved_element, generatedFunction);
						if (b) i++;
					}
//					addRef(text, Ref.MEMBER);
				} else {
					// TODO make tests pass but I dont like this (should throw an exception: not enough information)
					if (sl.size() == 0) {
						text = Emit.emit("/*149*/") + idte.getIdent().getText(); // TODO check if it belongs somewhere else
					} else {
						text = Emit.emit("/*152*/") + "vm" + idte.getIdent().getText();
					}
					String text2 = idte.getIdent().getText();
					addRef(text2, Ref.MEMBER);
				}
			} else if (ia instanceof ProcIA) {
				final ProcTableEntry prte = generatedFunction.getProcTableEntry(DeduceTypes2.to_int(ia));
				text = (prte.expression.getLeft()).toString();
//				assert i == sSize-1;
				addRef(text, Ref.FUNCTION); // TODO needs to use name of resolved function
			} else {
				throw new NotImplementedException();
			}
			sl.add(text);
		}
		rtext = Helpers.String_join(".", sl);
		return rtext;
	}

	public boolean _getIdentIAPath_IdentIAHelper(InstructionArgument ia_next,
												 List<String> sl,
												 int i,
												 int sSize,
												 OS_Element resolved_element,
												 GeneratedFunction generatedFunction) {
		boolean b = false;
		if (resolved_element instanceof ClassStatement) {
			// Assuming constructor call
			int code = ((ClassStatement) resolved_element)._a.getCode();
			if (code == 0) {
				System.err.println("** 31116 ClassStatement with 0 code "+resolved_element);
			}
			// README might be calling reflect or Type or Name
			// TODO what about named constructors -- should be called with construct keyword
			if (ia_next instanceof IdentIA) {
				IdentTableEntry ite = generatedFunction.getIdentTableEntry(((IdentIA) ia_next).getIndex());
				final String text = ite.getIdent().getText();
				if (text.equals("reflect")) {
					b = true;
					String text2 = String.format("ZS%d_reflect", code);
					addRef(text2, Ref.FUNCTION);
				} else if (text.equals("Type")) {
					b = true;
					String text2 = String.format("ZST%d", code); // return a TypeInfo structure
					addRef(text2, Ref.FUNCTION);
				} else if (text.equals("Name")) {
					b = true;
					String text2 = String.format("ZSN%d", code);
					addRef(text2, Ref.FUNCTION); // TODO make this not a function
				} else {
					assert i == sSize-1; // Make sure we are ending with a constructor call
					// README Assuming this is for named constructors
					String text2 = String.format("ZC%d%s", code, text);
					addRef(text2, Ref.CONSTRUCTOR);
				}
			} else {
				assert i == sSize-1; // Make sure we are ending with a constructor call
				String text2 = String.format("ZC%d", code);
				addRef(text2, Ref.CONSTRUCTOR);
			}
		} else if (resolved_element instanceof FunctionDef) {
			OS_Element parent = resolved_element.getParent();
			int code;
			if (parent instanceof ClassStatement) {
				code = ((ClassStatement) parent)._a.getCode();
			} else if (parent instanceof NamespaceStatement) {
				code = ((NamespaceStatement) parent)._a.getCode();
			} else {
				// TODO what about FunctionDef, etc
				code = -1;
			}
			// TODO what about overloaded functions
			assert i == sSize-1; // Make sure we are ending with a ProcedureCall
			sl.clear();
			if (code == -1) {
//				text2 = String.format("ZT%d_%d", enclosing_function._a.getCode(), closure_index);
			}
			String text2 = String.format("Z%d%s", code, ((FunctionDef) resolved_element).name());
			addRef(text2, Ref.FUNCTION);
		} else if (resolved_element instanceof VariableStatement) {
			// first getParent is VariableSequence
			final String text2 = ((VariableStatement) resolved_element).getName();
			if (resolved_element.getParent().getParent() == generatedFunction.getFD().getParent()) {
				// A direct member value. Doesn't handle when indirect
//				text = Emit.emit("/*124*/")+"vsc->vm" + text2;
				addRef(text2, Ref.DIRECT_MEMBER);
			} else {
				if (resolved_element.getParent().getParent() == generatedFunction.getFD()) {
//					final String text2 = ((VariableStatement) resolved_element).getName();
//					text = Emit.emit("/*126*/")+"vv" + text2;
					addRef(text2, Ref.LOCAL);
				} else {
//					final String text2 = ((VariableStatement) resolved_element).getName();
//					text = Emit.emit("/*126*/")+"vm" + text2;
					addRef(text2, Ref.MEMBER);
				}
			}
		} else if (resolved_element instanceof PropertyStatement) {
			OS_Element parent = resolved_element.getParent();
			int code;
			if (parent instanceof ClassStatement) {
				code = ((ClassStatement) parent)._a.getCode();
			} else if (parent instanceof NamespaceStatement) {
				code = ((NamespaceStatement) parent)._a.getCode();
			} else {
//							code = -1;
				throw new IllegalStateException("PropertyStatement cant have other parent than ns or cls. " + resolved_element.getClass().getName());
			}
			sl.clear();  // don't we want all the text including from sl?
//			if (text.equals("")) text = "vsc";
//			text = String.format("ZP%dget_%s(%s)", code, ((PropertyStatement) resolved_element).name(), text); // TODO Don't know if get or set!
			String text2 = String.format("ZP%dget_%s", code, ((PropertyStatement) resolved_element).name()); // TODO Don't know if get or set!
			addRef(text2, Ref.PROPERTY);
		} else if (resolved_element instanceof AliasStatement) {
			int y=2;
			NotImplementedException.raise();
//			text = Emit.emit("/*167*/")+((AliasStatement)resolved_element).name();
//			return _getIdentIAPath_IdentIAHelper(text, sl, i, sSize, _res)
		} else {
//						text = idte.getIdent().getText();
			System.out.println("1008 "+resolved_element.getClass().getName());
			throw new NotImplementedException();
		}
		return b;
	}

	@NotNull
	private List<InstructionArgument> _getIdentIAPathList(@NotNull InstructionArgument oo, GeneratedFunction generatedFunction) {
		List<InstructionArgument> s = new LinkedList<InstructionArgument>();
		while (oo != null) {
			if (oo instanceof IntegerIA) {
				s.add(0, oo);
				oo = null;
			} else if (oo instanceof IdentIA) {
				final IdentTableEntry ite1 = generatedFunction.getIdentTableEntry(((IdentIA) oo).getIndex());
				s.add(0, oo);
				oo = ite1.backlink;
			} else if (oo instanceof ProcIA) {
				final ProcTableEntry prte = generatedFunction.getProcTableEntry(DeduceTypes2.to_int(oo));
				s.add(0, oo);
				oo = null;
			} else
				throw new IllegalStateException("Invalid InstructionArgument");
		}
		return s;
	}

	@NotNull
	public String build() {
		StringBuilder sb = new StringBuilder();
		boolean open = false, needs_comma = false;
//		List<String> sl = new ArrayList<String>();
		String text = "";
		for (Reference ref : refs) {
			switch (ref.type) {
			case LOCAL:
				text = "vv" + ref.text;
				sb.append(text);
				break;
			case MEMBER:
				text = "->vm" + ref.text;
				sb.append(text);
				break;
			case INLINE_MEMBER:
				text = Emit.emit("/*219*/")+".vm" + ref.text;
				sb.append(text);
				break;
			case DIRECT_MEMBER:
				text = Emit.emit("/*124*/")+"vsc->vm" + ref.text;
				sb.append(text);
				break;
			case FUNCTION: {
				final String s = sb.toString();
				text = String.format("%s(%s", ref.text, s);
				sb = new StringBuilder();
				open = true;
				if (!s.equals("")) needs_comma = true;
				sb.append(text);
				break;
			}
			case CONSTRUCTOR: {
				final String s = sb.toString();
				text = String.format("%s(%s", ref.text, s);
				sb = new StringBuilder();
				open = true;
				if (!s.equals("")) needs_comma = true;
				sb.append(text);
				break;
			}
			case PROPERTY: {
				final String s = sb.toString();
				text = String.format("%s(%s", ref.text, s);
				sb = new StringBuilder();
				open = true;
				if (!s.equals("")) needs_comma = true;
				sb.append(text);
				break;
			}
			default:
				throw new IllegalStateException("Unexpected value: " + ref.type);
			}
//			sl.add(text);
		}
//		return Helpers.String_join("->", sl);
		if (needs_comma && args != null && args.size() > 0)
			sb.append(", ");
		if (open) {
			if (args != null) {
				sb.append(Helpers.String_join(", ", args));
			}
			sb.append(")");
		}
		return sb.toString();
	}

	/**
	 * Call before you call build
	 *
	 * @param sl3
	 */
	public void args(List<String> sl3) {
		args = sl3;
	}
}

//
//
//