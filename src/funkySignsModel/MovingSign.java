// !!! Task 6:
// !!! Put your answer here.



package funkySignsModel;


import Strategy.MoveStrategy;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * A specialisation of DynamicSign that wraps any other Sign.
 * This class takes some existing Sign and makes it move.
 */



public class MovingSign extends DynamicSign {

	/** Number of pixels to move right & down each timer invocation. */
	private Point deltaXY;

	/** Box that the sign's centre point must remain inside. */
	private Rectangle limit;

	private Rectangle originalBounds;

	//---------------------------------------//
	// add currentStrategy property;
	private MoveStrategy currentStrategy;
	//---------------------------------------//

	/**
	 * Construct a MovingSign that will update the given Sign
	 * at the given interval.
	 * @param theBaseSign The Sign to be wrapped.
	 * @param theDeltaXY A Point containing the number of pixels to move right and down each increment.
	 * @param myStrategy A strategy to be used
	 */
	public MovingSign(Sign theBaseSign, Point theDeltaXY, Rectangle theLimit,MoveStrategy myStrategy) {
		super(theBaseSign); // Takes care of the Timer etc.
		originalBounds = theBaseSign.getBounds();
		setDeltaXY(theDeltaXY);
		limit = theLimit;
		//--------------------------------------------------------//
		// add strategy
		currentStrategy = myStrategy;
		//--------------------------------------------------------//
	}

	//-----------------------------------------------------------//
	//getters for strategy
	public MoveStrategy getCurrentStrategy() {
		return currentStrategy;
	}

	//setters for strategy
	public void setCurrentStrategy(MoveStrategy newStrategy) {
		currentStrategy = newStrategy;
	}
	//-----------------------------------------------------------//

	public void setDeltaXY(Point theDeltaXY) {
		deltaXY = theDeltaXY;
	}

	public Point getDeltaXY() {
		return deltaXY;
	}

	/** The timer has fired, so perform this Sign's incremental actions. */
	public void tick() {
		move(); // Routine action.
		super.tick();
	}

	/** Change the position of the Sign by deltaXY. */	
	protected void move() {
		Point where = getLocation(); // top-left point.
		//change code for strategy pattern
		//---------------------------------------------//
		Point moveType = currentStrategy.move(deltaXY);
//		where.x += deltaXY.x;
//		where.y += deltaXY.y;
		where.x += moveType.x;
		where.y += moveType.y;
		//---------------------------------------------//
		setLocation(where);

		checkBounds();
	}


	protected void checkBounds() {
		Rectangle bounds = getBounds();
		Point left = new Point(bounds.x, bounds.y);
		Point right = new Point(bounds.x + bounds.width, bounds.y + bounds.height);

		if (!limit.contains(left) && !limit.contains(right)) {
			setLocation(new Point(originalBounds.x, originalBounds.y));
		}
	}

}
