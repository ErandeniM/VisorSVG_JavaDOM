package visor.parser;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.w3c.dom.Element;
import visor.modelo.EstiloSVG;

/** Conversiones reutilizables de cadenas XML a valores Java. */
public final class AtributosSVG {
    private static final Map<String, Color> COLORES = crearColores();

    private AtributosSVG() { }

    public static double numero(Element elemento, String nombre, double porDefecto) {
        if (!elemento.hasAttribute(nombre)) return porDefecto;
        String cadena = elemento.getAttribute(nombre).trim();
        if (cadena.isEmpty()) {
            throw new IllegalArgumentException("el atributo '" + nombre + "' esta vacio");
        }
        try {
            double valor = Double.parseDouble(cadena);
            if (!Double.isFinite(valor)) throw new NumberFormatException("no finito");
            return valor;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("'" + nombre + "' debe ser numerico y sin unidades: '" + cadena + "'");
        }
    }

    public static double noNegativo(Element el, String nombre, double porDefecto) {
        double valor = numero(el, nombre, porDefecto);
        if (valor < 0) throw new IllegalArgumentException("'" + nombre + "' no puede ser negativo");
        return valor;
    }

    public static EstiloSVG estilo(Element elemento) {
        Color relleno = color(elemento, "fill", Color.BLACK); // SVG: negro
        Color contorno = color(elemento, "stroke", null);     // SVG: none
        double grosor = noNegativo(elemento, "stroke-width", 1.0);
        String familia = elemento.hasAttribute("font-family")
                ? elemento.getAttribute("font-family").trim() : "SansSerif";
        if (familia.isEmpty()) familia = "SansSerif";
        // Este visualizador no implementa listas de fuentes ni font-weight.
        double tamano = numero(elemento, "font-size", 16.0);
        if (tamano <= 0) throw new IllegalArgumentException("'font-size' debe ser mayor que cero");
        return new EstiloSVG(relleno, contorno, grosor, familia, tamano);
    }

    public static Color color(Element elemento, String nombre, Color porDefecto) {
        if (!elemento.hasAttribute(nombre)) return porDefecto;
        String valor = elemento.getAttribute(nombre).trim().toLowerCase(Locale.ROOT);
        if (valor.equals("none")) return null;
        if (COLORES.containsKey(valor)) return COLORES.get(valor);
        if (valor.matches("#[0-9a-f]{3}")) {
            int r = Integer.parseInt(valor.substring(1, 2) + valor.substring(1, 2), 16);
            int g = Integer.parseInt(valor.substring(2, 3) + valor.substring(2, 3), 16);
            int b = Integer.parseInt(valor.substring(3, 4) + valor.substring(3, 4), 16);
            return new Color(r, g, b);
        }
        if (valor.matches("#[0-9a-f]{6}")) {
            return new Color(Integer.parseInt(valor.substring(1), 16));
        }
        throw new IllegalArgumentException("color no soportado en '" + nombre + "': '" + valor + "'");
    }

    public static List<Point2D.Double> puntos(Element elemento) {
        String texto = elemento.getAttribute("points").trim();
        if (texto.isEmpty()) throw new IllegalArgumentException("'points' necesita al menos un par de coordenadas");
        String[] valores = texto.split("[\\s,]+");
        if (valores.length % 2 != 0) {
            throw new IllegalArgumentException("'points' debe contener pares x,y");
        }
        List<Point2D.Double> resultado = new ArrayList<>();
        for (int i = 0; i < valores.length; i += 2) {
            try {
                double x = Double.parseDouble(valores[i]);
                double y = Double.parseDouble(valores[i + 1]);
                if (!Double.isFinite(x) || !Double.isFinite(y)) throw new NumberFormatException();
                resultado.add(new Point2D.Double(x, y));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("coordenada invalida en 'points': "
                        + valores[i] + "," + valores[i + 1]);
            }
        }
        return resultado;
    }

    private static Map<String, Color> crearColores() {
        Map<String, Color> c = new HashMap<>();
        c.put("black", Color.BLACK); c.put("white", Color.WHITE);
        c.put("red", Color.RED); c.put("green", new Color(0, 128, 0));
        c.put("blue", Color.BLUE); c.put("yellow", Color.YELLOW);
        c.put("orange", Color.ORANGE); c.put("purple", new Color(128, 0, 128));
        c.put("gray", Color.GRAY); c.put("grey", Color.GRAY);
        c.put("pink", new Color(255, 192, 203)); c.put("brown", new Color(165, 42, 42));
        c.put("cyan", Color.CYAN); c.put("magenta", Color.MAGENTA);
        c.put("lightblue", new Color(173, 216, 230));
        c.put("lightgreen", new Color(144, 238, 144));
        c.put("navy", new Color(0, 0, 128));
        return c;
    }
}
