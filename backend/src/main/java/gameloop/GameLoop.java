package gameloop;
import java.util.HashMap;
import java.util.Random;

import map.*;
import event.*;
import player.*;
import phasetwo.*;

public class GameLoop {
	public boolean hasStarted = false;
	public int difficulty = 1; //1 = no fire
	public int turnLength = 30; //in seconds
	private Player[] players;
	private int playerNumber = 0;
	private int maxPlayerNumber = 4;
	private HashMap<Integer, String> id_Name;
	public int totalTurn;
	private int currentEvent;
	private int villainId = -1;
	public int healthToLose = 1;
	private Random rd = new Random();
	private EventList t;
	private Map map;
	private PhaseTwo pt;
	
	public GameLoop () {
		t = new EventList();
		map = new Map();
		
		totalTurn = 0;
		id_Name = new HashMap<Integer, String>();
		players = new Player[maxPlayerNumber];

		// Initialize map
		map.initializeMap();
		map.createFloorMap();
			
		// initialize phase two
		pt = new PhaseTwo(getOriginalMap());
				
	}
	
	public boolean addPlayer(String playerName) {
		// Initialize Binary map
		for(int i = 0; i < playerNumber; i++) {
			try {
				map.updateBinaryMap(players[i].getLocation()[0], players[i].getLocation()[1]);
			}
			catch (Exception e) {
				System.out.println("Add player binary map error");
			}
		}
		
		System.out.println(id_Name);
		System.out.println(playerName);
		if(playerNumber < maxPlayerNumber) {
			players[playerNumber] = new Player(playerNumber, playerName, 5, 5, 5);
			players[playerNumber].setMotivation(players[playerNumber].getSpeed());
			id_Name.put(playerNumber, playerName);
			playerNumber++;
			return true;
		}
		return false;
	}
	
	// Updating player, binary map and determine the event
	// after a move. The input is player id and new position.
	// return true if the position is available.
	public boolean move (int r, int c) {
		if(isValidation(r, c)) {
			if(getBinaryMap()[r][c] == 0 && !pt.getPhaseTwo()) { //discover new tile and trigger event
				currentEvent = t.getEvent();
			}
			players[totalTurn % playerNumber].updateLocation(r, c);
			map.updateBinaryMap(r, c);
			if (pt.getPhaseTwo() && map.getFireTile(r, c)) {
				players[totalTurn % playerNumber].setHp(players[totalTurn % playerNumber].getHp() - 1);
			}
			return true;
		} else {
			return false;
		}
	}
	
	// Trigger the events.
	public String processEvent(int diceRoll) {
		return t.triggerEvent(currentEvent, diceRoll, players[totalTurn % playerNumber]);
	}
	
	// Attack
	public String attack(String playerName, int diceRoll1, int diceRoll2, String attackingPlayerName) { //playerName = defending player
		//totalTurn % playerNumber = current playerID
		int playerId = 0;
		int attackingPlayer = totalTurn % playerNumber;
		
		if(playerName.equals("")) {
			return "Please select a target";
		}

		if(players[totalTurn % playerNumber].getMotivation() == 0) {
			return "You can't fight twice!";
		}

		players[totalTurn % playerNumber].setMotivation(0);
		
		// Get player id of defending player.	
		for(int i = 0 ; i <= 3; i++) {
			if(id_Name.get(i).equals(playerName)) {
				playerId = i;
			}
		}

		// Generate fight outcome.
		if (diceRoll1 > diceRoll2) { //defender loses
			players[playerId].setHp(players[playerId].getHp() - (diceRoll1 - diceRoll2));
			
			if(pt.getEndGame() == 1) {
				checkKey(players[playerId]);
			}

			if (players[playerId].getHp() <= 0) {
				return players[attackingPlayer].getName() + " killed " + playerName;
			}
			else {
				return playerName + " lose " + (diceRoll1 - diceRoll2) + " HP";
			}

			
		}
		else if (diceRoll2 > diceRoll1) { //attacked loses
            players[attackingPlayer].setHp(players[attackingPlayer].getHp() - (diceRoll2 - diceRoll1));
            
            if(pt.getEndGame() == 1) {
				checkKey(players[attackingPlayer]);
			}
            
			if (players[attackingPlayer].getHp() <= 0) {
				return playerName + " killed " + players[attackingPlayer].getName();
			}
			else {
				return players[attackingPlayer].getName() + " lose " + (diceRoll2 - diceRoll1) + " HP";
			}
        }
		return "The fight was a draw!";
	}
	
	// check is the player dead.
	public boolean isDead() {
		if(players[totalTurn % playerNumber].getHp() <= 0) {
			return true;
		}		
		return false;
	}

	// check the validation of a move.
	public boolean isValidation (int r, int c){
		int shortestPath = map.shortestPath(players[totalTurn % playerNumber].getLocation()[0], players[totalTurn % playerNumber].getLocation()[1], r, c);
		
		if(shortestPath <= players[totalTurn % playerNumber].getMotivation() && shortestPath != -1){
			players[totalTurn % playerNumber].setMotivation(players[totalTurn % playerNumber].getMotivation() - shortestPath);
			return true;
		}
		return false;
	}
	
	// Return all players location
	public int[][] getAllLocation () {
		int[][] temp = new int[playerNumber][2];
		
		for(int i = 0; i < playerNumber; i++){
			if(players[i].getHp() == 0) {
				temp[i][0] = -1;
				temp[i][1] = -1;
			}
			temp[i] = players[i].getLocation();
		}

		return temp;
	}

	// Get the events id
	public int getEventId() {
		return currentEvent;
	}
	
