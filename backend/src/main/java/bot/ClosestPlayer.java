package bot;

import player.*;

public class ClosestPlayer {
	private Player[] players;

	public ClosestPlayer(Player[] players) {
		this.players = players;
	}

	public Player getClosestPlayer(Player bot) {
		int distance = 20;
		Player target = null;

		for(Player p : players) {
			if(!p.getName().equals(bot.getName())) {
				if(getDistance(p, bot) < distance) {
					distance = getDistance(p, bot);
					target = p;
				}
			}
		}
		return target;
	}

	public int getDistance(Player p1, Player p2) {
		int[] location1 = p1.getLocation();
		int[] location2 = p2.getLocation();

		return Math.abs(location1[0] - location2[0]) + Math.abs(location1[1] - location2[1]);
	}
}
