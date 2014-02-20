package org.rasterizer;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.codec.binary.Base64OutputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Converts input SVG into PNG output with optional width and height.
 */
public class RasterizerServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    RequestDispatcher view = req.getRequestDispatcher("index.html");
    view.forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      String data = req.getParameter("svg");
      String width = req.getParameter("w");
      String height = req.getParameter("h");
      boolean useBase64 = req.getHeader("HTTP_X_ACCEPT_ENCODING") != null;
      PNGTranscoder transcoder = new PNGTranscoder();
      if (height != null) {
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, new Float(height));
      }
      if (width != null) {
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Float(width));
      }
      resp.setContentType("image/png");
      OutputStream outputStream;
      if (useBase64) {
        resp.addHeader("Content-Encoding", "base64");
        outputStream = new Base64OutputStream(resp.getOutputStream());
      } else {
        outputStream = resp.getOutputStream();
      }      
      transcoder.transcode(
          new TranscoderInput(new ByteArrayInputStream(data.getBytes("UTF-8"))),
          new TranscoderOutput(outputStream));
      outputStream.close();
      resp.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
