package it.xdnl.hazelcast.monitor.spring.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TestComponent {
    @Autowired
    private HazelcastInstance hazelcastInstance;

    static class ComplexKey implements Serializable {
        private String name;
        private long age;

        public ComplexKey() {
        }

        public ComplexKey(String name, long age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getAge() {
            return age;
        }

        public void setAge(long age) {
            this.age = age;
        }
    }

    static class ComplexValue implements Serializable {
        private List<Integer> stats = new ArrayList<>();

        public ComplexValue() {
        }

        public ComplexValue(int ...values) {
            for (int value : values) {
                stats.add(value);
            }
        }

        public List<Integer> getStats() {
            return stats;
        }

        public void setStats(List<Integer> stats) {
            this.stats = stats;
        }
    }
    private IMap<String, String> myMap;
    private IMap<ComplexKey, ComplexValue> myMap2;
    private IMap<ComplexKey, String> myMap3;

    @PostConstruct
    public void init() {
        myMap = hazelcastInstance.getMap("simple_map");
        myMap.put("myKey1", "myValue1");
        myMap.put("myKey2", "myValue2");

        myMap2 = hazelcastInstance.getMap("complex_map");
        myMap2.put(new ComplexKey("Danilo1", 28), new ComplexValue(1, 2, 3));
        myMap2.put(new ComplexKey("Mario1", 50), new ComplexValue(7, -1, 5));
        myMap2.put(new ComplexKey("Danilo2", 28), new ComplexValue(1, 2, 3));
        myMap2.put(new ComplexKey("Mario2", 50), new ComplexValue(7, -1, 5));
        myMap2.put(new ComplexKey("Danilo3", 28), new ComplexValue(1, 2, 3));
        myMap2.put(new ComplexKey("Mario3", 50), new ComplexValue(7, -1, 5));
        myMap2.put(new ComplexKey("Danilo4", 28), new ComplexValue(1, 2, 3));
        myMap2.put(new ComplexKey("Mario4", 50), new ComplexValue(7, -1, 5));
        myMap2.put(new ComplexKey("Danilo5", 28), new ComplexValue(1, 2, 3));
        myMap2.put(new ComplexKey("Mario5", 50), new ComplexValue(7, -1, 5));

        myMap3 = hazelcastInstance.getMap("intermediate_map");
        myMap3.put(new ComplexKey("Danilo", 28), "Arcidiacono");
        myMap3.put(new ComplexKey("Mario", 50), "Rhossi");
        myMap3.put(new ComplexKey("Danilo2", 28), "Arcidiacono");
        myMap3.put(new ComplexKey("Mari3o", 50), "Rhossi");
        myMap3.put(new ComplexKey("Danil4o", 28), "Arcidiacono");
        myMap3.put(new ComplexKey("Mari4o", 50), "Rhossi");
        myMap3.put(new ComplexKey("Dani5lo", 28), "Arcidiacono");
        myMap3.put(new ComplexKey("Mari6o", 50), "Rhossi");
        myMap3.put(new ComplexKey("Dani7lo", 28), "Arcidiacono");
        myMap3.put(new ComplexKey("Mari8o", 50), "Rhossi");
        myMap3.put(new ComplexKey("Dani9lo", 28), "Arcidiacono");
        myMap3.put(new ComplexKey("Mari10o", 50), "Rhossi");
    }
}
