package com.example.progettoiumtweb;

import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import DAO.*;
import com.google.gson.Gson;

@WebServlet(name = "MainServlet", value = "/MainServlet")
public class MainServlet extends HttpServlet {
    DAO dao = null;
    SessionServlet sessionObj;

    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        DAO.registerDriver();
        ServletContext ctx = conf.getServletContext();
        String url = ctx.getInitParameter("DB-URL");
        String user = ctx.getInitParameter("user");
        String pwd = ctx.getInitParameter("pwd");
        dao = new DAO(url, user, pwd);
        sessionObj = new SessionServlet();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("debug attuale1");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String act = request.getParameter("submit");
        Gson gson = new Gson(); // traduttore da e verso formato JSON
        ArrayList<String> result;
        ArrayList<Tutoring> tutorings;
        String s;
        switch(act) {
            case "subjectAvailable":
                System.out.println("debug attuale2");
                result = dao.getSubjectAvailable();
                System.out.println("debug attuale3");
                s = gson.toJson(result);
                System.out.println("debug attuale4");
                System.out.println("STRINGA JSON " + s);
                System.out.println("debug attuale5");
                out.print(s);
                break;

            case "getTeachers":
                result = dao.getTeachers(request.getParameter("subject"));
                s = gson.toJson(result);
                System.out.println("STRINGA JSON " + s);
                out.print(s);
                break;

            case "teacherAvailability":
                System.out.println("Ci entri?");
                tutorings = dao.getTutoringsList(request.getParameter("subject"), request.getParameter("teacher"));
                s = gson.toJson(tutorings);
                System.out.println("STRINGA JSON " + s);
                out.print(s);
                break;
        }
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

                default:
                    System.out.println("Error.");
                    break;
            }
        }
    }
}
