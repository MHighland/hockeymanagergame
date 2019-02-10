
public class Team {

	String name, nameAbbrv, nameLocation, nickName, league;

	//How many players a team can have on their active roster
	static int rosterLimit = 21, rosterGlimit = 3;
	
	int OffensiveRating, DeffensiveRating, GoaltenderRating, Wins, Losses, OTLosses, Points, GoalsFor, GoalsAgainst;

	//Junior League name change?
	static String[] League = {"North American Major Hockey League", "European Major Hockey League", "North American Minor Hockey League", "North American Major Junior Hockey League", "Free Agent"};

	//                                  0            1         2            3        4            5            6          7          8           9           10         11
	static String[] TeamLocations = {"Anchorage", "Arizona", "Boston", "Buffalo", "Calgary", "California", "Carolina", "Chicago", "Colorado", "Columbus", "Dallas", "Detroit",
			//  12          13           14          15          16          17         18           19            20             21         22           23
			"Edmonton", "Hartford", "Halifax", "Kansas City", "Las Vegas", "Miami", "Minnesota", "Mexico City", "Montreal", "Nashville", "New York", "New Jersey",
			// 24               25            26       27        28          29          30         31          32               33  
			"Philadelphia", "Pittsburgh", "Ottawa", "Quebec", "Seattle", "St. Louis", "Toronto", "Vancouver", "Washington", "Winnipeg"},

			//                  0            1          2            3         4         5          6            7                 8                 9           10 
			TeamNames = {"Golden Huskies", "29ers", "Mayflowers", "Lights", "Outlaws", "Seals", "Red Drums", "Monarchs", "Greenback Cutthroats", "Buckeyes", "Pronghorns",
					// 11         12          13          14           15          16        17           18          19          20           21        22          23
					"Robins", "Champions", "Manatees", "Fishers", "Meadowlarks", "Dice", "Moonstones", "Walleyes", "Jaguars", "Wanderers", "Strings", "Liberties", "Horses",
					// 24        25         26          27            28            29        30         31           32           33  
					"Bells", "Hemlocks","Federals", "Athletics", "Metropolitans", "Eagles", "Pros", "Millionaires", "Senate", "Victorias"},

			TeamAbbrv = {"ANC", "ARZ", "BOS", "BUF", "CGY", "CAL", "CAR", "CHI", "COL", "CGC", "DAL", "DET", "EDM", "HFD", "HFX", "KCM", "LVD", "MIA", "MIN", "MEX", "MON", "NSH",
					"NYL", "NJH", "PHI","PIT", "OTT", "QUE", "SEA", "STL", "TOR", "VAN", "WSH", "WIN"},

			MinorTeams = {"MinorANCTeam1", "MinorARZTeam2","MinorBOSTeam3","MinorBUFTeam4","MinorCGYTeam5","MinorCALTeam6","MinorCARTeam7","MinorCHITeam8","MinorCOLTeam9",
					"MinorCGCTeam10","MinorDALTeam11","MinorDETTeam12","MinorEDMTeam13","MinorHFDTeam14","MinorHFXTeam15","MinorKCMTeam16","MinorLVDTeam17","MinorMIATeam18",
					"MinorMINTeam19","MinorMEXTeam20","MinorMONTeam21","MinorNSHTeam22","MinorNYLTeam23","MinorNJHTeam24","MinorPHITeam25","MinorPITTeam26","MinorOTTTeam27",
					"MinorQUETeam28","MinorSEATeam29","MinorSTLTeam30","MinorTORTeam31","MinorVANTeam32","MinorWSHTeam33","MinorWINTeam34"},

			EuropeanTeams = {"EuroTeam1", "EuroTeam2","EuroTeam3","EuroTeam4","EuroTeam5","EuroTeam6","EuroTeam7","EuroTeam8","EuroTeam9","EuroTeam10","EuroTeam11",
					"EuroTeam12","EuroTeam13","EuroTeam14","EuroTeam15","EuroTeam16","EuroTeam17","EuroTeam18","EuroTeam19","EuroTeam20","EuroTeam21","EuroTeam22",
					"EuroTeam23","EuroTeam24","EuroTeam25","EuroTeam26"},

