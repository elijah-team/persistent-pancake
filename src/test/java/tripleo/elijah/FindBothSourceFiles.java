/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah;

import java.io.File;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.comp.GenBuffer;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.gen.ModuleRef;
import tripleo.elijah.gen.TypeRef;
import tripleo.elijah.gen.nodes.*;
import tripleo.elijah.lang.NumericExpression;
import tripleo.elijah.lang.OS_Integer;
import tripleo.util.buffer.*;

import static tripleo.elijah.gen.nodes.Helpers.List_of;

/**
 * @author Tripleo(sb)
 *
 */
public class FindBothSourceFiles /* extends TestCase */ {

	public FindBothSourceFiles(/*String name*/) {
//		super(name);
	}

	/**
	 * @throws java.lang.Exception
	 */
	protected void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	protected void tearDown() throws Exception {
	}

	/**
	 * Compiler should find both parse files
	 * 
	 * Test method for {@link tripleo.elijah.Main#parseFile(java.lang.String, java.io.InputStream)}.
	 */
	@Test
	public final void testParseFile() {
		List<String> args = List_of("test/demo-el-normal", "test/demo-el-normal/main2", "-sE");
//		ErrSink eee = JMock.of(ErrSink.class);
		ErrSink eee = new StdErrSink();
		Compilation c = new Compilation(eee, new IO());

		c.feedCmdLine(args);
		
		//fail("Not yet implemented"); // TODO
		Assert.assertTrue(c.getIO().recordedRead(new File(".", "factdemo.elijah")));
		Assert.assertTrue(c.getIO().recordedRead(new File(new File(".", "main2"), "main2.elijah")));
	}

	void main2_el(CompilerContext cctx, GenBuffer gbn) {
		ModuleRef prelude = new ModuleRef(null, -1);
		TypeRef   sys_int = new TypeRef(prelude, prelude, "SystemInteger", 80);
		ModuleRef main2   = new ModuleRef("main2/main2.elijjah", 10000);
		TypeRef   main    = new TypeRef(main2, main2, "Main", 100);
				
		gbn.InitMod(cctx, "main2/main2.elijjah");
		ImportNode impn = new ImportNode("wprust.demo.fact");
		gbn.GenImportStmt(cctx, impn);
		
		ClassDeclNode cdn = new ClassDeclNode("Main", null, 
				List_of(new Inherited("Arguments", false))); // gen inh code 
		cdn.GenClassDecl(cctx, gbn);
		
		MethHdrNode mhn = new MethHdrNode(null, cdn.type(), "main", null, 1000);
		mhn.GenMethHdr(cctx, gbn);
		mhn.BeginMeth(cctx, gbn);
		
		LocalDeclAgnNode ldan1 = new LocalDeclAgnNode("b1", ExpressionNodeBuilder.integer(3));
		gbn.GenLocalDeclAgn(cctx, ldan1);
		
		LocalDeclAgnNode ldan_a1 = new LocalDeclAgnNode("a1", sys_int, 
				ExpressionNodeBuilder.fncall("argument", 
						convertToLocalAgnTmpNode(List_of(ExpressionNodeBuilder.integer(1)))));
		gbn.GenLocalDeclAgn(cctx, ldan_a1);
		
		TmpSSACtxNode latn = new TmpSSACtxNode(cctx);
		latn.GenLocalAgn(cctx, gbn);
		
		IfNode ifn = new IfNode(latn);
		ifn.BeginIfCtx(cctx, gbn);
		
		LocalAgnNode lan_b1 = new LocalAgnNode(
				ExpressionNodeBuilder.fncall("a1.toInt", null));
		lan_b1.GenLocalAgn(cctx, gbn);
		ifn.CloseIfCtx(cctx, gbn);
		
		LocalDeclAgnNode ldan_f1 = new LocalDeclAgnNode("f1", 
				convertToLocalAgnTmpNode2(ExpressionNodeBuilder.fncall("factorial",  List_of(new LocalAgnTmpNode("b1")))));
		gbn.GenLocalAgn(cctx, ldan_f1);
		
		SimpleFnCall sfc = new SimpleFnCall("print", List_of("f1"));
		sfc.GenFnCall(cctx, gbn);
		mhn.EndMeth(cctx, gbn);
		cdn.CloseClassDecl(cctx, gbn);
		
	}
	
	
	private @NotNull OS_Integer convertToLocalAgnTmpNode2(IExpressionNode fncall) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<LocalAgnTmpNode> convertToLocalAgnTmpNode(List<@NotNull NumericExpression> list_of) {
		// TODO Auto-generated method stub
		List<LocalAgnTmpNode> r = null;
		return r;
	}

//	private List List_of(Inherited e1) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	void factorial_r(CompilerContext cctx, GenBuffer gbn) {
		ModuleRef prelude = new ModuleRef(null, -1);
		TypeRef   u64     = new TypeRef(prelude, prelude, "u64", 81);
		ModuleRef main_m  = new ModuleRef("fact.elijah", -2);
		TypeRef   main_k  = new TypeRef(main_m, main_m, "Main", 100);
		
		
		MethHdrNode mhn=new MethHdrNode(u64, main_k, "factorial_r",
				List_of(new ArgumentNode("i", u64)), 1000);
		GenMethHdr(cctx, mhn, gbn);
		BeginMeth(cctx, mhn, gbn);
		
