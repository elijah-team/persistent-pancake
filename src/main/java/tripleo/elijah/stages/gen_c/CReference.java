/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: t; c-basic-offset: 4 -*- */
/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_c;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.lang.AliasStatement;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.ConstructorDef;
import tripleo.elijah.lang.DefFunctionDef;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.NamespaceStatement;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.PropertyStatement;
import tripleo.elijah.lang.VariableStatement;
import tripleo.elijah.nextgen.outputstatement.EG_CompoundStatement;
import tripleo.elijah.nextgen.outputstatement.EG_SingleStatement;
import tripleo.elijah.nextgen.outputstatement.EG_Statement;
import tripleo.elijah.nextgen.outputstatement.EX_Explanation;
import tripleo.elijah.stages.deduce.FunctionInvocation;
import tripleo.elijah.stages.deduce.post_bytecode.DeduceElement3_ProcTableEntry;
import tripleo.elijah.stages.deduce.post_bytecode.IDeduceElement3;
import tripleo.elijah.stages.gen_c.Generate_Code_For_Method.AOG;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.gen_fn.GeneratedContainerNC;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNamespace;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_fn.IdentTableEntry;
import tripleo.elijah.stages.gen_fn.ProcTableEntry;
import tripleo.elijah.stages.gen_fn.VariableTableEntry;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.Instruction;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.InstructionFixedList;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.ProcIA;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static tripleo.elijah.stages.deduce.DeduceTypes2.to_int;

/**
 * Created 1/9/21 7:12 AM
 */
public class CReference {
	private String          rtext = null;
	private List<String>    args;
	private List<Reference> refs;

	@Contract("null, _ -> fail")
	public static @NotNull EG_Statement forDeduceElement3(final IDeduceElement3 deduceElement3, final GenerateC gc) {
		//return deduceElement3.();
		if (deduceElement3 instanceof DeduceElement3_ProcTableEntry) {
			final DeduceElement3_ProcTableEntry de_pte = (DeduceElement3_ProcTableEntry) deduceElement3;
			return forDeduceElement3_ProcTableEntry(de_pte, gc);
		}

		throw new NotImplementedException();
	}

	private static @NotNull EG_Statement forDeduceElement3_ProcTableEntry(@NotNull final DeduceElement3_ProcTableEntry de_pte, final GenerateC gc) {
		final EG_SingleStatement beginning;
		final EG_SingleStatement ending;
		final EG_Statement       middle;
		final boolean            indent = false;
		final EX_Explanation     explanation;


		final ProcTableEntry pte = de_pte.getTablePrincipal();

		final BaseGeneratedFunction gf          = de_pte.getGeneratedFunction();
		final Instruction           instruction = de_pte.getInstruction();

		final StringBuilder sb = XXX_YYY.dispatch(pte, new XXX_YYY() {
			@Override
			public StringBuilder itsABoy(final IExpression expression) {
				final IdentExpression ptex = (IdentExpression) expression;
				final String          text = ptex.getText();

				@Nullable final InstructionArgument xx = gf.vte_lookup(text);
				assert xx != null;

				final String       realTargetName = gc.getRealTargetName(gf, (IntegerIA) xx, Generate_Code_For_Method.AOG.GET);
				final List<String> sl3            = gc.getArgumentStrings(gf, () -> new InstructionFixedList(instruction));

				final StringBuilder sb = new StringBuilder();
				sb.append(Emit.emit("/*424*/"));
				sb.append(realTargetName);
				sb.append('(');
				sb.append(Helpers.String_join(", ", sl3));
				sb.append(");");

				return sb;
			}

			@Override
			public StringBuilder itsAGirl(final InstructionArgument expression_num) {
				final IdentIA identIA = (IdentIA) expression_num;

				final CReference reference = new CReference();
				reference.getIdentIAPath(identIA, Generate_Code_For_Method.AOG.GET, null);
				final List<String> sl3 = gc.getArgumentStrings(gf, () -> new InstructionFixedList(instruction));
				reference.args(sl3);
				final @NotNull String path = reference.build();

				final StringBuilder sb = new StringBuilder();
				sb.append(Emit.emit("/*427*/"));
				sb.append(path);
				sb.append(";");

				return sb;
			}
		});

		beginning   = new EG_SingleStatement("", new EX_Explanation() {
		});
		ending      = new EG_SingleStatement("", new EX_Explanation() {
		});
		explanation = new EX_ProcTableEntryExplanation(de_pte);
		middle      = new EG_SingleStatement(sb.toString(), explanation);

		final EG_CompoundStatement stmt = new EG_CompoundStatement(beginning, ending, middle, indent, explanation);
		//new EX_TableEntryExplanation();
		return stmt;
	}

