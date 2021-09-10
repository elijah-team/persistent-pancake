/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.contexts.FunctionContext;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.work.WorkList;
import tripleo.elijah.work.WorkManager;

import java.util.ArrayList;

/**
 * Created 9/5/21 2:54 AM
 */
class Resolve_Variable_Table_Entry {
	private final BaseGeneratedFunction generatedFunction;
	private final Context ctx;

	private final DeduceTypes2 deduceTypes2;
	private final ElLog LOG;
	private final WorkManager wm;
	private final DeducePhase phase;
	private final ErrSink errSink;

	public Resolve_Variable_Table_Entry(BaseGeneratedFunction aGeneratedFunction, Context aCtx, DeduceTypes2 aDeduceTypes2) {
		generatedFunction = aGeneratedFunction;
		ctx = aCtx;
		deduceTypes2 = aDeduceTypes2;
		//
		LOG = deduceTypes2.LOG;
		wm = deduceTypes2.wm;
		errSink = deduceTypes2.errSink;
		phase = deduceTypes2.phase;
	}

	public void action(final VariableTableEntry vte) {
		switch (vte.vtt) {
		case ARG:
			action_ARG(vte);
			break;
		case VAR:
			action_VAR(vte);
			break;
		}
	}

	public void action_VAR(VariableTableEntry vte) {
		if (vte.type.getAttached() == null && vte.potentialTypes.size() == 1) {
			TypeTableEntry pot = new ArrayList<>(vte.potentialTypes()).get(0);
			if (pot.getAttached() instanceof OS_FuncExprType) {
				action_VAR_potsize_1_and_FuncExprType(vte, (OS_FuncExprType) pot.getAttached(), pot.genType, pot.expression);
			} else if (pot.getAttached() != null && pot.getAttached().getType() == OS_Type.Type.USER_CLASS) {
				int y = 1;
				vte.type = pot;
				vte.typeDeferred().resolve(pot.genType);
			} else {
				action_VAR_potsize_1_other(vte, pot);
			}
		}
	}

