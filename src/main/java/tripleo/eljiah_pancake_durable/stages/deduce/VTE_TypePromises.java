/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: t; c-basic-offset: 4 -*- */
/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.eljiah_pancake_durable.stages.deduce;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.Eventual;
import tripleo.eljiah_pancake_durable.lang.BaseFunctionDef;
import tripleo.eljiah_pancake_durable.lang.Context;
import tripleo.eljiah_pancake_durable.lang.FunctionDef;
import tripleo.eljiah_pancake_durable.lang.LookupResultList;
import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.lang.OS_Type;
import tripleo.eljiah_pancake_durable.lang.VariableStatement;
import tripleo.eljiah_pancake_durable.stages.gen_fn.AbstractDependencyTracker;
import tripleo.eljiah_pancake_durable.stages.gen_fn.BaseGeneratedFunction;
import tripleo.eljiah_pancake_durable.stages.gen_fn.Constructable;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenType;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedClass;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedContainer;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedFunction;
import tripleo.eljiah_pancake_durable.stages.gen_fn.IdentTableEntry;
import tripleo.eljiah_pancake_durable.stages.gen_fn.ProcTableEntry;
import tripleo.eljiah_pancake_durable.stages.gen_fn.TypeTableEntry;
import tripleo.eljiah_pancake_durable.stages.gen_fn.VariableTableEntry;
import tripleo.eljiah_pancake_durable.stages.instructions.IntegerIA;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.SimplePrintLoggerToRemoveSoon;

/**
 * Created 11/27/21 12:51 PM
 */
public class VTE_TypePromises {

	// region ProcTableListener

	static void resolved_element_pte(final Constructable co,
	                                 final ProcTableEntry pte,
	                                 final AbstractDependencyTracker depTracker,
	                                 final @NotNull FunctionDef fd,
	                                 final @NotNull VariableTableEntry aVariableTableEntry,
	                                 final ProcTableListener aProcTableListener) {
		aVariableTableEntry.typePromise().then(new DoneCallback<GenType>() {
			@Override
			public void onDone(@NotNull final GenType result) {
				assert result.getResolved().getClassOf() == fd.getParent();

				@NotNull final ProcTableListener.E_Is_FunctionDef e_Is_FunctionDef = aProcTableListener.new E_Is_FunctionDef(
				  pte, fd, fd.getParent()).invoke(aVariableTableEntry.type.getGenType(null).getNonGenericTypeName());
				@Nullable final FunctionInvocation fi      = e_Is_FunctionDef.getFi();
				final GenType                      genType = e_Is_FunctionDef.getGenType();
				aProcTableListener.finish(co, depTracker, fi, genType);
			}
		});
	}

	static void resolved_element_pte_VariableStatement(final Constructable co,
	                                                   final AbstractDependencyTracker depTracker,
	                                                   final @NotNull FunctionDef fd,
	                                                   final @NotNull VariableStatement variableStatement,
	                                                   final @NotNull ProcTableEntry aProcTableEntry,
	                                                   final ClassInvocation aCi,
	                                                   final ProcTableListener aProcTableListener) {
		aCi.resolvePromise().done(new DoneCallback<GeneratedClass>() {
			@Override
			public void onDone(final GeneratedClass result) {
				for (final GeneratedContainer.VarTableEntry varTableEntry : result.varTable) {
					if (varTableEntry.nameToken.getText().equals(variableStatement.getName())) {
						assert varTableEntry.varType.getClassOf() == fd.getParent();

						@NotNull final ProcTableListener.E_Is_FunctionDef e_Is_FunctionDef = aProcTableListener.new E_Is_FunctionDef(aProcTableEntry, fd, fd.getParent()).invoke(null/*variableTableEntry.type.genType.nonGenericTypeName*/);
						@Nullable final FunctionInvocation                fi1              = e_Is_FunctionDef.getFi();
						final GenType                                     genType1         = e_Is_FunctionDef.getGenType();
						aProcTableListener.finish(co, depTracker, fi1, genType1);

						break;
					}
				}
			}
		});
	}

	static void resolved_element_pte_VariableStatement2(final Constructable co,
	                                                    final AbstractDependencyTracker depTracker,
	                                                    final ProcTableEntry pte,
	                                                    final @NotNull FunctionDef fd,
	                                                    final @NotNull VariableTableEntry aVariableTableEntry,
	                                                    final ProcTableListener aProcTableListener) {
		aVariableTableEntry.typePromise().then(new DoneCallback<GenType>() {
			@Override
			public void onDone(@NotNull final GenType result) {
				if (result.getResolved().getClassOf() != fd.getParent()) {
					SimplePrintLoggerToRemoveSoon.println_err2("** Failed assertion");
				}

				@NotNull final ProcTableListener.E_Is_FunctionDef e_Is_FunctionDef = aProcTableListener.new E_Is_FunctionDef(pte, fd, fd.getParent()).invoke(aVariableTableEntry.type.getGenType(null).getNonGenericTypeName());
				@Nullable final FunctionInvocation                fi               = e_Is_FunctionDef.getFi();
				final GenType                                     genType          = e_Is_FunctionDef.getGenType();
				aProcTableListener.finish(co, depTracker, fi, genType);
			}
		});
	}

	// endregion ProcTableListener

	// region DeduceTypes2

