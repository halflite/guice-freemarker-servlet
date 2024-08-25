package app.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import app.servlet.IndexServlet;
import jakarta.inject.Singleton;

@Singleton
public class AppContextListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new ServletModule() {
      @Override
      protected void configureServlets() {
        serve("/").with(IndexServlet.class);
      }
    });
  }
}
