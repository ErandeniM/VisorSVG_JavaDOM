package visor.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import visor.modelo.*;

/** Interpreta SVG con DOM; nunca dibuja ni depende de componentes Swing. */
public final class ProcesadorSVG {
    private static final String NS_SVG = "http://www.w3.org/2000/svg";

    public DocumentoSVG cargar(File archivo)
            throws IOException, SAXException, ParserConfigurationException {
        if (archivo == null || !archivo.isFile() || !archivo.canRead()) {
            throw new IOException("El archivo no existe o no se puede leer.");
        }
        if (!archivo.getName().toLowerCase(java.util.Locale.ROOT).endsWith(".svg")) {
            throw new IOException("Seleccione un archivo con extension .svg.");
        }

        DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
        fabrica.setNamespaceAware(true);
        fabrica.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        fabrica.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        fabrica.setFeature("http://xml.org/sax/features/external-general-entities", false);
        fabrica.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        fabrica.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        fabrica.setXIncludeAware(false);
        fabrica.setExpandEntityReferences(false);
        fabrica.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        fabrica.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        DocumentBuilder constructor = fabrica.newDocumentBuilder();
        constructor.setErrorHandler(new DefaultHandler() {
            @Override public void error(SAXParseException e) throws SAXException { throw e; }
            @Override public void fatalError(SAXParseException e) throws SAXException { throw e; }
        });
        Document documento = constructor.parse(archivo);
        Element raiz = documento.getDocumentElement();
        if (raiz == null || !"svg".equals(nombreLocal(raiz)) || !esSvg(raiz)) {
            throw new SAXException("La raiz del documento debe ser <svg>.");
        }
        double ancho = AtributosSVG.numero(raiz, "width", 800);
        double alto = AtributosSVG.numero(raiz, "height", 600);
        if (ancho <= 0 || alto <= 0 || ancho > 10000 || alto > 10000) {
            throw new SAXException("El ancho y alto SVG deben estar entre 0 y 10000 (sin unidades).");
        }
        List<FiguraSVG> figuras = new ArrayList<>();
        recorrerHijos(raiz, figuras);
        return new DocumentoSVG(ancho, alto, figuras);
    }

    private void recorrerHijos(Element padre, List<FiguraSVG> figuras) throws SAXException {
        NodeList hijos = padre.getChildNodes();
        for (int i = 0; i < hijos.getLength(); i++) {
            Node nodo = hijos.item(i);
            if (nodo.getNodeType() != Node.ELEMENT_NODE) continue;
            Element elemento = (Element) nodo;
            if (!esSvg(elemento)) continue;
            String tipo = nombreLocal(elemento);
            if ("g".equals(tipo) || "svg".equals(tipo)) {
                recorrerHijos(elemento, figuras); // grupos anidados, en orden del XML
                continue;
            }
            try {
                FiguraSVG figura = construirFigura(tipo, elemento);
                if (figura != null) figuras.add(figura);
            } catch (IllegalArgumentException ex) {
                throw new SAXException("Error en <" + tipo + ">: " + ex.getMessage(), ex);
            }
        }
    }

    private FiguraSVG construirFigura(String tipo, Element e) {
        switch (tipo) {
            case "line":
                return new LineaSVG(AtributosSVG.numero(e, "x1", 0),
                        AtributosSVG.numero(e, "y1", 0), AtributosSVG.numero(e, "x2", 0),
                        AtributosSVG.numero(e, "y2", 0), AtributosSVG.estilo(e));
            case "circle":
                return new CirculoSVG(AtributosSVG.numero(e, "cx", 0),
                        AtributosSVG.numero(e, "cy", 0), AtributosSVG.noNegativo(e, "r", 0),
                        AtributosSVG.estilo(e));
            case "polyline":
                return new PolilineaSVG(AtributosSVG.puntos(e), AtributosSVG.estilo(e));
            case "rect":
                return new RectanguloSVG(AtributosSVG.numero(e, "x", 0),
                        AtributosSVG.numero(e, "y", 0), AtributosSVG.noNegativo(e, "width", 0),
                        AtributosSVG.noNegativo(e, "height", 0), AtributosSVG.estilo(e));
            case "ellipse":
                return new ElipseSVG(AtributosSVG.numero(e, "cx", 0),
                        AtributosSVG.numero(e, "cy", 0), AtributosSVG.noNegativo(e, "rx", 0),
                        AtributosSVG.noNegativo(e, "ry", 0), AtributosSVG.estilo(e));
            case "text":
                return new TextoSVG(AtributosSVG.numero(e, "x", 0),
                        AtributosSVG.numero(e, "y", 0), e.getTextContent().trim(),
                        AtributosSVG.estilo(e));
            default:
                // Elementos SVG fuera del alcance se ignoran.
                return null;
        }
    }

    private static String nombreLocal(Element elemento) {
        String local = elemento.getLocalName();
        return local != null ? local : elemento.getTagName();
    }

    private static boolean esSvg(Element elemento) {
        String espacio = elemento.getNamespaceURI();
        return espacio == null || NS_SVG.equals(espacio);
    }
}
