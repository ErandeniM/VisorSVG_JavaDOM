package visor.modelo;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public final class RectanguloSVG extends FiguraSVG {
    private final double x, y, ancho, alto;

    public RectanguloSVG(double x, double y, double ancho, double alto, EstiloSVG estilo) {
        super(estilo);
        this.x = x; this.y = y; this.ancho = ancho; this.alto = alto;
    }

    @Override public void dibujar(Graphics2D g) {
        pintarForma(g, new Rectangle2D.Double(x, y, ancho, alto));
    }
}
