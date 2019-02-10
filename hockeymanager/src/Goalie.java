
public class Goalie extends Player {
	
	String[][] seasonStats = new String[40][11];
	String[][] playoffStats = new String[40][11];
	
	//Stats
	int[] wins = new int[2], losses = new int[2], OTL = new int[2], shutouts = new int[2];

	/*TODO Add these some of these stats
	 shots Against
	 saves
	 save percentage
	 GAA
	 GS - Games Started
	 W% - Win Percentage
	 GA - Goals Against
	 ENGA - Empty Net Goals Against
	 PPGA - Powerplay Goals Against
	 SHGA - Short-Handed Goals Against
	 TOGV - Turnovers/Giveaways	
	 TOI - Time on Ice
	 TOTA - Turnovers/Takeaways	
	 TOIS - Average Time on Ice in Seconds
	 */
	
	
	/*
	 * GloveSide - Ability to make glove side saves
	 * BlockerSide - Ability to make blocker side saves
	 * Stick - Ability to pokecheck
	 * Passing - Ability to pass puck
	 */
	
	//Attributes
	int gloveSide, blockerSide, stick, passing;


	public Goalie(int teamNumb, int leagueNumb) {
		
		super(4, teamNumb, leagueNumb);
		
		seasonStats[0][0] = "Season";
		seasonStats[0][1] = "Team";
		seasonStats[0][2] = "GP";
		seasonStats[0][3] = "W";
		seasonStats[0][4] = "L";
		seasonStats[0][5] = "OTL";
		seasonStats[0][6] = "SO";
		seasonStats[0][7] = "G";
		seasonStats[0][8] = "A";
		seasonStats[0][9] = "P";
		seasonStats[0][10] = "PIM";
		
		playoffStats[0][0] = "Post Season";
		playoffStats[0][1] = "Team";
		playoffStats[0][2] = "GP";
		playoffStats[0][3] = "W";
		playoffStats[0][4] = "L";
		playoffStats[0][5] = "OTL";
		playoffStats[0][6] = "SO";
		playoffStats[0][7] = "G";
		playoffStats[0][8] = "A";
		playoffStats[0][9] = "P";
		playoffStats[0][10] = "PIM";

		gloveSide = (int)(Math.random()*100)+1;
		blockerSide = (int)(Math.random()*100)+1;
		stick = (int)(Math.random()*100)+1;
		passing = (int)(Math.random()*100)+1;
		
		rating = (gloveSide + blockerSide + stick + passing) /4;
		
		if (leagueNumb != 4) {
			 
			 team = Main.AllTeams[leagueNumb][teamNumb];
			 
		 }
		
		//North American League
		if (leagueNumb == 0) {

		}
		
		//European League
		else if (leagueNumb == 1) {


		}
		
		//Minor League
		else if (leagueNumb == 2) {

		}
		
		//Prospect League
		else if (leagueNumb == 3) {
			
			age = (int)(Math.random()*5)+16;

		}
		
		//Free Agents
		else if (leagueNumb == 4) {

			team = null;
			
		}
		

	}


	public int getWins() {
		return wins[Main.currentS];
	}


	public int getLosses() {
		return losses[Main.currentS];
	}


	public int getOTL() {
		return OTL[Main.currentS];
	}


	public int getShutouts() {
		return shutouts[Main.currentS];
	}

}
