package visor.pruebas;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import visor.ui.VentanaSVG;

/** Captura real de la ventana Swing; requiere escritorio grafico (o Xvfb). */
public final class CapturaVentana {
    private CapturaVentana() { }

    public static void main(String[] args) throws Exception {
        final VentanaSVG[] ventanas = new VentanaSVG[1];
        SwingUtilities.invokeAndWait(() -> {
            VentanaSVG ventana = new VentanaSVG();
            ventana.setVisible(true);
            ventana.abrirArchivo(new File("test_svg_dom.svg"));
            ventanas[0] = ventana;
        });
        try {
            Thread.sleep(1100); // Solo para la captura de evidencia, no en la aplicacion.
            Rectangle limites = ventanas[0].getBounds();
            BufferedImage captura = new Robot().createScreenCapture(limites);
            File destino = new File("evidencias/captura_ventana_swing.png");
            ImageIO.write(captura, "png", destino);
            System.out.println("Ventana real capturada en " + destino.getPath());
        } finally {
            SwingUtilities.invokeAndWait(ventanas[0]::dispose);
        }
    }
}
