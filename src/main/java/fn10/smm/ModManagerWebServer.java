package fn10.smm;

import net.freeutils.httpserver.HTTPServer;

import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

import static fn10.smm.ModManager.LOG;

public class ModManagerWebServer {

    public final int port;
    public final HTTPServer server;
    private HttpClient client = null;
    private static boolean webServerRunning;

    public HttpClient getClient() {
        return client;
    }

    public static boolean isServerRunning() {return webServerRunning;}

    public ModManagerWebServer(int port) {
        this.port = port;
        this.server = new HTTPServer();
        server.setPort(this.port);
    }

    public void start() throws Exception {
        try {
            LOG.info("Starting web server...");

            server.start();
            client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

            final HTTPServer.VirtualHost host = server.getVirtualHost(null);
            host.addContext("/", (req, resp) -> {
                resp.getBody().write("yo, world".getBytes(StandardCharsets.UTF_8));
                resp.send(200, "where is this");
                return 0;
            });
            LOG.info("Started web server at: {}", port);
            webServerRunning = true;
        } catch (Exception e) {
            LOG.error("Failed to start web server", e);
            throw e;
        }
    }
}
