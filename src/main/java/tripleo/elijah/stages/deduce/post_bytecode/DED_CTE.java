       
       

	package tripleo.elijah.stages.deduce.post_bytecode;
       
	import org.jetbrains.annotations.Contract;
	import tripleo.elijah.stages.gen_fn.ConstantTableEntry;
       
	public class DED_CTE implements DED {
		private final ConstantTableEntry constantTableEntry;
       
		@Contract(pure = true)
		public DED_CTE(ConstantTableEntry aConstantTableEntry) {
			constantTableEntry = aConstantTableEntry;
		}
       
		public ConstantTableEntry getConstantTableEntry() {
			return constantTableEntry;
		}
       
		@Override
		public DED.Kind kind() {
			return DED.Kind.DED_Kind_ConstantTableEntry;
		}
	}