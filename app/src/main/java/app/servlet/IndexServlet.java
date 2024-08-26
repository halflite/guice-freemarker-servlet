package app.servlet;

import java.io.IOException;

import com.google.inject.Singleton;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
public class IndexServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // index.ftl を表示
    req.getRequestDispatcher("/index.ftl").forward(req, resp);
  }

}
