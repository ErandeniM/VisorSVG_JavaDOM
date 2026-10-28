package visor.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import visor.modelo.FiguraSVG;
import visor.parser.DocumentoSVG;

/** paintComponent unicamente dibuja los objetos ya interpretados. */
public final class PanelDibujo extends JPanel {
    private List<FiguraSVG> figuras = Collections.emptyList();

    public PanelDibujo() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));
    }

    public void mostrar(DocumentoSVG documento) {
        figuras = documento.getFiguras();
        setPreferredSize(new Dimension((int) Math.ceil(documento.getAncho()),
                (int) Math.ceil(documento.getAlto())));
        revalidate();
        repaint();
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            for (FiguraSVG figura : figuras) {
                // Cada figura utiliza propiedades propias sin alterar a la siguiente.
                Graphics2D graficoFigura = (Graphics2D) g2.create();
                try { figura.dibujar(graficoFigura); }
                finally { graficoFigura.dispose(); }
            }
        } finally {
            g2.dispose();
        }
    }
}
