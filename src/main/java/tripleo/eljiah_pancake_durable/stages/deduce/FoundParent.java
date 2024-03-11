package tripleo.eljiah_pancake_durable.stages.deduce;

import org.jdeferred2.DoneCallback;
import org.jdeferred2.Promise;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.eljiah_pancake_durable.comp.ErrSink;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.eljiah_pancake_durable.lang.BaseFunctionDef;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.ConstructorDef;
import tripleo.eljiah_pancake_durable.lang.Context;
import tripleo.eljiah_pancake_durable.lang.LookupResultList;
import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.lang.OS_Type;
import tripleo.eljiah_pancake_durable.lang.TypeName;
import tripleo.eljiah_pancake_durable.lang.VariableStatement;
import tripleo.eljiah_pancake_durable.lang.types.OS_UserType;
import tripleo.eljiah_pancake_durable.stages.deduce.percy.DeduceTypeResolve2;
import tripleo.eljiah_pancake_durable.stages.deduce.zero.ITE_Zero;
import tripleo.eljiah_pancake_durable.stages.deduce.zero.PTE_Zero;
import tripleo.eljiah_pancake_durable.stages.deduce.zero.VTE_Zero;
import tripleo.eljiah_pancake_durable.stages.deduce.zero.Zero_PotentialTypes;
import tripleo.eljiah_pancake_durable.stages.gen_fn.BaseGeneratedFunction;
import tripleo.eljiah_pancake_durable.stages.gen_fn.BaseTableEntry;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenType;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedConstructor;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenericElementHolderWithType;
import tripleo.eljiah_pancake_durable.stages.gen_fn.IElementHolder;
import tripleo.eljiah_pancake_durable.stages.gen_fn.IdentTableEntry;
import tripleo.eljiah_pancake_durable.stages.gen_fn.ProcTableEntry;
import tripleo.eljiah_pancake_durable.stages.gen_fn.TypeTableEntry;
import tripleo.eljiah_pancake_durable.stages.gen_fn.VariableTableEntry;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;

public class FoundParent implements BaseTableEntry.StatusListener {
	private final DeduceTypes2          deduceTypes2;
	private final BaseTableEntry        bte;
	private final IdentTableEntry       ite;
	private final Context               ctx;
	private final BaseGeneratedFunction generatedFunction;

	@Contract(pure = true)
	public FoundParent(final DeduceTypes2 aDeduceTypes2, final BaseTableEntry aBte, final IdentTableEntry aIte, final Context aCtx, final BaseGeneratedFunction aGeneratedFunction) {
		deduceTypes2      = aDeduceTypes2;
		bte               = aBte;
		ite               = aIte;
		ctx               = aCtx;
		generatedFunction = aGeneratedFunction;
	}

	@Override
	public void onChange(final IElementHolder eh, final BaseTableEntry.Status newStatus) {
		if (newStatus == BaseTableEntry.Status.KNOWN) {
			if (bte instanceof VariableTableEntry) {
				final @NotNull VariableTableEntry vte = (VariableTableEntry) bte;
				onChangeVTE(vte);
			} else if (bte instanceof ProcTableEntry) {
				final @NotNull ProcTableEntry pte = (ProcTableEntry) bte;
				onChangePTE(pte);
			} else if (bte instanceof IdentTableEntry) {
				final @NotNull IdentTableEntry ite = (IdentTableEntry) bte;

				final ErrSink errSink = deduceTypes2._errSink();

				onChangeITE(ite.zero(), ite, errSink);
			}
			postOnChange(eh, deduceTypes2.resolver());
		}
	}

	private void onChangeVTE(@NotNull final VariableTableEntry vte) {
		@NotNull final ArrayList<TypeTableEntry> pot = deduceTypes2.getPotentialTypesVte(vte);

		final VTE_Zero            zero = vte.zero();
		final Zero_PotentialTypes pot1 = zero.potentialTypes();

		final ErrSink              errSink = deduceTypes2._errSink();
		@NotNull final DeducePhase phase   = deduceTypes2._phase();

		if (vte.getStatus() == BaseTableEntry.Status.KNOWN && vte.type.getAttached() != null && vte.getResolvedElement() != null) {
			zero.fp_onChange__001(vte.type, ite, deduceTypes2, errSink);
		} else if (pot.size() == 1) {
			final TypeTableEntry    tte = pot.get(0);
			@Nullable final OS_Type ty  = tte.getAttached();
			zero.fp_onChange__002(vte, ty, deduceTypes2, ite, errSink, phase);
		}
	}

