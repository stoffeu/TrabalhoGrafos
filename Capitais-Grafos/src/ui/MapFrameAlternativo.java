package ui;

import javax.swing.*;
import java.awt.*;
import model.Grafo;
import service.DadosCapitais;

public class MapFrameAlternativo extends JFrame {
    private MapPanelAlternativo mapPanel;

    public MapFrameAlternativo() {
        setTitle("Mapa Interativo de Capitais - Brasil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 700);
        setLocationRelativeTo(null);

        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Grafo grafo = new Grafo();
        DadosCapitais.inicializarConexoes(grafo);

        mapPanel = new MapPanelAlternativo(grafo);
        add(mapPanel);

        // Barra de status com tema claro
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBackground(new Color(240, 240, 240));
        JLabel statusLabel = new JLabel("Selecione duas capitais para ver o caminho mais curto");
        statusLabel.setForeground(Color.BLACK);
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MapFrameAlternativo frame = new MapFrameAlternativo();
            frame.setVisible(true);
        });
    }
}