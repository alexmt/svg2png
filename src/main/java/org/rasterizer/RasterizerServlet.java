package org.rasterizer;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Converts input SVG into PNG output with optional width and height.
 */
public class RasterizerServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      String data = req.getParameter("svg");
      String width = req.getParameter("w");
      String height = req.getParameter("h");
      PNGTranscoder transcoder = new PNGTranscoder();
      if (height != null) {
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, new Float(height));
      }
      if (width != null) {
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Float(width));
      }
      transcoder.transcode(
          new TranscoderInput(new ByteArrayInputStream(data.getBytes("UTF-8"))),
          new TranscoderOutput(resp.getOutputStream()));
      resp.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception e) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
