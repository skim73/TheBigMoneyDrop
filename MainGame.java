import java.io.*;
import java.util.*;

class MainGame extends Thread {
    @Override
    public void run() {
        Random qnaPack = new Random();
        Scanner qna = null;
        try {
            qna = new Scanner(new File("QnA" + qnaPack.nextInt(1) + ".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner in = new Scanner(System.in);
        final Map<String, Integer> letterToIndex = new HashMap<>();
        letterToIndex.put("A", 0);
        letterToIndex.put("B", 1);
        letterToIndex.put("C", 2);
        letterToIndex.put("D", 3);

        int money = 50;
        byte questionNum = 1;
        ArrayList<String> answers = new ArrayList<String>();

        System.out.println("\nWelcome to the Big Money Drop! In front of you are $500,000 in cash," +
            "\ndivided into 50 bundles each worth $10,000." +
            "\nThe goal of the game is to maintain all that money by answering 7 question correctly.");
        proceed(in);
        System.out.println("\nYou answer question by wagering ALL your money on the answer choices." +
            "\nAny money wagered on the incorrect answers will drop to the abyss, never to return." +
            "\nIn every question, YOU MUST LEAVE ONE ANSWER EMPTY, $0 WAGERED.");
        proceed(in);
        System.out.println("\nAre you ready? Then let's play the Big Money Drop!");
        proceed(in);


        do {
            String category1 = qna.nextLine(), category2 = qna.nextLine();
            System.out.println("\n\n~~~ QUESTION " + questionNum + " ~~~");
            System.out.println("[" + PrintMoney.printMoney(money * 10000) + " ON PLAY]");
            if (questionNum == 4) {
                System.out.println("\nNow, there will only be 3 answer choices, but you must still " +
                    "leave one answer empty, $0 wagered.");
                proceed(in);
            } else if (questionNum == 7) {
                System.out.println("\nThe final question will only have 2 answer choices. You will either " +
                    "leave with " + PrintMoney.printMoney(money * 10000) + "\nor nothing.");
                proceed(in);
            }
            System.out.println("\nWhich category will you choose?" +
                "\n (1) " + category1 +
                "\n (2) " + category2);

            byte c;
            try {
                c = in.nextByte();
            } catch (InputMismatchException e) {
                c = 2;
            }
            if (c <= 1) {
                System.out.println("\nYou chose " + category1);
            } else {
                System.out.println("\nYou chose " + category2);
                for (int i = 0; i < numAnswers(questionNum) + 2; i++) {
                    qna.nextLine();
                }
            }

            answers.clear();
            System.out.println("\nHere are the answers for this question:");
            for (int i = 0; i < numAnswers(questionNum); i++) {
                delay(2000);
                presentAnswer(answers, qna);
            }
            delay(2000);

            System.out.println("\nThe question is this:");
            String question = qna.nextLine();
            if (question.length() > 120) {
                question = question.substring(0, 120) + "\n" + question.substring(120);
            }
            delay(2000);
            System.out.println(question);
            delay(5000);

            int[] wagers = new int[numAnswers(questionNum)];
            Wagering wagering = new Wagering(money, answers, question, wagers, in);
            Countdown countdown = new Countdown(wagering);
            System.out.println("\nYou have 1 minute to wager your money on the answers as soon" +
                " as you proceed.\n(***Any money not wagered will be confiscated***)");
            delay(5000);

            countdown.start();
            try {
                countdown.join();
            } catch (InterruptedException e) {

            }


            money -= wagering.getRemainingMoney();
            System.out.println("\nThese are your final wagers:");
            for (int i = 0; i < answers.size(); i++) {
                System.out.printf(answers.get(i) + "$%7s]\n",
                    PrintMoney.printMoney(wagers[i] * 10000).substring(1));
            }
            proceed(in);


            System.out.println("Let's see what's going to drop.");
            delay(3200);
            for (int i = 0; i < numAnswers(questionNum) - 1; i++) {
                String droppedAnswer = qna.next();
                System.out.println("(" + droppedAnswer + "): " +
                    String.format("$%7s",
                        PrintMoney.printMoney(wagers[letterToIndex.get(droppedAnswer)] * 10000).substring(1))
                        + " DROPPED!");
                money -= wagers[letterToIndex.get(droppedAnswer)];
                delay(qna.nextLong());
            }
            qna.nextLine();
            if (c <= 1) {
                for (int i = 0; i < numAnswers(questionNum) + 2; i++) {
                    qna.nextLine();
                }
            }

            System.out.println("\nYou now have " + PrintMoney.printMoney(money * 10000) + " on play.");
            proceed(in);
            questionNum++;


        } while (money > 0 && questionNum <= 7);

        if (money == 0) {
            System.out.println("\n~~~~ GAME OVER ~~~~ \nAll your money has been dropped.");
        } else {
            System.out.println("\nCONGRATULATIONS!\nYou won " + PrintMoney.printMoney(money * 10000) + "!!!");
        }
        System.out.println("Thank you so much for playing!");
        System.exit(0);

    }

    private static void proceed(Scanner scanner) {
        System.out.print("\n[ENTER 0 TO CONTINUE] ");
        try {
            scanner.nextInt();
        } catch (InputMismatchException e) {

        }
        scanner.nextLine();
    }

    private static byte numAnswers(byte questionNum) {
        if (questionNum <= 3) {
            return 4;
        }
        if (questionNum <= 6) {
            return 3;
        }
        return 2;
    }

    private static void delay(long s) {
        try {
            Thread.sleep(s);
        } catch (Exception e) {

        }
    }

    private static void presentAnswer(List<String> list, Scanner scanner) {
        list.add(scanner.nextLine());
        System.out.println(
            list.get(list.size() - 1).substring(0, list.get(list.size() - 1).length() - 1));
    }
}
