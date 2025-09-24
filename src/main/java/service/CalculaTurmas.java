package service;

public class CalculaTurmas {
    public double calcularMedia(double m2Sala, int numAlunos){
        if(numAlunos<=0){
            throw new IllegalArgumentException("Número de alunos deve ser acima de 0.");
        }
        if (m2Sala<=0){
            throw new IllegalArgumentException("A área da sala não pode ser menor ou igual a 0.");
        }
        return m2Sala / numAlunos;
    }
}