			ProspectTeams = {"ProspectTeam1", "ProspectTeam2","ProspectTeam3","ProspectTeam4","ProspectTeam5","ProspectTeam6","ProspectTeam7","ProspectTeam8","ProspectTeam9",
					"ProspectTeam10","ProspectTeam11","ProspectTeam12","ProspectTeam13","ProspectTeam14","ProspectTeam15","ProspectTeam16","ProspectTeam17","ProspectTeam18",
					"ProspectTeam19","ProspectTeam20","ProspectTeam21","ProspectTeam22"};
	
	//For Main League & Minor League
	static int[] easternConf = {2,3,6,9,11,13,14,17,20,22,23,24,25,26,27,30,32}, westernConf = {0,1,4,5,7,8,10,12,15,16,18,19,21,28,29,31,33};
	
	boolean EConf = false, WConf = false;

	//Teams Rosters ~ Not used outside of Team Class
	Skater[] Roster = new Skater[rosterLimit];
	Goalie[] RosterG = new Goalie[rosterGlimit];

	//Team Rosters seperated by position type
	Skater[] Centers = new Skater[rosterLimit];
	Skater[] LeftWing = new Skater[rosterLimit];
	Skater[] RightWing = new Skater[rosterLimit];
	Skater[] Defenceman = new Skater[rosterLimit];

	//Team lines
	Skater[] LinesF = new Skater[12];
	Skater[] LinesD = new Skater[6];
	Skater[][] LinePP = new Skater[4][5]; //[2][4], [2][4] should be null
	Skater[][] LinePK = new Skater[4][4]; //[2][3], [3][3] should be null
	Skater[][] LineOT = new Skater[4][3];
	Goalie[] Tandem = new Goalie[2];
	
	//First Line 3 players
	//Second Lines 3 Players
	//Third Line 3 Players
	//Fourth Line 3 Players
	
	//Top 2 2 Players
	//Top 4 2 Players
	//Top 6 2 Players
	
	//Goalie Tandem 2 Players
	
	//1st Powerplay Unit 5 Players
	//2nd Powerplay Unit 5 Players

	//1st 4Man Powerplay Unit 4 Players
	//2nd 4Man Powerplay Unit 4 Players
	
	//1st PK Unit 4 Players
	//2nd PK Unit 4 Players

	//1st 3Man PK Unit 4 Players
	//2nd 3Man PK Unit 4 Players
	
	//1st Overtime Unit 3 Players
	//2nd Overtime Unit 3 Players
	//3rd Overtime Unit 3 Players
	//4th Overtime Unit 3 Players
	

	public Team(int teamNumb, int leagueNumb) {

		league = League[leagueNumb];

		//North American League
		if (leagueNumb == 0) {

			name = TeamLocations[teamNumb] + " " + TeamNames[teamNumb];

			nameLocation = TeamLocations[teamNumb];

			nickName = TeamNames[teamNumb];

			nameAbbrv = TeamAbbrv[teamNumb];
			
			setMainTeamsConference(teamNumb);
			
		}

		//European League
		else if (leagueNumb == 1) {

			name = EuropeanTeams[teamNumb];
			
			//TODO Sort by conferences 

		}


		//Minor League
		else if (leagueNumb == 2) {

			name = MinorTeams[teamNumb];
			
			setMainTeamsConference(teamNumb);

		}

		//Prospect League
		else if (leagueNumb == 3) {

			
			name = ProspectTeams[teamNumb];
			
			//TODO Sort by conferences 

		}

	}
	
	void setMainTeamsConference(int teamNumb) {
		
		for (int x = 0; x < easternConf.length; x++) {
			
			if (teamNumb == easternConf[x]) {
				
				EConf = true;
				
				break;
				
			}
			
			else if (teamNumb == westernConf[x]) {
				
				WConf = true;
				
				break;
			}
			
		}
		
	}

