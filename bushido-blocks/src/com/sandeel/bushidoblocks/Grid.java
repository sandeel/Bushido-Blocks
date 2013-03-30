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

import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;

/*
 * the gameplay grid which blocks go on
 */
public class Grid {

    /* array holding the grid's layout*/
    List<GridSpace> gridSpaces = new LinkedList<GridSpace>();
    
    //some things declared here in attempt at optimising
    GridSpace space;
    List<GridSpace> row = new LinkedList<GridSpace>();  
    HashSet<GridSpace> emptyMatches;
    HashSet<GridSpace> matches;
        
    public Grid() {
        
        // create the grid
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 9; y++) {
                GridSpace space = new GridSpace(this);
                space.setGridPosX(x);
                space.setGridPosY(y);
                
                // populate the gridspace with a block initially
                space.setBlock(new Block());
                
                // add the space to the list of spaces
                gridSpaces.add(space);
            }
        }
    }
    
    /* get number of empty spaces on the grid */
    public int countEmptySpaces() {
        
        int count = 0;
        
        for (GridSpace space : gridSpaces) {
            if (!space.hasBlock()) {
                count++;
            }
        }
        
        return count;
    }
    
    /* returns true if there's an empty space */
    public boolean hasEmptySpace() {
    
        for (GridSpace space : gridSpaces) {
            if (!space.hasBlock()) {
                return true;
            }
        }
        
        return false;
    }
    
    /* check if there's an empty space in the rop row of the grid */
    public boolean gapInTopRow() {
        
        for (GridSpace space : getTopRow()) {
            if (!space.hasBlock()) {
                return true;
            }
        }
        
        return false;
    }
    
    /* check if a space is occupied by a block */
    public boolean checkIfOccupied(int x, int y) {
        
        for (GridSpace space : gridSpaces) {
            if (space.getX() == x && space.getY() == y) {
                if (space.hasBlock()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /* whether the grid has a super block or not */
    public boolean hasSuperBlock() {
                
        for (GridSpace space : gridSpaces) {
            if (space.hasBlock()) {
                if (space.getBlock().getColour() == COLOURS.SUPER_HORIZONTAL
                    || space.getBlock().getColour() == COLOURS.SUPER_VERTICAL
                    || space.getBlock().getColour() == COLOURS.SUPER_2WAYS) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /*
     * returns the number of matches on the grid
     * note: may be slow. Use hasMatch() if possible!!
     */
    public int countMatches() {
        
        int count = 0;
      
        for (GridSpace space : gridSpaces) {
            
            if (space.hasBlock()) {
                  emptyMatches = new HashSet<GridSpace>();
                  matches = getMatches(space.getX(), space.getY(), space.getBlock().getColour(), emptyMatches);
                  
                  if (matches.size() >= 3) {
                      count++;
                  }
              }
          }
        
          return count;
    }
    
    /*
     * breaks and returns true if match on the grid
     */
    public boolean hasMatch() {
      
        for (GridSpace space : gridSpaces) {
            
            if (space.hasBlock()) {
                  emptyMatches = new HashSet<GridSpace>();
                  matches = getMatches(space.getX(), space.getY(), space.getBlock().getColour(), emptyMatches);
                  
                  if (matches.size() >= 3) {
                      return true;
                  }
              }
          }
        
          return false;
    }
    
    /* get a block's matches */
    public HashSet<GridSpace> getMatches (int x, int y, COLOURS colour, HashSet<GridSpace> matches) {
        
        GridSpace space = getGridSpace(x,y);
        
        // the Algorithm
        // an attempt at a flood fill
        // see http://en.wikipedia.org/wiki/Flood_fill
        
        if (!space.hasBlock() || (space.getBlock().getColour() != colour)) {
            return matches;
        }
        
        if (space.hasBlock() && space.getBlock().getColour() == colour && !matches.contains(space)) {
            matches.add(space);
        }
        
        if (space.hasGridSpaceLeft() && space.getGridSpaceLeft().hasBlock() && !matches.contains(space.getGridSpaceLeft()) && space.getGridSpaceLeft().getBlock().getColour() == colour) {
            for (GridSpace matchedSpace : getMatches(space.getGridSpaceLeft().getX(), space.getGridSpaceLeft().getY(), colour, matches)) {
                if (!matches.contains(matchedSpace)) {
                    matches.add(matchedSpace);
                }
            }
        }
        
        if (space.hasGridSpaceRight() && space.getGridSpaceRight().hasBlock() && !matches.contains(space.getGridSpaceRight()) && space.getGridSpaceRight().getBlock().getColour() == colour) {
            for (GridSpace matchedSpace : getMatches(space.getGridSpaceRight().getX(), space.getGridSpaceRight().getY(), colour, matches)) {
                if (!matches.contains(matchedSpace)) {
                    matches.add(matchedSpace);
                }
            }
        }
        
        if (space.hasGridSpaceAbove() && space.getGridSpaceAbove().hasBlock() && !matches.contains(space.getGridSpaceAbove()) && space.getGridSpaceAbove().getBlock().getColour() == colour) {
            for (GridSpace matchedSpace : getMatches(space.getGridSpaceAbove().getX(), space.getGridSpaceAbove().getY(), colour, matches)) {
                if (!matches.contains(matchedSpace)) {
                    matches.add(matchedSpace);
                }
            }
        } 
        
        if (space.hasGridSpaceBelow() && space.getGridSpaceBelow().hasBlock() && !matches.contains(space.getGridSpaceBelow()) && space.getGridSpaceBelow().getBlock().getColour() == colour) {
            for (GridSpace matchedSpace : getMatches(space.getGridSpaceBelow().getX(), space.getGridSpaceBelow().getY(), colour, matches)) {
                if (!matches.contains(matchedSpace)) {
                    matches.add(matchedSpace);
                }
            }
        }
        
        return matches;
    }
    
    /* move a block from one GridSpace to another */
    public void moveBlock(GridSpace source, GridSpace dest) {
        
        dest.setBlock(source.getBlock());
        
        // delete old gridpspace's reference to the block
        source.deleteBlock();
    }   
    
    /* get all the gridspaces*/
    public List<GridSpace> getSpaces() {
        return gridSpaces;
    }
    
    /* get a specific gridspace */
    public GridSpace getGridSpace(int x, int y) {
        for (GridSpace space : gridSpaces) {
            if (space.getX() == x && space.getY() == y) {
                    return space;
            }
        }
        return null;
    }
    
    /* get top row only*/
    public List<GridSpace> getTopRow() {
        return getRow(8);
    }
    
    /* get a particular row */
    public List<GridSpace> getRow (int y) {
        if (!row.isEmpty()) row.clear();
        for (int x = 0; x < 7; x++) {
            row.add(getGridSpace(x, y));
        }
        return row;
    }
    
    /* get a particular column */
    public List<GridSpace> getColumn (int x) {
        if (!row.isEmpty()) row.clear();
        for (int y = 0; y < 9; y++) {
            row.add(getGridSpace(x, y));
        }
        return row;
    }
}
