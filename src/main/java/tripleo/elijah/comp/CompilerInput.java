package tripleo.elijah.comp;

import com.google.common.base.*;
import tripleo.elijah.ci.*;
import tripleo.elijah.comp.i.*;
import tripleo.elijah.util.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class CompilerInput extends __Extensionable {
    public File makeFile() {
	    return switch (ty) {
		    case SOURCE_ROOT -> dir_carrier;
		    case ROOT -> new File(new File(inp).getParent());
		    default -> null;
	    };
    }

    public enum CompilerInputField {
        TY, HASH, ACCEPT_CI, DIRECTORY_RESULTS
    }

    public enum Ty {
        NULL, /* as is from command line/ */
        SOURCE_ROOT,
        ROOT, /* the base of the compilation */
        ARG, /* represents a compiler change (CC) */
        STDLIB
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

    public CompilerInput(final String aS) {
        inp = aS;
        ty  = Ty.NULL;
    }

    public void accept_ci(final Maybe<ILazyCompilerInstructions> compilerInstructionsMaybe) {
        accept_ci = compilerInstructionsMaybe;

        if (master != null)
            master.notifyChange(this, CompilerInputField.ACCEPT_CI);
    }

    public void accept_hash(final String hash) {
        this.hash = hash;

        if (master != null)
            master.notifyChange(this, CompilerInputField.HASH);
    }

    public Maybe<ILazyCompilerInstructions> acceptance_ci() {
        return accept_ci;
    }

    public void certifyRoot() {
        ty = Ty.ROOT;

        if (master != null)
            master.notifyChange(this, CompilerInputField.TY);
    }

    public File getDirectory() {
        Preconditions.checkNotNull(dir_carrier);

        return dir_carrier;
    }

    public boolean isElijjahFile() {
        return Pattern.matches(".+\\.elijjah$", inp) || Pattern.matches(".+\\.elijah$", inp);
    }

    public boolean isEzFile() {
        // new QuerySearchEzFiles.EzFilesFilter().accept()
        return Pattern.matches(".+\\.ez$", inp);
    }

    public boolean isNull() {
        return ty == Ty.NULL;
    }

    public boolean isSourceRoot() {
        return ty == Ty.SOURCE_ROOT;
    }

    public void setArg() {
        ty = Ty.ARG;

        if (master != null)
            master.notifyChange(this, CompilerInputField.TY);
    }

    public void setDirectory(File f) {
        ty          = Ty.SOURCE_ROOT;
        dir_carrier = f;

        if (master != null)
            master.notifyChange(this, CompilerInputField.TY);
    }

    public void setDirectoryResults(final List<Operation2<CompilerInstructions>> aLoci) {
        this.directoryResults = aLoci;

        for (Operation2<CompilerInstructions> locus : aLoci) {
	        CompilerInstructions focus = locus.success();
            focus.advise(this);
        }

        if (master != null)
            master.notifyChange(this, CompilerInputField.DIRECTORY_RESULTS);
    }

    public void setMaster(CompilerInputMaster master) {
        this.master = master;
    }

    public void setSourceRoot() {
        ty = Ty.SOURCE_ROOT;

        if (master != null)
            master.notifyChange(this, CompilerInputField.TY);
    }

    @Override
    public String toString() {
        return "CompilerInput{ty=%s, inp='%s'}".formatted(ty, inp);
    }

    public Ty ty() {
        return ty;
    }
}
