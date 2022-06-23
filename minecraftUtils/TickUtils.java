package net.staticstudios.utils;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;


/**
 * @author Sam (GitHub: <a href="https://github.com/Sammster10">Sam's GitHub</a>)
 * <br>
 * <br> Static class for listening to/getting data about tick timings.
 * <br>
 * <br> You can check the server's average MSPT over the last 20 ticks, the server's average TPS over the last 20 ticks, and if the server is healthy. (TPS >= 19.5)
 */
public class TickUtils {

    /**
     * Initializes the Listener to begin tracking tick timings.
     * @param parent The plugin to register the listener with.
     */
    public static void init(JavaPlugin parent) {
        parent.getServer().getPluginManager().registerEvents(new TickListener(), parent);
    }



    //Array to hold tick timings
    static long[] TICK_MSPT = new long[20 * 60 * 10];

    /**
     * @param ticks The number of ticks to get the average MSPT for. Must be between 0 and 12,000, inclusive.
     * @return The average MSPT of the last 20 ticks.
     */
    public static double getMSPT(int ticks) {
        ticks = Math.max(0, Math.min(ticks, 12000)); //Verify that the `ticks` is between 0 and 12,000, if not, correct it
        if (ticks == 0) return 0;
        int ticksRecorded = 0;
        long total = 0;
        boolean first = true;
        for (long mspt : TICK_MSPT) {
            if (first) {
                first = false;
                continue;
            }
            if (mspt == 0) continue; //Skip ticks that haven't been recorded yet
            total += mspt;
            ticksRecorded++;
            if (ticksRecorded >= ticks) break;
        }
        if (ticksRecorded == 0) return 0;
        return (double) total / ticksRecorded
                / 1000000; //Convert to milliseconds
    }

    /**
     * @param seconds The number of seconds to get the average MSPT from. Numbers less than 1 will return 0 and numbers grater than 600 will only return 600 seconds worth of data.
     * @return The average MSPT of the last `seconds` seconds.
     */
    public static double getMSPTOfLastSeconds(int seconds) {
        return getMSPT(seconds * 20);
    }
    /**
     * @return The average MSPT of the last 20 ticks.
     */
    public static double getMSPTOfLastSecond() {
        return getMSPT(20);
    }
    /**
     * @return The average MSPT of the last 1,200 ticks.
     */
    public static double getMSPTOfLastMinute() {
        return getMSPT(1200);
    }
    /**
     * @return The average MSPT of the last 6,000 ticks.
     */
    public static double getMSPTOfLast5Minutes() {
        return getMSPT(6000);
    }
    /**
     * @return The average MSPT of the last 12,000 ticks.
     */
    public static double getMSPTOfLast10Minutes() {
        return getMSPT(12000);
    }

    /**
     * @return The average TPS of the last 20 ticks.
     */
    public static double getTPS() {
        return Math.min(1000 / getMSPTOfLastSecond(), 20);
    }

    /**
     * Check if the server is able to keep up or if it is lagging.
     * @return Whether the server TPS is healthy.
     */
    public static boolean isServerHealthy() {
        return getTPS() >= 19.5;
    }


    //          vvv listener for tick timings vvv

    static class TickListener implements Listener {
        @EventHandler(priority = EventPriority.LOWEST)
        void onTickStart(ServerTickStartEvent event) {
            if (TICK_MSPT[0] > 100000000000L) TICK_MSPT[0] = 0;
            System.arraycopy(TICK_MSPT, 0, TICK_MSPT, 1, TICK_MSPT.length - 1); //Shift the array right
            TICK_MSPT[0] = System.nanoTime(); //Set the first element to the current time in ns (the time the tick started), later calculate the actual tick duration by subtracting this time from the end timing of the tick
        }

        @EventHandler(priority = EventPriority.MONITOR)
        void onTickEnd(ServerTickEndEvent e) {
            TICK_MSPT[0] = System.nanoTime() - TICK_MSPT[0]; //Set the first value in the array to the time it took to complete the tick
        }
    }
}
