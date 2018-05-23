import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    public static int numPlayers;
    public static Player[] city;

    public static void main(String[] args) {
        System.out.println("Welcome to the Interactive Choose-Your-Own-Adventure Game of Life.");
        System.out.println("The rules are simple. Do whatever you want.");
        System.out.println("Learn about yourself and your friends.");
        System.out.println("What is your true nature? What will you do to succeed?");
        System.out.println("Try your best to follow the prompts and remember this one simple rule:");
        System.out.println("You only take with you, that which you leave behind..."
                + "so spend money if you want to have fun.");
        String ans = "yes";
        while(ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
            Scanner user = new Scanner(System.in);
            System.out.println("How many players? (enter a number between 2 and 6 then hit enter)");
            numPlayers = user.nextInt();
            String[] names = askNames();
            makeCity(names);
            playGame();
            System.out.println("Would you like to play again?");
            ans = user.next().toLowerCase();
        }
    }

    public static String[] askNames() {
        Scanner user = new Scanner(System.in);
        String[] names = new String[numPlayers];
        for (int i = 1; i <= numPlayers; i++) {
            System.out.println("Name of player " + i + "? (type name, then hit enter)");
            names[i - 1] = user.nextLine();
        }
        return names;
    }

    public static void makeCity(String[] names) {
        city = new Player[numPlayers];
        int i = 0;
        for (String player : names) {
            city[i] = new Player(player);
            i++;
        }
    }

    public static void playGame() {
        int turn = 0;
        String gameState = "playing";
        while(gameState.equals("playing")) {
            System.out.println("It's " + city[turn].name + "'s turn!");
            System.out.println();
            gameState = turn(turn);
            turn = (turn + 1) % numPlayers;
        }
        if (gameState.equals("Elected")) {
           electedMessage();
        } else if (gameState.equals("Everyone Died")) {
           everyoneDiedMessage();
        } else if (gameState.equals("Ring Leader")) {
            ringLeaderMessage();
        } else if (gameState.equals("Movie Star")) {
            movieStarMessage();
        }
        System.out.println("Here was the final state of affairs:");
        for (Player p : city) {
            System.out.println(p.name + "'s Total worth: $" + (p.checkings + p.savings));
        }
        System.out.println("Thanks for playing!");
    }

    public static void electedMessage() {
        Player winner = city[0];
        for (Player p: city) {
            if(p.state.equals("Elected")) {
                winner = p;
            }
        }
        System.out.println("Congratulations to " + winner.name + " for being elected Mayor.");
        if (winner.link != null) {
            System.out.println("And congratulations to " + winner.link.name + "as well.");
        }
    }

    public static void movieStarMessage() {
        Player winner = city[0];
        for (Player p: city) {
            if(p.state.equals("Movie Star")) {
                winner = p;
            }
        }
        System.out.println("Congratulations to " + winner.name + " for becoming a Movie Star.");
        if (winner.link != null) {
            System.out.println("And congratulations to " + winner.link.name + "as well for being their friend and piggy-backing.");
        }
    }

    public static void ringLeaderMessage() {
        Player winner = city[0];
        for (Player p: city) {
            if(p.state.equals("Ring Leader")) {
                winner = p;
            }
        }
        System.out.println("Congratulations to " + winner.name
                + " for becoming the Ring Leader of the Mob.");
    }

    public static void everyoneDiedMessage() {
        System.out.println("I'm sorry. It appears everybody is dead.");
    }

    public static String turn(int turn) {
        Player player = city[turn];
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wakeup(player);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (player.state.equals("Robber")) {
            robber(player);
        } else if (player.state.equals("Worker")) {
            working(player);
        } else if (player.state.equals("Theater")) {
            theater(player);
        } else if (player.state.equals("Subway")) {
            subway(player);
        } else if (player.state.equals("Interview")) {
            interview(player);
        } else if (player.state.equals("Running")) {
            running(player);
        } else if (player.state.equals("Dead")) {
            dead(player);
        }
        player.updateFunds();
        Scanner user = new Scanner(System.in);
        String ans = "";
        if (!player.state.equals("Subway") && !player.state.equals("Interview") &&
                !player.state.equals("Dead") && !player.state.equals("Start")) {
            System.out.println();
            System.out.println("Whew! Long day. How about some fun? (A) Get some rest (B) Go to bar (C) Go to Casino");
            ans = user.next().toLowerCase();
            if (ans.startsWith("b")) {
                activity(player, "bar");
            } else if (ans.startsWith("c")) {
                activity(player, "casino");
            } else if (ans.startsWith("a")) {
                player.interestRate = player.interestRate + .01;
            }
        }
        return checkStates();
    }

    public static void activity(Player player, String place) {
        System.out.println();
        Scanner user = new Scanner(System.in);
        String ans = "";
        int talent = 0;
        System.out.println("Welcome to the " + place +".");
        String option_one = "Drink a beer";
        String option_two = "Play darts";
        if (place.equals("casino")) {
            option_two = "Play 21";
        }
        while(!ans.startsWith("c")) {
            System.out.println("Would you like to (A) " + option_one + " or (B) " + option_two + " or (C) go home");
            ans = user.next().toLowerCase();
            if (player.checkings < 4) {
                System.out.println("Looks like you're about broke, sorry.");
                System.out.println();
            } else {
                if (ans.startsWith("a")) {
                    System.out.println("checkings: " + player.checkings);
                    System.out.println("Beer is $4 a glass. Would you like a glass?");
                    user.reset();
                    ans = user.next().toLowerCase();
                    if (ans.startsWith("y") || ans.startsWith("s") || ans.startsWith("o")) {
                        System.out.println("Here ya go!");
                        System.out.println("*GLUG GLUG GLUG*");
                        System.out.println();
                        player.checkings = player.checkings - 4;
                        talent++;
                    }
                } else if (ans.startsWith("b")){
                    if (place.equals("casino")) {
                            System.out.println("In this variation there is an even chance of any "
                                    + "number between 1 and 11 on each hit");
                        System.out.println("There will be a single bet off your opening hand.");
                        System.out.println();
                        double bet = 0;
                        int hand = (int) Math.round(Math.random()*19) + 2;
                        int showing = (int) (Math.round(Math.random()*hand));
                        if (hand > 11) {
                            int dif = hand - 11;
                            showing = (int) Math.round((Math.random()*(12 - dif)) + (dif));
                        }
                        int dealerHand = (int) Math.round(Math.random()*19) + 2;
                        int dealerShowing = (int) (Math.round((Math.random())*dealerHand));
                        if (dealerHand > 11) {
                            int dif = dealerHand - 11;
                            dealerShowing = (int) Math.round((Math.random()*(12 - dif) + (dif)));
                        }
                        if (showing == 0) {
                            showing++;
                        }
                        if (dealerShowing == 0) {
                            showing++;
                        }
                        boolean stay = false;
                        boolean bust = false;
                        while (!stay && !bust) {
                            System.out.println("You have " + hand + " in your hand and " + showing +" showing.");
                            System.out.println("The dealer has " + dealerShowing + " showing");
                            if (bet == 0) {
                                System.out.println("Opening bet? Enter a number between 4 and " + player.checkings);
                                bet = user.nextDouble();
                            }
                            System.out.println("hit or stay?");
                            ans = user.next().toLowerCase();
                            if (ans.startsWith("h")) {
                                stay = false;
                                int hit = (int) (Math.round(Math.random()*10)) + 1;
                                if (talent > 2) {
                                    hit = (int) (Math.round(Math.random()*5)) + 1;
                                }
                                hand = hand + hit;
                                showing = showing + hit;
                                if (hand > 21) {
                                    bust = true;
                                }
                            } else {
                                stay = true;
                                System.out.println("Final bet? Enter a number between 0 and " + player.checkings);
                                bet = bet + user.nextDouble();
                                System.out.println();
                            }
                        }
                        stay = false;
                        System.out.println("The dealer flipped his card.");
                        System.out.println();
                        while(!stay && !bust) {
                            System.out.println("the dealer has " + dealerHand + " showing.");
                            System.out.println();
                            if ((dealerHand < 17 || dealerHand < showing) && !bust) {
                                stay = false;
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                int hit = (int) (Math.round(Math.random()*10)) + 1;
                                dealerHand = dealerHand + hit;
                                if (dealerHand > 21) {
                                    bust = true;
                                }
                            } else {
                                stay = true;
                            }
                        }
                        System.out.println("Final showing. You: " + hand + ", and dealer: " + dealerHand);
                        if (hand == dealerHand) {
                            System.out.println("Ties go to the dealer.");
                        }
                        if (bust) {
                            if (hand > 21) {
                                System.out.println("Sorry you busted. ");
                                System.out.println("Waitress: Maybe a beer would calm your nerves? eh?");
                                player.checkings = player.checkings - bet;
                            } else {
                                System.out.println("The dealer busted. You won! $" + bet);
                                player.checkings = player.checkings + bet;
                            }
                        } else {
                            if (hand > dealerHand) {
                                System.out.println("You won! $" + bet);
                                player.checkings = player.checkings + bet;
                            } else {
                                System.out.println("Sorry you lost.");
                                System.out.println("Waitress: Maybe a beer would calm your nerves? eh?");
                                player.checkings = player.checkings - bet;
                            }
                        }
                    } else {
                        List<Player> options = new LinkedList<Player>();
                        List<String> names = new LinkedList<String>();
                        String other = "the house";
                        Player partner = null;
                        for(Player p: city) {
                            if ((player.state.equals(p.state) || p.state.equals("Running")) && p.income > 4
                                    && !p.name.equals(player.name)) {
                                options.add(p);
                                names.add(p.name.toLowerCase());
                            }
                        }
                        if (!options.isEmpty()) {
                            System.out.println("Would you like to play against a (F)riend or the (H)ouse?");
                            ans = user.next().toLowerCase();
                            if (ans.startsWith("f")) {
                                while (!names.contains(ans)) {
                                    user.reset();
                                    System.out.println("Choose a friend from below by typing their name and hitting enter:");
                                    for(Player p: options) {
                                        if (p.state.equals("Running")) {
                                            System.out.print("Candidate: ");
                                        }
                                        System.out.println(p.name);
                                    }
                                    ans = user.next().toLowerCase();
                                }
                                for (Player p: options) {
                                    if (p.name.toLowerCase().equals(ans)) {
                                        partner = p;
                                        other = partner.name;
                                        break;
                                    }
                                }
                                System.out.println("Do you want to play? (Let " + other + " answer)");
                                ans = user.next().toLowerCase();
                                if (ans.startsWith("y") || ans.startsWith("s") || ans.startsWith("o")) {
                                    System.out.println("Great!");
                                    System.out.println();
                                } else {
                                    other = "the house";
                                    System.out.println("Looks like you're playing the house this time.");
                                }
                            }
                        }
                        System.out.println("Here are the rules:");
                        System.out.println("We start with 150 points and throw darts until we get to zero.");
                        System.out.println("If " + other + " gets to zero first, you pay " + other
                                + " as much money as points you have left.");
                        System.out.println("If YOU get to zero first, " + other + " pays you "
                                + "as much money as points " + other + " has left.");
                        System.out.println("Capiche?");
                        System.out.println();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        Integer[] scores = new Integer[2];
                        scores[0] = 150; scores[1] = 150;
                        int[] hits = new int[4];
                        while(!(scores[0] == 0) && !(scores[1] == 0)) {
                            for (int i = 0; i < 4; i++) {
                                if(i % 2 == 0) {
                                    System.out.println("Current scores: You = " + scores[0] + ", " + other +" = " + scores[1]);
                                    System.out.println();
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                String person;
                                if (i < 2) {person = player.name;} else {person = other;} // check who's turn it is
                                hits[i] = 0;
                                System.out.println(person +"'s turn! Throw number " + (i%2 + 1) +":");
                                if(i < 2 || !other.equals("the house")) {
                                    System.out.println("Would you like to do a (S)afe throw or go for "
                                            + "a (B)ullseye?");
                                    ans = user.next().toLowerCase();
                                } else {
                                    if ((scores[1] % 3) == 0 && scores[1] != 150) {
                                        ans = "b";
                                    } else {
                                        ans = "s";
                                    }
                                }
                                if (ans.startsWith("b")) {
                                    double success = Math.random();
                                    if (i < 2) {
                                        success = success + (.03 * talent);
                                    }
                                    if (success > .65) {
                                        hits[i] = 50;
                                        if (i < 2 || !other.equals("the house")) {
                                            System.out.println("Wow! Bullseye! Good job, that's 50 points.");
                                        }
                                    } else {
                                        if (i < 2 || !other.equals("the house")) {
                                            System.out.println("Wow! You almost took the bartender's eye out!");
                                            System.out.println("Bartender: Maybe you'd be better if "
                                                    + "you had a couple beers? eh?");
                                        }
                                    }
                                } else {
                                    hits[i] = (int) Math.round(Math.random()*19) + 1;
                                }
                                if ((i % 2) == 1 && hits[i] == hits[i - 1] && hits[i] != 0) {
                                    if (i < 2 || !other.equals("the house")) {
                                        System.out.println("You hit double points! You have good aim.");
                                    }
                                    hits[i] = 2 * hits[i];
                                }
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                System.out.println(person + " hit " + hits[i] + " on that throw.");
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                scores[i/2] = scores[i/2] - hits[i];
                                if (scores[i/2] <= 0) {
                                    scores[i/2] = 0;
                                    break;
                                }
                            }
                        }
                        System.out.println("Final scores: You: " + scores[0] + ", " + other + ": " + scores[1]);
                        if (scores[1] == 0) {
                            double delta = Math.min(scores[0],player.checkings);
                            System.out.println("Sorry you lost $" + delta + " to " + other);
                            player.checkings = player.checkings - delta;
                            if (partner != null) {
                                partner.checkings = partner.checkings + delta;
                            }
                        } else {
                            double delta = scores[1];
                            if (partner != null) {delta = Math.min(delta,partner.checkings);}
                            System.out.println("Congrats! You won $" + delta + " from " + other);
                            player.checkings = player.checkings + delta;
                            if (partner != null) {partner.checkings = partner.checkings - delta;}
                        }
                    }
                }
                System.out.println();
            }
        }
        System.out.println("See ya later!");
        System.out.println();
        System.out.println("It's late! Time for bed...");
        String alarm = user.next().toLowerCase();
        if(!alarm.startsWith("s") && (player.state.equals("Worker") || player.state.equals("Theater"))) {
            player.state = "Overslept";
        }
    }

    public static String checkStates() {
        int deathCount = 0;
        for (Player player: city) {
            if (player.state.equals("Dead")) {
                deathCount ++;
            }
            if (player.state.equals("Elected")) {
                return "Elected";
            }
            if (player.state.equals("Ring Leader")) {
                return "Ring Leader";
            }
            if (player.state.equals("Movie Star")) {
                return "Movie Star";
            }

        }
        if (deathCount == numPlayers) {
            return "all dead";
        }
        return "playing";
    }

    public static void wakeup(Player player) {
        Scanner user = new Scanner(System.in);
        if (player.state.equals("Start")) {
            Random randy = new Random();
            int time = randy.nextInt(numPlayers) + 1;
            if (time <= numPlayers/2) {
                System.out.println("You woke up on time for your interview with Big Evil Inc!");
                System.out.println("Get dressed and get out the door!");
                System.out.println("By the way, it is VERY important that you (S)et your alarm before bed");
                player.state = "Interview";
            } else {
                System.out.println("Oh no! You woke up late and missed your interview!");
                System.out.println("From now on you better (S)ET YOUR ALARM");
                System.out.println("You better catch the last morning train to see if they'll "
                        + "be willing to reschedule (I doubt it)");
                player.state = "Subway";
            }

        } else {
            if (player.state.equals("Overslept")) {
                System.out.println("Oh no! You woke up late and missed your morning meetings!");
                System.out.println("From now on you better (S)ET YOUR ALARM");
                System.out.println("You better catch the last morning train to see if they'll "
                        + "be willing to reschedule (I doubt it)");
                System.out.println();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                   e.printStackTrace();
                }
                if (player.link != null) {
                    System.out.println("*RING RING* It's " + player.link.name);
                    System.out.println(player.link.name + ": Hey, " + player.name + ", I covered "
                            + "for you this morning, no worries.");
                    System.out.println("Whew! That was a close one");
                    player.state = "Worker";
                } else {
                    System.out.println("*RING RING* It's your boss.");
                    System.out.println(player.name + "! You've done it now! You're fffired!!!");
                    player.income = 0;
                    player.state = "Subway";
                }
            } else if (!player.state.equals("Dead")){
                System.out.println("*ALARM SOUND*");
                System.out.println();
                if (player.checkings < 0) {
                    System.out.println("Oh No! You were robbed. Hopefully you have some savings to fall back on.");
                    System.out.println();
                    player.checkings = 0;
                }
                if (player.checkings > 0 || player.savings > 0) {
                    System.out.println("Let's take a look at your bank accounts and start the "
                        + "day with some savvy financial decisions!");
                    System.out.println("Press enter to view options.");
                    String choice = user.nextLine().toUpperCase();
                    while (!choice.startsWith("f")) {
                        player.checkings = Math.round(100*player.checkings)/100;
                        player.savings = Math.round(100*player.savings)/100;
                        player.income = Math.round(100*player.income)/100;
                        System.out.println("Checkings: $" + player.checkings);
                        System.out.println("Savings: $" + player.savings);
                        System.out.println();
                        System.out.println("Income: $" + player.income);
                        System.out.println("Interest: " + Math.round((player.interestRate - 1)*100) +"%");
                        System.out.println();
                        System.out.println("Would you like to:");
                        System.out.println("A: transfer $ from checkings to savings?");
                        System.out.println("B: donate to charity?");
                        System.out.println("C: invest in your own well-being?");
                        System.out.println("D: transfer $ from savings to checkings?");
                        if (player.link != null) {
                            System.out.println("E: invest in your partnership with " + player.link.name + "?");
                        }
                        System.out.println("Are you (F)inished?");
                        user.reset();
                        choice = user.next().toLowerCase();
                        if (choice.equals("a")){
                            System.out.println("How much $ (type number from 0 to " + player.checkings + ")?");
                            if (user.hasNextDouble()) {
                                double delta = user.nextDouble();
                                player.checkings = player.checkings - delta;
                                player.savings = player.savings + delta;
                            } else {
                                System.out.println("Invalid input. Try again.");
                            }
                        } else if (choice.equals("b")){
                            System.out.println("How much $ (type number from 0 to " + player.checkings + ")?");
                            if (user.hasNextDouble()) {
                                double delta = user.nextDouble();
                                player.checkings = player.checkings - delta;
                                player.charity = player.charity + delta;
                            } else {
                                System.out.println("Invalid input. Try again.");
                            }
                        } else if (choice.equals("c")){
                            System.out.println("How much $ (type number from 0 to " + player.checkings + ")?");
                            if (user.hasNextDouble()) {
                                double delta = user.nextDouble();
                                player.checkings = player.checkings - delta;
                                player.selfInvest = player.selfInvest + delta;
                            } else {
                                System.out.println("Invalid input. Try again.");
                            }
                        } else if (choice.equals("d")){
                            System.out.println("How much $ (type number from 0 to " + player.savings + ")?");
                            if (user.hasNextDouble()) {
                                double delta = user.nextDouble();
                                player.checkings = player.checkings + delta;
                                player.savings = player.savings - delta;
                            } else {
                                System.out.println("Invalid input. Try again.");
                            }
                        } else if (choice.equals("e") && !(player.link == null)){
                            System.out.println("How much $ (type number from 0 to " + player.checkings + ")?");
                            if (user.hasNextDouble()) {
                                double delta = user.nextDouble();
                                player.checkings = player.checkings - delta;
                                player.linkMoney = player.linkMoney + delta;
                                player.link.linkMoney = player.link.linkMoney + delta;
                            } else {
                                System.out.println("Invalid input. Try again.");
                            }
                        }
                        System.out.println();
                    }
                }
            }
        }
        System.out.println();
    }

    public static void interview(Player player) {
        Scanner user = new Scanner(System.in);
        int score = 0;
        System.out.println("Welcome to your interview with Big Evil Incorporated!");
        System.out.println("It says here your name is..." + player.name +"? Hmmm.. weird name");
        System.out.println("Well, anyway, let's get on to the questions, shall we?");
        System.out.println("Tell me, have you completed your undergraduate degree?");
        String ans = user.nextLine().toLowerCase();
        if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
            score ++;
            System.out.println("Oh, excellent!");
        } else {
            System.out.println("Oh... I see...");
        }
        System.out.println("Now, do you prefer cats or dogs?");
        ans = user.nextLine().toLowerCase();
        if (ans.startsWith("d")) {
            score ++;
        }
        System.out.println("Very good then.");
        System.out.println("So, do you have any work experience in the field of "
                + "being big and evil?");
        ans = user.nextLine().toLowerCase();
        if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
            score ++;
            System.out.println("Oh, excellent!");
        } else {
            System.out.println("Oh... I see...");
        }
        System.out.println("How about Excell? Can you use Excell proficiently? Lots of "
                + "big evil data to manage these days, you know.");
        ans = user.nextLine().toLowerCase();
        if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
            score ++;
            System.out.println("Oh, excellent!");
        } else {
            System.out.println("Oh... I see...");
        }
        System.out.println("Last question " + player.name + ", and I thank you "
                + "for your time in advance");
        System.out.println("Are you prepared to sell your soul to corporate America?");
        ans = user.nextLine().toLowerCase();
        if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
            score ++;
            System.out.println("Mhm Greeat.");
        } else {
            System.out.println("I'm sorry to hear that " + player.name);
        }
        System.out.println("After this interview I am confident in saying that you are ");
        if (score >= 3) {
            System.out.println("perfect for the job!");
            System.out.println("pay will begin at $20/turn! Does that work?");
            ans = user.nextLine().toLowerCase();
            if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                player.income = 20.00;
                System.out.println("Great! See you tomorrow for your first day on the job!");
                player.state = "Worker";
            } else {
                System.out.println("Hmmm... well since you are so qualified, how's $25/turn?");
                ans = user.nextLine().toLowerCase();
                if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                    player.income = 25.00;
                    System.out.println("Great! See you tomorrow for your first day on the job!");
                    player.state = "Worker";
                } else {
                    System.out.println(player.name + "! What do you take me for?"
                            + " Get out of my office!");
                    player.state = "Subway";
                }
            }
        } else {
            System.out.println("one of the least qualified candidates I've ever interviewed.");
            System.out.println("Maybe you should try acting. You managed to fool my recruiters.");
            System.out.println("Goodbye " + player.name + ". Shut the door on your way out.");
            player.state = "Subway";
        }
        System.out.println();
    }

    public static void subway(Player player) {
        Scanner user = new Scanner(System.in);
        System.out.println("*TRAIN WHISTLE*");
        System.out.println("Whew! You made it just in time. That was a close one.");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("There is a strange man eyeing you from across the platform!");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("This is your stop! ...but it looks like he's following you...");
        System.out.println("Strange Man: Hey you!");
        System.out.println("Strange Man: Got a wallet? Hurry up, don't try nuttin funny");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("    Would you like to");
        System.out.println("A: Tell him to scram.");
        System.out.println("B: Beg for mercy because you are unemployed "
                + "and trying to make ends meet just like him.");
        String ans = user.nextLine().toLowerCase();
        if (ans.startsWith("a")) {
            System.out.println("He's startled by your confidence.");
            System.out.println("He spits at you and runs off.");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("A nearby woman saw the whole thing and comes over to shake your hand.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Woman: That was amazing! And you had such enthusiasm! I work at a theater company downtown.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("She hands you her card.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("We don't have any current openings on stage but if you would like to come work as a stagehand");
            System.out.println("you could.. climb the ladder as they say!");
            System.out.println("Woman: what do you say? (type yes/no)");
            ans = user.nextLine().toLowerCase();
            if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                System.out.println("Woman: oh fantastic! I'll see you tomorrow, then!");
                player.state = "Theater";
            } else {
                System.out.println("Woman: well that's unfortunate for you. It looks like you're going nowhere in life.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("The strange man returns.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Strange Man: Don't take that from her!");
                System.out.println("Strange Man: You can come work for me, and I won't take no for an answer (type yes/no)");
                ans = user.nextLine().toLowerCase();
                if (ans.startsWith("n")) {
                    System.out.println("Strange Man: I warned you.");
                    System.out.println("*STAB*");
                    System.out.println("Everything's going dark...");
                    player.state = "Dead";
                } else {
                    System.out.println("Great! You're one of us now.");
                    System.out.println("Strange Man: Meet me by the old warehouse"
                            + "on the outside of town tomorrow for your first job");
                    System.out.println("Strange Man: Don't be late!!");
                    player.state = "Robber";
                }
            }
        } else {
            System.out.println("Strange Man: Geez, if you put it that way..");
            System.out.println("Strange Man: Well, I don't normally do this,"
                    + "but how would you like to join our ring of thieves? ("
                    + "type \"join\" if you want to join)");
            ans = user.nextLine().toLowerCase();
            if (!ans.startsWith("j")) {
                System.out.println("Strange Man: Are you sure?? I'll probably have "
                        + "to kill you now that you know about it. "
                        + "(type \"join\" if you want to join)");
                ans = user.nextLine().toLowerCase();
                if (!ans.startsWith("j")) {
                    System.out.println("Strange Man: I warned you.");
                    System.out.println("*STAB*");
                    System.out.println("Everything's going dark...");
                    player.state = "Dead";
                }
            }
            if (ans.startsWith("j")) {
                System.out.println("Great! You're one of us now.");
                System.out.println("Strange Man: Meet me by the old warehouse "
                        + "on the outside of town tomorrow for your first job");
                System.out.println("Strange Man: Don't be late!!");
                player.state = "Robber";
            }
        }
        System.out.println();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void robber(Player player) {
        Scanner user = new Scanner(System.in);
        String ans = "";
        int reward = 0;
        if((player.savings + player.checkings + player.selfInvest) > 800) {
            System.out.println("Congratulations! You are the toughest robber in town.");
            System.out.println("You have become the RING LEADER!!!");
            System.out.println("You are the most powereful person in City.");
            player.state = "Ring Leader";
        } else {
            if (player.charity > 100) {
                player.charity = 0;
                System.out.println("Because of your kindness, the townsfolk think of you as a"
                        + " Robinhood figure.");
                System.out.println("They've forgiven you for your evil ways and want you to be Mayor!");
                System.out.println("Would you like to run for Mayor?");
                ans = user.nextLine().toLowerCase();
                if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                    System.out.println("Great! Campaigning starts tomorrow!");
                    player.state = "Running";
                    player.selfInvest = 0;
                    player.charity = 0;
                    player.income = 50;
                } else {
                    System.out.println("That's too bad. What a story that would have made...");
                }
            }
            System.out.println("Welcome to the warehouse");
            System.out.println("We do everything from pickpocketing to diamond heists.");
            List<Player> victims = new LinkedList<Player>();
            for (Player p : city) {
                if (p.promoted && p.checkings > 0) {
                    victims.add(p);
                }
            }
            if (!victims.isEmpty()) {
                for(Player victim: victims) {
                    if (player.state.equals("Robber")) {
                        System.out.println("That hotshot, " + victim.name + ",  is becoming real big around town.");
                        System.out.println("Want to knock that big shot over, see?");
                        ans = user.nextLine().toLowerCase();
                        if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                            System.out.println("Alright, then. Hop in the car...");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("You: Hey! " + victim.name + "! You got about ten seconds"
                                    + " to convince me not to take everything ya got, you filthy animal.");
                            System.out.println(victim.name + ", would you like to (A) negotiate (B) fight (C) run");
                            ans = user.nextLine().toLowerCase();
                            if (ans.startsWith("a")) {
                                System.out.println(victim.name + ": Alright, calm down, " + player.name + ". You don't"
                                        + " have to live this life. I can get you an interview at my work and we can be partners.");
                                System.out.println(victim.name + ": Whatta ya say? (let " + player.name + " answer yes/no)");
                                ans = user.nextLine().toLowerCase();
                                if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                                    System.out.println(victim.name + "Great! I'll see you tomorrow.");
                                    player.state = "Interview";
                                    player.interestRate = player.interestRate + .05;
                                    victim.interestRate = victim.interestRate + .05;
                                    player.link = victim;
                                    victim.link = player;
                                } else {
                                    System.out.println(player.name + ": Nah, I'm just gonna rob you.");
                                    player.checkings = player.checkings + victim.checkings;
                                    victim.checkings = -1;
                                }
                            } else if (ans.startsWith("b")) {
                                double result = Math.random();
                                if (result > .75) {
                                    System.out.println(victim.name + " overtook " + player.name +". " + player.name + "is dead.");
                                    player.state = "Dead";
                                } else {
                                    System.out.println(player.name + " overtook " + victim.name + " and made off with the cash.");
                                    victim.state = "Dead";
                                    player.checkings = player.checkings + victim.checkings;
                                    victim.checkings = -1;
                                }
                            } else if (ans.startsWith("c")) {
                                double result = Math.random();
                                if (result > .5) {
                                    System.out.println(player.name + " caught up to " + victim.name + " and made off with the cash.");
                                    player.checkings = player.checkings + victim.checkings;
                                    victim.checkings = -1;
                                } else {
                                    System.out.println(victim.name + " got away!");
                                }
                            }
                        }
                    }
                }
            }
            System.out.println();
            System.out.println("Would you like to attempt a risky job or a safe job?");
            ans = user.nextLine().toLowerCase();
            if (ans.startsWith("r")) {
                System.out.println("Somebody thinks they're tough!");
                System.out.println("Alright, let's see...");
                double job = Math.random();
                if (job < .2) {
                    reward = rob(player, "bank");
                } else if (job < .8) {
                    reward = rob(player, "jewelry store");
                } else {
                    reward = rob(player, "museum");
                }
            } else {
                System.out.println("Maybe next time we can try something risky...");
                System.out.println("Alright, let's see...");
                System.out.println();
                System.out.println("We're planning a couple easy mugging jobs by the train.");
                System.out.println("We'll set you up there, good luck.");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Look! He's not paying any attention!");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Guy: Hey! Who are you? I'll fight you!");
                System.out.println();
                System.out.println("Would you like to (A) Fight. Or (B) Run.");
                ans = user.nextLine().toLowerCase();
                if (ans.startsWith("a")) {
                    double result = Math.random();
                    if (result < .9) {
                        System.out.println("You overtook him");
                        reward = (int) ((result * 35) + 3);
                        System.out.println("He had $" + reward + " on him");
                    } else {
                        System.out.println("Uh oh. He has a knife!");
                        System.out.println("*STAB*");
                        System.out.println("He killed you.");
                        player.state = "Dead";
                    }

                } else {
                    System.out.println("You got away! But it looks like you aren't making any money today.");
                }
            }
        }
        player.income = reward;
        System.out.println();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static int rob(Player player, String place) {
        System.out.println("We're hitting the " + place + " downtown. This is a big job. Get in the get-away car.");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("*SIRENS* *ALARMS*");
        System.out.println("Hurry up and get everything out of the registers!");
        if (place.equals("jewelry store")) {
            System.out.println("Grab the jewels!!!");
        }
        if (place.equals("museum")) {
            System.out.println("Grab the paintings!!!");
        }
        System.out.println();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        double success = Math.random();
        if (place.equals("bank") || place.equals("museum")) {
            success += .15;
        }
        if (success < .70) {
            System.out.println("Wow! We made it! We got so much money!");
            System.out.println("Count it " + player.name + "!");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int reward = ((int) (Math.random() * 100 + 50));
            if (place.equals("bank") || place.equals("museum")) {
                reward += 100;
            }
            System.out.println("There's $" + reward + ".00 each!!!");
            return reward;
        }
        System.out.println("OH NO!!! It's turning into a shoot out!!!");
        System.out.println("*BANG* *BANG* BANG*");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("You were killed in the cross fire");
        player.state = "Dead";
        return 0;
    }

    public static void working(Player player) {
        Scanner user = new Scanner(System.in);
        String ans = "";
        if (player.savings > 200) {
            System.out.println("It looks like the Bank defaulted.");
            System.out.println("Some of your savings were lost. We're sorry");
            player.savings = 100;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println();
            System.out.println("Boss: Heard about your savings, " + player.name + ", now you'll probably work twice as hard!");
            System.out.println("HA HA HA HA HA");
            System.out.println("Back to work!");
        }
        if (player.selfInvest > (player.income - 5) && player.selfInvest < player.income + 10) {
            System.out.println("You've set enough money aside for some entertainment!");
            System.out.println("There's a big broadway play happening downtown, tonight.");
            System.out.println("Would you like to see it?");
            ans = user.nextLine().toLowerCase();
            if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                player.selfInvest = player.selfInvest - player.income + 5;
                play(player,user);
            } else {
                System.out.println("That's too bad.");
                player.selfInvest = player.selfInvest + 15;
            }
        } else if (player.charity > player.income - 10 && player.charity < player.income + 15) {
            System.out.println("You have been so generous to local charities that one has invited you to speak at a benefit.");
            System.out.println("Do you accept?");
            ans = user.nextLine().toLowerCase();
            if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                System.out.println("You spoke at the event and everyone in town likes you now.");
                player.charity = player.charity + 25;
            } else {
                System.out.println("That's too bad.");
            }
        }
        if (player.selfInvest > 40 && !player.promoted) {
            System.out.println("Boss: " + player.name + ", you have been doing a fine job here"
                    + " at B.E.I. and I think it's time we rewarded you with a promotion!");
            System.out.println("You will be joining an elite analytics team of our most evil "
                    + "employees and your pay will increase to $35/turn!");
            System.out.println("Congratulations! This position impacts the entire city! Lots of politicians"
                    + " will be talking to you!");
            player.promoted = true;
            player.income = 35;
            player.selfInvest = player.selfInvest - 40;
            Player link = null;
            for (Player p : city) {
                if (p.promoted && !p.name.equals(player.name) && p.state.equals("Working")) {
                    link = p;
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (link != null) {
                System.out.println("You: Hey, " + link.name + ", I didn't know you were on this team!");
                System.out.println("Would you like to ask " + link.name + " to work together in a partnership?");
                if (player.link != null) {
                    System.out.println("You can benefit from " + player.link.name + " and " + link.name + "!");
                }
                ans = user.nextLine().toLowerCase();
                if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                    System.out.println("You: " + link.name + ", would you like to work together? "
                            + "(let " + link.name + " answer)");
                    ans = user.nextLine().toLowerCase();
                    if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                        player.interestRate = player.interestRate + .05;
                        link.interestRate = link.interestRate + .05;
                        player.link = link;
                        link.link = player;
                        System.out.println("You: Excellent! Looking forward to working with you.");
                    }
                } else {
                    System.out.println("That's too bad.");
                }
            }
        } else if (player.charity > 55 && player.promoted) {
            System.out.println("You are so popular around town with all of your gracious"
                    + " donations. You have been given the key to the city.");
            System.out.println("Would you like to run for Mayor?");
            ans = user.nextLine().toLowerCase();
            if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                player.state = "Running";
                player.selfInvest = 0;
                player.charity = 0;
                player.income = 50;
            } else {
                System.out.println("That's unfortunate.");
            }
        }
        if (player.promoted && player.selfInvest > (player.income*2 - 5)) {
            System.out.println("Boss: I suppose it's time we gave you a raise!");
            player.selfInvest = player.selfInvest - (player.income*2) + 5;
            player.income = player.income + 15;
            System.out.println("Boss: Your new pay will be " + player.income + "/turn!");
        }
        if (player.promoted && (Math.random() < .1)) {
            System.out.println("Boss: Time for your Christmas bonus!");
            player.checkings = player.checkings + 100;
        }
        System.out.println("Just another day on the job");
        System.out.println();
    }

    public static void play(Player player, Scanner user) {
        String ans = "";
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("Usher: Welcome take your seat.");
        System.out.println("You watch the play...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("It's okay.");
        System.out.println();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Player actor = null;
        for (Player p: city) {
            if (p.state.equals("Theater") && p.link == null && !p.name.equals(player.name)) {
               actor = p;
            }
        }
        if (!(actor == null)) {
            System.out.println("You: Hey, " + actor.name + "! I didn't know you were an actor!");
            System.out.println("You: Remember me? It's " + player.name + " from high school!");
            System.out.println(actor.name + ": ..oh yea! What's up, " + player.name + "?");
            System.out.println();
            System.out.println("Would you like to offer a job at Big Evil Inc to " + actor.name + "?");
            if (player.link != null) {
                System.out.println("You can benefit from " + player.link.name + " and " + actor.name + "!");
            }
            System.out.println("Having a partner may be a worthwhile investment..");
            ans = user.nextLine().toLowerCase();
            if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                System.out.println("You: Say, " + actor.name + "! Would you like to come work with me?"
                        + " Better pay? We could form a partnership! (let " + actor.name + " answer.)");
                ans = user.nextLine().toLowerCase();
                if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                    System.out.println("You: Great! See you tomorrow!");
                    actor.state = "Interview";
                    player.interestRate = player.interestRate + .05;
                    actor.interestRate = actor.interestRate + .05;
                    player.link = actor;
                    actor.link = player;
                }
            } else if (!ans.startsWith("y") && !ans.startsWith("o") && !ans.startsWith("s")) {
                System.out.println("You: Nothing much. Whelp. See ya around!");
            }

        }
        double chance = Math.random();
        if (chance < .4) {
            System.out.println("As you exit the theater, a woman approaches you.");
            System.out.println("Woman: Did you enjoy the play?");
            System.out.println("You look perfect for a part in a movie I'm shooting.");
            System.out.println("I can't guarentee the casting directors will agree but would you like to try out?");
            ans = user.nextLine().toLowerCase();
            if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                System.out.println("Great, Head straight to casting!");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Casting Director: Great to meet you, " + player.name + ". Show us what you've got!");
                System.out.println("(A) Tragic Monologue (B) Comedic Monologue");
                ans = user.nextLine().toLowerCase();
                double result = Math.random();
                if (result < .55) {
                    System.out.println("That was amazing! You got the part!");
                    player.income = player.income + 200;
                    System.out.println("You can continue working your day job and "
                            + "we will pay you for rehearsals as well!");
                } else {
                    System.out.println("I'm sorry " + player.name + ". Maybe next time...");
                }
            }
        }
        System.out.println();
    }

    public static void theater(Player player) {
        Scanner user = new Scanner(System.in);
        String ans = "";
        if (player.savings + player.checkings + player.selfInvest + player.charity > 800) {
            System.out.println("Congratulations! You have done so well working for the theater.");
            System.out.println("You recieved a call from Hollywood and they want to make you a MOVIE STAR!!!");
            System.out.println("You are now the coolest and most famous person in City");
            player.state = "Movie Star";
        } else {
            if (player.selfInvest > 30 && !player.promoted) {
                System.out.println("Lady: You've been doing great work around the theater, " + player.name +"! "
                        + "I would like to give you a raise and offer you a part in a MOVIE!");
                System.out.println("It's a one time gig but it pays a TON and can open doors for you!");
                System.out.println("What do you say?");
                ans = user.nextLine().toLowerCase();
                if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                    System.out.println("Great! Head straight over to casting!");
                    player.promoted = true;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Casting Director: Great to meet you, " + player.name + ". Show us what you've got!");
                    System.out.println("(A) Tragic Monologue (B) Comedic Monologue");
                    ans = user.nextLine().toLowerCase();
                    double result = Math.random();
                    if (result < .5) {
                        System.out.println("That was amazing! You got the part!");
                        player.income = 200;
                    } else {
                        System.out.println("I'm sorry " + player.name + ". Maybe next time...");
                    }
                } else {
                    System.out.println("That's too bad.");
                }
            }
            if (player.charity > 45 && player.promoted) {
                System.out.println("You are becoming pretty popular around town");
                System.out.println("Would you like to run for mayor?");
                ans = user.nextLine().toLowerCase();
                if (ans.startsWith("y") || ans.startsWith("o") || ans.startsWith("s")) {
                    player.state = "Running";
                    player.charity = 0;
                    player.selfInvest = 0;
                    player.income = 50;
                } else {
                    System.out.println("That's too bad.");
                }
            }
            System.out.println("Welcome to the theater. You can work backstage or try out "
                    + "for a part in the play");
            System.out.println();
            System.out.println("Acting pays better but you have to try out for each play.");
            System.out.println("Stagehands don't make as much but it's a steady paycheck.");
            System.out.println("You can always try out again next time.");
            System.out.println();
            System.out.println("Which would you like to do? (A) stagehand (B) try out");
            ans = user.nextLine().toLowerCase();
            if (ans.startsWith("a")) {
                System.out.println("Ok. Grab a broom.");
                player.income = 8;
                if (player.promoted) {
                    player.income = 16;
                    if (Math.random() < .1) {
                        System.out.println("Lady: Time for your Christmas bonus!");
                        player.checkings = player.checkings + 100;
                    }
                }
            } else {
                double part = Math.random();
                if (player.promoted) {
                    part = part - .1;
                }
                System.out.println("Great, hop up on stage, let's hear a monologue! (type a monologue, hit enter)");
                ans = user.nextLine();
                if (part < .55) {
                    System.out.println("That was excellent! You got the part!");
                    player.income = Math.round(80*Math.random()) + 10;
                } else {
                    System.out.println("Sorry you're just not what we're looking for. No spark.");
                    player.income = 0;
                }
            }
        }
        System.out.println();
    }

    public static void running(Player player) {
        String ans = "";
        Scanner user = new Scanner(System.in);
        double polls = 50;
        if (player.income < 200) {
            polls = player.income / 2;
        }
        System.out.println("Your current popularity = " + polls + "%");
        double shot = Math.random();
        if (shot < .08 && polls > 40) {
            System.out.println("*Breaking* OH NO! It appears a crazed gunman has opened fire at " + player.name + "'s rally.");
            System.out.println("I'm afraid candidate, " + player.name + "was killed.");
            player.state = "Dead";
        } else {
            if (player.campaignFunds > 1000 && polls > 50) {
                System.out.println("*This just in*");
                System.out.println("The election results are in and it looks like...");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(player.name + "Is the new Mayor of City with " + polls + "% of the vote!");
                System.out.println("Congratulations! Do you have anything to say to the people of City?");
                ans = user.nextLine();
                System.out.println("What a great speech!");
                if (player.link != null) {
                    System.out.println("And it looks like " + player.name + "'s vice mayor will be...");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(player.link.name + "! Congratulations to you as well!");
                }
                player.state = "Elected";
            } else {
                player.campaignFunds = player.campaignFunds + (player.linkMoney/2) + (player.charity) +
                        (player.selfInvest);
                System.out.println("Another exciting day on the campaign trail!");
                System.out.println("You have a rally in a local neighborhood");
                System.out.println("Would you like to:");
                System.out.println("A: Make promises you can't keep.");
                System.out.println("B: Be really vague.");
                System.out.println("C: Admit that there's not much you can change but you promise to "
                        + "bring a no bull shit business perspective to politics.");
                ans = user.nextLine().toLowerCase();
                if (ans.startsWith("a")) {
                    System.out.println("They loved you! But a few looked skeptical");
                    player.income = player.income + 12;
                    player.campaignFunds = player.campaignFunds + (Math.random()*40) + 20;
                } else if (ans.startsWith("b")) {
                    System.out.println("Nobody didn't like you but no one really knew what happened");
                    player.income = player.income + 6;
                    player.campaignFunds = player.campaignFunds - (Math.random()*40) + 20;
                } else if (ans.startsWith("c")) {
                    double response = Math.random();
                    if (response < .51) {
                        System.out.println("THE CROWD WENT INSANE.");
                        player.income = player.income + 24;
                        System.out.println("Nothing you said made sense but they loved it.");
                        player.campaignFunds = player.campaignFunds + (Math.random()*100);
                    } else {
                        System.out.println("You just made an absolute fool of yourself.");
                        player.campaignFunds = player.campaignFunds - (Math.random()*60);
                        player.income = player.income - 4;
                    }
                }
            }
        }
        System.out.println();
    }

    public static void dead(Player player) {
        player.charity = 0;
        player.selfInvest = 0;
        player.promoted = false;
        Scanner user = new Scanner(System.in);
        System.out.println("Welcome to purgatory, " + player.name + "." );
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("In order to escape you must win my game.");
        System.out.println("The rules are simple. We go back and forth choosing numbers "
                + "between 1 and 3");
        System.out.println("Whoever reaches or breaks 11 loses.");
        System.out.println("If you win, you get your life back!");
        int count = 0;
        Random rand = new Random();
        System.out.println("You start.");
        boolean purg = false;
        while(count < 11) {
            if(purg) {
                int choice = (rand.nextInt(3) + 1);
                if (count == 1) {
                    choice = 1;
                }
                if (count >= 3 && count <= 5) {
                    choice = 6 - count;
                }
                if (count >= 7 && count <= 9) {
                    choice = 10 - count;
                }
                System.out.println("My turn... I choose " + choice);
                count += choice;
                purg = false;
            } else {
                System.out.println("Your turn...");
                int yourChoice = user.nextInt();
                count += yourChoice;
                if (yourChoice != 1 && yourChoice != 2 && yourChoice != 3) {
                    count = 11;
                    System.out.println("Nice try.");
                }
                purg = true;
            }
            System.out.println("The count is " + count);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (purg) {
            System.out.println("Looks like you're stuck in Purgatory for another turn!!!");
            System.out.println("HA HA HA HA HA");
        } else {
            System.out.println("You win...");
            System.out.println("You will wake up in your bed like in Groundhog Day");
            player.state = "Start";
            player.checkings = 0;
            player.savings = 0;
        }
    }
}
