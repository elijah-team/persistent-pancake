package tripleo.elijah.world.impl;

import tripleo.elijah.lang.BaseFunctionDef;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.OS_Package;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.world.i.LivingClass;
import tripleo.elijah.world.i.LivingFunction;
import tripleo.elijah.world.i.LivingPackage;
import tripleo.elijah.world.i.LivingRepo;

import java.util.HashMap;
import java.util.Map;

public class DefaultLivingRepo implements LivingRepo {
	private final Map<String, OS_Package> _packages = new HashMap<String, OS_Package>();
	private int _packageCode  = 1;
	private int _classCode    = 101;
	private int _functionCode = 1001;

	public OS_Package makePackage(final Qualident pkg_name) {
		final String pkg_name_s = pkg_name.toString();
		if (!isPackage(pkg_name_s)) {
			final OS_Package newPackage = new OS_Package(pkg_name, nextPackageCode());
			_packages.put(pkg_name_s, newPackage);
			return newPackage;
		} else
			return _packages.get(pkg_name_s);
	}

	public boolean isPackage(final String pkg) {
		return _packages.containsKey(pkg);
	}

	private int nextPackageCode() {
		return _packageCode++;
	}

	public int nextClassCode() {
		return _classCode++;
	}

	public int nextFunctionCode() {
		return _functionCode++;
	}

	@Override
	public LivingClass addClass(final ClassStatement cs) {
		return null;
	}

	@Override
	public LivingFunction addFunction(final BaseFunctionDef fd) {
		return null;
	}

	@Override
	public LivingPackage addPackage(final OS_Package pk) {
		return null;
	}
}
