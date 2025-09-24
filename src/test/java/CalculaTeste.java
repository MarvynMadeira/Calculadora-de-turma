import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import service.CalculaTurmas;

class CalculadoraTest {
    @Test
    void testCalcularMedia(){
        CalculaTurmas service = new CalculaTurmas();
        // Teste: Uma sala de 50m² com 25 alunos deve resultar em 2m² por aluno.

        assertEquals(2.0, service.calcularMedia(50.0, 25));
    }

    @Test
    void testCalcularMedia2() {
        CalculaTurmas service = new CalculaTurmas();
        // Teste 2: Deve lançar um erro se os alunos for igual a Zero.

        assertThrows(IllegalArgumentException.class, () -> {
            service.calcularMedia(50.0, 0);
        });
    }
}