	private void onChangePTE(@NotNull final ProcTableEntry aPte) {
		if (aPte.getStatus() == BaseTableEntry.Status.KNOWN) { // TODO might be obvious
			final FunctionInvocation fi = aPte.getFunctionInvocation();

			if (fi == null) {
				throw new NotImplementedException();
			}

			final BaseFunctionDef fd = fi.getFunction();
			if (fd instanceof ConstructorDef) {
				fi.generatePromise()
				  .then((final BaseGeneratedFunction result) -> {
					  /*
					   * 1. Create Zero if not created
					   * 2. Get Promise, which is created automatically, because on some level, the purpose of zero
					   *    is to calculate this
					   * 3. Calculate because we now have all the information necessary to do it
					   * 4. Hook up pass/fail callbacks
					   */
					  final PTE_Zero                                  zero  = aPte.zero();
					  final Promise<IElementHolder, Diagnostic, Void> bestP = zero.foundCounstructorPromise();

					  zero.calculateConstructor((GeneratedConstructor) result, ite, deduceTypes2);

					  bestP.done(best -> ite.setStatus(BaseTableEntry.Status.KNOWN, best));
					  bestP.fail(err -> deduceTypes2._errSink().reportDiagnostic(err));
				  });
			}
		} else {
			deduceTypes2._LOG().info("1621");
			@Nullable LookupResultList lrl = null;
			try {
				lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ctx, deduceTypes2);
				@Nullable final OS_Element best = lrl.chooseBest(null);
				assert best != null;
				ite.setResolvedElement(best);
				deduceTypes2.found_element_for_ite(null, ite, best, ctx);
//						ite.setStatus(BaseTableEntry.Status.KNOWN, best);
			} catch (final ResolveError aResolveError) {
				aResolveError.printStackTrace();
			}
		}
	}

	private void onChangeITE(final ITE_Zero zero, @NotNull final IdentTableEntry identTableEntry, final ErrSink errSink) {
		if (identTableEntry.getType() != null) {
			zero.fp_onChange__001(identTableEntry.getType(), this.ite, deduceTypes2, errSink);
		} else {
			if (!identTableEntry.isFefi()) {
				final Found_Element_For_ITE fefi = new Found_Element_For_ITE(generatedFunction, ctx, deduceTypes2._LOG(), deduceTypes2._errSink(), new DeduceTypes2.DeduceClient1(deduceTypes2));
				fefi.action(identTableEntry);
				identTableEntry.setFefi(true);
				identTableEntry.onFefiDone(new DoneCallback<GenType>() {
					@Override
					public void onDone(final GenType result) {

						try {
							final ClassStatement   resolvedClassOf = result.getResolved().getClassOf();
							final LookupResultList lrl             = DeduceLookupUtils.lookupExpression(ite.getIdent(), resolvedClassOf.getContext(), deduceTypes2);

/*
							String ite_text = ite.getIdent().getText();
							String res_text     = resolvedClassOf.getName();
							assert ite_text.equals(res_text); // TODO no way this could pass
*/

							final OS_Element ele2 = lrl.chooseBest(null);


							if (ele2 != null) {
								ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolderWithType(ele2, result, deduceTypes2));
								ite.resolveTypeToClass(result.getNode());
							}
						} catch (final ResolveError aResolveError) {
//							aResolveError.printStackTrace();
							errSink.reportDiagnostic(aResolveError);
						}
					}
				});
			}
			// TODO we want to setStatus but have no USER or USER_CLASS to perform lookup with
//			identTableEntry.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(null));
		}
	}

	/* @ensures ite.type != null */
	private void postOnChange(@NotNull final IElementHolder eh, final DeduceTypeResolve2 aResolver) {
		if (ite.getType() == null && eh.getElement() instanceof VariableStatement) {
			@NotNull final TypeName typ = ((VariableStatement) eh.getElement()).typeName();
			@NotNull final OS_Type  ty  = new OS_UserType(typ);

			try {
				@Nullable final GenType ty2 = getTY2(typ, ty, aResolver);

				// no expression or TableEntryIV below
				if (ty2 != null) {
					final @NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null);
					// trying to keep genType up to date
					tte.setAttached(ty, aResolver);
					tte.setAttached(ty2);
					ite.setType(tte);
				}
			} catch (final ResolveError aResolveError) {
				deduceTypes2._errSink().reportDiagnostic(aResolveError);
			}
		}
	}

	private @Nullable GenType getTY2(@NotNull final TypeName aTyp, final @NotNull OS_Type ty, final DeduceTypeResolve2 aResolver) throws ResolveError {
		if (ty.getType() != OS_Type.Type.USER) {
			assert false;
			@NotNull final GenType genType = new GenType(aResolver);
			genType.set(ty);
			return genType;
		}

		@Nullable GenType ty2 = null;
		if (!aTyp.isNull()) {
			assert ty.getTypeName() != null;
			ty2 = deduceTypes2.newPFluffyType().resolve_type(ty, ty.getTypeName().getContext());
			return ty2;
		}

		if (bte instanceof VariableTableEntry) {
			final OS_Type attached = ((VariableTableEntry) bte).type.getAttached();
			if (attached == null) {
				type_is_null_and_attached_is_null_vte();
				// ty2 will probably be null here
			} else {
				ty2 = new GenType(aResolver);
				ty2.set(attached);
			}
		} else if (bte instanceof IdentTableEntry) {
			final TypeTableEntry tte = ((IdentTableEntry) bte).getType();
			if (tte != null) {
				final OS_Type attached = tte.getAttached();

				if (attached == null) {
					type_is_null_and_attached_is_null_ite((IdentTableEntry) bte);
					// ty2 will be null here
				} else {
					ty2 = new GenType(aResolver);
					ty2.set(attached);
				}
			}
		}

		return ty2;
	}

	private void type_is_null_and_attached_is_null_vte() {
		//LOG.err("2842 attached == null for "+((VariableTableEntry) bte).type);
		@NotNull final DeduceTypes2.PromiseExpectation<GenType> pe = deduceTypes2.promiseExpectation((VariableTableEntry) bte, "Null USER type attached resolved");
		((VariableTableEntry) bte).typePromise().then(new DoneCallback<GenType>() {
			@Override
			public void onDone(@NotNull final GenType result) {
				pe.satisfy(result);
				final OS_Type attached1 = result.getResolved() != null ? result.getResolved() : result.getTypeName();
				if (attached1 != null) {
					switch (attached1.getType()) {
						case USER_CLASS:
							ite.setType(generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached1));
							break;
						case USER:
							try {
								@NotNull final GenType ty3 = deduceTypes2.newPFluffyType().resolve_type(attached1, attached1.getTypeName().getContext());
								// no expression or TableEntryIV below
								@NotNull final TypeTableEntry tte4 = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null);
								// README trying to keep genType up to date
								tte4.setAttached(attached1, deduceTypes2.resolver());
								tte4.setAttached(ty3);
								ite.setType(tte4); // or ty2?
							} catch (final ResolveError aResolveError) {
								aResolveError.printStackTrace();
							}
							break;
					}
				}
			}
		});
	}

	private void type_is_null_and_attached_is_null_ite(final IdentTableEntry ite) {
//			PromiseExpectation<GenType> pe = promiseExpectation(ite, "Null USER type attached resolved");
//			ite.typePromise().done(new DoneCallback<GenType>() {
//				@Override
//				public void onDone(GenType result) {
//					pe.satisfy(result);
//					final OS_Type attached1 = result.resolved != null ? result.resolved : result.typeName;
//					if (attached1 != null) {
//						switch (attached1.getType()) {
//						case USER_CLASS:
//							FoundParent.this.ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached1);
//							break;
//						case USER:
//							try {
//								OS_Type ty3 = resolve_type(attached1, attached1.getTypeName().getContext());
//								// no expression or TableEntryIV below
//								@NotNull TypeTableEntry tte4 = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null);
//								// README trying to keep genType up to date
//								tte4.setAttached(attached1);
//								tte4.setAttached(ty3);
//								FoundParent.this.ite.type = tte4; // or ty2?
//							} catch (ResolveError aResolveError) {
//								aResolveError.printStackTrace();
//							}
//							break;
//						}
//					}
//				}
//			});
	}
}