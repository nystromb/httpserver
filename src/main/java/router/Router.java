package router;

import java.util.ArrayList;
import java.util.List;

public class Router {
    private static List<Route> routes = new ArrayList<>();

    public static void addRoute(Route route) {
        routes.add(route);
    }

    public static Route getRoute(String path) {
        for(Route route : routes){
            if(route.match(path))
                return route;
        }

        return null;
    }

    public static void clearAll() {
        routes.clear();
    }
}
