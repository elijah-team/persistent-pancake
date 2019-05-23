header {
  package tripleo.elijjah;
}

{
import tripleo.elijah.lang.*;
import tripleo.elijah.*;
}

class ElijjahParser extends Parser;
options {
	exportVocab=Elijjah;
//	defaultErrorHandler=false;
	k=2;
//	buildAST = true;
}

tokens {
    "tokens";
}

{
Qualident xy;
Out out;
IExpression expr;
}

program
        {String xx=null; ExpressionList el=null;	ParserClosure pc = out.closure();}
    : ( "indexing" (IDENT TOK_COLON {el=new ExpressionList();} expressionList[el])*
	  |"package" xy=qualident {pc.packageName(xy);}
	  |programStatement[pc])*
	  EOF {out.FinishModule();}
	;
constantValue returns [IExpression e]
	 {e=null;}
	:s:STRING_LITERAL {e=new StringExpression(s);}
	|c:CHAR_LITERAL   {e=new CharLitExpression(c);}
	|n:NUM_INT        {e=new NumericExpression(n);}
	|f:NUM_FLOAT      {e=new FloatExpression(f);}
	;
primitiveExpression  returns [IExpression e]
		{e=null;ExpressionList el=null;}
	: e=constantValue
	| e=variableReference
	| LBRACK        {e=new ListExpression();el=new ExpressionList();}
	    expressionList[el]  {((ListExpression)e).setContents(el);}
	  RBRACK
	;
qualident returns [Qualident q]
    {q=new Qualident();}
	:
     r1:IDENT /*ident2 */ {q.append(r1);}
      (d1:DOT r2:IDENT {q.appendDot(d1); q.append(r2);})*
    ;
classStatement [ClassStatement cls]:
    "class" ("interface"|"struct"|"signature"|"abstract")?
      i1:IDENT {cls.setName(i1);}
    ((LPAREN classInheritance_ [cls.classInheritance()] RPAREN)
    | classInheritanceRuby [cls.classInheritance()] )?
    LCURLY
     docstrings[cls]
     classScope[cls]
    RCURLY
    ;
namespaceStatement [NamespaceStatement cls]:
    "namespace" i1:IDENT {cls.setName(i1);}
    LCURLY
     docstrings[cls]
     namespaceScope[cls]
    RCURLY
    ;
importStatement [ImportStatement pc]:
      "from" xy=qualident "import" qualidentList[pc.importList()] {pc.importRoot(xy);}
    | "import" importPart[pc] (COMMA importPart[pc])*
    // next?
    ;
importPart [ImportStatement cr] //current rule
		{Qualident q1,q2,q3;IdentList il=null;}
    : i1:IDENT BECOMES q1=qualident {cr.addAssigningPart(i1,q1);}
    | (qualident LCURLY) =>
        q3=qualident LCURLY { il=cr.addSelectivePart(q3);} identList[il] RCURLY
    | q2=qualident {cr.addNormalPart(q2);}
    ;
classInheritance_[ClassInheritance ci]:
    inhTypeName[ci.next()]
      (COMMA inhTypeName[ci.next()])*
    ;
classInheritanceRuby[ClassInheritance ci]:
    LT_ classInheritance_[ci]
    ;
docstrings[Scope sc]:
    (s1:STRING_LITERAL {sc.addDocString(s1);})+
    ;
classScope[ClassStatement cr]
        {Scope sc=cr;}
    : docstrings[cr]
    ( ("constructor"|"ctor") x1:IDENT {sc=cr.addCtor(x1);} scope[sc]
    |    ("destructor"|"dtor") {sc=cr.addDtor();} scope[sc]
    | functionDef[cr.funcDef()]
    | varStmt[cr.statementClosure()]
    | typeAlias[cr.typeAlias()]
    | programStatement[cr.XXX()]
    | invariantStatement[cr.invariantStatement()]
    | accessNotation)*
    ;
namespaceScope[NamespaceStatement cr]
        {Scope sc=cr;}
    : docstrings[cr]
    ( functionDef[cr.funcDef()]
    | varStmt[cr.statementClosure()]
    | typeAlias[cr.typeAlias()]
    | programStatement[cr.XXX()]
    | invariantStatement[cr.invariantStatement()]
    | accessNotation)*
    ;
