package DAO;

import java.sql.*;
import java.util.ArrayList;
import org.apache.commons.codec.digest.DigestUtils;

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

    public boolean insertStudent(String email, String password, int role) {
        Connection conn1 = null;
        boolean res = false;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");

            Statement st = conn1.createStatement();
            password = encryptSHA2(password);
            String query = "INSERT INTO Utente (emailUtente, password, ruolo) VALUES ('"+email+"', '"+password+"', '"+role+"');";
            st.executeUpdate(query);
            res = true;

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

    public int searchUser(String email, String password) {
        Connection conn1 = null;
        int result = -1;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");

            Statement st = conn1.createStatement();
            password = encryptSHA2(password);
            String query = "SELECT * FROM Utente WHERE emailUtente='"+ email +"' AND password='"+ password +"';";
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

    public ArrayList<String> getTeachers (String subject, String email) {
        ArrayList<String> res = new ArrayList<>();
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            String query = "SELECT IDCorso FROM Corso WHERE NomeCorso ='"+subject+"';";
            ResultSet rs = st.executeQuery(query);
            if (!rs.next()) //resultSet vuoto
                return res;
            else {
                query = "SELECT d.nome, d.cognome, d.emailDocente FROM Insegnamento as i JOIN Docente as d WHERE i.IDCorso = " + rs.getInt("IDCorso")+ " AND i.emailDocente = d.emailDocente;";
                rs = st.executeQuery(query);
                if (!rs.isBeforeFirst()) //resultSet vuoto
                    return res;
                else {
                    if (email.equals("true")) {
                        while(rs.next()) {
                            res.add(rs.getString("nome") + " " + rs.getString("cognome") + " " + rs.getString("emailDocente"));
                        }
                    } else if (email.equals("false")) {
                        while(rs.next()) {
                            res.add(rs.getString("nome") + " " + rs.getString("cognome"));
                        }
                    }

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

    public ArrayList<Tutoring> getTutoringsList (String subject, String teacher) {
        ArrayList<Tutoring> res = new ArrayList<>();
        String[] subjectSplit = subject.split(" "); //use only [1]
        String[] TeacherSplit = teacher.split(" "); //use [1] for name, [2] for surnmane
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            String query = "SELECT r.Giorno, r.Ora FROM Corso as c JOIN Ripetizione as r on (c.IDCorso=r.IDCorso) JOIN Docente as d on (r.emailDocente=d.emailDocente) WHERE (SELECT IDCorso FROM Corso WHERE NomeCorso = '" + subjectSplit[1] + "') = r.IDCorso AND r.emailDocente=(SELECT emailDocente FROM Docente WHERE Nome='" + TeacherSplit[1] + "' AND Cognome= '" + TeacherSplit[2] + "');";
            ResultSet rs = st.executeQuery(query);
            if (!rs.isBeforeFirst()) //resultSet vuoto
                return res;
            else {
                while(rs.next()) {
                    res.add(new Tutoring(rs.getString("Giorno"), rs.getString("Ora")));
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
            rs = st.executeQuery(query);
            if (!rs.next()) //resultSet vuoto
                return false;
            else
                iDCourse = rs.getString("IDCorso");

            //obtain the email of the teacher from the name and surname of himself, there is only one email for each teacher
            query = "SELECT emailDocente FROM Docente WHERE Nome='" + (teacher.split(" "))[1] + "' AND Cognome='" + (teacher.split(" "))[2] + "';";
            rs = st.executeQuery(query);
            if (!rs.next()) //resultSet vuoto
                return false;
            else
                emailTeacher = rs.getString("emailDocente");

            //insert the dates from the database, in Ripetizioni Table
            query = "INSERT INTO Ripetizione (Giorno, Ora, emailDocente, emailStudente, IDCorso) VALUES ('" + day + "', '" + hour + "', '" + emailTeacher + "', '" + emailUser + "', '" + iDCourse + "');";
            st.executeUpdate(query);

            //insert the dates from the database, in Storico Table
            query = "INSERT INTO Storico (Data, Ora, emailDocente, emailStudente, IDCorso, Stato) VALUES ('" + day + "', '" + hour + "', '" + emailTeacher + "', '" + emailUser + "', '" + iDCourse + "', 0);";
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

    public ArrayList<StudentTutoring> getStudentTutoring (String email, int role ) {
        ArrayList<StudentTutoring> res = new ArrayList<>();
        String query = "";
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            if (role == 1)
                query = "SELECT c.NomeCorso, d.Nome, d.Cognome, r.Giorno, r.Ora FROM Corso as c JOIN Ripetizione as r on (c.IDCorso=r.IDCorso) JOIN Docente as d on (r.emailDocente=d.emailDocente) WHERE r.emailStudente = '" + email + "';";
            else if (role == 2)
                query = "SELECT c.NomeCorso, d.Nome, d.Cognome, r.Giorno, r.Ora, r.emailStudente FROM Corso as c JOIN Ripetizione as r on (c.IDCorso=r.IDCorso) JOIN Docente as d on (r.emailDocente=d.emailDocente);";
            ResultSet rs = st.executeQuery(query);
            if (!rs.isBeforeFirst()) //resultSet vuoto
                return res;
            else {
                if (role == 1) {
                    while(rs.next()) {
                        res.add(new StudentTutoring(rs.getString("Giorno"), rs.getString("Ora"), rs.getString("NomeCorso"), rs.getString("Nome") + " " + rs.getString("Cognome")));
                    }
                } else if (role == 2) {
                    while(rs.next()) {
                        res.add(new StudentTutoring(rs.getString("Giorno"), rs.getString("Ora"), rs.getString("NomeCorso"), rs.getString("Nome") + " " + rs.getString("Cognome"), rs.getString("emailStudente")));
                    }
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

    public boolean confirmTutoring (String emailUtente, String day, String hour, String teacher, String subject) {
        String query, idCorso;
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "SELECT IDCorso FROM Corso WHERE NomeCorso='" + subject + "';";
            ResultSet rs = st.executeQuery(query);
            if (!rs.next()) //resultSet vuoto
                return false;
            else idCorso = rs.getString("IDCorso");

            query = "DELETE FROM Ripetizione WHERE Giorno='" + day + "' AND Ora='" + hour + "' AND emailDocente=(SELECT emailDocente FROM Docente WHERE Nome='" + (teacher.split(" "))[0] + "' AND Cognome='" + (teacher.split(" "))[1] + "') AND emailStudente='" + emailUtente + "' AND IDCorso=" + idCorso + ";";
            st.executeUpdate(query);

            query = "UPDATE Storico SET Stato= 1 WHERE Data='" + day + "' AND Ora='" + hour + "' AND emailDocente=(SELECT emailDocente FROM Docente WHERE Nome='" + (teacher.split(" "))[0] + "' AND Cognome='" + (teacher.split(" "))[1] + "') AND emailStudente='" + emailUtente + "' AND IDCorso=" + idCorso + ";";
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
        return true;
    }

    public boolean deleteTutoring (String emailUtente, String day, String hour, String teacher, String subject, int role) {
        String query, idCorso;
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "SELECT IDCorso FROM Corso WHERE NomeCorso='" + subject + "';";
            ResultSet rs = st.executeQuery(query);
            if (!rs.next()) //resultSet vuoto
                return false;
            else idCorso = rs.getString("IDCorso");

            query = "DELETE FROM Ripetizione WHERE Giorno='" + day + "' AND Ora='" + hour + "' AND emailDocente=(SELECT emailDocente FROM Docente WHERE Nome='" + (teacher.split(" "))[0] + "' AND Cognome='" + (teacher.split(" "))[1] + "') AND emailStudente='" + emailUtente + "' AND IDCorso=" + idCorso + ";";
            st.executeUpdate(query);

            query = "UPDATE Storico SET Stato= 2 WHERE Data='" + day + "' AND Ora='" + hour + "' AND emailDocente=(SELECT emailDocente FROM Docente WHERE Nome='" + (teacher.split(" "))[0] + "' AND Cognome='" + (teacher.split(" "))[1] + "') AND emailStudente='" + emailUtente + "' AND IDCorso=" + idCorso + ";";
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
        return true;
    }

    public ArrayList<StudentTutoring> getHistory (int role, String email) {
        ArrayList<StudentTutoring> res = new ArrayList<>();
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            String query = "";
            if (role == 1) { //student
                query = "SELECT c.NomeCorso, d.Nome, d.Cognome, s.Data, s.Ora, s.Stato FROM Storico as s JOIN Corso as c on (s.IDCorso=c.IDCorso) JOIN Docente as d on (s.EmailDocente=d.emailDocente) WHERE s.emailStudente='" + email + "';";
            } else if (role == 2) { //admin
                query = "SELECT c.NomeCorso, d.Nome, d.Cognome, s.emailStudente, s.Data, s.Ora, s.Stato FROM Storico as s JOIN Corso as c on (s.IDCorso=c.IDCorso) JOIN Docente as d on (s.EmailDocente=d.emailDocente);";
            }
            ResultSet rs = st.executeQuery(query);
            if (!rs.isBeforeFirst()) //resultSet vuoto
                return res;
            else {
                if (role == 1) {
                    while(rs.next())
                        res.add(new StudentTutoring(rs.getString("Data"), rs.getString("Ora"), rs.getString("NomeCorso"), rs.getString("Nome") + " " + rs.getString("Cognome"), rs.getInt("Stato")));
                } else if (role == 2) {
                    while(rs.next())
                        res.add(new StudentTutoring(rs.getString("Data"), rs.getString("Ora"), rs.getString("NomeCorso"), rs.getString("Nome") + " " + rs.getString("Cognome"), rs.getString("emailStudente"), rs.getInt("Stato")));
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

    public ArrayList<StudentTutoring> getRestrictedHistory (int status) {
        ArrayList<StudentTutoring> res = new ArrayList<>();
        Connection conn1 = null;
        String query = "";
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "SELECT c.NomeCorso, d.Nome, d.Cognome, s.emailStudente, s.Data, s.Ora, s.Stato FROM Storico as s JOIN Corso as c on (s.IDCorso=c.IDCorso) JOIN Docente as d on (s.EmailDocente=d.emailDocente) WHERE s.Stato=" + status + ";";
            ResultSet rs = st.executeQuery(query);
            if (!rs.isBeforeFirst()) //resultSet vuoto
                return res;
            else {
                while(rs.next())
                    res.add(new StudentTutoring(rs.getString("Data"), rs.getString("Ora"), rs.getString("NomeCorso"), rs.getString("Nome") + " " + rs.getString("Cognome"), rs.getString("emailStudente"), rs.getInt("Stato")));
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

    public boolean insertTeacher (String name, String surname, String email) {
        Connection conn1 = null;
        String query = "";
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "SELECT * FROM Docente WHERE Nome='" + name + "' AND Cognome='" + surname + "' AND emailDocente='" + email + "';";
            ResultSet rs = st.executeQuery(query);
            if (!rs.next()) { //empty resultSet, the teacher can be inserted into the database
                query = "INSERT INTO Docente (emailDocente, Nome, Cognome) VALUES ('" + email + "', '" + name + "', '" + surname + "');";
                st.executeUpdate(query);
            } else //the teacher is already in the database
                return false;

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
        return true;
    }

    public void deleteTeacher (String name, String surname, String email) {
        Connection conn1 = null;
        String query = "";
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "DELETE FROM Docente WHERE Nome='" + name + "' AND Cognome='" + surname + "' AND emailDocente='" + email + "';";
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

    public boolean insertCourse (String subject) {
        Connection conn1 = null;
        String query = "";
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "INSERT INTO Corso (NomeCorso) VALUES ('" + subject + "');";
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
        return true;
    }

    public ArrayList<String> getAllTeachers (boolean email) {
        ArrayList<String> res = new ArrayList<>();
        Connection conn1 = null;
        String query = "";
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "SELECT Nome, Cognome, emailDocente FROM Docente";
            ResultSet rs = st.executeQuery(query);
            if (!rs.isBeforeFirst()) //resultSet vuoto
                return res;
            else {
                if (email) {
                    while(rs.next())
                        res.add(rs.getString("Nome") + " " + rs.getString("Cognome") + " " + rs.getString("emailDocente"));
                } else {
                    while(rs.next())
                        res.add(rs.getString("Nome") + " " + rs.getString("Cognome"));
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

    public boolean insertAssociation (String teacher, String subject) {
        Connection conn1 = null;
        String query = "";
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "SELECT * FROM Insegnamento WHERE IDCorso=(SELECT IDCorso FROM Corso WHERE NomeCorso='" + subject + "') AND emailDocente=(SELECT emailDocente FROM Docente WHERE Nome='" + (teacher.split(" "))[0] + "' AND Cognome='" + (teacher.split(" "))[1] + "');";
            ResultSet rs = st.executeQuery(query);
            if (!rs.next()) { //resultSet vuoto
                int idCorso; String emailDocente;
                query = "SELECT IDCorso FROM Corso WHERE NomeCorso='" + subject + "';";
                rs = st.executeQuery(query);
                rs.next();
                idCorso = rs.getInt("IDCorso");

                query = "SELECT emailDocente FROM Docente WHERE Nome='" + (teacher.split(" "))[0] + "' AND Cognome='" + (teacher.split(" "))[1] + "';";
                rs = st.executeQuery(query);
                rs.next();
                emailDocente = rs.getString("emailDocente");

                query = "INSERT INTO Insegnamento (IDCorso, emailDocente) VALUES (" + idCorso + ", '" + emailDocente + "')";
                st.executeUpdate(query);
            } else
                return false;
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
        return true;
    }

    public void deleteAssociation (String email, String subject) {
        Connection conn1 = null;
        String query = "";
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "DELETE FROM Insegnamento WHERE emailDocente='" + email + "' AND IDCorso=(SELECT IDCorso FROM Corso WHERE NomeCorso='" + subject + "');";
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

    public void deleteCourse (String subject) {
        Connection conn1 = null;
        String query = "";
        try {
            conn1 = DriverManager.getConnection(url, user, pwd);
            if (conn1 != null)
                System.out.println("Connected to the database PiattaformaRipetizioni");
            Statement st = conn1.createStatement();
            query = "DELETE FROM Corso WHERE NomeCorso='" + subject + "';";
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

    public static String encryptSHA2(String plaintext){
        String key = DigestUtils.sha256Hex(plaintext).toUpperCase();
        return key;
    }

}
