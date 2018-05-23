public class Player {
    String name;
    String state;
    double savings;
    double income; // added to checkings each turn
    double checkings;
    double interestRate; // multiplied by savings each turn
    double selfInvest; //
    double charity;
    Player link;
    double linkMoney;
    double campaignFunds;
    boolean promoted;
    public Player(String name) {
        this.name = name;
        state = "Start";
        savings = 0; checkings = 0; income = 0;
        selfInvest = 0; charity = 0; linkMoney = 0;
        interestRate = 1.05;
        promoted = false;
        campaignFunds = 0;
        link = null;
    }

    public String getAccounts() {
        String[][] accounts = {
                {"Savings:", "$" + savings},
                {"Interest Rate:", "" + (interestRate - 1)*100 + "%"},
                {"Checkings:", "$" + checkings},
                {"Income:", "$" + income}
        };
        return accounts.toString();
    }

    public void updateFunds(){
        checkings = checkings + income;
        savings = Math.round((savings*interestRate)*100.0)/100.0;
        linkMoney = linkMoney*interestRate;
    }
}
