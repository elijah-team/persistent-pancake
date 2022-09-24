	package tripleo.elijah.stages.deduce.post_bytecode;
       
	import tripleo.elijah.stages.gen_fn.IdentTableEntry;
	import tripleo.elijah.stages.gen_fn.VariableTableEntry;
       
	public class DED_VTE implements DED {
		private final VariableTableEntry variableTableEntry;
       
		public DED_VTE(VariableTableEntry aVariableTableEntry) {
			variableTableEntry = aVariableTableEntry;
		}
       
		public VariableTableEntry getVariableTableEntry() {
			return variableTableEntry;
		}
       
		@Override
		public Kind kind() {
			return Kind.DED_Kind_VariableTableEntry;
		}
       
	}
