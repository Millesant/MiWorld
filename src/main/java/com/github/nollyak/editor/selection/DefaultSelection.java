package com.github.nollyak.editor.selection;

import cn.nukkit.math.Vector3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultSelection implements Selection {

    private Vector3 firstPoint;

    private Vector3 secondPoint;

    @Override
    public boolean hasFirstPoint() {
        return Objects.nonNull(this.getFirstPoint());
    }

    @Override
    public boolean hasSecondPoint() {
        return Objects.nonNull(this.getSecondPoint());
    }

    @Override
    public void setFirstPoint(
        final Vector3 firstPoint
    ) {
        this.firstPoint = firstPoint;
    }

    @Override
    public void setSecondPoint(
        final Vector3 secondPoint
    ) {
        this.secondPoint = secondPoint;
    }

    @Override
    public Vector3 getMin() {
        return new Vector3(
            Math.min(this.getFirstPoint().getX(), this.getSecondPoint().getX()),
            Math.min(this.getFirstPoint().getY(), this.getSecondPoint().getY()),
            Math.min(this.getFirstPoint().getZ(), this.getSecondPoint().getZ())
        );
    }

    @Override
    public Vector3 getMax() {
        return new Vector3(
            Math.max(this.getFirstPoint().getX(), this.getSecondPoint().getX()),
            Math.max(this.getFirstPoint().getY(), this.getSecondPoint().getY()),
            Math.max(this.getFirstPoint().getZ(), this.getSecondPoint().getZ())
        );
    }

    @Override
    public double getVolume() {
        return Math.abs(this.getFirstPoint().getX() - this.getSecondPoint().getX()) * Math.abs(this.getFirstPoint().getY() - this.getSecondPoint().getY()) * Math.abs(this.getFirstPoint().getZ() - this.getSecondPoint().getZ());
    }

}
