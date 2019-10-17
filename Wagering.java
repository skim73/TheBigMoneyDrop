import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

class Wagering extends Thread {
    private int money;
    private ArrayList<String> answers;
    private String question;
    private int[] wagers;
    private Scanner in;

    public Wagering(int money, ArrayList<String> answers, String question, int[] wagers, Scanner in) {
        this.money = money;
        this.answers = answers;
        this.question = question;
        this.wagers = wagers;
        this.in = in;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            System.out.println("\n\n\n------ " + PrintMoney.printMoney(money * 10000) + " LEFT TO WAGER ------");
            System.out.println(question);
            for (int i = 0; i < answers.size(); i++) {
                System.out.printf(answers.get(i) + "$%7s]\n",
                    PrintMoney.printMoney(wagers[i] * 10000).substring(1));
            }
            enterWager();
        }
    }

    private void enterWager() {
        System.out.println("\nOn which answer will you wager? A, B, C, or D?" +
            "\n Enter \"S\" to stop the clock when wagered all your money.");
        try {
            switch (in.next()) {
                case "A":
                    System.out.print("SET YOUR NEW WAGER ON (A): $10,000 X ");
                    try {
                        checkWager(in.nextInt(), 0);
                    } catch (InputMismatchException e) {

                    }
                    break;
                case "B":
                    System.out.print("SET YOUR NEW WAGER ON (B): $10,000 X ");
                    try {
                        checkWager(in.nextInt(), 1);
                    } catch (InputMismatchException e) {

                    }
                    break;
                case "C":
                    if (answers.size() >= 3) {
                        System.out.print("SET YOUR NEW WAGER ON (C): $10,000 X ");
                        try {
                            checkWager(in.nextInt(), 2);
                        } catch (InputMismatchException e) {

                        }
                    }
                    break;
                case "D":
                    if (answers.size() == 4) {
                        System.out.print("SET YOUR NEW WAGER ON (D): $10,000 X ");
                        try {
                            checkWager(in.nextInt(), 3);
                        } catch (InputMismatchException e) {

                        }
                    }
                    break;
                case "S":
                    if (money > 0) {
                        System.out.println("\n#### You still have money left to wager! ####");
                    } else {
                        System.out.println("------ STOP THE CLOCK! ------");
                        System.out.println("[Enter \"T\" to continue.]");
                        in.next();
                        in.nextLine();
                        interrupt();
                    }
                    break;
                default:
                    break;

            }
        } catch (InputMismatchException e) {
            System.out.println();
        }
    }

    private void checkWager(int wager, int pos) {
        if (wager < 0) {
            System.out.println("\n#### Invalid wager! ####");
            return;
        }
        if (wager - wagers[pos] > money) {
            System.out.println("\n#### You don't have" + PrintMoney.printMoney(wager * 10000) 
                + " left to wager! ####");
            return;
        }
        money -= wager - wagers[pos];
        wagers[pos] = wager;
        int[] sorted = Arrays.copyOf(wagers, wagers.length);
        Arrays.sort(sorted);
        if (Arrays.binarySearch(sorted, 0) == -1) {
            wagers[pos] -= wager;
            System.out.println("\n#### YOU MUST LEAVE ONE ANSWER WAGERED $0! ####");
            money += wager;
        }
    }

    int getRemainingMoney() {
        return money;
    }

}