inhTypeName[TypeName tn]:
    (("const" {tn.set(TypeModifiers.CONST);})? specifiedGenericTypeName_xx[tn]
    | "typeof" xy=qualident {tn.typeof(xy);})
    ;
typeName[TypeName cr]:
      structTypeName[cr]
    | funcTypeExpr[cr]
    | simpleTypeName_xx[cr]
    ;
scope[Scope sc]
      {IExpression expr;}
    : LCURLY docstrings[sc]
      (statement[sc.statementClosure()]
      | expr=expression {sc.statementWrapper(expr);}
      )*
      RCURLY
    ;
functionDef[FunctionDef fd]:
    i1:IDENT {fd.setName(i1);}
    (TOK_ARROW typeName[fd.returnType()])?
    ( "const"|"immutable" )?
    opfal[fd.fal()]
    scope[fd.scope()]
    ;
programStatement[ProgramClosure pc]:
    importStatement[pc.importStatement(out.module())]
    | namespaceStatement[pc.namespaceStatement(out.module())]
    | classStatement[pc.classStatement(out.module())]
    | aliasStatement[pc.aliasStatement()]
    ;
varStmt[StatementClosure cr]
        {VariableSequence vsq=null;}
    : ("var" {vsq=cr.varSeq();}
    | ("const"|"val") {vsq=cr.varSeq();vsq.defaultModifiers(TypeModifiers.CONST);})
    ( varStmt_i[vsq.next()] (COMMA varStmt_i[vsq.next()])*
  //    LPAREN identList[cr.identList()] RPAREN BECOMES eee=expression // TODO what is this?
    )
    ;
typeAlias[TypeAliasExpression cr]
		{Qualident q=null;}
	:
	"type" "alias" i:IDENT {cr.setIdent(i);}
		BECOMES q=qualident {cr.setBecomes(q);}
	| "struct" name:IDENT opfal[null] scope[null] // TODO shouldnt be here
	;
opfal[FormalArgList fal]:
	LPAREN formalArgList[fal] RPAREN
	;
opfal2 returns [FormalArgList fal]
		{fal=new FormalArgList();}
	: LPAREN formalArgList[fal] RPAREN
	;
statement[StatementClosure cr]
		{Qualident q=null;FormalArgList o=null;}
	:
	(procedureCallStatement[cr.procCallExpr()]
	| ifConditional[cr.ifExpression()]
	| varStmt[cr]
	| whileLoop[cr]
	| frobeIteration[cr]
	| "construct" q=qualident o=opfal2 {cr.constructExpression(q,o);}
	| "yield" expr=expression {cr.yield(expr);}
	) opt_semi
	;
opt_semi: (SEMI|);
identList[IdentList ail]
		//{String s=null;}
	: s:IDENT {ail.push(new IdentExpression(s));}
	(COMMA s2:IDENT {ail.push(new IdentExpression(s2));})*
	;
expression returns [IExpression ee]
		{ee=null;}
	: ee=assignmentExpression
	;
variableQualifiers[TypeName cr]:
	( "once"  {cr.set(TypeModifiers.ONCE);}
	| "local"  {cr.set(TypeModifiers.LOCAL);}
	| "tagged"  {cr.set(TypeModifiers.TAGGED);}
	| "const"  {cr.set(TypeModifiers.CONST);}
	| "pooled"  {cr.set(TypeModifiers.POOLED);}
	| "manual"  {cr.set(TypeModifiers.MANUAL);}
	| "gc"  {cr.set(TypeModifiers.GC);})
	;
regularQualifiers[TypeName fp]:
	( "in"            {fp.setIn(true);}
	| "out"           {fp.setOut(true);}
	| ("const"        {fp.setConstant(true);} |"ref")
	  (IDENT
	  |"ref"          {fp.setReference(true);} 
	  |"generic"	  {fp.setGeneric(true);})
	)
	;
typeNameList[TypeNameList cr]:
	typeName [cr.next()] (COMMA typeName [cr.next()])*
	;
ident2 returns [String ident]
		{ident=null;}
	:
	r1:IDENT {ident=r1.getText();}
	;
aliasStatement[ProgramClosure pc]
	: "alias" i1:IDENT BECOMES expr=expression {}
	;
qualidentList[QualidentList qal]
		{Qualident qid;}
	: qid=qualident {qal.add(qid);} (COMMA qid=qualident {qal.add(qid);})*
	;
