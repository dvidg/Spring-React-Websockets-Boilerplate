package phasetwo;

import java.util.Arrays;
import java.util.Random;
import map.Map;
import player.Player;

public class PhaseTwo {
	private boolean phaseTwo = false;
	private int endGame = -1;
	private int[] keyPosition;
	private boolean key = false;
	private int[] escapePosition;
	private Random rd = new Random();
	private boolean hasWon = false;
	
	public PhaseTwo(int[][] originalMap) {
		keyPosition = new int[2];
		escapePosition = new int[2];
		
		// Generate key position
		int temp1 = rd.nextInt(Map.MAP_SIZE);
		int temp2 = rd.nextInt(Map.MAP_SIZE);
		int sign = 0;
			

		while(originalMap[temp1][temp2] == 0) {
			temp1 = rd.nextInt(Map.MAP_SIZE);
			temp2 = rd.nextInt(Map.MAP_SIZE);
		}			
		
		
		keyPosition[0] = temp1;
		keyPosition[1] = temp2;
		
		// Generate escape position
		temp1 = rd.nextInt(Map.MAP_SIZE);
		temp2 = rd.nextInt(Map.MAP_SIZE);
		sign = 0;
		
		while(sign == 0) {
			while(originalMap[temp1][temp2] == 0) {
				temp1 = rd.nextInt(Map.MAP_SIZE);
				temp2 = rd.nextInt(Map.MAP_SIZE);
				
			}
			
			sign = 1;			 
			if(temp1 == keyPosition[0] && temp2 == keyPosition[1]) {
				sign = 0;
				temp1 = rd.nextInt(Map.MAP_SIZE);
				temp2 = rd.nextInt(Map.MAP_SIZE);
			}
						
		}
		
		escapePosition[0] = temp1;
		escapePosition[1] = temp2;
	}
	
	// Determine the end game scenario
	public String choiceScenarios(int i, Player p) {
		switch(i) {
		//increases stats of villain
		case 0:
			p.setHp(p.getHp() + 5); 
			p.setAttack(p.getAttack() + 2);
			p.setSpeed(p.getSpeed() + 2);
			endGame = 0;
			return "Hunt the villain!";
		case 1:
			p.setHp(p.getHp() + 5);
			p.setAttack(p.getAttack() + 2);
			p.setSpeed(p.getSpeed() + 2);
			endGame = 1;
			key = true;
			return "Find the key!";
		default:
			return "error";
		}
	}
	
	public String choiceScenarios() {
		endGame = 2;
		return "Battle Rayole!";
	}
	
	// Check the win conditions
	public String[] winConditionCheck(Player[] players, int villainId) {
		String[] winner;
		int counter = 0;
		
		switch(endGame) {
			/* Hunt the villain */
			case 0:
				/* check HP for both side */
				if(players[villainId].getHp() <= 0) {
					winner = new String[players.length - 1];
					
					for(int i = 0; i < players.length; i++) {
						if(i != villainId) {
							winner[counter] = players[i].getName();
							counter++;
						}
					}
					System.out.println("winner appear!!!"); 
					hasWon = true;
					return winner;
				}
				
				for(Player p : players) {
					if(p.getId() != villainId && p.getHp() == 0) {
						counter++;
					}
				}
				
				if(counter == players.length - 1) {
					winner = new String[1];
					winner[0] = players[villainId].getName();
					System.out.println("winner appear!!!");
					hasWon = true;
					return winner;
				} 
				break;
			
			/* Find the key */
			case 1:
				// check HP for both side
				if(players[villainId].getHp() == 0) {
					winner = new String[players.length - 1];
					
					for(int i = 0; i < players.length; i++) {
						if(i != villainId) {
							winner[counter] = players[i].getName();
							counter++;
						}
					}
					System.out.println("winner appear!!!");
					hasWon = true;
					return winner;
				}
				
				for(Player p : players) {
					if(p.getId() != villainId && p.getHp() <= 0) {
						counter++;
					}
				}
				
				if(counter == players.length - 1) {
					winner = new String[1];
					winner[0] = players[villainId].getName();
					System.out.println("winner appear!!!");
					hasWon = true;
					return winner;
				} 
				
				// check for key
				for(Player p : players) {
					if(Arrays.equals(p.getLocation(), keyPosition)) {
						p.setWithKey(true);
						key = false;
					}
				}
				
				counter = 0;
				
				for(Player p : players) {
					if(Arrays.equals(p.getLocation(), escapePosition)) {
						counter++;
					}
				}
				if(counter >= 2 && !key) {
					counter = 0;
					winner = new String[players.length - 1];
					
					for(int i = 0; i < players.length; i++) {
						if(i != villainId) {
							winner[counter] = players[i].getName();
							counter++;
						}
					}
					System.out.println("winner appear!!!");
					hasWon = true;
					return winner;
				}
				
				break;
			/* Battle Royale */
			case 2:
				int survivor = -1;
				
				for(Player p : players) {
					if(p.getHp() == 0) {
						counter++;
					} else {
						survivor = p.getId();
					}
				}
				
				if(counter == players.length - 1) {
					winner = new String[1];
					winner[0] = players[survivor].getName();
					System.out.println("winner appear!!!");
					hasWon = true;
					return winner;
				}
				break;
				
			default:
				break;
		}
		
		return null;
	}
	
	public boolean getPhaseTwo() {
		return phaseTwo;
	}
	
	public void setPhaseTwo(boolean b) {
		phaseTwo = b;
	}
	
	public boolean getKey() {
		return key;
	}
	
	public void setKey(boolean b) {
		key = b;
	}
	
	public int getEndGame() {
		return endGame;
	}

	public int[] getKeyPosition() {
		return keyPosition;
	}
	
	public int[] getEscapePosition() {
		return escapePosition;
	}
	
	public void setKeyPosition(int[] position) {
		 keyPosition[0] = position[0];
		 keyPosition[1] = position[1];
	}

	public boolean getHasWon() {
		return hasWon;
	}

	public void reset() {
		phaseTwo = false;
		endGame = -1;
		key = false;
		hasWon = false;
	}
}
