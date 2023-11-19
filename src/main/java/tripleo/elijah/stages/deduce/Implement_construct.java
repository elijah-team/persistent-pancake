package tripleo.elijah.stages.deduce;

import java.util.Collection;
import java.util.Objects;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tripleo.elijah.DebugFlags;
import tripleo.elijah.contexts.ClassContext;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.ConstructorDef;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.DecideElObjectType;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.NormalTypeName;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.TypeName;
import tripleo.elijah.lang.VariableStatement;
import tripleo.elijah.lang.types.OS_UserType;
import tripleo.elijah.stages.deduce.percy.DeduceElement3_LookingUpCtx;
import tripleo.elijah.stages.deduce.percy.PFInvocation;
import tripleo.elijah.stages.deduce.percy.PFluffyClassStatement;
import tripleo.elijah.stages.deduce.percy.PFluffyClassStatementImpl;
import tripleo.elijah.stages.deduce.percy.PercyWantConstructor;
import tripleo.elijah.stages.deduce.percy.Provided;
import tripleo.elijah.stages.deduce.post_bytecode.DeduceElement3_IdentTableEntry;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.Constructable;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.IdentTableEntry;
import tripleo.elijah.stages.gen_fn.ProcTableEntry;
import tripleo.elijah.stages.gen_fn.TypeTableEntry;
import tripleo.elijah.stages.gen_fn.VariableTableEntry;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.Instruction;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.InstructionName;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.ProcIA;
import tripleo.elijah.stages.instructions.VariableTableType;
import tripleo.elijah.util.NotImplementedException;

public class Implement_construct {

	private final DeduceTypes2          deduceTypes2;
	private final BaseGeneratedFunction generatedFunction;
	private final Instruction           instruction;
	private final InstructionArgument   expression;

	private final @NotNull ProcTableEntry                 pte;
	private                Provided<PercyWantConstructor> ppwc;

	public Implement_construct(final DeduceTypes2 aDeduceTypes2, final BaseGeneratedFunction aGeneratedFunction, final Instruction aInstruction) {
		deduceTypes2      = aDeduceTypes2;
		generatedFunction = aGeneratedFunction;
		instruction       = aInstruction;

		// README all these asserts are redundant, I know
		assert instruction.getName() == InstructionName.CONSTRUCT;
		assert instruction.getArg(0) instanceof ProcIA;

		final int pte_num = ((ProcIA) instruction.getArg(0)).getIndex();
		pte = generatedFunction.getProcTableEntry(pte_num);

		expression = pte.expression_num;

		assert expression instanceof IntegerIA || expression instanceof IdentIA;
	}

	public void action(Provided<PercyWantConstructor> ppwc0) {
		this.ppwc = ppwc0;
		// TODO 11/17 needs eventualregister
		ppwc.on(pwc -> {
			if (expression instanceof IntegerIA) {
				action_IntegerIA(pwc);
			} else if (expression instanceof IdentIA) {
				action_IdentIA(pwc);
			} else {
				throw new NotImplementedException();
			}
		});
	}

