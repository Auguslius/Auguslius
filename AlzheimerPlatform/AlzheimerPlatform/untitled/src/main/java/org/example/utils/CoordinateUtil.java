package org.example.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class CoordinateUtil {
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    public static Point parseCoordinate(String coordinate) {
        if (coordinate == null || coordinate.isEmpty()) {
            return null;
        }
        String[] parts = coordinate.split(",");
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);

        return GEOMETRY_FACTORY.createPoint(new Coordinate(x, y));
    }
}
