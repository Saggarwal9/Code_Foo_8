
public class Utility {
    public static void tSleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
