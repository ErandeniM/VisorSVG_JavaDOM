package visor.modelo;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.GlyphVector;

public final class TextoSVG extends FiguraSVG {
    private final double x, y;
    private final String contenido;

    public TextoSVG(double x, double y, String contenido, EstiloSVG estilo) {
        super(estilo);
        this.x = x; this.y = y; this.contenido = contenido;
    }

    @Override public void dibujar(Graphics2D g) {
        // El valor y es la linea base (no la esquina superior) del texto SVG.
        Font fuente = new Font(estilo.getFamiliaFuente(), Font.PLAIN, 1)
                .deriveFont((float) estilo.getTamanoFuente());
        g.setFont(fuente);
        if (estilo.getContorno() == null || estilo.getGrosorContorno() == 0) {
            if (estilo.getRelleno() != null) {
                g.setColor(estilo.getRelleno());
                g.drawString(contenido, (float) x, (float) y);
            }
        } else {
            GlyphVector glifos = fuente.createGlyphVector(g.getFontRenderContext(), contenido);
            Shape silueta = glifos.getOutline((float) x, (float) y);
            if (estilo.getRelleno() != null) {
                g.setColor(estilo.getRelleno());
                g.fill(silueta);
            }
            g.setColor(estilo.getContorno());
            g.setStroke(new BasicStroke((float) estilo.getGrosorContorno()));
            g.draw(silueta);
        }
    }
}
