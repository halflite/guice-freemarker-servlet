package app.servlet;

import java.io.IOException;
import java.util.Date;

import app.helper.DateHelper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
public class IndexServlet extends HttpServlet {

  private final DateHelper dateHelper;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // 現在時刻を設定
    Date now = this.dateHelper.nowDate();
    req.setAttribute("now", now);
    // index.ftl を表示
    req.getRequestDispatcher("/index.ftl").forward(req, resp);
  }

  @Inject
  public IndexServlet(DateHelper dateHelper) {
    this.dateHelper = dateHelper;
  }
}