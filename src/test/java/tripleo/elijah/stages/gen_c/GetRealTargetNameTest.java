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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.RegularTypeName;
import tripleo.elijah.lang.VariableSequence;
import tripleo.elijah.lang.VariableStatement;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.TypeTableEntry;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.VariableTableType;
import tripleo.elijah.test_help.Boilerplate;
import tripleo.elijah.util.Helpers;

import static org.easymock.EasyMock.*;

public class GetRealTargetNameTest {
	private Boilerplate boilerPlate; // NOTE hmm. (reduce) boilerplate reductionism

	GeneratedFunction gf;
	OS_Module mod;

	@Before
	public void setUp() throws Exception {
		mod = mock(OS_Module.class);
		FunctionDef fd = mock(FunctionDef.class);
		gf = new GeneratedFunction(fd);

		boilerPlate = new Boilerplate();
		boilerPlate.get();
	}

	static class XX {

		public IdentExpression ident(final String aX) {
			final IdentExpression identExpression = Helpers.string_to_ident(aX);
			return identExpression;
		}

		public TypeTableEntry regularTypeName_specifyTableEntry(final IdentExpression aIdentExpression,
																final @NotNull BaseGeneratedFunction aBaseGeneratedFunction,
																final String aTypeName) {
			final RegularTypeName typeName = RegularTypeName.makeWithStringTypeName(aTypeName);
			final OS_Type         type     = new OS_Type(typeName);
			final TypeTableEntry  tte      = aBaseGeneratedFunction.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, type, aIdentExpression);

			return tte;
		}

		public VariableStatement sequenceAndVarNamed(final IdentExpression aIdentExpression) {
			final VariableSequence  seq   = new VariableSequence();
			final VariableStatement x_var = new VariableStatement(seq);

			x_var.setName(aIdentExpression);

			return x_var;
		}
	}

	@Test // too complicated
	@SuppressWarnings("JUnit3StyleTestMethodInJUnit4Class")
	public void testManualXDotFoo() {
		final XX              xx        = new XX();
		final IdentExpression x_ident   = xx.ident("x");
		final IdentExpression foo_ident = xx.ident("foo");

		//
		// create x.foo, where x is a VAR and foo is unknown
		// neither has type information
		// GenerateC#getRealTargetName doesn't use type information
		// TODO but what if foo was a property instead of a member
		//
		final TypeTableEntry    tte       = xx.regularTypeName_specifyTableEntry(x_ident, gf, "X_Type");
		final VariableStatement x_var     = xx.sequenceAndVarNamed(x_ident);
		final int               int_index = gf.addVariableTableEntry("x", VariableTableType.VAR, tte, x_var);
		final int               ite_index = gf.addIdentTableEntry(foo_ident, null);
		final IdentIA           ident_ia  = new IdentIA(ite_index, gf);
		final IntegerIA         integerIA = new IntegerIA(int_index, gf);
		ident_ia.setPrev(integerIA);

		Emit.emitting = false;

		//

		// TODO do we want silent?

		final OS_Module mod = boilerPlate.defaultMod();
		mod.setParent(boilerPlate.comp);

		final DeducePhase  phase        = boilerPlate.getDeducePhase();
		final DeduceTypes2 deduceTypes2 = new DeduceTypes2(mod, phase);
		final Context      ctx          = mock(Context.class);

		(gf.getIdentTableEntry(0)).setDeduceTypes2(deduceTypes2, ctx, gf);

		final LookupResultList lrl = new LookupResultList();
		lrl.add(x_ident.getText(), 1,x_var, null);

//		expect(ctx.lookup(foo_ident.getText())).andReturn(lrl);
		expect(ctx.lookup(x_ident.getText())).andReturn(lrl);
		expect(ctx.lookup(x_ident.getText())).andReturn(lrl);

		replay(ctx);

		final GenType genType = new GenType();
		genType.typeName = tte.getAttached();
		integerIA.getEntry().resolveType(genType);

		//
		//
		//

		boilerPlate.getGenerateFiles(mod);

		String x = ((GenerateC) boilerPlate.generateFiles).getRealTargetName(gf, ident_ia, Generate_Code_For_Method.AOG.GET, null);
		Assert.assertEquals("vvx->vmfoo", x);
	}
}

//
//
//
