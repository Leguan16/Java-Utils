package net.staticstudios.utils;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;



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
    static long[] TICK_MSPT = new long[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * @return The average MSPT of the last 20 ticks.
     */
    public static double getMSPT() {
        long total = 0;
        for (long mspt : TICK_MSPT) total += mspt;
        return (double) total / TICK_MSPT.length;
    }

    /**
     *
     * @return The average TPS of the last 20 ticks.
     */
    public static double getTPS() {
        return Math.min(1000 / getMSPT(), 20);
    }

    /**
     *
     * Check if the server is able to keep up or if it is lagging.
     *
     * @return Whether the server TPS is healthy.
     */
    public static boolean isServerHealthy() {
        return getTPS() >= 19.5;
    }


    //          vvv listener for tick timings vvv

    static class TickListener implements Listener {
        @EventHandler(priority = EventPriority.LOWEST)
        void onTickStart(ServerTickStartEvent event) {
            System.arraycopy(TICK_MSPT, 1, TICK_MSPT, 0, TICK_MSPT.length - 1); //Shift the array left
            TICK_MSPT[TICK_MSPT.length - 1] = System.currentTimeMillis(); //Set the last element to the current time in ms (the time the tick started), later calculate the actual tick duration by subtracting this time from the epoc of the time the tick ends
        }

        @EventHandler(priority = EventPriority.MONITOR)
        void onTickEnd(ServerTickEndEvent e) {
            TICK_MSPT[TICK_MSPT.length - 1] = System.currentTimeMillis() - TICK_MSPT[TICK_MSPT.length - 1]; //Set the last value in the array to the time it took to complete the tick
        }
    }
}