	public String getIdentIAPath(final @NotNull IdentIA ia2, final Generate_Code_For_Method.AOG aog, final String aValue) {
		final BaseGeneratedFunction     generatedFunction = ia2.gf;
		final List<InstructionArgument> s                 = _getIdentIAPathList(ia2);
		refs = new ArrayList<Reference>(s.size());

		//
		// TODO NOT LOOKING UP THINGS, IE PROPERTIES, MEMBERS
		//
		String             text = "";
		final List<String> sl   = new ArrayList<String>();
		for (int i = 0, sSize = s.size(); i < sSize; i++) {
			final InstructionArgument ia = s.get(i);
			if (ia instanceof IntegerIA) {
				// should only be the first element if at all
				assert i == 0;
				final VariableTableEntry vte = generatedFunction.getVarTableEntry(to_int(ia));
				text = "vv" + vte.getName();
				addRef(vte.getName(), Ref.LOCAL);
			} else if (ia instanceof IdentIA) {
				boolean               skip             = false;
				final IdentTableEntry idte             = ((IdentIA) ia).getEntry();
				final OS_Element      resolved_element = idte.getResolvedElement();
				if (resolved_element != null) {
					GeneratedNode resolved = null;
					if (resolved_element instanceof ClassStatement) {
						if (idte.type != null)
							resolved = idte.type.resolved();
						if (resolved == null)
							resolved = idte.resolvedType();
					} else if (resolved_element instanceof FunctionDef) {
						@Nullable final ProcTableEntry pte = idte.getCallablePTE();
						if (pte != null) {
							final FunctionInvocation fi = pte.getFunctionInvocation();
							if (fi != null) {
								final BaseGeneratedFunction gen = fi.getGenerated();
								if (gen != null)
									resolved = gen;
							}
						}
						if (resolved == null) {
							final GeneratedNode resolved1 = idte.resolvedType();
							if (resolved1 instanceof GeneratedFunction)
								resolved = resolved1;
							else if (resolved1 instanceof GeneratedClass)
								resolved = resolved1;
						}
					} else if (resolved_element instanceof PropertyStatement) {
						NotImplementedException.raise();
						final GeneratedNode resolved1 = idte.type.resolved();
						final int           code;
						if (resolved1 != null) {
							code = ((GeneratedContainerNC) resolved1).getCode();
						} else {
							code = -1;
						}
						short state = 0;
						if (i < sSize - 1) {
							state = 1;
						} else {
							switch (aog) {
								case GET:
									state = 1;
									break;
								case ASSIGN:
									state = 2;
									break;
							}
						}
						switch (state) {
							case 1:
								addRef(String.format("ZP%d_get%s(", code, idte.getIdent().getText()), Ref.PROPERTY_GET);
								skip = true;
								text = null;
								break;
							case 2:
								addRef(String.format("ZP%d_set%s(", code, idte.getIdent().getText()), Ref.PROPERTY_SET, aValue);
								skip = true;
								text = null;
								break;
							default:
								throw new IllegalStateException("Unexpected value: " + state);
						}
					}
					if (!skip) {
						short state = 1;
						if (idte.externalRef != null) {
							state = 2;
						}
						switch (state) {
							case 1:
								if (resolved == null) {
									tripleo.elijah.util.Stupidity.println_err("***88*** resolved is null for " + idte);
								}
								if (sSize >= i + 1) {
									_getIdentIAPath_IdentIAHelper(null, sl, i, sSize, resolved_element, generatedFunction, resolved, aValue);
									text = null;
								} else {
									final boolean b = _getIdentIAPath_IdentIAHelper(s.get(i + 1), sl, i, sSize, resolved_element, generatedFunction, resolved, aValue);
									if (b) i++;
								}
								break;
							case 2:
								if ((resolved_element instanceof VariableStatement)) {
									final String text2 = ((VariableStatement) resolved_element).getName();

									final GeneratedNode externalRef = idte.externalRef;
									if (externalRef instanceof GeneratedNamespace) {
										final String text3 = String.format("zN%d_instance", ((GeneratedNamespace) externalRef).getCode());
										addRef(text3, Ref.LITERAL, null);
									} else if (externalRef instanceof GeneratedClass) {
										assert false;
										final String text3 = String.format("zN%d_instance", ((GeneratedClass) externalRef).getCode());
										addRef(text3, Ref.LITERAL, null);
									} else
										throw new IllegalStateException();
									addRef(text2, Ref.MEMBER, aValue);
								} else
									throw new NotImplementedException();
								break;
						}
					}
				} else {
					switch (ia2.getEntry().getStatus()) {
						case KNOWN:
							assert false;
							break;
						case UNCHECKED:
							final String path2 = generatedFunction.getIdentIAPathNormal(ia2);
							final String text3 = String.format("<<UNCHECKED ia2: %s>>", path2/*idte.getIdent().getText()*/);
							text = text3;
//						assert false;
							break;
						case UNKNOWN:
							final String path = generatedFunction.getIdentIAPathNormal(ia2);
							final String text1 = idte.getIdent().getText();
//						assert false;
							// TODO make tests pass but I dont like this (should emit a dummy function or placeholder)
							if (sl.size() == 0) {
								text = Emit.emit("/*149*/") + text1; // TODO check if it belongs somewhere else (what does this mean?)
							} else {
								text = Emit.emit("/*152*/") + "vm" + text1;
							}
							tripleo.elijah.util.Stupidity.println_err("119 " + idte.getIdent() + " " + idte.getStatus());
							final String text2 = (Emit.emit("/*114*/") + String.format("%s is UNKNOWN", text1));
							addRef(text2, Ref.MEMBER);
							break;
						default:
							throw new IllegalStateException("Unexpected value: " + ia2.getEntry().getStatus());
					}
				}
			} else if (ia instanceof ProcIA) {
				final ProcTableEntry prte = generatedFunction.getProcTableEntry(to_int(ia));
				text = getIdentIAPath_Proc(prte);
			} else {
				throw new NotImplementedException();
			}
			if (text != null)
				sl.add(text);
		}
		rtext = Helpers.String_join(".", sl);
		return rtext;
	}