ident returns [IdentExpression id]
		{id=null;}
	: r1:IDENT {id=new IdentExpression(r1);}
	;
expressionList[ExpressionList el]
	: expr=expression {el.next(expr);} (COMMA expr=expression {el.next(expr);})*
	;
expressionList2 returns [ExpressionList el]
		{el = new ExpressionList();}
	: expr=expression {el.next(expr);} (COMMA expr=expression {el.next(expr);})*
	;
variableReference returns [IExpression ee]
		{VariableReference vr=new VariableReference();ProcedureCallExpression pcx;ee=null;}
	: r1:IDENT  {vr.setMain(r1);}
	( DOT r2:IDENT {vr.addIdentPart(r2);}
	| LBRACK expr=expression RBRACK {vr.addArrayPart(expr);}
	| pcx=procCallEx2 {vr.addProcCallPart(pcx);}
	) {ee=vr;}
	;
procCallEx2 returns [ProcedureCallExpression pce]
		{pce=null;ExpressionList el=null;}
	: lp:LPAREN el=expressionList2 rp:RPAREN {pce=new ProcedureCallExpression(lp,el,rp);}
	;
invariantStatement[InvariantStatement cr]
	: "invariant"
	;
accessNotation
	: "access" IDENT
	;


// expressions
// Note that most of these expressions follow the pattern
//   thisLevelExpression :
//       nextHigherPrecedenceExpression
//           (OPERATOR nextHigherPrecedenceExpression)*
// which is a standard recursive definition for a parsing an expression.
// The operators in java have the following precedences:
//    lowest  (13)  = *= /= %= += -= <<= >>= >>>= &= ^= |=
//            (12)  ?:
//            (11)  ||
//            (10)  &&
//            ( 9)  |
//            ( 8)  ^
//            ( 7)  &
//            ( 6)  == !=
//            ( 5)  < <= > >=
//            ( 4)  << >>
//            ( 3)  +(binary) -(binary)
//            ( 2)  * / %
//            ( 1)  ++ -- +(unary) -(unary)  ~  !  (type)
//                  []   () (method call)  . (dot -- identifier qualification)
//                  new   ()  (explicit parenthesis)
//
// the last two are not usually on a precedence chart; I put them in
// to point out that new has a higher precedence than '.', so you
// can validy use
//     new Frame().show()
//
// Note that the above precedence levels map to the rules below...
// Once you have a precedence chart, writing the appropriate rules as below
//   is usually very straightfoward


// assignment expression (level 13)
assignmentExpression returns [IExpression ee]
		{ee=null;IExpression e=null;IExpression e2;}
	:	e=conditionalExpression
		(

			(	ASSIGN/*^*/					{ee = ExpressionBuilder.buildPartial(ee, ExpressionType.ASSIGNMENT);}
            |   PLUS_ASSIGN/*^*/		    {ee = ExpressionBuilder.buildPartial(ee, ExpressionType.AUG_PLUS);}
            |   MINUS_ASSIGN/*^*/			{ee = ExpressionBuilder.buildPartial(ee, ExpressionType.AUG_MINUS);}
            |   STAR_ASSIGN/*^*/			{ee = ExpressionBuilder.buildPartial(ee, ExpressionType.AUG_MULT);}
            |   DIV_ASSIGN/*^*/				{ee = ExpressionBuilder.buildPartial(ee, ExpressionType.AUG_DIV);}
            |   MOD_ASSIGN/*^*/				{ee = ExpressionBuilder.buildPartial(ee, ExpressionType.AUG_MOD);}
            |   SR_ASSIGN/*^*/				{ee = ExpressionBuilder.buildPartial(ee, ExpressionType.AUG_SR);}
            |   BSR_ASSIGN/*^*/				{ee = ExpressionBuilder.buildPartial(ee, ExpressionType.AUG_BSR);}
            |   SL_ASSIGN/*^*/			    {ee = ExpressionBuilder.buildPartial(ee, ExpressionType.AUG_SL);}
            |   BAND_ASSIGN/*^*/			{ee = ExpressionBuilder.buildPartial(ee, ExpressionType.AUG_BAND);}
            |   BXOR_ASSIGN/*^*/			{ee = ExpressionBuilder.buildPartial(ee, ExpressionType.AUG_BXOR);}
            |   BOR_ASSIGN/*^*/				{ee = ExpressionBuilder.buildPartial(ee, ExpressionType.AUG_BOR);}
            )

			e2=assignmentExpression {	((IBinaryExpression) ee).setRight(e2);}
		)?
	;


