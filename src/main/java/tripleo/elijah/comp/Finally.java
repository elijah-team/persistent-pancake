package tripleo.elijah.comp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.nextgen.inputtree.EIT_InputType;
import tripleo.elijah.nextgen.outputtree.EOT_OutputFile;
import tripleo.elijah.nextgen.outputtree.EOT_FileNameProvider;

public class Finally {
	public int codeOutputSize() {
		final List<String> r2 = outputs.stream()
		                               .map(Output::getFilename)
		                               .filter(n -> n != null && !n.isEmpty())
		                               .collect(Collectors.toList());
		final List<Output> r = outputs.stream()
		                              .unordered()
		                              .filter(LookupResultList.distinctByKey(Output::getFilename))
		                              .distinct()
		                              .collect(Collectors.toList());

		r2.stream().forEach(arg0 -> System.out.println("1414 " + arg0));

//		System.err.println("1414 "+ r2);

		assert r2.size() == outputs.size(); // duh
//		assert r.size() == outputs.size();

		return r.size();
	}

	public int unboundedCodeOutputSize() {
		return outputs.size();
	}

	public boolean containsCodeInput(final String aS) {
		return this.containsInput(aS);
	}

	public int codeInputSize() {
		return inputs.size();
	}

	public static class Input {
		private final @NotNull Nameable      nameable;
		private final          EIT_InputType ty;

		public Input(final @NotNull Nameable aNameable, final EIT_InputType aTy) {
			System.err.println("66 Add Input >> " + aNameable.getNameableString());
			nameable = aNameable;
			ty = aTy;
		}

//		public Input(final CompilerInput aInp, final Out2 aTy) {
//			nameable = new Finally._CompilerInputNameable(aInp);
//			ty  = aTy;
//		}

//		public Input(final CompFactory.InputRequest aInp, final Out2 aTy) {
//			nameable = new Finally.InputRequestNameable(aInp);
//			ty = aTy;
//		}

		public String name() {
			return nameable.getNameableString();
		}

		@Override
		public String toString() {
			return "Input{" + "name=" + nameable.getNameableString() + ", ty=" + ty + '}';
		}
	}

	public interface Nameable {
		String getNameableString();
	}

	public static class Output {
		private final EOT_FileNameProvider fileNameProvider;
		@SuppressWarnings("FieldCanBeLocal")
		private final EOT_OutputFile       off;

		public Output(final EOT_FileNameProvider aFileNameProvider, final EOT_OutputFile aOff) {
			fileNameProvider = aFileNameProvider;
			off = aOff;
		}

		@Override
		public String toString() {
			final String sb = "Output{" + "off=" + off.getType() +
			  ", fileNameProvider=" + fileNameProvider.getFilename() +
			  '}';
			return sb;
		}

		public String name() {
			return fileNameProvider.getFilename();
		}

		public String getFilename() {
			return fileNameProvider.getFilename();
		}
	}

	public enum Outs {
		Out_6262, Out_727, Out_350, Out_364, Out_252, Out_2121, Out_486, Out_5757, Out_1069, Out_141, Out_EVTE_159,
		Out_401b
	}

	private final Set<Outs> outputOffs = new HashSet<>();

	private final List<Input> inputs = new ArrayList<>();

//	public void addInput(final CompilerInput aInp, final Out2 ty) {
//		inputs.add(new Input(aInp, ty));
//	}

	private final List<Output> outputs = new ArrayList<>();

	private boolean turnAllOutputOff;

//	public void addInput(final CompFactory.InputRequest aInp, final Out2 ty) {
//		inputs.add(new Input(aInp, ty));
//	}

	public Output addCodeOutput(final EOT_FileNameProvider aFileNameProvider, final EOT_OutputFile aOff) {
		final Output e = new Output(aFileNameProvider, aOff);
		outputs.add(e);
		return e;
	}

	public void addInput(final Nameable aNameable, final EIT_InputType ty) {
		inputs.add(new Input(aNameable, ty));
	}

	public boolean containsCodeOutput(@NotNull final String s) {
		return outputs.stream().anyMatch(i -> i.name().equals(s));
	}

	public boolean containsInput(final String aS) {
		return inputs.stream().anyMatch(i -> i.name().equals(aS));
	}

	public boolean outputOn(final Outs aOuts) {
		return !turnAllOutputOff && !outputOffs.contains(aOuts);
	}

	public void turnAllOutputOff() {
		turnAllOutputOff = true;
	}

	public void turnOutputOff(final Outs aOut) {
		outputOffs.add(aOut);
	}

//	private class _CompilerInputNameable implements Nameable {
//		private final CompilerInput aInp;
//
//		public _CompilerInputNameable(CompilerInput aInp) {
//			this.aInp = aInp;
//		}
//
//		@Override
//		public String getName() {
//			return aInp.getInp();
//		}
//	}

//	private class InputRequestNameable implements Nameable {
//		private final CompFactory.InputRequest aInp;
//
//		public InputRequestNameable(CompFactory.InputRequest aInp) {
//			this.aInp = aInp;
//		}
//
//		@Override
//		public String getName() {
//			return aInp.file().toString();
//		}
//	}
}
