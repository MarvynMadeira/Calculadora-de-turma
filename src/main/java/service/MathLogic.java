public class MathLogic {
    public double calcularMedia(double m2Sala, int numAlunos){
        if(numAlunos<=0){
            //Lança um erro ou retorna 0 para indicar cálculo impossível
            throw new IllegalArgumentException("Número de alunos deve ser acima de 0.");
        }
        return m2Sala / numAlunos;
    }
}