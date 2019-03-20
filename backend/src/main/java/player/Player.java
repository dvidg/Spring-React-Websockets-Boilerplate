package player;

public class Player {

   
    private int id;
    private String name;
    private int motivation;
    private int hp; //max 20
    private int attack; //max 10
    private int speed; //max 10
    private int[] coord;
    private boolean withKey = false;
  
	public Player(int id, String name, int row, int col, int speed) {  // p is the player's picture in game
        coord = new int[2];
        this.name = name;
        coord[0] = row;
        coord[1] = col;
        this.id = id;
    	hp = 10;
    	attack = 4;
    	this.speed = speed;	
    }
    
    public int getId() {
    	return id;
    }
    
    public String getName() {
    	return name;
    }
    
    // getter and setter for hp
    public int getHp() {
        return hp;
    }
    
    public void setHp(int h) {
    	if(h >20) {
    		h = 20;
    	} else if(h < 0) {
            h = 0;
        }
        hp = h;
    }
    
    // getter and setter for attack
    public int getAttack() {
        return attack;
    }
    
    public void setAttack(int a) {
        if(a > 10) {
        	a = 10;
        }
    	attack = a;
    }
    
    // getter and setter for speed
    public int getSpeed() {
        return speed;
    }
    
    public void setSpeed(int s) {
    	if(s > 10) {
    		s = 10;
    	}
        speed=s;
    }
    
    // getter and setter for motivation
    public int getMotivation() {
    	return motivation;
    }
    
    public void setMotivation(int motivation) {
    	this.motivation = motivation;
    }
    
    // getter and setter for withKey
    public boolean getWithKey() {
    	return withKey;
    }
    
    public void setWithKey(boolean b) {
    	withKey = b;
    }
    
    //get player location
    public int[] getLocation() {
    	return coord;
    }    

    //update player location;
    public void updateLocation(int rol, int col) {
        coord[0] = rol;
        coord[1] = col;
    } 

    public void reset() {
        motivation = 0;
        hp = 10;
        attack = 5;
        speed = 5;
        withKey = false;
    }
}
