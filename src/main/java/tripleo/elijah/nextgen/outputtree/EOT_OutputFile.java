package tripleo.elijah.nextgen.outputtree;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.lang.OS_Module;

public class EOT_OutputFile {
    private final OS_Module module;
    private final Compilation c;

    public EOT_OutputFile(final OS_Module module, final Compilation c) {
        this.module = module;
        this.c = c;
    }
}
