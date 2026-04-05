package fn10.smm;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;

public class ModManagerCommands implements CommandRegistrationCallback {

    private static ModManagerWebServer currentServer;

    public static final Command<CommandSourceStack> startCommand = css -> {
        final CommandSourceStack src = css.getSource();
        Integer port = 8880;
        try {
            port = css.getArgument("port", Integer.class);
        } catch (Exception ignored) {
        }
        src.sendSystemMessage(Component.literal("Starting web server at port: " + port));
        if (ModManagerWebServer.isServerRunning()) {
            src.sendFailure(Component.literal("Failed to start web server. See log for details."));
            return 1;
        } else {
           try {
                currentServer = new ModManagerWebServer(port);
                currentServer.start();
               src.sendSystemMessage(Component.literal("Server started at localhost:" + port));
            } catch (Exception e) {
               src.sendFailure(Component.literal("Failed to start web server. See log for details."));
           }
        }
        return 1;
    };

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        dispatcher.register(
                Commands.literal("servermodman")
                        .executes(css -> {
                            css.getSource().sendSystemMessage(Component.literal("-------- Server Mod Manager --------"));
                            css.getSource().sendSystemMessage(Component.literal("--------     By xFN10x    --------"));
                            return 1;
                        }).then(
                                Commands.literal("start")
                                        .requires(src -> src.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                                        .executes(startCommand)
                        )
        );
    }
}
