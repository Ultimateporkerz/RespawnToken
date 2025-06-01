package net.ultimporks.resptoken.compat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompatCheck {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final boolean IS_CURIOS_INSTALLED = isCuriosPresent();

    private static boolean isCuriosPresent() {
        try {
            Class.forName("top.theillusivec4.curios.api.CuriosApi");
            LOGGER.info("Curios was found!");
            return true;
        } catch (ClassNotFoundException ex) {
            LOGGER.info("Curios was not found!");
            return false;
        }
    }
}
