package tripleo.elijah;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tripleo.eljiah_pancake_durable.lang.IExpression;
import tripleo.eljiah_pancake_durable.lang.Qualident;
import tripleo.eljiah_pancake_durable.util.Helpers;

public class QualidentToDotExpressionTest {

	@Test
	void qualidentToDotExpression2() {
        final Qualident q = new Qualident();
        q.append(Helpers.string_to_ident("a"));
        q.append(Helpers.string_to_ident("b"));
        q.append(Helpers.string_to_ident("c"));
        final IExpression e = Helpers.qualidentToDotExpression2(q);
        System.out.println(e);
        Assertions.assertEquals("a.b.c", e.toString());
    }
}