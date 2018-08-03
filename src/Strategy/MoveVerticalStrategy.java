package Strategy;

import java.awt.*;

public class MoveVerticalStrategy implements MoveStrategy{

    @Override
    public Point move(Point deltaXY) {
        return new Point(0, deltaXY.y);
    }
}