		CaseHdrNode shn=new CaseHdrNode(ExpressionNodeBuilder.varref("i", mhn, u64));
		BeginCaseStatement(cctx, shn, gbn);
		
		CaseChoiceNode csn=new CaseChoiceNode(ExpressionNodeBuilder.integer(0), shn);
		BeginCaseChoice(cctx, csn, gbn);
		
//		ReturnAgnSimpleIntNode rasin=new ReturnAgnSimpleIntNode(ExpressionNodeBuilder.integer(1));
		ReturnAgnNode rasin=new ReturnAgnNode(ExpressionNodeBuilder.integer(1));
//		GenReturnAgnSimpleInt(cctx, rasin, gbn);
		GenReturnAgn(cctx, rasin, gbn);
		
		CloseCaseNode cccn1 = new CloseCaseNode(csn, ChoiceOptions.BREAK);
		CloseCaseChoice(cctx, cccn1, gbn);

		CaseChoiceNode csn2 = new CaseChoiceNode(cctx, ExpressionNodeBuilder.varref("n", shn, u64), shn);
		BeginDefaultCaseStatement(cctx, csn2, gbn);
		
		TmpSSACtxNode tccssan = new TmpSSACtxNode(cctx);
		LocalAgnTmpNode lamn=new LocalAgnTmpNode(tccssan, ExpressionNodeBuilder.binex(u64,
				ExpressionNodeBuilder.varref("n", shn, u64),
				ExpressionOperators.OP_MINUS, ExpressionNodeBuilder.integer(1)));
		BeginTmpSSACtx(cctx, tccssan, gbn);
		
		TmpSSACtxNode tccssan2 = new TmpSSACtxNode(cctx);
		LocalAgnTmpNode latn2=new LocalAgnTmpNode(tccssan2, ExpressionNodeBuilder.fncall(
				"factorial_r", List_of(lamn)));
		GenLocalAgn(cctx, latn2, gbn);
		
		TmpSSACtxNode tccssan3 = new TmpSSACtxNode(cctx);
		LocalAgnTmpNode latn3=new LocalAgnTmpNode(tccssan3, ExpressionNodeBuilder.binex(u64,
				ExpressionNodeBuilder.varref("n", mhn, u64), ExpressionOperators.OP_MULT, tccssan2));
		GenLocalAgn(cctx, latn3, gbn);
		
//		ReturnAgnSimpleIntNode rsin=new ReturnAgnSimpleIntNode(latn3); // TODO Not a simple int
		ReturnAgnNode rsin=new ReturnAgnNode(latn3); // TODO Not an expression!!!
		GenReturnAgn(cctx, rsin, gbn);
		
		CloseTmpCtx(cctx, latn3, gbn);
/*
		CloseTmpCtx(cctx, latn2, gbn);
		CloseTmpCtx(cctx, lamn, gbn);
*/

		CloseCaseNode cccn2=new CloseCaseNode(csn2, ChoiceOptions.BREAK, true);
		CloseCaseChoice(cctx, cccn2, gbn);
		
