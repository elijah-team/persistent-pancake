package tripleo.elijah.stages.expand;

import tripleo.elijah.lang.IExpression;

public class DotExpressionInstruction implements FunctionInstruction {
    private final IntroducedVariable variable;
    private final IExpression expr;

    public DotExpressionInstruction(IntroducedVariable i, IExpression de) {
        this.variable = i;
        this.expr     = de;
    }

    @Override
    public String toString() {
        return "DotExpressionInstruction{" +
                "variable=" + variable +
                ", dot_exp=" + expr +
                '}';
    }
}
