/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.jdeferred2.DoneCallback;
import org.jdeferred2.Promise;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.contexts.ClassContext;
import tripleo.elijah.contexts.FunctionContext;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.lang2.SpecialFunctions;
import tripleo.elijah.lang2.SpecialVariables;
import tripleo.elijah.stages.deduce.declarations.DeferredMember;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.ConstTableIA;
import tripleo.elijah.stages.instructions.FnCallArgs;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.Instruction;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.InstructionName;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.ProcIA;
import tripleo.elijah.stages.instructions.VariableTableType;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.work.WorkJob;
import tripleo.elijah.work.WorkList;
import tripleo.elijah.work.WorkManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created 9/15/20 12:51 PM
 */
public class DeduceTypes2 {
	private static final String PHASE = "DeduceTypes2";
	private final OS_Module module;
	private final DeducePhase phase;
	private final ErrSink errSink;
	final ElLog LOG;
	WorkManager wm = new WorkManager();

	public DeduceTypes2(OS_Module module, DeducePhase phase) {
		this(module, phase, ElLog.Verbosity.VERBOSE);
	}

	public DeduceTypes2(OS_Module module, DeducePhase phase, ElLog.Verbosity verbosity) {
		this.module = module;
		this.phase = phase;
		this.errSink = module.getCompilation().getErrSink();
		this.LOG = new ElLog(module.getFileName(), verbosity, PHASE);
		//
		phase.addLog(LOG);
	}

	public void deduceFunctions(final @NotNull Iterable<GeneratedNode> lgf) {
		for (final GeneratedNode generatedNode : lgf) {
			if (generatedNode instanceof GeneratedFunction) {
				GeneratedFunction generatedFunction = (GeneratedFunction) generatedNode;
				deduceOneFunction(generatedFunction, phase);
			}
		}
		List<GeneratedNode> generatedClasses = new ArrayList<GeneratedNode>(phase.generatedClasses);
		int size;
		do {
			size = df_helper(generatedClasses, new dfhi_functions());
			generatedClasses = new ArrayList<GeneratedNode>(phase.generatedClasses);
		} while (size > 0);
		do {
			size = df_helper(generatedClasses, new dfhi_constructors());
			generatedClasses = new ArrayList<GeneratedNode>(phase.generatedClasses);
		} while (size > 0);
	}

	/**
	 * Deduce functions or constructors contained in classes list
	 *
	 * @param aGeneratedClasses assumed to be a list of {@link GeneratedContainerNC}
	 * @param dfhi specifies what to select for:<br>
	 *             {@link dfhi_functions} will select all functions from {@code functionMap}, and <br>
	 *             {@link dfhi_constructors} will select all constructors from {@code constructors}.
	 * @param <T> generic parameter taken from {@code dfhi}
	 * @return the number of deduced functions or constructors, or 0
	 */
	<T> int df_helper(List<GeneratedNode> aGeneratedClasses, df_helper_i<T> dfhi) {
		int size = 0;
		for (GeneratedNode generatedNode : aGeneratedClasses) {
			GeneratedContainerNC generatedContainerNC = (GeneratedContainerNC) generatedNode;
			final df_helper<T> dfh = dfhi.get(generatedContainerNC);
			if (dfh == null) continue;
			Collection<T> lgf2 = dfh.collection();
			for (final T generatedConstructor : lgf2) {
				if (dfh.deduce(generatedConstructor))
					size++;
			}
		}
		return size;
	}

	interface df_helper_i<T> {
		df_helper<T> get(GeneratedContainerNC generatedClass);
	}

	class dfhi_constructors implements df_helper_i<GeneratedConstructor> {
		@Override
		public df_helper_Constructors get(GeneratedContainerNC aGeneratedContainerNC) {
			if (aGeneratedContainerNC instanceof GeneratedClass) // TODO namespace constructors
				return new df_helper_Constructors((GeneratedClass) aGeneratedContainerNC);
			else
				return null;
		}
	}

	class dfhi_functions implements df_helper_i<GeneratedFunction> {
		@Override
		public df_helper_Functions get(GeneratedContainerNC aGeneratedContainerNC) {
			return new df_helper_Functions(aGeneratedContainerNC);
		}
	}

	interface df_helper<T> {
		Collection<T> collection();

		boolean deduce(T generatedConstructor);
	}

	class df_helper_Constructors implements df_helper<GeneratedConstructor> {
		private final GeneratedClass generatedClass;

		public df_helper_Constructors(GeneratedClass aGeneratedClass) {
			generatedClass = aGeneratedClass;
		}

		@Override
		public Collection<GeneratedConstructor> collection() {
			return generatedClass.constructors.values();
		}

		@Override
		public boolean deduce(GeneratedConstructor generatedConstructor) {
			return deduceOneConstructor(generatedConstructor, phase);
		}
	}

	class df_helper_Functions implements df_helper<GeneratedFunction> {
		private final GeneratedContainerNC generatedContainerNC;

		public df_helper_Functions(GeneratedContainerNC aGeneratedContainerNC) {
			generatedContainerNC = aGeneratedContainerNC;
		}

		@Override
		public Collection<GeneratedFunction> collection() {
			return generatedContainerNC.functionMap.values();
		}

		@Override
		public boolean deduce(GeneratedFunction aGeneratedFunction) {
			return deduceOneFunction(aGeneratedFunction, phase);
		}
	}
	
	public boolean deduceOneFunction(GeneratedFunction aGeneratedFunction, DeducePhase aDeducePhase) {
		if (aGeneratedFunction.deducedAlready) return false;
		deduce_generated_function(aGeneratedFunction);
		aGeneratedFunction.deducedAlready = true;
		for (IdentTableEntry identTableEntry : aGeneratedFunction.idte_list) {
			if (identTableEntry.resolved_element instanceof VariableStatement) {
				final VariableStatement vs = (VariableStatement) identTableEntry.resolved_element;
				OS_Element el = vs.getParent().getParent();
				OS_Element el2 = aGeneratedFunction.getFD().getParent();
				if (el != el2) {
					if (el instanceof ClassStatement || el instanceof NamespaceStatement)
						// NOTE there is no concept of gf here
						aDeducePhase.registerResolvedVariable(identTableEntry, el, vs.getName());
				}
			}
		}
		{
			final GeneratedFunction gf = aGeneratedFunction;

			@Nullable InstructionArgument result_index = gf.vte_lookup("Result");
			if (result_index == null) {
				// if there is no Result, there should be Value
				result_index = gf.vte_lookup("Value");
				// but Value might be passed in. If it is, discard value
				if (result_index != null) {
					@NotNull VariableTableEntry vte = ((IntegerIA) result_index).getEntry();
					if (vte.vtt != VariableTableType.RESULT) {
						result_index = null;
					}
				}
			}
			if (result_index != null) {
				@NotNull VariableTableEntry vte = ((IntegerIA) result_index).getEntry();
				if (vte.resolvedType() == null) {
					GenType b = vte.genType;
					OS_Type a = vte.type.getAttached();
					if (a != null) {
						// see resolve_function_return_type
						switch (a.getType()) {
							case USER_CLASS:
								dof_uc(vte, a);
								break;
							case USER:
								vte.genType.typeName = a;
								try {
									@NotNull OS_Type rt = resolve_type(a, a.getTypeName().getContext());
									if (rt.getClassOf().getGenericPart().size() > 0)
										vte.genType.nonGenericTypeName = a.getTypeName(); // TODO might be wrong
									dof_uc(vte, rt);
								} catch (ResolveError aResolveError) {
									errSink.reportDiagnostic(aResolveError);
								}
								break;
							default:
								// TODO do nothing for now
								int y3 = 2;
								break;
						}
					} /*else
							throw new NotImplementedException();*/
				}
			}
		}
		aDeducePhase.addFunction(aGeneratedFunction, (FunctionDef) aGeneratedFunction.getFD());
		return true;
	}

	public boolean deduceOneConstructor(GeneratedConstructor aGeneratedConstructor, DeducePhase aDeducePhase) {
		if (aGeneratedConstructor.deducedAlready) return false;
		deduce_generated_function_base(aGeneratedConstructor, aGeneratedConstructor.getFD());
		aGeneratedConstructor.deducedAlready = true;
		for (IdentTableEntry identTableEntry : aGeneratedConstructor.idte_list) {
			if (identTableEntry.resolved_element instanceof VariableStatement) {
				final VariableStatement vs = (VariableStatement) identTableEntry.resolved_element;
				OS_Element el = vs.getParent().getParent();
				OS_Element el2 = aGeneratedConstructor.getFD().getParent();
				if (el != el2) {
					if (el instanceof ClassStatement || el instanceof NamespaceStatement)
						// NOTE there is no concept of gf here
						aDeducePhase.registerResolvedVariable(identTableEntry, el, vs.getName());
				}
			}
		}
		{
			final GeneratedConstructor gf = aGeneratedConstructor;

			@Nullable InstructionArgument result_index = gf.vte_lookup("Result");
			if (result_index == null) {
				// if there is no Result, there should be Value
				result_index = gf.vte_lookup("Value");
				// but Value might be passed in. If it is, discard value
				if (result_index != null) {
					@NotNull VariableTableEntry vte = ((IntegerIA) result_index).getEntry();
					if (vte.vtt != VariableTableType.RESULT) {
						result_index = null;
					}
				}
			}
			if (result_index != null) {
				@NotNull VariableTableEntry vte = ((IntegerIA) result_index).getEntry();
				if (vte.resolvedType() == null) {
					GenType b = vte.genType;
					OS_Type a = vte.type.getAttached();
					if (a != null) {
						// see resolve_function_return_type
						switch (a.getType()) {
							case USER_CLASS:
								dof_uc(vte, a);
								break;
							case USER:
								b.typeName = a;
								try {
									@NotNull OS_Type rt = resolve_type(a, a.getTypeName().getContext());
									if (rt.getClassOf().getGenericPart().size() > 0)
										b.nonGenericTypeName = a.getTypeName(); // TODO might be wrong
									dof_uc(vte, rt);
								} catch (ResolveError aResolveError) {
									errSink.reportDiagnostic(aResolveError);
								}
								break;
							default:
								// TODO do nothing for now
								int y3 = 2;
								break;
						}
					} /*else
							throw new NotImplementedException();*/
				}
			}
		}
//		aDeducePhase.addFunction(aGeneratedConstructor, (FunctionDef) aGeneratedConstructor.getFD()); // TODO do we need this?
		return true;
	}

	private void dof_uc(@NotNull VariableTableEntry aVte, OS_Type aA) {
		// we really want a ci from somewhere
		assert aA.getClassOf().getGenericPart().size() == 0;
		ClassInvocation ci = new ClassInvocation(aA.getClassOf(), null);
		ci = phase.registerClassInvocation(ci);

		aVte.genType.resolved = aA; // README assuming OS_Type cannot represent namespaces
		aVte.genType.ci = ci;

		ci.resolvePromise().done(new DoneCallback<GeneratedClass>() {
			@Override
			public void onDone(GeneratedClass result) {
				aVte.resolveType(result);
			}
		});
	}

	List<Runnable> onRunnables = new ArrayList<Runnable>();

	void onFinish(Runnable r) {
		onRunnables.add(r);
	}

	public void deduce_generated_constructor(final GeneratedFunction generatedFunction) {
		final ConstructorDef fd = (ConstructorDef) generatedFunction.getFD();
		deduce_generated_function_base(generatedFunction, fd);
	}

	public void deduce_generated_function(final GeneratedFunction generatedFunction) {
		final FunctionDef fd = (FunctionDef) generatedFunction.getFD();
		deduce_generated_function_base(generatedFunction, fd);
	}

