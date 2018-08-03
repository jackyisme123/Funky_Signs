package Command;

import Strategy.MoveVerticalStrategy;
import funkySignsModel.MovingSign;

public class VerticalCommand implements Command{

    private MovingSign movingSign;
    public VerticalCommand(MovingSign movingSign){
        this.movingSign = movingSign;
    }
    @Override
    public void execute() {
        movingSign.setCurrentStrategy(new MoveVerticalStrategy());
    }
}