	/**
	 * Call before you call build
	 *
	 * @param sl3
	 */
	public void args(final List<String> sl3) {
		args = sl3;
	}

	@NotNull
	public String build() {
		final BuildState st = new BuildState();

		for (final Reference ref : refs) {
			switch (ref.type) {
				case LITERAL:
				case DIRECT_MEMBER:
				case INLINE_MEMBER:
				case MEMBER:
				case LOCAL:
				case FUNCTION:
				case PROPERTY_GET:
				case PROPERTY_SET:
				case CONSTRUCTOR:
					ref.buildHelper(st);
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + ref.type);
			}
//			sl.add(text);
		}
//		return Helpers.String_join("->", sl);

		final StringBuilder sb = st.sb;

		if (st.needs_comma && args != null && args.size() > 0)
			sb.append(", ");

		if (st.open) {
			if (args != null) {
				sb.append(Helpers.String_join(", ", args));
			}
			sb.append(")");
		}

		return sb.toString();
	}

	@NotNull
	static List<InstructionArgument> _getIdentIAPathList(@NotNull InstructionArgument oo) {
		final List<InstructionArgument> s = new LinkedList<InstructionArgument>();
		while (oo != null) {
			if (oo instanceof IntegerIA) {
				s.add(0, oo);
				oo = null;
			} else if (oo instanceof IdentIA) {
				final IdentTableEntry ite1 = ((IdentIA) oo).getEntry();
				s.add(0, oo);
				oo = ite1.getBacklink();
			} else if (oo instanceof ProcIA) {
//				final ProcTableEntry prte = ((ProcIA)oo).getEntry();
				s.add(0, oo);
				oo = null;
			} else
				throw new IllegalStateException("Invalid InstructionArgument");
		}
		return s;
	}

	void addRef(final String text, final Ref type) {
		refs.add(new Reference(text, type));
	}

	void addRef(final String text, final Ref type, final String aValue) {
		refs.add(new Reference(text, type, aValue));
	}

	boolean _getIdentIAPath_IdentIAHelper(final InstructionArgument ia_next,
	                                      final List<String> sl,
	                                      final int i,
	                                      final int sSize,
	                                      final OS_Element resolved_element,
	                                      final BaseGeneratedFunction generatedFunction,
	                                      final GeneratedNode aResolved,
	                                      final String aValue) {
		return new CReference_getIdentIAPath_IdentIAHelper(ia_next, sl, i, sSize, resolved_element, generatedFunction, aResolved, aValue).action(this);
	}

