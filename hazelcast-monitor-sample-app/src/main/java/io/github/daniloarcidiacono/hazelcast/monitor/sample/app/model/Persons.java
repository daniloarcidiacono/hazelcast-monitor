package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model;

import java.util.*;

/**
 * Stores some randomly initialized {@link Person} instances.
 */
public abstract class Persons {
    private static final Random random = new Random();

    /**
     * Returns a collection of unique random persons.
     * @param size the desidered size. If more data than available is requested,
     *             the result size will be less than requested.
     * @return a collection of random persons.
     */
    public static Collection<Person> random(final int size) {
        if (size == 1) {
            return Collections.singletonList(random());
        }

        // pool contains the unused index of Persons in DATA
        final int[] pool = new int[DATA.length];
        int poolSize = DATA.length;
        for (int i = 0; i < DATA.length; i++) {
            pool[i] = i;
        }

        final List<Person> result = new ArrayList<>();
        while (result.size() < size && poolSize > 0) {
            // Pick a random index in pool
            final int poolIndex = random.nextInt(poolSize);

            // Pick the index in DATA
            result.add(DATA[pool[poolIndex]]);

            // Remove the chosen index from pool
            for (int j = poolIndex; j + 1 < poolSize; j++) {
                pool[j] = pool[j + 1];
            }
            poolSize--;

            // Clean
            pool[poolSize] = -1;
        }

        return result;
    }

    /**
     * Returns a random {@link Person} instance.
     * @return a random instance.
     */
    public static Person random() {
        return DATA[random.nextInt(DATA.length)];
    }

