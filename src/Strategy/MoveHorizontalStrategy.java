package Strategy;

import java.awt.*;

public class MoveHorizontalStrategy implements MoveStrategy {
    @Override
    public Point move(Point deltaXY) {
        return new Point(deltaXY.x, 0);
    }
}