//	private final static class BuildState {
//		StringBuilder sb = new StringBuilder();
//		boolean open = false, needs_comma = false;
//
//		public void appendText(final String text, final boolean erase) {
//			if (erase)
//				sb = new StringBuilder();
//
//			sb.append(text);
//		}
//
//		@Override
//		public String toString() {
//			return sb.toString();
//		}
//		//ABOVE 3a
//	}

	public String getIdentIAPath_Proc(final @NotNull ProcTableEntry aPrte) {
		final String[]              text      = new String[1];
		final BaseGeneratedFunction generated = aPrte.getFunctionInvocation().getGenerated();

		if (generated == null)
			throw new IllegalStateException();

		final GeneratedContainerNC genClass = (GeneratedContainerNC) generated.getGenClass();

		if (generated instanceof GeneratedConstructor) {
			final int    y                   = 2;
			final String constructorNameText = generated.getFunctionName();

			text[0] = String.format("ZC%d%s", genClass.getCode(), constructorNameText);
			addRef(text[0], Ref.CONSTRUCTOR);
		} else {
			text[0] = String.format("Z%d%s", genClass.getCode(), generated.getFunctionName());
			addRef(text[0], Ref.FUNCTION);
		}

		return text[0];
	}

	public void getIdentIAPath(IdentIA ia2, BaseGeneratedFunction gf, AOG get, Object object) {
		// TODO remove this
		getIdentIAPath(ia2, get, rtext);
	}


