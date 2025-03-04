package me.millesant.editor.selection;

import cn.nukkit.math.Vector3;

public interface Selection {

    Vector3 getFirstPoint();

    void setFirstPoint(final Vector3 firstPoint);

    Vector3 getSecondPoint();

    void setSecondPoint(final Vector3 secondPoint);

    boolean hasFirstPoint();

    boolean hasSecondPoint();

    Vector3 getMin();

    Vector3 getMax();

    double getVolume();

    void rotate(int angle);

    void flip(String direction);

}
