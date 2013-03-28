/*
	Copyright © 2012 Sandeel Software
	
	This file is part of Bushido Blocks.
	    
	Bushido Blocks is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.sandeel.bushidoblocks;

/*
 * simple timer used to countdown remaining time in the game
 */
public class GameTimer {
	
	long timeStarted;
	long timeToFinish;
	
	public GameTimer(long countDownTime) {
		// set initial time remaining
		this.timeToFinish = System.currentTimeMillis() + countDownTime;
	}

	public void start() {
		timeStarted = System.currentTimeMillis();
	}
	
	public void addTime(long timeToAdd) {
		timeToFinish += (timeToAdd * 1000);
	}
	
	public long getTimeRemaining() {
		return (timeToFinish - System.currentTimeMillis());
	}
	
	public int getTimeRemainingInSeconds() {
		return (int)((timeToFinish - System.currentTimeMillis()) / 1000);
	}
}
