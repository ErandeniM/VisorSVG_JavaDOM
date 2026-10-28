package visor.modelo;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public final class CirculoSVG extends FiguraSVG {
    private final double cx, cy, radio;

    public CirculoSVG(double cx, double cy, double radio, EstiloSVG estilo) {
        super(estilo);
        this.cx = cx; this.cy = cy; this.radio = radio;
    }

    @Override public void dibujar(Graphics2D g) {
        pintarForma(g, new Ellipse2D.Double(cx - radio, cy - radio, 2 * radio, 2 * radio));
    }
}
