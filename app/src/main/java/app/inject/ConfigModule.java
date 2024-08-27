package app.inject;

import java.util.AbstractMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    // MicroProfileConfigを取得
    final Config config = ConfigProvider.getConfig();
    bind(Config.class).toInstance(config);
    // MicroProfileConfigの設定値をDIする
    final Map<String, String> props = new HashMap<>();
    StreamSupport.stream(config.getConfigSources().spliterator(), false)
        .map(ConfigSource::getProperties)
        .forEach(props::putAll);
    LOG.debug("DI params: {}", props);
    Names.bindProperties(this.binder(), props);

    // Gson をDIする
    Gson gson = new GsonBuilder().serializeNulls()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
    bind(Gson.class).toInstance(gson);

    // ServletContextListener の実装をDIする
    bind(AppContextListener.class);
  }

  /** FreeMarker設定値を作成する */
  @Provides
  @Singleton
  @Named("freemarker.init.parameters")
  public Map<String, String> prividesFreeMarker(Config config) {
    Map<String, String> params = StreamSupport.stream(config.getConfigSources().spliterator(), false)
        .map(ConfigSource::getProperties)
        .map(Map::entrySet)
        .flatMap(Set::stream)
        .filter(e -> e.getKey().startsWith("freemarker."))
        .map(e -> new AbstractMap.SimpleImmutableEntry<String, String>(
            e.getKey().replaceFirst("^freemarker\\.", ""),
            e.getValue()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1));
    LOG.debug("freemarker init params: {}", params);
    return params;
  }

  /** Jetty Server インスタンスを作成 */
  @Provides
  @Singleton
  public Server providesServer(@Named("server.port") Integer port,
      AppContextListener appContextListener) {
    Server server = new Server(port);

    ServletContextHandler contextHandler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
    contextHandler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
    contextHandler.addEventListener(appContextListener);

    return server;
  }
}
