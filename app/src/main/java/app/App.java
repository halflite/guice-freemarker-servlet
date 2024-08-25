package app;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.inject.Guice;
import com.google.inject.Injector;

import app.inject.ConfigModule;

/** App Main */
public class App {
  /** logger */
  private final static Logger LOG = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    // jul to slf4j
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    // Server
    Injector injector = Guice.createInjector(new ConfigModule());
    Server server = injector.getInstance(Server.class);

    try {
      LOG.info("started.");
      server.start();
      server.join();
    } catch (Exception e) {
      LOG.warn("error.", e);
      server.destroy();
    }
  }
}
