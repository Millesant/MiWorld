package com.github.nollyak.editor.selection;

import cn.nukkit.math.Vector3;

public interface Selection {

    Vector3 getFirstPoint();

    Vector3 getSecondPoint();

    boolean hasFirstPoint();

    boolean hasSecondPoint();

    void setFirstPoint(
        final Vector3 firstPoint
    );

    void setSecondPoint(
        final Vector3 secondPoint
    );

    Vector3 getMin();

    Vector3 getMax();

    double getVolume();

    void rotate(int angle);

    void flip(String direction);

}
