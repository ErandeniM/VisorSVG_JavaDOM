package visor.pruebas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import visor.modelo.FiguraSVG;
import visor.parser.DocumentoSVG;
import visor.parser.ProcesadorSVG;
import visor.ui.PanelDibujo;

/** Prueba sin dependencias externas: carga SVG y exporta render Java 2D. */
public final class PruebasSVG {
    private PruebasSVG() { }

    public static void main(String[] args) throws Exception {
        ProcesadorSVG procesador = new ProcesadorSVG();
        DocumentoSVG principal = procesador.cargar(new File("test_svg_dom.svg"));
        Map<String, Integer> cantidades = new LinkedHashMap<>();
        for (FiguraSVG f : principal.getFiguras()) {
            cantidades.merge(f.getClass().getSimpleName(), 1, Integer::sum);
        }
        String[] tipos = {"LineaSVG", "CirculoSVG", "PolilineaSVG", "RectanguloSVG",
                          "ElipseSVG", "TextoSVG"};
        for (String tipo : tipos) {
            if (cantidades.getOrDefault(tipo, 0) < 2) {
                throw new AssertionError("Se esperaban al menos dos figuras: " + tipo);
            }
        }
        System.out.println("PASS: seis tipos con al menos dos instancias: " + cantidades);

        DocumentoSVG otro = procesador.cargar(new File("test_svg_alternativo.svg"));
        if (otro.getFiguras().isEmpty()) throw new AssertionError("El segundo SVG esta vacio.");
        System.out.println("PASS: se cargo un segundo archivo independiente.");

        esperarError(procesador, "test_atributo_invalido.svg");
        esperarError(procesador, "test_xml_malformado.svg");
        esperarError(procesador, "test_entidad_externa.svg");
        System.out.println("PASS: los errores invalidos, XML mal formado y DOCTYPE fueron rechazados.");

        int ancho = (int) Math.ceil(principal.getAncho());
        int alto = (int) Math.ceil(principal.getAlto());
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        PanelDibujo panel = new PanelDibujo();
        panel.mostrar(principal);
        panel.setSize(ancho, alto);
        Graphics2D g = imagen.createGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, ancho, alto);
            panel.paint(g);
        } finally { g.dispose(); }
        File destino = new File("evidencias/resultado_java2d.png");
        File carpeta = destino.getParentFile();
        if (!carpeta.exists() && !carpeta.mkdirs()) {
            throw new java.io.IOException("No se pudo crear carpeta evidencias.");
        }
        ImageIO.write(imagen, "png", destino);
        System.out.println("PASS: render Java 2D guardado en " + destino.getPath());
    }

    private static void esperarError(ProcesadorSVG procesador, String ruta) throws Exception {
        try {
            procesador.cargar(new File(ruta));
            throw new AssertionError("Debio rechazarse: " + ruta);
        } catch (javax.xml.parsers.ParserConfigurationException | java.io.IOException |
                 org.xml.sax.SAXException | IllegalArgumentException esperado) {
            System.out.println("PASS: error detectado en " + ruta + ": " + esperado.getMessage());
        }
    }
}
