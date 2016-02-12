import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import misc.PriceChecker;


import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Equipment;
import com.rarebot.script.methods.Game;
import com.rarebot.script.methods.Game.Tab;
import com.rarebot.script.methods.Magic.Book;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.util.Timer;
import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSComponent;
import com.rarebot.script.wrappers.RSGroundItem;
import com.rarebot.script.wrappers.RSItem;
import com.rarebot.script.wrappers.RSModel;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSPath;
import com.rarebot.script.wrappers.RSPlayer;
import com.rarebot.script.wrappers.RSTile;

@ScriptManifest(authors = { "Psion" }, name = "ThunderRevenants", keywords = "Revenant, killer", version = 2.16, description = "Kills Revenants for money")
public class Main extends Script implements MessageListener, PaintListener, ActionListener, MouseListener, MouseMotionListener {

	//RSObjects/RSTiles/RSAreas
	private final int revEntID = 20600;
	private final RSArea edgeBank = new RSArea(new RSTile(3089, 3488), new RSTile(3099, 3500));
	private final RSArea wallAreaEdge = new RSArea(new RSTile(3087, 3514), new RSTile(3100, 3520));
	private final RSArea wallAreaWildy = new RSArea(new RSTile(3087, 3523), new RSTile(3100, 3530));
	private final RSArea edgeSpawn = new RSArea(new RSTile(3100, 3488), new RSTile(3106, 3496));
	private final int wallID = 65094;
	private final RSTile[] pathFromBank = new RSTile[]{
			new RSTile(3093, 3497, 0),
			new RSTile(3092, 3502, 0),
			new RSTile(3087, 3504, 0),
			new RSTile(3087, 3508, 0),
			new RSTile(3087, 3512, 0),
			new RSTile(3090, 3516, 0),
			new RSTile(3088, 3520, 0),
	};
	private final RSTile[] pathToCave = new RSTile[]{
			new RSTile(3088, 3527, 0), 
			new RSTile(3085, 3531, 0),
			new RSTile(3081, 3534, 0), 
			new RSTile(3077, 3537, 0),
			new RSTile(3073, 3540, 0), 
			new RSTile(3069, 3543, 0),
			new RSTile(3068, 3548, 0), 
			new RSTile(3068, 3554, 0),
			new RSTile(3068, 3559, 0), 
			new RSTile(3068, 3564, 0),
			new RSTile(3069, 3569, 0), 
			new RSTile(3068, 3573, 0),
			new RSTile(3068, 3579, 0), 
			new RSTile(3068, 3584, 0),
			new RSTile(3070, 3588, 0), 
			new RSTile(3070, 3592, 0),
			new RSTile(3070, 3596, 0), 
			new RSTile(3070, 3600, 0),
			new RSTile(3070, 3604, 0), 
			new RSTile(3070, 3608, 0),
			new RSTile(3071, 3613, 0), 
			new RSTile(3071, 3618, 0),
			new RSTile(3072, 3622, 0), 
			new RSTile(3073, 3626, 0),
			new RSTile(3072, 3630, 0), 
			new RSTile(3071, 3634, 0),
			new RSTile(3070, 3638, 0), 
			new RSTile(3069, 3642, 0),
			new RSTile(3070, 3646, 0) 
	};
	private final RSTile[] pathToWall = new RSTile[]{
			new RSTile(3069, 3646, 0),
			new RSTile(3069, 3642, 0),
			new RSTile(3070, 3638, 0),
			new RSTile(3071, 3634, 0),
			new RSTile(3072, 3630, 0),
			new RSTile(3073, 3626, 0),
			new RSTile(3072, 3622, 0),
			new RSTile(3071, 3618, 0),
			new RSTile(3071, 3613, 0),
			new RSTile(3070, 3608, 0),
			new RSTile(3070, 3604, 0),
			new RSTile(3070, 3600, 0),
			new RSTile(3070, 3596, 0),
			new RSTile(3070, 3592, 0),
			new RSTile(3070, 3588, 0),
			new RSTile(3068, 3584, 0),
			new RSTile(3068, 3579, 0),
			new RSTile(3068, 3573, 0),
			new RSTile(3069, 3569, 0),
			new RSTile(3068, 3564, 0),
			new RSTile(3068, 3559, 0),
			new RSTile(3068, 3554, 0),
			new RSTile(3068, 3548, 0),
			new RSTile(3069, 3543, 0),
			new RSTile(3073, 3540, 0),
			new RSTile(3077, 3537, 0),
			new RSTile(3085, 3540, 0),
			new RSTile(3081, 3534, 0),
			new RSTile(3085, 3531, 0),
			new RSTile(3088, 3527, 0),
			new RSTile(3088, 3523, 0)
	};
	private final RSTile[] pathToRevs = new RSTile[]{ 
			new RSTile(3081, 10060),
			new RSTile(3086, 10065),
			new RSTile(3088, 10070),
			new RSTile(3090, 10075),
			new RSTile(3090, 10080),
			new RSTile(3093, 10083),
			new RSTile(3096, 10085),
			new RSTile(3101, 10085),
			new RSTile(3105, 10083),
			new RSTile(3110, 10085),
			new RSTile(3113, 10090),
			new RSTile(3115, 10093),
			new RSTile(3116, 10096),
			new RSTile(3116, 10101),
			new RSTile(3117, 10104),
			new RSTile(3117, 10107),
			new RSTile(3117, 10112),
			new RSTile(3117, 10115),
			new RSTile(3118, 10118),
			new RSTile(3122, 10121),
			new RSTile(3122, 10124),
			new RSTile(3122, 10128),
			new RSTile(3122, 10132),
			new RSTile(3120, 10136)
	};
	private final RSTile[] pathToExit = new RSTile[]{
			new RSTile(3120, 10136),
			new RSTile(3122, 10132),
			new RSTile(3122, 10128),
			new RSTile(3122, 10124),
			new RSTile(3122, 10121),
			new RSTile(3118, 10118),
			new RSTile(3117, 10115),
			new RSTile(3117, 10112),
			new RSTile(3117, 10107),
			new RSTile(3117, 10104),
			new RSTile(3116, 10101),
			new RSTile(3116, 10096),
			new RSTile(3115, 10093),
			new RSTile(3113, 10090),
			new RSTile(3110, 10085),
			new RSTile(3105, 10083),
			new RSTile(3101, 10085),
			new RSTile(3090, 10080),
			new RSTile(3090, 10075),
			new RSTile(3088, 10070),
			new RSTile(3086, 10065),
			new RSTile(3081, 10060),
			new RSTile(3077, 10058),
	};
	private final RSTile[] pathToGuild = new RSTile[]{
			new RSTile(2898, 3541), 
			new RSTile(2899, 3537), 
			new RSTile(2902, 3533), 
			new RSTile(2907, 3531), 
			new RSTile(2912, 3530), 
			new RSTile(2917, 3529), 
			new RSTile(2919, 3524), 
			new RSTile(2920, 3519),
			new RSTile(2918, 3514) 
	};
	private final RSTile[] pathToFount = new RSTile[]{
			new RSTile(2896, 9909),
			new RSTile(2899, 9911),
			new RSTile(2904, 9913),
			new RSTile(2909, 9912),
			new RSTile(2915, 9913),
			new RSTile(2918, 9911),
			new RSTile(2922, 9910),
			new RSTile(2927, 9910),
			new RSTile(2932, 9909),
			new RSTile(2937, 9908),
			new RSTile(2938, 9905),
			new RSTile(2935, 9902),
			new RSTile(2935, 9902),
			new RSTile(2932, 9899),
			new RSTile(2934, 9896),
			new RSTile(2930, 9894),
			new RSTile(2924, 9894),
			new RSTile(2920, 9894),
	};
	private final int lodePage = 1092;
	private final int burthTele = 42;
	private final RSArea burthLodeArea = new RSArea(new RSTile(2897, 3543), new RSTile(2901, 3546), 0);
	private final int guildDoor = 2625;
	private final RSTile ladderTile = new RSTile(2906, 3515, 0);
	private final int ladder = 67690;
	private final int fountain = 36695;
	private final int edgeTele = 45;
	private final RSArea edgeLodeArea = new RSArea(new RSTile(3065, 3504), new RSTile(3069, 3507), 0);
	private final RSArea revArea = new RSArea(new RSTile(3084, 10133), new RSTile(3130, 10170));
	private final RSArea revEntArea = new RSArea(new RSTile(3065, 3646), new RSTile(3072, 3652));
	private final RSArea revExitArea = new RSArea(new RSTile(3077, 10056), new RSTile(3089, 10064));
	private final RSArea edgeTeleArea = new RSArea(new RSTile(3086, 3495), new RSTile(3088, 34097));
	private final int revExitID = 18342;

