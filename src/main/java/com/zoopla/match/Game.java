package com.zoopla.match;

import java.util.*;

public class Game {
    public static void main(String[] a){
        //read user input
        Scanner sc = new Scanner(System.in);
        int packsOfCards = getPacksOfCards(sc);
        MatchingConditions matchingCondition = getMatchingCondition(sc);

        //set up players
        Player[] players = setUpPlayers();

        //build a pack
        Stack<Card> pack = buildPack();

        //build a pile
        Stack<Card> pile = buildPile(pack, packsOfCards);

        //shuffle all cards
        Collections.shuffle(pile);

        //play the game
        play(players, pile, matchingCondition);

        //print the results
        printResults(players);
    }

    private static void printResults(Player[] players){
        Player winner;
        if(players[0].getAllocatedCards().size() > players[1].getAllocatedCards().size()){
            winner = players[0];
        }else if(players[0].getAllocatedCards().size() < players[1].getAllocatedCards().size()){
            winner = players[1];
        }else{
            winner = null;
        }
        System.out.println(winner != null ? "Winner is " + winner : "Nobody won");
    }

    private static Player[] setUpPlayers(){
        Player[] players = {new Player("player 1"), new Player("player 2")};
        return players;
    }

    private static void play(Player[] players, Stack<Card> pile, MatchingConditions matchingCondition){
        if(!matchingCondition.equals(MatchingConditions.INVALID)) {
            Stack<Card> unallocatedCards = new Stack<>();
            while(!pile.empty()) {
                //pick a card
                Card currentCard = pile.pop();
                Card prevCard = unallocatedCards.empty() ? null : unallocatedCards.peek();
                boolean cardsMatch = compareCards(currentCard, prevCard, matchingCondition);
                if (cardsMatch) {
                    Player randomPlayer = chooseRandomPlayer(players);
                    unallocatedCards.push(currentCard);
                    randomPlayer.getAllocatedCards().addAll(unallocatedCards);
                    unallocatedCards.clear();
                }else{
                    unallocatedCards.push(currentCard);
                }
            }
        }
    }

    private static Player chooseRandomPlayer(Player[] players){
        int randomIndex = new Random().nextInt(2);
        return players[randomIndex];
    }

    private static boolean compareCards(Card currentCard, Card prevCard, MatchingConditions matchingConditions){
        if(currentCard == null || prevCard == null) return false;

        if(matchingConditions.equals(MatchingConditions.FACE_VALUE)){ // Face value comparison
            return currentCard.getRank().equals(prevCard.getRank());
        }else if(matchingConditions.equals(MatchingConditions.SUIT)){ // Suit comparison
            return currentCard.getSuit().equals(prevCard.getSuit());
        }else if(matchingConditions.equals(MatchingConditions.BOTH)){ // Face value and suit comparison
            return currentCard.getSuit().equals(prevCard.getSuit())
                    && currentCard.getRank().equals(prevCard.getRank());
        }else{
            return false;
        }
    }

    private static Stack<Card> buildPile(Stack<Card> pack, int packsOfCards){
        Stack<Card> pile = new Stack<>();
        for(int i = 0; i < packsOfCards; i++){
            pile.addAll(pack);
        }
        return pile;
    }

    private static Stack<Card> buildPack(){
        Stack<Card> pack = new Stack<>();
        for(int i = 0; i < Ranks.values().length; i++){
            for(int j = 0; j < Suits.values().length; j++){
                Card card = new Card(Ranks.values()[i], Suits.values()[j]);
                pack.push(card);
            }
        }
        return pack;
    }

    private static int getPacksOfCards(Scanner sc){
        int input = -1;
        do{
            System.out.println("Enter number of card packs to use (anywhere between 1 and 100): ");
            while(!sc.hasNextInt()){
                System.out.println("Please enter valid number of card packs!");
                sc.next();
            }
            input = sc.nextInt();
        }while(input < 0 || input > 100);
        return input;
    }

    private static MatchingConditions getMatchingCondition(Scanner sc){
        System.out.println("Enter matching condition to use (F = Face value, S = Suit, B = Both): ");
        String matchingCondition = sc.next();
        if(matchingCondition.equalsIgnoreCase("F")){
            return MatchingConditions.FACE_VALUE;
        }else if (matchingCondition.equalsIgnoreCase("S")){
            return MatchingConditions.SUIT;
        }else if(matchingCondition.equalsIgnoreCase("B")){
            return MatchingConditions.BOTH;
        }else{
            return MatchingConditions.INVALID;
        }
    }
}
