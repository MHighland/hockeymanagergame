import java.util.Calendar;

import javax.swing.JOptionPane;

import javafx.geometry.Pos;

public class Season  {

	//TODO Universal counter for scheduleCalendar&TeamMatchup to include all games played instead of clearing after each segment?
	
	int League;
	
	Team[][] TeamMatchup;
	
	Calendar[] scheduleCalendar;

	//Playoff Seeding
	Team[] EConf, WConf, ESeed, WSeed, EQuarters = new Team[4], ESemis = new Team[2], WQuarters = new Team[4], WSemis = new Team[2], Finals = new Team[2];
	Team Winner;
	
	int roundNumb, playoffWinCounter;
	
	public void main() {

		//Get Date Text
		Main.date.setText(Main.gameCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Main.locale) + " " + Main.gameCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Main.locale) 
		+ " " + Main.gameCalendar.get(Calendar.DAY_OF_MONTH) + Main.suffixes[Main.gameCalendar.get(Calendar.DATE)] + " "	+ Main.gameCalendar.get(Calendar.YEAR));
		Main.date.setAlignment(Pos.TOP_RIGHT);

		//Preseason
		if (Main.gameCalendar.get(Calendar.MONTH) == Calendar.SEPTEMBER) {
			
			//Season text
			Main.season.setText(Main.gameCalendar.get(Calendar.YEAR) + "-" + (Main.gameCalendar.get(Calendar.YEAR)+1));
			Main.seasonType.setText("Preseason");

			//Reset Team Records for Regular Season
			if (Main.gameCalendar.get(Calendar.DATE) >= 28) {

				Main.season.setText(Main.gameCalendar.get(Calendar.YEAR) + "-" + (Main.gameCalendar.get(Calendar.YEAR)+1));
				Main.seasonType.setText("Season");

				if (Main.gameCalendar.get(Calendar.DATE) == 28) {

					//Move to method
					//Reset clear all skater/goalie stats for new season
					for (int x = 0; x < Main.AllTeams.length; x++) {
						
						for (int y = 0; y < Main.AllTeams[x].length; y++) {
						
							for (int z = 0; z < Main.AllTeams[x][y].Roster.length; z++) {

								Main.AllTeams[x][y].Roster[z].goals[0] = 0; 
								Main.AllTeams[x][y].Roster[z].assists[0] = 0; 
								Main.AllTeams[x][y].Roster[z].points[0] = 0; 
								Main.AllTeams[x][y].Roster[z].gamesPlayed[0] = 0;
								Main.AllTeams[x][y].Roster[z].PIM[0] = 0;

							}
							
							for (int z = 0; z < Main.AllTeams[x][y].RosterG.length; z++) {

								if (Main.AllTeams[x][y].RosterG[z] == null) {
									
									continue;
									
								}
								
								else {
								Main.AllTeams[x][y].RosterG[z].goals[0] = 0; 
								Main.AllTeams[x][y].RosterG[z].assists[0] = 0; 
								Main.AllTeams[x][y].RosterG[z].points[0] = 0; 
								Main.AllTeams[x][y].RosterG[z].gamesPlayed[0] = 0;
								Main.AllTeams[x][y].RosterG[z].PIM[0] = 0;

								Main.AllTeams[x][y].RosterG[z].wins[0] = 0;
								Main.AllTeams[x][y].RosterG[z].losses[0] = 0;
								Main.AllTeams[x][y].RosterG[z].OTL[0] = 0; 
								Main.AllTeams[x][y].RosterG[z].shutouts[0] = 0;
								}
							}
							
						}
						
					}
					
					
					clearSeason();
					
					planSeason();
					
				}

			}
			
			playGames();
			
			return;
			
		}

		//Season
		else if (Main.gameCalendar.get(Calendar.MONTH) == Calendar.OCTOBER ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.NOVEMBER ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.DECEMBER ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.JANUARY ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.FEBRUARY ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.MARCH
				) {

			//Season start year
			if (Main.gameCalendar.get(Calendar.MONTH) == Calendar.OCTOBER 
					|| Main.gameCalendar.get(Calendar.MONTH) == Calendar.NOVEMBER
					|| Main.gameCalendar.get(Calendar.MONTH) == Calendar.DECEMBER) {

				//Season text
				Main.season.setText(Main.gameCalendar.get(Calendar.YEAR) + "-" + (Main.gameCalendar.get(Calendar.YEAR)+1));

			}

			//Season end year
			else if (Main.gameCalendar.get(Calendar.MONTH) == Calendar.JANUARY 
					|| Main.gameCalendar.get(Calendar.MONTH) == Calendar.FEBRUARY
					|| Main.gameCalendar.get(Calendar.MONTH) == Calendar.MARCH) {

				//Season text
				Main.season.setText((Main.gameCalendar.get(Calendar.YEAR)-1) + "-" + (Main.gameCalendar.get(Calendar.YEAR)));

			}
			
			playGames();
			
			return;

		}

		//Post Season
		else if (Main.gameCalendar.get(Calendar.MONTH) == Calendar.APRIL ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.MAY ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.JUNE
				) {

			//Season text
			Main.season.setText((Main.gameCalendar.get(Calendar.YEAR)-1) + "-" + (Main.gameCalendar.get(Calendar.YEAR)));
			Main.seasonType.setText("Playoffs");

			//First Day of Post Season
			if (Main.gameCalendar.get(Calendar.MONTH) == Calendar.APRIL && Main.gameCalendar.get(Calendar.DATE) == 1) {


				bubbleSortTeam(EConf);
				bubbleSortTeam(WConf);

				for (int x = 0; x < ESeed.length; x++) {

					ESeed[x] = EConf[x];

					WSeed[x] = WConf[x];


				}

				clearSeason();

				playoffWinCounter = 0;
				roundNumb = 0;
				
				planPostSeason();
				
			}
			
			//Move onto round 2 - QUARTERFINALS
			if (playoffWinCounter == 8 && roundNumb == 1) {
				
				playoffWinCounter = 0;
				
				planPostSeason();
				
			}

			//Move onto round 3 -SEMIFINALS
			else if (playoffWinCounter == 4 && roundNumb == 2) {

				playoffWinCounter = 0;
				
				planPostSeason();

			}

			//Move onto round 4 - FINALS
			else if (playoffWinCounter == 2 && roundNumb == 3) {
				
				playoffWinCounter = 0;
				
				planPostSeason();

			}
			
			//Declare Winner
			else if (playoffWinCounter == 1 && roundNumb == 4) {
				
				playoffWinCounter = 0;
				
				planPostSeason();
				
				if (League == 0) {
					
					JOptionPane.showMessageDialog(null, Winner.name + " have won the cup!");
					
					
				}
				
			}
			
			//Temporary, As Other Leagues aren't set up in E/W Conferences Yet
			if (League == 0 || League == 2) {
				
				playGames();
				
			}
			

			return;

		}

		//Free Agency
		else if (Main.gameCalendar.get(Calendar.MONTH) == Calendar.JULY ||
				Main.gameCalendar.get(Calendar.MONTH) == Calendar.AUGUST
				) {

			//Season text
			Main.season.setText((Main.gameCalendar.get(Calendar.YEAR)-1) + "-" + (Main.gameCalendar.get(Calendar.YEAR)));
			Main.seasonType.setText("Offseason");

		}

	}

	//constructor
	public Season(int leagueNumb) {

		League = leagueNumb;

		//[][0] = Home Team #, [][1] = Away Team #, [][2] Day of Year
		TeamMatchup = new Team[Main.AllTeams[League].length* (Main.AllTeams[League].length-1)][2];

		scheduleCalendar = new Calendar[Main.AllTeams[League].length* (Main.AllTeams[League].length-1)];

		EConf = new Team[Main.AllTeams[League].length/2];
		WConf = new Team[Main.AllTeams[League].length/2];

		ESeed = new Team[8];
		WSeed = new Team[8];
		
		int e = 0, w = 0;
		
		//Get Conferences
		for (int x = 0; x < Main.AllTeams[League].length; x++) {
			
			if (Main.AllTeams[League][x].EConf == true) {

				EConf[e] = Main.AllTeams[League][x];

				e++;

			}

			else if (Main.AllTeams[League][x].WConf == true) {

				WConf[w] = Main.AllTeams[League][x];

				w++;

			}

		}

		//Also initializes schedule calendar objects
		clearSeason();

		//Only User League plays preseason
		if (League == 0) {

			planPreSeason();

		}


	}

	void clearSeason() {

		for (int x = 0; x < scheduleCalendar.length; x++) {

			scheduleCalendar[x] = Calendar.getInstance();
			
			TeamMatchup[x][0] = null;
			TeamMatchup[x][1] = null;

		}

		for (int x = 0; x < Main.AllTeams[League].length; x++) {

			Main.AllTeams[League][x].Wins = 0;
			Main.AllTeams[League][x].Losses = 0;
			Main.AllTeams[League][x].OTLosses = 0;
			Main.AllTeams[League][x].Points = 0;

		}

	}

	void planPostSeason() {
		
		//Best of 7
		//Higher Seed -> H H A A H A H

		int counter = 0;

		//First Round
		if (roundNumb == 0) {

			for (int y = 0; y < 14; y++) {

				for (int x = 0; x < 4; x++) {

					scheduleCalendar[counter].set(Main.gameCalendar.get(Calendar.YEAR),Main.gameCalendar.get(Calendar.MONTH) , Main.gameCalendar.get(Calendar.DATE) + y + 3, 0, 0, 0);
					
					counter++;

				}

			}
			
			//reset
			counter = 0;

			//EAST
			for (int x = 0; x < 7; x++) {

				//Top Seeds Home Games
				if (x == 0 || x == 1 || x == 4 || x == 6) {					

					TeamMatchup[counter][0] = ESeed[0];
					TeamMatchup[counter][1] = ESeed[7];
					counter++;

					TeamMatchup[counter][0] = ESeed[1];
					TeamMatchup[counter][1] = ESeed[6];
					counter++;

					TeamMatchup[counter][0] = ESeed[2];
					TeamMatchup[counter][1] = ESeed[5];
					counter++;

					TeamMatchup[counter][0] = ESeed[3];
					TeamMatchup[counter][1] = ESeed[4];

				}

				//Top Seeds Away Games
				else if (x == 2 || x == 3 || x == 5) {

					
					TeamMatchup[counter][0] = ESeed[7];
					TeamMatchup[counter][1] = ESeed[0];
					counter++;

					TeamMatchup[counter][0] = ESeed[6];
					TeamMatchup[counter][1] = ESeed[1];
					counter++;

					TeamMatchup[counter][0] = ESeed[5];
					TeamMatchup[counter][1] = ESeed[2];
					counter++;

					TeamMatchup[counter][0] = ESeed[4];
					TeamMatchup[counter][1] = ESeed[3];

				}

				counter += 5;

			}

			//Reset
			counter = 4;
			
			//WEST
			for (int x = 0; x < 7; x++) {

				//Top Seeds Home Games
				if (x == 0 || x == 1 || x == 4 || x == 6) {

					TeamMatchup[counter][0] = WSeed[0];
					TeamMatchup[counter][1] = WSeed[7];
					counter++;

					TeamMatchup[counter][0] = WSeed[1];
					TeamMatchup[counter][1] = WSeed[6];
					counter++;

					TeamMatchup[counter][0] = WSeed[2];
					TeamMatchup[counter][1] = WSeed[5];
					counter++;

					TeamMatchup[counter][0] = WSeed[3];
					TeamMatchup[counter][1] = WSeed[4];

				}

				//Top Seeds Away Games
				else if (x == 2 || x == 3 || x == 5) {

					TeamMatchup[counter][0] = WSeed[7];
					TeamMatchup[counter][1] = WSeed[0];
					counter++;

					TeamMatchup[counter][0] = WSeed[6];
					TeamMatchup[counter][1] = WSeed[1];
					counter++;

					TeamMatchup[counter][0] = WSeed[5];
					TeamMatchup[counter][1] = WSeed[2];
					counter++;

					TeamMatchup[counter][0] = WSeed[4];
					TeamMatchup[counter][1] = WSeed[3];

				}

				counter += 5;

			}
			
			roundNumb++;
			
		}
		
		//Second Round - Quarter Finals
		else if (roundNumb == 1) {

			for (int x = 0; x < ESeed.length; x++) {

				if (ESeed[x] == null) {

					continue;

				}

				EQuarters[counter] = ESeed[x]; 

				counter++;

			}

			counter = 0;

			for (int x = 0; x < WSeed.length; x++) {

				if (WSeed[x] == null) {

					continue;

				}

				WQuarters[counter] = WSeed[x]; 

				counter++;

			}

			counter = 0;

			for (int y = 0; y < 14; y++) {

				for (int x = 0; x < 2; x++) {

					scheduleCalendar[counter].set(Main.gameCalendar.get(Calendar.YEAR),Main.gameCalendar.get(Calendar.MONTH) , Main.gameCalendar.get(Calendar.DATE) + y + 2, 0, 0, 0);
					
					counter++;

				}

			}
			
			//reset
			counter = 0;

			//EAST
			for (int x = 0; x < 7; x++) {

				//Top Seeds Home Games
				if (x == 0 || x == 1 || x == 4 || x == 6) {					

					TeamMatchup[counter][0] = EQuarters[0];
					TeamMatchup[counter][1] = EQuarters[3];
					counter++;

					TeamMatchup[counter][0] = EQuarters[1];
					TeamMatchup[counter][1] = EQuarters[2];
					
				}

				//Top Seeds Away Games
				else if (x == 2 || x == 3 || x == 5) {

					
					TeamMatchup[counter][0] = EQuarters[3];
					TeamMatchup[counter][1] = EQuarters[0];
					counter++;

					TeamMatchup[counter][0] = EQuarters[2];
					TeamMatchup[counter][1] = EQuarters[1];
					
				}

				counter += 3;

			}

			//Reset
			counter = 2;
			
			//WEST
			for (int x = 0; x < 7; x++) {

				//Top Seeds Home Games
				if (x == 0 || x == 1 || x == 4 || x == 6) {

					TeamMatchup[counter][0] = WQuarters[0];
					TeamMatchup[counter][1] = WQuarters[3];
					counter++;

					TeamMatchup[counter][0] = WQuarters[1];
					TeamMatchup[counter][1] = WQuarters[2];
					

				}

				//Top Seeds Away Games
				else if (x == 2 || x == 3 || x == 5) {

					TeamMatchup[counter][0] = WQuarters[3];
					TeamMatchup[counter][1] = WQuarters[0];
					counter++;

					TeamMatchup[counter][0] = WQuarters[2];
					TeamMatchup[counter][1] = WQuarters[1];

				}

				counter += 3;

			}
			
			roundNumb++;
			
		}

		//Third Round - Semi Finals
		else if (roundNumb == 2) {

			for (int x = 0; x < ESeed.length; x++) {
				
				if (ESeed[x] == null) {

					continue;

				}

				ESemis[counter] = ESeed[x]; 

				counter++;


			}

			counter = 0;

			for (int x = 0; x < WSeed.length; x++) {

				if (WSeed[x] == null) {

					continue;

				}


				WSemis[counter] = WSeed[x]; 

				counter++;

			}
			
			counter = 0;

			for (int y = 0; y < 14; y++) {

					scheduleCalendar[counter].set(Main.gameCalendar.get(Calendar.YEAR),Main.gameCalendar.get(Calendar.MONTH) , Main.gameCalendar.get(Calendar.DATE) + y + 2, 0, 0, 0);
					
					counter++;

			}
			
			//reset
			counter = 0;

			//EAST
			for (int x = 0; x < 7; x++) {

				//Top Seeds Home Games
				if (x == 0 || x == 1 || x == 4 || x == 6) {					

					TeamMatchup[counter][0] = ESemis[0];
					TeamMatchup[counter][1] = ESemis[1];
					
				}

				//Top Seeds Away Games
				else if (x == 2 || x == 3 || x == 5) {

					
					TeamMatchup[counter][0] = ESemis[1];
					TeamMatchup[counter][1] = ESemis[0];
					
				}

				counter += 2;

			}

			//Reset
			counter = 1;
			
			//WEST
			for (int x = 0; x < 7; x++) {

				//Top Seeds Home Games
				if (x == 0 || x == 1 || x == 4 || x == 6) {

					TeamMatchup[counter][0] = WSemis[0];
					TeamMatchup[counter][1] = WSemis[1];

				}

				//Top Seeds Away Games
				else if (x == 2 || x == 3 || x == 5) {

					TeamMatchup[counter][0] = WSemis[1];
					TeamMatchup[counter][1] = WSemis[0];

				}

				counter += 2;

			}
			
			roundNumb++;
			
			
		}
		
		//Fourth Round - Finals
		else if (roundNumb == 3) {
			
			for (int x = 0; x < ESeed.length; x++) {
				
				if (ESeed[x] != null) {

					Finals[counter] = ESeed[x]; 

					counter++;

				}
				
				else if (WSeed[x] != null) {
					
					Finals[counter] = WSeed[x]; 

					counter++;
					
					
				}


			}
			
			counter = 0;

			for (int y = 0; y < 14; y+=2) {

					scheduleCalendar[counter].set(Main.gameCalendar.get(Calendar.YEAR),Main.gameCalendar.get(Calendar.MONTH) , Main.gameCalendar.get(Calendar.DATE) + y + 3, 0, 0, 0);
					
					counter++;

			}
			
			//reset
			counter = 0;

			for (int x = 0; x < 7; x++) {

				//Top Seeds Home Games
				if (x == 0 || x == 1 || x == 4 || x == 6) {					

					TeamMatchup[counter][0] = Finals[0];
					TeamMatchup[counter][1] = Finals[1];
					counter++;
					
				}

				//Top Seeds Away Games
				else if (x == 2 || x == 3 || x == 5) {
					
					TeamMatchup[counter][0] = Finals[1];
					TeamMatchup[counter][1] = Finals[0];
					
				}

				counter++;

			}
		
			roundNumb++;
			
		}
		
		//Winner winner chicken dinner
		else if (roundNumb == 4) {

			for (int x = 0; x < ESeed.length; x++) {

				if (ESeed[x] != null) {

					Winner = ESeed[x]; 

				}

				else if (WSeed[x] != null) {

					Winner = WSeed[x]; 



				}


			}

			
		}
		

	}

	void planPreSeason() {

		//10 Days
		//17 Games a Day

		int[] days = {8,10,12,14,16,18,20,22,24,26};

		int count = 0;

		for (int x = 0; x < Main.Teams.length*days.length/2; x++) {

			if (count == days.length) {

				count = 0;

			}

			scheduleCalendar[x] = Calendar.getInstance();

			scheduleCalendar[x].set(Main.gameCalendar.get(Calendar.YEAR), Calendar.SEPTEMBER, days[count], 0,0,0);

			count++;

		}



		//0-16 33-17
		int temp1 = -2, temp2 = -1;

		//September 8th Matches
		for (int t = 0; t < Main.Teams.length*days.length/2; t+=10) {

			temp1+=2;
			temp2+=2;

			TeamMatchup[t][0] = Main.Teams[temp1];
			TeamMatchup[t][1] = Main.Teams[temp2];

		}


		temp1 = -1;
		temp2 = 30;

		//September 10th Matches
		for (int t = 1; t < Main.Teams.length*days.length/2; t+=10) {

			temp1+=2;
			temp2+=2;

			if (temp2 >= Main.Teams.length) {

				temp2 = 0;

			}

			TeamMatchup[t][0] = Main.Teams[temp1];
			TeamMatchup[t][1] = Main.Teams[temp2];

		}

		temp1 = 28;
		temp2 = -1;

		//September 12th Matches
		for (int t = 2; t < Main.Teams.length*days.length/2; t+=10) {

			temp1+=2;
			temp2+=2;
			if (temp1 >= Main.Teams.length) {

				temp1 = 0;

			}

			TeamMatchup[t][0] = Main.Teams[temp1];
			TeamMatchup[t][1] = Main.Teams[temp2];

		}

		temp1 = -1;
		temp2 = 26;

		//September 14th Matches
		for (int t = 3; t < Main.Teams.length*days.length/2; t+=10) {

			temp1+=2;
			temp2+=2;

			if (temp2 >= Main.Teams.length) {

				temp2 = 0;

			}

			TeamMatchup[t][0] = Main.Teams[temp1];
			TeamMatchup[t][1] = Main.Teams[temp2];

		}

		temp1 = 24;
		temp2 = -1;

		//September 16th Matches
		for (int t = 4; t < Main.Teams.length*days.length/2; t+=10) {

			temp1+=2;
			temp2+=2;

			if (temp1 >= Main.Teams.length) {

				temp1 = 0;

			}

			TeamMatchup[t][0] = Main.Teams[temp1];
			TeamMatchup[t][1] = Main.Teams[temp2];

		}

		temp1 = -1;
		temp2 = 22;

		//September 18th Matches
		for (int t = 5; t < Main.Teams.length*days.length/2; t+=10) {

			temp1+=2;
			temp2+=2;
			if (temp2 >= Main.Teams.length) {

				temp2 = 0;

			}

			TeamMatchup[t][0] = Main.Teams[temp1];
			TeamMatchup[t][1] = Main.Teams[temp2];

		}

		temp1 = 20;
		temp2 = -1;
		//September 20th Matches
		for (int t = 6; t < Main.Teams.length*days.length/2; t+=10) {

			temp1+=2;
			temp2+=2;

			if (temp1 >= Main.Teams.length) {

				temp1 = 0;

			}

			TeamMatchup[t][0] = Main.Teams[temp1];
			TeamMatchup[t][1] = Main.Teams[temp2];

		}

		temp1 = -1;
		temp2 = 18;

		//September 22th Matches
		for (int t = 7; t < Main.Teams.length*days.length/2; t+=10) {

			temp1+=2;
			temp2+=2;
			if (temp2 >= Main.Teams.length) {

				temp2 = 0;

			}

			TeamMatchup[t][0] = Main.Teams[temp1];
			TeamMatchup[t][1] = Main.Teams[temp2];

		}

		temp1 = 16;
		temp2 = -1;

		//September 24th Matches
		for (int t = 8; t < Main.Teams.length*days.length/2; t+=10) {

			temp1+=2;
			temp2+=2;

			if (temp1 >= Main.Teams.length) {

				temp1 = 0;

			}

			TeamMatchup[t][0] = Main.Teams[temp1];
			TeamMatchup[t][1] = Main.Teams[temp2];
		}

		temp1 = -1;
		temp2 = 14;
		//September 26th Matches
		for (int t = 9; t < Main.Teams.length*days.length/2; t+=10) {

			temp1+=2;
			temp2+=2;
			if (temp2 >= Main.Teams.length) {

				temp2 = 0;

			}

			TeamMatchup[t][0] = Main.Teams[temp1];
			TeamMatchup[t][1] = Main.Teams[temp2];

		}




		/*
		 * 
		int test = 0;

		//Test Print
		for (int x = 0; x < Main.Teams.length*days.length/2; x++) {

			test++;

			System.out.println(TeamMatchup[x][0] + " VS " + TeamMatchup[x][1] + " ON             "  + gameDay[x].getTime() + " " + test);

		}

		 */


	}

	void planSeason() {

		int count = 0;
		//Set Matchups
		for (int y = 0; y < Main.AllTeams[League].length; y++) {

			for (int i = 0; i < Main.AllTeams[League].length; i++) {

				if (i == y) {

					i++;

					if (i == Main.AllTeams[League].length) {

						break;

					}

				}



				//y = Home Team
				TeamMatchup[count][0] = Main.AllTeams[League][y];
				//i = Away Team
				TeamMatchup[count][1] = Main.AllTeams[League][i];

				// test print System.out.println(y + "," + i + ": " + count);

				count++;
			}

		}

		//Set Dates of Matchups
		for(int a = 0; a < TeamMatchup.length; a++) {

			int thisYearOrNext = (int)(Math.random()*2);
			int[] months = new int[3];
			//This Year
			if (thisYearOrNext == 0) {

				months[0] = Calendar.OCTOBER;
				months[1] = Calendar.NOVEMBER;
				months[2] = Calendar.DECEMBER;

			}

			//Next Year
			if (thisYearOrNext == 1) {

				months[0] = Calendar.JANUARY;
				months[1] = Calendar.FEBRUARY;
				months[2] = Calendar.MARCH;

			}

			scheduleCalendar[a] = Calendar.getInstance();

			scheduleCalendar[a].set(
					Main.gameCalendar.get(Calendar.YEAR) + thisYearOrNext, 
					months[(int)(Math.random()*3)], 
					((int)(Math.random()*27) + 1), 0, 0, 0);


		}

		if (League == 0) {

			boolean sameDay = true;

			//Should check to see if any single team plays two games on same day, and fixes problem
			while (sameDay == true) {

				sameDay = false;

				for (int u = 0; u < TeamMatchup.length; u++) {

					for (int i = u+1; i < TeamMatchup.length; i++) {

						if ( 
								( TeamMatchup[i][0] == TeamMatchup[u][0] 
										|| TeamMatchup[i][0] == TeamMatchup[u][1]
												|| TeamMatchup[i][1] == TeamMatchup[u][0] 
														||TeamMatchup[i][1] == TeamMatchup[u][1]  )
								&& scheduleCalendar[i].get(Calendar.DAY_OF_YEAR) == scheduleCalendar[u].get(Calendar.DAY_OF_YEAR)
								) {

							int temp = (int)(Math.random()*2);
							int max = 0, min = 0, range = 0;

							//Season End Year
							if (temp == 0) {
								max = 90;
								min = 1;
							}

							//Season Beginning Year
							else if(temp == 1) {
								max = 365;
								min = 274;
							}

							range = max-min +1;

							scheduleCalendar[i].set(Calendar.DAY_OF_YEAR, ((int)(Math.random()*range) + min));
							sameDay = true;
							i = 0;
							u = 0;
							break;
						}


					}

					if (sameDay == true) {
						continue;

					}

				}
				
			}
			
		}

	}

	void playGames() {

		for(int z = 0; z < TeamMatchup.length; z++) {

			//Check if there is a game scheduled for today
			if (Main.gameCalendar.get(Calendar.DAY_OF_YEAR) == scheduleCalendar[z].get(Calendar.DAY_OF_YEAR)) {

				if (TeamMatchup[z][0] != null && TeamMatchup[z][1] != null) {

					if (TeamMatchup[z][0] != Main.UserTeam && TeamMatchup[z][1] != Main.UserTeam ) {

						new Game(TeamMatchup[z][0],TeamMatchup[z][1]);				

						TeamMatchup[z][0].Points = (TeamMatchup[z][0].Wins*2) + TeamMatchup[z][0].OTLosses;

						TeamMatchup[z][1].Points = (TeamMatchup[z][1].Wins*2) + TeamMatchup[z][1].OTLosses;

						if (Main.gameCalendar.get(Calendar.MONTH) == Calendar.APRIL || Main.gameCalendar.get(Calendar.MONTH) == Calendar.MAY || Main.gameCalendar.get(Calendar.MONTH) == Calendar.JUNE) {

							playoffWinCheck(TeamMatchup[z][0], TeamMatchup[z][1]);

						}
						
					}

				}

			}
			
		}

	}

	public void userTeamPlayGames() {

		for(int z = 0; z < TeamMatchup.length; z++) {

			//Check if there is a game scheduled for today
			if (Main.gameCalendar.get(Calendar.DAY_OF_YEAR) == scheduleCalendar[z].get(Calendar.DAY_OF_YEAR) ) {

				if (TeamMatchup[z][0] != null && TeamMatchup[z][1] != null) {

					if (TeamMatchup[z][0] == Main.UserTeam || TeamMatchup[z][1] == Main.UserTeam ) {

						new Game(TeamMatchup[z][0],TeamMatchup[z][1]);

						TeamMatchup[z][0].Points = (TeamMatchup[z][0].Wins*2) + TeamMatchup[z][0].OTLosses;

						TeamMatchup[z][1].Points = (TeamMatchup[z][1].Wins*2) + TeamMatchup[z][1].OTLosses;

						if (Main.gameCalendar.get(Calendar.MONTH) == Calendar.APRIL || Main.gameCalendar.get(Calendar.MONTH) == Calendar.MAY || Main.gameCalendar.get(Calendar.MONTH) == Calendar.JUNE) {

							playoffWinCheck(TeamMatchup[z][0], TeamMatchup[z][1]);

						}

					}
					
				}
				
			}

		}

	}

	void playoffWinCheck(Team Home, Team Away) {

		if (Home.Wins == 4*roundNumb) {

			playoffWinCounter++;

			removePlayOffLosers(Away);

		}

		else if (Away.Wins == 4*roundNumb) {

			playoffWinCounter++;

			removePlayOffLosers(Home);

		}

	}

	void removePlayOffLosers(Team loser) {

		for (int x = 0; x < scheduleCalendar.length; x++) {

			if (scheduleCalendar[x].after(Main.gameCalendar) == true ) {

				if (TeamMatchup[x][0] == loser || TeamMatchup[x][1] == loser) {

					TeamMatchup[x][0] = null;
					TeamMatchup[x][1] = null;
					scheduleCalendar[x].clear();

				}

			}

		}

		for (int x = 0; x < ESeed.length; x++) {

			if (ESeed[x] == loser) {

				ESeed[x] = null;

				break;
				
			}

			else if (WSeed[x] == loser) {

				WSeed[x] = null;

				break;

			}


		}

	}


	//Sort Teams by most points
	void bubbleSortTeam(Team arr[]) {
		int n = arr.length;

		for (int i = 0; i < n-1; i++) {

			for (int j = 0; j < n-i-1; j++) {

				if (arr[j+1] == null) {

					break;

				}

				if (arr[j].Points < arr[j+1].Points) {
					// swap temp and arr[i]
					Team temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
				}

			}

		}
	}


}