

public class Game {

	/*
	 * Gametime Attributes

	 Fatigue - As fatigue increases, player attributes temporarily decrease for durtion of game

   	 Health - If player is suffering from injury, lowers attributes for game
   	 
	 Injury Severity Level

	 Home Ice Advantage, Buff Faceoff stat & Discipline

	 */

	Skater puckholder;
	
	//0 = Home, 1 = Away
	Team[] Teams = new Team[2];
	int[] teamGoals = new int[2];
	
	//0 = Away, 1 = Home
	Goalie[] Goalies = new Goalie[2];
	
	public Game(Team HomeTeam, Team AwayTeam) {
		
		teamGoals[0] = 0;
		teamGoals[1] = 0;

		Teams[0] = HomeTeam;
		Teams[1] = AwayTeam;
		
		boolean temp;

		int chance;
		
		int u = 1;
		
		//Choose which Goalie from each team Tandem is their Starter;
		for (int x = 0; x < Teams.length; x++) {
	
			if (x == 0) {
				u = 1;
			}
			if (x == 1) {
				
				u = 0;
				
			}
			
			chance = (int)(Math.random()*100);
			
			if (chance < 70) {
				
				Goalies[x] = Teams[u].Tandem[0];
				
			}
			
			else {
				
				Goalies[x] = Teams[u].Tandem[1];
			}
			
			
		}
		
		

		//List of stats
		
		//Skater
		//int[] goals = new int[2], assists  = new int[2], points  = new int[2], gamesPlayed = new int[2], PIM = new int[2];
		
		
		//Goalie
		//int[] goals = new int[2], assists  = new int[2], points  = new int[2], gamesPlayed = new int[2], PIM = new int[2];
		//int[] wins = new int[2], losses = new int[2], OTL = new int[2], shutouts = new int[2];
		
		//Shot Attempts
		for (int x = 0; x < Teams.length; x++) {
			
			for (int y = 0; y < (int)(Math.random()*11)+25; y++) {
				
				chance = (int)(Math.random()*100) + 1;
				
				//Goalie Shot On Goal
				if (chance == 1) {
					
					
				}
				
				//D Shot on Goal
				else if (chance > 1 && chance < 35) {
					
					temp = shoot(Teams[x].LinesD[(int)(Math.random()*6)], Goalies[x]);
					
					if (temp == true) {
						
						teamGoals[x]++;
						
					}
					
					
				}
				
				//F Shot on Goal
				else {
					
					temp = shoot(Teams[x].LinesF[(int)(Math.random()*12)], Goalies[x]);

					if (temp == true) {

						teamGoals[x]++;

					}


					
				}
				
			}
			
			
		}


		
		//Increase games played, total points
		for (int x = 0; x < Teams.length; x++) {

			//Forwards
			for (int y = 0; y < Teams[0].LinesF.length; y++) {

				Teams[x].LinesF[y].gamesPlayed[Main.currentS]++;
				Teams[x].LinesF[y].points[Main.currentS] = Teams[x].LinesF[y].goals[Main.currentS] + Teams[x].LinesF[y].assists[Main.currentS];
				
			}

			//Defencemen
			for (int y = 0; y < Teams[0].LinesD.length; y++) {

				Teams[x].LinesD[y].gamesPlayed[Main.currentS]++;
				Teams[x].LinesD[y].points[Main.currentS] = Teams[x].LinesD[y].goals[Main.currentS] + Teams[x].LinesD[y].assists[Main.currentS];
				

			}

			Goalies[x].gamesPlayed[Main.currentS]++;
			//Goalies[y].points[Main.currentS] = Goalies[y].goals[Main.currentS] + Goalies[y].assists[Main.currentS];


		}
		
		
		if (teamGoals[0] > teamGoals[1]) {
			
			Teams[0].Wins++;
			
			Teams[1].Losses++;
			
			Goalies[1].wins[Main.currentS]++;
			
			Goalies[0].losses[Main.currentS]++;
			
			
			
		}
		
		else if (teamGoals[0] < teamGoals[1]) {
			
			Teams[1].Wins++;
			
			Teams[0].Losses++;
			
			Goalies[0].wins[Main.currentS]++;
			
			Goalies[1].losses[Main.currentS]++;
			
			
		}
	
		else if (teamGoals[0] == teamGoals[1]) {
		
			chance = (int)(Math.random()*2);
			
			if (chance == 0) {
				
				Teams[0].Wins++;
				
				Teams[1].OTLosses++;
				

				Goalies[1].wins[Main.currentS]++;
				
				Goalies[0].OTL[Main.currentS]++;
				
				
			}
			
			else if (chance == 1) {
				
				Teams[1].Wins++;
				
				Teams[0].OTLosses++;
				
				Goalies[0].wins[Main.currentS]++;
				
				Goalies[1].OTL[Main.currentS]++;
				
				
			}
			
			
		}
		
	}
	
	boolean shoot (Skater Shooter, Goalie Goalie) {
		
		int roll = (int)(Math.random()*(Shooter.wristshot + Goalie.gloveSide + Goalie.blockerSide));
		
		//Score
		if (roll <= Shooter.wristshot) {
			
			Shooter.goals[Main.currentS]++;
			
			return true;
			
		}
		
		//No score
		else {
		
			return false;
		}		
		
	}
	

}