	// LootIDs
	final public int LootID[] = { 13911, 13923, 13917, 13929, 13899, 13893, 13905, 13887, /* Vesta */
			13920, 13908, 13914, 13926, 13896, 13884, 13890, 13902, /* Statius */
			13938, 13935, 13932, 13941, 13864, 13861, 13858, 13867, /* Zuriel */
			13954, 13955, 13956, 13957, 13950, 13953, 13944, 13947, 13876, 13879, 13880, 13881, 13882, 13870, 13873, 13883, /* Morrigan */
			13958, 13961, 13964, 13967, 13970, 13973, 13976, 13979, 13982, 13985, 13988, /* Corrupt Dragon Stuff */
			14876, 14877, 14878, 14879, 14880, 14881, 14882, 14883, 14884, 14885, 14886, 14887, 14888, 14889, 14890, 14891, 14892, /* Statuettes */
			13845, 13846, 13847, 13848, 13849, 13850, 13851, 13852, 13853, 13854, 13855, 13856, 13857, /* Brawler Gloves */
			6794 /* Choc Ice */
			/*995 /* Gold */
	};

	//Use Items
	final private int braces[] = { 11095, 11097, 11099, 11101, 11103 };
	final private int supers[] = { 2436, 2440, 2442 };
	final private int glory[] = { 1710, 1712, 1708, 1706 };
	final private int vial = 229;


	//Paint
	private final Image revPaint = getImage("http://dl.dropbox.com/u/39936143/Photo/RevPaint.png");
	private final Image revX = getImage("http://dl.dropbox.com/u/39936143/Photo/revX.png");
	private final Rectangle hideButton = new Rectangle(482, 350, 25, 21);
	private final RenderingHints antialiasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	private boolean show = true;
	private Timer run;
	private String status;
	private final Rectangle debugButton = new Rectangle(0, 219, 25, 21);
	private boolean debug = false;

	//Variables
	int threatLvl;
	int timesKilled = 0;
	RSItem[] equip;
	boolean repelled = false;
	boolean shutdown = false;
	boolean itemShutdown = false;
	boolean useCentralClicking = false;
	boolean walkFromBank = false;
	boolean walkToRevs = false;
	boolean walkToCave = false;
	boolean walkToExit = false;
	boolean walkToWall = false;
	boolean needToBank = false;
	boolean needToRech = false;
	boolean walkToGuild = false;
	boolean walkToFount = false;
	int looted = 0;
	int currentLoot = 0;
	int kills = 0;
	private RSTile destination;
	private RSNPC currNPC = null;
	private boolean teleblocked = false;
	private Timer blocked;

	//GUI
	private JFrame frame;
	private javax.swing.JComboBox bankingSelect;
	private javax.swing.JComboBox potSelect;
	private javax.swing.JComboBox diffSelect;
	private javax.swing.JCheckBox chkPots;
	private javax.swing.JCheckBox chkKillsteal;
	private javax.swing.JCheckBox chkSwitch;
	private javax.swing.JTextField txtFoodID;
	private javax.swing.JTextField txtFoodAmt;
	private javax.swing.JTextField txtLootAmt;
	private javax.swing.JTextField txtSwitch;
	private int valBank;
	private int revLvl;
	private boolean killsteal = false;
	private int banking;
	private int foodID;
	private int foodAmt;
	private boolean usePots = true;
	private int potions;
	private boolean worldHop = false;
	private int switchAfter;

	//Idle Thread
	private boolean runThread = true;
	private final int SLEEP = 10000;
	private final int TIME_TO_STOP = 13;
	private int SKILL;
	private int lastXp;
	private int timeElapsed;