	public void deduce_generated_function_base(final BaseGeneratedFunction generatedFunction, BaseFunctionDef fd) {
		final Context fd_ctx = fd.getContext();
		//
		{
			ProcTableEntry pte = generatedFunction.fi.pte;
			final String pte_string = getPTEString(pte);
			LOG.err("** deduce_generated_function "+ fd.name()+" "+pte_string);//+" "+((OS_Container)((FunctionDef)fd).getParent()).name());
		}
		//
		//
		for (final Instruction instruction : generatedFunction.instructions()) {
			final Context context = generatedFunction.getContextFromPC(instruction.getIndex());
//			LOG.info("8006 " + instruction);
			switch (instruction.getName()) {
			case E:
				{
					//
					// resolve all cte expressions
					//
					for (final ConstantTableEntry cte : generatedFunction.cte_list) {
						resolve_cte_expression(cte, context);
					}
					//
					// add proc table listeners
					//
					add_proc_table_listeners(generatedFunction);
					//
					// resolve ident table
					//
					for (IdentTableEntry ite : generatedFunction.idte_list) {
						ite.resolveExpectation = promiseExpectation(ite, "Element Resolved");
						resolve_ident_table_entry(ite, generatedFunction, context);
					}
					//
					// resolve arguments table
					//
					for (VariableTableEntry vte : generatedFunction.vte_list) {
						resolve_arguments_table_entry(vte, generatedFunction, context);
					}
				}
				break;
			case X:
				{
					//
					// resolve var table. moved from `E'
					//
					for (VariableTableEntry vte : generatedFunction.vte_list) {
						resolve_var_table_entry(vte, generatedFunction, context);
					}
					for (Runnable runnable : onRunnables) {
						runnable.run();
					}
//					LOG.info("167 "+generatedFunction);
					//
					// ATTACH A TYPE TO VTE'S
					// CONVERT USER TYPES TO USER_CLASS TYPES
					//
					for (final VariableTableEntry vte : generatedFunction.vte_list) {
//						LOG.info("704 "+vte.type.attached+" "+vte.potentialTypes());
						final OS_Type attached = vte.type.getAttached();
						if (attached != null && attached.getType() == OS_Type.Type.USER) {
							final TypeName x = attached.getTypeName();
							if (x instanceof NormalTypeName) {
								final String tn = ((NormalTypeName) x).getName();
								final LookupResultList lrl = x.getContext().lookup(tn);
								OS_Element best = lrl.chooseBest(null);
								if (best != null) {
									while (best instanceof AliasStatement) {
										best = DeduceLookupUtils._resolveAlias((AliasStatement) best, this);
									}
									if (!(OS_Type.isConcreteType(best))) {
										errSink.reportError(String.format("Not a concrete type %s for (%s)", best, tn));
									} else {
	//									LOG.info("705 " + best);
										// NOTE that when we set USER_CLASS from USER generic information is
										// still contained in constructable_pte
										GenType genType = new GenType();
										genType.typeName = attached;
										genType.resolved = new OS_Type((ClassStatement) best);
//										genType.copy(vte.type.genType);
										genType.ci = genCI(genType, x);
										vte.type.genType.copy(genType);
										// set node when available
										((ClassInvocation) vte.type.genType.ci).resolvePromise().done(new DoneCallback<GeneratedClass>() {
											@Override
											public void onDone(GeneratedClass result) {
												vte.type.genType.node = result;
												vte.resolveType(result);
											}
										});
									}
									//vte.el = best;
									// NOTE we called resolve_var_table_entry above
									LOG.err("200 "+best);
									if (vte.el != null)
										assert vte.getStatus() == BaseTableEntry.Status.KNOWN;
//									vte.setStatus(BaseTableEntry.Status.KNOWN, best/*vte.el*/);
								} else {
									errSink.reportDiagnostic(new ResolveError(x, lrl));
								}
							}
						}
					}
					for (final VariableTableEntry vte : generatedFunction.vte_list) {
						if (vte.vtt == VariableTableType.ARG) {
							final OS_Type attached = vte.type.getAttached();
							if (attached != null) {
								if (attached.getType() == OS_Type.Type.USER)
									//throw new AssertionError();
									errSink.reportError("369 ARG USER type (not deduced) "+vte);
							} else {
								errSink.reportError("457 ARG type not deduced/attached "+vte);
							}
						}
					}
					//
					// ATTACH A TYPE TO IDTE'S
					//
					for (IdentTableEntry ite : generatedFunction.idte_list) {
						int y=2;
						assign_type_to_idte(ite, generatedFunction, fd_ctx, context);
					}
					{
						// TODO why are we doing this?
//						Resolve_each_typename ret = new Resolve_each_typename(phase, this, errSink);
//						for (TypeTableEntry typeTableEntry : generatedFunction.tte_list) {
//							ret.action(typeTableEntry);
//						}
					}
					{
						final WorkManager workManager = wm;//new WorkManager();
						Dependencies deps = new Dependencies(/*phase, this, errSink*/);
						for (GenType genType : generatedFunction.dependentTypes()) {
							deps.action_type(genType, workManager);
						}
						for (FunctionInvocation dependentFunction : generatedFunction.dependentFunctions()) {
							deps.action_function(dependentFunction, workManager);
						}
						workManager.drain();
					}
					//
					// RESOLVE FUNCTION RETURN TYPES
					//
					resolve_function_return_type(generatedFunction);
					//
					// LOOKUP FUNCTIONS
					//
					{
						Lookup_function_on_exit lfoe = new Lookup_function_on_exit();
						for (ProcTableEntry pte : generatedFunction.prte_list) {
							lfoe.action(pte);
						}
						wm.drain();
					}

					expectations.check();
				}
				break;
			case ES:
				break;
			case XS:
				break;
			case AGN:
				{ // TODO doesn't account for __assign__
					final InstructionArgument agn_lhs = instruction.getArg(0);
					if (agn_lhs instanceof IntegerIA) {
						final IntegerIA arg = (IntegerIA) agn_lhs;
						final VariableTableEntry vte = generatedFunction.getVarTableEntry(arg.getIndex());
						final InstructionArgument i2 = instruction.getArg(1);
						if (i2 instanceof IntegerIA) {
							final VariableTableEntry vte2 = generatedFunction.getVarTableEntry(to_int(i2));
							vte.addPotentialType(instruction.getIndex(), vte2.type);
						} else if (i2 instanceof FnCallArgs) {
							final FnCallArgs fca = (FnCallArgs) i2;
							do_assign_call(generatedFunction, context, vte, fca, instruction);
						} else if (i2 instanceof ConstTableIA) {
							do_assign_constant(generatedFunction, instruction, vte, (ConstTableIA) i2);
						} else if (i2 instanceof IdentIA) {
							IdentTableEntry idte = generatedFunction.getIdentTableEntry(to_int(i2));
							assert idte.type != null;
							assert idte.resolved_element != null;
							vte.addPotentialType(instruction.getIndex(), idte.type);
						} else if (i2 instanceof ProcIA) {
							throw new NotImplementedException();
						} else
							throw new NotImplementedException();
					} else if (agn_lhs instanceof IdentIA) {
						final IdentIA arg = (IdentIA) agn_lhs;
						final IdentTableEntry idte = arg.getEntry();
						final InstructionArgument i2 = instruction.getArg(1);
						if (i2 instanceof IntegerIA) {
							final VariableTableEntry vte2 = generatedFunction.getVarTableEntry(to_int(i2));
							idte.addPotentialType(instruction.getIndex(), vte2.type);
						} else if (i2 instanceof FnCallArgs) {
							final FnCallArgs fca = (FnCallArgs) i2;
							do_assign_call(generatedFunction, fd_ctx, idte, fca, instruction.getIndex());
						} else if (i2 instanceof IdentIA) {
							IdentTableEntry idte2 = generatedFunction.getIdentTableEntry(to_int(i2));
							if (idte2.type == null) {
								idte2.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, idte2.getIdent(), idte2);
							}
							LookupResultList lrl1 = fd_ctx.lookup(idte2.getIdent().getText());
							OS_Element best1 = lrl1.chooseBest(null);
							if (best1 != null) {
								idte2.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best1));
								// TODO check for elements which may contain type information
								if (best1 instanceof VariableStatement) {
									final VariableStatement vs = (VariableStatement) best1;
									DeferredMember dm = deferred_member(vs.getParent().getParent(), null, vs, idte2);
									dm.typePromise().done(new DoneCallback<GenType>() {
											@Override
											public void onDone(GenType result) {
												assert result.resolved != null;
												idte2.type.setAttached(result.resolved);
											}
										});
								}
							} else {
								idte2.setStatus(BaseTableEntry.Status.UNKNOWN, null);
								LOG.err("242 Bad lookup" + idte2.getIdent().getText());
							}
							idte.addPotentialType(instruction.getIndex(), idte2.type);
						} else if (i2 instanceof ConstTableIA) {
							do_assign_constant(generatedFunction, instruction, idte, (ConstTableIA) i2);
						} else if (i2 instanceof ProcIA) {
							throw new NotImplementedException();
						} else
							throw new NotImplementedException();
					}
				}
				break;
			case AGNK:
				{
					final IntegerIA arg = (IntegerIA)instruction.getArg(0);
					final VariableTableEntry vte = generatedFunction.getVarTableEntry(arg.getIndex());
					final InstructionArgument i2 = instruction.getArg(1);
					final ConstTableIA ctia = (ConstTableIA) i2;
					do_assign_constant(generatedFunction, instruction, vte, ctia);
				}
				break;
			case AGNT:
				break;
			case AGNF:
				LOG.info("292 Encountered AGNF");
				break;
			case JE:
				LOG.info("296 Encountered JE");
				break;
			case JNE:
				break;
			case JL:
				break;
			case JMP:
				break;
			case CALL: {
				final int pte_num = ((ProcIA)instruction.getArg(0)).getIndex();
				final ProcTableEntry pte = generatedFunction.getProcTableEntry(pte_num);
//				final InstructionArgument i2 = (instruction.getArg(1));
				{
					final IdentIA identIA = (IdentIA) pte.expression_num;
					final String x = generatedFunction.getIdentIAPathNormal(identIA);
					LOG.info("298 Calling "+x);
					resolveIdentIA_(context, identIA, generatedFunction, new FoundElement(phase) {

						@SuppressWarnings("unused") final String xx = x;

						@Override
						public void foundElement(OS_Element e) {
							pte.setStatus(BaseTableEntry.Status.KNOWN, new ConstructableElementHolder(e, identIA));
							if (fd instanceof DefFunctionDef) {
								final IInvocation invocation = getInvocation((GeneratedFunction) generatedFunction);
								forFunction(newFunctionInvocation((FunctionDef) e, pte, invocation, phase), new ForFunction() {
									@Override
									public void typeDecided(GenType aType) {
										@Nullable InstructionArgument x = generatedFunction.vte_lookup("Result");
										assert x != null;
										((IntegerIA) x).getEntry().type.setAttached(gt(aType));
									}
								});
							}
						}

						@Override
						public void noFoundElement() {
							errSink.reportError("370 Can't find callsite "+x);
							// TODO don't know if this is right
							@NotNull IdentTableEntry entry = identIA.getEntry();
							if (entry.getStatus() != BaseTableEntry.Status.UNKNOWN)
								entry.setStatus(BaseTableEntry.Status.UNKNOWN, null);
						}
					});
				}
			}
			break;
			case CALLS: {
				final int i1 = to_int(instruction.getArg(0));
				final InstructionArgument i2 = (instruction.getArg(1));
				final ProcTableEntry fn1 = generatedFunction.getProcTableEntry(i1);
				{
					implement_calls(generatedFunction, fd_ctx, i2, fn1, instruction.getIndex());
				}
/*
				if (i2 instanceof IntegerIA) {
					int i2i = to_int(i2);
					VariableTableEntry vte = generatedFunction.getVarTableEntry(i2i);
					int y =2;
				} else
					throw new NotImplementedException();
*/
			}
			break;
			case RET:
				break;
			case YIELD:
				break;
			case TRY:
				break;
			case PC:
				break;
			case CAST_TO:
				// README potentialType info is already added by MatchConditional
				break;
			case DECL:
				// README for GenerateC, etc: marks the spot where a declaration should go. Wouldn't be necessary if we had proper Range's
				break;
			case IS_A:
				break;
			case NOP:
				break;
			case CONSTRUCT:
				implement_construct(generatedFunction, instruction);
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + instruction.getName());
			}
		}
		for (final VariableTableEntry vte : generatedFunction.vte_list) {
			if (vte.type.getAttached() == null) {
				int potential_size = vte.potentialTypes().size();
				if (potential_size == 1)
					vte.type.setAttached(getPotentialTypesVte(vte).get(0).getAttached());
				else if (potential_size > 1) {
					// TODO Check type compatibility
					LOG.err("703 "+vte.getName()+" "+vte.potentialTypes());
					errSink.reportDiagnostic(new CantDecideType(vte, vte.potentialTypes()));
				} else {
					// potential_size == 0
					// Result is handled by phase.typeDecideds, self is always valid
					if (/*vte.getName() != null &&*/ !(vte.vtt == VariableTableType.RESULT || vte.vtt == VariableTableType.SELF))
						errSink.reportDiagnostic(new CantDecideType(vte, vte.potentialTypes()));
				}
			}
		}
		{
			//
			// NOW CALCULATE DEFERRED CALLS
			//
			for (final Integer deferred_call : generatedFunction.deferred_calls) {
				final Instruction instruction = generatedFunction.getInstruction(deferred_call);

				final int i1 = to_int(instruction.getArg(0));
				final InstructionArgument i2 = (instruction.getArg(1));
				final ProcTableEntry fn1 = generatedFunction.getProcTableEntry(i1);
				{
//					generatedFunction.deferred_calls.remove(deferred_call);
					implement_calls_(generatedFunction, fd_ctx, i2, fn1, instruction.getIndex());
				}
			}
		}
	}

	private void add_proc_table_listeners(BaseGeneratedFunction generatedFunction) {
		for (final ProcTableEntry pte : generatedFunction.prte_list) {
			pte.addStatusListener(new BaseTableEntry.StatusListener() {
				@Override
				public void onChange(final IElementHolder eh, final BaseTableEntry.Status newStatus) {
					Constructable co = null;
					if (eh instanceof ConstructableElementHolder) {
						final ConstructableElementHolder constructableElementHolder = (ConstructableElementHolder) eh;
						co = constructableElementHolder.getConstructable();
					}
					if (newStatus != BaseTableEntry.Status.UNKNOWN) { // means eh is null
						AbstractDependencyTracker depTracker;
						if (co instanceof IdentIA) {
							final IdentIA identIA = (IdentIA) co;
							depTracker = identIA.gf;
						} else if (co instanceof IntegerIA) {
							final IntegerIA integerIA = (IntegerIA) co;
							depTracker = integerIA.gf;
						} else
							depTracker = null;

						set_resolved_element_pte(co, eh.getElement(), pte, depTracker);
					}
				}

				class E_Is_FunctionDef {
					private ProcTableEntry pte;
					private FunctionDef fd;
					private OS_Element parent;
					private FunctionInvocation fi;
					private GenType genType;

					public E_Is_FunctionDef(ProcTableEntry pte, FunctionDef aFd, OS_Element aParent) {
						this.pte = pte;
						fd = aFd;
						parent = aParent;
					}

					public FunctionInvocation getFi() {
						return fi;
					}

					public GenType getGenType() {
						return genType;
					}

					public E_Is_FunctionDef invoke(TypeName typeName) {
						ClassInvocation ci;
						if (parent instanceof NamespaceStatement) {
							final NamespaceStatement namespaceStatement = (NamespaceStatement) parent;
							genType = new GenType(namespaceStatement);
							final NamespaceInvocation nsi = phase.registerNamespaceInvocation(namespaceStatement);
							//												  pte.setNamespaceInvocation(nsi);
							genType.ci = nsi;
							fi = newFunctionInvocation(fd, pte, nsi, phase);
						} else if (parent instanceof ClassStatement) {
							final ClassStatement classStatement = (ClassStatement) parent;
							genType = new GenType(classStatement);
//							ci = new ClassInvocation(classStatement, null);
//							ci = phase.registerClassInvocation(ci);
//							genType.ci = ci;
							ci = genCI(genType, typeName);
							pte.setClassInvocation(ci);
							fi = newFunctionInvocation(fd, pte, ci, phase);
						} else if (parent instanceof FunctionDef) {
							if (pte.expression_num == null) {
								// TODO need the instruction to get args from FnCallArgs
								fi = null;
							}
						} else
							throw new IllegalStateException("Unknown parent");
						if (fi != null)
							pte.setFunctionInvocation(fi);
						return this;
					}
				}

				void set_resolved_element_pte(final Constructable co,
											  final OS_Element e,
											  final ProcTableEntry pte,
											  final AbstractDependencyTracker depTracker) {
					ClassInvocation ci;
					FunctionInvocation fi;
					GenType genType = null;

//								pte.setResolvedElement(e); // README already done
					if (e instanceof ClassStatement) {
						ci = new ClassInvocation((ClassStatement) e, null);
						ci = phase.registerClassInvocation(ci);
						fi = newFunctionInvocation(ConstructorDef.defaultVirtualCtor, pte, ci, phase); // TODO might not be virtual ctor, so check
						pte.setFunctionInvocation(fi);

						if (co != null) {
							co.setConstructable(pte);
							ci.resolvePromise().done(new DoneCallback<GeneratedClass>() {
								@Override
								public void onDone(GeneratedClass result) {
									co.resolveType(result);
								}
							});
						}
					} else if (e instanceof FunctionDef) {
						FunctionDef fd = (FunctionDef) e;
						if (pte.expression_num != null) {
							DeducePath dp = ((IdentIA)pte.expression_num).getEntry().buildDeducePath(generatedFunction);

							if (dp.size() > 1) {
								@Nullable OS_Element el_self = dp.getElement(dp.size() - 2);

								final OS_Element parent = el_self;
								if (parent instanceof IdentExpression) {
									@Nullable InstructionArgument vte_ia = generatedFunction.vte_lookup(((IdentExpression) parent).getText());
									assert vte_ia != null;
									final VariableTableEntry variableTableEntry = ((IntegerIA) vte_ia).getEntry();
									variableTableEntry.typePromise().then(new DoneCallback<GenType>() {
										@Override
										public void onDone(GenType result) {
											assert result.resolved.getClassOf() == fd.getParent();

											E_Is_FunctionDef e_Is_FunctionDef = new E_Is_FunctionDef(pte, fd, fd.getParent()).invoke(variableTableEntry.type.genType.nonGenericTypeName);
											FunctionInvocation fi1 = e_Is_FunctionDef.getFi();
											GenType genType1 = e_Is_FunctionDef.getGenType();
											finish(co, depTracker, fi1, genType1);
										}
									});
								} else {
									E_Is_FunctionDef e_Is_FunctionDef = new E_Is_FunctionDef(pte, fd, parent).invoke(null);
									fi = e_Is_FunctionDef.getFi();
									if (fi != null) { // TODO
										genType = e_Is_FunctionDef.getGenType();
										finish(co, depTracker, fi, genType);
									}
								}
							} else {
								final OS_Element parent = fd.getParent();
								E_Is_FunctionDef e_Is_FunctionDef = new E_Is_FunctionDef(pte, fd, parent).invoke(null);
								fi = e_Is_FunctionDef.getFi();
								genType = e_Is_FunctionDef.getGenType();
								finish(co, depTracker, fi, genType);
							}
						} else {
							OS_Element parent = pte.getResolvedElement(); // for dunder methods

							assert parent != null;

							if (parent instanceof IdentExpression) {
								@Nullable InstructionArgument vte_ia = generatedFunction.vte_lookup(((IdentExpression) parent).getText());
								assert vte_ia != null;
								final VariableTableEntry variableTableEntry = ((IntegerIA) vte_ia).getEntry();
								variableTableEntry.typePromise().then(new DoneCallback<GenType>() {
									@Override
									public void onDone(GenType result) {
										assert result.resolved.getClassOf() == fd.getParent();

										E_Is_FunctionDef e_Is_FunctionDef = new E_Is_FunctionDef(pte, fd, fd.getParent()).invoke(variableTableEntry.type.genType.nonGenericTypeName);
										FunctionInvocation fi1 = e_Is_FunctionDef.getFi();
										GenType genType1 = e_Is_FunctionDef.getGenType();
										finish(co, depTracker, fi1, genType1);
									}
								});
							} else {
								TypeName typeName = null;

								if (fd == parent) {
									parent = fd.getParent();
									TypeTableEntry x = pte.getArgs().get(0);
									// TODO highly specialized condition...
									if (x.getAttached() == null && x.tableEntry == null) {
										String text = ((IdentExpression) x.expression).getText();
										@Nullable InstructionArgument vte_ia = generatedFunction.vte_lookup(text);
										GenType gt = ((IntegerIA) vte_ia).getEntry().type.genType;
										typeName = gt.nonGenericTypeName != null ? gt.nonGenericTypeName : gt.typeName.getTypeName();
									}
								}

								E_Is_FunctionDef e_Is_FunctionDef = new E_Is_FunctionDef(pte, fd, parent).invoke(typeName, null);
								fi = e_Is_FunctionDef.getFi();
								genType = e_Is_FunctionDef.getGenType();
								finish(co, depTracker, fi, genType);
							}
						}
					} else {
						LOG.err("845 Unknown element for ProcTableEntry "+e);
						return;
					}
				}

				private void finish(Constructable co, AbstractDependencyTracker depTracker, FunctionInvocation aFi, GenType aGenType) {
					if (co != null && aGenType != null)
						co.setGenType(aGenType);

					if (depTracker != null) {
						if (aGenType == null && aFi.getFunction() == null) {
							// README Assume constructor
							final ClassStatement c = aFi.getClassInvocation().getKlass();
							final GenType genType2 = new GenType(c);
							depTracker.addDependentType(genType2);
						} else {
							depTracker.addDependentFunction(aFi);
							if (aGenType != null)
								depTracker.addDependentType(aGenType);
						}
					}
				}
			});

			InstructionArgument en = pte.expression_num;
			if (en != null) {
				if (en instanceof IdentIA) {
					final IdentIA identIA = (IdentIA) en;
					@NotNull IdentTableEntry idte = identIA.getEntry();
					idte.addStatusListener(new BaseTableEntry.StatusListener() {
						@Override
						public void onChange(IElementHolder eh, BaseTableEntry.Status newStatus) {
							if (newStatus != BaseTableEntry.Status.KNOWN)
								return;

							final OS_Element el = eh.getElement();

							@NotNull ElObjectType type = DecideElObjectType.getElObjectType(el);

							switch (type) {
							case NAMESPACE:
								GenType genType = new GenType((NamespaceStatement) el);
								generatedFunction.addDependentType(genType);
								break;
							case CLASS:
								GenType genType2 = new GenType((ClassStatement) el);
								generatedFunction.addDependentType(genType2);
								break;
							case FUNCTION:
								IdentIA identIA2 = null;
								if (pte.expression_num instanceof IdentIA)
									identIA2 = (IdentIA) pte.expression_num;
								if (identIA2 != null) {
									@NotNull IdentTableEntry idte2 = identIA.getEntry();
									ProcTableEntry procTableEntry = idte2.getCallablePTE();
									if (procTableEntry == pte) System.err.println("940 procTableEntry == pte");
									if (procTableEntry != null) {
										// TODO doesn't seem like we need this
										procTableEntry.onFunctionInvocation(new DoneCallback<FunctionInvocation>() {
											@Override
											public void onDone(FunctionInvocation functionInvocation) {
												ClassInvocation ci = functionInvocation.getClassInvocation();
												NamespaceInvocation nsi = functionInvocation.getNamespaceInvocation();
												// do we register?? probably not
												if (ci == null && nsi == null)
													assert false;
												FunctionInvocation fi = newFunctionInvocation((FunctionDef) el, pte, ci != null ? ci : nsi, phase);

												{
													if (functionInvocation.getClassInvocation() == fi.getClassInvocation() &&
														functionInvocation.getFunction() == fi.getFunction() &&
														functionInvocation.pte == fi.pte)
														System.err.println("955 It seems like we are generating the same thing...");
													else {
														int ok=2;
													}

												}
												generatedFunction.addDependentFunction(fi);
											}
										});
										// END
									}
								}
								break;
							default:
								LOG.err(String.format("228 Don't know what to do %s %s", type, el));
								break;
							}
						}
					});
				} else if (en instanceof IntegerIA) {
					final IntegerIA integerIA = (IntegerIA) en;
					@NotNull VariableTableEntry vte = integerIA.getEntry();
					vte.addStatusListener(new BaseTableEntry.StatusListener() {
						@Override
						public void onChange(IElementHolder eh, BaseTableEntry.Status newStatus) {
							if (newStatus != BaseTableEntry.Status.KNOWN)
								return;

							@NotNull VariableTableEntry vte2 = vte;

							final OS_Element el = eh.getElement();

							@NotNull ElObjectType type = DecideElObjectType.getElObjectType(el);

							switch (type) {
							case VAR:
								break;
							default:
								throw new NotImplementedException();
							}
						}
					});
				} else
					throw new NotImplementedException();
			}
		}
	}

	private @NotNull String getPTEString(ProcTableEntry pte) {
		String pte_string;
		if (pte == null)
			pte_string = "[]";
		else {
			List<String> l = new ArrayList<String>();

			for (TypeTableEntry typeTableEntry : pte.getArgs()) {
				OS_Type attached = typeTableEntry.getAttached();

				if (attached != null)
					l.add(attached.toString());
				else {
					LOG.err("267 attached == null for "+typeTableEntry);

					if (typeTableEntry.expression != null)
						l.add(String.format("<Unknown expression: %s>", typeTableEntry.expression));
					else
						l.add("<Unknkown>");
				}
			}

			StringBuilder sb2 = new StringBuilder();
			sb2.append("[");
			sb2.append(Helpers.String_join(", ", l));
			sb2.append("]");
			pte_string = sb2.toString();
		}
		return pte_string;
	}

	/**
	 * See {@link Implement_construct#_implement_construct_type}
	 */
	private ClassInvocation genCI(TypeTableEntry aType) {
		GenType genType = aType.genType;
		if (genType.nonGenericTypeName != null) {
			NormalTypeName aTyn1 = (NormalTypeName) genType.nonGenericTypeName;
			String constructorName = null; // TODO this comes from nowhere
			ClassStatement best = genType.resolved.getClassOf();
			//
			List<TypeName> gp = best.getGenericPart();
			ClassInvocation clsinv = new ClassInvocation(best, constructorName);
			if (gp.size() > 0) {
				TypeNameList gp2 = aTyn1.getGenericPart();
				for (int i = 0; i < gp.size(); i++) {
					final TypeName typeName = gp2.get(i);
					@NotNull OS_Type typeName2;
					try {
						typeName2 = resolve_type(new OS_Type(typeName), typeName.getContext());
						clsinv.set(i, gp.get(i), typeName2);
					} catch (ResolveError aResolveError) {
						aResolveError.printStackTrace();
						return null;
					}
				}
			}
			clsinv = phase.registerClassInvocation(clsinv);
			genType.ci = clsinv;
			return clsinv;
		}
		if (genType.resolved != null) {
			ClassStatement best = genType.resolved.getClassOf();
			String constructorName = null; // TODO what to do about this, nothing I guess

			List<TypeName> gp = best.getGenericPart();
			ClassInvocation clsinv = new ClassInvocation(best, constructorName);
			assert best.getGenericPart().size() == 0;
/*
			if (gp.size() > 0) {
				TypeNameList gp2 = aTyn1.getGenericPart();
				for (int i = 0; i < gp.size(); i++) {
					final TypeName typeName = gp2.get(i);
					@NotNull OS_Type typeName2;
					try {
						typeName2 = resolve_type(new OS_Type(typeName), typeName.getContext());
						clsinv.set(i, gp.get(i), typeName2);
					} catch (ResolveError aResolveError) {
						aResolveError.printStackTrace();
						return null;
					}
				}
			}
*/
			clsinv = phase.registerClassInvocation(clsinv);
			genType.ci = clsinv;
			return clsinv;
		}
		return null;
	}

	private ClassInvocation genCI(GenType genType, TypeName aTypeName) {
		if (genType.nonGenericTypeName != null) {
			NormalTypeName aTyn1 = (NormalTypeName) genType.nonGenericTypeName;
			String constructorName = null; // TODO this comes from nowhere
			ClassStatement best = genType.resolved.getClassOf();
			//
			List<TypeName> gp = best.getGenericPart();
			ClassInvocation clsinv = new ClassInvocation(best, constructorName);
			if (gp.size() > 0) {
				TypeNameList gp2 = aTyn1.getGenericPart();
				for (int i = 0; i < gp.size(); i++) {
					final TypeName typeName = gp2.get(i);
					@NotNull OS_Type typeName2;
					try {
						typeName2 = resolve_type(new OS_Type(typeName), typeName.getContext());
						clsinv.set(i, gp.get(i), typeName2);
					} catch (ResolveError aResolveError) {
						aResolveError.printStackTrace();
						return null;
					}
				}
			}
			clsinv = phase.registerClassInvocation(clsinv);
			genType.ci = clsinv;
			return clsinv;
		}
		if (genType.resolved != null) {
			ClassStatement best = genType.resolved.getClassOf();
			String constructorName = null; // TODO what to do about this, nothing I guess

			List<TypeName> gp = best.getGenericPart();
			ClassInvocation clsinv;
			if (genType.ci == null) {
				clsinv = new ClassInvocation(best, constructorName);
				if (gp.size() > 0) {
					if (aTypeName instanceof NormalTypeName) {
						final NormalTypeName tn = (NormalTypeName) aTypeName;
						TypeNameList tngp = tn.getGenericPart();
						for (int i = 0; i < gp.size(); i++) {
							final TypeName typeName = tngp.get(i);
							@NotNull OS_Type typeName2;
							try {
								typeName2 = resolve_type(new OS_Type(typeName), typeName.getContext());
								clsinv.set(i, gp.get(i), typeName2);
							} catch (ResolveError aResolveError) {
								aResolveError.printStackTrace();
								return null;
							}
						}
					}
				}
				clsinv = phase.registerClassInvocation(clsinv);
				genType.ci = clsinv;
			} else
				clsinv = (ClassInvocation) genType.ci;
			return clsinv;
		}
		return null;
	}

	final List<FunctionInvocation> functionInvocations = new ArrayList<>();

	FunctionInvocation newFunctionInvocation(BaseFunctionDef aFunctionDef, ProcTableEntry aPte, IInvocation aInvocation, DeducePhase aDeducePhase) {
		FunctionInvocation fi = new FunctionInvocation(aFunctionDef, aPte, aInvocation, aDeducePhase.generatePhase);
		// TODO register here
		return fi;
	}

	public String getFileName() {
		return module.getFileName();
	}

	public GenerateFunctions getGenerateFunctions(OS_Module aModule) {
		return phase.generatePhase.getGenerateFunctions(aModule);
	}

	class Resolve_each_typename {

		private final DeducePhase phase;
		private final DeduceTypes2 dt2;
		private final ErrSink errSink;

		public Resolve_each_typename(DeducePhase aPhase, DeduceTypes2 aDeduceTypes2, ErrSink aErrSink) {
			phase = aPhase;
			dt2 = aDeduceTypes2;
			errSink = aErrSink;
		}

		public void action(@NotNull TypeTableEntry typeTableEntry) {
			@Nullable OS_Type attached = typeTableEntry.getAttached();
			if (attached == null) return;
			if (attached.getType() == OS_Type.Type.USER) {
				action_USER(typeTableEntry, attached);
			} else if (attached.getType() == OS_Type.Type.USER_CLASS) {
				action_USER_CLASS(typeTableEntry, attached);
			}
		}

		public void action_USER_CLASS(TypeTableEntry typeTableEntry, @NotNull OS_Type aAttached) {
			ClassStatement c = aAttached.getClassOf();
			assert c != null;
			phase.onClass(c, new OnClass() {
				// TODO what about ClassInvocation's?
				@Override
				public void classFound(GeneratedClass cc) {
					typeTableEntry.resolve(cc); // set genType.node
				}
			});
		}

		public void action_USER(TypeTableEntry typeTableEntry, @Nullable OS_Type aAttached) {
			TypeName tn = aAttached.getTypeName();
			if (tn == null) return; // hack specifically for Result
			switch (tn.kindOfType()) {
				case FUNCTION:
				case GENERIC:
				case TYPE_OF:
					return;
			}
			try {
				typeTableEntry.setAttached(dt2.resolve_type(aAttached, aAttached.getTypeName().getContext()));
				switch (typeTableEntry.getAttached().getType()) {
				case USER_CLASS:
					action_USER_CLASS(typeTableEntry, typeTableEntry.getAttached());
					break;
				case GENERIC_TYPENAME:
					LOG.err(String.format("801 Generic Typearg %s for %s", tn, "genericFunction.getFD().getParent()"));
					break;
				default:
					LOG.err("245 typeTableEntry attached wrong type " + typeTableEntry);
					break;
				}
			} catch (ResolveError aResolveError) {
				LOG.err("288 Failed to resolve type "+ aAttached);
				errSink.reportDiagnostic(aResolveError);
			}
		}
	}

	class Dependencies {
		final WorkList wl = new WorkList();

		public void action_type(GenType genType, WorkManager aWorkManager) {
			// TODO work this out further
			if (genType.resolvedn != null) {
				OS_Module mod = genType.resolvedn.getContext().module();
				final GenerateFunctions gf = phase.generatePhase.getGenerateFunctions(mod);
				NamespaceInvocation ni = phase.registerNamespaceInvocation(genType.resolvedn);
				WlGenerateNamespace gen = new WlGenerateNamespace(gf, ni, phase.generatedClasses);
				wl.addJob(gen);
			} else if (genType.resolved != null) {
				final ClassStatement c = genType.resolved.getClassOf();
				final OS_Module mod = c.getContext().module();
				final GenerateFunctions gf = phase.generatePhase.getGenerateFunctions(mod);
				ClassInvocation ci;
				if (genType.ci == null) {
					ci = new ClassInvocation(c, null);
					ci = phase.registerClassInvocation(ci);
				} else {
					assert genType.ci instanceof ClassInvocation;
					ci = (ClassInvocation) genType.ci;
				}
				WlGenerateClass gen = new WlGenerateClass(gf, ci, phase.generatedClasses);
				wl.addJob(gen);
			}
			//
			aWorkManager.addJobs(wl);
		}

		public void action_function(FunctionInvocation aDependentFunction, WorkManager aWorkManager) {
			final BaseFunctionDef function = aDependentFunction.getFunction();
			WorkJob gen;
			final OS_Module mod;
			if (function == ConstructorDef.defaultVirtualCtor) {
				ClassInvocation ci = aDependentFunction.getClassInvocation();
				if (ci == null) {
					NamespaceInvocation ni = aDependentFunction.getNamespaceInvocation();
					if (ni == null)
						assert false;
					mod = ni.getNamespace().getContext().module();
				} else {
					mod = ci.getKlass().getContext().module();
				}
				final GenerateFunctions gf = getGenerateFunctions(mod);
				gen = new WlGenerateDefaultCtor(gf, aDependentFunction);
			} else {
				mod = function.getContext().module();
				final GenerateFunctions gf = getGenerateFunctions(mod);
				gen = new WlGenerateFunction(gf, aDependentFunction);
			}
			wl.addJob(gen);
			aWorkManager.addJobs(wl);
		}
	}

	private void resolve_cte_expression(ConstantTableEntry cte, Context aContext) {
		final IExpression iv = cte.initialValue;
		switch (iv.getKind()) {
		case NUMERIC:
			{
				final OS_Type a = cte.getTypeTableEntry().getAttached();
				if (a == null || a.getType() != OS_Type.Type.USER_CLASS) {
					try {
						cte.getTypeTableEntry().setAttached(resolve_type(new OS_Type(BuiltInTypes.SystemInteger), aContext));
					} catch (ResolveError resolveError) {
						LOG.info("71 Can't be here");
//										resolveError.printStackTrace(); // TODO print diagnostic
					}
				}
				break;
			}
		case STRING_LITERAL:
			{
				final OS_Type a = cte.getTypeTableEntry().getAttached();
				if (a == null || a.getType() != OS_Type.Type.USER_CLASS) {
					try {
						cte.getTypeTableEntry().setAttached(resolve_type(new OS_Type(BuiltInTypes.String_), aContext));
					} catch (ResolveError resolveError) {
						LOG.info("117 Can't be here");
//										resolveError.printStackTrace(); // TODO print diagnostic
					}
				}
				break;
			}
		case CHAR_LITERAL:
			{
				final OS_Type a = cte.getTypeTableEntry().getAttached();
				if (a == null || a.getType() != OS_Type.Type.USER_CLASS) {
					try {
						cte.getTypeTableEntry().setAttached(resolve_type(new OS_Type(BuiltInTypes.SystemCharacter), aContext));
					} catch (ResolveError resolveError) {
						LOG.info("117 Can't be here");
//										resolveError.printStackTrace(); // TODO print diagnostic
					}
				}
				break;
			}
		case IDENT:
			{
				final OS_Type a = cte.getTypeTableEntry().getAttached();
				if (a != null) {
					assert a.getType() != null;
					if (a.getType() == OS_Type.Type.BUILT_IN && a.getBType() == BuiltInTypes.Boolean) {
						assert BuiltInTypes.isBooleanText(cte.getName());
					} else
						throw new NotImplementedException();
				} else {
					assert false;
				}
				break;
			}
		default:
			{
				LOG.err("8192 "+iv.getKind());
				throw new NotImplementedException();
			}
		}
	}

	class Lookup_function_on_exit {
		WorkList wl = new WorkList();

		public void action(ProcTableEntry pte) {
			FunctionInvocation fi = pte.getFunctionInvocation();
			if (fi == null) return;

			if (fi.getFunction() == null) {
				if (fi.pte == null) {
					return;
				} else {
//					LOG.err("592 " + fi.getClassInvocation());
					if (fi.pte.getClassInvocation() != null)
						fi.setClassInvocation(fi.pte.getClassInvocation());
//					else
//						fi.pte.setClassInvocation(fi.getClassInvocation());
				}
			}

			ClassInvocation ci = fi.getClassInvocation();
			BaseFunctionDef fd3 = fi.getFunction();
			if (ci == null) {
				ci = fi.pte.getClassInvocation();
			}
			if (fd3 == ConstructorDef.defaultVirtualCtor) {
				if (ci == null) {
					if (/*fi.getClassInvocation() == null &&*/ fi.getNamespaceInvocation() == null) {
						// Assume default constructor
						ci = new ClassInvocation((ClassStatement) pte.getResolvedElement(), null);
						ci = phase.registerClassInvocation(ci);
						fi.setClassInvocation(ci);
					} else
						throw new NotImplementedException();
				}
				final ClassStatement klass = ci.getKlass();

				Collection<ConstructorDef> cis = klass.getConstructors();
				for (ConstructorDef constructorDef : cis) {
					final Iterable<FormalArgListItem> constructorDefArgs = constructorDef.getArgs();

					if (!constructorDefArgs.iterator().hasNext()) { // zero-sized arg list
						fd3 = constructorDef;
						break;
					}
				}
			}

			final OS_Element parent;
			if (fd3 != null) {
				parent = fd3.getParent();
				if (parent instanceof ClassStatement) {
					ci = new ClassInvocation((ClassStatement) parent, null);
					{
						final ClassInvocation classInvocation = pte.getClassInvocation();
						if (classInvocation != null) {
							Map<TypeName, OS_Type> gp = classInvocation.genericPart;
							if (gp != null) {
								int i = 0;
								for (Map.Entry<TypeName, OS_Type> entry : gp.entrySet()) {
									ci.set(i, entry.getKey(), entry.getValue());
									i++;
								}
							}
						}
					}
					proceed(fi, ci, (ClassStatement) parent, wl);
				} else if (parent instanceof NamespaceStatement) {
					proceed(fi, (NamespaceStatement) parent, wl);
				}
			} else {
				parent = ci.getKlass();
				{
					final ClassInvocation classInvocation = pte.getClassInvocation();
					if (classInvocation != null && classInvocation.genericPart != null) {
						Map<TypeName, OS_Type> gp = classInvocation.genericPart;
						int i = 0;
						for (Map.Entry<TypeName, OS_Type> entry : gp.entrySet()) {
							ci.set(i, entry.getKey(), entry.getValue());
							i++;
						}
					}
				}
				proceed(fi, ci, (ClassStatement) parent, wl);
			}

//			proceed(fi, ci, parent);
		}

		void proceed(FunctionInvocation fi, ClassInvocation ci, ClassStatement aParent, WorkList wl) {
			ci = phase.registerClassInvocation(ci);

			ClassStatement kl = ci.getKlass(); // TODO Don't you see aParent??
			assert kl != null;

			final BaseFunctionDef fd2 = fi.getFunction();
			int state = 0;

			if (fd2 == ConstructorDef.defaultVirtualCtor) {
				if (fi.pte.getArgs().size() == 0)
					state = 1;
				else
					state = 2;
			} else if (fd2 instanceof ConstructorDef) {
				if (fi.getClassInvocation().getConstructorName() != null)
					state = 3;
				else
					state = 2;
			} else {
				if (fi.getFunction() == null && fi.getClassInvocation() != null)
					state = 3;
				else
					state = 4;
			}

			switch (state) {
			case 1:
				assert fi.pte.getArgs().size() == 0;
				// default ctor
				wl.addJob(new WlGenerateDefaultCtor(phase.generatePhase.getGenerateFunctions(module), fi));
				break;
			case 2:
				wl.addJob(new WlGenerateCtor(phase.generatePhase.getGenerateFunctions(module), fi, null)); // TODO check this
				break;
			case 3:
				// README this is a special case to generate constructor
				// TODO should it be GenerateDefaultCtor? (check args size and ctor-name)
				final String constructorName = fi.getClassInvocation().getConstructorName();
				final IdentExpression constructorName1 = constructorName != null ? IdentExpression.forString(constructorName) : null;
				wl.addJob(new WlGenerateCtor(phase.generatePhase.getGenerateFunctions(module), fi, constructorName1));
				break;
			case 4:
				wl.addJob(new WlGenerateFunction(phase.generatePhase.getGenerateFunctions(module), fi));
				break;
			default:
				throw new NotImplementedException();
			}

			wm.addJobs(wl);
		}

		void proceed(FunctionInvocation fi, NamespaceStatement aParent, WorkList wl) {
//			ci = phase.registerClassInvocation(ci);

			final OS_Module module1 = aParent.getContext().module();

			final NamespaceInvocation nsi = phase.registerNamespaceInvocation(aParent);

			wl.addJob(new WlGenerateNamespace(phase.generatePhase.getGenerateFunctions(module1), nsi, phase.generatedClasses));
			wl.addJob(new WlGenerateFunction(phase.generatePhase.getGenerateFunctions(module1), fi));

			wm.addJobs(wl);
		}
	}

	public void assign_type_to_idte(IdentTableEntry ite,
									BaseGeneratedFunction generatedFunction,
									Context aFunctionContext,
									Context aContext) {
		if (!ite.hasResolvedElement()) {
			IdentIA ident_a = new IdentIA(ite.getIndex(), generatedFunction);
			resolveIdentIA_(aContext, ident_a, generatedFunction, new FoundElement(phase) {

				final String path = generatedFunction.getIdentIAPathNormal(ident_a);

				@Override
				public void foundElement(OS_Element x) {
					ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(x));
					if (ite.type != null && ite.type.getAttached() != null) {
						switch (ite.type.getAttached().getType()) {
						case USER:
							try {
								OS_Type xx = resolve_type(ite.type.getAttached(), aFunctionContext);
								ite.type.setAttached(xx);
							} catch (ResolveError resolveError) {
								LOG.info("192 Can't attach type to " + path);
								errSink.reportDiagnostic(resolveError);
							}
							if (ite.type.getAttached().getType() == OS_Type.Type.USER_CLASS) {
								use_user_class(ite.type.getAttached(), ite);
							}
							break;
						case USER_CLASS:
							use_user_class(ite.type.getAttached(), ite);
							break;
						case FUNCTION:
							{
								// TODO All this for nothing
								//  the ite points to a function, not a function call,
								//  so there is no point in resolving it
								if (ite.type.tableEntry instanceof ProcTableEntry) {
									final ProcTableEntry pte = (ProcTableEntry) ite.type.tableEntry;

								} else if (ite.type.tableEntry instanceof IdentTableEntry) {
									final IdentTableEntry identTableEntry = (IdentTableEntry) ite.type.tableEntry;
									if (identTableEntry.getCallablePTE() != null) {
										@Nullable ProcTableEntry cpte = identTableEntry.getCallablePTE();
										cpte.typePromise().then(new DoneCallback<GenType>() {
											@Override
											public void onDone(GenType result) {
												System.out.println("1483 "+result.resolved+" "+result.node);
											}
										});
									}
								}
							}
							break;
						default:
							throw new IllegalStateException("Unexpected value: " + ite.type.getAttached().getType());
						}
					} else {
						int yy=2;
						if (!ite.hasResolvedElement()) {
							LookupResultList lrl = null;
							try {
								lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), aFunctionContext, DeduceTypes2.this);
								OS_Element best = lrl.chooseBest(null);
								if (best != null) {
									ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(x));
									if (ite.type != null && ite.type.getAttached() != null) {
										if (ite.type.getAttached().getType() == OS_Type.Type.USER) {
											try {
												OS_Type xx = resolve_type(ite.type.getAttached(), aFunctionContext);
												ite.type.setAttached(xx);
											} catch (ResolveError resolveError) { // TODO double catch
												LOG.info("210 Can't attach type to "+ite.getIdent());
												errSink.reportDiagnostic(resolveError);
//												continue;
											}
										}
									}
								} else {
									LOG.err("184 Couldn't resolve "+ite.getIdent());
								}
							} catch (ResolveError aResolveError) {
								LOG.err("184-506 Couldn't resolve "+ite.getIdent());
								aResolveError.printStackTrace();
							}
							if (ite.type.getAttached().getType() == OS_Type.Type.USER_CLASS) {
								use_user_class(ite.type.getAttached(), ite);
							}
						}
					}
				}

				private void use_user_class(OS_Type aType, IdentTableEntry aEntry) {
					final ClassStatement cs = aType.getClassOf();
					if (aEntry.constructable_pte != null) {
						int yyy=3;
						System.out.println("use_user_class: "+cs);
					}
				}

				@Override
				public void noFoundElement() {
					ite.setStatus(BaseTableEntry.Status.UNKNOWN, null);
					errSink.reportError("165 Can't resolve "+path);
				}
			});
		}
	}

	public void resolve_arguments_table_entry(VariableTableEntry vte, BaseGeneratedFunction generatedFunction, Context ctx) {
		switch (vte.vtt) {
		case ARG:

			TypeTableEntry tte = vte.type;
			final OS_Type attached = tte.getAttached();
			if (attached != null) {
				if (attached.getType() == OS_Type.Type.USER) {
					if (tte.genType.typeName == null)
						tte.genType.typeName = attached;
					try {
						tte.genType.resolved = resolve_type(attached, ctx);
						tte.setAttached(tte.genType.resolved); // TODO probably not necessary, but let's leave it for now
					} catch (ResolveError aResolveError) {
	//				aResolveError.printStackTrace();
						errSink.reportDiagnostic(aResolveError);
						LOG.err("Can't resolve argument type " + attached);
						return;
					}
				}
			} else {
				int y=2;
			}
			break;
		case VAR:
			if (vte.type.getAttached() == null && vte.potentialTypes.size() == 1) {
				TypeTableEntry pot = new ArrayList<>(vte.potentialTypes()).get(0);
				if (pot.getAttached() instanceof OS_FuncExprType) {
					final OS_FuncExprType funcExprType = (OS_FuncExprType) pot.getAttached();

					pot.genType.typeName = funcExprType;

					final FuncExpr fe = (FuncExpr) funcExprType.getElement();

					// add namespace
					OS_Module mod1 = fe.getContext().module();
					NamespaceStatement mod_ns = lookup_module_namespace(mod1);

					ProcTableEntry callable_pte = null;

					if (mod_ns != null) {
						// add func_expr to namespace
						FunctionDef fd1 = new FunctionDef(mod_ns, mod_ns.getContext());
						fd1.setFal(fe.fal());
						fd1.setContext((FunctionContext) fe.getContext());
						fd1.scope(fe.getScope());
						fd1.setSpecies(BaseFunctionDef.Species.FUNC_EXPR);
//						System.out.println("1630 "+mod_ns.getItems()); // element 0 is ctor$0
						fd1.setName(IdentExpression.forString(String.format("$%d", mod_ns.getItems().size()+1)));

						WorkList wl=new WorkList();
						@NotNull GenerateFunctions gen = phase.generatePhase.getGenerateFunctions(mod1);
						NamespaceInvocation modi = new NamespaceInvocation(mod_ns);
						final ProcTableEntry pte = findProcTableEntry(generatedFunction, pot.expression);
						assert pte != null;
						callable_pte = pte;
						FunctionInvocation fi = new FunctionInvocation(fd1, pte, modi, phase.generatePhase);
						wl.addJob(new WlGenerateNamespace(gen, modi, phase.generatedClasses)); // TODO hope this works (for more than one)
						final WlGenerateFunction wlgf = new WlGenerateFunction(gen, fi);
						wl.addJob(wlgf);
						wm.addJobs(wl);
						wm.drain(); // TODO here?

						pot.genType.ci = modi;
						pot.genType.node = wlgf.getResult();

						vte.typeDeferred().resolve(pot.genType);
					}

					if (callable_pte != null)
						vte.setCallablePTE(callable_pte);
				} else if (pot.getAttached() != null && pot.getAttached().getType() == OS_Type.Type.USER_CLASS) {
					int y=1;
					vte.type = pot;
					vte.typeDeferred().resolve(pot.genType);
				} else {
					try {
						if (pot.tableEntry instanceof ProcTableEntry) {
							final ProcTableEntry pte1 = (ProcTableEntry) pot.tableEntry;
							OS_Element e = DeduceLookupUtils.lookup(pte1.expression, ctx, this);
							assert e != null;
							if (e instanceof FunctionDef) {
								final FunctionDef fd = (FunctionDef) e;
								@NotNull IdentTableEntry ite1 = ((IdentIA) pte1.expression_num).getEntry();
								DeducePath dp = ite1.buildDeducePath(generatedFunction);
								GenType t = dp.getType(dp.size() - 1);
								ite1.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(e));
								pte1.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(e));
								pte1.typePromise().then(new DoneCallback<GenType>() {
									@Override
									public void onDone(GenType result) {
										if (t == null) {
											ite1.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, result.resolved);
											ite1.setGenType(result);
										} else {
											assert false; // we don't expect this, but note there is no problem if it happens
											t.copy(result);
										}
									}
								});
								int y=2;
							} else {
								vte.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(e));
								pte1.setStatus(BaseTableEntry.Status.KNOWN, new ConstructableElementHolder(e, vte));
//								vte.setCallablePTE(pte1);

								GenType gt = pot.genType;
//								FunctionInvocation fi;
								if (e instanceof NamespaceStatement) {
									final NamespaceStatement namespaceStatement = (NamespaceStatement) e;
									gt.resolvedn = (NamespaceStatement) e;
									final NamespaceInvocation nsi = phase.registerNamespaceInvocation(namespaceStatement);
//										pte.setNamespaceInvocation(nsi);
									gt.ci = nsi;
//									fi = newFunctionInvocation(fd, pte, nsi, phase);
								} else if (e instanceof ClassStatement) {
									final ClassStatement classStatement = (ClassStatement) e;
									gt.resolved = new OS_Type((ClassStatement) e);
									ClassInvocation ci = new ClassInvocation(classStatement, null);
									ci = phase.registerClassInvocation(ci);
									gt.ci = ci;
//									pte.setClassInvocation(ci);
//									fi = newFunctionInvocation(fd, pte, ci, phase);
								} else // TODO will fail on FunctionDef's
									throw new IllegalStateException("Unknown parent");

								gt.node = vte.genType.node;

								vte.genType.copy(gt);
							}
							int y=2;
						} else
							throw new NotImplementedException();
					} catch (ResolveError aResolveError) {
						errSink.reportDiagnostic(aResolveError);
					}
				}
			}
			break;
		}
	}

	private @Nullable ProcTableEntry findProcTableEntry(BaseGeneratedFunction aGeneratedFunction, IExpression aExpression) {
		for (ProcTableEntry procTableEntry : aGeneratedFunction.prte_list) {
			if (procTableEntry.expression == aExpression)
				return procTableEntry;
		}
		return null;
	}

	public NamespaceStatement lookup_module_namespace(OS_Module aModule) {
		try {
			final IdentExpression module_ident = IdentExpression.forString("__MODULE__");
			OS_Element e = DeduceLookupUtils.lookup(module_ident, aModule.getContext(), this);
			if (e != null) {
				if (e instanceof NamespaceStatement) {
					return (NamespaceStatement) e;
				} else {
					LOG.err("__MODULE__ should be namespace");
					return null;
				}
			} else {
				// not found, so add. this is where AST would come in handy
				NamespaceStatement ns = new NamespaceStatement(aModule, aModule.getContext());
				ns.setName(module_ident);
				return ns;
			}
		} catch (ResolveError aResolveError) {
//			LOG.err("__MODULE__ should be namespace");
			errSink.reportDiagnostic(aResolveError);
			return null;
		}
	}

	public void resolve_ident_table_entry(IdentTableEntry ite, BaseGeneratedFunction generatedFunction, Context ctx) {
		InstructionArgument itex = new IdentIA(ite.getIndex(), generatedFunction);
		while (itex != null) {
			IdentTableEntry itee = ((IdentIA) itex).getEntry();
			while (itex != null) {
				BaseTableEntry x = null;
				if (itee.backlink instanceof IntegerIA) {
					@NotNull VariableTableEntry vte = ((IntegerIA) itee.backlink).getEntry();
					x = vte;
//					if (vte.constructable_pte != null)
					itex = null;
				} else if (itee.backlink instanceof IdentIA) {
					x = ((IdentIA) itee.backlink).getEntry();
					itex = ((IdentTableEntry) x).backlink;
				} else if (itee.backlink instanceof ProcIA) {
					x = ((ProcIA) itee.backlink).getEntry();
					if (itee.getCallablePTE() == null)
						// turned out to be wrong (by double calling), so let's wrap it
						itee.setCallablePTE((ProcTableEntry) x);
					itex = null; //((ProcTableEntry) x).backlink;
				} else if (itee.backlink == null) {
					itex = null;
					x = null;
				}

				if (x != null) {
//					LOG.err("162 Adding FoundParent for "+itee);
					x.addStatusListener(new FoundParent(x, itee, itee.getIdent().getContext(), generatedFunction)); // TODO context??
				}
			}
		}
		if (ite.resolved_element != null)
			return;
		if (true || ite.backlink == null) {
			final IdentIA identIA = new IdentIA(ite.getIndex(), generatedFunction);
			resolveIdentIA_(ite.getPC(), identIA, generatedFunction, new FoundElement(phase) {

				final String x = generatedFunction.getIdentIAPathNormal(identIA);

				@Override
				public void foundElement(OS_Element e) {
//					ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(e)); // this is called in resolveIdentIA_
					found_element_for_ite(generatedFunction, ite, e, ctx);
				}

				@Override
				public void noFoundElement() {
					ite.setStatus(BaseTableEntry.Status.UNKNOWN, null);
					//errSink.reportError("1004 Can't find element for "+ x); // Already reported by 1179
				}
			});
		}
	}

	public void resolve_var_table_entry(VariableTableEntry vte, BaseGeneratedFunction generatedFunction, Context ctx) {
		if (vte.el == null)
			return;
		{
			if (vte.type.getAttached() == null && vte.constructable_pte != null) {
				ClassStatement c = vte.constructable_pte.getFunctionInvocation().getClassInvocation().getKlass();
				final OS_Type attached = new OS_Type(c);
				// TODO this should have been set somewhere already
				//  typeName and nonGenericTypeName are not set
				//  but at this point probably wont be needed
				vte.type.genType.resolved = attached;
				vte.type.setAttached(attached);
			}
			vte.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(vte.el));
			{
				final GenType genType = vte.type.genType;
				if (genType.resolved != null && genType.node == null) {
					genCI(genType, genType.nonGenericTypeName);
//					genType.node = makeNode(genType);
					//
					// registerClassInvocation does the job of makeNode, so results should be immediately available
					//
					((ClassInvocation) genType.ci).resolvePromise().then(new DoneCallback<GeneratedClass>() {
						@Override
						public void onDone(GeneratedClass result) {
							genType.node = result;
							vte.typeDeferred().resolve(genType);
						}
					});
				}
			}
		}
	}

