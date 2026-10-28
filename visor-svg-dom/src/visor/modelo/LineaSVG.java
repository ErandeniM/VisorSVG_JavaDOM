package visor.modelo;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public final class LineaSVG extends FiguraSVG {
    private final double x1, y1, x2, y2;

    public LineaSVG(double x1, double y1, double x2, double y2, EstiloSVG estilo) {
        super(estilo);
        this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
    }

    @Override public void dibujar(Graphics2D g) {
        // SVG line no admite relleno, solo trazo.
        if (estilo.getContorno() != null && estilo.getGrosorContorno() > 0) {
            g.setColor(estilo.getContorno());
            g.setStroke(new java.awt.BasicStroke((float) estilo.getGrosorContorno()));
            g.draw(new Line2D.Double(x1, y1, x2, y2));
        }
    }
}