	public void setRoster() {

		int count = 0;

		for (int x = 0; x < Main.Skaters.length; x++) {

			if (Main.Skaters[x].team == this && Main.Skaters[x].league == league) {

				Roster[count] = Main.Skaters[x];

				count++;

			}

		}

		count = 0;

		for (int x = 0; x < Main.Goalies.length; x++) {

			if (Main.Goalies[x].team == this && Main.Goalies[x].league == league) {

				RosterG[count] = Main.Goalies[x];

				count++;

			}

		}

		count = 0;

		//C
		for (int x = 0; x < Roster.length; x++) {

			if (Roster[x].position == Skater.positionType[0]) {

				Centers[count] = Roster[x];

				count++;

			}

		}

		count = 0;

		//LW
		for (int x = 0; x < Roster.length; x++) {

			if (Roster[x].position == Skater.positionType[1]) {

				LeftWing[count] = Roster[x];

				count++;

			}

		}

		count = 0;

		//RW
		for (int x = 0; x < Roster.length; x++) {

			if (Roster[x].position == Skater.positionType[2]) {

				RightWing[count] = Roster[x];

				count++;

			}

		}

		count = 0;

		//Defencemen
		for (int x = 0; x < Roster.length; x++) {

			if (Roster[x].position == Skater.positionType[3]) {

				Defenceman[count] = Roster[x];

				count++;

			}

		}

		count = 0;

		setLines();

		getRating();



	}

	//totals forwards, defencemen and goalies to get Offensive, Deffendive, Goaltender rating, respectively
	void getRating () {

		OffensiveRating = (int)(LinesF[0].rating + LinesF[1].rating + LinesF[2].rating + 
				LinesF[3].rating + LinesF[4].rating + LinesF[5].rating + 
				LinesF[6].rating + LinesF[7].rating + LinesF[8].rating + 
				LinesF[9].rating + LinesF[10].rating + LinesF[11].rating)/12;

		DeffensiveRating = (int)(LinesD[0].rating + LinesD[1].rating + LinesD[2].rating + LinesD[3].rating + LinesD[4].rating + LinesD[5].rating)/6;

		GoaltenderRating = (int)(Tandem[0].rating + Tandem[1].rating)/2;

	}

	//Sort skaters from greatest overall rating  to least
	void bubbleSortSkaterOverallRating(Skater arr[]) {
		
		int n = arr.length;

		for (int i = 0; i < n-1; i++) {

			for (int j = 0; j < n-i-1; j++) {

				if (arr[j+1] == null) {

					break;

				}

				if (arr[j].rating < arr[j+1].rating) {
					// swap temp and arr[i]
					Skater temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
				}

			}

		}
	}

	//Sort goalies from greatest overall rating to least
	void bubbleSortGoalieOverallRating(Goalie arr[]) {
		
		int n = arr.length;

		for (int i = 0; i < n-1; i++) {

			for (int j = 0; j < n-i-1; j++) {

				if (arr[j+1] == null) {

					break;

				}

				if (arr[j].rating < arr[j+1].rating) {
					// swap temp and arr[i]
					Goalie temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
				}

			}

		}
	}
	
	//TODO Bubble Sorts for Offense & Deffense

	//Set Lines based on highest rating of player
	void setLines() {

		bubbleSortSkaterOverallRating(Centers);

		bubbleSortSkaterOverallRating(LeftWing);

		bubbleSortSkaterOverallRating(RightWing);

		bubbleSortSkaterOverallRating(Defenceman);

		bubbleSortGoalieOverallRating(RosterG);

		int count = 0;

		for (int x = 0; x < LinesF.length-2; x+=3) {

			LinesF[x] = LeftWing[count];

			LinesF[x+1] = Centers[count];

			LinesF[x+2] = RightWing[count];

			count++;

		}

		for (int x = 0; x < LinesD.length; x++) {

			LinesD[x] = Defenceman[x];


		}

		for (int x = 0; x < Tandem.length; x++) {

			Tandem[x] = RosterG[x];


		}


	}
	


	public String getName() {
		return name;
	}


	public String getNameAbbrv() {
		return nameAbbrv;
	}



	public int getWins() {
		return Wins;
	}


	public int getLosses() {
		return Losses;
	}


	public int getOTLosses() {
		return OTLosses;
	}



	public int getPoints() {
		return Points;
	}
	
	public String getConference() {
		
		if (this.EConf == true) {
			
			return "E";
			
		}
		
		else {
			
			return "W";
			
		}
		
		
	}


}
