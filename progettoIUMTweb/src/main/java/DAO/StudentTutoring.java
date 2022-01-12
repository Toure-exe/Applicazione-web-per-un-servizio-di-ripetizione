package DAO;

public class StudentTutoring extends Tutoring{
    private String subject;
    private String teacher;

    public StudentTutoring (String date, String hour, String subject, String teacher) {
        super(date, hour);
        this.subject = subject;
        this.teacher = teacher;
    }
}
