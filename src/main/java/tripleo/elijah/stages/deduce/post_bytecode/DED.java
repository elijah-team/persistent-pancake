	package tripleo.elijah.stages.deduce.post_bytecode;
       
	public interface DED {
		Kind kind();
       
		enum Kind {
			DED_Kind_GeneratedFunction,
			DED_Kind_ProcTableEntry,
			DED_Kind_IdentTableEntry,
			DED_Kind_VariableTableEntry,
			DED_Kind_ConstantTableEntry, /*
			DED_Kind_GeneratedFunction,
			DED_Kind_GeneratedFunction,
			DED_Kind_GeneratedFunction,
			DED_Kind_GeneratedFunction,
	*/
			DED_Kind_Type
		}
	}
