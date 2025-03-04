package me.millesant.editor.selection;

import cn.nukkit.math.Vector3;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectionImpl implements Selection {

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
    public void setFirstPoint(final Vector3 firstPoint) {
        this.firstPoint = firstPoint;
    }

    @Override
    public void setSecondPoint(final Vector3 secondPoint) {
        this.secondPoint = secondPoint;
    }

    @Override
    public Vector3 getMin() {
        return new Vector3(Math.min(this.getFirstPoint().getX(), this.getSecondPoint().getX()), Math.min(this.getFirstPoint().getY(), this.getSecondPoint().getY()), Math.min(this.getFirstPoint().getZ(), this.getSecondPoint().getZ()));
    }

    @Override
    public Vector3 getMax() {
        return new Vector3(Math.max(this.getFirstPoint().getX(), this.getSecondPoint().getX()), Math.max(this.getFirstPoint().getY(), this.getSecondPoint().getY()), Math.max(this.getFirstPoint().getZ(), this.getSecondPoint().getZ()));
    }

    @Override
    public double getVolume() {
        return Math.abs(this.getFirstPoint().getX() - this.getSecondPoint().getX()) * Math.abs(this.getFirstPoint().getY() - this.getSecondPoint().getY()) * Math.abs(this.getFirstPoint().getZ() - this.getSecondPoint().getZ());
    }

    @Override
    public void rotate(final int angle) {
        final var min = this.getMin();
        final var max = this.getMax();

        final var centerX = (min.getX() + max.getX()) / 2;
        final var centerZ = (min.getZ() + max.getZ()) / 2;

        final var firstPointX = this.getFirstPoint().getX() - centerX;
        final var firstPointZ = this.getFirstPoint().getZ() - centerZ;

        final var secondPointX = this.getSecondPoint().getX() - centerX;
        final var secondPointZ = this.getSecondPoint().getZ() - centerZ;

        this.setFirstPoint(this.rotateVector(new Vector3(firstPointX, this.getFirstPoint().getY(), firstPointZ), angle, centerX, centerZ));
        this.setSecondPoint(this.rotateVector(new Vector3(secondPointX, this.getSecondPoint().getY(), secondPointZ), angle, centerX, centerZ));
    }

    private Vector3 rotateVector(final Vector3 vector, final int angle, final double centerX, final double centerZ) {
        return switch (angle) {
            case 90 -> new Vector3(centerX - vector.getZ(), vector.getY(), centerZ + vector.getX());
            case 180 -> new Vector3(centerX - vector.getX(), vector.getY(), centerZ - vector.getZ());
            case 270 -> new Vector3(centerX + vector.getZ(), vector.getY(), centerZ - vector.getX());
            default -> vector;
        };
    }

    @Override
    public void flip(final String direction) {
        final var min = this.getMin();
        final var max = this.getMax();

        final var firstPointX = this.getFirstPoint().getX();
        final var firstPointY = this.getFirstPoint().getY();
        final var firstPointZ = this.getFirstPoint().getZ();

        final var secondPointX = this.getSecondPoint().getX();
        final var secondPointY = this.getSecondPoint().getY();
        final var secondPointZ = this.getSecondPoint().getZ();

        switch (direction) {
            case "horizontal" -> {
                this.setFirstPoint(new Vector3(max.getX() - (firstPointX - min.getX()), firstPointY, firstPointZ));
                this.setSecondPoint(new Vector3(max.getX() - (secondPointX - min.getX()), secondPointY, secondPointZ));
            }
            case "vertical" -> {
                this.setFirstPoint(new Vector3(firstPointX, max.getY() - (firstPointY - min.getY()), firstPointZ));
                this.setSecondPoint(new Vector3(secondPointX, max.getY() - (secondPointY - min.getY()), secondPointZ));
            }
            default -> throw new IllegalArgumentException("Invalid flip direction");
        }
    }

}