//	private GeneratedNode makeNode(GenType aGenType) {
//		if (aGenType.ci instanceof ClassInvocation) {
//			final ClassInvocation ci = (ClassInvocation) aGenType.ci;
//			@NotNull GenerateFunctions gen = phase.generatePhase.getGenerateFunctions(ci.getKlass().getContext().module());
//			WlGenerateClass wlgc = new WlGenerateClass(gen, ci, phase.generatedClasses);
//			wlgc.run(null);
//			return wlgc.getResult();
//		}
//		return null;
//	}

	class Implement_construct {

		private final BaseGeneratedFunction generatedFunction;
		private final Instruction instruction;
		private final InstructionArgument expression;

		private final ProcTableEntry pte;

		public Implement_construct(BaseGeneratedFunction aGeneratedFunction, Instruction aInstruction) {
			generatedFunction = aGeneratedFunction;
			instruction = aInstruction;

			// README all these asserts are redundant, I know
			assert instruction.getName() == InstructionName.CONSTRUCT;
			assert instruction.getArg(0) instanceof ProcIA;

			final int pte_num = ((ProcIA) instruction.getArg(0)).getIndex();
			pte = generatedFunction.getProcTableEntry(pte_num);

			expression = pte.expression_num;

			assert expression instanceof IntegerIA || expression instanceof IdentIA;
		}

		public void action() {
			if (expression instanceof IntegerIA) {
				action_IntegerIA();
			} else if (expression instanceof IdentIA) {
				action_IdentIA();
			} else {
				throw new NotImplementedException();
			}
		}

		public void action_IdentIA() {
			IdentTableEntry idte = ((IdentIA)expression).getEntry();
			DeducePath deducePath = idte.buildDeducePath(generatedFunction);
			{
				OS_Element el3;
				Context ectx = generatedFunction.getFD().getContext();
				for (int i = 0; i < deducePath.size(); i++) {
					InstructionArgument ia2 = deducePath.getIA(i);

					el3 = deducePath.getElement(i);

					if (ia2 instanceof IntegerIA) {
						@NotNull VariableTableEntry vte = ((IntegerIA) ia2).getEntry();
						// TODO will fail if we try to construct a tmp var, but we never try to do that
						assert vte.vtt != VariableTableType.TEMP;
						assert el3     != null;
						assert i       == 0;
						ectx  = deducePath.getContext(i);
					} else if (ia2 instanceof IdentIA) {
						@NotNull IdentTableEntry idte2 = ((IdentIA) ia2).getEntry();
						final String s = idte2.getIdent().toString();
						LookupResultList lrl = ectx.lookup(s);
						OS_Element el2 = lrl.chooseBest(null);
						if (el2 == null) {
							assert el3 instanceof VariableStatement;
							VariableStatement vs = (VariableStatement) el3;
							@NotNull TypeName tn = vs.typeName();
							OS_Type ty = new OS_Type(tn);

							if (idte2.type == null) {
								// README Don't remember enough about the constructors to select a different one
								@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, ty);
								try {
									@NotNull OS_Type resolved = resolve_type(ty, tn.getContext());
									LOG.err("892 resolved: "+resolved);
									tte.setAttached(resolved);
								} catch (ResolveError aResolveError) {
									errSink.reportDiagnostic(aResolveError);
								}

								idte2.type = tte;
							}
							// s is constructor name
							implement_construct_type(idte2, ty, s);
							return;
						} else {
							if (i+1 == deducePath.size()) {
								assert el3 == el2;
								if (el2 instanceof ConstructorDef) {
									GenType type = deducePath.getType(i);
									if (type.nonGenericTypeName == null) {
										type.nonGenericTypeName = deducePath.getType(i-1).nonGenericTypeName; // HACK. not guararnteed to work!
									}
									OS_Type ty = new OS_Type(type.nonGenericTypeName);
									implement_construct_type(idte2, ty, s);
								}
							} else {
								ectx = deducePath.getContext(i);
							}
						}
//						implement_construct_type(idte/*??*/, ty, null); // TODO how bout when there is no ctor name
					} else {
						throw new NotImplementedException();
					}
				}
			}
		}

		public void action_IntegerIA() {
			VariableTableEntry vte = generatedFunction.getVarTableEntry(((IntegerIA) expression).getIndex());
			assert vte.type.getAttached() != null; // TODO will fail when empty variable expression
			@Nullable OS_Type ty = vte.type.getAttached();
			implement_construct_type(vte, ty, null);
		}

		private void implement_construct_type(Constructable co, @Nullable OS_Type aTy, String constructorName) {
			assert aTy != null;
			if (aTy.getType() == OS_Type.Type.USER) {
				TypeName tyn = aTy.getTypeName();
				if (tyn instanceof NormalTypeName) {
					final NormalTypeName tyn1 = (NormalTypeName) tyn;
					_implement_construct_type(co, constructorName, (NormalTypeName) tyn);
				}
			} else
				throw new NotImplementedException();
			if (co != null) {
				co.setConstructable(pte);
				ClassInvocation best = pte.getClassInvocation();
				assert best != null;
				best.promise().done(new DoneCallback<GeneratedClass>() {
					@Override
					public void onDone(GeneratedClass result) {
						co.resolveType(result);
					}
				});
			}
		}

		private void _implement_construct_type(Constructable co, String constructorName, NormalTypeName aTyn1) {
			String s = aTyn1.getName();
			LookupResultList lrl = aTyn1.getContext().lookup(s);
			OS_Element best = lrl.chooseBest(null);
			assert best instanceof ClassStatement;
			List<TypeName> gp = ((ClassStatement) best).getGenericPart();
			ClassInvocation clsinv = new ClassInvocation((ClassStatement) best, constructorName);
			if (gp.size() > 0) {
				TypeNameList gp2 = aTyn1.getGenericPart();
				for (int i = 0; i < gp.size(); i++) {
					final TypeName typeName = gp2.get(i);
					@NotNull OS_Type typeName2;
					try {
						// TODO transition to GenType
						typeName2 = resolve_type(new OS_Type(typeName), typeName.getContext());
						clsinv.set(i, gp.get(i), typeName2);
					} catch (ResolveError aResolveError) {
						aResolveError.printStackTrace();
					}
				}
			}
			clsinv = phase.registerClassInvocation(clsinv);
			if (co != null) {
				if (co instanceof IdentTableEntry) {
					final IdentTableEntry idte3 = (IdentTableEntry) co;
					idte3.type.genTypeCI(clsinv);
					clsinv.resolvePromise().then(new DoneCallback<GeneratedClass>() {
						@Override
						public void onDone(GeneratedClass result) {
							idte3.resolveType(result);
						}
					});
				} else if (co instanceof VariableTableEntry) {
					final VariableTableEntry vte = (VariableTableEntry) co;
					vte.type.genTypeCI(clsinv);
					clsinv.resolvePromise().then(new DoneCallback<GeneratedClass>() {
						@Override
						public void onDone(GeneratedClass result) {
							vte.resolveType(result);
						}
					});
				}
			}
			pte.setClassInvocation(clsinv);
			pte.setResolvedElement(best);
			// set FunctionInvocation with pte args
			{
				ConstructorDef cc = null;
				if (constructorName != null) {
					Collection<ConstructorDef> cs = ((ClassStatement) best).getConstructors();
					for (ConstructorDef c : cs) {
						if (c.name().equals(constructorName)) {
							cc = c;
							break;
						}
					}
				}
				// TODO also check arguments
				{
					if (cc == null) assert pte.getArgs().size() == 0;
					FunctionInvocation fi = newFunctionInvocation(cc, pte, clsinv, phase);
					pte.setFunctionInvocation(fi);
				}
			}
		}
	}

	void implement_construct(BaseGeneratedFunction generatedFunction, Instruction instruction) {
		final Implement_construct ic = newImplement_construct(generatedFunction, instruction);
		ic.action();
	}

	@NotNull
	public DeduceTypes2.Implement_construct newImplement_construct(BaseGeneratedFunction generatedFunction, Instruction instruction) {
		return new Implement_construct(generatedFunction, instruction);
	}

	void resolve_function_return_type(BaseGeneratedFunction generatedFunction) {
		// MODERNIZATION Does this have any affinity with DeferredMember?
		@Nullable final InstructionArgument vte_index = generatedFunction.vte_lookup("Result");
		if (vte_index != null) {
			final VariableTableEntry vte = generatedFunction.getVarTableEntry(to_int(vte_index));

			if (vte.type.getAttached() != null) {
				phase.typeDecided((GeneratedFunction) generatedFunction, vte.type.genType);
			} else {
				@NotNull Collection<TypeTableEntry> pot1 = vte.potentialTypes();
				ArrayList<TypeTableEntry> pot = new ArrayList<TypeTableEntry>(pot1);
				if (pot.size() == 1) {
					phase.typeDecided((GeneratedFunction) generatedFunction, pot.get(0).genType);
				} else if (pot.size() == 0) {
					GenType unitType = new GenType();
					unitType.typeName = new OS_Type(BuiltInTypes.Unit);
					phase.typeDecided((GeneratedFunction) generatedFunction, unitType);
				} else {
					// TODO report some kind of error/diagnostic and/or let ForFunction know...
					errSink.reportWarning("Can't resolve type of `Result'. potentialTypes > 1 for "+vte);
				}
			}
		} else {
			if (generatedFunction instanceof GeneratedConstructor) {
				// cant set return type of constructors
			} else {
				// if Result is not present, then make function return Unit
				// TODO May not be correct in all cases, such as when Value is present
				// but works for current code structure, where Result is a always present
				GenType unitType = new GenType();
				unitType.typeName = new OS_Type(BuiltInTypes.Unit);
				phase.typeDecided((GeneratedFunction) generatedFunction, unitType);
			}
		}
	}

	class Found_Element_For_ITE {

		private final BaseGeneratedFunction generatedFunction;
		private final Context ctx;

		public Found_Element_For_ITE(BaseGeneratedFunction aGeneratedFunction, Context aCtx) {
			generatedFunction = aGeneratedFunction;
			ctx = aCtx;
		}

		public void action(IdentTableEntry ite) {
			final OS_Element y = ite.resolved_element;

			if (y instanceof VariableStatement) {
				action_VariableStatement(ite, (VariableStatement) y);
			} else if (y instanceof ClassStatement) {
				action_ClassStatement(ite, (ClassStatement) y);
			} else if (y instanceof FunctionDef) {
				action_FunctionDef(ite, (FunctionDef) y);
			} else if (y instanceof PropertyStatement) {
				action_PropertyStatement(ite, (PropertyStatement) y);
			} else if (y instanceof AliasStatement) {
				action_AliasStatement(ite, (AliasStatement) y);
			} else {
				//LookupResultList exp = lookupExpression();
				LOG.info("2009 "+y);
			}
		}

		public void action_AliasStatement(IdentTableEntry ite, AliasStatement y) {
			LOG.err("396 AliasStatement");
			OS_Element x = DeduceLookupUtils._resolveAlias(y, DeduceTypes2.this);
			if (x == null) {
				ite.setStatus(BaseTableEntry.Status.UNKNOWN, null);
				errSink.reportError("399 resolveAlias returned null");
			} else {
				ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(x));
				found_element_for_ite(generatedFunction, ite, x, ctx);
			}
		}

		public void action_PropertyStatement(IdentTableEntry ite, PropertyStatement ps) {
			OS_Type attached;
			switch (ps.getTypeName().kindOfType()) {
				case GENERIC:
					attached = new OS_Type(ps.getTypeName());
					break;
				case NORMAL:
					try {
						attached = new OS_Type(resolve_type(new OS_Type(ps.getTypeName()), ctx).getClassOf());
					} catch (ResolveError resolveError) {
						LOG.err("378 resolveError");
						resolveError.printStackTrace();
						return;
					}
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + ps.getTypeName().kindOfType());
			}
			if (ite.type == null) {
				ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
			} else
				ite.type.setAttached(attached);
			int yy = 2;
		}

		public void action_FunctionDef(IdentTableEntry ite, FunctionDef functionDef) {
			OS_Type attached = new OS_FuncType(functionDef);
			if (ite.type == null) {
				ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
			} else
				ite.type.setAttached(attached);
		}

		public void action_ClassStatement(IdentTableEntry ite, ClassStatement classStatement) {
			OS_Type attached = new OS_Type(classStatement);
			if (ite.type == null) {
				ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
			} else
				ite.type.setAttached(attached);
		}

		public void action_VariableStatement(IdentTableEntry ite, VariableStatement vs) {
			TypeName typeName = vs.typeName();
			if (ite.type == null || ite.type.getAttached() == null) {
				if (!(typeName.isNull())) {
					if (ite.type == null)
						ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, vs.initialValue());
					ite.type.setAttached(new OS_Type(typeName));
				} else {
					final OS_Element parent = vs.getParent().getParent();
					if (parent instanceof NamespaceStatement || parent instanceof ClassStatement) {
						boolean state;
						if (generatedFunction instanceof GeneratedFunction) {
							final GeneratedFunction generatedFunction1 = (GeneratedFunction) generatedFunction;
							state = (parent != generatedFunction1.getFD().getParent());
						} else {
							state = (parent != ((GeneratedConstructor) generatedFunction).getFD().getParent());
						}
						if (state) {
							DeferredMember dm = deferred_member(parent, getInvocationFromBacklink(ite.backlink), vs, ite);
							dm.typePromise().
									done(new DoneCallback<GenType>() {
										@Override
										public void onDone(GenType result) {
											if (ite.type == null)
												ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, vs.initialValue());
											assert result.resolved != null;
											ite.setGenType(result);
										}
									});
						}

						GenType genType = null;
						if (parent instanceof NamespaceStatement)
							genType = new GenType((NamespaceStatement) parent);
						else if (parent instanceof ClassStatement)
							genType = new GenType((ClassStatement) parent);

						generatedFunction.addDependentType(genType);
					}
					LOG.err("394 typename is null "+ vs.getName());
				}
			}
		}
	}

	void found_element_for_ite(BaseGeneratedFunction generatedFunction, IdentTableEntry ite, @Nullable OS_Element y, Context ctx) {
		assert y == ite.resolved_element;

		Found_Element_For_ITE fefi = new Found_Element_For_ITE(generatedFunction, ctx);
		fefi.action(ite);
	}

	private IInvocation getInvocationFromBacklink(InstructionArgument aBacklink) {
		if (aBacklink == null) return null;
		// TODO implement me
		return null;
	}

	private DeferredMember deferred_member(OS_Element aParent, IInvocation aInvocation, VariableStatement aVariableStatement, IdentTableEntry ite) {
		DeferredMember dm = deferred_member(aParent, aInvocation, aVariableStatement);
		dm.externalRef().then(new DoneCallback<GeneratedNode>() {
			@Override
			public void onDone(GeneratedNode result) {
				ite.externalRef = result;
			}
		});
		return dm;
	}

	private DeferredMember deferred_member(OS_Element aParent, IInvocation aInvocation, VariableStatement aVariableStatement) {
		if (aInvocation == null) {
			if (aParent instanceof NamespaceStatement)
				aInvocation = phase.registerNamespaceInvocation((NamespaceStatement) aParent);
		}
		DeferredMember dm = new DeferredMember(aParent, aInvocation, aVariableStatement);
		phase.addDeferredMember(dm);
		return dm;
	}

	@NotNull OS_Type resolve_type(final OS_Type type, final Context ctx) throws ResolveError {
		return resolve_type(module, type, ctx);
	}

	@NotNull
	/*static*/ OS_Type resolve_type(final OS_Module module, final OS_Type type, final Context ctx) throws ResolveError {
		switch (type.getType()) {

		case BUILT_IN:
			{
				switch (type.getBType()) {
				case SystemInteger:
					{
						String typeName = type.getBType().name();
						assert typeName.equals("SystemInteger");
						OS_Module prelude = module.prelude;
						if (prelude == null) // README Assume `module' IS prelude
							prelude = module;
						final LookupResultList lrl = prelude.getContext().lookup(typeName);
						OS_Element best = lrl.chooseBest(null);
						while (!(best instanceof ClassStatement)) {
							if (best instanceof AliasStatement) {
								best = DeduceLookupUtils._resolveAlias2((AliasStatement) best, this);
							} else if (OS_Type.isConcreteType(best)) {
								throw new NotImplementedException();
							} else
								throw new NotImplementedException();
						}
						if (best == null) {
							throw new ResolveError(IdentExpression.forString(typeName), lrl);
						}
						return new OS_Type((ClassStatement) best);
					}
				case String_:
					{
						String typeName = type.getBType().name();
						assert typeName.equals("String_");
						OS_Module prelude = module.prelude;
						if (prelude == null) // README Assume `module' IS prelude
							prelude = module;
						final LookupResultList lrl = prelude.getContext().lookup("ConstString"); // TODO not sure about String
						OS_Element best = lrl.chooseBest(null);
						while (!(best instanceof ClassStatement)) {
							if (best instanceof AliasStatement) {
								best = DeduceLookupUtils._resolveAlias2((AliasStatement) best, this);
							} else if (OS_Type.isConcreteType(best)) {
								throw new NotImplementedException();
							} else
								throw new NotImplementedException();
						}
						if (best == null) {
							throw new ResolveError(IdentExpression.forString(typeName), lrl);
						}
						return new OS_Type((ClassStatement) best);
					}
				case SystemCharacter:
					{
						String typeName = type.getBType().name();
						assert typeName.equals("SystemCharacter");
						OS_Module prelude = module.prelude;
						if (prelude == null) // README Assume `module' IS prelude
							prelude = module;
						final LookupResultList lrl = prelude.getContext().lookup("SystemCharacter");
						OS_Element best = lrl.chooseBest(null);
						while (!(best instanceof ClassStatement)) {
							if (best instanceof AliasStatement) {
								best = DeduceLookupUtils._resolveAlias2((AliasStatement) best, this);
							} else if (OS_Type.isConcreteType(best)) {
								throw new NotImplementedException();
							} else if (best == null) {
								// passthrough
								break;
							} else {
								throw new NotImplementedException();
							}
						}
						if (best == null) {
							throw new ResolveError(IdentExpression.forString(typeName), lrl);
						}
						return new OS_Type((ClassStatement) best);
					}
				case Boolean:
					{
						OS_Module prelude = module.prelude;
						if (prelude == null) // README Assume `module' IS prelude
							prelude = module;
						final LookupResultList lrl = prelude.getContext().lookup("Boolean");
						final OS_Element best = lrl.chooseBest(null);
						return new OS_Type((ClassStatement) best); // TODO might change to Type
					}
				default:
					throw new IllegalStateException("531 Unexpected value: " + type.getBType());
				}
			}
		case USER:
			{
				final TypeName tn1 = type.getTypeName();
				switch (tn1.kindOfType()) {
				case NORMAL:
					{
						final Qualident tn = ((NormalTypeName) tn1).getRealName();
						LOG.info("799 [resolving USER type named] " + tn);
						final LookupResultList lrl = DeduceLookupUtils.lookupExpression(tn, tn1.getContext(), this);
						OS_Element best = lrl.chooseBest(null);
						while (best instanceof AliasStatement) {
							best = DeduceLookupUtils._resolveAlias2((AliasStatement) best, this);
						}
						if (best == null) {
							if (tn.asSimpleString().equals("Any"))
								return new OS_AnyType();
							throw new ResolveError(tn1, lrl);
						}

						if (best instanceof ClassContext.OS_TypeNameElement) {
							return new OS_GenericTypeNameType((ClassContext.OS_TypeNameElement) best);
						}

						return new OS_Type((ClassStatement) best);
					}
				case FUNCTION:
				case GENERIC:
				case TYPE_OF:
					throw new NotImplementedException();
				default:
					throw new IllegalStateException("414 Unexpected value: " + tn1.kindOfType());
				}
			}
		case USER_CLASS:
			return type;
		case FUNCTION:
			return type;
		default:
			throw new IllegalStateException("565 Unexpected value: " + type.getType());
		}
//		throw new IllegalStateException("Cant be here.");
	}

	private void do_assign_constant(final BaseGeneratedFunction generatedFunction, final Instruction instruction, final VariableTableEntry vte, final ConstTableIA i2) {
		if (vte.type.getAttached() != null) {
			// TODO check types
		}
		final ConstantTableEntry cte = generatedFunction.getConstTableEntry(i2.getIndex());
		if (cte.type.getAttached() == null) {
			LOG.info("Null type in CTE "+cte);
		}
//		vte.type = cte.type;
		vte.addPotentialType(instruction.getIndex(), cte.type);
	}

	private void do_assign_call(final BaseGeneratedFunction generatedFunction,
								final Context ctx,
								final VariableTableEntry vte,
								final FnCallArgs fca,
								final Instruction instruction) {
		final int instructionIndex = instruction.getIndex();
		final ProcTableEntry pte = generatedFunction.getProcTableEntry(to_int(fca.getArg(0)));
		IdentIA identIA = (IdentIA) pte.expression_num;

		if (vte.getStatus() == BaseTableEntry.Status.UNCHECKED) {
			pte.typePromise().then(new DoneCallback<GenType>() {
				@Override
				public void onDone(GenType result) {
					vte.typeDeferred().resolve(result);
				}
			});
			if (vte.el != null) {
				try {
					OS_Element el;
					if (vte.el instanceof IdentExpression)
						el = DeduceLookupUtils.lookup((IdentExpression) vte.el, ctx, this);
					else
						el = DeduceLookupUtils.lookup(((VariableStatement) vte.el).getNameToken(), ctx, this);
					vte.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(el));
				} catch (ResolveError aResolveError) {
					errSink.reportDiagnostic(aResolveError);
					return;
				}
			}
		}

		if (identIA != null){
//			LOG.info("594 "+identIA.getEntry().getStatus());

			resolveIdentIA_(ctx, identIA, generatedFunction, new FoundElement(phase) {

				final String xx = generatedFunction.getIdentIAPathNormal(identIA);

				@Override
				public void foundElement(OS_Element e) {
//					LOG.info(String.format("600 %s %s", xx ,e));
//					LOG.info("601 "+identIA.getEntry().getStatus());
					final OS_Element resolved_element = identIA.getEntry().resolved_element;
					assert e == resolved_element;
//					set_resolved_element_pte(identIA, e, pte);
					pte.setStatus(BaseTableEntry.Status.KNOWN, new ConstructableElementHolder(e, identIA));
					pte.onFunctionInvocation(new DoneCallback<FunctionInvocation>() {
						@Override
						public void onDone(FunctionInvocation result) {
							result.generateDeferred().done(new DoneCallback<BaseGeneratedFunction>() {
								@Override
								public void onDone(BaseGeneratedFunction bgf) {
									PromiseExpectation<GenType> pe = promiseExpectation(bgf, "Function Result type");
									bgf.typePromise().then(new DoneCallback<GenType>() {
										@Override
										public void onDone(GenType result) {
											pe.satisfy(result);
											@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, result.resolved); // TODO there has to be a better way
											tte.genType.copy(result);
											vte.addPotentialType(instructionIndex, tte);
										}
									});
								}
							});
						}
					});
				}

				@Override
				public void noFoundElement() {
					// TODO create Diagnostic and quit
					LOG.info("1005 Can't find element for " + xx);
				}
			});
		}
		List<TypeTableEntry> args = pte.getArgs();
		for (int i = 0; i < args.size(); i++) {
			final TypeTableEntry tte = args.get(i); // TODO this looks wrong
//			LOG.info("770 "+tte);
			final IExpression e = tte.expression;
			if (e == null) continue;
			switch (e.getKind()) {
			case NUMERIC:
				tte.setAttached(new OS_Type(BuiltInTypes.SystemInteger));
				//vte.type = tte;
				break;
			case CHAR_LITERAL:
				tte.setAttached(new OS_Type(BuiltInTypes.SystemCharacter));
				break;
			case IDENT:
				do_assign_call_args_ident(generatedFunction, ctx, vte, instructionIndex, pte, i, tte, (IdentExpression) e);
				break;
			case PROCEDURE_CALL:
				{
					final ProcedureCallExpression pce = (ProcedureCallExpression) e;
					try {
						final LookupResultList lrl = DeduceLookupUtils.lookupExpression(pce.getLeft(), ctx, this);
						OS_Element best = lrl.chooseBest(null);
						if (best != null) {
							while (best instanceof AliasStatement) {
								best = DeduceLookupUtils._resolveAlias2((AliasStatement) best, this);
							}
							if (best instanceof FunctionDef) {
								final OS_Element parent = best.getParent();
								IInvocation invocation;
								if (parent instanceof NamespaceStatement) {
									invocation = phase.registerNamespaceInvocation((NamespaceStatement) parent);
								} else if (parent instanceof ClassStatement) {
									ClassInvocation ci = new ClassInvocation((ClassStatement) parent, null);
									invocation = phase.registerClassInvocation(ci);
								} else 
									throw new NotImplementedException(); // TODO implement me
								
								forFunction(newFunctionInvocation((FunctionDef) best, pte, invocation, phase), new ForFunction() {
									@Override
									public void typeDecided(GenType aType) {
										tte.setAttached(gt(aType)); // TODO stop setting attached!
										tte.genType.copy(aType);
//										vte.addPotentialType(instructionIndex, tte);
									}
								});
//								tte.setAttached(new OS_FuncType((FunctionDef) best));
								
							} else {
								final int y=2;
								throw new NotImplementedException();
							}
						} else {
							final int y=2;
							throw new NotImplementedException();
						}
					} catch (ResolveError aResolveError) {
						aResolveError.printStackTrace();
						int y=2;
						throw new NotImplementedException();
					}
				}
				break;
			case DOT_EXP:
				{
					final DotExpression de = (DotExpression) e;
					try {
						final LookupResultList lrl = DeduceLookupUtils.lookupExpression(de.getLeft(), ctx, this);
						OS_Element best = lrl.chooseBest(null);
						if (best != null) {
							while (best instanceof AliasStatement) {
								best = DeduceLookupUtils._resolveAlias2((AliasStatement) best, this);
							}
							if (best instanceof FunctionDef) {
								tte.setAttached(new OS_FuncType((FunctionDef) best));
								//vte.addPotentialType(instructionIndex, tte);
							} else if (best instanceof ClassStatement) {
								tte.setAttached(new OS_Type((ClassStatement) best));
							} else if (best instanceof VariableStatement) {
								final VariableStatement vs = (VariableStatement) best;
								@Nullable InstructionArgument vte_ia = generatedFunction.vte_lookup(vs.getName());
								TypeTableEntry tte1 = ((IntegerIA) vte_ia).getEntry().type;
								tte.setAttached(tte1.getAttached());
							} else {
								final int y=2;
								LOG.err(best.getClass().getName());
								throw new NotImplementedException();
							}
						} else {
							final int y=2;
							throw new NotImplementedException();
						}
					} catch (ResolveError aResolveError) {
						aResolveError.printStackTrace();
						int y=2;
						throw new NotImplementedException();
					}
				}
				break;

			case GET_ITEM:
				{
					final GetItemExpression gie = (GetItemExpression) e;
					do_assign_call_GET_ITEM(gie, tte, generatedFunction, ctx);
					continue;
				}
//				break;
			default:
				throw new IllegalStateException("Unexpected value: " + e.getKind());
			}
		}
		{
			if (pte.expression_num == null) {
				if (fca.expression_to_call.getName() != InstructionName.CALLS) {
					final String text = ((IdentExpression) pte.expression).getText();
					final LookupResultList lrl = ctx.lookup(text);

					final OS_Element best = lrl.chooseBest(null);
					if (best != null)
						pte.setResolvedElement(best); // TODO do we need to add a dependency for class?
					else {
						errSink.reportError("Cant resolve "+text);
					}
				} else {
					implement_calls(generatedFunction, ctx.getParent(), instruction.getArg(1), pte, instructionIndex);
				}
			} else {
				final int y=2;
				resolveIdentIA_(ctx, identIA, generatedFunction, new FoundElement(phase) {

					final String x = generatedFunction.getIdentIAPathNormal(identIA);

					@Override
					public void foundElement(OS_Element el) {
						if (pte.getResolvedElement() == null)
							pte.setResolvedElement(el);
						if (el instanceof FunctionDef) {
							FunctionDef fd = (FunctionDef) el;
							final IInvocation invocation;
							if (fd.getParent() == generatedFunction.getFD().getParent()) {
								invocation = getInvocation((GeneratedFunction) generatedFunction);
							} else {
								if (fd.getParent() instanceof NamespaceStatement) {
									NamespaceInvocation ni = phase.registerNamespaceInvocation((NamespaceStatement) fd.getParent());
									invocation = ni;
								} else if (fd.getParent() instanceof ClassStatement) {
									final ClassStatement classStatement = (ClassStatement) fd.getParent();
									ClassInvocation ci = new ClassInvocation(classStatement, null);
									final List<TypeName> genericPart = classStatement.getGenericPart();
									if (genericPart.size() > 0) {
										// TODO handle generic parameters somehow (getInvocationFromBacklink?)

									}
									ci = phase.registerClassInvocation(ci);
									invocation = ci;
								} else
									throw new NotImplementedException();
							}
							forFunction(newFunctionInvocation(fd, pte, invocation, phase), new ForFunction() {
								@Override
								public void typeDecided(GenType aType) {
									if (!vte.typeDeferred().isPending()) {
										if (vte.resolvedType() == null) {
											final ClassInvocation ci = genCI(aType, null);
											vte.type.genTypeCI(ci);
											ci.resolvePromise().then(new DoneCallback<GeneratedClass>() {
												@Override
												public void onDone(GeneratedClass result) {
													vte.resolveType(result);
												}
											});
										}
										LOG.err("2041 type already found "+vte);
										return; // type already found
									}
									// I'm not sure if below is ever called
									@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, gt(aType), pte.expression, pte);
									vte.addPotentialType(instructionIndex, tte);
								}
							});
						} else if (el instanceof ClassStatement) {
							ClassStatement kl = (ClassStatement) el;
							OS_Type type = new OS_Type(kl);
							@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, type, pte.expression, pte);
							vte.addPotentialType(instructionIndex, tte);
							vte.setConstructable(pte);

							register_and_resolve(vte, kl);
						} else {
							LOG.err("7890 "+el.getClass().getName());
						}
					}

					@Override
					public void noFoundElement() {
						LOG.err("IdentIA path cannot be resolved "+ x);
					}
				});
			}
		}
	}

	PromiseExpectations expectations = new PromiseExpectations();

	static class PromiseExpectations {
		static long counter = 0;

		List<PromiseExpectation> exp = new ArrayList<>();

		public void add(PromiseExpectation aExpectation) {
			counter++;
			aExpectation.setCounter(counter);
			exp.add(aExpectation);
		}

		public void check() {
			for (PromiseExpectation promiseExpectation : exp) {
				if (!promiseExpectation.isSatisfied())
					promiseExpectation.fail();
			}
		}
	}

	public interface ExpectationBase {
		String expectationString();
	}

	public class PromiseExpectation<B> {

		private final ExpectationBase base;
		private B result;
		private long counter;
		private final String desc;
		private boolean satisfied;
		private boolean _printed;

		public PromiseExpectation(ExpectationBase aBase, String aDesc) {
			base = aBase;
			desc = aDesc;
		}

		public void satisfy(B aResult) {
			result = aResult;
			satisfied = true;
			LOG.info(String.format("Expectation (%s, %d) met: %s %s", DeduceTypes2.this, counter, desc, base.expectationString()));
		}

		public void fail() {
			if (!_printed) {
				LOG.err(String.format("Expectation (%s, %d) not met", DeduceTypes2.this, counter));
				_printed = true;
			}
		}

		public boolean isSatisfied() {
			return satisfied;
		}

		public void setCounter(long aCounter) {
			counter = aCounter;

			LOG.info(String.format("Expectation (%s, %d) set: %s %s", DeduceTypes2.this, counter, desc, base.expectationString()));
		}
	}

	public <B> PromiseExpectation<B> promiseExpectation(ExpectationBase base, String desc) {
		final PromiseExpectation<B> promiseExpectation = new PromiseExpectation<>(base, desc);
		expectations.add(promiseExpectation);
		return promiseExpectation;
	}

	public IInvocation getInvocation(@NotNull GeneratedFunction generatedFunction) {
		final ClassInvocation classInvocation = generatedFunction.fi.getClassInvocation();
		final NamespaceInvocation ni;
		if (classInvocation == null) {
			ni = generatedFunction.fi.getNamespaceInvocation();
			return ni;
		} else
			return classInvocation;
	}

	private void do_assign_call_args_ident(@NotNull BaseGeneratedFunction generatedFunction,
										   Context ctx,
										   VariableTableEntry vte,
										   int aInstructionIndex,
										   ProcTableEntry aPte,
										   int aI,
										   TypeTableEntry aTte,
										   @NotNull IdentExpression aExpression) {
		final String e_text = aExpression.getText();
		final InstructionArgument vte_ia = generatedFunction.vte_lookup(e_text);
//		LOG.info("10000 "+vte_ia);
		if (vte_ia != null) {
			final VariableTableEntry vte1 = generatedFunction.getVarTableEntry(to_int(vte_ia));
			final Promise<GenType, Void, Void> p = vte1.typePromise();
			p.done(new DoneCallback<GenType>() {
				@Override
				public void onDone(GenType result) {
//					assert vte != vte1;
//					aTte.setAttached(result.resolved != null ? result.resolved : result.typeName);
					aTte.genType.copy(result);
//					vte.addPotentialType(aInstructionIndex, result); // TODO!!
				}
			});
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					final List<TypeTableEntry> ll = getPotentialTypesVte((GeneratedFunction) generatedFunction, vte_ia);
					doLogic(ll);
				}

				public void doLogic(@NotNull List<TypeTableEntry> potentialTypes) {
					assert potentialTypes.size() >= 0;
					switch (potentialTypes.size()) {
						case 1:
//							tte.attached = ll.get(0).attached;
//							vte.addPotentialType(instructionIndex, ll.get(0));
							if (p.isResolved())
								LOG.info(String.format("1047 (vte already resolved) vte1.type = %s, gf = %s, tte1 = %s %n", vte1.type, generatedFunction, potentialTypes.get(0)));
							else {
								final OS_Type attached = potentialTypes.get(0).getAttached();
								if (attached == null) return;
								switch (attached.getType()) {
									case USER:
										vte1.type.setAttached(attached); // !!
										break;
									case USER_CLASS:
										final GenType gt = vte1.genType;
										gt.resolved = attached;
										vte1.typeDeferred().resolve(gt);
										break;
									default:
										errSink.reportWarning("Unexpected value: " + attached.getType());
//										throw new IllegalStateException("Unexpected value: " + attached.getType());
								}
							}
							break;
						case 0:
							// README moved up here to elimiate work
							if (p.isResolved()) {
								System.out.printf("890-1 Already resolved type: vte1.type = %s, gf = %s %n", vte1.type, generatedFunction);
								break;
							}
							LookupResultList lrl = ctx.lookup(e_text);
							OS_Element best = lrl.chooseBest(null);
							if (best instanceof FormalArgListItem) {
								@NotNull final FormalArgListItem fali = (FormalArgListItem) best;
								final OS_Type osType = new OS_Type(fali.typeName());
								if (!osType.equals(vte.type.getAttached())) {
									@NotNull TypeTableEntry tte1 = generatedFunction.newTypeTableEntry(
											TypeTableEntry.Type.SPECIFIED, osType, fali.getNameToken(), vte1);
									/*if (p.isResolved())
										System.out.printf("890 Already resolved type: vte1.type = %s, gf = %s, tte1 = %s %n", vte1.type, generatedFunction, tte1);
									else*/ {
										final OS_Type attached = tte1.getAttached();
										switch (attached.getType()) {
											case USER:
												vte1.type.setAttached(attached); // !!
												break;
											case USER_CLASS:
												final GenType gt = vte1.genType;
												gt.resolved = attached;
												vte1.typeDeferred().resolve(gt);
												break;
											default:
												errSink.reportWarning("Unexpected value: " + attached.getType());
//												throw new IllegalStateException("Unexpected value: " + attached.getType());
										}
									}
								}
//								vte.type = tte1;
//								tte.attached = tte1.attached;
//								vte.setStatus(BaseTableEntry.Status.KNOWN, best);
							} else if (best instanceof VariableStatement) {
								final VariableStatement vs = (VariableStatement) best;
								//
								assert vs.getName().equals(e_text);
								//
								@Nullable InstructionArgument vte2_ia = generatedFunction.vte_lookup(vs.getName());
								VariableTableEntry vte2 = generatedFunction.getVarTableEntry(to_int(vte2_ia));
								if (p.isResolved())
									System.out.printf("915 Already resolved type: vte2.type = %s, gf = %s %n", vte1.type, generatedFunction);
								else {
									final GenType gt = vte1.genType;
									final OS_Type attached = vte2.type.getAttached();
									gt.resolved = attached;
									vte1.typeDeferred().resolve(gt);
								}
//								vte.type = vte2.type;
//								tte.attached = vte.type.attached;
								vte.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
								vte2.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best)); // TODO ??
							} else {
								int y = 2;
								LOG.err("543 " + best.getClass().getName());
								throw new NotImplementedException();
							}
							break;
						default:
							// TODO hopefully this works
							final ArrayList<TypeTableEntry> potentialTypes1 = new ArrayList<TypeTableEntry>(
									Collections2.filter(potentialTypes, new Predicate<TypeTableEntry>() {
										@Override
										public boolean apply(@org.checkerframework.checker.nullness.qual.Nullable TypeTableEntry input) {
											assert input != null;
											return input.getAttached() != null;
										}
									}));
							// prevent infinite recursion
							if (potentialTypes1.size() < potentialTypes.size())
								doLogic(potentialTypes1);
							else
								LOG.info("913 Don't know");
							break;
					}
				}
			};
			onFinish(runnable);
		} else {
			int ia = generatedFunction.addIdentTableEntry(aExpression, ctx);
			IdentTableEntry idte = generatedFunction.getIdentTableEntry(ia);
			idte.addPotentialType(aInstructionIndex, aTte); // TODO DotExpression??
			final int ii = aI;
			idte.onType(phase, new OnType() {
				@Override
				public void typeDeduced(@NotNull OS_Type aType) {
					aPte.setArgType(ii, aType); // TODO does this belong here or in FunctionInvocation?
					aTte.setAttached(aType); // since we know that tte.attached is always null here
				}

				@Override
				public void noTypeFound() {
					LOG.err("719 no type found "+generatedFunction.getIdentIAPathNormal(new IdentIA(ia, generatedFunction)));
				}
			});
		}
	}

	private void do_assign_call_GET_ITEM(GetItemExpression gie, TypeTableEntry tte, BaseGeneratedFunction generatedFunction, Context ctx) {
		try {
			final LookupResultList lrl = DeduceLookupUtils.lookupExpression(gie.getLeft(), ctx, this);
			final OS_Element best = lrl.chooseBest(null);
			if (best != null) {
				if (best instanceof VariableStatement) { // TODO what about alias?
					VariableStatement vs = (VariableStatement) best;
					String s = vs.getName();
					@Nullable InstructionArgument vte_ia = generatedFunction.vte_lookup(s);
					if (vte_ia != null) {
						VariableTableEntry vte1 = generatedFunction.getVarTableEntry(to_int(vte_ia));
						throw new NotImplementedException();
					} else {
						final IdentTableEntry idte = generatedFunction.getIdentTableEntryFor(vs.getNameToken());
						assert idte != null;
						@Nullable OS_Type ty = idte.type.getAttached();
						idte.onType(phase, new OnType() {
							@Override public void typeDeduced(final @NotNull OS_Type ty) {
								assert ty != null;
								OS_Type rtype = null;
								try {
									rtype = resolve_type(ty, ctx);
								} catch (ResolveError resolveError) {
	//								resolveError.printStackTrace();
									errSink.reportError("Cant resolve " + ty); // TODO print better diagnostic
									return;
								}
								LookupResultList lrl2 = rtype.getClassOf().getContext().lookup("__getitem__");
								OS_Element best2 = lrl2.chooseBest(null);
								if (best2 != null) {
									if (best2 instanceof FunctionDef) {
										FunctionDef fd = (FunctionDef) best2;
										ProcTableEntry pte = null;
										final IInvocation invocation = getInvocation((GeneratedFunction) generatedFunction);
										forFunction(newFunctionInvocation(fd, pte, invocation, phase), new ForFunction() {
											@Override
											public void typeDecided(final GenType aType) {
												assert fd == generatedFunction.getFD();
												//
												if (idte.type == null) {
													@NotNull TypeTableEntry tte1 = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, gt(aType), idte); // TODO expression?
													idte.type = tte1;
												} else
													idte.type.setAttached(gt(aType));
											}
										});
									} else {
										throw new NotImplementedException();
									}
								} else {
									throw new NotImplementedException();
								}
							}

							@Override
							public void noTypeFound() {
								throw new NotImplementedException();
							}
						});
						if (ty == null) {
							@NotNull TypeTableEntry tte3 = generatedFunction.newTypeTableEntry(
									TypeTableEntry.Type.SPECIFIED, new OS_Type(vs.typeName()), vs.getNameToken());
							idte.type = tte3;
							ty = idte.type.getAttached();
						}
					}

	//				tte.attached = new OS_FuncType((FunctionDef) best); // TODO: what is this??
					//vte.addPotentialType(instructionIndex, tte);
				} else if (best instanceof FormalArgListItem) {
					final FormalArgListItem fali = (FormalArgListItem) best;
					String s = fali.name();
					@Nullable InstructionArgument vte_ia = generatedFunction.vte_lookup(s);
					if (vte_ia != null) {
						VariableTableEntry vte2 = generatedFunction.getVarTableEntry(to_int(vte_ia));

	//					final @Nullable OS_Type ty2 = vte2.type.attached;
						vte2.typePromise().done(new DoneCallback<GenType>() {
							@Override
							public void onDone(GenType result) {
	//							assert false; // TODO this code is never reached
								final @Nullable OS_Type ty2 = result.typeName/*.getAttached()*/;
								assert ty2 != null;
								OS_Type rtype = null;
								try {
									rtype = resolve_type(ty2, ctx);
								} catch (ResolveError resolveError) {
	//								resolveError.printStackTrace();
									errSink.reportError("Cant resolve " + ty2); // TODO print better diagnostic
									return;
								}
								LookupResultList lrl2 = rtype.getClassOf().getContext().lookup("__getitem__");
								OS_Element best2 = lrl2.chooseBest(null);
								if (best2 != null) {
									if (best2 instanceof FunctionDef) {
										FunctionDef fd = (FunctionDef) best2;
										ProcTableEntry pte = null;
										final IInvocation invocation = getInvocation((GeneratedFunction) generatedFunction);
										forFunction(newFunctionInvocation(fd, pte, invocation, phase), new ForFunction() {
											@Override
											public void typeDecided(final GenType aType) {
												assert fd == generatedFunction.getFD();
												//
												@NotNull TypeTableEntry tte1 = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, gt(aType), vte2); // TODO expression?
												vte2.type = tte1;
											}
										});
									} else {
										throw new NotImplementedException();
									}
								} else {
									throw new NotImplementedException();
								}
							}
						});
	//					vte2.onType(phase, new OnType() {
	//						@Override public void typeDeduced(final OS_Type ty2) {
	//						}
	//
	//						@Override
	//						public void noTypeFound() {
	//							throw new NotImplementedException();
	//						}
	//					});
	/*
						if (ty2 == null) {
							@NotNull TypeTableEntry tte3 = generatedFunction.newTypeTableEntry(
									TypeTableEntry.Type.SPECIFIED, new OS_Type(fali.typeName()), fali.getNameToken());
							vte2.type = tte3;
	//						ty2 = vte2.type.attached; // TODO this is final, but why assign anyway?
						}
	*/
					}
				} else {
					final int y=2;
					throw new NotImplementedException();
				}
			} else {
				final int y=2;
				throw new NotImplementedException();
			}
		} catch (ResolveError aResolveError) {
			aResolveError.printStackTrace();
			int y=2;
			throw new NotImplementedException();
		}
	}