		EndCaseStatement(cctx, shn, gbn);
		EndMeth(cctx, mhn, gbn);

	}
	
	private void GenReturnAgn(CompilerContext cctx, ReturnAgnNode node, GenBuffer gbn) {
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		buf.append("vsr =");
		buf.append(node.getExpr().genText(cctx));
		buf.append_ln(";");
		// if(node.usesRetKW()) {buf.append ("return vsr;");}
	}
	
	@Deprecated private void GenReturnAgnSimpleInt(CompilerContext cctx, ReturnAgnSimpleIntNode rasin, GenBuffer gbn) {
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		buf.append("vsr = ");
		buf.append(((Integer)rasin.getValue()).toString());
		buf.append_nl(";");
	}
	
	public static Buffer GenLocalAgn(CompilerContext cctx, LocalAgnTmpNode node, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
//		if (node instanceof LocalAgnTmpNode) {
//			BufferSequenceBuilder sb=new BufferSequenceBuilder(6).
//					named("open").named("type").named("name").
//					named("equality").named("value").semieol();
//			sb.set("open", "{", XX.INDENT);
//			sb.set("type", node.getRight().genType, XX.SPACE);
//		}
		buf.append(node.getLeft().genText(cctx));
		buf.append(" = ");
		buf.append(node.getRight().genText(cctx));
		buf.append_ln(";");
		return buf;
	}
	
	private void CloseTmpCtx(CompilerContext cctx, LocalAgnTmpNode lamn, GenBuffer gbn) {
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		buf.append_cb(""); // close-brace
	}
	
	private void BeginDefaultCaseStatement(CompilerContext cctx, CaseChoiceNode node, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		buf.incr_i();
		buf.append_ln("default: {");
		if (!node.is_default())
			throw new IllegalStateException("node is not default");
		//
		final VariableReferenceNode varref = node.varref();
		if (varref == null)
			throw new IllegalStateException("varref is null");
		
		{
			buf.append_s(node.header.getExpr().genType());//varref().getType().getText());
			String string;
			if (varref != null) {
				string = varref.genText();
			} else {
				System.err.println("reached no mans land #6");
				string = "----------------------6";
			}
			buf.append_s(string);
			buf.append(" = ");
			buf.append(node.header.simpleGenText());
			buf.append_ln(";");
/*
		} else {
			final VariableReferenceNode3 vr3 = node.varref3();
			buf.append_s(vr3.getType().genType());
			buf.append(vr3.genText());
			buf.append(" = ");
			buf.append(node.header.simpleGenText());
			buf.append_ln(";");
		}
*/
		}
	}

	private void BeginCaseChoice(CompilerContext cctx, CaseChoiceNode node, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		boolean is_simple = node.left.is_const_expr();
		boolean is_default = node.is_simple();
		if (is_default) {
			assert node.left.is_underscore() ||
					(node.left.is_var_ref() && node.left.ref_ == null);
		}
		if (is_simple ){
			buf.append("case "+node.left.genText(cctx));
			buf.append_s(": {");
		}
		if (is_default) {
			buf.append_nl("default:");
		}
	}
	
	private void CloseCaseChoice(CompilerContext cctx, CloseCaseNode node, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf = gbn.moduleBufImpl(cctx.module());
		buf.decr_i();
		buf.append_ln("break; }");
//		buf.append_nl("} // close select ("+node.hdr_node.left.genName+")"); // TODO left was expr
	}
	
	public static Buffer BeginTmpSSACtx(CompilerContext cctx, TmpSSACtxNode node, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		buf.incr_i();
		buf.append_ln("{");
		if (node._tmp != null) {
//			NotImplementedException.raise();
//			buf.append(node._tmp.genName());
			buf.append(node._tmp.getRight().genType());
		}else {
			buf.append(node.getType().genText(cctx));
		}
		buf.append(" ");
		return buf;
	}

	private void BeginCaseStatement(CompilerContext cctx, CaseHdrNode node, GenBuffer gbn) {
		Buffer buf = gbn.moduleBufImpl(cctx.module());
		boolean is_simple = node.getExpr().is_simple();
		buf.append_s("switch (");
		if (is_simple) {
			buf.append(node.getExpr().genText(cctx));
		} else {
			// TODO implement complex part
		}
		buf.append_nl_i(") {");
		buf.incr_i();
	}
	
	private void EndCaseStatement(CompilerContext cctx, CaseHdrNode node, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		buf.decr_i();
		buf.append_nl("} // close select "+node.getExpr().genText()+"");
	}
	
	public class Transform1 implements Transform {
		/* (non-Javadoc)
		 * @see tripleo.util.buffer.Transform#transform(tripleo.elijah.gen.nodes.ArgumentNode, tripleo.util.buffer.DefaultBuffer)
		 */
		@Override
		public void transform(ArgumentNode na, DefaultBuffer bufbldr) {
			bufbldr.append_s(na.getGenType(), XX.SPACE);
			bufbldr.append(na.getVarName());
		}
	}

	private void BeginMeth(CompilerContext cctx, MethHdrNode node, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
/*
		BufferSequenceBuilder sb = new BufferSequenceBuilder(4).
				named("type").named("name").named("args").semieol();
		sb.set("type", node.returnType.genType, XX.SPACE);
		sb.set("name", node.methName.genName);
		EnclosedBuffer sb2 = new EnclosedBuffer("(", XX.RPAREN);
		Transform1 transform1 = new Transform1();
		BufferSequenceBuilder sb3 = new BufferSequenceBuilder(node.argCount,
				node.ArgumentsIterator(), transform1, XX.COMMA, gbn);
		sb2.setPayload(sb3);
		sb.set("args", sb2);
		CodeGen gbm = gbn.getCodeGen(); // TODO should be CSimpleGen
		gbm.appendHeader(cctx.module(), sb.build());
*/
		buf.append_s(node.returnType().genType());
		buf.append(node.genName());
		buf.append("(");
		for (int c=0;c<node.argCount;c++) {
			buf.append_s(node.argument(c).getGenType());
			buf.append(node.argument(c).getGenName());
			if (c<node.argCount-1) {buf.append(",");}
		}
		buf.append_ln(") {");
		buf.incr_i();
		}
	
	private void EndMeth(CompilerContext cctx, MethHdrNode mhn, GenBuffer gbn) {
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		buf.append_ln("return vsr;");
		buf.decr_i();
		buf.append_ln("}");
	}
	
	public void GenMethHdr(CompilerContext cctx, MethHdrNode node, GenBuffer gbn) {
		Buffer buf = gbn.moduleBufHdr(cctx.module());
		buf.append_s(node.returnType().genType());
		buf.append(node.genName());
		buf.append("(");
		for (int c=0;c<node.argCount;c++) {
			buf.append_s(node.argument(c).getGenType());
			buf.append(node.argument(c).getGenName());
			if (c<node.argCount-1) {buf.append(",");}
		}
		buf.append(");");
	}
		
}
	
//
//
//
