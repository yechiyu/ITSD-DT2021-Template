package events;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 * 
 * { 
 *   messageType = “tileClicked”
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();
		
		System.out.println("curent game state:"+ gameState.getStateMark());
		boolean xcondition = gameState.getUnitByPosition(tilex, tiley) != null;
		System.out.println("conditional:"+xcondition);
		
		if (gameState.getStateMark()==2 && gameState.getUnitByPosition(tilex, tiley) == null) {
			if (gameState.getPlayers()[0].getMana() >= gameState.getSelectedCard().getManacost()) {
				for(int k = 0; k < gameState.getTileUnits().size(); k++) {

					for(int i = gameState.getTileUnits().get(k).getPosition().getTiley()-2; i <= gameState.getTileUnits().get(k).getPosition().getTiley()+2; i++) {
						if (i < 0 || i > 4) {
							continue;
						}
						outer1: for(int j = gameState.getTileUnits().get(k).getPosition().getTilex()-2; j <= gameState.getTileUnits().get(k).getPosition().getTilex()+2; j++) {
							if (j < 0 || j > 8) {
								continue outer1;
							}
							if ((i == gameState.getTileUnits().get(k).getPosition().getTiley() || j == gameState.getTileUnits().get(k).getPosition().getTilex()) || (Math.abs(gameState.getTileUnits().get(k).getPosition().getTiley()-i) == 1 && Math.abs(gameState.getTileUnits().get(k).getPosition().getTilex()-j) == 1)) {
								for(int l = 0; l < gameState.getTileUnits().size(); l++) {
									
									if (j == gameState.getTileUnits().get(l).getPosition().getTilex() && i == gameState.getTileUnits().get(l).getPosition().getTiley()) {
										continue outer1;
									}
								}
								
								if(tilex==j && tiley == i) {
									drawCardToTile(out, gameState, message, tilex, tiley);
								}
							}
						}
						try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
					}
				}
				
			}
			
		} else if(gameState.getStateMark()==2 && gameState.getUnitByPosition(tilex, tiley) != null) {
			System.out.println("tile units ID:"+gameState.getUnitByPosition(tilex, tiley).getId());
			for(int i = 0; i < 5; i++) {
				outer1: for(int j = 0; j < 9; j++) {
					for(int k = 0; k < gameState.getTileUnits().size(); k++) {
						
						if (j == gameState.getTileUnits().get(k).getPosition().getTilex() && i == gameState.getTileUnits().get(k).getPosition().getTiley()) {
							continue outer1;
						}
					}
					BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 0);
				}
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
			}
			
			Set<int[]> cutTile = new HashSet<int[]>();
			for(int i = tiley-2; i <= tiley+2; i++) {
				if (i < 0 || i > 4) {
					continue;
				}
				outer1: for(int j = tilex-2; j <= tilex+2; j++) {
					if (j < 0 || j > 8) {
						continue outer1;
					}
					if ((i == tiley || j == tilex) || (Math.abs(tiley-i) == 1 && Math.abs(tilex-j) == 1)) {
						for(int k = 0; k < gameState.getTileUnits().size(); k++) {

							if ((Math.abs(gameState.getTileUnits().get(k).getPosition().getTilex()-tilex) == 1)&&(Math.abs(gameState.getTileUnits().get(k).getPosition().getTiley()-tiley) == 0)) {
								int[] arrcurrent = {tilex+2*(gameState.getTileUnits().get(k).getPosition().getTilex()-tilex),tiley};
								cutTile.add(arrcurrent);
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
							} else if ((Math.abs(gameState.getTileUnits().get(k).getPosition().getTiley()-tiley) == 1)&&(Math.abs(gameState.getTileUnits().get(k).getPosition().getTilex()-tilex) == 0)) {
								int[] arrcurrent = {tilex,tiley+2*(gameState.getTileUnits().get(k).getPosition().getTiley()-tiley)};
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
								cutTile.add(arrcurrent);
							}
							
							if (j == gameState.getTileUnits().get(k).getPosition().getTilex() && i == gameState.getTileUnits().get(k).getPosition().getTiley()) {
								continue outer1;
							}
						}

						for(int k = 0; k < gameState.getTileAIUnits().size(); k++) {
							if ((Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTilex()-tilex) == 1)&&(Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTiley()-tiley) == 0)) {
								int[] arrcurrent = {tilex+2*(gameState.getTileAIUnits().get(k).getPosition().getTilex()-tilex),tiley};
								cutTile.add(arrcurrent);
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
							} else if ((Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTiley()-tiley) == 1)&&(Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTilex()-tilex) == 0)) {
								int[] arrcurrent = {tilex,tiley+2*(gameState.getTileAIUnits().get(k).getPosition().getTiley()-tiley)};
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
								cutTile.add(arrcurrent);
							}
							
							if (Math.abs(i - tiley) == 1 || Math.abs(j - tilex) == 1) {
								if (gameState.getTileAIUnits().get(k).getPosition().getTilex() == j && gameState.getTileAIUnits().get(k).getPosition().getTiley() == i) {
									BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 2);
									gameState.addAttackTileList(gameState.getTileAIUnits().get(k));
									continue outer1;
								}
							}

							if (gameState.getTileAIUnits().get(k).getPosition().getTilex() == j && gameState.getTileAIUnits().get(k).getPosition().getTiley() == i) {
								BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 0);
								continue outer1;
							}
						}
						
						BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 1);
					}
				}
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
			}
			
			Iterator<int[]> it = cutTile.iterator();
			while (it.hasNext()) {
				int[] nowArray = it.next();
				BasicCommands.drawTile(out, gameState.getGridTile()[nowArray[0]][nowArray[1]], 0);
				if (gameState.getUnitByPosition(tilex, tiley+1) != null && gameState.getUnitByPosition((int)(tilex+nowArray[0])/2, nowArray[1]) != null) {
					BasicCommands.drawTile(out, gameState.getGridTile()[(int)(tilex+nowArray[0])/2][nowArray[1]+1], 0);
				}
				if (gameState.getUnitByPosition(tilex, tiley-1) != null && gameState.getUnitByPosition((int)(tilex+nowArray[0])/2, nowArray[1]) != null) {
					BasicCommands.drawTile(out, gameState.getGridTile()[(int)(tilex+nowArray[0])/2][nowArray[1]-1], 0);
				}
//				
//				if (gameState.getUnitByPosition(tilex+1, tiley) != null) {
//					
//				}
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
				
			}
			
			gameState.setStateMark(3);
			gameState.setTileSelectUnit(gameState.getUnitByPosition(tilex, tiley));
		} else if (gameState.getStateMark() == 3 && gameState.getUnitByPosition(tilex, tiley) != null) {
			System.out.println("tile units ID:"+gameState.getUnitByPosition(tilex, tiley).getId());
			for(int i = 0; i < 5; i++) {
				outer1: for(int j = 0; j < 9; j++) {
					for(int k = 0; k < gameState.getTileUnits().size(); k++) {
						
						if (j == gameState.getTileUnits().get(k).getPosition().getTilex() && i == gameState.getTileUnits().get(k).getPosition().getTiley()) {
							continue outer1;
						}
					}
					BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 0);
				}
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
			}
			
			Set<int[]> cutTile = new HashSet<int[]>();
			for(int i = tiley-2; i <= tiley+2; i++) {
				if (i < 0 || i > 4) {
					continue;
				}
				outer1: for(int j = tilex-2; j <= tilex+2; j++) {
					if (j < 0 || j > 8) {
						continue outer1;
					}
					if ((i == tiley || j == tilex) || (Math.abs(tiley-i) == 1 && Math.abs(tilex-j) == 1)) {
						for(int k = 0; k < gameState.getTileUnits().size(); k++) {

							if ((Math.abs(gameState.getTileUnits().get(k).getPosition().getTilex()-tilex) == 1)&&(Math.abs(gameState.getTileUnits().get(k).getPosition().getTiley()-tiley) == 0)) {
								int[] arrcurrent = {tilex+2*(gameState.getTileUnits().get(k).getPosition().getTilex()-tilex),tiley};
								cutTile.add(arrcurrent);
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
							} else if ((Math.abs(gameState.getTileUnits().get(k).getPosition().getTiley()-tiley) == 1)&&(Math.abs(gameState.getTileUnits().get(k).getPosition().getTilex()-tilex) == 0)) {
								int[] arrcurrent = {tilex,tiley+2*(gameState.getTileUnits().get(k).getPosition().getTiley()-tiley)};
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
								cutTile.add(arrcurrent);
							}
							
							if (j == gameState.getTileUnits().get(k).getPosition().getTilex() && i == gameState.getTileUnits().get(k).getPosition().getTiley()) {
								continue outer1;
							}
						}
						
						for(int k = 0; k < gameState.getTileAIUnits().size(); k++) {

							if ((Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTilex()-tilex) == 1)&&(Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTiley()-tiley) == 0)) {
								int[] arrcurrent = {tilex+2*(gameState.getTileAIUnits().get(k).getPosition().getTilex()-tilex),tiley};
								cutTile.add(arrcurrent);
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
							} else if ((Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTiley()-tiley) == 1)&&(Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTilex()-tilex) == 0)) {
								int[] arrcurrent = {tilex,tiley+2*(gameState.getTileAIUnits().get(k).getPosition().getTiley()-tiley)};
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
								cutTile.add(arrcurrent);
							}
							
							
							if (Math.abs(i - tiley) == 1 || Math.abs(j - tilex) == 1) {
								if (gameState.getTileAIUnits().get(k).getPosition().getTilex() == j && gameState.getTileAIUnits().get(k).getPosition().getTiley() == i) {
									BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 2);
									gameState.addAttackTileList(gameState.getTileAIUnits().get(k));
									continue outer1;
								}
							}
							

							if (gameState.getTileAIUnits().get(k).getPosition().getTilex() == j && gameState.getTileAIUnits().get(k).getPosition().getTiley() == i) {
								BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 0);
								continue outer1;
							}
						}
						
						BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 1);
					}
				}
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
			}
			
			Iterator<int[]> it = cutTile.iterator();
			while (it.hasNext()) {
				int[] nowArray = it.next();
				if (gameState.getUnitByPosition(tilex, tiley+1) != null && gameState.getUnitByPosition((int)(tilex+nowArray[0])/2, nowArray[1]) != null) {
					BasicCommands.drawTile(out, gameState.getGridTile()[(int)(tilex+nowArray[0])/2][nowArray[1]+1], 0);
				}
				if (gameState.getUnitByPosition(tilex, tiley-1) != null && gameState.getUnitByPosition((int)(tilex+nowArray[0])/2, nowArray[1]) != null) {
					BasicCommands.drawTile(out, gameState.getGridTile()[(int)(tilex+nowArray[0])/2][nowArray[1]-1], 0);
				}
				BasicCommands.drawTile(out, gameState.getGridTile()[nowArray[0]][nowArray[1]], 0);
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
				
			}
			
			gameState.setTileSelectUnit(gameState.getUnitByPosition(tilex, tiley));
			
		} else if (gameState.getStateMark() == 3 && gameState.getUnitByPosition(tilex, tiley) == null) {

//			for(int i  = 0; i < gameState.getTileAIUnits().size(); i++) {
//				if (tilex == gameState.getTileAIUnits().get(i).getPosition().getTilex() && tiley == gameState.getTileAIUnits().get(i).getPosition().getTiley()) {
//					return;
//				}
//			}
			int origix = gameState.getTileSelectUnit().getPosition().getTilex();
			int origiy = gameState.getTileSelectUnit().getPosition().getTiley();
			boolean moveCheck = false;
			boolean attackCheck = false;
			Set<int[]> cutTile = new HashSet<int[]>(); //Set
			for(int i = origiy-2; i <= origiy+2; i++) {
				if (i < 0 || i > 4) {
					continue;
				}
				outer1: for(int j = origix-2; j <= origix+2; j++) {
					if (j < 0 || j > 8) {
						continue outer1;
					}
					if ((i == origiy || j == origix) || (Math.abs(origiy-i) == 1 && Math.abs(origix-j) == 1)) {
						for(int k = 0; k < gameState.getTileUnits().size(); k++) {

							if ((Math.abs(gameState.getTileUnits().get(k).getPosition().getTilex()-origix) == 1)&&(Math.abs(gameState.getTileUnits().get(k).getPosition().getTiley()-origiy) == 0)) {
								int[] arrcurrent = {origix+2*(gameState.getTileUnits().get(k).getPosition().getTilex()-origix),origiy};
								cutTile.add(arrcurrent);
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
							} else if ((Math.abs(gameState.getTileUnits().get(k).getPosition().getTiley()-origiy) == 1)&&(Math.abs(gameState.getTileUnits().get(k).getPosition().getTilex()-origix) == 0)) {
								int[] arrcurrent = {origix,origiy+2*(gameState.getTileUnits().get(k).getPosition().getTiley()-origiy)};
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
								cutTile.add(arrcurrent);
							}
							
							if (j == gameState.getTileUnits().get(k).getPosition().getTilex() && i == gameState.getTileUnits().get(k).getPosition().getTiley()) {
								continue outer1;
							}
						}
						for(int k = 0; k < gameState.getTileAIUnits().size(); k++) {
							
							if ((Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTilex()-origix) == 1)&&(Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTiley()-origiy) == 0)) {
								int[] arrcurrent = {origix+2*(gameState.getTileAIUnits().get(k).getPosition().getTilex()-origix),origiy};
								cutTile.add(arrcurrent);
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
							} else if ((Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTiley()-tiley) == 1)&&(Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTilex()-tilex) == 0)) {
								int[] arrcurrent = {origix,origiy+2*(gameState.getTileAIUnits().get(k).getPosition().getTiley()-origiy)};
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
								cutTile.add(arrcurrent);
							}
							
							if (Math.abs(tiley - origiy) == 1 || Math.abs(tilex - origix) == 1) {
								if (gameState.getTileAIUnits().get(k).getPosition().getTilex() == tilex && gameState.getTileAIUnits().get(k).getPosition().getTiley() == tiley) {
									attackCheck = true;
									
									continue outer1;
								}
							}
							
						}
						
						if (j == tilex && i == tiley) {
							moveCheck = true;
						}
					}
				}
				try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
			}
			

