import javafx.geometry.Point2D;

class Player {

	//Constant & Game Time Var
	
	//Game Time Variables
	Point2D location = new Point2D(0, 0);
	
	int rating, age, weight;

	Team team;
	
	String league, position, handiness, firstName, lastName, fullName, shortName, height, ProTeam, DraftTeam;
	
	int[] goals = new int[2], assists  = new int[2], points  = new int[2], gamesPlayed = new int[2], PIM = new int[2];
	
	static String[] positionType = {"C", "LW", "RW", "D", "G"}, handinesstype = {"Left Handed", "Right Handed"},

			firstNames = {"Mike", "John", "Mark", "Matt", "Ryan", "Liam", "Noah", "Ethan", "Logan", "Aidan", "Jacob", "Ethan", "Nicholas", "Matthew", "Chris", "Nick",
					"Alex", "Drew", "Jake", "Justin", "Tyler", "Brandon", "Kyle", "Nikita", "Dmitry", "Henrik", "Jean", "Andy", "Max", "Ray", "Johnny", "Martin",
					"Bobby", "Paul", "Charlie", "Ken", "Phil", "Bill", "Tony", "Peter", "Ron", "Grant", "Bernie", "Wayne", "Glenn", "Doug", "Tim", "Brett", "Patrick",
					"Duncan", "Dave", "Ted", "Pat", "Jacques", "Brian", "Nicklas", "Eric", "Frank", "Stan", "Scott", "Joe", "Adam", "Brad", "Dennis", "Henri", "Henry",
					"Larry", "Terry", "Brendan", "Eddie", "Billy", "Mats", "Jonathan", "Jon", "Bryan", "Steve", "George", "Cam", "Cameron", "Roman", "Leo", "Shane",
					"Mason", "William", "Michael", "Alexander", "James", "Daniel", "Taylor"}, 
			lastNames = {"Smith", "Brown", "Wilson", "Johnson", "Murray", "Stewart", "Anderson", "Miller", "Martin", "Jackson", "Jones", "McDonald", "Morrison", "Taylor", "Williams", "Campbell",
					"Evans", "Fraser", "Hughes", "MacDonald", "Ward", "White", "Young", "Adams", "Armstrong", "Carter", "Kelly", "Mitchell", "Murphy", "Harris", "King", "Lemieux", "Reid", 
					"Sullivan", "Gauthier", "Gratton", "Green", "Hall", "Kennedy","Larose","McLean","Patrick", "Roberts", "Sutter", "Thompson", "Tremblay", "Watson", "Allen", "Andersson", 
					"Conacher", "Cook", "Crawford", "Cullen", "Davis", "Foster", "Giroux", "Gordon", "Henry", "Jensen", "McCarthy", "Moore", "Morris", "Peters", "Richardson", "Robinson", "Roy",
					"Schmidt", "Thomas", "Walker", "Bailey", "Blake", "Clark", "Cloutier", "Davidson", "Fisher", "Gilbert", "Hamilton", "Henderson", "Lewis", "MacKenzie", "Marshall", "O'Neill",
					"Picard", "Plante", "Price", "Robertson", "Ryan", "Scott", "Simon", "Stevens", "Stuart", "Johansson", "Karlsson", "Nilsson", "Eriksson", "Larsson", "Olsson", "Persson",
					"Svensson", "Gustafsson", "Pettersson", "Jonsson", "Jansson", "Hansson", "Bengtsson", "Jönsson", "Petersson", "Carlsson", "Gustavsson", "Magnusson", "Lindberg", "Olofsson",
					"Lindström", "Axelsson", "Lindgren", "Jakobsson", "Lundberg", "Bergström", "Lundgren", "Berglund", "Berg", "Fredriksson", "Mattsson", "Sandberg", "Henriksson", "Håkansson",
					"Sjöberg", "Forsberg", "Lindqvist", "Danielsson", "Engström", "Lundin", "Fransson", "Eklund", "Lind", "Johnsson", "Samuelsson", "Gunnarsson", "Holm", "Bergman", "Nyström",
					"Holmberg", "Lundqvist", "Arvidsson", "Mårtensson", "Isaksson", "Nyberg", "Söderberg", "Björk", "Nordström", "Lundström", "Eliasson", "Wallin", "Berggren", "Björklund",
					"Ström", "Hermansson", "Nordin", "Sandström", "Holmgren", "Sundberg", "Ekström", "Åberg", "Hedlund", "Sjögren", "Månsson", "Martinsson", "Öberg", "Jonasson", "Andreasson",
					"Abrahamsson", "Dahlberg", "Hellström", "Strömberg", "Blomqvist", "Norberg", "Åkesson", "Blom", "Göransson", "Sundström", "Åström", "Söderström", "Ivarsson", "Löfgren", 
					"Ek", "Bergqvist", "Lindholm", "Lund", "Nyman", "Josefsson", "Parsons", "Grant", "Singh", "Michaud", "Nadeau", "Russell", "Desjardins", "Bennett", "Ouellet", "Wood",
					"Lapointe", "Bergeron", "Gagnon", "Gagne", "Hill", "Beaulieu", "Baker", "Girard", "Wright", "Poirier", "Clarke", "Bell", "Coleman", "Jenkins", "Garcia", "Martinez",
					"Rodriguez", "Lee", "Hernandez", "Lopez", "Gonzalez", "Perez", "Turner", "Phillips", "Parker", "Edwards", "Sanchez", "Collins", "Rogers", "Reed", "Cox", "Gray", "Brooks",
					"Barnes", "Perry", "Long", "Hayes", "Myers", "Cole", "Ivanov", "Smirnov", "Kuznetsov", "Popov", "Vasiliev", "Petrov", "Sokolov", "Mikhailov", "Fedorov", "Morozov", "Volkov",
					"Alexeev", "Lebedev", "Semenov", "Egorov", "Pavlov", "Kozlov", "Stepanov", "Nikolaev", "Orlov", "Virtanen", "Korhonen", "Nieminen", "Mäkinen", "Hämäläinen", "Koskinen",
					"Heikkinen", "Järvinen", "Niemi", "Halla", "Novák", "Svoboda", "Novotný", "Dvořák", "Černý", "Procházka", "Kučera", "Veselý", "Horák", "Nemec", "Pokorný", "Pospíšil", "Marek",
					"Hájek", "Jelínek", "Nielsen", "Hansen", "Andersen", "Larsen", "Sørensen", "Rasmussen", "Horváth", "Gruber", "Bauer", "Wagner", "Müller", "Schneider", "Fischer", "Brunner", 
					"Lang", "Wolf", "Lehner", "Meyer", "Weber", "Hoffmann", "Horvat", "Nilsen", "Jørgensen", "Dahl", "Bernard", "Dubois", "Durand", "Lefebvre", "Côté", "Bouchard", "Morin", 
					"Lavoie", "Fortin", "Gagné", "Torres", "Flores", "Nguyen", "Cooper", "Reyes", "Sanders", "Ross", "Morales", "Powell", "Ortiz", "Butler", "Clune"},
			heights = {"5'6", "5'7", "5'8", "5'9", "5'10", "5'11", "6'0", "6'1", "6'2", "6'3", "6'4", "6'5", "6'6", "6'7", "6'8", "6'9"};
	