// conditional test (level 12)
conditionalExpression returns [IExpression ee]
		{ee=null;}
	:	ee=logicalOrExpression
//		( QUESTION/*^*/ assignmentExpression COLON/*!*/ conditionalExpression )? // TODO ignoring this for now
	;


// logical or (||)  (level 11)
logicalOrExpression returns [IExpression ee]
		{ee=null;
		IExpression e3=null;}
	:	ee=logicalAndExpression
		(LOR/*^*/ e3=logicalAndExpression   	{ee = ExpressionBuilder.build(ee, ExpressionType.LOR, e3);})*
	;


// logical and (&&)  (level 10)
logicalAndExpression returns [IExpression ee]
		{ee=null;IExpression e3=null;}
	:	ee=inclusiveOrExpression
		(LAND/*^*/ e3=inclusiveOrExpression	{ee = ExpressionBuilder.build(ee, ExpressionType.LAND, e3);})*
	;


// bitwise or non-short-circuiting or (|)  (level 9)
inclusiveOrExpression returns [IExpression ee]
		{ee=null;IExpression e3=null;}
	:	ee=exclusiveOrExpression
		(BOR/*^*/ e3=exclusiveOrExpression		{ee = ExpressionBuilder.build(ee, ExpressionType.BOR, e3);})*
	;


// exclusive or (^)  (level 8)
exclusiveOrExpression returns [IExpression ee]
		{ee=null;
		IExpression e3=null;}
	:	ee=andExpression
		(BXOR/*^*/ e3=andExpression				{ee = ExpressionBuilder.build(ee, ExpressionType.BXOR, e3);})*
	;


// bitwise or non-short-circuiting and (&)  (level 7)
andExpression returns [IExpression ee]
		{ee=null;
		IExpression e3=null;}
	:	ee=equalityExpression
		(BAND/*^*/ e3=equalityExpression	{ee = ExpressionBuilder.build(ee, ExpressionType.BAND, e3);})*
	;


// equality/inequality (==/!=) (level 6)
equalityExpression  returns [IExpression ee]
		{ee=null;
		ExpressionType e2=null;
		IExpression e3=null;}
	:	ee=relationalExpression
		((NOT_EQUAL/*^*/                   {e2=ExpressionType.NOT_EQUAL;}
		| EQUAL/*^*/                       {e2=ExpressionType.EQUAL;}
		) e3=relationalExpression 				{ee = ExpressionBuilder.build(ee, e2, e3);})*
	;


// boolean relational expressions (level 5)
relationalExpression returns [IExpression ee]
		{ee=null;
		ExpressionType e2;
		IExpression e3=null;}

	:	ee=shiftExpression
		(	(	(	LT_/*^*/            {e2=ExpressionType.LT_;}
				|	GT/*^*/             {e2=ExpressionType.GT;}
				|	LE/*^*/             {e2=ExpressionType.LE;}
				|	GE/*^*/             {e2=ExpressionType.GE;}
				)
				e3=shiftExpression
			)*
		|	"is_a"/*^*/ typeName[null] //typeSpec[true]
		)
	;


// bit shift expressions (level 4)
shiftExpression returns [IExpression ee]
		{ee=null;ExpressionType e2=null;
		IExpression e3=null;}
	:	additiveExpression
	((SL/*^*/ {e2=ExpressionType.LSHIFT;}
	 | SR/*^*/ {e2=ExpressionType.RSHIFT;}
	 | BSR/*^*/ {e2=ExpressionType.BSHIFTR;}
	 ) e3=additiveExpression				{ee = ExpressionBuilder.build(ee, e2, e3);})*
	;


// binary addition/subtraction (level 3)
additiveExpression returns [IExpression ee]
		{ee=null;ExpressionType e2=null;
		IExpression e3=null;}
	:	multiplicativeExpression 
	(
		( PLUS/*^*/  {e2=ExpressionType.ADDITION;}
		| MINUS/*^*/ {e2=ExpressionType.SUBTRACTION;}) 
		multiplicativeExpression {ee = ExpressionBuilder.build(ee, e2, e3);})*
	;


