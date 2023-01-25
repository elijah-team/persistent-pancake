package tripleo.elijah.stages.deduce.post_bytecode;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.elijah.diagnostic.Locatable;
import tripleo.elijah.lang.AliasStatement;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.NormalTypeName;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.TypeName;
import tripleo.elijah.nextgen.query.Operation2;
import tripleo.elijah.stages.deduce.DeduceLookupUtils;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.deduce.FoundElement;
import tripleo.elijah.stages.deduce.ResolveError;
import tripleo.elijah.stages.deduce.post_bytecode.DED.DED_VTE;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.BaseTableEntry;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah.stages.gen_fn.VariableTableEntry;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.VariableTableType;
import tripleo.elijah.stages.logging.ElLog;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

public class DeduceElement3_VariableTableEntry extends Stateful implements IDeduceElement3 {

	private final VariableTableEntry principal;

	private final State                 st;
	private       DeduceTypes2          deduceTypes2;
	private       BaseGeneratedFunction generatedFunction;
	private       GenType               genType;

	@Contract(pure = true)
	public DeduceElement3_VariableTableEntry(final VariableTableEntry aVariableTableEntry) {
		principal = aVariableTableEntry;
		st        = ST.INITIAL;
	}

	DeduceElement3_VariableTableEntry(final OS_Type vte_type_attached) {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}

	@Override
	public void resolve(final IdentIA aIdentIA, final Context aContext, final FoundElement aFoundElement) {
		throw new UnsupportedOperationException("Should not be reached");
	}

	@Override
	public void resolve(final Context aContext, final DeduceTypes2 aDeduceTypes2) {
		throw new UnsupportedOperationException("Should not be reached");
	}

	@Override
	public OS_Element getPrincipal() {
		return principal.getDeduceElement3().getPrincipal();
	}

	@Override
	public DED elementDiscriminator() {
		return new DED_VTE(principal);
	}

	@Override
	public DeduceTypes2 deduceTypes2() {
		return deduceTypes2;
	}

	@Override
	public BaseGeneratedFunction generatedFunction() {
		return generatedFunction;
	}

	@Override
	public GenType genType() {
		return genType;
	}

	@Override
	public DeduceElement3_Kind kind() {
		return DeduceElement3_Kind.GEN_FN__VTE;
	}

	public Operation2<OS_Type> decl_test_001(final BaseGeneratedFunction gf) {
		final VariableTableEntry vte = principal;

		final OS_Type x = vte.type.getAttached();
		if (x == null && vte.potentialTypes().size() == 0) {
			final Diagnostic diag;
			if (vte.vtt == VariableTableType.TEMP) {
				diag = new Diagnostic_8884(vte, gf);
			} else {
				diag = new Diagnostic_8885(vte);
			}
			return Operation2.failure(diag);
		}

		return Operation2.success(x);
	}

	public void setDeduceTypes2(final DeduceTypes2 aDeduceTypes2, final BaseGeneratedFunction aGeneratedFunction) {
		deduceTypes2      = aDeduceTypes2;
		generatedFunction = aGeneratedFunction;
	}

	public static class ST {
		public static State EXIT_RESOLVE;
		public static State EXIT_CONVERT_USER_TYPES;
		static        State INITIAL;

		public static void register(final @NotNull DeducePhase aDeducePhase) {
			EXIT_RESOLVE            = aDeducePhase.register(new ExitResolveState());
			INITIAL                 = aDeducePhase.register(new InitialState());
			EXIT_CONVERT_USER_TYPES = aDeducePhase.register(new ExitConvertUserTypes());
		}

		static class ExitConvertUserTypes implements State {
			private int identity;

			@Override
			public void apply(final Stateful element) {
				final VariableTableEntry vte = ((DeduceElement3_VariableTableEntry) element).principal;

				final DeduceTypes2         dt2     = ((DeduceElement3_VariableTableEntry) element).deduceTypes2();
				final ErrSink              errSink = dt2._errSink();
				final @NotNull DeducePhase phase   = dt2._phase();
				final @NotNull ElLog       LOG     = dt2._LOG();

				final @Nullable OS_Type attached = vte.type.getAttached();

				if (attached == null) return;
				if (Objects.requireNonNull(attached.getType()) == OS_Type.Type.USER) {
					final TypeName x = attached.getTypeName();
					if (x instanceof NormalTypeName) {
						final String tn = ((NormalTypeName) x).getName();
						apply_normal(vte, dt2, errSink, phase, LOG, attached, x, tn);
					}
				}
			}