	static void getItemFali(final @NotNull BaseGeneratedFunction generatedFunction,
	                        final @NotNull Context ctx,
	                        final @NotNull VariableTableEntry aVte2,
	                        final @NotNull DeduceTypes2 aDeduceTypes2) {
		aVte2.typePromise().then(new DoneCallback<GenType>() {
			@Override
			public void onDone(@NotNull final GenType result) {
				final @Nullable OS_Type ty2 = result.getTypeName()/*.getAttached()*/;
				assert ty2 != null;
				@NotNull GenType rtype = null;
				try {
					rtype = aDeduceTypes2.resolve_type(ty2, ctx);
				} catch (final ResolveError resolveError) {
					aDeduceTypes2.errSink.reportError("Cant resolve " + ty2); // TODO print better diagnostic
					return;
				}
				if (rtype.getResolved() != null && rtype.getResolved().getType() == OS_Type.Type.USER_CLASS) {
					final LookupResultList     lrl2  = rtype.getResolved().getClassOf().getContext().lookup("__getitem__");
					@Nullable final OS_Element best2 = lrl2.chooseBest(null);
					if (best2 != null) {
						if (best2 instanceof FunctionDef) {
							@Nullable final FunctionDef    fd         = (FunctionDef) best2;
							@Nullable final ProcTableEntry pte        = null;
							final IInvocation              invocation = aDeduceTypes2.getInvocation((GeneratedFunction) generatedFunction);
							aDeduceTypes2.forFunction(aDeduceTypes2.newFunctionInvocation(fd, pte, invocation, aDeduceTypes2.phase), new ForFunction() {
								@Override
								public void typeDecided(final @NotNull GenType aType) {
									assert fd == generatedFunction.getFD();
									//
									@NotNull final TypeTableEntry tte1 = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, aDeduceTypes2.gt(aType), aVte2); // TODO expression?
									aVte2.type = tte1;
								}
							});
						} else {
							throw new NotImplementedException();
						}
					} else {
						throw new NotImplementedException();
					}
				}
			}
		});
	}

	static Eventual<GenType> do_assign_call_args_ident_vte_promise(final @NotNull TypeTableEntry aTte, final @NotNull VariableTableEntry aVte1) {
		final var p = aVte1.typePromise();
		p.then(new DoneCallback<GenType>() {
			@Override
			public void onDone(final GenType result) {
//					assert vte != vte1;
//					aTte.setAttached(result.resolved != null ? result.resolved : result.typeName);
				aTte.getGenType(null).copy(result);
//					vte.addPotentialType(aInstructionIndex, result); // TODO!!
			}
		});
		return p;
	}

	// endregion DeduceTypes2

	static void dunder(final String pn, final IntegerIA aIntegerIA, final ProcTableEntry pte, final DeduceTypes2 aDeduceTypes2) {
		aIntegerIA.getEntry().typePromise().then(new DoneCallback<GenType>() {
			@Override
			public void onDone(@NotNull final GenType result) {
				final boolean found1 = aDeduceTypes2.lookup_name_calls(result.getResolved().getClassOf().getContext(), pn, pte);
				if (found1) {
					final int y = 2;
//					tripleo.elijah.util.Stupidity.println2("3071 "+pte.getStatus());
					final IInvocation invocation = result.getCi();
//							final BaseFunctionDef fd = gf.getFD();
					final BaseFunctionDef fd = pte.getFunctionInvocation().getFunction();
					if (pte.getFunctionInvocation() == null) {
						@NotNull final FunctionInvocation fi = aDeduceTypes2.newFunctionInvocation(fd, pte, invocation, aDeduceTypes2.phase);
						pte.setFunctionInvocation(fi);
					} else
						SimplePrintLoggerToRemoveSoon.println2("175 pte.fi is not null");
					aIntegerIA.gf.addDependentFunction(pte.getFunctionInvocation()); // TODO is this needed (here)?
				} else {
					final int y = 3;
					SimplePrintLoggerToRemoveSoon.println2("3074");
				}
			}
		});
	}

	static void found_parent(final @NotNull DeduceTypes2.PromiseExpectation<GenType> aPromiseExpectation,
	                         final BaseGeneratedFunction generatedFunction,
	                         final VariableTableEntry aBte,
	                         final IdentTableEntry ite,
	                         final DeduceTypes2 aDeduceTypes2) {
		aBte.typePromise().then((final GenType result) -> {
			aPromiseExpectation.satisfy(result);
			final OS_Type attached1 = result.getResolved() != null ? result.getResolved() : result.getTypeName();
			if (attached1 != null) {
				switch (attached1.getType()) {
					case USER_CLASS:
						if (ite.getType().getAttached() == null)
							ite.makeType(generatedFunction, TypeTableEntry.Type.TRANSIENT, attached1);
						else {
							aDeduceTypes2.LOG.err(String.format("3603 Trying to set %s to %s", ite.getType().getAttached(), attached1));
						}
						break;
					case USER:
						try {
							@NotNull final GenType ty3 = aDeduceTypes2.resolve_type(attached1, attached1.getTypeName().getContext());
							// no expression or TableEntryIV below
							@NotNull final TypeTableEntry tte4 = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null);
							// README trying to keep genType up to date
							tte4.setAttached(attached1, aDeduceTypes2.resolver());
							tte4.setAttached(ty3);
							ite.setType(tte4); // or ty2?
						} catch (final ResolveError aResolveError) {
							aResolveError.printStackTrace();
						}
						break;
				}
			}
		});
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
