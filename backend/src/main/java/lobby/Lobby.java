package gameloop;

import java.util.*;

/* Hold players until game is full and can be initialised */
public class Lobby {

	ArrayList<String> players = new ArrayList<String>();
	ArrayList<String> spectators = new ArrayList<String>();	
	int counter = 0;

	//game capped at 4 players
	public int addPlayer(String userName) {
		if (players.size() < 4) {
			players.add(userName);
			int val = players.size();
			return (val-1);
		} 
		else {
			spectators.add(userName);
			int val = -1 * spectators.size();
			return (val-1);
		}
	}

	public int getHighestPlayerNumber() {
		int highestNumber = 0;
		for (int i = 0; i < players.size(); i++) {
				highestNumber++;
		}
		return highestNumber;
	}

	/**** Spectator Funcs ****/
	public int getSpectatorID(String username) {
		for (int i = 0; i < spectators.size(); i++) {
			if (username == spectators.get(i)) {
				return i;
			}
		}
		return -1; //error
	}

	public ArrayList<String> getSpectatorList() {
		return spectators;
	}

	/**** Player Funcs ****/
	public int getPlayerID(String username) {
		for (int i = 0; i <= 3; i++) {
			if (username == players.get(i)) {
				return i;
			}
		}
		return 4; //error
	}

	public ArrayList<String> getPlayerList() {
		return players;
	}

	public void reset() {
        players.clear();
		spectators.clear();
    }

}
