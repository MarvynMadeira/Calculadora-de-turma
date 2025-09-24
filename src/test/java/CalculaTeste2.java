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
}