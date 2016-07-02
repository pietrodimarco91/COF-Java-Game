package controller;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomPlayerIterator implements Iterator<Player> {

    private Iterator<Player> randomIterator;

    public RandomPlayerIterator(List<Player> players) {
        Random random = new Random();
        Set<Player> playerSet = new LinkedHashSet<>();
        while(playerSet.size() != players.size())
            playerSet.add(players.get(random.nextInt(players.size())));
        randomIterator = playerSet.iterator();
    }

    @Override
    public boolean hasNext() {
        return randomIterator.hasNext();
    }

    @Override
    public Player next() {
        return randomIterator.next();
    }
}
