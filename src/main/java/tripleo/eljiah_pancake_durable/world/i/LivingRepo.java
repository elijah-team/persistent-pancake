package tripleo.eljiah_pancake_durable.world.i;

import tripleo.eljiah_pancake_durable.lang.BaseFunctionDef;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.OS_Package;
import tripleo.eljiah_pancake_durable.stages.gen_fn.BaseGeneratedFunction;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedClass;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedNamespace;
import tripleo.eljiah_pancake_durable.world.impl.DefaultLivingClass;
import tripleo.eljiah_pancake_durable.world.impl.DefaultLivingFunction;

public interface LivingRepo {
	LivingClass addClass(ClassStatement cs);

	LivingFunction addFunction(BaseFunctionDef fd);

	LivingPackage addPackage(OS_Package pk);

	OS_Package getPackage(String aPackageName);

	DefaultLivingFunction addFunction(BaseGeneratedFunction aFunction, Add aMainFunction);

	DefaultLivingClass addClass(GeneratedClass aClass, Add aMainClass);

	void addNamespace(GeneratedNamespace aNamespace, Add aNone);

	enum Add {NONE, MAIN_FUNCTION, MAIN_CLASS}
}
