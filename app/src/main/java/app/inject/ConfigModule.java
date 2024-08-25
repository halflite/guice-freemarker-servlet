package app.inject;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceFilter;

import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.servlet.DispatcherType;

/** 設定値/環境変数をDIするモジュール */
public class ConfigModule extends AbstractModule {
  /** logger */
  private final static Logger LOG = LoggerFactory.getLogger(ConfigModule.class);

  @Override
  protected void configure() {
    // MicroProfileConfigをDIする
    final Map<String, String> props = new HashMap<>();
    StreamSupport.stream(ConfigProvider.getConfig().getConfigSources().spliterator(), false)
        .map(ConfigSource::getProperties)
        .forEach(props::putAll);
    Names.bindProperties(this.binder(), props);

    // ServletContextListener の実装をDIする
    bind(AppContextListener.class);
  }

  @Provides
  @Singleton
  public Server providesServer(@Named("server.port") Integer port,
      AppContextListener appContextListener) {
    LOG.info("appContextListener: {}", appContextListener);
    Server server = new Server(port);

    ServletContextHandler contextHandler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
    contextHandler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
    contextHandler.addEventListener(appContextListener);

    return server;
  }

}
