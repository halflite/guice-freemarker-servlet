package app.servlet;

import java.io.IOException;
import java.util.Map;
import com.google.gson.Gson;
import com.google.inject.Singleton;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
public class JsonServlet extends HttpServlet {

  private final Gson gson;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    ServletOutputStream out = resp.getOutputStream();
    Map<String, Object> result = Map.of("message", "Hello Json!!");
    out.write(gson.toJson(result).getBytes());
    out.flush();
    out.close();
  }

  @Inject
  public JsonServlet(Gson gson) {
    this.gson = gson;
  }
}
