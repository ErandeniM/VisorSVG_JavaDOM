package visor.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import visor.modelo.FiguraSVG;

/** Resultado del procesamiento del XML; no contiene elementos DOM. */
public final class DocumentoSVG {
    private final double ancho, alto;
    private final List<FiguraSVG> figuras;

    public DocumentoSVG(double ancho, double alto, List<FiguraSVG> figuras) {
        this.ancho = ancho;
        this.alto = alto;
        this.figuras = Collections.unmodifiableList(new ArrayList<>(figuras));
    }

    public double getAncho() { return ancho; }
    public double getAlto() { return alto; }
    public List<FiguraSVG> getFiguras() { return figuras; }
}
