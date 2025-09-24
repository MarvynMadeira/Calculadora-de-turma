package controllers;

import service.CalculaTurmas;
import model.Turma;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CalculadoraController {
    @FXML private TextField escolaField;
    @FXML private TextField salaField;
    @FXML private TextField turmaField;
    @FXML private TextField numAlunosField;
    @FXML private TextField m2SalaField;
    @FXML private TextField inferiorAField;
    @FXML private TextField superiorAField;
    @FXML private Label resultadoLabel;
    @FXML private Button aplicarButton;

    private PrincipalController principalController;
    private Stage dialogStage;
    private double mediaCalculada;

    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handleCalcular(){
        try{
            int numAlunos = Integer.parseInt(numAlunosField.getText());
            double m2Sala = Double.parseDouble(m2SalaField.getText());

            mediaCalculada = CalculaTurmas.calcularMedia(m2Sala, numAlunos);
            resultadoLabel.setText(String.format("%.2f m² por aluno", mediaCalculada));
            aplicarButton.setDisable(false);

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro de Formato", "Os campos 'N° de Alunos' e 'm² da Sala' devem ser números válidos.");
            aplicarButton.setDisable(true);

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro e Lógica", e.getMessage());
            aplicarButton.setDisable(true);
        }
    }

    @FXML
    public void handleAplicar(){
        if (isInputValid()) {
            String escola = escolaField.getText();
            String sala = salaField.getText();
            String nomeTurma = turmaField.getText();
            int numAlunos = Integer.parseInt(numAlunosField.getText());

            String inferior_ou_igual_a = "N/A";
            if (!inferiorAField.getText().isBlank()) {
                double limiteInferior = Double.parseDouble(inferiorAField.getText());
                inferior_ou_igual_a = mediaCalculada <= limiteInferior ? "Sim" : "Não";
            }

            String superior_ou_igual = "N/A";
            if (!superiorAField.getText().isBlank()) {
                double limiteSuperior = Double.parseDouble(superiorAField.getText());
                superior_ou_igual = mediaCalculada >= limiteSuperior ? "Sim" : "Não";
            }

            Turma novaTurma = new Turma(escola, sala, nomeTurma, numAlunos, mediaCalculada, inferior_ou_igual_a, superior_ou_igual);
            principalController.addTurma(novaTurma);

            dialogStage.close();

        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (escolaField.getText() == null || escolaField.getText().isBlank()) {
            errorMessage += "Nome da escola é obrigatório. \n";
        }
        // Lógica de validação corrigida
        if (salaField.getText() == null || salaField.getText().isBlank()) {
            errorMessage += "Nome da sala é obrigatório. \n";
        }
        // Lógica de validação corrigida
        if (turmaField.getText() == null || turmaField.getText().isBlank()) {
            errorMessage += "Nome da turma é obrigatório. \n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            mostrarAlerta("Campos Inválidos", errorMessage);
            return false;
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}