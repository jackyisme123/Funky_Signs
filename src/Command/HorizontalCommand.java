package Command;

import Strategy.MoveHorizontalStrategy;
import funkySignsModel.MovingSign;

public class HorizontalCommand implements Command {

    private MovingSign movingSign;
    public HorizontalCommand(MovingSign movingSign){
        this.movingSign = movingSign;
    }
    @Override
    public void execute() {
        movingSign.setCurrentStrategy(new MoveHorizontalStrategy());
    }
}