	//remove
	boolean signedToMainTeam = false;
	//OwnedBy = MainTeam ?

	double contractA = 0;
	int contractL = 0;
	
	public Player(int positionNumb, int teamNumb, int leagueNumb) {
		
		firstName = firstNames[(int)(Math.random()*firstNames.length)];
		lastName = lastNames[(int)(Math.random()*lastNames.length)];
		fullName = firstName + " " + lastName;
		shortName = String.valueOf(firstName.charAt(0)) + ". " + lastName;
		handiness = handinesstype[(int)(Math.random()*handinesstype.length)];
		position = positionType[positionNumb];
		
		if (leagueNumb != 4) {

			league = Team.League[leagueNumb];

		}
		
		setHeight();
		setWeight();
		setAge();
		
		contractA = Math.random()*10 + 1;
		
		contractA = Math.round(contractA);
		
		contractL = (int)(Math.random()*8)+1;
		
		
	}
	

	
	void setHeight() {
		
		
		int chance = (int)(Math.random()*100);
		
		if (chance <= 80) {
			
			height = heights[(int)(Math.random()*8) +3];
			
		}
		
		else if (chance > 80) {
			
			height = heights[(int)(Math.random()*heights.length)];
			
		}
		
	}
	
	void setWeight() {
		

		/*
		int range = (max - min) + 1;     
		   return (int)(Math.random() * range) + min;
		*/
		
		weight = (int)(Math.random() *101) + 160;
		
	}
	
	void setAge() {
		
		/*
		int range = (max - min) + 1;     
		   return (int)(Math.random() * range) + min;
		*/
		
		age = (int)(Math.random()*17)+19;
		
	}
	
	public String getTeam() {
		return team.name;
	}

	public String getPosition() {
		return position;
	}

	public String getHandiness() {
		return handiness;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public String getTeamAbrrv() {
		return team.nameAbbrv;
	}

	public int getAge() {
		return age;
	}

	public int getGoals() {
		return goals[Main.currentS];
	}


	public int getAssists() {
		return assists[Main.currentS];
	}



	public int getPoints() {
		return points[Main.currentS];
	}



	public int getGamesPlayed() {
		return gamesPlayed[Main.currentS];
	}



	public int getPIM() {
		return PIM[Main.currentS];
	}
	
	
}
