import spock.lang.Shared
import spock.lang.Specification
import tripleo.elijah.comp.Compilation
import tripleo.elijah.factory.comp.CompilationFactory
import tripleo.elijah_durable_pancake.comp.impl.IO_
import tripleo.elijah_durable_pancake.comp.impl.StdErrSink

class ClassInstantiationSpec extends Specification {
	def "errorCount"() {
		expect:
		errorCount(b) == c

		where:
		b                                   || c
		"test/basic1/class_instantiation/"  || 9
		"test/basic1/class_instantiation2/" || 5
		"test/basic1/class_instantiation3/" || 8
	}

	def "codeOutputSize"() {
		expect:
		codeOutputSize(b) == c

		where:
		b                                   || c
		"test/basic1/class_instantiation/"  || 6
		"test/basic1/class_instantiation2/" || 6
		"test/basic1/class_instantiation3/" || 6
	}

	def "file #a containsCodeOutput #b == #c"() {
		expect:
		containsCodeOutput2_(a, b) == c

		where:
		a                                   | b                                || c
		"test/basic1/class_instantiation3/" | "class_instantiation3/Foo.h"     || true
		"test/basic1/class_instantiation3/" | "class_instantiation3/Foo.c"     || true
		"test/basic1/class_instantiation3/" | "class_instantiation3/Main.h"    || true
		"test/basic1/class_instantiation3/" | "class_instantiation3/Main.c"    || true
		"test/basic1/class_instantiation3/" | "class_instantiation3/Bar_103.c" || true
		"test/basic1/class_instantiation3/" | "class_instantiation3/Bar_103.h" || true
		"test/basic1/class_instantiation2/" | "class_instantiation2/Foo.h"     || true
		"test/basic1/class_instantiation2/" | "class_instantiation2/Foo.c"     || true
		"test/basic1/class_instantiation2/" | "class_instantiation2/Main.h"    || true
		"test/basic1/class_instantiation2/" | "class_instantiation2/Main.c"    || true
		"test/basic1/class_instantiation2/" | "class_instantiation2/Bar_103.c" || true
		"test/basic1/class_instantiation2/" | "class_instantiation2/Bar_103.h" || true
	}

	def "containsCodeOutput"() {
		expect:
		containsCodeOutput(b) == c

		where:
		b                               || c
		"class_instantiation/Bar_103.c" || true
		"class_instantiation/Bar_103.c" || true
		"class_instantiation/Foo.h"     || true
		"class_instantiation/Main.h"    || true
		"class_instantiation/Foo.c"     || true
		"class_instantiation/Main.c"    || true
		"class_instantiation/Bar_105.c" || false
		"prelude/Prelude/IPrintable.h"  || false
		"class_instantiation/Bar_102.c" || false
		"class_instantiation/Bar_105.c" || false
		"prelude/Prelude/IPrintable.h"  || false
		"class_instantiation/Bar_102.c" || false
		"prelude/Prelude/IPrintable.c"  || false
		"prelude/Prelude/Prelude.h"     || false // --
		"prelude/Prelude/Prelude.c"     || false // --
		"prelude/Prelude/ConstString.c" || false
		"class_instantiation/Bar_110.h" || false
		"class_instantiation/Bar_110.c" || false
		"class_instantiation/Bar_107.h" || false
		"class_instantiation/Bar_108.h" || false
		"class_instantiation/Bar_105.h" || false
		"class_instantiation/Bar_106.h" || false
		"class_instantiation/Bar_108.c" || false
		"class_instantiation/Bar_109.c" || false
		"class_instantiation/Bar_106.c" || false
		"class_instantiation/Bar_102.h" || false
		"class_instantiation/Bar_107.c" || false
		"prelude/Prelude/ConstString.h" || false
		"class_instantiation/Bar_109.h" || false
	}

//    def "maximum of two numbers"() {
//        expect:
//        Math.max(a, b) == c
//
//        where:
//        a << [3, 5, 9]
//        b << [7, 4, 9]
//        c << [7, 5, 9]
//    }
//
//    def "minimum of #a and #b is #c"() {
//        expect:
//        Math.min(a, b) == c
//
//        where:
//        a | b || c
//        3 | 7 || 3
//        5 | 4 || 4
//        9 | 9 || 9
//    }
//
//    def "#person.name is a #sex.toLowerCase() person"() {
//        expect:
//        person.getSex() == sex
//
//        where:
//        person                    || sex
//        new Person(name: "Fred")  || "Male"
//        new Person(name: "Wilma") || "Female"
//    }

	def setupSpec() {
		def f = "test/basic1/class_instantiation/"

		def cc = CompilationFactory.mkCompilation(new StdErrSink(), new IO_())
		cc.feedCmdLine([f])

		this.cc = cc
	}

	boolean containsCodeOutput(String c) {
		cc.reports().containsCodeOutput c
	}

	@Shared
	Compilation cc = null

//    static class Person {
//        String name
//
//        String getSex() {
//            name == "Fred" ? "Male" : "Female"
//        }
//    }
	int codeOutputSize(String x) {
		return cf(x).reports().codeOutputSize()
	}

	int errorCount(String x) {
		return cf(x).errorCount()
	}

	@Shared
	Map m = new HashMap()

	Compilation cf(String rootCI) {
		if (!m.containsKey(rootCI)) {
			def cc = CompilationFactory.mkCompilation(new StdErrSink(), new IO_())
			cc.feedCmdLine([rootCI])
			m.put(rootCI, cc)
		}
		m[rootCI]
	}

	boolean containsCodeOutput2_(String f, String f2) {
		cf(f).reports().containsCodeOutput(f2)
	}
}