	public void action_IdentIA(final PercyWantConstructor aPwc) {
		@NotNull final IdentTableEntry idte       = ((IdentIA) expression).getEntry();
		final DeducePath               deducePath = idte.buildDeducePath(generatedFunction, deduceTypes2.resolver());
		{
			@Nullable OS_Element el3;
			@Nullable Context    ectx = generatedFunction.getFD().getContext();
			for (int i = 0; i < deducePath.size(); i++) {
				final InstructionArgument ia2 = deducePath.getIA(i);

				if (ia2 instanceof IdentIA identIA) {
					@NotNull final IdentTableEntry identTableEntry = identIA.getEntry();
					if (identTableEntry.getIdent().getText().equals("x")) {
						NotImplementedException.raise_stop();
					}
				}

				// TODO 11/17 controlplane??

				el3 = deducePath.getElement(i);

				if (ia2 instanceof IntegerIA) {
					@NotNull final VariableTableEntry vte = ((IntegerIA) ia2).getEntry();
					// TODO will fail if we try to construct a tmp var, but we never try to do that
					assert vte.vtt != VariableTableType.TEMP;
					assert el3 != null;
					assert i == 0;
					ectx = deducePath.getContext(i);

					if (ectx instanceof DeducePath.MemberContext mc) {
						// README should be equiv
						//ppwc.on(mc::setPwc);
						mc.setPwc(aPwc);
					}
				} else if (ia2 instanceof IdentIA) {
					@NotNull final IdentTableEntry idte2 = ((IdentIA) ia2).getEntry();

					final DeduceElement3_IdentTableEntry de3_ite = idte2.getDeduceElement3(deduceTypes2, generatedFunction);
					final DeduceElement3_LookingUpCtx    luck    = de3_ite.lookingUp(/*ia2,*/ ectx, deduceTypes2);

					luck.onSuccess((a) -> {
						final OS_Element el4 = luck.getElement();

						//					ppwc.on(pwc -> { pwc.});

						if (el4 instanceof VariableStatement vs) {
							final @NotNull TypeName tn = vs.typeName();
							final @NotNull OS_Type  ty = new OS_UserType(tn);
							final String            s  = idte2.getIdent().toString();

							if (idte2.getType() == null) {
								// README Don't remember enough about the constructors to select a different one
								@NotNull final TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, ty);
								try {
									@NotNull final GenType resolved = deduceTypes2.newPFluffyType().resolve_type(ty, tn.getContext());
									deduceTypes2._LOG().err("892 resolved: " + resolved);
									tte.setAttached(resolved);
								} catch (final ResolveError aResolveError) {
									deduceTypes2._errSink().reportDiagnostic(aResolveError);
								}

								idte2.setType(tte);
							}
							// s is constructor name
							implement_construct_type(idte2, ty, s);
						}
					});

					final String               s   = idte2.getIdent().toString();
					final LookupResultList     lrl = ectx.lookup(s);
					@Nullable final OS_Element el2 = lrl.chooseBest(null);

					if (DebugFlags.classInstantiation2) {
//						assert el2 instanceof ConstructorDef;
					}


					if (el2 == null) {
//						assert el3 != null;
						if (!(el3 instanceof VariableStatement vs)) {
//							throw new AssertionError();
							System.err.println("FAIL 139");

							if (aPwc.getEventualConstructorDef().isPending()) {
								if (aPwc.getEnclosingGenType() != null) {
									aPwc.onResolver(resolver -> {
										NotImplementedException.raise_stop();
									});
								}
							}

						} else {
							luck.force(vs);
							return;
						}
					} else {
						if (i + 1 == deducePath.size()) {
							if (el3 != null ) {
								assert el3 == el2;
								if (el2 instanceof ConstructorDef) {
									@Nullable final GenType type = deducePath.getType(i);

									if (type != null) {
										if (type.getNonGenericTypeName() == null) {
											final GenType genType = (deducePath.getType(i - 1));
											if (genType != null) {
												type.setNonGenericTypeName(genType.getNonGenericTypeName()); // HACK. not guararnteed to work!
											}
										}
										@NotNull final OS_Type ty = new OS_UserType(type.getNonGenericTypeName());
										implement_construct_type(idte2, ty, s);

										ppwc.on(pwc -> pwc.provide((ConstructorDef) el2));
									}
								}
							} else {
								ppwc.on(pwc -> {
									luck.force(el2);
//									pwc.provide((VariableStatement) el2);
								});
							}
						} else {
							ectx = deducePath.getContext(i);
							if (ectx instanceof DeducePath.MemberContext mc) {
								// README should be equiv
								//ppwc.on(mc::setPwc);
								mc.setPwc(aPwc);
							}
						}
					}
//						implement_construct_type(idte/*??*/, ty, null); // TODO how bout when there is no ctor name
				} else {
					throw new NotImplementedException();
				}
			}
		}

