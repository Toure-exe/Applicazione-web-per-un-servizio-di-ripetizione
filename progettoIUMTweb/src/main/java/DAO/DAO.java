package DAO;

import java.sql.*;
import java.util.ArrayList;

public class DAO {
    private String url;
    private String user;
    private String pwd;

    public DAO(String url, String user, String pwd){
        this.url = url;
        this.user = user;
        this.pwd = pwd;
    }

    public static void registerDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    public void insertStudent(String email, String password, int role) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");

            Statement st = conn1.createStatement();
            String query = "INSERT INTO Utente (emailUtente, password, ruolo) VALUES ('"+email+"', '"+password+"', '"+role+"');";
            System.out.println(query);
            st.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                }
            }
        }
    }

    public int searchUser(String email, String password) {
        Connection conn1 = null;
        int result = -1;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            String query = "SELECT * FROM Utente WHERE emailUtente='"+ email +"' AND password='"+ password +"';";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            if (!rs.isBeforeFirst())
                result = -1;
            else if (rs.next())
                result = Integer.parseInt(rs.getString("ruolo"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                }
            }
        }
        return result;
    }

    public ArrayList<String> getSubjectAvailable() {
        ArrayList<String> res = new ArrayList<>();
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            String query = "SELECT NomeCorso FROM Corso;";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            if (!rs.isBeforeFirst()) //resultSet vuoto
                return res;
            else {
                while(rs.next()) {
                    res.add(rs.getString("NomeCorso"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                }
            }
        }
        return res;
    }

    public ArrayList<String> getTeachers(String subject) {
        String[] temp = subject.split(" ");
        System.out.println("PROVA STRINGA"+temp[1]+"aaaaaaaa");
        ArrayList<String> res = new ArrayList<>();
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            String query = "SELECT IDCorso FROM Corso WHERE NomeCorso ='"+temp[1]+"';";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            if (!rs.next()) //resultSet vuoto
                return res;
            else {
                query = "SELECT d.nome, d.cognome FROM Insegnamento as i JOIN Docente as d WHERE i.IDCorso = '" + rs.getString("IDCorso")+ "' AND i.emailDocente = d.emailDocente;";
                System.out.println(query);
                rs = st.executeQuery(query);
                if (!rs.isBeforeFirst()) //resultSet vuoto
                    return res;
                else {
                    while(rs.next()) {
                        res.add(rs.getString("nome") + " " + rs.getString("cognome"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("qui ci cadi"+e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                }
            }
        }
        return res;
    }

}
