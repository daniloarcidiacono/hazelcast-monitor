package it.xdnl.hazelcast.monitor.agent.script;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ScriptEngineTest {
    static class Base {
        private int age;

        public Base(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    static class Person extends Base {
        private String name;

        public Person(final String name, final int age) {
            super(age);
            this.name = name;
        }

        public void evil() {
            System.exit(127);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    public void testScript() {
        final ScriptEngine.CompiledScript compiledScript = ScriptEngine.getInstance().createScript("const MIN = 100; const MAX = 250; person.age >= MIN && person.age <= MAX");
        final ScriptEngine.CompiledScript otherScript = ScriptEngine.getInstance().createScript("const MIN = 251; const MAX = 500; person.age >= MIN && person.age <= MAX");

        // Init input dat
        final List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            final Person person = new Person("name" + i, i);
            persons.add(person);
        }

        // Filter the data using plain old Java
        final long startJavaTime = System.nanoTime();
        final List<Person> javaFiltered = persons.stream().filter(person -> person.getAge() >= 100 && person.getAge() <= 500).collect(Collectors.toList());
        final long endJavaTime = System.nanoTime();
        final long javaDuration = (endJavaTime - startJavaTime) / 1000L;
        System.out.println("Java sequential filtering took: " + javaDuration + " milliseconds.");

        // Filter the data using scripts
        final long startScriptTime = System.nanoTime();
        final List<Person> scriptFiltered = persons.parallelStream().filter(person -> {
            final Map<String, Object> scopeVariables = new HashMap<>();
            scopeVariables.put("person", person);
            return compiledScript.<Boolean>exec(scopeVariables) || otherScript.<Boolean>exec(scopeVariables);
        }).collect(Collectors.toList());
        final long endScriptTime = System.nanoTime();
        final long scriptDuration = (endScriptTime - startScriptTime) / 1000L;
        System.out.println("Script parallel filtering took: " + scriptDuration + " milliseconds.");

        // Check that Java and Script filtering match
        assertEquals("Works", javaFiltered, scriptFiltered);
    }
}