package tripleo.elijah.comp.i;

import org.jetbrains.annotations.Nullable;
import tripleo.elijah.ci.LibraryStatementPart;
//import tripleo.elijah.lang.i.OS_Module;
//import tripleo.elijah.lang.i.Qualident;
import tripleo.elijah.comp.InputRequest;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.nextgen.inputtree.EIT_ModuleInput;
import tripleo.elijah.world.i.WorldModule;

import java.io.File;
import java.util.List;

public interface CompFactory {
	EIT_ModuleInput createModuleInput(OS_Module aModule);

	Qualident createQualident(List<String> sl);

	InputRequest createInputRequest(File aFile, final boolean aDo_out, final @Nullable LibraryStatementPart aLsp);

	WorldModule createWorldModule(OS_Module aM);
}
