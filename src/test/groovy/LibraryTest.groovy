/*
 * This Spock specification was auto generated by running 'gradle init --type groovy-library'
 * by 'Webonise' at '3/11/15 3:37 PM' with Gradle 2.3
 *
 * @author Webonise, @date 3/11/15 3:37 PM
 */

import spock.lang.Specification

class LibraryTest extends Specification{
    def "someLibraryMethod returns true"() {
        setup:
        Library lib = new Library()
        when:
        def result = lib.someLibraryMethod()
        then:
        result == true
    }
}