/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */

package tripleo.elijah.stages.gen_c;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.VariableStatement;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.TypeTableEntry;
import tripleo.elijah.stages.gen_generic.OutputFileFactoryParams;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.VariableTableType;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.util.Helpers;
import tripleo.elijah_durable_pancake.comp.AccessBus;
import tripleo.elijah_durable_pancake.comp.impl.DefaultCompilationAccess;
import tripleo.elijah_durable_pancake.comp.impl.IO_;
import tripleo.elijah_durable_pancake.comp.PipelineLogic;
import tripleo.elijah_durable_pancake.comp.impl.StdErrSink;
import tripleo.elijah_durable_pancake.comp.internal.CompilationImpl;

import static org.easymock.EasyMock.mock;

public class GetRealTargetNameTest {

	GeneratedFunction gf;
	OS_Module mod;

	@BeforeEach
	public void setUp() throws Exception {
		mod = mock(OS_Module.class);
		final FunctionDef fd = mock(FunctionDef.class);
		gf = new GeneratedFunction(fd);
	}

	@Test
	void testManualXDotFoo() {
		final IdentExpression          x_ident   = Helpers.string_to_ident("x");
		@NotNull final IdentExpression foo_ident = Helpers.string_to_ident("foo");
		//
		// create x.foo, where x is a VAR and foo is unknown
		// neither has type information
		// GenerateC#getRealTargetName doesn't use type information
		// TODO but what if foo was a property instead of a member
		//
		final OS_Type        type      = null;
		final TypeTableEntry tte       = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, type, x_ident);
		final int            int_index = gf.addVariableTableEntry("x", VariableTableType.VAR, tte, mock(VariableStatement.class));
		final int            ite_index = gf.addIdentTableEntry(foo_ident, null);
		final IdentIA        ident_ia  = new IdentIA(ite_index, gf);
		ident_ia.setPrev(new IntegerIA(int_index, gf));
		//
		final CompilationImpl         c = new CompilationImpl(new StdErrSink(), new IO_());
		final AccessBus     ab           = new AccessBus(new DefaultCompilationAccess(c));
		final PipelineLogic           pl          = new PipelineLogic(ab);
		final OutputFileFactoryParams p           = new OutputFileFactoryParams(mod, new StdErrSink(), ElLog.Verbosity.SILENT, pl);  // TODO do we want silent?
		final GenerateC               gc           = new GenerateC(p);
		//
		Emit.emitting = false;
		final String x = gc.getRealTargetName(gf, ident_ia, Generate_Code_For_Method.AOG.GET, null); // TODO is null correct?
		Assertions.assertEquals("vvx->vmfoo", x);
	}
}

//
//
//
