package tripleo.eljiah_pancake_durable.stages.gen_c;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.eljiah_pancake_durable.lang.IExpression;
import tripleo.eljiah_pancake_durable.lang.IdentExpression;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_CompoundStatement;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_SingleStatement;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_Statement;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EX_Explanation;
import tripleo.eljiah_pancake_durable.stages.deduce.post_bytecode.DeduceElement3_ProcTableEntry;
import tripleo.eljiah_pancake_durable.stages.deduce.post_bytecode.IDeduceElement3;
import tripleo.eljiah_pancake_durable.stages.gen_fn.BaseGeneratedFunction;
import tripleo.eljiah_pancake_durable.stages.gen_fn.ProcTableEntry;
import tripleo.eljiah_pancake_durable.stages.instructions.IdentIA;
import tripleo.eljiah_pancake_durable.stages.instructions.Instruction;
import tripleo.eljiah_pancake_durable.stages.instructions.InstructionArgument;
import tripleo.eljiah_pancake_durable.stages.instructions.InstructionFixedList;
import tripleo.eljiah_pancake_durable.stages.instructions.IntegerIA;
import tripleo.eljiah_pancake_durable.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

import java.util.List;

import static tripleo.eljiah_pancake_durable.stages.gen_c.Generate_Code_For_Method.AOG.GET;

public class _GF {
	@Contract("null, _ -> fail")
	public static @NotNull EG_Statement forDeduceElement3(final IDeduceElement3 deduceElement3, final GenerateC gc) {
		//return deduceElement3.();
		if (deduceElement3 instanceof DeduceElement3_ProcTableEntry) {
			final DeduceElement3_ProcTableEntry de_pte = (DeduceElement3_ProcTableEntry) deduceElement3;
			return forDeduceElement3_ProcTableEntry(de_pte, gc);
		}

		throw new NotImplementedException();
	}

	private static @NotNull EG_Statement forDeduceElement3_ProcTableEntry(@NotNull final DeduceElement3_ProcTableEntry de_pte, final GenerateC gc) {
		final EG_SingleStatement beginning;
		final EG_SingleStatement ending;
		final EG_Statement       middle;
		final boolean            indent = false;
		final EX_Explanation     explanation;


		final ProcTableEntry pte = de_pte.getTablePrincipal();

		final BaseGeneratedFunction gf          = de_pte.getGeneratedFunction();
		final Instruction           instruction = de_pte.getInstruction();

		final StringBuilder sb = XXX_YYY.dispatch(pte, new XXX_YYY() {
			@Override
			public StringBuilder itsABoy(final IExpression expression) {
				final IdentExpression ptex = (IdentExpression) expression;
				final String          text = ptex.getText();

				@Nullable final InstructionArgument xx = gf.vte_lookup(text);
				assert xx != null;

				final String       realTargetName = gc.getRealTargetName((IntegerIA) xx, GET);
				final List<String> sl3            = gc.getArgumentStrings(() -> new InstructionFixedList(instruction));

				final StringBuilder sb = new StringBuilder();
				sb.append(Emit.emit("/*424*/"));
				sb.append(realTargetName);
				sb.append('(');
				sb.append(Helpers.String_join(", ", sl3));
				sb.append(");");

				return sb;
			}

			@Override
			public StringBuilder itsAGirl(final InstructionArgument expression_num) {
				final IdentIA identIA = (IdentIA) expression_num;

				final CReference reference = new CReference();
				reference.getIdentIAPath(identIA, GET, null);
				final List<String> sl3 = gc.getArgumentStrings(() -> new InstructionFixedList(instruction));
				reference.args(sl3);
				final @NotNull String path = reference.build();

				final StringBuilder sb = new StringBuilder();
				sb.append(Emit.emit("/*427*/"));
				sb.append(path);
				sb.append(";");

				return sb;
			}
		});

		beginning   = new EG_SingleStatement("", new EX_Explanation() {
		});
		ending      = new EG_SingleStatement("", new EX_Explanation() {
		});
		explanation = new EX_ProcTableEntryExplanation(de_pte);
		middle      = new EG_SingleStatement(sb.toString(), explanation);

		final EG_CompoundStatement stmt = new EG_CompoundStatement(beginning, ending, middle, indent, explanation);
		//new EX_TableEntryExplanation();
		return stmt;
	}

	interface XXX_YYY {
		static StringBuilder dispatch(@NotNull final ProcTableEntry pte, final XXX_YYY xy) {
			if (pte.expression_num == null) {
				return xy.itsABoy(pte.expression);
			} else {
				return xy.itsAGirl(pte.expression_num);
			}
		}

		StringBuilder itsABoy(IExpression expression);

		StringBuilder itsAGirl(InstructionArgument expreesion_num);
	}
}
