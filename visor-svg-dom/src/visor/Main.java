package visor;

import javax.swing.SwingUtilities;
import visor.ui.VentanaSVG;

/** Punto de entrada del visualizador. */
public final class Main {
    private Main() { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaSVG ventana = new VentanaSVG();
            ventana.setVisible(true);
            if (args.length > 0) {
                ventana.abrirArchivo(new java.io.File(args[0]));
            }
        });
    }
}
