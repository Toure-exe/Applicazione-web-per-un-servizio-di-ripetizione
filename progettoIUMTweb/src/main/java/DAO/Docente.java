package DAO;

public class Docente {
    private String nome;
    private String cognome;
    private String emailDocente;
    boolean occupato[][];

    public Docente(String nome, String cognome, String emailDocente) {
        this.nome = nome;
        this.cognome = cognome;
        this.emailDocente = emailDocente;
        this.occupato = new boolean[5][4];
        for(int i=0; i<5; i++) {
            for(int j=0; j<4; j++) {
                occupato[i][j] = false;
            }
        }
    }


}