// multiplication/division/modulo (level 2)
multiplicativeExpression returns [IExpression ee]
		{ee=null;
		IExpression e3=null;ExpressionType e2=null;}
	:	ee=unaryExpression 
	((STAR/*^*/ {e2=ExpressionType.MULTIPLY;}
	| DIV/*^*/  {e2=ExpressionType.DIVIDE;}
	| MOD/*^*/  {e2=ExpressionType.MODULO;}
	) e3=unaryExpression {ee = ExpressionBuilder.build(ee, e2, e3);})*
	;

unaryExpression returns [IExpression ee]
		{ee=null;
		IExpression e3=null;}
	:	INC/*^*/ ee=unaryExpression {ee.set(ExpressionType.INC);}
	|	DEC/*^*/ ee=unaryExpression {ee.set(ExpressionType.DEC);}
	|	MINUS/*^*/ /*{#MINUS.setType(UNARY_MINUS);}*/ ee=unaryExpression {ee.set(ExpressionType.NEG);}
	|	PLUS/*^*/  /*{#PLUS.setType(UNARY_PLUS);}*/ ee=unaryExpression {ee.set(ExpressionType.POS);}
	|	ee=unaryExpressionNotPlusMinus
	;

unaryExpressionNotPlusMinus returns [IExpression ee]
		{ee=null;
		IExpression e3=null;}
	:	BNOT/*^*/ ee=unaryExpression {ee.set(ExpressionType.BNOT);}
	|	LNOT/*^*/ ee=unaryExpression {ee.set(ExpressionType.LNOT);}

	|	(	// subrule allows option to shut off warnings
			options {
				// "(int" ambig with postfixExpr due to lack of sequence
				// info in linear approximate LL(k).  It's ok.  Shut up.
				generateAmbigWarnings=false;
			}
		:	// If typecast is built in type, must be numeric operand
			// Also, no reason to backtrack if type keyword like int, float...
			lpb:LPAREN/*^*/ /*{#lpb.setType(TYPECAST);}*/ builtInTypeSpec[true] RPAREN/*!*/
			ee=unaryExpression

			// Have to backtrack to see if operator follows.  If no operator
			// follows, it's a typecast.  No semantic checking needed to parse.
			// if it _looks_ like a cast, it _is_ a cast; else it's a "(expr)"
//		|	(LPAREN classTypeSpec[true] RPAREN unaryExpressionNotPlusMinus)=>
//			lp:LPAREN/*^*/ /*{#lp.setType(TYPECAST);}*/ classTypeSpec[true] RPAREN/*!*/
//			unaryExpressionNotPlusMinus

		|	ee=postfixExpression
		)
	;

// qualified names, array expressions, method invocation, post inc/dec
postfixExpression returns [IExpression ee]
		{ee=null;
		IExpression e3=null;ExpressionList el=null;}
	:	ee=primaryExpression // start with a primary

		(	// qualified id (id.id.id.id...) -- build the name
			DOT/*^*/ ( IDENT
				| "this"
				| "class"
//				| newExpression
//				| "inherit" LPAREN ( expressionList2 )? RPAREN
				)
			// the above line needs a semantic check to make sure "class"
			//   is the _last_ qualifier.

			// allow ClassName[].class
		|	( lbc:LBRACK/*^*/ /*{#lbc.setType(ARRAY_DECLARATOR);}*/ RBRACK/*!*/ )+
			DOT/*^*/ "class"

			// an array indexing operation
		|	lb:LBRACK/*^*/ /*{#lb.setType(INDEX_OP);}*/ expr=expression RBRACK/*!*/

			// method invocation
			// The next line is not strictly proper; it allows x(3)(4) or
			//  x[2](4) which are not valid in Java.  If this grammar were used
			//  to validate a Java program a semantic check would be needed, or
			//   this rule would get really ugly...
		|	lp:LPAREN/*^*/ /*{#lp.setType(METHOD_CALL);}*/
/////////////////////////////////////////////////////////////////				argList
el=expressionList2 ////////////////
			RPAREN/*!*/
		)*

		// possibly add on a post-increment or post-decrement.
		// allows INC/DEC on too much, but semantics can check
		(	in:INC/*^*/ /*{#in.setType(POST_INC);}*/
	 	|	de:DEC/*^*/ /*{#de.setType(POST_DEC);}*/
		|	// nothing
		)

		// look for int.class and int[].class
