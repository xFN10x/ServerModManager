package fn10.smm;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import fn10.smm.server.ModManagerWebServer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.Screenshot;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
        final Integer finalPort = port;
        src.sendSystemMessage(Component.literal("Starting web server at port: " + finalPort));
        if (ModManagerWebServer.isServerRunning()) {
            src.sendFailure(Component.literal("Failed to start web server. Server is already running."));
            return 0;
        } else {
           try {
                currentServer = new ModManagerWebServer(port, src.getServer());
                currentServer.start();
               src.sendSuccess(() -> Component.literal("Server started at localhost:" + finalPort), true);
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
                                        .then(Commands.argument("port", IntegerArgumentType.integer(1,65535))
                                                .executes(startCommand))

                        ).then(
                                Commands.literal("stop")
                                        .requires(src -> src.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                                        .executes(css -> {
                                            final CommandSourceStack src = css.getSource();
                                            if (ModManagerWebServer.isServerRunning()) {
                                                currentServer.stop();
                                                src.sendSuccess(() -> Component.literal("Stopped web server"), true);
                                                return 1;
                                            } else {
                                                src.sendFailure(Component.literal("No web server is running."));
                                                return 0;
                                            }
                                        })
                        )
        );
    }
}
