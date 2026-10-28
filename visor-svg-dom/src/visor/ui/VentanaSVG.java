package visor.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import visor.parser.DocumentoSVG;
import visor.parser.ProcesadorSVG;

/** Interfaz: orquesta la seleccion de archivos y actualiza el panel. */
public final class VentanaSVG extends JFrame {
    private final ProcesadorSVG procesador = new ProcesadorSVG();
    private final PanelDibujo panelDibujo = new PanelDibujo();
    private final JLabel estado = new JLabel("Seleccione un archivo SVG para visualizarlo.");
    private final JFileChooser selector = new JFileChooser();

    public VentanaSVG() {
        super("Visualizador SVG - Java DOM y Java 2D");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton botonAbrir = new JButton("Abrir SVG...");
        botonAbrir.addActionListener(e -> seleccionarArchivo());
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.add(botonAbrir);
        add(barra, BorderLayout.NORTH);
        add(new JScrollPane(panelDibujo), BorderLayout.CENTER);
        add(estado, BorderLayout.SOUTH);

        selector.setFileFilter(new FileNameExtensionFilter("Archivos SVG (*.svg)", "svg"));
        setSize(1050, 760);
        setLocationRelativeTo(null);
    }

    private void seleccionarArchivo() {
        if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            abrirArchivo(selector.getSelectedFile());
        }
    }

    public void abrirArchivo(File archivo) {
        try {
            DocumentoSVG documento = procesador.cargar(archivo);
            panelDibujo.mostrar(documento);
            estado.setText(archivo.getName() + " | " + documento.getFiguras().size()
                    + " figuras | " + documento.getAncho() + " x " + documento.getAlto());
            setTitle("Visualizador SVG - " + archivo.getName());
        } catch (Exception ex) {
            estado.setText("No se pudo abrir: " + archivo.getName());
            JOptionPane.showMessageDialog(this,
                    "No se pudo interpretar el documento SVG:\n" + ex.getMessage(),
                    "Error de lectura o interpretacion", JOptionPane.ERROR_MESSAGE);
        }
    }
}
