package fn10.smm;

import com.mojang.logging.LogUtils;
import fn10.smm.server.ModManagerWebServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;

public class ModManager implements ModInitializer {

    public static final Logger LOG = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOG.info("ServerModManager initializing...");

        CommandRegistrationCallback.EVENT.register(new ModManagerCommands());
        ServerLifecycleEvents.SERVER_STOPPING.register(ModManagerWebServer.StoppingEvent);
    }
}