//	enum Ref {
//		LOCAL, MEMBER, PROPERTY_GET, PROPERTY_SET, INLINE_MEMBER, CONSTRUCTOR, DIRECT_MEMBER, LITERAL, PROPERTY, FUNCTION
//	}


	enum Ref {
		// https://www.baeldung.com/a-guide-to-java-enums
		LOCAL {
			@Override
			public void buildHelper(final Reference ref, final BuildState sb) {
				final String text = "vv" + ref.text;
				sb.appendText(text, false);
			}
		},
		MEMBER {
			@Override
			public void buildHelper(final Reference ref, final @NotNull BuildState sb) {
				final String text = "->vm" + ref.text;

				final StringBuilder sb1 = new StringBuilder();

				sb1.append(text);
				if (ref.value != null) {
					sb1.append(" = ");
					sb1.append(ref.value);
					sb1.append(";");
				}

				sb.appendText(sb1.toString(), false);
			}
		},
		PROPERTY_GET {
			@Override
			public void buildHelper(final Reference ref, final BuildState sb) {
				final String text;
				final String s = sb.toString();
				text    = String.format("%s%s)", ref.text, s);
				sb.open = false;
//				if (!s.equals(""))
				sb.needs_comma = false;
				sb.appendText(text, true);
			}
		},
		PROPERTY_SET {
			@Override
			public void buildHelper(final Reference ref, final BuildState sb) {
				final String text;
				final String s = sb.toString();
				text    = String.format("%s%s, %s);", ref.text, s, ref.value);
				sb.open = false;
//				if (!s.equals(""))
				sb.needs_comma = false;
				sb.appendText(text, true);
			}
		},
		INLINE_MEMBER {
			@Override
			public void buildHelper(final Reference ref, final BuildState sb) {
				final String text = Emit.emit("/*219*/") + ".vm" + ref.text;
				sb.appendText(text, false);
			}
		},
		CONSTRUCTOR {
			@Override
			public void buildHelper(final Reference ref, final BuildState sb) {
				final String          text;
				final @NotNull String s = sb.toString();
				text    = String.format("%s(%s", ref.text, s);
				sb.open = false;
				if (!s.equals("")) sb.needs_comma = true;
				sb.appendText(text + ")", true);
			}
		},
		DIRECT_MEMBER {
			@Override
			public void buildHelper(final Reference ref, final @NotNull BuildState sb) {
				final String text;
				text = Emit.emit("/*124*/") + "vsc->vm" + ref.text;

				final StringBuilder sb1 = new StringBuilder();

				sb1.append(text);
				if (ref.value != null) {
					sb1.append(" = ");
					sb1.append(ref.value);
					sb1.append(";");
				}

				sb.appendText(sb1.toString(), false);
			}
		},
		LITERAL {
			@Override
			public void buildHelper(final Reference ref, final BuildState sb) {
				final String text = ref.text;
				sb.appendText(text, false);
			}
		},
		FUNCTION {
			@Override
			public void buildHelper(final Reference ref, final BuildState sb) {
				final String text;
				final String s = sb.toString();
				text    = String.format("%s(%s", ref.text, s);
				sb.open = true;
				if (!s.equals("")) sb.needs_comma = true;
				sb.appendText(text, true);
			}
		};

		public abstract void buildHelper(final Reference ref, final BuildState sb);
	}

	interface XXX_YYY {
		static StringBuilder dispatch(@NotNull final ProcTableEntry pte, final XXX_YYY xy) {
			if (pte.expression_num == null) {
				return xy.itsABoy(pte.expression);
			} else {
				return xy.itsAGirl(pte.expression_num);
			}
		}

		StringBuilder itsABoy(IExpression expression);

		StringBuilder itsAGirl(InstructionArgument expreesion_num);
	}

	static class Reference {
		final String text;
		final Ref    type;
		final String value;

		public Reference(final String aText, final Ref aType, final String aValue) {
			text  = aText;
			type  = aType;
			value = aValue;
		}

		public Reference(final String aText, final Ref aType) {
			text  = aText;
			type  = aType;
			value = null;
		}

		public void buildHelper(final BuildState st) {
			type.buildHelper(this, st);
		}
	}

	private static class CReference_getIdentIAPath_IdentIAHelper {
		private final InstructionArgument   ia_next;
		private final List<String>          sl;
		private final int                   i;
		private final int                   sSize;
		private final OS_Element            resolved_element;
		private final BaseGeneratedFunction generatedFunction;
		private final GeneratedNode         resolved;
		private final String                value;


		public int code;


		private CReference_getIdentIAPath_IdentIAHelper(final InstructionArgument ia_next, final List<String> sl, final int i, final int sSize, final OS_Element resolved_element, final BaseGeneratedFunction generatedFunction, final GeneratedNode aResolved, final String aValue) {
			this.ia_next           = ia_next;
			this.sl                = sl;
			this.i                 = i;
			this.sSize             = sSize;
			this.resolved_element  = resolved_element;
			this.generatedFunction = generatedFunction;
			resolved               = aResolved;
			value                  = aValue;
		}

		boolean action(final CReference aCReference) {
			boolean b = false;
			if (getResolved_element() instanceof ClassStatement) {
				b = _act_ClassStatement(aCReference, b);
			} else if (getResolved_element() instanceof ConstructorDef) {
				_act_ConstructorDef(aCReference);
			} else if (getResolved_element() instanceof FunctionDef) {
				_act_FunctionDef(aCReference);
			} else if (getResolved_element() instanceof DefFunctionDef) {
				_act_DefFunctionDef(aCReference);
			} else if (getResolved_element() instanceof VariableStatement) {
				_act_VariableStatement(aCReference);
			} else if (getResolved_element() instanceof PropertyStatement) {
				_act_PropertyStatement(aCReference);
			} else if (getResolved_element() instanceof AliasStatement) {
				_act_AliasStatement();
			} else {
				//						text = idte.getIdent().getText();
				tripleo.elijah.util.Stupidity.println_out("1008 " + getResolved_element().getClass().getName());
				throw new NotImplementedException();
			}
			return b;
		}

		@Contract(pure = true)
		public InstructionArgument getIa_next() {
			return ia_next;
		}

		@Contract(pure = true)
		public List<String> getSl() {
			return sl;
		}

		@Contract(pure = true)
		public int getI() {
			return i;
		}

		@Contract(pure = true)
		public int getsSize() {
			return sSize;
		}

		@Contract(pure = true)
		public OS_Element getResolved_element() {
			return resolved_element;
		}

		@Contract(pure = true)
		public BaseGeneratedFunction getGeneratedFunction() {
			return generatedFunction;
		}

		@Contract(pure = true)
		public GeneratedNode getResolved() {
			return resolved;
		}

		@Contract(pure = true)
		public String getValue() {
			return value;
		}

		private boolean _act_ClassStatement(final CReference aCReference, boolean b) {
			// Assuming constructor call
			final int code;
			if (getResolved() != null) {
				code = ((GeneratedContainerNC) getResolved()).getCode();
			} else {
				code = -1;
				tripleo.elijah.util.Stupidity.println_err("** 31116 not resolved " + getResolved_element());
			}
			// README might be calling reflect or Type or Name
			// TODO what about named constructors -- should be called with construct keyword
			if (getIa_next() instanceof IdentIA) {
				final IdentTableEntry ite  = ((IdentIA) getIa_next()).getEntry();
				final String          text = ite.getIdent().getText();
				if (text.equals("reflect")) {
					b = true;
					final String text2 = String.format("ZS%d_reflect", code);
					aCReference.addRef(text2, Ref.FUNCTION);
				} else if (text.equals("Type")) {
					b = true;
					final String text2 = String.format("ZST%d", code); // return a TypeInfo structure
					aCReference.addRef(text2, Ref.FUNCTION);
				} else if (text.equals("Name")) {
					b = true;
					final String text2 = String.format("ZSN%d", code);
					aCReference.addRef(text2, Ref.FUNCTION); // TODO make this not a function
				} else {
					assert getI() == getsSize() - 1; // Make sure we are ending with a constructor call
					// README Assuming this is for named constructors
					final String text2 = String.format("ZC%d%s", code, text);
					aCReference.addRef(text2, Ref.CONSTRUCTOR);
				}
			} else {
				assert getI() == getsSize() - 1; // Make sure we are ending with a constructor call
				final String text2 = String.format("ZC%d", code);
				aCReference.addRef(text2, Ref.CONSTRUCTOR);
			}
			return b;
		}

		private void _act_ConstructorDef(final CReference aCReference) {
			assert getI() == getsSize() - 1; // Make sure we are ending with a constructor call
			final int code;
			if (getResolved() != null) {
				code = ((BaseGeneratedFunction) getResolved()).getCode();
			} else {
				code = -1;
				tripleo.elijah.util.Stupidity.println_err("** 31161 not resolved " + getResolved_element());
			}
			// README Assuming this is for named constructors
			final String text  = ((ConstructorDef) getResolved_element()).name();
			final String text2 = String.format("ZC%d%s", code, text);
			aCReference.addRef(text2, Ref.CONSTRUCTOR);
		}

		private void _act_FunctionDef(final CReference aCReference) {
			final OS_Element parent = getResolved_element().getParent();
			int              code   = -1;
			if (getResolved() != null) {
				if (getResolved() instanceof BaseGeneratedFunction) {
					((BaseGeneratedFunction) getResolved()).onGenClass(gc -> {
//						GeneratedNode gc = rf.getGenClass();
						if (gc instanceof GeneratedContainerNC) // and not another function
							this.code = ((GeneratedContainerNC) gc).getCode();
						else
							this.code = -2;
					});
				} else if (getResolved() instanceof GeneratedClass) {
					final GeneratedClass generatedClass = (GeneratedClass) getResolved();
					this.code = generatedClass.getCode();
				}
			}
			// TODO what about overloaded functions
			assert getI() == getsSize() - 1; // Make sure we are ending with a ProcedureCall
			getSl().clear();

			code = this.code;

			if (code == -1) {
//				text2 = String.format("ZT%d_%d", enclosing_function._a.getCode(), closure_index);
			}
			final String text2 = String.format("Z%d%s", code, ((FunctionDef) getResolved_element()).name());
			aCReference.addRef(text2, Ref.FUNCTION);
		}

		private void _act_DefFunctionDef(final CReference aCReference) {
			final OS_Element parent = getResolved_element().getParent();
			final int        code;
			if (getResolved() != null) {
				assert getResolved() instanceof BaseGeneratedFunction;
				final BaseGeneratedFunction rf = (BaseGeneratedFunction) getResolved();
				final GeneratedNode         gc = rf.getGenClass();
				if (gc instanceof GeneratedContainerNC) // and not another function
					code = ((GeneratedContainerNC) gc).getCode();
				else
					code = -2;
			} else {
				if (parent instanceof ClassStatement) {
					code = ((ClassStatement) parent)._a.getCode();
				} else if (parent instanceof NamespaceStatement) {
					code = ((NamespaceStatement) parent)._a.getCode();
				} else {
					// TODO what about FunctionDef, etc
					code = -1;
				}
			}
			// TODO what about overloaded functions
			assert getI() == getsSize() - 1; // Make sure we are ending with a ProcedureCall
			getSl().clear();
			if (code == -1) {
//				text2 = String.format("ZT%d_%d", enclosing_function._a.getCode(), closure_index);
			}
			final DefFunctionDef defFunctionDef = (DefFunctionDef) getResolved_element();
			final String         text2          = String.format("Z%d%s", code, defFunctionDef.name());
			aCReference.addRef(text2, Ref.FUNCTION);
		}

		private void _act_VariableStatement(final CReference aCReference) {
			// first getParent is VariableSequence
			final String text2 = ((VariableStatement) getResolved_element()).getName();
			if (getResolved_element().getParent().getParent() == getGeneratedFunction().getFD().getParent()) {
				// A direct member value. Doesn't handle when indirect
//				text = Emit.emit("/*124*/")+"vsc->vm" + text2;
				aCReference.addRef(text2, Ref.DIRECT_MEMBER, getValue());
			} else {
				final OS_Element parent = getResolved_element().getParent().getParent();
				if (parent == getGeneratedFunction().getFD()) {
					aCReference.addRef(text2, Ref.LOCAL);
				} else {
//					if (parent instanceof NamespaceStatement) {
//						int y=2;
//					}
					aCReference.addRef(text2, Ref.MEMBER, getValue());
				}
			}
		}

		private void _act_PropertyStatement(final CReference aCReference) {
			final OS_Element parent = getResolved_element().getParent();
			final int        code;
			if (parent instanceof ClassStatement) {
				code = ((ClassStatement) parent)._a.getCode();
			} else if (parent instanceof NamespaceStatement) {
				code = ((NamespaceStatement) parent)._a.getCode();
			} else {
//							code = -1;
				throw new IllegalStateException("PropertyStatement cant have other parent than ns or cls. " + getResolved_element().getClass().getName());
			}
			getSl().clear();  // don't we want all the text including from sl?
			//			if (text.equals("")) text = "vsc";
			//			text = String.format("ZP%dget_%s(%s)", code, ((PropertyStatement) resolved_element).name(), text); // TODO Don't know if get or set!
			final String text2 = String.format("ZP%dget_%s", code, ((PropertyStatement) getResolved_element()).name()); // TODO Don't know if get or set!
			aCReference.addRef(text2, Ref.PROPERTY_GET);
		}

		@Contract(pure = true)
		private static void _act_AliasStatement() {
			final int y = 2;
			NotImplementedException.raise();
			//			text = Emit.emit("/*167*/")+((AliasStatement)resolved_element).name();
			//			return _getIdentIAPath_IdentIAHelper(text, sl, i, sSize, _res)
		}
	}

	private final static class BuildState {
		StringBuilder sb   = new StringBuilder();
		boolean       open = false, needs_comma = false;

		public void appendText(final String text, final boolean erase) {
			if (erase)
				sb = new StringBuilder();

			sb.append(text);
		}

		@Override
		public String toString() {
			return sb.toString();
		}
		//ABOVE 3a
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
