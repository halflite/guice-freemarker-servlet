package app.inject;

import java.time.ZoneId;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import app.helper.DateHelper;
import app.servlet.IndexServlet;
import app.servlet.JsonServlet;
import freemarker.ext.jakarta.servlet.FreemarkerServlet;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
public class AppContextListener extends GuiceServletContextListener {
  /** logger */
  private final static Logger LOG = LoggerFactory.getLogger(DateHelper.class);

  /** FreeMarker設定パラメーター */
  private final Map<String, String> fmInitParam;
  /** タイムゾーン定義 */
  private final ZoneId zoneId;

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        // DateHelperをDI
        // TODO: より良いDIを考える
        bind(DateHelper.class).toInstance(new DateHelper(zoneId));
        // FreemarkerServlet を SingletonスコープでDI
        bind(FreemarkerServlet.class).in(Singleton.class);

        serve("/").with(IndexServlet.class);
        serve("/json").with(JsonServlet.class);

        serve("*.ftl").with(FreemarkerServlet.class, fmInitParam);
      }
    });
  }

  @Inject
  public AppContextListener(@Named("freemarker.init.parameters") Map<String, String> fmInitParam,
      @Named("tz") String tz) {
    this.fmInitParam = fmInitParam;
    LOG.info("timezone: {}", tz);
    this.zoneId = ZoneId.of(tz);
  }
}
