package event;

import java.util.HashMap;
import java.util.Random;

import player.Player;

public class EventList {
	private Functions functions;
	private Random rd;

	private int numberOfEvents = 6;
	private HashMap<Integer, String> id_Description = new HashMap<Integer, String>();

    public EventList() {
    	functions = new Functions();
    	rd = new Random();
    	generateEvents();
    }
    
    //to replace 'you', use an array list. 


    private void generateEvents() {

    	id_Description.put(0, "You feel a tingling feeling crawing up your leg, you look down and see thousands of spiders! Perform a roll, Take 1 damage if you roll less than 3.");
    	
    	id_Description.put(1, "You have set off a trap and see arrows being shot at you. You will now perform a roll. Take 3 damage if you roll less than 2.");

    	id_Description.put(2, "A book flies out of nowhere and hits you. Perform a roll. If you roll higher than 2 you can read the contents of the book and learn how to run faster!");
    	
    	id_Description.put(3, "You notice a pile of bones...Suddenly a ghost appears out of the ground! Perform a roll. If you roll higher than a 4, gain 1 speed.");
    	
    	id_Description.put(4, "You find a mysterious bottle of liquid sitting in the middle of the room. You drink the solution and feel your arms start to shake uncontrollably. If you roll higher than a 4, gain one strength. ");
    	
    	id_Description.put(5, "You enter the room and see a man holding his decapitated head. The man sprints. Perform a roll, if you roll higher than a 6, your speed will increase. If you roll less than a 2, lose a point from each stat.");

		id_Description.put(6, "You see a coffin and you open it. You see yourself inside the coffin. If you roll higher than a 4, gain 1 strength. else, lose 1 strength");

		id_Description.put(7, "Plaster from the ceiling starts breaking apart and falling on you. If you roll higher than a 3, gain 1 speed. else, lose 1 health");

		id_Description.put(8, "Blood starts dripping from the ceiling on to you. If you roll higher than a 5, gain 2 speed. else, lose 1 strength and 1 health");

		id_Description.put(9, "You hear a piercing scream so loud it starts to hurt your ears. If you roll lower than a 3, lose 2 health");
    }

    
    // return event id
    public int getEvent() {
    	int temp = rd.nextInt(numberOfEvents);
    	return temp;
    }
    
    public String getEventDescription(int id) {
    	System.out.println(id_Description.get(id));
        return id_Description.get(id);
    }
    
    // Trigger event base on the id, dice roll and player
    public String triggerEvent(int functionId, int diceRoll, Player p) {
    	return functions.chooseType(functionId, diceRoll, p);
    }
}
