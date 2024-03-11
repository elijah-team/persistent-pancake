package tripleo.eljiah_pancake_durable.stages.deduce.zero;

import org.jdeferred2.Promise;
import org.jdeferred2.impl.DeferredObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.eljiah_pancake_durable.comp.ErrSink;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.eljiah_pancake_durable.lang.BaseFunctionDef;
import tripleo.eljiah_pancake_durable.lang.LookupResultList;
import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.stages.deduce.DeduceLookupUtils;
import tripleo.eljiah_pancake_durable.stages.deduce.DeduceTypes2;
import tripleo.eljiah_pancake_durable.stages.deduce.ResolveError;
import tripleo.eljiah_pancake_durable.stages.gen_fn.BaseTableEntry;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedConstructor;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenericElementHolder;
import tripleo.eljiah_pancake_durable.stages.gen_fn.IElementHolder;
import tripleo.eljiah_pancake_durable.stages.gen_fn.IdentTableEntry;
import tripleo.eljiah_pancake_durable.stages.gen_fn.ProcTableEntry;

public class PTE_Zero {
    private final ProcTableEntry                                   procTableEntry;
    private final DeferredObject<IElementHolder, Diagnostic, Void> _foundCounstructorDef2Promise = new DeferredObject<>();

    public PTE_Zero(final ProcTableEntry aProcTableEntry) {
        procTableEntry = aProcTableEntry;
    }

    public void foundCounstructorDef(final @NotNull GeneratedConstructor constructorDef,
                                     final @NotNull IdentTableEntry ite,
                                     final @NotNull DeduceTypes2 deduceTypes2,
                                     final @NotNull ErrSink errSink) {
        @NotNull final BaseFunctionDef ele = constructorDef.getFD();

        try {
            final LookupResultList     lrl  = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele.getContext(), deduceTypes2);
            @Nullable final OS_Element best = lrl.chooseBest(null);
            ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
        } catch (final ResolveError aResolveError) {
//            aResolveError.printStackTrace();
            errSink.reportDiagnostic(aResolveError);
        }
    }

    public Promise<IElementHolder, Diagnostic, Void> foundCounstructorPromise() {
        return _foundCounstructorDef2Promise.promise();
    }

    public void calculateConstructor(@NotNull final GeneratedConstructor constructorDef, @NotNull final IdentTableEntry ite, @NotNull final DeduceTypes2 deduceTypes2) {
        if (_foundCounstructorDef2Promise.isResolved()) return;

        @NotNull final BaseFunctionDef ele = constructorDef.getFD();

        try {
            final LookupResultList     lrl  = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele.getContext(), deduceTypes2);
            @Nullable final OS_Element best = lrl.chooseBest(null);
//            ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
            final GenericElementHolder elementHolder = new GenericElementHolder(best);
            _foundCounstructorDef2Promise.resolve(elementHolder);
        } catch (final ResolveError aResolveError) {
            _foundCounstructorDef2Promise.reject(aResolveError);
        }
    }
}
