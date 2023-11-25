package tripleo.elijah.lang;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

//@RunWith(ArchUnitRunner.class) // Remove this line for JUnit 5!!
@AnalyzeClasses(packages = "tripleo.elijah")
public class Ci_ArchUnit_Test {

	//@Test
	@ArchTest
	public void langTest(JavaClasses importedClasses) {

		ArchRule rule = noClasses().that()
		                           .resideInAPackage("tripleo.elijah.ci")
		                           .should()
		                           .dependOnClassesThat()
		                           .resideInAPackage("tripleo.elijah.lang.*");
		ArchRule rule2 = noClasses().that()
		                           .resideInAPackage("tripleo.elijah.ci_impl")
		                           .should()
		                           .dependOnClassesThat()
		                           .resideInAPackage("tripleo.elijah.lang.*")
		  ;

		if (false)
		{
			final JavaClasses importedClasses1 = importedClasses;

			rule.check(importedClasses1);
			rule2.check(importedClasses1);
		}
	}
}
