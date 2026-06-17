package org.example;

import org.example.chart.RevenueChartFactory;
import org.example.entity.Order;
import org.example.persistence.HibernateUtil;
import org.example.persistence.OrderRepository;
import org.example.ui.MainWindow;
import org.jfree.chart.JFreeChart;

import javax.swing.SwingUtilities;
import java.util.List;

/**
 * Application entry point.
 *
 * <p>Loads the order records from the database via Hibernate, builds the
 * revenue chart, and shows the Swing window with the chart and export buttons.</p>
 */
public class Main {

    public static void main(String[] args) {
        // 1. Load data from the database (Hibernate + the Order entity mapping).
        List<Order> orders = new OrderRepository().findAll();

        // 2. Build the chart from the loaded records.
        JFreeChart chart = RevenueChartFactory.createRevenueByCategoryChart(orders);

        // 3. Show the window on the Swing event dispatch thread.
        SwingUtilities.invokeLater(() -> new MainWindow(orders, chart).setVisible(true));

        // 4. Release the Hibernate resources when the JVM shuts down.
        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));
    }
}
