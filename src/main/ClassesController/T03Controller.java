package ClassesController;

import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;

public class T03Controller {
    private BarChart<String, Integer> acessosChart;
    private Button visitantesButton;
    private Button moradoresButton;
    private Button prestadoresButton;

    private void handleVisitantesButtonAction() {
        // apresentar no gráfico o respectivo número de acessos por dia para "visitantes", precisa requisitar a quantidade de acessos do banco de dados que envolvem "visitante"
    }

    private void handleMoradoresButtonAction() {
        // apresentar no gráfico o respectivo número de acessos por dia para "moradores", precisa requisitar a quantidade de acessos do banco de dados que envolvem "morador"
    }

    private void handlePrestadoresButtonAction() {
        // apresentar no gráfico o respectivo número de acessos por dia para "prestadores, precisa requisitar a quantidade de acessos do banco de dados que envolvem "prestador"
    }
}