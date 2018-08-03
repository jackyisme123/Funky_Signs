// !!! Task 4: 
// !!! Put your answer here
// It's better to add functions as below.
// (1)
// It is necessary to add/delete updaters
// public void addUpdater(Updater u)   //add an update to the list
// public void deleteUpdater(Updater u)   //delete an update from the list
// public void deleteUpdaters()  //delete the updaters list, it no longer has any updaters
// (2)
// The getUpdaters() function is meaningless, because we just need to add/delete them. Do not need to get them.
// getUpdaters() can be changed to countUpdaters(), which will return the number of updaters.
// Or just remove getUpdaters() function.
// (3)
// The updaters type should not be ArrayList, because it will be a risk to add two same updaters into the list.
// Use Set instead of List will be a good solution
// (4)
// It's better to let Spy class extends Object class. In java documentation java.util.observable extends Object. And it
// is not necessary to be abstract class
// (5)
// It's better to ensure when the object changed, all updaters can be notified. And if with no change, updaters will not be notified.
// add functions as below
// hasChanged(), setChanged(), clearChanged()
// If object changed, setChanged() -> if (hasChanged) -> update() -> clearChanged()

package funkySignsModel;

import java.util.*;

/** A Spy tells a collection of Updaters to update. **/
public class Spy {

	/** The updaters list. **/
	private ArrayList<Updater> updaters;

	public Spy() {
		updaters = new ArrayList<Updater>();
	}
	
	/** Getter for updaters list. **/
	public ArrayList<Updater> getUpdaters() {
		return updaters;
	}

	/** Do an update. **/
	public void update() {
		for (Updater updater: updaters)
			updater.update();
	}
}