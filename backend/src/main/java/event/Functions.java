package event;

import player.Player;

public class Functions {

	public Functions() {
		
	}
	
	// Determine the function of event.
	public String chooseType(int id, int diceRoll, Player p) {
		int difficultyInt = 1;
		switch(id) {

			case 0:
				if(diceRoll < 3 ) {
					p.setHp(p.getHp() - 1);
					p.setMotivation(0);
					return "HP - 1";
				} else if(diceRoll > 4 ) {
					p.setHp(p.getHp() - 1);
					p.setMotivation(0);
					return "HP - 2";
				} else { 
					p.setMotivation(0);
					return "Nothing happened";
				}
			
			case 1: 
				if(diceRoll < 2 ) {
					p.setHp(p.getHp() - 3);
					p.setMotivation(0);
					return "HP - 3";
				} else if(diceRoll > 2 ) {
					p.setHp(p.getHp() - 3);
					p.setMotivation(0);
					return "HP - 1";
				} else { 
					p.setMotivation(0);
					return "Nothing happened"; 
				}
			
			case 2:
				if(diceRoll > 2 ) {
					p.setSpeed(p.getSpeed() + 1);
					p.setMotivation(0);
					return "Speed + 1";
				} else {
					p.setSpeed(p.getSpeed() - 2);
					p.setMotivation(0);
					return "Speed - 2";
				}
			case 3:
				if(diceRoll > 4 ) {
					p.setSpeed(p.getSpeed() + 1);
					p.setMotivation(0);
					return "Speed + 1";
				} else {
					p.setHp(p.getHp() - 1);
					p.setMotivation(0);
					return "HP + 2";
				}	
			case 4:
				if(diceRoll > 4 ) {
					if(diceRoll > 6) {
						p.setAttack(p.getAttack() + 3);
						p.setMotivation(0);
						return "Strength + 3";
					}
					p.setAttack(p.getAttack() + 1);
					p.setMotivation(0);
					return "Strength + 1";	
				} else {
					p.setMotivation(0);
					return "Nothing happened";
				}
			case 5:
				if(diceRoll > 4 ) {
					p.setSpeed(p.getSpeed() + 1);
					p.setMotivation(0);
					return "Speed + 1";
				} else if(diceRoll < 3) {
					p.setSpeed(p.getSpeed() - 1);
					p.setAttack(p.getAttack() - 1);
					p.setHp(p.getHp() - 1);
					p.setMotivation(0);
					return "All stats - 1";
				} else {
					p.setMotivation(0);
					return "Nothing happened";
				}
			case 6:
				if (diceRoll > 2) {
					p.setAttack(p.getAttack() + 1);
					return "Attack + 1";
				} else {
					p.setAttack(p.getAttack() - 1);
					return "Attack - 1";
				}
			case 7:
				if (diceRoll > 3) {
					p.setSpeed(p.getSpeed() + 1);
					return "Speed + 1";
				} else {
					p.setHp(p.getHp() - 1);
					return "Speed - 1";
				}
			case 8:
				if (diceRoll > 5) {
					p.setSpeed(p.getSpeed() + 2);
					return "Speed + 2";
				} else {
					p.setAttack(p.getAttack() - 1);
					p.setHp(p.getHp() - 1);
					return "Attack and Health - 1";
				}
			case 9:
				if (diceRoll < 5) {
					p.setHp(p.getHp() -2);
					return "Health + 2";
				} else {
					p.setMotivation(0);
                    return "Nothing happened";
				} 

			default:
				break;
		}
		
		return "Error";
	}	
}
