package visor.modelo;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PolilineaSVG extends FiguraSVG {
    private final List<Point2D.Double> puntos;

    public PolilineaSVG(List<Point2D.Double> puntos, EstiloSVG estilo) {
        super(estilo);
        this.puntos = Collections.unmodifiableList(new ArrayList<>(puntos));
    }

    @Override public void dibujar(Graphics2D g) {
        if (puntos.isEmpty()) return;
        Path2D.Double camino = new Path2D.Double();
        camino.moveTo(puntos.get(0).x, puntos.get(0).y);
        for (int i = 1; i < puntos.size(); i++) {
            camino.lineTo(puntos.get(i).x, puntos.get(i).y);
        }
        // Graphics2D.fill cierra el camino implicitamente. draw lo deja abierto.
        pintarForma(g, camino);
    }
}
