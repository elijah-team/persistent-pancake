package tripleo.elijah.persistent_pancake

import spock.lang.Specification

class ListClassesExampleSpec extends Specification {
	def "testListClasses"() {
		final File projectDir = new File("test")
		new ListClassesExample().listClasses(projectDir)

		expect: true == true
	}
}
