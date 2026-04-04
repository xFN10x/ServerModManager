package fn10.smm;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModManager implements ModInitializer {

    public static final Logger LOG = LoggerFactory.getLogger("ServerModManager");

    @Override
    public void onInitialize() {
        LOG.info("ServerModManager initializing...");

    }
}
