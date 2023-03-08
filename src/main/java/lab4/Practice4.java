package lab4;

import java.io.IOException;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Practice4 {
    public static class City
    {
        private String name;
        private String state;
        private int population;

        public City(String name, String state, int population)
        {
            this.name = name;
            this.state = state;
            this.population = population;
        }

        public String getName()
        {
            return name;
        }

        public String getState()
        {
            return state;
        }

        public int getPopulation()
        {
            return population;
        }

        public String toString(){
            return "City{name='"+name+"', state='"+state+"', population="+population+"}";
        }

    }

    public static Stream<City> readCities(String filename) throws IOException
    {
        return Files.lines(Paths.get(filename))
                .map(l -> l.split(", "))
                .map(a -> new City(a[0], a[1], Integer.parseInt(a[2])));
    }

    public static void main(String[] args) throws IOException, URISyntaxException {

        Stream<City> cities = readCities("src/cities.txt");
        // Q1: count how many cities there are for each state
        // TODO: Map<String, Long> cityCountPerState = ...
        System.out.println(cities.collect(Collectors.groupingBy(City::getState, Collectors.counting())));


        cities = readCities("src/cities.txt");
        // Q2: count the total population for each state
        // TODO: Map<String, Integer> statePopulation = ...
        System.out.println(cities.collect(Collectors.groupingBy(City::getState, Collectors.summingInt(City::getPopulation))));


        cities = readCities("src/cities.txt");
        // Q3: for each state, get the set of cities with >500,000 population
        // TODO: Map<String, Set<City>> largeCitiesByState = ...
        cities
            .collect(Collectors.groupingBy(City::getState, Collectors.toSet()))
            .forEach((t, u) -> System.out.println(t+":"+u.stream().filter(city -> city.getPopulation() > 500000).toList()));

    }
}
