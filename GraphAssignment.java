import java.util.*;

class Road {
    public String city1;
    public String city2;
    public int distance;

    public Road(String city1, String city2, int distance) {
        this.city1 = city1;
        this.city2 = city2;
        this.distance = distance;
    }
}
class RoadMap {
    Map<String, Set<Road>> roadMap = new HashMap<String, Set<Road>>();

    public Set<String> getAllCities() {
        return this.roadMap.keySet();
    }

    public void readLine(String line) {
        String[] csv = line.split(",");
        String city1 = csv[0];
        String city2 = csv[1];
        int distance = Integer.parseInt(csv[2]);
        addRoad(city1, city2, distance);
    }

    private void addCity(String city) {
        this.roadMap.put(city, new HashSet<Road>());
    }

    private void addRoad(String city1, String city2, int distance) {
        Road road1 = new Road(city1, city2, distance);
        Road road2 = new Road(city2, city1, distance);
        if (!this.roadMap.containsKey(city1)) {
            addCity(city1);
        }
        if (!this.roadMap.containsKey(city2)) {
            addCity(city2);
        }
        this.roadMap.get(city1).add(road1);
        this.roadMap.get(city2).add(road2);
    }

    public Set<Road> getAllOutgoingRoads(String node) {
        return this.roadMap.get(node);
    }
}

public class GraphAssignment {
    static RoadMap roadMap = new RoadMap();

    public static void readMap(Scanner scanner) {
        while (true) {
            String mapLine = scanner.nextLine();
            if (mapLine.equals("")) {
                break;
            }
            roadMap.readLine(mapLine);
        }
        System.out.println("Read map");
    }
    public static void findShortestPath(String source, String destination) {
        Map<String, Integer> distance = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));

        for (String city : roadMap.getAllCities()) {
            distance.put(city, Integer.MAX_VALUE);
            previous.put(city, null);
        }

        distance.put(source, 0);
        queue.add(source);

        while (!queue.isEmpty()) {
            String currentCity = queue.poll();

            if (currentCity.equals(destination)) {
                break;
            }

            if (visited.contains(currentCity)) {
                continue;
            }

            visited.add(currentCity);

            for (Road road : roadMap.getAllOutgoingRoads(currentCity)) {
                String neighborCity = road.city2;
                int alt = distance.get(currentCity) + road.distance;

                if (alt < distance.get(neighborCity)) {
                    distance.put(neighborCity, alt);
                    previous.put(neighborCity, currentCity);
                    queue.add(neighborCity);
                }
            }
        }

        if (previous.get(destination) == null) {
            System.out.println("No route found");
        } else {
            String currentCity = destination;
            LinkedList<String> path = new LinkedList<>();
            while (currentCity != null) {
                path.addFirst(currentCity);
                currentCity = previous.get(currentCity);
            }
            System.out.println(String.join(" -> ", path));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        readMap(scanner);
        findShortestPath("City1", "City9");
    }
}