	// Return the description of events
	public String getEventDescription() {
		String description = "";
		description = t.getEventDescription(currentEvent);
		return description;
	}
	
	// Get different type of maps.
	public int[][] getOriginalMap(){
		return map.getOriginalMap();
	}

	public int[][] getMapType(){
		return map.getMapType();
	}

	public int[][] getBinaryMap(){
		return map.getBinaryMap();
	}
	
	public int[][] getFloorMap(){
		return map.getFloorMap();
	}

	// This method will output the id of the player who will move in this turn.
	public String getPlayerTurn(){
		return id_Name.get(totalTurn % playerNumber);
	}
	
	// Get total turn number
	public int getTotalTurn() {
		return totalTurn;
	}

	// move to next turn.
	public String[] nextTurn() {
		String[] winner = pt.winConditionCheck(players, villainId);
		if(winner != null) {
			System.out.println(winner[0]);
		}
		

		if(winner != null) {
			return winner;
		}
		
		if(pt.getPhaseTwo() && 
		   map.getFireTile(players[totalTurn % playerNumber].getLocation()[0],
		   				   players[totalTurn % playerNumber].getLocation()[1])) {
			players[totalTurn % playerNumber].setHp(players[totalTurn % playerNumber].getHp() - 1);
		}
		
		players[totalTurn % playerNumber].setMotivation(players[totalTurn % playerNumber].getSpeed());
		totalTurn++;
		while(players[totalTurn % playerNumber].getHp() <= 0) {
			players[totalTurn % playerNumber].updateLocation(10, 10);
			totalTurn++;
		} //skip turn if dead
		
		if(pt.getPhaseTwo() && villainId != -1) {
			//playerStateChangeInPhaseTwo();
		}
		currentEvent = -1;
		return null;
	}
	
	// Players' state change in phase two
	public void playerStateChangeInPhaseTwo() {
		for(Player p : players) {
			if(p.getId() != villainId) {
				p.setHp(p.getHp() - 1);
				if(pt.getEndGame() == 1) {
					checkKey(p);
				}
			}
		}
	}	
	
	// If the key keeper dead, the key will drop.
	public void checkKey(Player p) {
		if(p.getWithKey() && p.getHp() <= 0) {
			p.setWithKey(false);
			pt.setKey(true);
			pt.setKeyPosition(p.getLocation());
		}
			
	}

	
	// Start phase two
	public String startPhaseTwo() {
		if (difficulty > 2 ) {
			healthToLose = 2;
		}
		else if (difficulty > 3) {
			healthToLose = 3;
		}
		int choice = 2; //rd.nextInt(3); // int 0:3 exclusive
		pt.setPhaseTwo(true);
		map.swapFloorMap(difficulty);
		if(choice == 2) {
			return pt.choiceScenarios();
		}
		else {
			villainId = rd.nextInt(playerNumber);
			return pt.choiceScenarios(choice, players[villainId]);
		}
	}

	//gets the visual map, a product of maptype and binary map
	public int[][] multiplyVisualMap(){
        int[][] binaryMap = getBinaryMap();
		int[][] mapType = getMapType();
		int[][] floorMap = getFloorMap();
		int MAP_SIZE = map.getMAP_SIZE();
		int [][] result = new int[MAP_SIZE][MAP_SIZE];
            for (int i = 0; i < MAP_SIZE; i++) { 
                for (int j = 0; j < MAP_SIZE; j++) { 
                        result[i][j] = mapType[i][j]*binaryMap[i][j] + floorMap[i][j];
                }
            }
        return result;
    }

    public int[][] getPlayerStats() {
    	int[][] playerStats = new int[4][3];
    	for (int i = 0; i < 4; i++) {
    		playerStats[i][0] = players[i].getHp();
			playerStats[i][1] = players[i].getAttack();
			playerStats[i][2] = players[i].getSpeed();
    	}
    	return playerStats;
    }

    public void setStarted() {
    	hasStarted = true;
    }

    public void setDifficulty(int inputDifficulty) {
    	difficulty = inputDifficulty;
    }

    public void setTurnLength(int inputTurnLength) {
    	turnLength = inputTurnLength;
    }

    public boolean getHasStarted() {
    	return hasStarted;
    }
    
    public int getPlayerNumber() {
    	return playerNumber;
    }
    
    public Player[] getPlayers() {
    	return players;
    }
    
    public boolean getPhaseTwo() {
    	return pt.getPhaseTwo();
    }
    
    public int getEndGame() {
    	return pt.getEndGame();
    }
    
    public int getVillainId() {
    	return villainId;
    }

    public String getVillainName() {
    	if(getVillainId() != -1) {
    		return players[villainId].getName();
    	}
    	return "";
    }

    public boolean getKey() {
    	return pt.getKey();
    }
    
    public int[] getKeyPosition() {
    	return pt.getKeyPosition();
    }
    
    public int[] getEscapePosition() {
    	return pt.getEscapePosition();
    }

    public boolean getHasWon() {
    	return pt.getHasWon();
    }

    public void reset() {
    	totalTurn = 0;
    	currentEvent = -1;
    	villainId = -1;
		healthToLose = 1;
		map = new Map();
		map.initializeMap();
		map.createFloorMap();
		id_Name = new HashMap<Integer, String>();
		players = new Player[maxPlayerNumber];
		playerNumber = 0;
		pt.reset();
    }

    public void moveIfDead() {
    	System.out.println("Move if dead");
    	for (int i = 0; i < 4; i++ ) {
    		if (players[i].getHp() <= 0) {
    			System.out.println("Inside if");
    			players[i].updateLocation(20,20);
    		}
    	}
    }
}

