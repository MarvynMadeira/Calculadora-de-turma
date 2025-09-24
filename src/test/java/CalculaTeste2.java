package service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraServiceTest {

    @Test
    void testCalcularMediaCorretamente() {
        //Cenário: Uma sala de 50m² com 25 alunos.
        //Resultado esperado: 2.0m² por aluno.

        assertEquals(2.0, CalculaTurmas.calcularMedia(50.0, 25), 0.001);

    }

    @Test
    void testCalcularComZero() {
        //Cenário: Tentar calcular com 0 alunos.
        // Resultado esperado: Lançar um erro.
        assertThrows(IllegalArgumentException.class, () -> {
            CalculaTurmas.calcularMedia(50.0, 0);
        });
    }

    @Test
    void testCalcularComNegativo() {
        //Cenário: Tentar calcular com -5 alunos.
        //Resultado esperado: lançar um erro.
        assertThrows(IllegalArgumentException.class, () -> {
            CalculaTurmas.calcularMedia(50.0, -5);
        });
    }
}