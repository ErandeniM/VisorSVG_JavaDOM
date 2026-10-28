package visor.modelo;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;

/** Abstraccion comun: toda figura conoce como dibujarse. */
public abstract class FiguraSVG {
    protected final EstiloSVG estilo;

    protected FiguraSVG(EstiloSVG estilo) {
        this.estilo = estilo;
    }

    public abstract void dibujar(Graphics2D g);

    /** Rellenar primero; dibujar el contorno despues. */
    protected final void pintarForma(Graphics2D g, Shape forma) {
        if (estilo.getRelleno() != null) {
            g.setColor(estilo.getRelleno());
            g.fill(forma);
        }
        if (estilo.getContorno() != null && estilo.getGrosorContorno() > 0) {
            g.setColor(estilo.getContorno());
            g.setStroke(new BasicStroke((float) estilo.getGrosorContorno()));
            g.draw(forma);
        }
    }
}
