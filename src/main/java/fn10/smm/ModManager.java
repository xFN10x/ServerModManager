package fn10.smm;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;

public class ModManager implements ModInitializer {

    public static final Logger LOG = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOG.info("ServerModManager initializing...");

        CommandRegistrationCallback.EVENT.register(new ModManagerCommands());
    }
}
