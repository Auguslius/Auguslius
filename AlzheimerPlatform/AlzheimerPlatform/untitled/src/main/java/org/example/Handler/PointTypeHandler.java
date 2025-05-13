package org.example.Handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.sql.*;

public class PointTypeHandler extends BaseTypeHandler<Point> {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Point parameter, JdbcType jdbcType) throws SQLException {
        // 将 Point 转换为 WKT 格式 (Well-Known Text)
        String wkt = String.format("POINT(%f %f)", parameter.getX(), parameter.getY());
        ps.setString(i, wkt);
    }

    @Override
    public Point getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String wkt = rs.getString(columnName);
        return parseWktToPoint(wkt);
    }

    @Override
    public Point getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String wkt = rs.getString(columnIndex);
        return parseWktToPoint(wkt);
    }

    @Override
    public Point getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String wkt = cs.getString(columnIndex);
        return parseWktToPoint(wkt);
    }

    private Point parseWktToPoint(String wkt) {
        if (wkt == null || wkt.isEmpty()) {
            return null;
        }
        String[] parts = wkt.replace("POINT(", "").replace(")", "").split(" ");
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        return GEOMETRY_FACTORY.createPoint(new Coordinate(x, y));
    }
}
