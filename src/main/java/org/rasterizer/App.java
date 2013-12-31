package org.rasterizer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class App {

  public static void main(String[] args) throws Exception {
    Server server = new Server(8080);
    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/");
    context.addServlet(RasterizerServlet.class, "/rasterize");
    server.setHandler(context);
    server.start();
  }
}
