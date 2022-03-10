package com.gapache.vertx.web.server;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;

/**
 * 没有任何操作的WebServer实现
 *
 * @author HuSen
 * @since 2021/3/2 7:59 下午
 */
public class NoOpWebServer implements WebServer {

    private final int port;

    public NoOpWebServer(int port) {
        this.port = port;
    }

    @Override
    public void start() throws WebServerException {

    }

    @Override
    public void stop() throws WebServerException {

    }

    @Override
    public int getPort() {
        return this.port;
    }
}
