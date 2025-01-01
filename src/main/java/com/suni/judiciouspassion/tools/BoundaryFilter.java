package com.suni.judiciouspassion.tools;

import com.suni.judiciouspassion.entity.taste.Taste;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoundaryFilter {

    private static final double EARTH_RADIUS_KM = 6378.137;

    private final ModelMapper modelMapper;

    @Autowired
    public BoundaryFilter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * 반경을 인수로 받아, 이를 통해 중심 좌표에서 해당 반경에 일치하는 바운딩 박스를 계산하는 로직입니다.
     * */
    public static double[] calculateBoundingBox(double centerLat, double centerLon, int boundaryKm) {
        double latDelta = (double) boundaryKm / EARTH_RADIUS_KM;
        double lonDelta = (double) boundaryKm / (EARTH_RADIUS_KM * Math.cos(Math.toRadians(centerLat)));

        double latMin = centerLat - Math.toDegrees(latDelta);
        double latMax = centerLat + Math.toDegrees(latDelta);
        double lonMin = centerLon - Math.toDegrees(lonDelta);
        double lonMax = centerLon + Math.toDegrees(lonDelta);

        return new double[]{latMin, latMax, lonMin, lonMax};
    }

    /**
     * 하버사인 공식을 통해 바운딩 박스 계산 내부의 좌표가 중심좌표 선상에서 얼마나 떨어져 있는지를 확인 하고 이를 검증합니다.
     *
     * 하버사인 공식
     * d = 2r * arcsin (sqrt ((lat2-lat1)/2) + cos (lat1) * cos (lat2) * sin² ((lon2-lon1)/2))
     * d는 두 점 사이의 거리이고 r은 구의 반지름이며, lat1, lat2, lon1, lon2 는 라디안으로 표시된 두 점의 위도와 경도 좌표입니다.
     * */
    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    public Taste getPlacesWithinRadius(Taste taste, double centerLat, double centerLon, int boundaryKm) {
        // 바운딩 박스 계산
        double[] boundingBox = calculateBoundingBox(centerLat, centerLon, boundaryKm);
        double latMin = boundingBox[0];
        double latMax = boundingBox[1];
        double lonMin = boundingBox[2];
        double lonMax = boundingBox[3];

        if (taste.getLatitude() >= latMin && taste.getLatitude() <= latMax &&
                taste.getLongitude() >= lonMin && taste.getLongitude() <= lonMax) {
            // 하버사인 공식으로 거리 확인
            double distance = haversineDistance(centerLat, centerLon, taste.getLatitude(), taste.getLongitude());
            if (distance <= boundaryKm) {

                return taste;
            }
        }
        return null;
    }
}
