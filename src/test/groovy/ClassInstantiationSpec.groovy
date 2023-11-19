import spock.lang.Shared
import spock.lang.Specification
import tripleo.elijah.comp.IO
import tripleo.elijah.comp.StdErrSink
import tripleo.elijah.factory.comp.CompilationFactory

class ClassInstantiationSpec extends Specification {
    def "a"() {
        expect:
        containsCodeOutput(b) == c

        where:
        b                               || c
        "class_instantiation/Bar_105.c" || false
        "class_instantiation/Bar_103.c" || true
        "prelude/Prelude/IPrintable.h"  || false
        "class_instantiation/Bar_102.c" || false
        "class_instantiation/Bar_105.c" || false
        "prelude/Prelude/IPrintable.h"  || false
        "class_instantiation/Bar_102.c" || false
        "class_instantiation/Bar_103.c" || true
        "prelude/Prelude/IPrintable.c"  || false
        "prelude/Prelude/Prelude.h"     || false // --
        "class_instantiation/Foo.h"     || true
        "prelude/Prelude/Prelude.c"     || false // --
        "class_instantiation/Main.h"    || true
        "prelude/Prelude/ConstString.c" || false
        "class_instantiation/Foo.c"     || true
        "class_instantiation/Bar_110.h" || false
        "class_instantiation/Bar_110.c" || false
        "class_instantiation/Bar_107.h" || false
        "class_instantiation/Bar_108.h" || false
        "class_instantiation/Bar_105.h" || false
        "class_instantiation/Bar_106.h" || false
        "class_instantiation/Bar_103.h" || true
        "class_instantiation/Bar_108.c" || false
        "class_instantiation/Bar_109.c" || false
        "class_instantiation/Bar_106.c" || false
        "class_instantiation/Bar_102.h" || false
        "class_instantiation/Bar_107.c" || false
        "prelude/Prelude/ConstString.h" || false
        "class_instantiation/Main.c"    || true
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

        def cc = CompilationFactory.mkCompilation(new StdErrSink(), new IO())
        cc.feedCmdLine([f])

        this.cc = cc
    }

    boolean containsCodeOutput(String c) {
        cc.reports().containsCodeOutput c
    }

    @Shared cc = null

//    static class Person {
//        String name
//
//        String getSex() {
//            name == "Fred" ? "Male" : "Female"
//        }
//    }
}
