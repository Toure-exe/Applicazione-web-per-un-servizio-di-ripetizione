package com.example.progettoiumtweb;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import DAO.*;
import com.google.gson.Gson;

@WebServlet(name = "SessionServlet", value = "/SessionServlet")
public class SessionServlet extends HttpServlet {
    private HttpSession session;
    DAO dao = null;

    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        session = null;
        DAO.registerDriver();
        ServletContext ctx = conf.getServletContext();
        String url = ctx.getInitParameter("DB-URL");
        String user = ctx.getInitParameter("user");
        String pwd = ctx.getInitParameter("pwd");
        dao = new DAO(url, user, pwd);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            String act = request.getParameter("submit");
            String subject, teacher, day, hour, emailUser;
            emailUser = (String)session.getAttribute("email");
            System.out.println("EMAIL UTENTE: "+emailUser);
            ArrayList<String> result;
            ArrayList<StudentTutoring> st;
            String s;
            response.setContentType("application/json");
            Gson gson = new Gson(); // traduttore da e verso formato JSON
            switch (act) {
                case "insertBooking":
                    subject = request.getParameter("subject");
                    teacher = request.getParameter("teacher");
                    System.out.println("SESSIONSERVLET: "+ teacher);
                    day = request.getParameter("day");
                    hour = request.getParameter("hour");
                    if (dao.insertBooking(subject, teacher, day, hour, emailUser))
                        out.print("true");
                    else
                        out.print("false");
                    break;

                case "getStudentSubject":
                    result = dao.getStudentSubject(emailUser);
                    s = gson.toJson(result);
                    System.out.println("STRINGA JSON " + s);
                    out.print(s);
                    break;

                case "getStudentTutoring":
                    st = dao.getStudentTutoring(emailUser);
                    s = gson.toJson(st);
                    System.out.println("STRINGA JSON " + s);
                    out.print(s);
                    break;

                case "confirmTutoring":
                    if (dao.confirmTutoring(emailUser, request.getParameter("date"), request.getParameter("hour"), request.getParameter("teacher"), request.getParameter("subject")))
                        out.print("true");
                    else
                        out.print("false");
                    break;

                default:
                    System.out.println("Errore");
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String act = request.getParameter("submit");
            String email, password, rPassword;
            switch(act) {
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
                    System.out.println("logout");
                    break;

                case "indexSession":
                    System.out.println(session);
                    if(session != null){
                        out.print("Welcome " + session.getAttribute("email"));
                    } else {
                        out.print("Welcome guest");
                    }
                    System.out.println("sessioneprova");
                    break;

                case "getRoleSession":
                    if (session == null)
                        out.print(0);
                    else
                        out.print(session.getAttribute("role"));
                    break;

                default:
                    System.out.println("Error");
                    break;
            }
        }
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response, String email, int role) throws ServletException, IOException {
        System.out.println(role);
        session = request.getSession();
        System.out.println(session);
        System.out.println(email);
        if (email != null){
            System.out.println("p4");
            session.setAttribute("email", email);
            session.setAttribute("role", role);
        }
        System.out.println("p3");
    }

    public void processLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        session.invalidate();
        session = null;
        out.print("logout");
    }
}
