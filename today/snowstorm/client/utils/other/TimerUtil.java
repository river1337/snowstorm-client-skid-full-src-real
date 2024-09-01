package today.snowstorm.client.utils.other;

public class TimerUtil {

    public long lstMs = System.currentTimeMillis();


    // Resets timer
    public void reset() {
        lstMs = System.currentTimeMillis();
    }

    // Usage: timer.stopAfter(numberOfSeconds, restartWhenSecondsReached)
    public boolean stopAfter(long time, boolean reset) {
        if (lstMs > System.currentTimeMillis()) {
            lstMs = System.currentTimeMillis();
        }

        if (System.currentTimeMillis() - lstMs > time) {
            if (reset) {
                reset();
            }
            return true;
        }else {
            return false;
        }
    }

    public double getMs() {
        return System.currentTimeMillis() - lstMs;
    }

    public void setMs(long time) {
        lstMs = time;
    }

}
