package org.example.export;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.chart.JFreeChart;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Exports a {@link JFreeChart} to a PDF file using Apache PDFBox.
 *
 * <p>The chart is first rendered to an in-memory image, which is then embedded
 * on a single PDF page sized to match the image.</p>
 */
public final class PdfExporter {

    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;

    private PdfExporter() {
    }

    /**
     * Renders the chart and writes it to {@code output/chart.pdf}.
     *
     * @return the path of the written PDF file
     */
    public static Path export(JFreeChart chart) throws IOException {
        Path target = OutputDirectory.resolve("chart.pdf");
        BufferedImage image = chart.createBufferedImage(WIDTH, HEIGHT);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(WIDTH, HEIGHT));
            document.addPage(page);

            PDImageXObject pdfImage = LosslessFactory.createFromImage(document, image);
            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                content.drawImage(pdfImage, 0, 0, WIDTH, HEIGHT);
            }
            document.save(target.toFile());
        }
        return target;
    }
}