	public void action_VAR_potsize_1_other(VariableTableEntry vte, TypeTableEntry aPot) {
		try {
			if (aPot.tableEntry instanceof ProcTableEntry) {
				final ProcTableEntry pte1 = (ProcTableEntry) aPot.tableEntry;
				OS_Element e = DeduceLookupUtils.lookup(pte1.expression, ctx, deduceTypes2);
				assert e != null;
				if (e instanceof FunctionDef) {
//						final FunctionDef fd = (FunctionDef) e;
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
					int y = 2;
				} else {
					vte.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(e));
					pte1.setStatus(BaseTableEntry.Status.KNOWN, new ConstructableElementHolder(e, vte));
//					vte.setCallablePTE(pte1);

					GenType gt = aPot.genType;
					setup_GenType(e, gt);
					if (gt.node == null)
						gt.node = vte.genType.node;

					vte.genType.copy(gt);
				}
				int y = 2;
			} else if (aPot.tableEntry == null) {
				int y=2;
			} else
				throw new NotImplementedException();
		} catch (ResolveError aResolveError) {
			errSink.reportDiagnostic(aResolveError);
		}
	}

	public void setup_GenType(OS_Element element, GenType aGt) {
		if (element instanceof NamespaceStatement) {
			final NamespaceStatement namespaceStatement = (NamespaceStatement) element;
			aGt.resolvedn = (NamespaceStatement) element;
			final NamespaceInvocation nsi = phase.registerNamespaceInvocation(namespaceStatement);
//			pte.setNamespaceInvocation(nsi);
			aGt.ci = nsi;
//			fi = newFunctionInvocation(fd, pte, nsi, phase);
		} else if (element instanceof ClassStatement) {
			final ClassStatement classStatement = (ClassStatement) element;
			aGt.resolved = new OS_Type((ClassStatement) element);
			// TODO genCI ??
			ClassInvocation ci = new ClassInvocation(classStatement, null);
			ci = phase.registerClassInvocation(ci);
			aGt.ci = ci;
//			pte.setClassInvocation(ci);
//			fi = newFunctionInvocation(fd, pte, ci, phase);
		} else if (element instanceof FunctionDef) {
			// TODO this seems to be an elaborate copy of the above with no differentiation for functionDef
			final FunctionDef functionDef = (FunctionDef) element;
			OS_Element parent = functionDef.getParent();
			IInvocation inv;
			switch (DecideElObjectType.getElObjectType(parent)) {
			case CLASS:
				aGt.resolved = new OS_Type((ClassStatement) parent);
				inv = phase.registerClassInvocation((ClassStatement) parent, null);
				((ClassInvocation)inv).resolveDeferred().then(new DoneCallback<GeneratedClass>() {
					@Override
					public void onDone(GeneratedClass result) {
						aGt.node = result;
					}
				});
				break;
			case NAMESPACE:
				aGt.resolvedn = (NamespaceStatement) parent;
				inv = phase.registerNamespaceInvocation((NamespaceStatement) parent);
				((NamespaceInvocation)inv).resolveDeferred().then(new DoneCallback<GeneratedNamespace>() {
					@Override
					public void onDone(GeneratedNamespace result) {
						aGt.node = result;
					}
				});
				break;
			default:
				throw new NotImplementedException();
			}
			aGt.ci = inv;
		} else if (element instanceof AliasStatement) {
			OS_Element el = element;
			while (el instanceof AliasStatement) {
				el = DeduceLookupUtils._resolveAlias((AliasStatement) el, deduceTypes2);
			}
			setup_GenType(el, aGt);
		} else // TODO will fail on FunctionDef's
			throw new IllegalStateException("Unknown parent");
	}

	public void action_VAR_potsize_1_and_FuncExprType(VariableTableEntry vte,
													  OS_FuncExprType funcExprType,
													  GenType aGenType,
													  IExpression aPotentialExpression) {
		aGenType.typeName = funcExprType;

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
//			System.out.println("1630 "+mod_ns.getItems()); // element 0 is ctor$0
			fd1.setName(IdentExpression.forString(String.format("$%d", mod_ns.getItems().size() + 1)));

			WorkList wl = new WorkList();
			@NotNull GenerateFunctions gen = phase.generatePhase.getGenerateFunctions(mod1);
			NamespaceInvocation modi = new NamespaceInvocation(mod_ns);
			final ProcTableEntry pte = findProcTableEntry(generatedFunction, aPotentialExpression);
			assert pte != null;
			callable_pte = pte;
			FunctionInvocation fi = new FunctionInvocation(fd1, pte, modi, phase.generatePhase);
			wl.addJob(new WlGenerateNamespace(gen, modi, phase.generatedClasses)); // TODO hope this works (for more than one)
			final WlGenerateFunction wlgf = new WlGenerateFunction(gen, fi);
			wl.addJob(wlgf);
			wm.addJobs(wl);
			wm.drain(); // TODO here?

			aGenType.ci = modi;
			aGenType.node = wlgf.getResult();

			DeduceTypes2.PromiseExpectation<GenType> pe = deduceTypes2.promiseExpectation(/*pot.genType.node*/new DeduceTypes2.ExpectationBase() {
				@Override
				public String expectationString() {
					return "FuncType..."; // TODO
				}
			}, "FuncType Result");
			((GeneratedFunction) aGenType.node).typePromise().then(new DoneCallback<GenType>() {
				@Override
				public void onDone(GenType result) {
					pe.satisfy(result);
					vte.typeDeferred().resolve(result);
				}
			});

			//vte.typeDeferred().resolve(pot.genType); // this is wrong
		}

		if (callable_pte != null)
			vte.setCallablePTE(callable_pte);
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
			OS_Element e = DeduceLookupUtils.lookup(module_ident, aModule.getContext(), deduceTypes2);
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

	public void action_ARG(VariableTableEntry vte) {
		TypeTableEntry tte = vte.type;
		final OS_Type attached = tte.getAttached();
		if (attached != null) {
			switch (attached.getType()) {
				case USER:
					if (tte.genType.typeName == null)
						tte.genType.typeName = attached;
					try {
						tte.genType.resolved = deduceTypes2.resolve_type(attached, ctx);
						tte.setAttached(tte.genType.resolved); // TODO probably not necessary, but let's leave it for now
					} catch (ResolveError aResolveError) {
						errSink.reportDiagnostic(aResolveError);
						LOG.err("Can't resolve argument type " + attached);
						return;
					}
					vte.typeDeferred().resolve(tte.genType);
					break;
				case USER_CLASS:
					if (tte.genType.resolved == null)
						tte.genType.resolved = attached;
					// TODO genCI and all that -- Incremental?? (.increment())
					vte.typeDeferred().resolve(tte.genType);
					break;
			}
		} else {
			int y = 2;
		}
	}
}

//
//
//