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

import com.badlogic.gdx.math.Rectangle;

/*
 * the coloured blocks
 * contained in GridSpaces
 */
public class Block {

	// possible block colours
	final static String[] colours = {"GREEN", "RED", "YELLOW", "PINK", "BLUE"};
	
	// the colour of the block
	String colour;
	
	// rectangle for rendering purposes
	Rectangle rectangle;
	
	// hardcoded block size
	final int blockSize = 60;
		
	// the yoffset is used to simulate a block falling into place
	// causes block to be rendered above it's actual location
	int yOffset;
	
	public Block() {
		// set up the blocks rectangle and initial size
		rectangle = new Rectangle();
		
		rectangle.x = 0;
		rectangle.y = 0;
		
		yOffset = 0;

		rectangle.width = blockSize;
		rectangle.height = blockSize;
		
		// set colour to a random colour from the colours
		int c = (int)(Math.random() * colours.length);
		colour = colours[c];
	}
	
	// overloaded constructor to create a block of a certain colour
	// also used to create super blocks
	public Block(String colour) {
		this();
		
		this.colour = colour;
	}
	
	// called every frame on the block
	// used to apply gravity to the yOffset
	public void update() {
		if (yOffset > 0) {
			yOffset -= 35;
		}
		if (yOffset < 0) {
			yOffset = 0;
		}
	}
	
	public Rectangle getRectangle () {
		return rectangle;
	}
	
	public String getColour() {
		return colour;
	}
	
	public int getYOffset() {
		return yOffset;
	}
	
	public void setYOffset(int y) {
		yOffset = y;
	}
}
