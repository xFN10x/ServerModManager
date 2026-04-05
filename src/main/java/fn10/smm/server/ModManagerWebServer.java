package fn10.smm.server;

import net.freeutils.httpserver.HTTPServer;
import net.minecraft.server.MinecraftServer;

import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

import static fn10.smm.ModManager.LOG;

public class ModManagerWebServer {

    public final int port;
    public final HTTPServer server;
    private HttpClient client = null;
    private final MinecraftServer mcServer;
    private static boolean webServerRunning;

    public HttpClient getClient() {
        return client;
    }

    public static boolean isServerRunning() {return webServerRunning;}

    public ModManagerWebServer(int port, MinecraftServer server) {
        this.port = port;
        this.server = new HTTPServer();
        server.setPort(this.port);
        this.mcServer = server;
    }

    public void stop() {
        try {
            LOG.error("Stopping web server...");
            client.close();

            if (webServerRunning) webServerRunning = false;

            server.stop();
            LOG.error("Stopped web server.");
        } catch (Exception e) {
            LOG.error("Failed to stop web server", e);
            throw e;
        }
    }

    public void start() throws Exception {
        try {
            LOG.info("Starting web server...");

            server.start();
            client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

            final HTTPServer.VirtualHost host = server.getVirtualHost(null);
            host.addContexts(new ModManagerHandlers(mcServer));
            LOG.info("Started web server at: {}", port);
            webServerRunning = true;
        } catch (Exception e) {
            LOG.error("Failed to start web server", e);
            throw e;
        }
    }
}
