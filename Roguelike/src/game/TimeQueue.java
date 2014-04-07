/*
 * A type of priority queue that deals with Sentients and their actions.
 */

package game;

import java.util.ArrayList;

import entities.Sentient;

public class TimeQueue {
	private ArrayList<Sentient> sentientQueue;
	private ArrayList<Float> timeQueue;
	
	public TimeQueue() {
		sentientQueue = new ArrayList<Sentient>();
		timeQueue = new ArrayList<Float>();
	}
	
	public int size() {
		return sentientQueue.size();
	}
	
	public void addEventToQueue(Sentient s, float f) {
		if (size() == 0) {
			timeQueue.add(f);
			sentientQueue.add(s);
		} else {
			for (int i = 0; i < this.size(); i++) {
				if (timeQueue.get(i) > f) {
					timeQueue.add(i, f);
					sentientQueue.add(i, s);
				}
			}
		}
	}
	
	private void adjustPriority(float amount) {
		for (Float f : timeQueue) {
			f += amount;
		}
	}
	
	public Sentient getNextEvent() {
		float timePassed = timeQueue.remove(0);
		this.adjustPriority(-timePassed);
		return sentientQueue.remove(0);
	}
}