	@Override
	public boolean onStart() {
		try {
			log("Attempting to start GUI");
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					GUI();
					frame.setVisible(true);
				}
			});
		} catch (Exception e) {
			log.severe(e.toString());
			return false;
		}
		if (game.isLoggedIn()) {
			while (frame.isVisible()) {
				sleep(100);
			}
			run = new Timer(0);
			game.openTab(Game.Tab.EQUIPMENT);
			sleep(300);
			equip = equipment.getWornItems();
			for (int i = 0; i < equip.length; i++) {
				if(equip[i].getID() != 0 && equip[i].getID() != -1){
					log("Equipment slot: " + equip[i].getID());
				}
			}
			sleep(150);
			game.openTab(Game.Tab.INVENTORY);
		}
		try {
			SKILL = Skills.CONSTITUTION;
			startThread();
			log.warning("Will shutdown if we gain no Consitution EXP for 13 minutes!");
		} catch (Exception e) {
			log.severe("Error starting idle detection thread!");
		}
		return true;
	}

	public int loop() {
		try{
			if (failSafe()){
				return -1;
			}
			if (camera.getPitch() < 2800){
				camera.setPitch(2800);
			}
			mouse.setSpeed(random(4, 6));
			if (teleblocked && !blocked.isRunning()){
				teleblocked = false;
			}
			threatLvl = Assess();
			if (threatLvl == 3) {
				log.warning("Being PK'd. In route to bank.");
				needToBank = true;
			}
			if ((prayer.isQuickPrayerOn() && threatLvl == 0 && currentLoot < valBank) || (!prayer.isQuickPrayerOn() && threatLvl == 3) || (!prayer.isQuickPrayerOn() && currentLoot >= valBank)) {
				quickPrayer();
			}
			if (canPot()) {
				drinkPotion();
			}
			if (inventory.getItem(vial) != null){
				inventory.getItem(vial).interact("Drop");
			}
			// Equips a new glory if there is one in the inventory
			if (inventory.getItem(1710) != null && !bank.isOpen() && banking == 2) {
				mouse.move(inventory.getItem(1710).getComponent().getLocation());
				mouse.click(true);
			}
			if (walkToWall){
				status = "Walking to Wall";
			}
			if (walkToRevs){
				status = "Walking to Revs";
			}
			if (walkToCave){
				status = "Walking to Cave";
			}
			if (walkToExit){
				status = "Walking to Exit";
			}
			if (walkToGuild){
				status = "Walking to Guild";
			}
			if (walkToFount){
				status = "Walking to Fount";
			}
			if (!needToBank){
				if (inventory.getCount(foodID) <= 1 && !bank.isOpen() && !edgeBank()) {
					needToBank = true;
					log.warning("Uh oh, we're out of food and need to bank.");
				}
				if (inventory.getItem(braces) == null && repelled == false){
					needToBank = true;
					log.warning("Uh oh, we're out of braces and need to bank.");
				}
				if (currentLoot >= valBank) {
					needToBank = true;
					log(Color.BLUE, "Banking due to current loot being greater than or equal to loot2bank.");
				}
				if (edgeBank() && inventory.getItem(foodID) != null) {
					walkFromBank = true;
				}
				if (walkFromBank) {
					walkPath(pathFromBank);
				}
				if (revArea()) {
					attackRevs();
					status = "Attacking";
				}
				if (revExitArea() && !repelled) {
					applyRepel();
				}
				if (wallAreaEdge.contains(playerPosition())){
					interactObj(wallID, "Cross Wilderness wall");
					sleep(2700, 3200);
				}
				if (wallAreaWildy.contains(playerPosition())) {
					walkFromBank = false;
					walkToCave = true;
				}
				if (revEntArea() && objects.getNearest(revEntID) != null) {
					walkToCave = false;
					interactObj(revEntID, "Enter Cave");
					sleep(2700, 3200);
					walkToRevs = true;
				}
				if (walkToCave){
					walkPath(pathToCave);
				}
				if (revArea()) {
					walkToRevs = false;
				}
				if (walkToRevs) {
					walkPath(pathToRevs);
				}
			}
			if(needToBank && !needToRech){
				gloryHandler();
				if (revExitArea() && objects.getNearest(revExitID) != null) {
					interactObj(revExitID, "Leave Exit");
					sleep(2700, 3200);
					walkToExit = false;
				}
				if (revEntArea() && !walkToWall) {
					walkToWall = true;
				}
				if (walkToWall) {
					walkPath(pathToWall);
				}
				if (revArea() && !walkToExit && banking == 1){
					walkToExit = true;
				}
				if (walkToExit){
					walkPath(pathToExit);
				}
				if (wallAreaWildy.contains(playerPosition())){
					interactObj(wallID, "Cross Wilderness wall");
					sleep(2700, 3200);
				}
				if(wallAreaEdge.contains(playerPosition())){
					walkToWall = false;
					status = "Walking to Bank";
					setDestination(edgeBank.getCentralTile());
				}
				if (edgeTeleArea.contains(playerPosition()) 
						|| edgeSpawn.contains(playerPosition())) {
					reEquip();
					setDestination(edgeBank.getCentralTile());
				}
				if (edgeBank()) {
					bank();
					status = "Banking";
				}
				if(objects.getNearest(fountain) != null){
					teleport(2);
				}
				if(edgeLodeArea.contains(playerPosition())){
					setDestination(edgeBank.getCentralTile());
				}
			}
			if(needToRech){
				if(edgeBank() && inventory.getItem(1704) != null && !bank.isOpen()){
					teleport(1);
				}
				if(burthLodeArea.contains(playerPosition())){
					walkToGuild = true;
				}
				if(walkToGuild){
					walkPath(pathToGuild);
				}
				if(walkToGuild && objects.getNearest(guildDoor) != null){
					if(objects.getNearest(guildDoor).interact("Open")){
						walkToGuild = false;
						sleep(1000, 1200);
					}
				}
				if(!walkToGuild && !walkToFount){
					setDestination(ladderTile);
				}
				if(objects.getNearest(ladder) != null){
					if(objects.getNearest(ladder).interact("Climb-down")){
						sleep(1800, 2500);
						walkToFount = true;
					}
				}
				if(walkToFount){
					walkPath(pathToFount);
				}
				if(objects.getNearest(fountain) != null){
					if(inventory.getSelectedItem() == null){
						inventory.selectItem(1704);
					}else{
						if(objects.getNearest(fountain).doClick(true)){
							needToRech = false;
						}
					}
				}
			}
			if(groundItems.getNearest(LootID) != null){
				RSGroundItem lootIt = groundItems.getNearest(LootID);
				needToBank = false;
				if (!lootIt.isOnScreen()) {
					walking.walkTileMM(groundItems.getNearest(LootID).getLocation());
					sleep(500, 700);
				}
				if (inventory.isFull()) {
					inventory.getItem(foodID).doClick(true);
					sleep(500, 700);
				}
				if (lootIt.isOnScreen()) {
					int price = 0;
					int collected = 0;
					if (lootIt.getItem().getID() > 13857
							&& lootIt.getItem().getID() < 14876) {
						price = grandExchange.lookup(lootIt.getItem().getID()).getGuidePrice();
					}
					if (price == 0) {
						collected = PriceChecker.Lookup(lootIt.getItem().getID());
					}
					if (price != 0) {
						collected = price;
					}
					int tempID = lootIt.getItem().getID();
					if (lootIt.interact("Take")){
						log(Color.GREEN, "Found a: " + lootIt.getItem().getName());
						sleep(900, 1200);
					}
					int count = 1;
					count = inventory.getCount(tempID);
					if (count == 0) {
						count = 1;
					}
					currentLoot += collected * count;
					looted += collected * count;
				}
			}
			if (destination != null && calc.distanceTo(destination) > 3) {
				walk();
			}
			if(revArea()){
				if (!repelled) {
					applyRepel();
				}
				if(needToBank){
					setDestination(new RSTile(3120, 10136));
				}
			}
			if (hp() < random(53, 59)) {
				eat();
			}
			if (walking.getEnergy() >= random(35, 50)) {
				walking.setRun(true);
				sleep(500, 800);
			}
			if (interfaces.getComponent(382, 19).isValid()) {
				interfaces.getComponent(382, 19).doClick(true);
			}
			if (timesKilled >= switchAfter && worldHop) {
				env.disableRandom("Login");
				sworlds(randWorld());
			}
		}catch(Exception e){
		}
		return random(300, 400);
	}

	@Override
	public void onFinish() {
		log("Thanks for using Thunder Revenants!");
		runThread = false;
		return;
	}

	@Override
	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHints(antialiasing);
		g.drawImage(revX, 482, 350, null);
		int killsHour = (int) ((3600000.0 / (double) run.getElapsed()) * kills);
		int lootHour = (int) ((3600000.0 / (double) run.getElapsed()) * looted);
		final double version = this.getClass().getAnnotation(ScriptManifest.class).version();
		if (show) {
			g.drawImage(revPaint, -8, 219, null);
			final Font font1 = new Font("Arial", 1, 13);
			g.setFont(font1);
			g.drawString(Timer.format(run.getElapsed()), 160, 380);
			g.drawString(" " + kills, 160, 397);
			g.drawString(" " + killsHour, 160, 414);
			g.drawString(" " + looted, 160, 433);
			g.drawString(" " + lootHour, 160, 450);
			g.drawString(status, 86, 300);
			g.drawString(" " + version, 86, 317);
			if(debug){
				if (threatLvl == 3) {
					g.setPaint(Color.red);
					g.fill(new Rectangle2D.Double(20, 10, 50, 200));
					g.setPaint(Color.white);
				}
				g.setPaint(Color.black);
				g.fill(new Rectangle2D.Double(550, 210, 185, 280));
				g.setPaint(Color.white);
				g.drawString("Debug stuff:", 555, 240);
				g.drawString("NeedTobank = " + needToBank, 555, 262);
				g.drawString("WalkToRevs = " + walkToRevs, 555, 274);
				g.drawString("Destination:", 555, 286);
				if (destination != null) {
					g.drawString("Tile: " + destination, 555, 298);
				}
				if (players.getMyPlayer().getInteracting() != null) {
					g.drawString("Interacting with: ", 555, 310);
					g.drawString("" + getMyPlayer().getInteracting(), 555,
							322);
				}
				g.drawString("Threat: " + threatLvl, 555, 334);
				g.drawString("Food: " + foodID + " Amt: " + foodAmt, 555, 346);
				g.drawString("Times Killed: " + timesKilled, 555, 358);
			}
		}
	}

	@Override
	public void messageReceived(MessageEvent e) {
		if (e.getID() == MessageEvent.MESSAGE_SERVER || e.getID() == MessageEvent.MESSAGE_CLIENT) {
			String text = e.getMessage().toLowerCase();
			if (text.contains("teleport block")){
				if(banking == 2){
					log.warning("You've been teleblocked. We'll have to walk to the bank.");
					teleblocked = true;
					blocked = new Timer(300000);
				}
			}
			if (text.contains("revenants are once again aggressive towards you")) {
				log(Color.BLUE, "Forinthry Brace has worn off. Reusing.");
				repelled = false;
				applyRepel();
			}
			if (text.contains("you still have")) {
				repelled = true;
			}
			if (text.contains("oh dear")) {
				timesKilled += 1;
				needToBank = true;
			}
		}
	}

	private RSTile playerPosition() {
		return getMyPlayer().getLocation();
	}

	private boolean edgeBank(){
		return edgeBank.contains(playerPosition());
	}

	private boolean revEntArea(){
		return revEntArea.contains(playerPosition());
	}


	private boolean revArea(){
		return revArea.contains(playerPosition());
	}

	private boolean revExitArea(){
		return revExitArea.contains(playerPosition());
	}

	private void setDestination(RSTile t) {
		destination = t;
	}

	private int walk() {
		walking.walkTileMM(destination);
		return random(100, 300);
	}

	private void walkPath(RSTile[] path){
		RSPath walkPath = walking.newTilePath(path);
		if(calc.distanceTo(walkPath.getEnd()) > 3){
			walkPath.traverse();
		}
	}

	private boolean teleport(int destination) {
		int component = 0;
		if(destination == 1){
			component = burthTele;
		}
		if(destination == 2){
			component = edgeTele;
		}
		if (interfaces.get(lodePage).isValid()) {
			if (interfaces.getComponent(lodePage, component).isValid()) {
				return interfaces.getComponent(lodePage, component).interact("teleport");
			}
		}
		if (game.getTab() != Tab.MAGIC) {
			keyboard.pressKey((char)KeyEvent.VK_F4);
			sleep(250, 300);
			keyboard.releaseKey((char)KeyEvent.VK_F4);
		}
		if (magic.getCurrentSpellBook() == Book.MODERN) {
			return magic.castSpell(24);
		} else if (magic.getCurrentSpellBook() == Book.ANCIENT) {
			return magic.castSpell(48);
		} else {
			return magic.castSpell(38);
		}
	}

	public int hp() {
		return (int) ((double) (Integer.parseInt(interfaces.get(748).getComponent(8).getText()) / 10)
				/ skills.getRealLevel(Skills.CONSTITUTION) * 100);
	}

	private boolean quickPrayer() {
		if (!bank.isOpen() && prayer.getPrayerLeft() > 1) {
			return interfaces.get(749).getComponent(2).doClick();
		}
		return false;
	}

	private void eat() {
		RSItem food = edibleItem();
		if (food != null){
			if(food.doClick(true)){
				sleep(350);
			}
		}
	}

	private RSItem edibleItem() {
		RSItem[] is = inventory.getItems(true);
		for (RSItem i : is) {
			if (isEdible(i)){
				return i;
			}
		}
		return null;
	}

	private boolean isEdible(final RSItem item) {
		if (item.getComponent().getActions() == null || item.getComponent().getActions()[0] == null){
			return false;
		}
		return item.getComponent().getActions()[0].contains("Eat");
	}

	private boolean attack(final RSNPC npc, final String action) {
		Point p = npc.getPoint();
		mouse.hop(p);
		if (menu.contains(npc.getName()) && !menu.contains(action)) {
			return false;
		} else {
			if (menu.isOpen() && !menu.contains(action)) {
				if (menu.doAction("Cancel")) {
					Timer j = new Timer(1200);
					while (j.isRunning() && menu.isOpen()
							&& !menu.contains(action)) {
						sleep(10, 20);
					}
				}
			} else {
				if (menu.getIndex(action) != 0) {
					mouse.click(false);
					return menu.contains(action) && menu.doAction(action);
				} else {
					if (menu.getIndex(action) == 0) {
						return menu.contains(action) && menu.doAction(action);
					}
				}
			}
		}
		return false;
	}

	/*int[] revenantIds = { 13465, 13466, 13467, 13475, 13468, 13471, 13470,
			13469, 13472, 13476, 13474, 13473, 13477, 13478, 13479 };*/
	int r = 0;
	final int[] easyRevs = { 13465, 13466, 13467, 13468, 13470, 13469 };
	final int[] medRevs = { 13465, 13466, 13467, 13475, 13468, 13471, 13470, 13469,
			13472, 13473 };
	final int[] hardRevs = { 13465, 13466, 13467, 13475, 13468, 13471, 13470, 13469,
			13472, 13473, 13476, 13474 };
	final int[] extremeRevs = { 13465, 13466, 13467, 13475, 13468, 13471, 13470,
			13469, 13472, 13473, 13476, 13474, 13477, 13478, 13479 };

	private RSNPC getInteractingNPC() {
		if(getMyPlayer().getInteracting() instanceof RSNPC){
			return (RSNPC) getMyPlayer().getInteracting();
		}
		return null;
	}

	private int attackRevs() {
		RSNPC revs = null;
		if (r > 13 && revLvl == 4)
			r = 0;
		if (r > 11 && revLvl == 3)
			r = 0;
		if (r > 9 && revLvl == 2)
			r = 0;
		if (r > 5 && revLvl == 1)
			r = 0;

		if (revLvl == 1) {
			revs = npcs.getNearest(easyRevs[r]);
		}
		if (revLvl == 2) {
			revs = npcs.getNearest(medRevs[r]);
		}
		if (revLvl == 3) {
			revs = npcs.getNearest(hardRevs[r]);
		}
		if (revLvl == 4) {
			revs = npcs.getNearest(extremeRevs[r]);
		}
		if (revs != null && getInteractingNPC() == null) {
			if (revs.isDead() == false && groundItems.getNearest(LootID) == null){
				if (revs.isOnScreen()) {
					if (random(0, 10) == 5) {
						camera.turnTo(revs.getLocation().randomize(-20, 10));
					}
					if (killsteal) {
						if (!revs.isInteractingWithLocalPlayer() && !isNpcCombat()) {
							attack(revs, "Attack " + revs.getName());
						}
					}
					if (!killsteal) {
						if (!revs.isInteractingWithLocalPlayer() && !revs.isInCombat() && !isNpcCombat()) {
							attack(revs, "Attack " + revs.getName());
						}
					}
				}
				if (killsteal) {
					if (!revs.isOnScreen()) {
						walking.walkTileMM(revs.getLocation());
					}
				}
				if (!killsteal) {
					if (!revs.isOnScreen() && !revs.isInCombat()) {
						walking.walkTileMM(revs.getLocation());
					}
				}
			}
		} else {
			r++;
		}
		if(getInteractingNPC() != null && currNPC == null){
			currNPC = getInteractingNPC();
		}
		if(currNPC != null && getInteractingNPC() == null){
			kills++;
			r++;
			currNPC = null;
		}
		return random(100, 150);
	}

	private boolean isNpcCombat() {
		return getMyPlayer().getInteracting() instanceof RSNPC;
	}

	private void gloryHandler() {
		if(banking == 2 && !teleblocked){
			if(revArea()) {
				if(combat.getWildernessLevel() <= 30) {
					if(game.getTab() != Game.Tab.EQUIPMENT) {
						game.openTab(Game.Tab.EQUIPMENT);
					}
					if(equipment.getItem(Equipment.NECK) != null) {
						// full charge
						if(equipment.getItem(Equipment.NECK).getID() == 1710) {
							if(equipment.getItem(Equipment.NECK).interact("Edgeville")){
								sleep(2000, 3000);
								game.openTab(Game.Tab.INVENTORY);
							}
						}
						// 3 charges
						if(equipment.getItem(Equipment.NECK).getID() == 1712) {
							if(equipment.getItem(Equipment.NECK).interact("Edgeville")){
								sleep(2000, 3000);
								game.openTab(Game.Tab.INVENTORY);
							}
						}
						// 2 charges
						if(equipment.getItem(Equipment.NECK).getID() == 1708) {
							if(equipment.getItem(Equipment.NECK).interact("Edgeville")){
								sleep(2000, 3000);
								game.openTab(Game.Tab.INVENTORY);
							}
						}
						// 1 Charge
						if(equipment.getItem(Equipment.NECK).getID() == 1706) {
							if(equipment.getItem(Equipment.NECK).interact("Edgeville")){
								sleep(2500, 3000);
								equipment.getItem(1704).doClick(true);
								log.warning("Glory is out of charges, getting a new one");
								game.openTab(Game.Tab.INVENTORY);
							}
						}
						// No Charge
						if(equipment.getItem(Equipment.NECK).getID() == 1704) {
							log.warning("Glory is out of charges. Walking back.");
							if(equipment.getItem(1704).doClick(true)){
								game.openTab(Game.Tab.INVENTORY);
								walkToExit = true;
							}
						}
					}
				}
			}
		}
	}

	private int bank() {
		RSNPC banker = npcs.getNearest("Banker");
		if (banker != null && !bank.isOpen()) {
			if(banker.interact("Bank Banker")){
				sleep(500, 700);
			}
		}
		if (bank.isOpen()) {
			sleep(random(500, 800));
			if (banking == 2 && (inventory.getItem(1704) != null || equipment.getItem(Equipment.NECK).getID() == 1704) && bank.getItem(glory[0]) == null) {
				needToRech = true;
			}
			if (inventory.containsOneOf(LootID)) {
				bank.depositAll();
				sleep(random(500, 800));
				return 0;
			}else {
				if (inventory.isFull()) {
					if(bank.depositAll()){
						sleep(500, 800);
						return 0;
					}
				}
				reEquip();
				if(needToRech && bank.getCount(1704) >= 1){
					bank.withdraw(1704, bank.getCount(1704));
					bank.close();
				}
				if(banking == 2 && inventory.getItem(glory) != null){
					for (int i = 0; i < glory.length; i++) {
						if(inventory.getItem(glory[i]) != null){
							bank.deposit(glory[i], 0);
						}
					}
				}
				if (banking == 2 && (equipment.getItem(Equipment.NECK).getID() == 0 || equipment.getItem(Equipment.NECK).getID() == 1704)) {
					int glory = 1710;
					bank.withdraw(glory, 1);
					sleep(500);
					RSItem ammy = inventory.getItem(glory);
					ammy.interact("Wear");
					bank.deposit(1704, 1);
				}
				if(usePots){
					if (bank.getCount(9739) != 0 && potions == 1) {
						if(bank.withdraw(9739, 1)){
							sleep(1200, 1500);
						}
					}
					if (bank.getCount(supers) != 0 && potions == 2) {
						if(bank.withdraw(supers[0], 1)){
							sleep(1200, 1500);
						}
						if(bank.withdraw(supers[1], 1)){
							sleep(1200, 1500);
						}
						if(bank.withdraw(supers[2], 1)){
							sleep(1200, 1500);
						}
					}
				}
				if (bank.getCount(foodID) != 0) {
					if(bank.withdraw(foodID, foodAmt)){
						sleep(1200, 1500);
					}
				}
				if(bank.getCount(braces) != 0 && !inventory.containsOneOf(braces)) {
					if (bank.getCount(11095) > 0) {
						bank.withdraw(11095, 1);
					}
					if (bank.getCount(11095) == 0 && bank.getCount(11097) > 0) {
						bank.withdraw(11097, 1);
					}
					if (bank.getCount(11095) == 0 && bank.getCount(11097) == 0 && bank.getCount(11099) > 0) {
						bank.withdraw(11099, 1);
					}
					if (bank.getCount(11095) == 0 && bank.getCount(11097) == 0 && bank.getCount(11099) == 0 && bank.getCount(11101) > 0) {
						bank.withdraw(11101, 1);
					}
					if (bank.getCount(11095) == 0 && bank.getCount(11097) == 0 && bank.getCount(11099) == 0 && bank.getCount(11101) == 0 && bank.getCount(11103) > 0) {
						bank.withdraw(11103, 1);
					}
				}
				currentLoot = 0;
				bank.close();
				needToBank = false;
			}
		}
		return random(200, 400);
	}

	private boolean isInCombat() {
		return getMyPlayer().getInteracting() instanceof RSPlayer;
	}

	private int Assess() {
		int threatLvl = 0;
		if (isInCombat()) {
			threatLvl = 3;
		}
		return threatLvl;
	}

	private int randWorld() {
		ArrayList<String> w = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new URL("http://services.runescape.com/m=rswiki/en/Server_and_Game_Worlds").openStream()));
			String line = reader.readLine();

			String all = "";

			while (line != null) {
				all += line;
				line = reader.readLine();
			}

			final Pattern pattern = Pattern
					.compile("<td class=\"m\">(.+?)</td>");
			final Matcher matcher = pattern.matcher(all);
			while (matcher.find()) {
				String s = "";
				try {
					s = (matcher.group(1));
				} catch (Exception e) {
				}
				if (s.equals("Members")) {
					continue;
				}
				s = s.split("World")[1];
				s = s.split("</a>")[0];
				int i = Integer.parseInt(s.replace(" ", ""));
				w.add("" + i);
				// log(s);
			}
			w.remove("137");
			w.remove("124");
			w.remove("114");
			w.remove("86");
			w.remove("65");
			w.remove("23");
		} catch (Exception e) {
			log.severe("Failed to load worlds");
			log(e.getMessage());
			return 160;
		}
		return Integer.parseInt(w.get((int) (Math.random() * w.size())));
	}

	private void sworlds(final int world) {
		try {
			RSComponent t = null;
			for (int i = 0; i <= 10 && game.isLoggedIn(); i++) {
				game.openTab(Game.Tab.LOGOUT);
				game.logout(true);
				sleep(1000);
			}
			RSComponent w = interfaces.get(906).getComponent(22);
			if (w == null) {
				log("Failed to find login button.");
				return;
			}
			while (!w.doClick())
				sleep(5000);
			while (interfaces.get(910).getComponent(77) == null) {
				w.doClick();
				sleep(1000);
				log("Null.");
			}
			for (RSComponent c : interfaces.get(910).getComponent(77).getComponents()) {
				if (c.getText().equals("World " + world)) {
					// log("Located: "+c.getItemName());
					t = c;
					break;
				}
			}
			// Component: 86; Child: 1
			// 5 for max Y
			if (t == null) {
				log(java.awt.Color.RED, "Error finding component.");
				return;
			}
			while (t.getCenter().getY() > interfaces.get(910).getComponent(86).getComponent(5).getCenter().getY()) {
				RSComponent s = interfaces.get(910).getComponent(86).getComponent(1);
				mouse.hop(s.getCenter());
				mouse.drag(new java.awt.Point((int) s.getCenter().getX(),
						(int) s.getCenter().getY() + 20));
				sleep(100);
				// mouse.release();
			}

			t.doClick();
			sleep(1000);
			if (interfaces.get(906).getComponent(185).doClick()) {
				log(java.awt.Color.GREEN, "Hopped sucessfully!");
			}
			for (int i = 0; i <= 35 && interfaces.get(906).getComponent(29).isValid(); i++)
				Thread.sleep(1000);
			env.enableRandoms();
			timesKilled = 0;
			return;
		} catch (Exception e) {
			log(java.awt.Color.RED, "Failed to hop worlds!");
			interfaces.get(906).getComponent(185).doClick();
			return;
		}
	}

	public boolean canPot(){
		return revArea() && !walkToRevs && !needToBank && usePots;

	}

	public void drinkPotion(){
		int combPots[] = {9739, 9741, 9743, 9745};
		int atkPots[] = {2436, 145, 147, 149};
		int strPots[] = {2440, 157, 159, 161};
		int defPots[] = {2442, 163, 165, 167};

		int RealAttack = skills.getRealLevel(Skills.ATTACK);
		int RealStrength = skills.getRealLevel(Skills.STRENGTH);
		int RealDefence = skills.getRealLevel(Skills.DEFENSE);

		if(potions == 1){
			if(inventory.containsOneOf(combPots)){
				if ((skills.getCurrentLevel(Skills.STRENGTH) <= RealStrength + 2)) {
					if (inventory.getItem(combPots) != null) {
						if(inventory.getItem(combPots).interact("Drink")){
							sleep(550, 700);
						}
					}
				}
			}
		}
		if (potions == 2) {
			if(inventory.containsOneOf(atkPots)){
				if ((skills.getCurrentLevel(Skills.ATTACK) <= RealAttack * 1.1)) {
					if (inventory.getItem(atkPots) != null) {
						if(inventory.getItem(atkPots).interact("Drink")){
							sleep(550, 700);
						}
					}
				}
			}

			if(inventory.containsOneOf(strPots)){
				if ((skills.getCurrentLevel(Skills.STRENGTH) <= RealStrength * 1.1)) {
					if (inventory.getItem(strPots) != null) {
						if(inventory.getItem(strPots).interact("Drink")){
							sleep(550, 700);
						}
					}
				}
			}

			if(inventory.containsOneOf(defPots)){
				if ((skills.getCurrentLevel(Skills.DEFENSE) <= RealDefence * 1.1)) {
					if (inventory.getItem(defPots) != null) {
						if(inventory.getItem(defPots).interact("Drink")){
							sleep(550, 700);
						}
					}
				}
			}
		}
	}

	public void reEquip() {
		if(!bank.isOpen()){
			for (int i = 0; i < equip.length; i++) {
				if (equip[i] != null) {
					if (inventory.containsOneOf(equip[i].getID())) {
						mouse.move(inventory.getItem(equip[i].getID()).getComponent().getLocation());
						mouse.click(true);
						sleep(200, 350);
					}
				}
			}
		}
		if(bank.isOpen()){
			for (int i = 0; i < equip.length; i++) {
				if (equip[i].getID() != 0) {
					if (inventory.containsOneOf(equip[i].getID())) {
						if (i == 3) {
							mouse.move(inventory.getItem(equip[i].getID()).getComponent().getLocation());
							RSItem equips = inventory.getItem(equip[i].getID());
							equips.interact("Wield");
							sleep(300, 500);
						} else {
							mouse.move(inventory.getItem(equip[i].getID()).getComponent().getLocation());
							RSItem equips = inventory.getItem(equip[i].getID());
							equips.interact("Wear");
							sleep(300, 500);
						}
					}
				}
			}
		}
	}

	public void applyRepel() {
		game.openTab(Game.Tab.INVENTORY);
		if(inventory.getItem(braces) != null) {
			if(inventory.getItem(braces).interact("Repel")){
				repelled = true;
			}
		}
		if (!inventory.containsOneOf(braces)	
				&& !needToBank) {
			log("Out of braces. Headed to the bank.");
			needToBank = true;
		}
	}

	public boolean failSafe(){
		if (shutdown) {
			log.severe("Failed to move/attack for 13 minutes. Shutting down.");
			runThread = false;
			return true;
		}
		if (itemShutdown) {
			log.severe("Out of an essential item (Food and/or Braces), shutting down");
			runThread = false;
			return true;
		}
		return false;
	}

	private Image getImage(final String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}

	public int interactObj(final int tempObject, final String tempAction) {
		final String tempAction2 = tempAction;
		RSObject tempObjectz = objects.getNearest(tempObject);
		for (int i = 0; i < 7; i++) {
			if (isPartiallyOnScreen(tempObjectz.getModel())) {
				Point p = useCentralClicking ? getCentralPoint(tempObjectz.getModel()) : getPointOnScreen(tempObjectz.getModel(), false);
				if (p == null || !calc.pointOnScreen(p)) {
					continue;
				}
				mouse.move(p, useCentralClicking ? 3 : 0,
						useCentralClicking ? 3 : 0);
				String[] items = menu.getItems();
				if (items.length > 0 && items[0].contains(tempAction)) {
					mouse.click(true);
					return 0;
				} else if (items.length > 0 && items[0].contains(tempAction2)) {
					mouse.click(true);
					return 0;
				} else if (menu.contains(tempAction)
						|| menu.contains(tempAction2)) {
					mouse.click(false);
					sleep(random(100, 200));
					for (int x = 0; x < 4; x++) {
						if (!menu.contains(tempAction)
								&& !menu.contains(tempAction2)) {
							sleep(10, 420);
							mouse.moveSlightly();
							break;
						}
						if (menu.doAction(tempAction)
								|| menu.doAction(tempAction2)) {
							sleep(400, 500);
							return 0;
						}
					}
				}

			} else {
				int angle = camera.getObjectAngle(tempObjectz);
				if (calc.distanceTo(tempObjectz) < 8
						&& Math.abs(angle - camera.getAngle()) > 20) {
					camera.setAngle(angle + random(-20, 20));
				}
			}
		}
		return -1;
	}

	public boolean interactLoot(final RSGroundItem grounditem, final String action) {
		Point p = grounditem.getPoint();
		mouse.hop(p);
		if (menu.contains(grounditem.getItem().getName())
				&& !menu.contains(action)) {
			return false;
		} else {
			if (menu.isOpen() && !menu.contains(action)) {
				if (menu.doAction("Cancel")) {
					Timer j = new Timer(1200);
					while (j.isRunning() && menu.isOpen()
							&& !menu.contains(action)) {
						sleep(10, 20);
					}
				}
			} else {
				if (menu.getIndex(action) != 0) {
					mouse.click(false);
					return menu.contains(action) && menu.doAction(action);
				} else {
					if (menu.getIndex(action) == 0) {
						return menu.contains(action) && menu.doAction(action);
					}
				}
			}
		}
		return false;
	}

	private Point getCentralPoint(final RSModel m) {
		if (m == null) {
			return null;
		}
		try {
			int x = 0, y = 0, total = 0;
			for (Polygon poly : m.getTriangles()) {
				for (int i = 0; i < poly.npoints; i++) {
					x += poly.xpoints[i];
					y += poly.ypoints[i];
					total++;
				}
			}
			Point central = new Point(x / total, y / total);
			Point curCentral = null;
			double dist = 20000;
			for (Polygon poly : m.getTriangles()) {
				for (int i = 0; i < poly.npoints; i++) {
					Point p = new Point(poly.xpoints[i], poly.ypoints[i]);
					if (!calc.pointOnScreen(p)) {
						continue;
					}
					double dist2 = distanceBetween(central, p);
					if (curCentral == null || dist2 < dist) {
						curCentral = p;
						dist = dist2;
					}
				}
			}
			return curCentral;
		} catch (Exception e) {
		}
		return null;
	}

	private static double distanceBetween(final Point p1, final Point p2) {
		return Math.sqrt(((p1.x - p2.x) * (p1.x - p2.x))
				+ ((p1.y - p2.y) * (p1.y - p2.y)));
	}

	private boolean isPartiallyOnScreen(final RSModel m) {
		return getPointOnScreen(m, true) != null;
	}

	private Point getPointOnScreen(final RSModel m, final boolean first) {
		if (m == null) {
			return null;
		}
		ArrayList<Point> list = new ArrayList<Point>();
		try {
			Polygon[] tris = m.getTriangles();
			for (int i = 0; i < tris.length; i++) {
				Polygon p = tris[i];
				for (int j = 0; j < p.xpoints.length; j++) {
					Point pt = new Point(p.xpoints[j], p.ypoints[j]);
					if (calc.pointOnScreen(pt)) {
						if (first) {
							return pt;
						}
						list.add(pt);

					}
				}
			}
		} catch (Exception e) {
		}
		return list.size() > 0 ? list.get(random(0, list.size())) : null;
	}

	public void GUI(){
		try{
			frame = new JFrame("ThunderRevenants");
			frame.setSize(600, 190);
			JTabbedPane tabs = new JTabbedPane();
			JPanel MAIN = new JPanel();

			JButton startButton = new JButton("Start");
			startButton.addActionListener(this);

			MAIN.add(startButton);

			JPanel panelBank = new JPanel();
			JLabel lblMethod = new JLabel("Method: ");
			panelBank.add(lblMethod);
			String[] bankOptions = { "Walk", "Glory" };
			bankingSelect = new JComboBox(bankOptions);
			bankingSelect.setSelectedIndex(0);
			panelBank.add(bankingSelect);
			JLabel lblLootAmt = new JLabel("Loot Value Before Banking: ");
			panelBank.add(lblLootAmt);
			txtLootAmt = new JTextField("140000");
			panelBank.add(txtLootAmt);
			tabs.add("Banking", panelBank);

			JPanel panelFood = new JPanel();
			JLabel lblFood = new JLabel("FoodID: ");
			panelFood.add(lblFood);
			txtFoodID = new JTextField("7946");
			panelFood.add(txtFoodID);
			JLabel lblFoodAmt = new JLabel("Food Amount: ");
			panelFood.add(lblFoodAmt);
			txtFoodAmt = new JTextField("10");
			panelFood.add(txtFoodAmt);
			tabs.add("Food", panelFood);

			JPanel panelPotion = new JPanel();
			chkPots = new JCheckBox("Use Potions");
			chkPots.setSelected(false);
			panelPotion.add(chkPots);
			JLabel lblPot = new JLabel("Potion: ");
			panelPotion.add(lblPot);
			String[] potOptions = { "Combat", "Supers" };
			potSelect = new JComboBox(potOptions);
			potSelect.setSelectedIndex(0);
			panelPotion.add(potSelect);
			tabs.add("Potions", panelPotion);

			JPanel panelCombat = new JPanel();
			JLabel lblDiff = new JLabel("Difficulty: ");
			panelCombat.add(lblDiff);
			String[] diffOptions = { "Easy", "Medium", "Hard", "Extreme" };
			diffSelect = new JComboBox(diffOptions);
			diffSelect.setSelectedIndex(0);
			panelCombat.add(diffSelect);
			chkKillsteal = new JCheckBox("Killstealing");
			chkKillsteal.setSelected(false);
			panelCombat.add(chkKillsteal);
			tabs.add("Combat", panelCombat);

			JPanel panelAntiban = new JPanel();
			chkSwitch = new JCheckBox("World switching");
			chkSwitch.setSelected(false);
			panelAntiban.add(chkSwitch);
			JLabel lblSwitch = new JLabel("Deaths to switch after: ");
			panelAntiban.add(lblSwitch);
			txtSwitch = new JTextField("2");
			panelAntiban.add(txtSwitch);
			tabs.add("Antiban", panelAntiban);

			frame.setResizable(false);
			MAIN.add(tabs);
			frame.add(MAIN);
		}catch(Exception e){
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.setVisible(false);
		usePots = chkPots.isSelected();
		killsteal = chkKillsteal.isSelected();
		worldHop = chkSwitch.isSelected();
		valBank = Integer.parseInt(txtLootAmt.getText());
		foodID = Integer.parseInt(txtFoodID.getText());
		foodAmt = Integer.parseInt(txtFoodAmt.getText());
		switchAfter = Integer.parseInt(txtSwitch.getText());

		final String bankingMethod = (String) bankingSelect.getSelectedItem();
		if (bankingMethod.contains("alk")) {
			banking = 1;
		}
		if (bankingMethod.contains("lory")) {
			banking = 2;
		}

		final String difficulty = (String) diffSelect.getSelectedItem();
		if (difficulty.contains("asy")) {
			revLvl = 1;
		}
		if (difficulty.contains("edium")) {
			revLvl = 2;
		}
		if (difficulty.contains("ard")) {
			revLvl = 3;
		}
		if (difficulty.contains("xtreme")) {
			revLvl = 4;
		}

		final String potMethod = (String) potSelect.getSelectedItem();
		if (potMethod.contains("ombat")) {
			potions = 1;
		}
		if (potMethod.contains("upers")) {
			potions = 2;
		}
	}

	public void mouseClicked(MouseEvent e) {
		Point n = e.getPoint();
		if (hideButton.contains(n)) {
			if (show) {
				show = false;
			}else {
				show = true;
			}
		}
		if (debugButton.contains(n)) {
			if (debug) {
				debug = false;
			}else {
				debug = true;
			}
		}
	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void startThread() {
		new Thread() {
			public void run() {
				log("AntiBan started!");
				while (runThread) {
					if (game.isLoggedIn()) {
						if (lastXp == skills.getCurrentExp(SKILL)) {
							timeElapsed += SLEEP;
						} else {
							timeElapsed = 0;
						}
						if (lastXp == skills.getCurrentExp(Skills.CONSTITUTION) && timeElapsed >= TIME_TO_STOP * 60 * 1000) {
							shutdown = true;
						}
						lastXp = skills.getCurrentExp(Skills.CONSTITUTION);
					}try {
						Thread.sleep(SLEEP);
					} catch (Exception ignored) {
					}
				}
			}
		}.start();
	}
}