    /**
     * Randomly generated persons data (with faker.js)
     */
    public static final Person[] DATA = new Person[] {
        new Person().setName("Meredith").setSurname("Kuhn").contact("Winnifred23@example.org"),
        new Person().setName("Marcelo").setSurname("Satterfield").contact("(632) 930-2918 x156").contact("Valerie26@example.net").contact("Connie_Zboncak@example.com"),
        new Person().setName("Felipa").setSurname("Beatty").contact("Dylan6@example.org").contact("Effie_Moen@example.org"),
        new Person().setName("Caleb").setSurname("Dare").setAddress(new Address().setStreet("8257 Mary Rest").setCity("Amberstad").setZipCode("40098")),
        new Person().setName("Marge").setSurname("Ruecker").contact("134-510-7815 x13913").contact("Karina_Wolf@example.net").contact("806-752-8920 x37536").setAddress(new Address().setStreet("2474 Satterfield Village").setCity("New Katarinachester").setZipCode("29888")),
        new Person().setName("Emilie").setSurname("Dach").contact("062-332-4192 x224"),
        new Person().setName("Tyrel").setSurname("Herman").setAddress(new Address().setStreet("60268 Delpha Plaza").setCity("Port Paxtonshire").setZipCode("15173-5547")),
        new Person().setName("Elva").setSurname("Beier").contact("Alessandro.Gutkowski@example.com").contact("Wilford48@example.com"),
        new Person().setName("Paige").setSurname("Greenfelder").contact("861-262-1555").contact("920-603-0196 x57647").contact("Trudie27@example.org"),
        new Person().setName("Marcia").setSurname("Sawayn").contact("(287) 152-0997").contact("1-036-571-1719 x22878"),
        new Person().setName("Jena").setSurname("Schoen").contact("1-425-471-5202 x268"),
        new Person().setName("Beulah").setSurname("Harvey").setAddress(new Address().setStreet("36327 Madilyn Trail").setCity("Stanfordton").setZipCode("94857-6243")),
        new Person().setName("Eunice").setSurname("Strosin").contact("Icie.Abbott@example.net").contact("Sven.Tremblay84@example.com").setAddress(new Address().setStreet("242 Johnathan Land").setCity("Port Josie").setZipCode("96575")),
        new Person().setName("Albertha").setSurname("Hackett"),
        new Person().setName("Eloise").setSurname("Schmeler").contact("(472) 355-2743 x693").contact("1-003-327-2256").contact("Susie_Raynor58@example.com"),
        new Person().setName("Yvette").setSurname("Brakus").contact("1-009-837-9436 x8248").contact("Annabell.Bergstrom17@example.net"),
        new Person().setName("Margarete").setSurname("Abernathy"),
        new Person().setName("Jonatan").setSurname("Klocko").contact("672.687.4204").contact("(356) 440-5573 x33315").setAddress(new Address().setStreet("49232 Hermann Light").setCity("Lake Jody").setZipCode("16683-9873")),
        new Person().setName("Monte").setSurname("Schmitt").contact("Ole.Hand@example.net").contact("1-884-217-1873 x49473").contact("Shea_Jacobi@example.org"),
        new Person().setName("Anna").setSurname("Batz").setAddress(new Address().setStreet("9551 Wyman Drives").setCity("East Kristinaport").setZipCode("57242-8402")),
        new Person().setName("Jenifer").setSurname("Durgan").contact("Rosalee_Ledner@example.net").contact("(137) 307-3897 x85268").contact("(467) 393-5823 x10039").setAddress(new Address().setStreet("69006 Forest Freeway").setCity("West Kayley").setZipCode("65090-6593")),
        new Person().setName("Karl").setSurname("Kozey").contact("425.198.2713").contact("Jayson.Spencer62@example.net"),
        new Person().setName("Adelle").setSurname("Rice").contact("Monte34@example.org"),
        new Person().setName("Elody").setSurname("O'Keefe").contact("Narciso.Fritsch90@example.net").setAddress(new Address().setStreet("202 Loren Estates").setCity("Hardyfort").setZipCode("19384-9660")),
        new Person().setName("Judge").setSurname("Breitenberg").setAddress(new Address().setStreet("25418 Bianka Parkways").setCity("Russelmouth").setZipCode("96274")),
        new Person().setName("Alvis").setSurname("Braun").contact("1-719-307-6308").setAddress(new Address().setStreet("76644 Gislason Mission").setCity("South Madison").setZipCode("57754-0041")),
        new Person().setName("Gertrude").setSurname("Reichel").contact("(479) 712-9972 x303"),
        new Person().setName("Earnestine").setSurname("Kunde").contact("1-617-018-7537 x28223").contact("Gideon_Bosco91@example.net").contact("Lauren_Olson@example.net"),
        new Person().setName("Eli").setSurname("Rodriguez").contact("104-695-0395 x83223").contact("(591) 847-4672"),
        new Person().setName("Lavern").setSurname("Schuster"),
        new Person().setName("William").setSurname("Bogan").contact("Sean.Donnelly@example.org").contact("208.215.2775").contact("(426) 719-5610"),
        new Person().setName("Sebastian").setSurname("Terry").contact("191-625-1682 x97535").contact("1-173-425-5056 x928").contact("Thea.Witting@example.com"),
        new Person().setName("Devan").setSurname("Harris").contact("962-345-2699 x5697"),
        new Person().setName("Emerson").setSurname("Grimes").contact("839.683.9821").contact("459.467.8892"),
        new Person().setName("Antone").setSurname("Rolfson").contact("469-605-5645 x50515"),
        new Person().setName("River").setSurname("Emard").contact("Florence44@example.net"),
        new Person().setName("Antonina").setSurname("Fahey").setAddress(new Address().setStreet("5136 Horace Burg").setCity("Eliseochester").setZipCode("94323-8577")),
        new Person().setName("Sonny").setSurname("Rempel"),
        new Person().setName("Dana").setSurname("Terry"),
        new Person().setName("Luigi").setSurname("Friesen").contact("Jerald91@example.com").contact("(194) 490-0208 x12415").contact("1-060-006-5104"),
        new Person().setName("Tre").setSurname("Crona").contact("Cloyd80@example.org"),
        new Person().setName("William").setSurname("Wolf"),
        new Person().setName("Demario").setSurname("Kemmer").setAddress(new Address().setStreet("64121 Weldon Junctions").setCity("West Maeganburgh").setZipCode("61591-8904")),
        new Person().setName("Bart").setSurname("Hudson").contact("994-196-7101").contact("Christian.Beier@example.org").contact("(516) 491-8759 x252"),
        new Person().setName("Elenora").setSurname("Stanton").contact("166.088.2075 x11218").contact("(814) 849-6970 x6562").contact("888-363-1436 x894"),
        new Person().setName("Pascale").setSurname("Mann").contact("Estrella_Nitzsche22@example.org").contact("356-076-9171 x9202").setAddress(new Address().setStreet("6342 Rodrigo Hollow").setCity("Abshireside").setZipCode("53310")),
        new Person().setName("Telly").setSurname("Yundt").contact("357.208.2959"),
        new Person().setName("Frances").setSurname("Langosh").contact("1-976-876-6604"),
        new Person().setName("Alivia").setSurname("Hermiston").setAddress(new Address().setStreet("664 Tatum Track").setCity("Hesselmouth").setZipCode("73308")),
        new Person().setName("Tremayne").setSurname("Ruecker").contact("837.340.5925")
    };
}
