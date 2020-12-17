import java.util.ArrayList;
import java.util.List;

/**
 *
 * A class that stores basic information of player, health, money
 *
 *
 * **/

public class Player {
    private static Player _instance = null;
    private List<Observer> observers = new ArrayList<Observer>();
    private int money;
    private int health;

    /**
     * Constructor
     * @param money stores money player has right now
     * @param health stores current lives player has right now
     */
    private Player(int money, int health){
        this.money = money;
        this.health = health;
    }

    /**
     *
     * singleton structure, if already have player, reset player status
     * @oaram _instance to check if we have duplicate player
     **/
    public static Player setPlayer(int money, int health){
        if (_instance==null){
            _instance = new Player(money, health);
        }
        else{
            _instance.reset(money, health);
        }
        return _instance;
    }


    //implement observer design pattern:
    public void attach(Observer observer){
        observers.add(observer);
    }

    public void notifyAllObservers(){
        for (Observer observer: observers){
            observer.updateInfo();
        }
    }


    /**add money to player's wallet
     *
     * @param money the money earned here
     */
    public void addMoney(int money){
        this.money += money;
        notifyAllObservers();
    }

    /**remove money to player's wallet
     *
     * @param money the money spent here
     * @return true if successfully deducted, flase if cannot afford
     */
    public boolean deductMoney(int money){
        if (this.money < money){
            return false;
        }
        this.money -= money;
        notifyAllObservers();
        //if we gonna spend all money, return false
        return true;
    }

    /**deduct health if been attacked.
     * allow health to be below 0 for checking if player is died
     *
     * @param health the health deducted here
     */
    public void deductHealth(int health){
        //in case health fall below 0
        if (this.health <= health){
            this.health = 0;
        }
        else {
            this.health -= health;
        }
        notifyAllObservers();
    }

    //getters:
    public int getHealth() {
        return health;
    }
    public int getMoney() {
        return money;
    }

    /**
     * Reset player information
     * @param money money player will have
     * @param health initial lives player will have
     */
    private void reset(int money, int health){
        this.money = money;
        this.health = health;
        notifyAllObservers();
    }

}
