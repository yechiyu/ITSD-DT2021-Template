package structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import commands.BasicCommands;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {

	private Player[] players = new Player[2];
	private List<Tile> tiles;
	private List<Unit> units = new ArrayList<Unit>();
	private List<Card> handCards =  new ArrayList<Card>();
	private List<Unit> handUnits = new ArrayList<Unit>();
	private List<Unit> tileUnits = new ArrayList<>();
	private List<Unit> tileAIUnits = new ArrayList<>();
	private Map<Unit, List<String>> turnTileUnits = new HashMap<>();
	private Map<Unit, List<String>> turnTileAIUnits = new HashMap<>();
	private List<String> cards = new ArrayList<String>();
	private int turnTimes = 3;
	private int stateMark = 0;  // 1. 为唤起全部tile 2.手中有牌 3.唤起了一个Unit
	private Tile [][] gridTile = new Tile[9][5];
	private Unit selectedUnit = new Unit();
	private Card selectedCard = new Card();
	private Unit tileSelectUnit = new Unit();
	private int selectPosition;
	private List<Card> turnCards = new ArrayList<Card>();
	private int handCardDeal = 0;
	private int unitIDcout = 1;
	private int AIunitIDcout = 1;
	private List<Unit> attackTileList = new ArrayList<Unit>();
//	private Map<Integer , Card> deleteHandCards = new HashMap<>();
	
	public GameState() {
		for(int i=0; i < 2; i++) {
			this.cards.add("truestrike");
			this.cards.add("sundrop_elixir");
			this.cards.add("comodo_charger");
			this.cards.add("azure_herald");
			this.cards.add("azurite_lion");
			this.cards.add("fire_spitter");
			this.cards.add("hailstone_golem");
			this.cards.add("ironcliff_guardian");
			this.cards.add("pureblade_enforcer");
			this.cards.add("silverguard_knight");
		}
		
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 9; j++) {
				this.gridTile[j][i] = BasicObjectBuilders.loadTile(j, i);
			}
		}
	}
	
	public void initialize(Player player1, Player player2) {
		this.players[0] = player1;
		this.players[1] = player2;
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
	public List<Tile> getTiles() {
		return tiles;
	}
	
	public List<Unit> getUnits() {
		return units;
	}
	
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	
	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
	}
	
	public void setUnits(List<Unit> units) {
		this.units = units;
	}
	
	public int getTurnTimes() {
		return turnTimes;
	}
	
	public void setTurnTimes(int turnTimes) {
		this.turnTimes = turnTimes;
	}
	
	public List<Card> getHandCards() {
		return handCards;
	}
	
	public void setHandCards(List<Card> handCards) {
		this.handCards = handCards;
	}
	
	public void addHandCard(Card card) {
		this.handCards.add(card);
	}
	
	public void removeHandCard(Card card) {
		this.handCards.remove(card);
	}
	
	public List<String> getCards() {
		return cards;
	}
	
	public void setCards(List<String> cards) {
		this.cards = cards;
	}
	
	public void removeCard(String cardName) {
		this.cards.remove(cardName);
	}
	
	public Tile[][] getGridTile() {
		return gridTile;
	}
	
	public void setGridTile(Tile[][] gridTile) {
		this.gridTile = gridTile;
	}
	
	public void addUnit(Unit unit) {
		this.units.add(unit);
	}
	
	public int getStateMark() {
		return stateMark;
	}
	
	public void setStateMark(int stateMark) {
		this.stateMark = stateMark;
	}
	
	public Unit getSelectedUnit() {
		return selectedUnit;
	}
	
	public void setSelectedUnit(Unit selectedUnit) {
		this.selectedUnit = selectedUnit;
	}
	
	public List<Unit> getHandUnits() {
		return handUnits;
	}
	
	public void setHandUnits(List<Unit> handUnits) {
		this.handUnits = handUnits;
	}
	
	public void addHandUnits(Unit unit) {
		this.handUnits.add(unit);
	}
	
	public void deleteHandUnit(Unit unit) {
		this.handUnits.remove(unit);
	}
	
	public Card getSelectedCard() {
		return selectedCard;
	}
	
	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}
	
	public int getSelectPosition() {
		return selectPosition;
	}
	
	public void setSelectPosition(int selectPosition) {
		this.selectPosition = selectPosition;
	}
	
	public void setTurnCards(List<Card> turnCards) {
		this.turnCards = turnCards;
	}
	
	public List<Card> getTurnCards() {
		return turnCards;
	}
	
	public void addTurnCard(Card card) {
		this.turnCards.add(card);
	}
	
	public int getHandCardDeal() {
		return handCardDeal;
	}
	
	public void setHandCardDeal(int handCardDeal) {
		this.handCardDeal = handCardDeal;
	}
	
	public Unit getUnitByPosition(int x, int y) {
		for (int i = 0; i < this.tileUnits.size(); i++) {
			if (this.tileUnits.get(i).getPosition().getTilex() == x && this.tileUnits.get(i).getPosition().getTiley() == y) {
				return this.tileUnits.get(i);
			}
		}
		return null;
	}
	
	public void setUnitByPosition(int x, int y, Unit transitUnit) {

		List<Unit> modifyUnits = new ArrayList<Unit>();
		for (int i = 0; i < this.tileUnits.size(); i++) {
			if (this.tileUnits.get(i).getPosition().getTilex() == x && this.tileUnits.get(i).getPosition().getTiley() == y) {
				modifyUnits.add(transitUnit);
				continue;
			}
			modifyUnits.add(tileUnits.get(i));
		}

		this.tileUnits.clear();
		this.tileUnits.addAll(modifyUnits);
	}
	
	
	public Unit getAIUnitByPosition(int x, int y) {
		for (int i = 0; i < this.tileAIUnits.size(); i++) {
			if (this.tileAIUnits.get(i).getPosition().getTilex() == x && this.tileAIUnits.get(i).getPosition().getTiley() == y) {
				return this.tileAIUnits.get(i);
			}
		}
		return null;
	}
	
	
	public Unit getTileSelectUnit() {
		return tileSelectUnit;
	}
	
	public void setTileSelectUnit(Unit tileSelectUnit) {
		this.tileSelectUnit = tileSelectUnit;
	}
	
	public void initTileSelectUnitID(int num) {
		this.selectedUnit.setId(num);
	}
	
	public List<Unit> getTileUnits() {
		return tileUnits;
	}
	
	public void setTileUnits(List<Unit> tileUnits) {
		this.tileUnits = tileUnits;
	}
	
	public void addTileUnit(Unit unit) {
		unit.setId(unitIDcout);
		this.tileUnits.add(unit);
	}
	
	public int getUnitIDcout() {
		return unitIDcout;
	}
	
	public void setUnitIDcout(int unitIDcout) {
		this.unitIDcout = unitIDcout;
	}
	
	public void addUnitIDcout() {
		this.unitIDcout += 1;
	}
	
	public void addAIUnitIDcout() {
		this.AIunitIDcout += 1;
	}
	
	public Map<Unit, List<String>> getTurnTileUnits() {
		return this.turnTileUnits;
	}
