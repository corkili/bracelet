package org.bracelet.common.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class OutputUtils {
    public static void print(HttpServletResponse response, String content) throws IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println(content);
        writer.flush();
        writer.close();
    }
}
