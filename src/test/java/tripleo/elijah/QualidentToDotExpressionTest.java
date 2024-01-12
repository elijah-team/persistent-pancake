package tripleo.elijah;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.util.Helpers;

public class QualidentToDotExpressionTest {

	@Test
	void qualidentToDotExpression2() {
        final Qualident q = new Qualident();
        q.append(tripleo.elijah.util.Helpers.string_to_ident("a"));
        q.append(tripleo.elijah.util.Helpers.string_to_ident("b"));
        q.append(tripleo.elijah.util.Helpers.string_to_ident("c"));
        final IExpression e = Helpers.qualidentToDotExpression2(q);
        System.out.println(e);
        Assertions.assertEquals("a.b.c", e.toString());
    }
}