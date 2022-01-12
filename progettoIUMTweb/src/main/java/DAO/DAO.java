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

    public ArrayList<Tutoring> getTutoringsList (String subject, String teacher) {
        ArrayList<Tutoring> res = new ArrayList<>();
        String[] subjectSplit = subject.split(" "); //use only [1]
        String[] TeacherSplit = teacher.split(" "); //use [1] for name, [3] for surnmane
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            System.out.println("100000000000");
            String query = "SELECT r.Giorno, r.Ora FROM Corso as c JOIN Ripetizione as r on (c.IDCorso=r.IDCorso) JOIN Docente as d on (r.emailDocente=d.emailDocente) WHERE (SELECT IDCorso FROM Corso WHERE NomeCorso = '" + subjectSplit[1] + "') = r.IDCorso AND r.emailDocente=(SELECT emailDocente FROM Docente WHERE Nome='" + TeacherSplit[1] + "' AND Cognome= '" + TeacherSplit[2] + "');";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            if (!rs.isBeforeFirst()) //resultSet vuoto
                return res;
            else {
                while(rs.next()) {
                    res.add(new Tutoring(rs.getString("Giorno"), rs.getString("Ora")));
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

    public boolean insertBooking(String subject, String teacher, String day, String hour, String emailUser) {
        String query, iDCourse, emailTeacher;
        ResultSet rs;
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();

            //obtain the IDCourse from the name of the course, there is only one result
            query = "SELECT IDCorso FROM Corso WHERE NomeCorso='" + (subject.split(" "))[1] + "';";
            System.out.println(query);
            rs = st.executeQuery(query);
            if (!rs.next()) //resultSet vuoto
                return false;
            else
                iDCourse = rs.getString("IDCorso");

            //obtain the email of the teacher from the name and surname of himself, there is only one email for each teacher
            System.out.println(teacher);
            System.out.println((teacher.split(" "))[0]);
            System.out.println((teacher.split(" "))[1]);
            query = "SELECT emailDocente FROM Docente WHERE Nome='" + (teacher.split(" "))[1] + "' AND Cognome='" + (teacher.split(" "))[2] + "';";
            System.out.println(query);
            rs = st.executeQuery(query);
            if (!rs.next()) //resultSet vuoto
                return false;
            else
                emailTeacher = rs.getString("emailDocente");

            //insert the dates from the database, in Ripetizioni Table
            query = "INSERT INTO Ripetizione (Giorno, Ora, emailDocente, emailStudente, IDCorso) VALUES ('" + day + "', '" + hour + "', '" + emailTeacher + "', '" + emailUser + "', '" + iDCourse + "');";
            System.out.println(query);
            st.executeUpdate(query);

            //insert the dates from the database, in Storico Table
            query = "INSERT INTO Storico (Data, Ora, emailDocente, emailStudente, IDCorso, Stato) VALUES ('" + day + "', '" + hour + "', '" + emailTeacher + "', '" + emailUser + "', '" + iDCourse + "', 0);";
            System.out.println(query);
            st.executeUpdate(query);

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
        System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
        return true;
    }

    public ArrayList<String> getStudentSubject (String email) {
        ArrayList<String> res = new ArrayList<>();
        String query;
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "SELECT c.NomeCorso FROM Corso as c JOIN Ripetizione as r on (c.IDCorso=r.IDCorso) WHERE r.emailStudente = '" + email + "';";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            if (!rs.isBeforeFirst()) //resultSet vuoto
                return res;
            else {
                while(rs.next()) {
                    System.out.println(rs.getString("NomeCorso"));
                    res.add(rs.getString("NomeCorso"));
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

    public ArrayList<StudentTutoring> getStudentTutoring (String email) {
        ArrayList<StudentTutoring> res = new ArrayList<>();
        String query;
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "SELECT c.NomeCorso, d.Nome, d.Cognome, r.Giorno, r.Ora FROM Corso as c JOIN Ripetizione as r on (c.IDCorso=r.IDCorso) JOIN Docente as d on (r.emailDocente=d.emailDocente) WHERE r.emailStudente = '" + email + "';";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            if (!rs.isBeforeFirst()) //resultSet vuoto
                return res;
            else {
                while(rs.next()) {
                    System.out.println(rs.getString("Nome"));
                    res.add(new StudentTutoring(rs.getString("Giorno"), rs.getString("Ora"), rs.getString("NomeCorso"), rs.getString("Nome") + " " + rs.getString("Cognome")));
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

    public boolean confirmTutoring (String emailUtente, String day, String hour, String teacher, String subject) {
        String query, idCorso;
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "SELECT IDCorso FROM Corso WHERE NomeCorso='" + subject + "';";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            if (!rs.next()) //resultSet vuoto
                return false;
            else idCorso = rs.getString("IDCorso");

            System.out.println(idCorso);
            System.out.println((teacher.split(" "))[0]);
            System.out.println((teacher.split(" "))[1]);
            query = "DELETE FROM Ripetizione WHERE Giorno='" + day + "' AND Ora='" + hour + "' AND emailDocente=(SELECT emailDocente FROM Docente WHERE Nome='" + (teacher.split(" "))[0] + "' AND Cognome='" + (teacher.split(" "))[1] + "') AND emailStudente='" + emailUtente + "' AND IDCorso=" + idCorso + ";";
            System.out.println(query);
            st.executeUpdate(query);

            query = "UPDATE Storico SET Stato= 1 WHERE Data='" + day + "' AND Ora='" + hour + "' AND emailDocente=(SELECT emailDocente FROM Docente WHERE Nome='" + (teacher.split(" "))[0] + "' AND Cognome='" + (teacher.split(" "))[1] + "') AND emailStudente='" + emailUtente + "' AND IDCorso=" + idCorso + ";";
            System.out.println(query);
            st.executeUpdate(query);

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
        return true;
    }

    public boolean deleteTutoring (String emailUtente, String day, String hour, String teacher, String subject) {
        String query, idCorso;
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "SELECT IDCorso FROM Corso WHERE NomeCorso='" + subject + "';";
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            if (!rs.next()) //resultSet vuoto
                return false;
            else idCorso = rs.getString("IDCorso");

            System.out.println(idCorso);
            System.out.println((teacher.split(" "))[0]);
            System.out.println((teacher.split(" "))[1]);
            query = "DELETE FROM Ripetizione WHERE Giorno='" + day + "' AND Ora='" + hour + "' AND emailDocente=(SELECT emailDocente FROM Docente WHERE Nome='" + (teacher.split(" "))[0] + "' AND Cognome='" + (teacher.split(" "))[1] + "') AND emailStudente='" + emailUtente + "' AND IDCorso=" + idCorso + ";";
            System.out.println(query);
            st.executeUpdate(query);

            query = "UPDATE Storico SET Stato= 2 WHERE Data='" + day + "' AND Ora='" + hour + "' AND emailDocente=(SELECT emailDocente FROM Docente WHERE Nome='" + (teacher.split(" "))[0] + "' AND Cognome='" + (teacher.split(" "))[1] + "') AND emailStudente='" + emailUtente + "' AND IDCorso=" + idCorso + ";";
            System.out.println(query);
            st.executeUpdate(query);

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
        return true;
    }

}
