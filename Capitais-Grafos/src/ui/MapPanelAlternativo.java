// MapPanelAlternativo.java
package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import algorithm.AlgoritmoDijkstra;
import model.Capital;
import model.Grafo;

public class MapPanelAlternativo extends JPanel {
    private Grafo grafo;
    private AlgoritmoDijkstra algoritmo;
    private Capital selectedCapital1, selectedCapital2;
    private List<Capital> pathCapitals;
    private Map<String, Point> capitalCoordinates;
    private float animationProgress = 0f;
    private Timer animationTimer;
    private String hoveredCapital = null;
    private BufferedImage mapImage;

    public MapPanelAlternativo(Grafo grafo) {
        this.grafo = grafo;
        this.algoritmo = new AlgoritmoDijkstra(grafo);
        this.pathCapitals = new ArrayList<>();
        initializeCapitalCoordinates();
        loadMapImage(); 

        setPreferredSize(new Dimension(900, 650));
        setBackground(Color.WHITE); // Fundo branco para o painel

        // Configuração do mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleCapitalSelection(e.getPoint());
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                checkHover(e.getPoint());
            }
        });

        
        animationTimer = new Timer(30, e -> {
            animationProgress += 0.02f;
            if (animationProgress >= 1f) {
                animationProgress = 1f;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
    }

    private void loadMapImage() {
        try {
            
            mapImage = ImageIO.read(new File("src/resources/mapa_brasil.jpg"));
            mapImage = resizeImage(mapImage, 900, 650);
        } catch (IOException e) {
            System.err.println("Erro ao carregar a imagem do mapa: " + e.getMessage());
            mapImage = null;
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    private void initializeCapitalCoordinates() {
        capitalCoordinates = new HashMap<>();
        // Região Norte (Verde)
        capitalCoordinates.put("RR", new Point(300, 65));   // Boa Vista
        capitalCoordinates.put("AP", new Point(510, 80));   // Macapá
        capitalCoordinates.put("AM", new Point(300, 150));  // Manaus
        capitalCoordinates.put("PA", new Point(570, 130));  // Belém
        capitalCoordinates.put("AC", new Point(130, 260));  // Rio Branco
        capitalCoordinates.put("RO", new Point(230, 250));  // Porto Velho
        capitalCoordinates.put("TO", new Point(580, 250));  // Palmas

        // Região Nordeste (Vermelho)
        capitalCoordinates.put("MA", new Point(680, 150));  // São Luís
        capitalCoordinates.put("PI", new Point(715, 190));  // Teresina
        capitalCoordinates.put("CE", new Point(800, 170));  // Fortaleza
        capitalCoordinates.put("RN", new Point(870, 200));  // Natal
        capitalCoordinates.put("PB", new Point(850, 225));  // João Pessoa
        capitalCoordinates.put("PE", new Point(875, 240));  // Recife
        capitalCoordinates.put("AL", new Point(855, 262));  // Maceió
        capitalCoordinates.put("SE", new Point(825, 280));  // Aracaju
        capitalCoordinates.put("BA", new Point(780, 320));  // Salvador

        // Região Centro-Oeste (Laranja)
        capitalCoordinates.put("MT", new Point(410, 330));  // Cuiabá
        capitalCoordinates.put("GO", new Point(550, 375));  // Goiânia
        capitalCoordinates.put("DF", new Point(592, 352));  // Brasília
        capitalCoordinates.put("MS", new Point(440, 435));  // Campo Grande

        // Região Sudeste (Amarelo)
        capitalCoordinates.put("MG", new Point(660, 425));  // Belo Horizonte
        capitalCoordinates.put("ES", new Point(740, 430));  // Vitória
        capitalCoordinates.put("RJ", new Point(685, 470));  // Rio de Janeiro
        capitalCoordinates.put("SP", new Point(605, 475));  // São Paulo

        // Região Sul (Roxo)
        capitalCoordinates.put("PR", new Point(550, 505));  // Curitiba
        capitalCoordinates.put("SC", new Point(560, 542));  // Florianópolis
        capitalCoordinates.put("RS", new Point(515, 585));  // Porto Alegre
    }

    private void handleCapitalSelection(Point clickPoint) {
        for (Map.Entry<String, Point> entry : capitalCoordinates.entrySet()) {
            if (clickPoint.distance(entry.getValue()) < 15) {
                Capital capital = findCapitalByUF(entry.getKey());
                if (selectedCapital1 == null) {
                    selectedCapital1 = capital;
                } else if (selectedCapital2 == null && !capital.equals(selectedCapital1)) {
                    selectedCapital2 = capital;
                    calculatePath();
                } else {
                    // Reset selection
                    selectedCapital1 = capital;
                    selectedCapital2 = null;
                    pathCapitals.clear();
                    animationProgress = 0f;
                }
                repaint();
                return;
            }
        }
    }

    private void checkHover(Point mousePoint) {
        String previousHover = hoveredCapital;
        hoveredCapital = null;

        for (Map.Entry<String, Point> entry : capitalCoordinates.entrySet()) {
            if (mousePoint.distance(entry.getValue()) < 15) {
                hoveredCapital = entry.getKey();
                break;
            }
        }

        if ((hoveredCapital == null && previousHover != null) ||
                (hoveredCapital != null && !hoveredCapital.equals(previousHover))) {
            repaint();
        }
    }

    private Capital findCapitalByUF(String uf) {
        for (Capital capital : grafo.getCapitais()) {
            if (capital.getEstado().equals(uf)) {
                return capital;
            }
        }
        return null;
    }

    private void calculatePath() {
        if (selectedCapital1 != null && selectedCapital2 != null) {
            AlgoritmoDijkstra.Resultado resultado = algoritmo.calcularMenorCaminho(selectedCapital1, selectedCapital2);
            pathCapitals = resultado.getCaminho();
            animationProgress = 0f;
            animationTimer.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Configurações de renderização
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Desenhar a imagem do mapa se existir
        if (mapImage != null) {
            g2d.drawImage(mapImage, 0, 0, this);
        } else {
            // Fallback: fundo branco
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        
        if (!pathCapitals.isEmpty()) {
            drawPath(g2d);
        }

       
        drawCapitals(g2d);

       
        if (hoveredCapital != null) {
            drawTooltip(g2d, hoveredCapital);
        }
    }

    private void drawPath(Graphics2D g2d) {
        for (int i = 0; i < pathCapitals.size() - 1; i++) {
            Point p1 = capitalCoordinates.get(pathCapitals.get(i).getEstado());
            Point p2 = capitalCoordinates.get(pathCapitals.get(i + 1).getEstado());

            // Linha base mais discreta
            g2d.setColor(new Color(100, 100, 100, 50));
            g2d.setStroke(new BasicStroke(4));
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);

            // Animação do caminho
            float progress = Math.min(1f, animationProgress * (i + 1));
            int animX = (int) (p1.x + (p2.x - p1.x) * progress);
            int animY = (int) (p1.y + (p2.y - p1.y) * progress);

            // Linha animada
            g2d.setColor(new Color(255, 50, 50, 200));
            g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(p1.x, p1.y, animX, animY);

            // Ponto no final
            if (progress >= 1f) {
                g2d.setColor(new Color(255, 50, 50));
                g2d.fillOval(p2.x - 4, p2.y - 4, 8, 8);
            }
        }

        // Mostrar distância
        if (animationProgress >= 1f && selectedCapital1 != null && selectedCapital2 != null) {
            AlgoritmoDijkstra.Resultado resultado = algoritmo.calcularMenorCaminho(selectedCapital1, selectedCapital2);
            String distanceText = "Distância: " + resultado.getDistanciaTotal() + " km";

            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g2d.getFontMetrics();

            // Fundo do texto
            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.fillRoundRect(20, 20, fm.stringWidth(distanceText) + 20, fm.getHeight() + 10, 10, 10);

            // Texto
            g2d.setColor(Color.BLACK);
            g2d.drawString(distanceText, 30, 40);
        }
    }

    private void drawCapitals(Graphics2D g2d) {
        for (Map.Entry<String, Point> entry : capitalCoordinates.entrySet()) {
            Point p = entry.getValue();
            String uf = entry.getKey();

            // Círculo da capital
            if (selectedCapital1 != null && uf.equals(selectedCapital1.getEstado())) {
                g2d.setColor(new Color(50, 200, 50));
                g2d.fillOval(p.x - 8, p.y - 8, 16, 16);
            } else if (selectedCapital2 != null && uf.equals(selectedCapital2.getEstado())) {
                g2d.setColor(new Color(200, 50, 50));
                g2d.fillOval(p.x - 8, p.y - 8, 16, 16);
            } else if (uf.equals(hoveredCapital)) {
                g2d.setColor(new Color(100, 100, 255));
                g2d.fillOval(p.x - 6, p.y - 6, 12, 12);
            } else {
                g2d.setColor(new Color(70, 70, 200));
                g2d.fillOval(p.x - 5, p.y - 5, 10, 10);
            }

            // Sigla do estado
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(uf);

            // Fundo da sigla
            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.fillRect(p.x - textWidth/2 - 2, p.y + 10, textWidth + 4, fm.getHeight());

            // Texto da sigla
            g2d.setColor(Color.BLACK);
            g2d.drawString(uf, p.x - textWidth/2, p.y + 20);
        }
    }

    private void drawTooltip(Graphics2D g2d, String uf) {
        Capital capital = findCapitalByUF(uf);
        if (capital == null) return;

        Point p = capitalCoordinates.get(uf);
        String tooltipText = capital.getNome() + " (" + capital.getEstado() + ")";

        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(tooltipText) + 10;
        int height = fm.getHeight() + 6;

        // Posicionamento
        int x = p.x - width/2;
        int y = p.y - 30;

        // Ajuste para não sair da tela
        if (x < 5) x = 5;
        if (x + width > getWidth() - 5) x = getWidth() - width - 5;

        // Fundo
        g2d.setColor(new Color(255, 255, 230));
        g2d.fillRoundRect(x, y, width, height, 5, 5);

        // Borda
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawRoundRect(x, y, width, height, 5, 5);

        // Texto
        g2d.setColor(Color.BLACK);
        g2d.drawString(tooltipText, x + 5, y + fm.getAscent() + 3);
    }
}