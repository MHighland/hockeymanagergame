
public class Skater extends Player {

	/*
	int range = (max - min) + 1;     
	   return (int)(Math.random() * range) + min;
	*/
	
	//Record Keeping
	String[][] seasonStats = new String[40][7];
	String[][] playoffStats = new String[40][7];
	
	/*TODO Add some of these stats
	 * int[] plusMinus = new int[2], shotsOnGoal = new int[2], hits = new int[2];
	double[] shootingPercentage = new double[2];
	 * shotsOnGoal
	 * takeaways
	 * giveaways
	 * blocked shots
	GWG - Game-Winning Goals
	PPG - Power Play Goals	
	PPA - Power Play Assists
	SHG - Short-Handed Goals	
	SHA - Short-Handed Assists
	FOW - Faceoffs Won
	FOL - Faceoffs Lost
	PCT - Faceoff Percentage
	TOI - Total Time on Ice
	ATOI - Average Time on Ice
	PPTOI - Power Play Time on Ice
	SHTOI - Short-Handed Time on Ice
	*/

	/*Overall Attributes
	 * Wristshot - Ability to perform a wristshot, dictates how often a player misses net / hits net when taking a shot, dictates likelyhood of scoring via wristshot
	 * Slapshot - Ability to perform a slapshot, dictates how often a player misses net / hits net when taking a shot, dictates likelyhood of scoring via slapshot
	 * Puckcontrol - Ability to hold onto the puck, dictates how susceptible player is to stickchecks
	 * HandEye - Ability to receive passes & pick up loose pucks
	 * Stickhandling - Ability to maneuver the stick while holding the puck
	 * Stickchecking - Ability to perform stick lifts & stick checks
	 * Passing - Ability to make accurate passes
	 * Faceoffs - Ability to win faceoffs
	 * HockeyIQ - Ability to read the game, dictates what action player decides to take, dictates postioning of player
	 * Discipline - How often a player takes penalties
	 * Balance - Ability to withstand hits
	 * Speed - How fast a player skates
	 * Durability - How often a player is injured
	 * Strength - Affects how effective performing a hit is, figthing ability
	 * Stamina - How long a player can be on ice before being affected by Fatigue
	 * Aggressiveness - Dictates how often a player hits & fights
	 * Shotblocking - Ability to block shots from hitting the net
	 */

	//Store Attributes in array after creation?
	//Attributes 
	int wristshot, slapshot, puckcontrol, handeye, stickhandling, stickchecking, passing, faceoffs, hockeyIQ, balance, speed, stamina, durability, strength, discipline,
	aggressiveness, shotblocking;
			
	public Skater(int positionNumb, int teamNumb, int leagueNumb) {
		
		super(positionNumb, teamNumb, leagueNumb);
		
		seasonStats[0][0] = "Season";
		seasonStats[0][1] = "Team";
		seasonStats[0][2] = "GP";
		seasonStats[0][3] = "G";
		seasonStats[0][4] = "A";
		seasonStats[0][5] = "P";
		seasonStats[0][6] = "PIM";
		
		playoffStats[0][0] = "Post Season";
		playoffStats[0][1] = "Team";
		playoffStats[0][2] = "GP";
		playoffStats[0][3] = "G";
		playoffStats[0][4] = "A";
		playoffStats[0][5] = "P";
		playoffStats[0][6] = "PIM";
		
		wristshot = (int)(Math.random()*100)+1;
		slapshot = (int)(Math.random()*100)+1;
		puckcontrol = (int)(Math.random()*100)+1;
		handeye = (int)(Math.random()*100)+1;
		stickhandling = (int)(Math.random()*100)+1;
		stickchecking = (int)(Math.random()*100)+1;
		passing = (int)(Math.random()*100)+1;
		faceoffs = (int)(Math.random()*100)+1;
		hockeyIQ = (int)(Math.random()*100)+1;
		balance = (int)(Math.random()*100)+1;
		speed = (int)(Math.random()*100)+1;
		stamina = (int)(Math.random()*100)+1;
		durability = (int)(Math.random()*100)+1;
		strength = (int)(Math.random()*100)+1;
		discipline = (int)(Math.random()*100)+1;
		aggressiveness = (int)(Math.random()*100)+1;
		shotblocking = (int)(Math.random()*100)+1;
		
		 rating = (wristshot+ slapshot+ puckcontrol+ handeye+ stickhandling+ stickchecking+ passing+ faceoffs+ hockeyIQ+ balance+ speed+ stamina+ durability+ strength+ discipline+
			aggressiveness+ shotblocking)/17;
					
		 if (leagueNumb != 4) {
			 
			 team = Main.AllTeams[leagueNumb][teamNumb];
			 
		 }
		
		//North American League
		if (leagueNumb == 0) {

			signedToMainTeam = true;

		}
		
		//European League
		else if (leagueNumb == 1) {

			
		}
		
		//Minor League
		else if (leagueNumb == 2) {
		
			if (age < 26) {
			
				signedToMainTeam = true;
				
			}
			
			
		}
		
		//Prospect League
		else if (leagueNumb == 3) {
			
			age = (int)(Math.random()*5)+16;

			//TODO add method to generate prospects & their ratings
		}
		
		//Free Agents
		else if (leagueNumb == 4) {

			team = null;
			
			
		}
			
	}
	

	
	public void gameUpdate() {
		
		
		
	}
	
}
