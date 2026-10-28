package visor.modelo;

import java.awt.Color;

/** Estilo interpretado directamente desde los atributos del elemento SVG. */
public final class EstiloSVG {
    private final Color relleno;
    private final Color contorno;
    private final double grosorContorno;
    private final String familiaFuente;
    private final double tamanoFuente;

    public EstiloSVG(Color relleno, Color contorno, double grosorContorno,
                     String familiaFuente, double tamanoFuente) {
        this.relleno = relleno;
        this.contorno = contorno;
        this.grosorContorno = grosorContorno;
        this.familiaFuente = familiaFuente;
        this.tamanoFuente = tamanoFuente;
    }

    public Color getRelleno() { return relleno; }
    public Color getContorno() { return contorno; }
    public double getGrosorContorno() { return grosorContorno; }
    public String getFamiliaFuente() { return familiaFuente; }
    public double getTamanoFuente() { return tamanoFuente; }
}
