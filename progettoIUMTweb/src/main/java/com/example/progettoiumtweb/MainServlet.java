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
    HttpSession session = null;

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
                    int result = dao.searchUser(email, password);
                    if (result >= 0 && result <= 2) {
                        System.out.println("user found");
                        out.println(1); //write the JSONArray to the response
                        processRequest(request, response, email, result);
                    } else {
                        System.out.println("User not found");
                        out.println(0); //write the JSONArray to the response
                    }
                    out.close();
                    break;

                case "logout":
                    processLogout(request, response);
                    break;

                case "indexSession":
                    if(this.session != null){
                        out.print("Welcome " + this.session.getAttribute("email"));
                    } else {
                        out.print("Welcome guest");
                    }
                    System.out.println("sessioneprova");
                    break;

                case "getRoleSession":
                    if (session == null)
                        out.print(0);
                    else
                        out.print(this.session.getAttribute("role"));
                    break;

                default:
                    System.out.println("Error.");
                    break;
            }
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response, String email, int role) throws ServletException, IOException {
        System.out.println(role);
        this.session = request.getSession();
        System.out.println(email);
        if (email != null){
            System.out.println("p4");
            this.session.setAttribute("email", email);
            this.session.setAttribute("role", role);
        }
        System.out.println("p3");
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.session.invalidate();
        this.session = null;
    }

}
