package Command;

import Strategy.MoveDiagonalStrategy;
import funkySignsModel.MovingSign;

public class DiagonalCommand implements Command{
    private MovingSign movingSign;
    public DiagonalCommand(MovingSign movingSign){
        this.movingSign = movingSign;
    }

    @Override
    public void execute() {
        movingSign.setCurrentStrategy(new MoveDiagonalStrategy());
    }
}