//	|	builtInType
//		( lbt:LBRACK/*^*/ /*{#lbt.setType(ARRAY_DECLARATOR);}*/ RBRACK/*!*/ )*
//		DOT/*^*/ "class"
	;

// the basic element of an expression
primaryExpression returns [IExpression ee]
		{ee=null;
		IExpression e3=null;}
	:	e:IDENT {ee=new IdentExpression(e);}
//	|	newExpression
	|	expr=constantValue
//	|	"super"
	|	"true"
	|	"false"
	|	"this"
	|	"null"
	|	LPAREN/*!*/ ee=assignmentExpression RPAREN/*!*/
	;
// A builtin type specification is a builtin type with possible brackets
// afterwards (which would make it an array type).
builtInTypeSpec[boolean addImagNode]
	:	builtInType (lb:LBRACK/*^*/ /*{#lb.setType(ARRAY_DECLARATOR);}*/ RBRACK/*!*/)*
		{
			if ( addImagNode ) {
//				#builtInTypeSpec = #(#[TYPE,"TYPE"], #builtInTypeSpec);
			}
		}
	;

// A type name. which is either a (possibly qualified) class name or
//   a primitive (builtin) type
type
	:	IDENT // identifier
//	|	builtInType
	;


// The primitive types.
builtInType
	:	"void"
	|	"boolean"
	|	"byte"
	|	"char"
	|	"short"
	|	"int"
	|	"float"
	|	"long"
	|	"double"
	;







procedureCallStatement[StatementClosure cr]
	 {ProcedureCallExpression pce=cr.procedureCallExpression();}
//	 {ProcedureCallStatement pce=cr.procedureCallStatement();}
	: xy=qualident {pce.identifier(xy);}
	  procCallEx[pce]
	;
ifConditional[IfExpression ifex]
	: "if" expr=expression {ifex.expr(expr);}
	scope[ifex.scope()]
	("else" scope[ifex.else_().scope()]
	| elseif_part[ifex.elseif()]
	)*
	;
whileLoop[StatementClosure cr]
	 {Loop loop=cr.loop();}
	:( "while"                 {loop.type(Loop.WHILE);}
	  expr=expression         {loop.expr(expr);}
	  scope[loop.scope()]
	| "do"                    {loop.type(Loop.DO_WHILE);}
	  scope[loop.scope()]
      "while" expr=expression {loop.expr(expr);})
    ;
frobeIteration[StatementClosure cr]
	 {Loop loop=cr.loop();}
	:"iterate"
	( "from"                   {loop.type(Loop.FROM_TO_TYPE);}
	  expr=expression          {loop.frompart(expr);}
	  "to" expr=expression     {loop.topart(expr);}
	| "to"                     {loop.type(Loop.TO_TYPE);}
	  expr=expression          {loop.topart(expr);}
	|                          {loop.type(Loop.EXPR_TYPE);}
	  expr=expression          {loop.topart(expr);}
      	("with" i1:IDENT       {loop.iterName(i1);})?
    )
    scope[loop.scope()]
    ;
procCallEx[ProcedureCallExpression pce]
	: LPAREN expressionList[pce.exprList()] RPAREN
	;
varStmt_i[VariableStatement vs]
	: i:IDENT                  {vs.setName(i);}
	( TOK_COLON typeName[vs.typeName()])?
	( BECOMES expr=expression  {vs.initial(expr);})?
	;
elseif_part[IfExpression ifex]
	: "elseif" expr=expression {ifex.expr(expr);}
	scope[ifex.scope()]
	;
structTypeName[TypeName cr]
	:
	( genericQualifiers[cr]
	  ( abstractGenericTypeName_xx[cr]
	  | specifiedGenericTypeName_xx[cr] )
	| "typeof" xy=qualident {cr.typeof(xy);}
	)
	;
genericQualifiers[TypeName cr]
	: ( "const"    {cr.set(TypeModifiers.CONST);})?
	  ( "ref"      {cr.set(TypeModifiers.REFPAR);})?
	;
abstractGenericTypeName_xx[TypeName tn]
	: "generic" xy=qualident {tn.typeName(xy); tn.set(TypeModifiers.GENERIC);}
	;