//			for(int i = 0; i < cutTile.size(); i++) {
//				if (tilex == cutTile.get(i)[0] && tiley == cutTile.get(i)[1]) {
//					moveCheck = false;
//				}
//				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
//			}
			

			if (!cutTile.isEmpty()) {
				Iterator<int[]> it = cutTile.iterator();
				while (it.hasNext()) {
					int[] nowArray = it.next();
					if (gameState.getUnitByPosition(origix, origiy+1) != null && gameState.getUnitByPosition((int)(origix+nowArray[0])/2, nowArray[1]) != null) {
						if (tilex == (int)(origix+nowArray[0])/2 && tiley == nowArray[1]+1) {
							moveCheck = false;
						}
					}
					if (gameState.getUnitByPosition(origix, origiy-1) != null && gameState.getUnitByPosition((int)(origix+nowArray[0])/2, nowArray[1]) != null) {
						if (tilex == (int)(origix+nowArray[0])/2 && tiley == nowArray[1]-1) {
							moveCheck = false;
						}
					}
					

//					if (gameState.getAIUnitByPosition(origix, origiy+1) != null && gameState.getUnitByPosition((int)(origix+nowArray[0])/2, nowArray[1]) != null) {
//						if (tilex == (int)(origix+nowArray[0])/2 && tiley == nowArray[1]+1) {
//							moveCheck = false;
//						}
//					}
//					if (gameState.getAIUnitByPosition(origix, origiy-1) != null && gameState.getUnitByPosition((int)(origix+nowArray[0])/2, nowArray[1]) != null) {
//						if (tilex == (int)(origix+nowArray[0])/2 && tiley == nowArray[1]-1) {
//							moveCheck = false;
//						}
//					}
					
					if (tilex == nowArray[0] && tiley == nowArray[1]) {
						moveCheck = false;
					}
					try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
					
				}
			}
			
			
			if(attackCheck) {
				System.out.println("there is a anime");
				attackTile(out, gameState, message, tilex, tiley);
				return;
			}
			
			if (moveCheck) {
				if (gameState.getUnitByPosition(origix-1, origiy) != null || gameState.getUnitByPosition(origix+1, origiy) != null) {
					moveUnitToTile(out, gameState, message, tilex, tiley, true);
				}else if (gameState.getAIUnitByPosition(origix-1, origiy) != null || gameState.getAIUnitByPosition(origix+1, origiy) != null) {
					moveUnitToTile(out, gameState, message, tilex, tiley, true);
				}else {
					moveUnitToTile(out, gameState, message, tilex, tiley, false);
				}
			}
		} else if ((gameState.getUnitByPosition(tilex, tiley) != null) && gameState.getStateMark() == 0) {
			for(int i = 0; i < 5; i++) {
				outer1: for(int j = 0; j < 9; j++) {
					for(int k = 0; k < gameState.getTileUnits().size(); k++) {
						
						if (j == gameState.getTileUnits().get(k).getPosition().getTilex() && i == gameState.getTileUnits().get(k).getPosition().getTiley()) {
							continue outer1;
						}
					}
					
					BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 0);
				}
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
			}
			
			Set<int[]> cutTile = new HashSet<int[]>();
			for(int i = tiley-2; i <= tiley+2; i++) {
				if (i < 0 || i > 4) {
					continue;
				}
				outer1: for(int j = tilex-2; j <= tilex+2; j++) {
					if (j < 0 || j > 8) {
						continue outer1;
					}
					if ((i == tiley || j == tilex) || (Math.abs(tiley-i) == 1 && Math.abs(tilex-j) == 1)) {
						for(int k = 0; k < gameState.getTileUnits().size(); k++) {

							if ((Math.abs(gameState.getTileUnits().get(k).getPosition().getTilex()-tilex) == 1)&&(Math.abs(gameState.getTileUnits().get(k).getPosition().getTiley()-tiley) == 0)) {
								int[] arrcurrent = {tilex+2*(gameState.getTileUnits().get(k).getPosition().getTilex()-tilex),tiley};
								cutTile.add(arrcurrent);
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
							} else if ((Math.abs(gameState.getTileUnits().get(k).getPosition().getTiley()-tiley) == 1)&&(Math.abs(gameState.getTileUnits().get(k).getPosition().getTilex()-tilex) == 0)) {
								int[] arrcurrent = {tilex,tiley+2*(gameState.getTileUnits().get(k).getPosition().getTiley()-tiley)};
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
								cutTile.add(arrcurrent);
							}
							
							if (j == gameState.getTileUnits().get(k).getPosition().getTilex() && i == gameState.getTileUnits().get(k).getPosition().getTiley()) {
								continue outer1;
							}
						}
						
						for(int k = 0; k < gameState.getTileAIUnits().size(); k++) {
							
							if ((Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTilex()-tilex) == 1)&&(Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTiley()-tiley) == 0)) {
								int[] arrcurrent = {tilex+2*(gameState.getTileAIUnits().get(k).getPosition().getTilex()-tilex),tiley};
								cutTile.add(arrcurrent);
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
							} else if ((Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTiley()-tiley) == 1)&&(Math.abs(gameState.getTileAIUnits().get(k).getPosition().getTilex()-tilex) == 0)) {
								int[] arrcurrent = {tilex,tiley+2*(gameState.getTileAIUnits().get(k).getPosition().getTiley()-tiley)};
								System.out.println("got a neighboor:"+arrcurrent[0]+","+arrcurrent[1]);
								cutTile.add(arrcurrent);
							}
							
							if (Math.abs(i - tiley) == 1 || Math.abs(j - tilex) == 1) {
								if (gameState.getTileAIUnits().get(k).getPosition().getTilex() == j && gameState.getTileAIUnits().get(k).getPosition().getTiley() == i) {
									BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 2);
									gameState.addAttackTileList(gameState.getTileAIUnits().get(k));
									continue outer1;
								}
							}
							if (gameState.getTileAIUnits().get(k).getPosition().getTilex() == j && gameState.getTileAIUnits().get(k).getPosition().getTiley() == i) {
								BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 0);
								continue outer1;
							}
						}
						
						BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 1);
					}
				}
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
			}
			
			if (!cutTile.isEmpty()) {
				Iterator<int[]> it = cutTile.iterator();
				while (it.hasNext()) {
					int[] nowArray = it.next();
					if (gameState.getUnitByPosition(tilex, tiley+1) != null && gameState.getUnitByPosition((int)(tilex+nowArray[0])/2, nowArray[1]) != null) {
						BasicCommands.drawTile(out, gameState.getGridTile()[(int)(tilex+nowArray[0])/2][nowArray[1]+1], 0);
					}
					if (gameState.getUnitByPosition(tilex, tiley-1) != null && gameState.getUnitByPosition((int)(tilex+nowArray[0])/2, nowArray[1]) != null) {
						BasicCommands.drawTile(out, gameState.getGridTile()[(int)(tilex+nowArray[0])/2][nowArray[1]-1], 0);
					}
					BasicCommands.drawTile(out, gameState.getGridTile()[nowArray[0]][nowArray[1]], 0);
					try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
					
				}
			}
			
			gameState.setStateMark(3);
			gameState.setTileSelectUnit(gameState.getUnitByPosition(tilex, tiley));
			System.out.println("Select Unit:"+gameState.getTileSelectUnit().getPosition().getTilex()+","+gameState.getTileSelectUnit().getPosition().getTiley());
			
		}

		
		
	}
	public void drawCardToTile(ActorRef out, GameState gameState, JsonNode message, int tilex, int tiley) {

		Tile clickedTile = gameState.getGridTile()[tilex][tiley];
		Unit toTile = gameState.getSelectedUnit();
		toTile.setPositionByTile(clickedTile);
		gameState.addUnitIDcout();
		gameState.addTileUnit(toTile);
		int[] unitStates = {gameState.getSelectedCard().getBigCard().getAttack(),gameState.getSelectedCard().getBigCard().getHealth()};
		gameState.addTurnTileUnits(toTile, unitStates);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		gameState.getSelectedUnit().setPositionByTile(clickedTile); 
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.deleteCard(out, gameState.getSelectPosition());
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.drawUnit(out, gameState.getSelectedUnit(), clickedTile);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitAttack(out, gameState.getSelectedUnit(), gameState.getSelectedCard().getBigCard().getAttack());
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, gameState.getSelectedUnit(), gameState.getSelectedCard().getBigCard().getHealth());
//		gameState.addUnit(gameState.getSelectedUnit());
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		gameState.removeHandCard(gameState.getSelectedCard());
		gameState.deleteHandUnit(gameState.getSelectedUnit());
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		gameState.setStateMark(0);
		


		BasicCommands.drawTile(out, gameState.getGridTile()[tilex][tiley], 0);
		gameState.getPlayers()[0].setMana(gameState.getPlayers()[0].getMana()-gameState.getSelectedCard().getManacost());
		BasicCommands.setPlayer1Mana(out, gameState.getPlayers()[0]);
		try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		gameState.setStateMark(2);
		gameState.setHandCardDeal(gameState.getTurnCards().size());
		int markTimes = 1;
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		
		for(int i = 0; i < 5; i++) {
			outer1: for(int j = 0; j < 9; j++) {
				for(int k = 0; k < gameState.getTileUnits().size(); k++) {
					
					if (j == gameState.getTileUnits().get(k).getPosition().getTilex() && i == gameState.getTileUnits().get(k).getPosition().getTiley()) {
						continue outer1;
					}
				}
				BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 0);
			}
			try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		}
		outer2: for(int k = 0; k < gameState.getTurnCards().size(); k++) {
			if (gameState.getTurnCards().get(k).getCardname().equals(gameState.getSelectedCard().getCardname())) {
				markTimes++;
				System.out.println("The card is deleted");
				continue outer2;
			}
			System.out.println("delete posiition:"+markTimes);
			try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
			BasicCommands.deleteCard(out, markTimes);
			
			markTimes++;
			try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		}
		
		for(int i = 0; i < gameState.getHandCards().size(); i++) {
			BasicCommands.drawCard(out, gameState.getHandCards().get(i), i+1, 0);
			try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		}
		
		gameState.getTurnCards().clear();
		for(int i = 0; i < gameState.getHandCards().size(); i++) {
			gameState.addTurnCard(gameState.getHandCards().get(i));
		}
		gameState.setSelectedCard(new Card());
		gameState.setSelectedUnit(new Unit());
		gameState.setSelectPosition(0);
		gameState.setStateMark(0);
	}
	
	public void moveUnitToTile(ActorRef out, GameState gameState, JsonNode message, int tilex, int tiley, boolean moveState) {
		Tile moveToTile = gameState.getGridTile()[tilex][tiley];
		System.out.println("Move From Unit:"+gameState.getTileSelectUnit().getPosition().getTilex()+","+gameState.getTileSelectUnit().getPosition().getTiley());
//		System.out.println("tile units ID:"+gameState.getUnitByPosition(tilex, tiley).getId());
		if (moveState) {
			BasicCommands.moveUnitToTile(out, gameState.getTileSelectUnit(), moveToTile, true);
		} else {
			BasicCommands.moveUnitToTile(out, gameState.getTileSelectUnit(), moveToTile);
		}
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		Unit mediaUnit = gameState.getTileSelectUnit();
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		mediaUnit.setPositionByTile(moveToTile);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		gameState.setUnitByPosition(tilex, tiley, mediaUnit);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}

		BasicCommands.drawTile(out, gameState.getGridTile()[tilex][tiley], 0);
		try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		for(int i = 0; i < 5; i++) {
			outer1: for(int j = 0; j < 9; j++) {
				for(int k = 0; k < gameState.getTileUnits().size(); k++) {
					
					if (j == gameState.getTileUnits().get(k).getPosition().getTilex() && i == gameState.getTileUnits().get(k).getPosition().getTiley()) {
						continue outer1;
					}
				}
				BasicCommands.drawTile(out, gameState.getGridTile()[j][i], 0);
			}
			try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		}
		
		gameState.setStateMark(0);
		gameState.setTileSelectUnit(new Unit());
	}
	
	public void attackTile(ActorRef out, GameState gameState, JsonNode message, int tilex, int tiley) {
		Unit attacker = gameState.getSelectedUnit();
		Unit sufferer = gameState.getTurnTileAiUniteBySite(tilex, tiley);
		BasicCommands.playUnitAnimation(out, attacker, UnitAnimationType.attack);
		try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
		if(gameState.getTurnTileAIUnits()!=null) {
			System.out.println("Ai to string:"+gameState.getTurnTileAIUnits());
		}
//		Iterator<Unit> it = gameState.getTurnTileAIUnits().keySet().iterator();
//		
//		while(it.hasNext()) {
//			Unit currentUnit = it.next();
//		}
		System.out.println("health:"+gameState.getTurnTileUnits().get(attacker)[1]);
		gameState.reduceTurnTileAIUnitsHealth(sufferer, gameState.getTurnTileUnits().get(attacker)[0]);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		BasicCommands.setUnitHealth(out, sufferer, gameState.getTurnTileAIUnits().get(sufferer)[1]-gameState.getTurnTileUnits().get(attacker)[0]);
		try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		if(gameState.getTurnTileAIUnits().get(sufferer)[1]-gameState.getTurnTileUnits().get(attacker)[0] == 0) {
			return;
		}
	}
}
