package visor.modelo;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public final class ElipseSVG extends FiguraSVG {
    private final double cx, cy, rx, ry;

    public ElipseSVG(double cx, double cy, double rx, double ry, EstiloSVG estilo) {
        super(estilo);
        this.cx = cx; this.cy = cy; this.rx = rx; this.ry = ry;
    }

    @Override public void dibujar(Graphics2D g) {
        pintarForma(g, new Ellipse2D.Double(cx - rx, cy - ry, 2 * rx, 2 * ry));
    }
}
