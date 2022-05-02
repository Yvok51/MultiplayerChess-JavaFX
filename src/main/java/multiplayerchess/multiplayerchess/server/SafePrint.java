package multiplayerchess.multiplayerchess.server;

public class SafePrint {
    public static synchronized void print(String s) {
        System.out.println(s);
    }
    public static synchronized void printErr(String s) {
        System.out.println(s);
    }
}
