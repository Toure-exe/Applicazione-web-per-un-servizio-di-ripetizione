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
        System.out.println("ENTRA");
        try (PrintWriter out = response.getWriter()) {
            System.out.println("ENTRA1");
            String act = request.getParameter("submit");
            String email, password, rPassword;
            System.out.println(act);
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
                    if (dao.searchUser(email, password)) {
                        System.out.println("user found");
                        out.println(1); //write the JSONArray to the response
                        processRequest(request, response, email);
                    } else {
                        System.out.println("User not found");
                        out.println(0); //write the JSONArray to the response
                    }
                    out.close();
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
        System.out.println("p1");
        HttpSession s = request.getSession();
        System.out.println(email);
        if (email != null){
            System.out.println("p4");
            s.setAttribute("email", email);
        }
        System.out.println("p3");
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        session.invalidate();
    }

}
