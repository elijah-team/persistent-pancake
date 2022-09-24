package tripleo.elijah.stages.deduce.post_bytecode;

import tripleo.elijah.stages.gen_fn.ConstantTableEntry;
import tripleo.elijah.stages.gen_fn.IdentTableEntry;

public class DED_ITE implements DED {
    private final IdentTableEntry identTableEntry;

    public DED_ITE(IdentTableEntry aIdentTableEntry) {
        identTableEntry = aIdentTableEntry;
    }

    public IdentTableEntry getIdentTableEntry() {
        return identTableEntry;
    }

    @Override
    public Kind kind() {
        return Kind.DED_Kind_IdentTableEntry;
    }

}
