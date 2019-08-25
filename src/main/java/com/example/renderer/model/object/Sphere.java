package com.example.renderer.model.object;

import com.example.renderer.model.Material;
import com.example.renderer.model.RayHit;
import com.example.renderer.service.custom.RadiusControlFactory;
import javafx.geometry.Point3D;
import lombok.AllArgsConstructor;
import lombok.Data;

import static java.lang.Math.*;

@Data
@AllArgsConstructor
public class Sphere implements Renderable {

    @UIParameter
    private Point3D center;
    @UIParameter(controlFactory = RadiusControlFactory.class)
    private double radius;
    private Material material;

    @Override
    public RayHit intersection(Point3D origin, Point3D direction) {
        Point3D originToCenter = center.subtract(origin);
        double projection = originToCenter.dotProduct(direction);
        if (projection < 0) {
            return RayHit.MISS;
        }

        double centerToProjection = sqrt(originToCenter.dotProduct(originToCenter) - projection * projection);
        if (centerToProjection > radius) {
            return RayHit.MISS;
        }

        double intersection = sqrt(radius * radius - centerToProjection * centerToProjection);
        double originToIntersection0 = projection - intersection;
        double originToIntersection1 = projection + intersection;

        if (originToIntersection0 < 0) {
            // Camera is inside the sphere
            //originToIntersection0 = originToIntersection1;
            return RayHit.MISS;
        }

        return new RayHit(
                true,
                this,
                originToIntersection0,
                origin.add(direction.multiply(originToIntersection0)),
                origin.add(direction.multiply(originToIntersection0)).subtract(center).normalize());
    }
}