specifiedGenericTypeName_xx[TypeName tn]
	: simpleTypeName_xx [tn]
	  (LBRACK typeName[tn] RBRACK)
	;
formalArgTypeName[TypeName tn]
	: structTypeName[tn]
	| funcTypeExpr[tn]
	;
simpleTypeName_xx[TypeName tn]
	: xy=qualident  {tn.setName(xy);}
	;
defFunctionDef[DefFunctionDef fd]
		{FormalArgList op=null;}
	: "def" i1:IDENT op=opfal2/*[fd.fal()]*/  BECOMES expr=expression
	   {fd.setType(DefFunctionDef.DEF_FUN); fd.setName(i1); fd.setOpfal(op); fd.setExpr(expr); }
	;
funcTypeExpr[TypeName pc]
	:
	( "function"  {	pc.type(TypeModifiers.FUNCTION);	}
	  (LPAREN typeNameList[pc.argList()] RPAREN)?
	  ((TOK_ARROW|TOK_COLON) typeName[pc.returnValue()] )?
	| "procedure" {	pc.type(TypeModifiers.PROCEDURE);	}
	  (LPAREN typeNameList[pc.argList()] RPAREN)?
	)
	;
formalArgList[FormalArgList fal]
	: formalArgListItem_priv[fal.next()]
	  (COMMA formalArgListItem_priv[fal.next()])*
	;
formalArgListItem_priv[FormalArgListItem fali]
	:
		( regularQualifiers[fali.typeName()]
		i:IDENT  {	fali.setName(i.getText());	}
		(TOK_COLON formalArgTypeName[fali.typeName()])?
		|
			abstractGenericTypeName_xx[fali.typeName()]
		)
	;
constant
	:	NUM_INT
	|	CHAR_LITERAL
	|	STRING_LITERAL
	|	NUM_FLOAT
	;

//----------------------------------------------------------------------------
// The Java scanner
//----------------------------------------------------------------------------
class ElijjahLexer extends Lexer;

options {
	exportVocab=Elijjah;      // call the vocabulary "Java"
	testLiterals=false;    // don't automatically test for literals
	k=4;                   // four characters of lookahead
//	charVocabulary='\u0003'..'\uFFFF';
	interactive=true;
}



// OPERATORS
QUESTION		:	'?'		;
LPAREN			:	'('		;
RPAREN			:	')'		;
LBRACK			:	'['		;
RBRACK			:	']'		;
LCURLY			:	'{'		;
RCURLY			:	'}'		;
COLON			:	':'		;
COMMA			:	','		;
//DOT			:	'.'		;
ASSIGN			:	'='		;
EQUAL			:	"=="	;
LNOT			:	'!'		;
BNOT			:	'~'		;
NOT_EQUAL		:	"!="	;
DIV				:	'/'		;
DIV_ASSIGN		:	"/="	;
PLUS			:	'+'		;
PLUS_ASSIGN		:	"+="	;
INC				:	"++"	;
MINUS			:	'-'		;
MINUS_ASSIGN	:	"-="	;
DEC				:	"--"	;
STAR			:	'*'		;
STAR_ASSIGN		:	"*="	;
MOD				:	'%'		;
MOD_ASSIGN		:	"%="	;
SR				:	">>"	;
SR_ASSIGN		:	">>="	;
BSR				:	">>>"	;
BSR_ASSIGN		:	">>>="	;
GE				:	">="	;
GT				:	">"		;
SL				:	"<<"	;
SL_ASSIGN		:	"<<="	;
LE				:	"<="	;
LT				:	'<'		;
BXOR			:	'^'		;
BXOR_ASSIGN		:	"^="	;
BOR				:	'|'		;
BOR_ASSIGN		:	"|="	;
LOR				:	"||"	;
BAND			:	'&'		;
BAND_ASSIGN		:	"&="	;
LAND			:	"&&"	;
SEMI			:	';'		;


// Whitespace -- ignored
WS	:	(	' '
		|	'\t'
		|	'\f'
		// handle newlines
		|	(	"\r\n"  // Evil DOS
			|	'\r'    // Macintosh
			|	'\n'    // Unix (the right way)
			)
			{ newline(); }
		)
		{ _ttype = Token.SKIP; }
	;

