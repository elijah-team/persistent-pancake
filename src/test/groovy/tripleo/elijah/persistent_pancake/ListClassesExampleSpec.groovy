package tripleo.elijah.persistent_pancake

import spock.lang.Specification
import tripleo.elijah_pancake.crib.ListClassesExample

import java.util.stream.Collectors

class ListClassesExampleSpec extends Specification {
	def "testListClasses"() {
		final File projectDir = new File("test")

		def example = new ListClassesExample()
		example.setQuiet()
		example.listClasses(projectDir)

		expect:
		true == true

		example.ts.stream().forEach {it1 -> System.err.println(quote(it1))}

//		where:
//		[a, b, c] << [example.ts]
	}

	def quote(s) { s.stream().map {return String.format("\"%s\"", it) }.collect(Collectors.toList())}
//	def quote(s) { s.map { String.format("\"%s\"", it) } } // ??
//	def quote2(s) { return String.format("\"%s\"", s) }
//	def quote(s) { [quote2(s[0]),quote2(s[1]),quote2(s[2])] }
}
