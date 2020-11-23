import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class FindDistance {
    public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        Map<String, Map> map = readCitiesJson();
        String input="BOM";
        FindDistance findDistance = new FindDistance();
        Map inputCity= map.get(input);
        Map inputLocation = (Map) inputCity.get("location");
        double inputLat=(Double) inputLocation.get("lat");
        double inputLon=(Double)inputLocation.get("lat");
        String inputConti=(String) inputCity.get("contId");
        System.out.println("map "+map.size());
        Map<String, Map> europeMap=getContinentHashMap("europe",map);
        System.out.println("europeMap.size(); "+europeMap.size());
        Map<String, Map> asiaMap=getContinentHashMap("asia",map);
        System.out.println("asiaMap.size(); "+europeMap.size());
        Map<String, Map> south_americaMap=getContinentHashMap("south-america",map);
        System.out.println("south_americaMap.size(); "+europeMap.size());
        Map<String, Map> north_americaMap=getContinentHashMap("north-america",map);
        System.out.println("north_americaMap.size(); "+europeMap.size());
        Map<String, Map> africaMap=getContinentHashMap("africa",map);
        System.out.println("africaMap.size(); "+europeMap.size());
        Map<String, Map> oceaniaMap=getContinentHashMap("oceania",map);
        System.out.println("oceaniaMap.size(); "+europeMap.size());

        List<Map<String, Map>> listOfContinentsToBeSearched= new ArrayList<>();
        String contient = inputConti;
        switch (contient) {
            case "asia":
                listOfContinentsToBeSearched.add(europeMap);
                listOfContinentsToBeSearched.add(south_americaMap);
                listOfContinentsToBeSearched.add(north_americaMap);
                listOfContinentsToBeSearched.add(africaMap);
                listOfContinentsToBeSearched.add(oceaniaMap);
                findDistance.submitMultipleCallablesWithExecutor(inputCity,listOfContinentsToBeSearched);

                break;

        }

    }

    private static Map getContinentHashMap(String inputContinent,Map<String, Map> map) throws IOException {
        Map<String, Map> continent=new HashMap<>();
        for (Map.Entry<String, Map> entry : map.entrySet()) {
            if(inputContinent.equals((String)entry.getValue().get("contId"))){
                continent.put(entry.getKey(),entry.getValue());
            }
        }
        return  continent;
    }


    private static Map getClosestContinent(Map inputCity,Map<String, Map> map) throws IOException {
        //System.out.println("Map "+map);
        int firstMin = 0;
        Map inputLocation = (Map) inputCity.get("location");
        double inputLat=(Double) inputLocation.get("lat");
        double inputLon=(Double)inputLocation.get("lat");
        String inputConti=(String) inputCity.get("contId");
        Map foundCity = new HashMap();
        for (Map.Entry<String, Map> entry : map.entrySet()) {

            //find nearest continent
            Map location2 = (Map) entry.getValue().get("location");
            double toLat2 = (Double) location2.get("lat");
            double toLon2 = (Double) location2.get("lat");
            String conti2 = (String) entry.getValue().get("contId");
            int distance = calculateDistanceInKilometer(inputLat, inputLon, toLat2, toLon2);
            if (firstMin == 0) {
                firstMin = distance;
                foundCity = entry.getValue();
            } else {
                if (distance < firstMin) {
                    firstMin = distance;
                    foundCity = null;
                    foundCity = entry.getValue();
                }
            }
        }
        return  foundCity;
    }


    public void submitMultipleCallablesWithExecutor(Map input,List<Map<String, Map>> listOfContinentsToBeSearched ) throws InterruptedException, ExecutionException, TimeoutException {

        ExecutorService executorService = null;

        try{
            executorService = Executors.newFixedThreadPool(6);

            Collection<Callable<Map>> callables = new ArrayList<>();
            listOfContinentsToBeSearched.forEach(i-> {
                callables.add(createCallable(input,i));
            });

            List<Future<Map>> taskFutureList = executorService.invokeAll(callables);


            for (Future<Map> future : taskFutureList) {

                Map value = future.get();
                System.out.println(String.format("TaskFuture returned value %s", value));
            }
        }
        finally{
            executorService.shutdown();
        }
    }


    private Callable<Map> createCallable(Map input,Map<String, Map> map){
        return new Callable<Map>() {
            public Map call() throws Exception {
                Map returnedValue = getClosestContinent(input,map);
                return returnedValue;
            }
        };
    }



    public static Map<String, Map> readCitiesJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //JSON file to Java object

        Map<String, Map> map = mapper.readValue(new File("/Users/amsys/Documents/distancetest/src/main/resources/cities.json"), Map.class);

    return map;

    }

    public static int calculateDistanceInKilometer(double userLat, double userLng,
                                            double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
    }



}