/*
	private void forFunction(GeneratedFunction gf, ForFunction forFunction) {
		phase.forFunction(this, gf, forFunction);
	}
*/

	void forFunction(FunctionInvocation gf, ForFunction forFunction) {
		phase.forFunction(this, gf, forFunction);
	}

	private void do_assign_constant(final BaseGeneratedFunction generatedFunction, final Instruction instruction, final IdentTableEntry idte, final ConstTableIA i2) {
		if (idte.type != null && idte.type.getAttached() != null) {
			// TODO check types
		}
		final ConstantTableEntry cte = generatedFunction.getConstTableEntry(i2.getIndex());
		if (cte.type.getAttached() == null) {
			LOG.err("*** ERROR: Null type in CTE "+cte);
		}
		// idte.type may be null, but we still addPotentialType here
		idte.addPotentialType(instruction.getIndex(), cte.type);
	}

	private void do_assign_call(final BaseGeneratedFunction generatedFunction,
								final Context ctx,
								final IdentTableEntry idte,
								final FnCallArgs fca,
								final int instructionIndex) {
		final ProcTableEntry pte = generatedFunction.getProcTableEntry(to_int(fca.getArg(0)));
		for (final TypeTableEntry tte : pte.getArgs()) {
			LOG.info("771 "+tte);
			final IExpression e = tte.expression;
			if (e == null) continue;
			switch (e.getKind()) {
			case NUMERIC:
			{
				tte.setAttached(new OS_Type(BuiltInTypes.SystemInteger));
				idte.type = tte; // TODO why not addPotentialType ? see below for example
			}
			break;
			case IDENT:
			{
				final InstructionArgument vte_ia = generatedFunction.vte_lookup(((IdentExpression) e).getText());
				final List<TypeTableEntry> ll = getPotentialTypesVte((GeneratedFunction) generatedFunction, vte_ia);
				if (ll.size() == 1) {
					tte.setAttached(ll.get(0).getAttached());
					idte.addPotentialType(instructionIndex, ll.get(0));
				} else
					throw new NotImplementedException();
			}
			break;
			default:
			{
				throw new NotImplementedException();
			}
			}
		}
		{
			final LookupResultList lrl = ctx.lookup(((IdentExpression)pte.expression).getText());
			final OS_Element best = lrl.chooseBest(null);
			if (best != null)
				pte.setResolvedElement(best); // TODO do we need to add a dependency for class?
			else
				throw new NotImplementedException();
		}
	}

	private void implement_calls(final BaseGeneratedFunction gf, final Context context, final InstructionArgument i2, final ProcTableEntry fn1, final int pc) {
		if (gf.deferred_calls.contains(pc)) {
			LOG.err("Call is deferred "/*+gf.getInstruction(pc)*/+" "+fn1);
			return;
		}
		implement_calls_(gf, context, i2, fn1, pc);
	}

	private void implement_calls_(final BaseGeneratedFunction gf,
								  final Context context,
								  final InstructionArgument i2,
								  final ProcTableEntry pte,
								  final int pc) {
		final IExpression pn1 = pte.expression;
		if (!(pn1 instanceof IdentExpression)) {
			throw new IllegalStateException("pn1 is not IdentExpression");
		}

		final String pn = ((IdentExpression) pn1).getText();
		boolean found = lookup_name_calls(context, pn, pte);
		if (found) return;

		final String pn2 = SpecialFunctions.reverse_name(pn);
		if (pn2 != null) {
//			LOG.info("7002 "+pn2);
			found = lookup_name_calls(context, pn2, pte);
			if (found) return;
		}

		if (i2 instanceof IntegerIA) {
			final VariableTableEntry vte = gf.getVarTableEntry(to_int(i2));
			final Context ctx = gf.getContextFromPC(pc); // might be inside a loop or something
			final String vteName = vte.getName();
			if (vteName != null) {
				if (SpecialVariables.contains(vteName)) {
					LOG.err("Skipping special variable " + vteName + " " + pn);
				} else {
					final LookupResultList lrl2 = ctx.lookup(vteName);
//					LOG.info("7003 "+vteName+" "+ctx);
					final OS_Element best2 = lrl2.chooseBest(null);
					if (best2 != null) {
						found = lookup_name_calls(best2.getContext(), pn, pte);
						if (found) return;

						if (pn2 != null) {
							found = lookup_name_calls(best2.getContext(), pn2, pte);
						}

						if (!found) {
							//throw new NotImplementedException(); // TODO
							errSink.reportError("Special Function not found " + pn);
						}
					} else {
						throw new NotImplementedException(); // Cant find vte, should never happen
					}
				}
			} else {
				final List<TypeTableEntry> tt = getPotentialTypesVte(vte);
				if (tt.size() == 1) {
					final OS_Type x = tt.get(0).getAttached();
					assert x != null;
					switch (x.getType()) {
					case USER_CLASS:
						final Context ctx1 = x.getClassOf().getContext();

						found = lookup_name_calls(ctx1, pn, pte);
						if (found) return;

						if (pn2 != null) {
							found = lookup_name_calls(ctx1, pn2, pte);
						}

						if (!found) {
							//throw new NotImplementedException(); // TODO
							errSink.reportError("Special Function not found " + pn);
						}
						break;
					default:
						assert false;
						break;
					}
				} else
					assert false;
			}
		} else {
			assert Pattern.matches("__.*__", pn);
//			LOG.err(String.format("i2 is not IntegerIA (%s)",i2.getClass().getName()));
			//
			// try to get dunder method from class
			//
			IExpression exp = pte.getArgs().get(0).expression;
			if (exp instanceof IdentExpression) {
				final IdentExpression identExpression = (IdentExpression) exp;
				@Nullable InstructionArgument vte_ia = gf.vte_lookup(identExpression.getText());
				if (vte_ia != null) {
					((IntegerIA) vte_ia).getEntry().typePromise().then(new DoneCallback<GenType>() {
						@Override
						public void onDone(GenType result) {
							boolean found1 = lookup_name_calls(result.resolved.getClassOf().getContext(), pn, pte);
							if (found1) {
								int y=2;
								System.out.println("3071 "+pte.getStatus());
								IInvocation invocation = result.ci;
								FunctionInvocation fi = newFunctionInvocation(gf.getFD(), pte, invocation, phase);
								pte.setFunctionInvocation(fi);
							} else {
								int y=3;
								System.out.println("3074");
							}
						}
					});
				}
			}
		}

		pte.setStatus(BaseTableEntry.Status.UNKNOWN, null);
	}

	private boolean lookup_name_calls(final Context ctx, final String pn, final ProcTableEntry pte) {
		final LookupResultList lrl = ctx.lookup(pn);
		final OS_Element best = lrl.chooseBest(null); // TODO check arity and arg matching
		if (best != null) {
			pte.setStatus(BaseTableEntry.Status.KNOWN, new ConstructableElementHolder(best, null)); // TODO why include if only to be null?
//			set_resolved_element_pte(null, best, pte);
			return true;
		}
		return false;
	}

	public static int to_int(@NotNull final InstructionArgument arg) {
		if (arg instanceof IntegerIA)
			return ((IntegerIA) arg).getIndex();
		if (arg instanceof ProcIA)
			return ((ProcIA) arg).getIndex();
		if (arg instanceof IdentIA)
			return ((IdentIA) arg).getIndex();
		throw new NotImplementedException();
	}

	public void resolveIdentIA2_(Context context, IdentIA identIA, GeneratedFunction generatedFunction, FoundElement foundElement) {
		final List<InstructionArgument> s = generatedFunction._getIdentIAPathList(identIA);
		resolveIdentIA2_(context, identIA, s, generatedFunction, foundElement);
	}

	public void resolveIdentIA_(Context context, IdentIA identIA, BaseGeneratedFunction generatedFunction, FoundElement foundElement) {
		Resolve_Ident_IA ria = new Resolve_Ident_IA(this, phase, context, identIA, generatedFunction, foundElement, errSink);
		ria.action();
	}

	public static class Holder<T> {
		private T el;

		public void set(T el) {
			this.el = el;
		}

		public T get() {
			return el;
		}
	}


	public void resolveIdentIA2_(@NotNull final Context ctx,
								 @Nullable IdentIA identIA,
								 @Nullable List<InstructionArgument> s,
								 @NotNull final BaseGeneratedFunction generatedFunction,
								 @NotNull final FoundElement foundElement) {
		Resolve_Ident_IA2 ria2 = new Resolve_Ident_IA2(this, errSink, phase, generatedFunction, foundElement);
		ria2.resolveIdentIA2_(ctx, identIA, s);
	}

	OS_Type gt(GenType aType) {
		return aType.resolved != null ? aType.resolved : aType.typeName;
	}

	@NotNull ArrayList<TypeTableEntry> getPotentialTypesVte(VariableTableEntry vte) {
		return new ArrayList<TypeTableEntry>(vte.potentialTypes());
	}

	@NotNull
	private ArrayList<TypeTableEntry> getPotentialTypesVte(GeneratedFunction generatedFunction, InstructionArgument vte_index) {
		return getPotentialTypesVte(generatedFunction.getVarTableEntry(to_int(vte_index)));
	}

	public class FoundParent implements BaseTableEntry.StatusListener {
		private BaseTableEntry bte;
		private IdentTableEntry ite;
		private Context ctx;
		private BaseGeneratedFunction generatedFunction;

		@Contract(pure = true)
		public FoundParent(BaseTableEntry aBte, IdentTableEntry aIte, Context aCtx, BaseGeneratedFunction aGeneratedFunction) {
			bte = aBte;
			ite = aIte;
			ctx = aCtx;
			generatedFunction = aGeneratedFunction;
		}

		@Override
		public void onChange(IElementHolder eh, BaseTableEntry.Status newStatus) {
			if (newStatus == BaseTableEntry.Status.KNOWN) {
				if (bte instanceof VariableTableEntry) {
					final VariableTableEntry vte = (VariableTableEntry) bte;
					onChangeVTE(vte);
				} else if (bte instanceof ProcTableEntry) {
					final ProcTableEntry pte = (ProcTableEntry) bte;
					onChangePTE(pte);
				}
				postOnChange(eh);
			}
		}

		/* @ensures ite.type != null */
		private void postOnChange(IElementHolder eh) {
			if (ite.type == null && eh.getElement() instanceof VariableStatement) {
				@NotNull TypeName typ = ((VariableStatement) eh.getElement()).typeName();
				OS_Type ty = new OS_Type(typ);

				try {
					@Nullable OS_Type ty2 = getTY2(typ, ty);

					// no expression or TableEntryIV below
					if (ty2 != null) {
						final TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null);
						// trying to keep genType up to date
						tte.setAttached(ty);
						tte.setAttached(ty2);
						ite.type = tte;
					}
				} catch (ResolveError aResolveError) {
					errSink.reportDiagnostic(aResolveError);
				}
			}
		}

		@Nullable
		private OS_Type getTY2(@NotNull TypeName aTyp, @NotNull OS_Type aTy) throws ResolveError {
			if (aTy.getType() != OS_Type.Type.USER) {
				assert false;
				return aTy;
			}

			OS_Type ty2 = null;
			if (!aTyp.isNull()) {
				assert aTy.getTypeName() != null;
				ty2 = resolve_type(aTy, aTy.getTypeName().getContext());
				return ty2;
			}

			final OS_Type attached = ((VariableTableEntry) bte).type.getAttached();
			if (attached == null) {
				type_is_null_and_attached_is_null();
			} else
				ty2 = attached;

			return ty2;
		}

		private void type_is_null_and_attached_is_null() {
			//LOG.err("2842 attached == null for "+((VariableTableEntry) bte).type);
			PromiseExpectation<GenType> pe = promiseExpectation((VariableTableEntry) bte, "Null USER type attached resolved");
			((VariableTableEntry) bte).typePromise().done(new DoneCallback<GenType>() {
				@Override
				public void onDone(GenType result) {
					pe.satisfy(result);
					final OS_Type attached1 = result.resolved != null ? result.resolved : result.typeName;
					if (attached1 != null) {
						switch (attached1.getType()) {
						case USER_CLASS:
							ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached1);
							break;
						case USER:
							try {
								OS_Type ty3 = resolve_type(attached1, attached1.getTypeName().getContext());
								// no expression or TableEntryIV below
								@NotNull TypeTableEntry tte4 = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null);
								// README trying to keep genType up to date
								tte4.setAttached(attached1);
								tte4.setAttached(ty3);
								ite.type = tte4; // or ty2?
							} catch (ResolveError aResolveError) {
								aResolveError.printStackTrace();
							}
							break;
						}
					}
				}
			});
		}

		private void onChangePTE(ProcTableEntry aPte) {
			if (aPte.getStatus() == BaseTableEntry.Status.KNOWN) { // TODO might be obvious
				if (aPte.getFunctionInvocation() != null) {
					FunctionInvocation fi = aPte.getFunctionInvocation();
					BaseFunctionDef fd = fi.getFunction();
					if (fd instanceof ConstructorDef) {
						fi.generateDeferred().done(new DoneCallback<BaseGeneratedFunction>() {
							@Override
							public void onDone(BaseGeneratedFunction result) {
								GeneratedConstructor constructorDef = (GeneratedConstructor) result;

								@NotNull BaseFunctionDef ele = constructorDef.getFD();

								try {
									LookupResultList lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele.getContext(), DeduceTypes2.this);
									OS_Element best = lrl.chooseBest(null);
									ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
								} catch (ResolveError aResolveError) {
									aResolveError.printStackTrace();
									errSink.reportDiagnostic(aResolveError);
								}
							}
						});
					}
				} else
					throw new NotImplementedException();
			} else {
				LOG.info("1621");
				LookupResultList lrl = null;
				try {
					lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ctx, DeduceTypes2.this);
					OS_Element best = lrl.chooseBest(null);
					assert best != null;
					ite.setResolvedElement(best);
					found_element_for_ite(null, ite, best, ctx);
//						ite.setStatus(BaseTableEntry.Status.KNOWN, best);
				} catch (ResolveError aResolveError) {
					aResolveError.printStackTrace();
				}
			}
		}

		private void onChangeVTE(VariableTableEntry vte) {
			@NotNull ArrayList<TypeTableEntry> pot = getPotentialTypesVte(vte);
			if (vte.getStatus() == BaseTableEntry.Status.KNOWN && vte.type.getAttached() != null && vte.el != null) {

				final OS_Type ty = vte.type.getAttached();

				OS_Element ele2 = null;

				try {
					if (ty.getType() == OS_Type.Type.USER) {
						@NotNull OS_Type ty2 = resolve_type(ty, ty.getTypeName().getContext());
						OS_Element ele;
						if (vte.type.genType.resolved == null) {
							if (ty2.getType() == OS_Type.Type.USER_CLASS) {
								vte.type.genType.resolved = ty2;
							}
						}
						ele = ty2.getElement();
						LookupResultList lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele.getContext(), DeduceTypes2.this);
						ele2 = lrl.chooseBest(null);
					} else
						ele2 = ty.getClassOf(); // TODO might fail later (use getElement?)

					LookupResultList lrl = null;

					lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele2.getContext(), DeduceTypes2.this);
					OS_Element best = lrl.chooseBest(null);
					// README commented out because only firing for dir.listFiles, and we always use `best'
//					if (best != ele2) LOG.err(String.format("2824 Divergent for %s, %s and %s", ite, best, ele2));;
					ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
				} catch (ResolveError aResolveError) {
					aResolveError.printStackTrace();
					errSink.reportDiagnostic(aResolveError);
				}
			} else if (pot.size() == 1) {
				TypeTableEntry tte = pot.get(0);
				@Nullable OS_Type ty = tte.getAttached();
				if (ty != null) {
					switch (ty.getType()) {
					case USER:
						vte_pot_size_is_1_USER_TYPE(vte, ty);
						break;
					case USER_CLASS:
						vte_pot_size_is_1_USER_CLASS_TYPE(vte, ty);
						break;
					}
				} else {
					LOG.err("1696");
				}
			}
		}

		private void vte_pot_size_is_1_USER_CLASS_TYPE(VariableTableEntry vte, @Nullable OS_Type aTy) {
			ClassStatement klass = aTy.getClassOf();
			LookupResultList lrl = null;
			try {
				lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), klass.getContext(), DeduceTypes2.this);
				OS_Element best = lrl.chooseBest(null);
