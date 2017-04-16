package team_f.server.controller;

import team_f.application.Application;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@WebServlet(name = "example", urlPatterns = {""})
public class Root extends HttpServlet {
    @Override
    public void init() throws ServletException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(MediaType.TEXT_HTML);
        req.getRequestDispatcher(getServletContext().getContextPath() + "/views/pages/plan_overview.jsp").include(req, resp);
    }

    @Override
    public void destroy() {
        Application facade = new Application();
        facade.closeSession();
    }
}
