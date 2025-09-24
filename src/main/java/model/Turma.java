package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Turma {
    private final StringProperty escola;
    private final StringProperty sala;
    private final StringProperty turma;
    private final StringProperty num_de_alunos;
    private final StringProperty metro_quadrado_sala;
    private final StringProperty inferior_ou_igual_a;
    private final StringProperty superior_ou_igual_a;

    public Turma(String escola, String sala, String turma, int num_de_alunos, double metro_quadrado_sala, String inferior_ou_igual_a, String superior_ou_igual_a) {
        this.escola = new SimpleStringProperty(escola);
        this.sala = new SimpleStringProperty(sala);
        this.turma = new SimpleStringProperty(turma);
        this.num_de_alunos = new SimpleStringProperty(String.valueOf(num_de_alunos));
        this.metro_quadrado_sala = new SimpleStringProperty(String.format("%.2f", metro_quadrado_sala));
        this.inferior_ou_igual_a = new SimpleStringProperty(String.format("%.2f", inferior_ou_igual_a));
        this.superior_ou_igual_a = new SimpleStringProperty(String.format("%.2f", superior_ou_igual_a));
    }

    public String getEscola() { return escola.get(); }
    public String getSala() { return sala.get(); }
    public String getTurma() { return turma.get(); }
    public String getNum_de_alunos() { return num_de_alunos.get(); }
    public String getMetro_quadrado_sala() { return metro_quadrado_sala.get(); }
    public String getInferior_ou_igual_a() { return inferior_ou_igual_a.get(); }
    public String getSuperior_ou_igual_a() { return superior_ou_igual_a.get();}

    public StringProperty escolaProperty() { return escola; }
    public StringProperty salaProperty() { return sala; }
    public StringProperty turmaProperty() { return turma; }
    public StringProperty num_de_alunosProperty() { return num_de_alunos; }
    public StringProperty metro_quadrado_salaProperty() { return metro_quadrado_sala; }
    public StringProperty inferior_ou_igual_aProperty() { return superior_ou_igual_a; }
    public StringProperty superior_ou_igual_aProperty() { return superior_ou_igual_a; }
}