//	
//	public void setTurnTileUnits(Map<Unit, List> turnTileUnits) {
//		this.turnTileUnits = turnTileUnits;
//	}
	
	public void addTurnTileUnits(Unit unit, List stateList) {
		unit.setId(unitIDcout);
		this.turnTileUnits.put(unit, stateList);
	}
	
	public List<Unit> getTileAIUnits() {
		return tileAIUnits;
	}
	
	public void setTileAIUnits(List<Unit> tileAIUnits) {
		this.tileAIUnits = tileAIUnits;
	}
	
	public void addTileAIUnits(Unit unit) {
		unit.setId(this.AIunitIDcout);
		this.tileAIUnits.add(unit);
	}
	
	public List<Unit> getAttackTileList() {
		return attackTileList;
	}
	
	public void setAttackTileList(List<Unit> attackTileList) {
		this.attackTileList = attackTileList;
	}
	
	public void addAttackTileList(Unit unit) {
		this.attackTileList.add(unit);
	}
	
	public Map<Unit, List<String>> getTurnTileAIUnits() {
		return turnTileAIUnits;
	}
	
	public void setTurnTileAIUnits(Map<Unit, List<String>> turnTileAIUnits) {
		this.turnTileAIUnits = turnTileAIUnits;
	}

	public void addTurnTileAIUnits(Unit unit, List<String> stateList) {
		unit.setId(unitIDcout);
		this.turnTileAIUnits.put(unit, stateList);
	}
	
	public Unit getTurnTileAiUniteBySite(int x, int y) {
		Iterator it = this.turnTileAIUnits.keySet().iterator();
		while(it.hasNext()) {
//			Unit currentUnit = it.next();
			System.out.println(it.next());
//			if (currentUnit.getPosition().getTilex() == x && currentUnit.getPosition().getTiley() == y) {
//				return currentUnit;
//			}
		}
		
		return null;
	}
	
	public void reduceTurnTileAIUnitsHealth(Unit unit, Integer damage) {
		List<String> stateList = new ArrayList<String>();
		stateList.add(this.turnTileAIUnits.get(unit).get(0));
		int newHealth = Integer.parseInt(this.turnTileAIUnits.get(unit).get(1))-damage;
		stateList.add(String.valueOf(newHealth));
		this.turnTileAIUnits.remove(unit);
		this.turnTileAIUnits.put(unit, stateList);
	}
}
