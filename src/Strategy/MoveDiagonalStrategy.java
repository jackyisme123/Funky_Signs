package Strategy;

import java.awt.*;

public class MoveDiagonalStrategy implements MoveStrategy {
    @Override
    public Point move(Point deltaXY) {
        return deltaXY;
    }
}
