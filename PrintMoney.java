class PrintMoney {
    static String printMoney(int money) {
        return "$" + String.format("%,d", money);
    }
}