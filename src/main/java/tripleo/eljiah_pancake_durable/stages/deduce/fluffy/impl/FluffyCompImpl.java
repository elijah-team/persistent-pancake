package tripleo.eljiah_pancake_durable.stages.deduce.fluffy.impl;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;
import tripleo.eljiah_pancake_durable.entrypoints.MainClassEntryPoint;
import tripleo.eljiah_pancake_durable.lang.ClassItem;
import tripleo.eljiah_pancake_durable.lang.FunctionDef;
import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.eljiah_pancake_durable.stages.deduce.fluffy.i.FluffyComp;
import tripleo.eljiah_pancake_durable.stages.deduce.fluffy.i.FluffyModule;

import java.util.HashMap;
import java.util.Map;

public class FluffyCompImpl implements FluffyComp {

	private final EDP_Compilation              _comp;
	private final Map<OS_Module, FluffyModule> fluffyModuleMap = new HashMap<>();

	public FluffyCompImpl(final EDP_Compilation aComp) {
		_comp = aComp;
	}

	public static boolean isMainClassEntryPoint(@NotNull final ClassItem input) {
		final FunctionDef fd = (FunctionDef) input;
		return MainClassEntryPoint.is_main_function_with_no_args(fd);
	}

	@Override
	public FluffyModule module(final OS_Module aModule) {
		if (fluffyModuleMap.containsKey(aModule)) {
			return fluffyModuleMap.get(aModule);
		}

		final FluffyModuleImpl fluffyModule = new FluffyModuleImpl(aModule, _comp);

		fluffyModuleMap.put(aModule, fluffyModule);
//		fluffyModule.

		return fluffyModule;
	}
}