package com.example.progettoiumtweb;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import DAO.*;
import com.google.gson.Gson;

/*
 USER: mario@rossi.it
 PASSWORD: r

 USER: luigi@bianchi.it
 PASSWORD: l

 ADMIN: admin@admin.it
 PASSWORD: root
 */

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
            ArrayList<String> result;
            ArrayList<StudentTutoring> st;
            String s;
            Gson gson = new Gson(); // traduttore da e verso formato JSON
            switch (act) {
                case "insertBooking":
                    response.setContentType("text/html;charset=UTF-8");
                    subject = request.getParameter("subject");
                    teacher = request.getParameter("teacher");
                    day = request.getParameter("day");
                    hour = request.getParameter("hour");
                    if (dao.insertBooking(subject, teacher, day, hour, emailUser))
                        out.print("true");
                    else
                        out.print("false");
                    break;

                case "getStudentSubject":
                    response.setContentType("application/json");
                    result = dao.getStudentSubject(emailUser);
                    s = gson.toJson(result);
                    out.print(s);
                    break;

                case "getStudentTutoring":
                    response.setContentType("application/json");
                    st = dao.getStudentTutoring(emailUser, (int)session.getAttribute("role"));
                    s = gson.toJson(st);
                    out.print(s);
                    break;

                case "confirmTutoring":
                    response.setContentType("text/html;charset=UTF-8");
                    if (dao.confirmTutoring(emailUser, request.getParameter("date"), request.getParameter("hour"), request.getParameter("teacher"), request.getParameter("subject")))
                        out.print("true");
                    else
                        out.print("false");
                    break;

                case "getHistory":
                    response.setContentType("application/json");
                    st = dao.getHistory((int)session.getAttribute("role"), emailUser);
                    s = gson.toJson(st);
                    out.print(s);
                    break;

                case "getRestrictedHistory":
                    response.setContentType("application/json");
                    int status = Integer.parseInt(request.getParameter("status"));
                    st = dao.getRestrictedHistory(status);
                    s = gson.toJson(st);
                    out.print(s);
                    break;

                default:
                    System.out.println("Switch error in doGet function.");
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String act = request.getParameter("submit");
            String email, password;
            switch(act) {
                case "login":
                    email = request.getParameter("email");
                    password = request.getParameter("password");
                    int result = dao.searchUser(email, password);
                    if (result >= 0 && result <= 2) {
                        out.println(1);
                        processRequest(request, response, email, result);
                    } else
                        out.println(0);
                    out.close();
                    break;

                case "logout":
                    processLogout(request, response);
                    System.out.println("Logout");
                    break;

                case "indexSession":
                    if(session != null)
                        out.print("Welcome " + session.getAttribute("email"));
                    else
                        out.print("Welcome guest");
                    break;

                case "getRoleSession":
                    if (session == null)
                        out.print(0);
                    else
                        out.print(session.getAttribute("role"));
                    break;

                case "deleteTutoring":
                    if ((int)session.getAttribute("role") == 2) {
                        if (dao.deleteTutoring(request.getParameter("emailStudent"), request.getParameter("date"), request.getParameter("hour"), request.getParameter("teacher"), request.getParameter("subject"), (int)session.getAttribute("role")))
                            out.print("true");
                        else
                            out.print("false");
                    } else {
                        if (dao.deleteTutoring((String)session.getAttribute("email"), request.getParameter("date"), request.getParameter("hour"), request.getParameter("teacher"), request.getParameter("subject"), (int)session.getAttribute("role")))
                            out.print("true");
                        else
                            out.print("false");
                    }
                    break;

                default:
                    System.out.println("Switch error in doPost function.");
                    break;
            }
        }
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response, String email, int role) throws ServletException, IOException {
        session = request.getSession();
        if (email != null){
            session.setAttribute("email", email);
            session.setAttribute("role", role);
        }
    }

    public void processLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        session.invalidate();
        session = null;
        Gson gson = new Gson();
        out.print(gson.toJson("logout"));
    }
}
