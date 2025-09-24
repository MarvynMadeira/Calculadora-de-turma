package controllers;

import main.Main;
import model.Turma;
import service.UpdateService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PrincipalController {

    @FXML
    private TabPane escolaTabPane;

    private final Map<String, Tab> escolaMap = new HashMap<>();
    private final Map<String, Map<String, VBox>> salasMap = new HashMap<>();
    private final Map<String, Map<String, List<Turma>>> dadosEscolas = new HashMap<>();

    @FXML
    public void handleNovaAnalise() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CalculadoraView.fxml"));
            AnchorPane page = fxmlLoader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nova Análise de Turma");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(escolaTabPane.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CalculadoraController controller = fxmlLoader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPrincipalController(this);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTurma(Turma turma) {
        String nomeEscola = turma.getEscola();
        String nomeSala = turma.getSala();

        if (!escolaMap.containsKey(nomeEscola)) {
            criarAbaEscola(nomeEscola);
        }

        if (!salasMap.get(nomeEscola).containsKey(nomeSala)) {
            criarContainerSala(nomeEscola, nomeSala);
        }

        adicionarTurmaNaSala(nomeEscola, nomeSala, turma);

        salvarDadosTurma(nomeEscola, nomeSala, turma);

        escolaTabPane.getSelectionModel().select(escolaMap.get(nomeEscola));
    }

    private void criarAbaEscola(String nomeEscola) {
        Tab novaAba = new Tab(nomeEscola);

        ScrollPane scrollPane = new ScrollPane();
        VBox containerEscola = new VBox(15);
        containerEscola.setStyle("-fx-padding: 15;");

        scrollPane.setContent(containerEscola);
        scrollPane.setFitToWidth(true);

        novaAba.setContent(scrollPane);
        escolaTabPane.getTabs().add(novaAba);

        escolaMap.put(nomeEscola, novaAba);
        salasMap.put(nomeEscola, new HashMap<>());
        dadosEscolas.put(nomeEscola, new HashMap<>());
    }

    private void criarContainerSala(String nomeEscola, String nomeSala) {
        VBox containerSala = new VBox(8);
        containerSala.setStyle(
                "-fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 5; " +
                "-fx-padding: 12; -fx-background-color: #f9f9f9; -fx-background-radius: 5;"
        );

        Label tituloSala = new Label("Sala: " + nomeSala);
        tituloSala.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        VBox containerTurmas = new VBox(5);
        containerTurmas.setStyle("-fx-padding: 10 0 0 0;");

        containerSala.getChildren().addAll(tituloSala, new Separator(), containerTurmas);

        ScrollPane scrollPane = (ScrollPane) escolaMap.get(nomeEscola).getContent();
        VBox containerEscola = (VBox) scrollPane.getContent();
        containerEscola.getChildren().add(containerSala);

        salasMap.get(nomeEscola).put(nomeSala, containerTurmas);
        dadosEscolas.get(nomeEscola).put(nomeSala, new ArrayList<>());
    }

    private void adicionarTurmaNaSala(String nomeEscola, String nomeSala, Turma turma) {
        VBox containerTurmas = salasMap.get(nomeEscola).get(nomeSala);

        HBox linhaTurma = new HBox(15);
        linhaTurma.setStyle("-fx-alignment: center-left; -fx-padding: 5;");

        Label labelTurma = new Label("Turma: " + turma.getTurma());
        Label labelAlunos = new Label("Alunos: " + turma.getNum_de_alunos());
        Label labelMedia = new Label("Média: " + turma.getMetro_quadrado_sala() + " m²");

        labelTurma.setPrefWidth(200);
        labelAlunos.setPrefWidth(150);

        linhaTurma.getChildren().addAll(labelTurma, labelAlunos, labelMedia);
        containerTurmas.getChildren().add(linhaTurma);
    }

    private void salvarDadosTurma(String nomeEscola, String nomeSala, Turma turma) {
        dadosEscolas.get(nomeEscola).get(nomeSala).add(turma);
    }

    @FXML
    private void handleExportarExcel() {
        Tab abaSelecionada = escolaTabPane.getSelectionModel().getSelectedItem();
        if (abaSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nenhuma Aba Selecionada", "Por favor, selecione uma escola para exportar.");
            return;
        }

        String nomeEscola = abaSelecionada.getText();
        Map<String, List<Turma>> salasDaEscola = dadosEscolas.get(nomeEscola);

        if (salasDaEscola == null || salasDaEscola.isEmpty()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sem Dados", "Não há dados para exportar nesta escola.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo Excel (*.xlsx)", "*.xlsx"));
        fileChooser.setInitialFileName("Relatorio_" + nomeEscola + ".xlsx");
        File arquivo = fileChooser.showSaveDialog(escolaTabPane.getScene().getWindow());

        if (arquivo != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet planilha = workbook.createSheet(nomeEscola);
                // Criação do cabeçalho
                Row cabecalho = planilha.createRow(0);
                String[] titulos = {"Escola", "Sala", "Turma", "N° Alunos", "Média (m²)", "Atende Inferior", "Atende Superior"};
                for(int i = 0; i < titulos.length; i++) {
                    cabecalho.createCell(i).setCellValue(titulos[i]);
                }

                int rowIndex = 1;
                // Itera sobre as salas e suas turmas para preencher a planilha
                for (Map.Entry<String, List<Turma>> entrySala : salasDaEscola.entrySet()) {
                    String nomeSala = entrySala.getKey();
                    for (Turma turma : entrySala.getValue()) {
                        Row linha = planilha.createRow(rowIndex++);
                        linha.createCell(0).setCellValue(turma.getEscola());
                        linha.createCell(1).setCellValue(turma.getSala());
                        linha.createCell(2).setCellValue(turma.getTurma());
                        linha.createCell(3).setCellValue(turma.getNum_de_alunos());
                        linha.createCell(4).setCellValue(turma.getMetro_quadrado_sala());
                        linha.createCell(5).setCellValue(turma.getInferior_ou_igual_a());
                        linha.createCell(6).setCellValue(turma.getSuperior_ou_igual_a());
                    }
                }

                for (int i = 0; i < titulos.length; i++) {
                    planilha.autoSizeColumn(i);
                }

                try (FileOutputStream fileOut = new FileOutputStream(arquivo)) {
                    workbook.write(fileOut);
                }
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Planilha exportada com sucesso em:\n" + arquivo.getAbsolutePath());

            } catch (IOException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro na Exportação", "Não foi possível criar a planilha.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleVerificarAtualizacoes() {
        UpdateService updateService = new UpdateService();
        Optional<UpdateService.UpdateInfo> updateInfoOpt = updateService.checkForUpdates();

        if (updateInfoOpt.isPresent()) {
            UpdateService.UpdateInfo updateInfo = updateInfoOpt.get();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Atualização Encontrada");
            alert.setHeaderText("Uma nova versão (" + updateInfo.latestVersion() + ") está disponível!");
            alert.setContentText("Deseja abrir a página de download?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    Desktop.getDesktop().browse(new URI(updateInfo.downloadUrl()));
                } catch (Exception e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir o navegador.");
                }
            }
        } else {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Nenhuma Atualização", "Você já está usando a versão mais recente.");
        }
    }

    @FXML
    private void handleFechar() {
        Stage stage = (Stage) escolaTabPane.getScene().getWindow();
        stage.fireEvent(new javafx.stage.WindowEvent(stage, javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}