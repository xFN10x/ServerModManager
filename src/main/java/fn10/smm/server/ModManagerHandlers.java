package fn10.smm.server;

import net.freeutils.httpserver.HTTPServer;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ModManagerHandlers {
    private final MinecraftServer server;

    public ModManagerHandlers() {
        server = null;
    }

    public ModManagerHandlers(MinecraftServer server) {
        this.server = server;
    }

    public static String getPageAsString(String fileName) throws IOException {
        final URL resource = ModManagerHandlers.class.getResource("/web/" + fileName);
        if (resource == null) return "not found";
        try (var str = resource.openStream()) {
            return new String(str.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @HTTPServer.Context("/")
    public int index(HTTPServer.Request req, HTTPServer.Response resp) throws IOException {
        resp.getHeaders().add("Content-Type", "text/html");
        //resp.getBody().write("yo, world".getBytes(StandardCharsets.UTF_8));
        resp.send(200, getPageAsString("manager.html"));
        return 0;
    }

    @HTTPServer.Context("/detail/{*}")
    public int test(HTTPServer.Request req, HTTPServer.Response resp) throws IOException {
        final String parsed = req.getPath().replace("/detail/", "");
        switch (parsed) {
            case "stype":
                resp.send(200, server.getServerModName());
                return 0;
            case "motd":
                resp.send(200, server.getMotd());
                return 0;
            case "plrs":
                resp.send(200, server.getPlayerCount() + "/" + server.getMaxPlayers());
                return 0;
        }
        resp.send(404, "Detail: '" + parsed + "' not found");
        return 0;
    }

    @HTTPServer.Context("/{*}")
    public int getFile(HTTPServer.Request req, HTTPServer.Response resp) throws IOException {
        final String parsed = req.getPath().replaceFirst("/", "");
        try {
            final String string = getPageAsString(parsed);

            resp.send(200, string);
            return 0;
        } catch (IOException e) {
            resp.send(404, "File: "+ parsed + " is not built in.");
            return 0;
        }
    }
}
