package tripleo.elijah_durable_pancake.input;

import com.google.common.base.Preconditions;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.util.__Extensionable;
import tripleo.elijah.util.Maybe;
import tripleo.elijah.util.Operation2;
import tripleo.elijah_durable_pancake.comp.CompilerInput;
import tripleo.elijah_durable_pancake.comp.CompilerInputMaster;
import tripleo.elijah_durable_pancake.comp.ILazyCompilerInstructions;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class CompilerInputImpl extends __Extensionable implements CompilerInput {
    @Override public File makeFile() {
	    return switch (ty) {
		    case SOURCE_ROOT -> dir_carrier;
		    case ROOT -> new File(new File(inp).getParent());
		    default -> null;
	    };
    }

    private final String                           inp;
    private       Maybe<ILazyCompilerInstructions> accept_ci;
    private       File                             dir_carrier;
    // @Getter(fluent)
    private       Ty                               ty;
    private       String                           hash;

    private CompilerInputMaster master;

    @SuppressWarnings("unchecked") // README squiggly line in idea
    private List<Operation2<CompilerInstructions>> directoryResults = Collections.EMPTY_LIST;

    public CompilerInputImpl(final String aS) {
        inp = aS;
        ty  = Ty.NULL;
    }

    @Override public void accept_ci(final Maybe<ILazyCompilerInstructions> compilerInstructionsMaybe) {
        accept_ci = compilerInstructionsMaybe;

        if (master != null)
            master.notifyChange(this, CompilerInputField.ACCEPT_CI);
    }

    @Override public void accept_hash(final String hash) {
        this.hash = hash;

        if (master != null)
            master.notifyChange(this, CompilerInputField.HASH);
    }

    @Override public Maybe<ILazyCompilerInstructions> acceptance_ci() {
        return accept_ci;
    }

    @Override public void certifyRoot() {
        ty = Ty.ROOT;

        if (master != null)
            master.notifyChange(this, CompilerInputField.TY);
    }

    @Override public File getDirectory() {
        Preconditions.checkNotNull(dir_carrier);

        return dir_carrier;
    }

    @Override public boolean isElijjahFile() {
        return Pattern.matches(".+\\.elijjah$", inp) || Pattern.matches(".+\\.elijah$", inp);
    }

    @Override public boolean isEzFile() {
        // new QuerySearchEzFiles.EzFilesFilter().accept()
        return Pattern.matches(".+\\.ez$", inp);
    }

    @Override public boolean isNull() {
        return ty == Ty.NULL;
    }

    @Override public boolean isSourceRoot() {
        return ty == Ty.SOURCE_ROOT;
    }

    @Override public void setArg() {
        ty = Ty.ARG;

        if (master != null)
            master.notifyChange(this, CompilerInputField.TY);
    }

    @Override public void setDirectory(File f) {
        ty          = Ty.SOURCE_ROOT;
        dir_carrier = f;

        if (master != null)
            master.notifyChange(this, CompilerInputField.TY);
    }

    @Override public void setDirectoryResults(final List<Operation2<CompilerInstructions>> aLoci) {
        this.directoryResults = aLoci;

        for (Operation2<CompilerInstructions> locus : aLoci) {
	        CompilerInstructions focus = locus.success();
            focus.advise(this);
        }

        if (master != null)
            master.notifyChange(this, CompilerInputField.DIRECTORY_RESULTS);
    }

    @Override public void setMaster(CompilerInputMaster master) {
        this.master = master;
    }

    @Override public void setSourceRoot() {
        ty = Ty.SOURCE_ROOT;

        if (master != null)
            master.notifyChange(this, CompilerInputField.TY);
    }

    @Override
    public String toString() {
        return "CompilerInput{ty=%s, inp='%s'}".formatted(ty, inp);
    }

    @Override public Ty ty() {
        return ty;
    }
}
