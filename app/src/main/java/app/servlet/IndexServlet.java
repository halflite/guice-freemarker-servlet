package app.servlet;

import java.io.IOException;

import com.google.inject.Singleton;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
public class IndexServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    ServletOutputStream out = resp.getOutputStream();
    out.write("hello world".getBytes());
    out.flush();
    out.close();
  }

}
