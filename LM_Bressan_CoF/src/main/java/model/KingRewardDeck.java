package model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Pietro Di Marco on 14/05/16.
 * Enumeration of bonus.
 */
public class KingRewardDeck {

    /**
     * The KingReward pile.
     */
    private Queue<KingRewardTile> deck;

    /**
     * This constructor create the deck with 5 KingRewardTile.
     */
    public KingRewardDeck() {
        deck=new LinkedList<KingRewardTile>();
        deck.add(new KingRewardTile(35));
        deck.add(new KingRewardTile(30));
        deck.add(new KingRewardTile(25));
        deck.add(new KingRewardTile(20));
        deck.add(new KingRewardTile(15));
    }

    /**
     * @return the head of the queue, it's used when a player win a KingRewardTile.
     */
    public KingRewardTile getKingReward() {
        return deck.poll();
    }

}