// Single-line comments
SL_COMMENT
	:	"//"
		(~('\n'|'\r'))* ('\n'|'\r'('\n')?)
		{$setType(Token.SKIP); newline();}
	;

// multiple-line comments
ML_COMMENT
	:	"/*"
		(	/*	'\r' '\n' can be matched in one alternative or by matching
				'\r' in one iteration and '\n' in another.  I am trying to
				handle any flavor of newline that comes in, but the language
				that allows both "\r\n" and "\r" and "\n" to all be valid
				newline is ambiguous.  Consequently, the resulting grammar
				must be ambiguous.  I'm shutting this warning off.
			 */
			options {
				generateAmbigWarnings=false;
			}
		:
			{ LA(2)!='/' }? '*'
		|	'\r' '\n'		{newline();}
		|	'\r'			{newline();}
		|	'\n'			{newline();}
		|	~('*'|'\n'|'\r')
		)*
		"*/"
		{$setType(Token.SKIP);}
	;


// character literals
CHAR_LITERAL
	:	'\'' ( ESC | ~'\'' ) '\''
	;

// string literals
STRING_LITERAL
	:	'"' (ESC|~('"'|'\\'))* '"'
	;


// escape sequence -- note that this is protected; it can only be called
//   from another lexer rule -- it will not ever directly return a token to
//   the parser
// There are various ambiguities hushed in this rule.  The optional
// '0'...'9' digit matches should be matched here rather than letting
// them go back to STRING_LITERAL to be matched.  ANTLR does the
// right thing by matching immediately; hence, it's ok to shut off
// the FOLLOW ambig warnings.
protected
ESC
	:	'\\'
		(	'n'
		|	'r'
		|	't'
		|	'b'
		|	'f'
		|	'"'
		|	'\''
		|	'\\'
		|	('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
		|	('0'..'3')
			(
				options {
					warnWhenFollowAmbig = false;
				}
			:	('0'..'7')
				(
					options {
						warnWhenFollowAmbig = false;
					}
				:	'0'..'7'
				)?
			)?
		|	('4'..'7')
			(
				options {
					warnWhenFollowAmbig = false;
				}
			:	('0'..'9')
			)?
		)
	;


// hexadecimal digit (again, note it's protected!)
protected
HEX_DIGIT
	:	('0'..'9'|'A'..'F'|'a'..'f')
	;


// a dummy rule to force vocabulary to be all characters (except special
//   ones that ANTLR uses internally (0 to 2)
protected
VOCAB
	:	'\3'..'\377'
	;


// an identifier.  Note that testLiterals is set to true!  This means
// that after we match the rule, we look in the literals table to see
// if it's a literal or really an identifer
IDENT
	options {testLiterals=true;}
	:	('a'..'z'|'A'..'Z'|'_'|'$') ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'$')*
	;


// a numeric literal
NUM_INT
	{boolean isDecimal=false;}
	:	'.' {_ttype = DOT;}
			(('0'..'9')+ (EXPONENT)? (FLOAT_SUFFIX)? { _ttype = NUM_FLOAT; })?
	|	(	'0' {isDecimal = true;} // special case for just '0'
			(	('x'|'X')
				(											// hex
					// the 'e'|'E' and float suffix stuff look
					// like hex digits, hence the (...)+ doesn't
					// know when to stop: ambig.  ANTLR resolves
					// it correctly by matching immediately.  It
					// is therefor ok to hush warning.
					options {
						warnWhenFollowAmbig=false;
					}
				:	HEX_DIGIT
				)+
			|	('0'..'7')+									// octal
			)?
		|	('1'..'9') ('0'..'9')*  {isDecimal=true;}		// non-zero decimal
		)
		(	('l'|'L')

		// only check to see if it's a float if looks like decimal so far
		|	{isDecimal}?
			(	'.' ('0'..'9')* (EXPONENT)? (FLOAT_SUFFIX)?
			|	EXPONENT (FLOAT_SUFFIX)?
			|	FLOAT_SUFFIX
			)
			{ _ttype = NUM_FLOAT; }
		)?
	;


// a couple protected methods to assist in matching floating point numbers
protected
EXPONENT
	:	('e'|'E') ('+'|'-')? ('0'..'9')+
	;


protected
FLOAT_SUFFIX
	:	'f'|'F'|'d'|'D'
	;

