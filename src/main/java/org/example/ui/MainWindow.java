package org.example.ui;

import org.example.entity.Order;
import org.example.export.JsonExporter;
import org.example.export.PdfExporter;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.nio.file.Path;
import java.util.List;

/**
 * Main application window: shows the revenue chart and offers two export buttons
 * (chart to PDF, and all records to JSON).
 */
public class MainWindow extends JFrame {

    private final transient List<Order> orders;
    private final transient JFreeChart chart;

    public MainWindow(List<Order> orders, JFreeChart chart) {
        super("Online Shop Orders - Revenue Dashboard");
        this.orders = orders;
        this.chart = chart;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(900, 600));
        add(chartPanel, BorderLayout.CENTER);

        add(buildButtonBar(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // centre on screen
    }

    private JPanel buildButtonBar() {
        JButton exportPdfButton = new JButton("Export Chart as PDF");
        exportPdfButton.addActionListener(e -> exportPdf());

        JButton exportJsonButton = new JButton("Export");
        exportJsonButton.setToolTipText("Export all order records as a JSON file");
        exportJsonButton.addActionListener(e -> exportJson());

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.add(exportPdfButton);
        panel.add(exportJsonButton);
        return panel;
    }

    private void exportPdf() {
        try {
            Path file = PdfExporter.export(chart);
            showInfo("Chart exported as PDF:\n" + file);
        } catch (Exception ex) {
            showError("Could not export the chart to PDF", ex);
        }
    }

    private void exportJson() {
        try {
            Path file = JsonExporter.export(orders);
            showInfo(orders.size() + " records exported as JSON:\n" + file);
        } catch (Exception ex) {
            showError("Could not export the records to JSON", ex);
        }
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Export successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message, Exception ex) {
        JOptionPane.showMessageDialog(this, message + ":\n" + ex.getMessage(),
                "Export failed", JOptionPane.ERROR_MESSAGE);
    }
}