			private static void apply_normal(final VariableTableEntry vte,
			                                 final DeduceTypes2 dt2,
			                                 final ErrSink errSink,
			                                 final DeducePhase phase,
			                                 final @NotNull ElLog LOG,
			                                 final @NotNull OS_Type attached,
			                                 final TypeName x,
			                                 final String tn) {
				final LookupResultList lrl  = x.getContext().lookup(tn);
				@Nullable OS_Element   best = lrl.chooseBest(null);

				while (best instanceof AliasStatement) {
					best = DeduceLookupUtils._resolveAlias((AliasStatement) best, dt2);
				}

				if (best != null) {
					if (!(OS_Type.isConcreteType(best))) {
						errSink.reportError(String.format("Not a concrete type %s for (%s)", best, tn));
					} else {
						LOG.info("705 " + best);
						// NOTE that when we set USER_CLASS from USER generic information is
						// still contained in constructable_pte
						@NotNull final GenType genType = new GenType(attached, ((ClassStatement) best).getOS_Type(), true, x, dt2, errSink, phase);
						vte.setLikelyType(genType);
					}
					//vte.el = best;
					// NOTE we called resolve_var_table_entry above
					LOG.info("200 " + best);
					assert vte.getResolvedElement() == null || vte.getStatus() == BaseTableEntry.Status.KNOWN;
					//									vte.setStatus(BaseTableEntry.Status.KNOWN, best/*vte.el*/);
				} else {
					errSink.reportDiagnostic(new ResolveError(x, lrl));
				}
			}

			@Override
			public void setIdentity(final int aId) {
				identity = aId;
			}

			@Override
			public boolean checkState(final Stateful aElement3) {
				return true;
			}
		}

		static class InitialState implements State {
			private int identity;

			@Override
			public void apply(final Stateful element) {

			}

			@Override
			public void setIdentity(final int aId) {
				identity = aId;
			}

			@Override
			public boolean checkState(final Stateful aElement3) {
				return true;
			}
		}

		static class ExitResolveState implements State {
			private int identity;

			@Override
			public void apply(final Stateful element) {
				final VariableTableEntry vte = ((DeduceElement3_VariableTableEntry) element).principal;
				vte.resolve_var_table_entry_for_exit_function();
			}

			@Override
			public void setIdentity(final int aId) {
				identity = aId;
			}

			@Override
			public boolean checkState(final Stateful aElement3) {
				return ((DeduceElement3_VariableTableEntry) aElement3).st == ST.INITIAL;
			}
		}
	}

	private static class Diagnostic_8884 implements GCFM_Diagnostic {
		private final VariableTableEntry    vte;
		private final BaseGeneratedFunction gf;
		private final int                   _code = 8884;

		public Diagnostic_8884(final VariableTableEntry aVte, final BaseGeneratedFunction aGf) {
			vte = aVte;
			gf  = aGf;
		}

		@Override
		public String code() {
			return "" + _code;
		}

		@Override
		public Severity severity() {
			return Severity.ERROR;
		}

		@Override
		public @NotNull Locatable primary() {
			return null;
		}

		@Override
		public @NotNull List<Locatable> secondary() {
			return null;
		}

		@Override
		public void report(final PrintStream stream) {
			stream.printf(_message());
		}

		@Override
		public String _message() {
			return String.format("%d temp variable has no type %s %s", _code, vte, gf);
		}
	}

	private static class Diagnostic_8885 implements GCFM_Diagnostic {
		private final VariableTableEntry vte;
		private final int                _code = 8885;

		public Diagnostic_8885(final VariableTableEntry aVte) {
			vte = aVte;
		}

		@Override
		public String code() {
			return "" + _code;
		}

		@Override
		public Severity severity() {
			return Severity.ERROR;
		}

		@Override
		public @NotNull Locatable primary() {
			return null;
		}

		@Override
		public @NotNull List<Locatable> secondary() {
			return null;
		}

		@Override
		public void report(final @NotNull PrintStream stream) {
			stream.printf(_message());
		}

		@Override
		public String _message() {
			return String.format("%d x is null (No typename specified) for %s%n", _code, vte.getName());
		}
	}
}