//							ite.setStatus(BaseTableEntry.Status.KNOWN, best);
				assert best != null;
				ite.setResolvedElement(best);

				final GenType genType = new GenType(klass);
				final TypeName typeName = vte.type.genType.nonGenericTypeName;
				final ClassInvocation ci = genCI(genType, typeName);
//							resolve_vte_for_class(vte, klass);
				ci.resolvePromise().done(new DoneCallback<GeneratedClass>() {
					@Override
					public void onDone(GeneratedClass result) {
						vte.resolveType(result);
					}
				});
			} catch (ResolveError aResolveError) {
				errSink.reportDiagnostic(aResolveError);
			}
		}

		private void vte_pot_size_is_1_USER_TYPE(VariableTableEntry vte, @Nullable OS_Type aTy) {
			try {
				@NotNull OS_Type ty2 = resolve_type(aTy, aTy.getTypeName().getContext());
				// TODO ite.setAttached(ty2) ??
				OS_Element ele = ty2.getElement();
				LookupResultList lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele.getContext(), DeduceTypes2.this);
				OS_Element best = lrl.chooseBest(null);
				ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
//									ite.setResolvedElement(best);

				final ClassStatement klass = (ClassStatement) ele;

				register_and_resolve(vte, klass);
			} catch (ResolveError resolveError) {
				errSink.reportDiagnostic(resolveError);
			}
		}
	}

	public void register_and_resolve(VariableTableEntry aVte, ClassStatement aKlass) {
		ClassInvocation ci = new ClassInvocation(aKlass, null);
		ci = phase.registerClassInvocation(ci);
		ci.resolvePromise().done(new DoneCallback<GeneratedClass>() {
			@Override
			public void onDone(GeneratedClass result) {
				aVte.resolveType(result);
			}
		});
	}
}

//
//
//
