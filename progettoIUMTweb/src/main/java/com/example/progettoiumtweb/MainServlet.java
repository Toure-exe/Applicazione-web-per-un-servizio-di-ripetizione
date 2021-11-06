package com.example.progettoiumtweb;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import DAO.*;

@WebServlet(name = "MainServlet", value = "/MainServlet")
public class MainServlet extends HttpServlet {
    DAO dao = null;

    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        DAO.registerDriver();
        ServletContext ctx = conf.getServletContext();
        String url = ctx.getInitParameter("DB-URL");
        String user = ctx.getInitParameter("user");
        String pwd = ctx.getInitParameter("pwd");
        dao = new DAO(url, user, pwd);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            String act = request.getParameter("submit");
            String email, password, rPassword;
            switch(act) {
                case "registration":
                    email = request.getParameter("email");
                    password = request.getParameter("password");
                    rPassword = request.getParameter("rPassword");
                    if(password.equals(rPassword)){
                        dao.insertStudent(email, password, 1);
                        System.out.println("Student correctly insert in the system");
                    } else {
                        out.println("The password are not equal");
                    }
                    break;

                case "login":
                    email = request.getParameter("email");
                    password = request.getParameter("password");
                    if (dao.searchUser(email, password)){
                        out.println("User found");
                        processRequest(request, response, email);
                    } else
                        out.println("User not found");
                    break;

                case "logout":
                    processLogout(request, response);
                    break;

                default:
                    System.out.println("Error.");
                    break;
            }
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response, String email) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession s = request.getSession();
        if (email != null)
            s.setAttribute("email", email);
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>User Session</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>Sei collegato come: " + s.getAttribute("email") + "</p>");
            out.println("<form action=\"/progettoIUMTweb_war_exploded/MainServlet\" method=\"post\">");
            out.println("<button type=\"submit\" name=\"submit\" value=\"logout\">Logout</button>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        session.invalidate();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Life1</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>You are successfully logged out!</p>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

}
