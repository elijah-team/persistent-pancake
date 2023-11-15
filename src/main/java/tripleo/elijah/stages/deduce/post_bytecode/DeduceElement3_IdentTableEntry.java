	package tripleo.elijah.stages.deduce.post_bytecode;
       
	import org.jetbrains.annotations.Contract;
	import tripleo.elijah.lang.Context;
	import tripleo.elijah.stages.deduce.DeduceTypes2;
	import tripleo.elijah.stages.deduce.FoundElement;
	import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
	import tripleo.elijah.stages.gen_fn.GenType;
	import tripleo.elijah.stages.gen_fn.GeneratedFunction;
	import tripleo.elijah.stages.gen_fn.IdentTableEntry;
	import tripleo.elijah.stages.instructions.ConstTableIA;
	import tripleo.elijah.stages.instructions.IdentIA;
	import tripleo.elijah.util.NotImplementedException;
       
	public class DeduceElement3_IdentTableEntry implements IDeduceElement3 {
		private final IdentTableEntry principal;
		public BaseGeneratedFunction generatedFunction;
		public DeduceTypes2 deduceTypes2;
       
		@Contract(pure = true)
		public DeduceElement3_IdentTableEntry(final IdentTableEntry aIdentTableEntry) {
			principal = aIdentTableEntry;
		}
       
		@Override
		public void resolve(final IdentIA aIdentIA, final Context aContext, final FoundElement aFoundElement) {
			// FoundElement is the "disease"
			deduceTypes2.resolveIdentIA_(aContext, aIdentIA, generatedFunction, aFoundElement);
		}
       
		@Override
		public void resolve(final Context aContext, final DeduceTypes2 aDeduceTypes2) {
	//		deduceTypes2.resolveIdentIA_(aContext, aIdentIA, generatedFunction, aFoundElement);
			throw new NotImplementedException();
			// careful with this
	//		throw new UnsupportedOperationException("Should not be reached");
		}
       
		@Override
		public DED elementDiscriminator() {
			return new DED() {
				@Override
				public Kind kind() {
					return Kind.DED_Kind_IdentTableEntry;
				}
			};
		}
       
		@Override
		public DeduceTypes2 deduceTypes2() {
			return null;
		}
       
		@Override
		public GeneratedFunction generatedFunction() {
			return null;
		}
       
		@Override
		public GenType genType() {
			return null;
		}
       
		@Override
		public DeduceElement3_Kind kind() {
			return DeduceElement3_Kind.GEN_FN__ITE;
		}
	}
