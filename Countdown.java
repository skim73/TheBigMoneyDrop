import java.util.Timer;
import java.util.TimerTask;

class Countdown extends Thread {
    private Timer timer;
    private TimerTask countdown;
    private int t;
    private Wagering thread;

    public Countdown(Wagering thread) {
        timer = new Timer();
        this.thread = thread;
        t = 60;
        countdown = new TimerTask() {
            @Override
            public void run() {
                if (--t == 30) {
                    System.out.println("\n**** " + t + " SECONDS LEFT ****");
                } else if (t == 10) {
                    System.out.println("\n**** " + t + " SECONDS LEFT. WAGER ALL YOUR MONEY NOW! ****");
                } else if (t == 0) {
                    System.out.println("\n------ TIME! ------ \n[Enter \"T\" to continue, or finish your wager" +
                        "if you were in the middle of wagering.]");
                    thread.interrupt();
                    cancel();
                }
            }
        };
    }

    @Override
    public void run() {
        timer.schedule(countdown, 1000, 1000);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        countdown.cancel();
        interrupt();
    }
}
