package funkySignsApplication;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;


import Strategy.MoveStrategy;
import Strategy.MoveVerticalStrategy;
import funkySignsGui.FunkySignsView;
import funkySignsGui.FunkySignsDisplayFrame;
import funkySignsModel.*;
import Command.*;

import static java.lang.Thread.sleep;

/**
 * The master application.
 * The main() method lives here.  It constructs an instance of FunkySignsApp,
 * which constructs the GUI and a Timer to kick off tick() events.
 * The command line arguments dictate which animations to set up.
 */

public class FunkySignsApp {

	/** Number of milliseconds between ticks. */
	private static final int TICKTIME = 100;

	/** Timer that will call tick() repeatedly in the GUI thread. */
    protected javax.swing.Timer timer;
	
	/** Collection of Signs to be animated. */
    protected Set<Sign> signs;
    
    /** The main Frame of the application. */
	private FunkySignsDisplayFrame gui;

	//task6: properties for command pattern
    private static Invoker invoker = new Invoker();
    private static HashMap<String, Command> cowboyCommands = null;

	/** Construct the SignsApp.
	 *  The gui is set up, but it will still need to be made visible.
	 *  The timer is set up too, but will still need to be started.
	 */
	public FunkySignsApp() {
//		signs = new HashSet<Sign>();
        // 2.2 change type from HashSet to LinkedHashSet
        signs = new LinkedHashSet<>();
    	gui = new FunkySignsDisplayFrame();
    	
        timer = new javax.swing.Timer(TICKTIME, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick(); // Call this every time timer fires.
			}
		});
     }

	/** Entry point for the application.
	 *  @param args Names of animations to run.
	 *  @see //handleArgs()
	 **/
    public static void main(String[] args) throws InterruptedException {
        FunkySignsApp app = new FunkySignsApp();      
        app.handleArgs(args);
        app.go();
        //task 6 change the cowboy sign moving direction every 3s
        while(true) {
            if (cowboyCommands != null) {
                invoker.setCommand(cowboyCommands.get("vertical"));
                invoker.executeCommand();
                sleep(3000);
                invoker.setCommand(cowboyCommands.get("horizontal"));
                invoker.executeCommand();
                sleep(3000);
                invoker.setCommand(cowboyCommands.get("diagonal"));
                invoker.executeCommand();
                sleep(3000);
            } else{
                System.out.println("Cannot find any command");
                return;
            }
        }
    }
    
	/** Read the argument list to decide which animations to run. **/
    private void handleArgs(String[] args) {
    	
    	// !!! Task 2 a): Ensure unique args.
        //original code
//    	Set <String> arguments = new HashSet<String>();
//     	for (String arg: args)
//    		handleArg(arg);
        //modified: change string[] to hashset, make sure every element in hashset is unique.
        Set <String> arguments = new HashSet<String>(Arrays.asList(args));
        for(String arg1: arguments){
            handleArg(arg1);
        }
    }
    
    /** Handle a single command line argument. */
    private void handleArg(String arg) {
    	
    	switch (arg.toUpperCase()) {
    	case "PLAINSIGN":
    		makePlainSign();
    		break;
    	case "COWBOYSIGN":
    		makeCowboySign();
    		break;
    	case "VEGASSIGN":
    		makeVegasSign();
    		break;
    	case "OUTDOORSIGN":
    		makeOutdoorSign();
    		break;
    	default:
    		System.err.println("Unknown argument: " + arg);
    	}
    }

	/** Start the animation.  Makes the gui appear & starts the timer. **/
    private void go() {
    	timer.start();
    	gui.setVisible(true);
    }



	/** Increment the animation. **/
    private void tick() {
    	// !!! Task 2 b): Eliminate randomness of iteration.
        //change signs type from HashSet to LinkedHashSet in Line 47
        for(Sign sign: signs) {
            sign.tick();
        }
    }

    private Rectangle getGuiBounds() {
    	return gui.getSignsDisplayPanel().getBounds();
    }
    
	/** Construct a simple sign using one of the gifs. 
	 *  @param //imagePath full path to the image.
	 * */
    
	private void makePlainSign() {
		Icon myImageIcon = new ImageIcon("images/saloon-sign.jpg");
		Sign plainSign = new PlainSign(myImageIcon);
        new FunkySignsView(gui.getSignsDisplayPanel(), plainSign);
		plainSign.setLocation(new Point(650,30));
        signs.add(plainSign);  timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            }
        });
     }
	
	/**
	 * Utility method for assembling an AnimatedSign.
	 * This method loads a set of image files with the given root name 
	 * and extension.  A number (0-numIcons) is inserted into the filenames.
	 * @param basePath Root of the filenames containing images.tree set
	 * @param numIcons Number of image files to load.
	 * @param extension Remainder of the filename after the generated number.
	 */
	private AnimatedSign makeAnimation(String basePath, int numIcons, String extension) {
		
		// Generate filenames & make ImageIcons for each of the images.
		Icon[] icons = new Icon[numIcons];
		for (int i = 1; i <= numIcons; i++){
			icons[i-1] = new ImageIcon(basePath + "-" + i + extension);
		}

		// Create the basic plainSign
		Sign plainSign = new PlainSign(icons[0]);
		new FunkySignsView(gui.getSignsDisplayPanel(),plainSign);
		
		// Create an animated sign by giving it a plain sign and all the icons.
		AnimatedSign animatedSign = new AnimatedSign(plainSign, icons);		
        return animatedSign;
    }
	
	/** Utility method to wrap a Sign so it moves.
	 *  @param base The Sign to wrap.
	 *  @param deltaXY The amount to increment the Sign's XY coordinates.
	 **/
	private MovingSign makeMovingSign(Sign base, Point deltaXY, MoveStrategy myStrategy) {
		//---------------------------------------------------------------------------------//
		//add strategy attribute to create a new moving sign object
		return new MovingSign(base, deltaXY, getGuiBounds(), myStrategy);
		//---------------------------------------------------------------------------------//
	}
	
	/** Utility method to wrap a Sign so it spins.
	 *  @param base The Sign to wrap.
	 *  @param deltaDegrees The amount to increment the Sign's rotation.
	 **/
	private SpinningSign makeSpinner(Sign base, int deltaDegrees) {
		return new SpinningSign(base, deltaDegrees);
	}
	
	private void makeVegasSign(){
		
		// Create the flashing lights
		Sign flashingLights = makeAnimation("images/surround-lights",2,".png");
		// Create the Vegas sign. Animate it.
		Sign vegasSignAnimated = makeAnimation("images/vegas-sign", 3, ".png");

		// Line up both the signs to each other
		vegasSignAnimated.setLocation(new Point(0, 0)); 
		flashingLights.setLocation(new Point (175,275));
		
		// Create the Compound sign by adding both the animated sign and the flashing lights
        Sign vegasSign = new CompoundSign();
        vegasSign.addSign(flashingLights);
        vegasSign.addSign(vegasSignAnimated);
        
        // Choose the new location for the completed sign
        vegasSign.setLocation(new Point(400,330));


        // task5: try to spin vegasSign, but it does not work.
		//--------------------------------------------------
		vegasSign = makeSpinner(vegasSign, 20);
		//--------------------------------------------------
 
        // Add it to our collection of signs    
        signs.add(vegasSign);
	}
	
	private void makeOutdoorSign(){
		
		// Create the individual signs
        Icon seng301ImageIcon = new ImageIcon("images/seng301-sign.png");
		Sign seng301Sign = new PlainSign(seng301ImageIcon);	
        new FunkySignsView(gui.getSignsDisplayPanel(), seng301Sign);

        
    	Icon metalSignIcon = new ImageIcon("images/metal-background.png");
        Sign metalSign = new PlainSign(metalSignIcon);
        new FunkySignsView(gui.getSignsDisplayPanel(), metalSign);
        
        // Align the signs to each other
        metalSign.setLocation(new Point(0, 0)); 
		seng301Sign.setLocation(new Point(0,0));

        
		CompoundSign outdoorSign = new CompoundSign();
		outdoorSign.addSign(seng301Sign);
		outdoorSign.addSign(metalSign);				

		// Set the location of the OutdoorSign
		
		outdoorSign.setLocation(new Point(10,40));
		signs.add(outdoorSign);
		
	}
	/* Construct a cowboy sign using the cowboy and basic signs */
	private void makeCowboySign(){
		
		//Create the plain sign
	   	Icon baseIcon = new ImageIcon("images/plain-sign.png");
        Sign baseSign = new PlainSign(baseIcon);
        new FunkySignsView(gui.getSignsDisplayPanel(), baseSign);

        // Create the two cowboys (left and right)
		Icon cowboyIcon = new ImageIcon("images/cowboy.png");
        Sign stillCowboyLeft= new PlainSign(cowboyIcon);
        new FunkySignsView(gui.getSignsDisplayPanel(), stillCowboyLeft);
        Sign stillCowboyRight = new PlainSign(cowboyIcon);
        new FunkySignsView(gui.getSignsDisplayPanel(), stillCowboyRight);
        
        // Line up all cowboys with the base sign
        stillCowboyLeft.setLocation(new Point(0, 0)); 
        baseSign.setLocation(new Point(225, 110)); 
        stillCowboyRight.setLocation(new Point(400, 0));


    	// !!! Task 1 a): Spin each cowboy. You can choose the speed and direction of the spin.
        int spinningDegree = 20; //set rotation speed
        int spinningDirection = -1; //set rotation direction (-1 for counter-clockwise, 1 for clockwise)
        Sign spinningCowboyLeft;
        spinningCowboyLeft = makeSpinner(stillCowboyLeft, spinningDegree * spinningDirection);
        Sign spinningCowboyRight;
        spinningCowboyRight= makeSpinner(stillCowboyRight, spinningDegree * spinningDirection);

    	// !!! Task 1 b): Combine the base sign and the two cowboy signs to create one cowboySign
        Sign cowboySign = new CompoundSign();
        cowboySign.addSign(spinningCowboyLeft);
        cowboySign.addSign(baseSign);
        cowboySign.addSign(spinningCowboyRight);
        // !!! Task 1 b): Place the movingCowboySign somewhere appropriate on the screen
        int movingSignLocX = 0; //set location.x
        int movingSignLocY = 100; //set location y
        cowboySign.setLocation(new Point(movingSignLocX, movingSignLocY));// set location for compound sign
    	// !!! Task 1 c): Move the movingCowboySign so that it moves from one side of the screen to the other
        int movePixelRight = 8; //speed of right direction
        int movePixelDown = 4; //speed of down direction
        // Task 1 & 6 choose a strategy for moving Sign, add a new attribute strategy type
        Sign movingCowboySign = makeMovingSign(cowboySign, new Point(movePixelRight, movePixelDown), new MoveVerticalStrategy());
        // !!! Add the movingCowboySign to our collection of signs
        signs.add(movingCowboySign);
        // Task 6: build commands for movingCowboySign
        cowboyCommands = buildCommands(movingCowboySign);
	}

	// Task 6: use this function can build command for any movingSign.
	private HashMap<String, Command> buildCommands(Sign sign){
        HashMap<String, Command> myCommands = new HashMap<>();
        Command vertical = null;
        Command horizontal = null;
        Command diagonal = null;
        if(sign instanceof MovingSign){
            vertical = new VerticalCommand((MovingSign) sign);
            horizontal = new HorizontalCommand((MovingSign) sign);
            diagonal = new DiagonalCommand((MovingSign) sign);
        }else{
            System.err.println(sign.toString() + " is not a type of MovingSign");
            System.err.println("Only MovingSign object can execute buildCommands method");
            System.exit(-1);
        }
        if (!myCommands.containsKey("vertical")){
            myCommands.put("vertical", vertical);
        }
        if (!myCommands.containsKey("horizontal")){
            myCommands.put("horizontal", horizontal);
        }
        if (!myCommands.containsKey("diagonal")){
            myCommands.put("diagonal", diagonal);
        }
        return myCommands;
    }

}
