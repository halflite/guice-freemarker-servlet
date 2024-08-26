package app.inject;

import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import app.servlet.IndexServlet;
import app.servlet.JsonServlet;
import freemarker.ext.jakarta.servlet.FreemarkerServlet;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
public class AppContextListener extends GuiceServletContextListener {

  /** FreeMarker設定パラメーター */
  private final Map<String, String> fmInitParam;

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        bind(FreemarkerServlet.class).in(Singleton.class);

        serve("/").with(IndexServlet.class);
        serve("/json").with(JsonServlet.class);

        serve("*.ftl").with(FreemarkerServlet.class, fmInitParam);
      }
    });
  }

  @Inject
  public AppContextListener(@Named("freemarker.init.parameters") Map<String, String> fmInitParam) {
    this.fmInitParam = fmInitParam;
  }
}