		if (aPwc.getEventualConstructorDef().isPending()) {
			//assert false;
		}
	}

	public void action_IntegerIA(final PercyWantConstructor aPwc) {
		@NotNull final VariableTableEntry vte = generatedFunction.getVarTableEntry(((IntegerIA) expression).getIndex());
		assert vte.type.getAttached() != null; // TODO will fail when empty variable expression
		@Nullable final OS_Type ty = vte.type.getAttached();
		implement_construct_type(vte, ty, null);
	}

	private void implement_construct_type(@Nullable final Constructable co, @Nullable final OS_Type aTy, final String constructorName) {
		if (aTy != null) {
			switch (aTy.getType()) {
			case USER -> {
				final TypeName tyn = aTy.getTypeName();
				if (tyn instanceof NormalTypeName) {
					final @NotNull NormalTypeName tyn1 = (NormalTypeName) tyn;
					_implement_construct_type(co, constructorName, tyn1);
				}
			}
			default -> {
				throw new NotImplementedException();
	//			return;
			}
			}
			if (co != null) {
				co.setConstructable(pte);
				pte.onClassInvocation(classInvocation -> classInvocation.resolvePromise().done(co::resolveTypeToClass));
			}
		} else {
			throw new AssertionError();
		}
	}

	private void _implement_construct_type(@Nullable final Constructable co, @Nullable final String constructorName, @NotNull final NormalTypeName aTyn1) {
		final String               s    = aTyn1.getName();
		final LookupResultList     lrl  = aTyn1.getContext().lookup(s);
		@Nullable final OS_Element best = lrl.chooseBest(null);

		switch (DecideElObjectType.getElObjectType(best)) {
		case CLASS -> {
			final ClassStatement    classStatement = (ClassStatement) best;
			final ConstructableHook ch             = new ConstructableHook(co);
			_ict_ClassStatement(ch, constructorName, aTyn1, classStatement);
		}
		case TYPE_NAME_ELEMENT -> {
			final ClassContext.OS_TypeNameElement typeNameElement = (ClassContext.OS_TypeNameElement) best;
			_ict_TypeNameElement(co, constructorName, aTyn1, typeNameElement);
		}

		default -> {
			throw new IllegalStateException("Unexpected value: " + DecideElObjectType.getElObjectType(best));
		}
		}
	}

	private void _ict_ClassStatement(final @Nullable ConstructableHook ch,
	                                 final @Nullable String constructorName,
	                                 final @NotNull NormalTypeName aTyn1,
	                                 final ClassStatement classStatement) {
		final PFInvocation.Setter[] ss = {null};

		final PFInvocation pinv = deduceTypes2.get(generatedFunction).makeInvocation(classStatement);

		pinv.setter((final PFInvocation.Setter s)->{
			ss[0] = s;

			PFluffyClassStatement fcs = new PFluffyClassStatementImpl(classStatement);
			fcs.extracted(constructorName, aTyn1, s, deduceTypes2);
		});

		var b = classStatement.getContext(); // ??

		ss[0].hookClassInvocation(clsinv21 -> ch.invoke2(clsinv21, deduceTypes2, b, generatedFunction));

		ss[0].hookClassInvocation(clsinv2 -> {
			pte.setClassInvocation(clsinv2);
			pte.setResolvedElement(classStatement);

			// set FunctionInvocation with pte args
			{
				@Nullable ConstructorDef cc = null;
				if (constructorName != null) {
					final Collection<ConstructorDef> cs = classStatement.getConstructors();
					for (@NotNull final ConstructorDef c : cs) {
						if (c.name().equals(constructorName)) {
							cc = c;
							break;
						}
					}
				}
				// TODO also check arguments
				{
					assert cc != null || pte.getArgs().size() == 0;
					@NotNull final FunctionInvocation fi = deduceTypes2.newFunctionInvocation(cc, pte, clsinv2, deduceTypes2._phase());
					pte.setFunctionInvocation(fi);
				}
			}
		});
	}

	private void _ict_TypeNameElement(final @Nullable Constructable co,
	                                  final @Nullable String constructorName,
	                                  final @NotNull NormalTypeName aTyn1,
	                                  final ClassContext.OS_TypeNameElement aTypeNameElement) {
		@Nullable ClassInvocation     clsinv = null;

		@NotNull final IdentTableEntry ite = ((IdentIA) this.expression).getEntry();

		final ClassStatement        classStatement = (ClassStatement) generatedFunction.getFD().getParent();

		// FIXME 11/18 invocation table, aka prte_list
		final PFInvocation          pinv   = deduceTypes2.get(generatedFunction).makeInvocation(classStatement);
		final PFInvocation.Setter[] ss     = { null };

		pinv.setter((final PFInvocation.Setter s) -> {
			ss[0] = s;

			PFluffyClassStatement fcs = new PFluffyClassStatementImpl(classStatement);
			fcs.extracted(constructorName, aTyn1, s, deduceTypes2);
		});

		int y = 2;

		if (ite.getType().getGenType() == null) {
			ite.makeType(generatedFunction, TypeTableEntry.Type.TRANSIENT, (OS_Type) null);
			final GenType gt = ite.getType().getGenType();
			assert gt != null;
		}

		assert clsinv != null;

		clsinv = deduceTypes2._phase().registerClassInvocation(clsinv);
		if (co != null) {
			if (co instanceof IdentTableEntry) {
				final @Nullable IdentTableEntry idte3 = (IdentTableEntry) co;
				idte3.getType().genTypeCI(clsinv);
				clsinv.resolvePromise().then(new DoneCallback<GeneratedClass>() {
					@Override
					public void onDone(final GeneratedClass result) {
						idte3.resolveTypeToClass(result);
					}
				});
			} else if (co instanceof VariableTableEntry) {
				final @NotNull VariableTableEntry vte = (VariableTableEntry) co;
				vte.type.genTypeCI(clsinv);
				clsinv.resolvePromise().then(new DoneCallback<GeneratedClass>() {
					@Override
					public void onDone(final GeneratedClass result) {
						vte.resolveTypeToClass(result);
					}
				});
			}
		}
		pte.setClassInvocation(clsinv);
		pte.setResolvedElement(aTypeNameElement);
		// set FunctionInvocation with pte args
		{
			@Nullable ConstructorDef cc = null;
//			if (constructorName != null) {
//				final Collection<ConstructorDef> cs = aTypeNameElement.getConstructors();
//				for (@NotNull final ConstructorDef c : cs) {
//					if (c.name().equals(constructorName)) {
//						cc = c;
//						break;
//					}
//				}
//			}
			// TODO also check arguments
			{
				assert cc != null || pte.getArgs().size() == 0;
				@NotNull final FunctionInvocation fi = deduceTypes2.newFunctionInvocation(cc, pte, clsinv, deduceTypes2._phase());
				pte.setFunctionInvocation(fi);
			}
		}
	}
}
