package org.example.chart;

import org.example.entity.Order;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Builds the chart shown by the application: total revenue per product category.
 *
 * <p>Revenue is aggregated from the loaded {@link Order} records
 * ({@code quantity * unitPrice}) and grouped by {@code productCategory}.</p>
 */
public final class RevenueChartFactory {

    private static final String ROW_KEY = "Revenue";

    private RevenueChartFactory() {
    }

    public static JFreeChart createRevenueByCategoryChart(List<Order> orders) {
        return ChartFactory.createBarChart(
                "Revenue by Product Category",   // chart title
                "Product Category",              // domain (x) axis label
                "Total Revenue",                 // range (y) axis label
                buildDataset(orders),
                PlotOrientation.VERTICAL,
                false,                           // legend not needed (single series)
                true,                            // tooltips
                false);                          // urls
    }

    private static CategoryDataset buildDataset(List<Order> orders) {
        // TreeMap keeps the categories in a stable, alphabetical order.
        Map<String, BigDecimal> revenueByCategory = new TreeMap<>();
        for (Order order : orders) {
            String category = order.getProductCategory() == null
                    ? "Unknown"
                    : order.getProductCategory();
            revenueByCategory.merge(category, order.getRevenue(), BigDecimal::add);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        revenueByCategory.forEach((category, revenue) ->
                dataset.addValue(revenue.setScale(2, RoundingMode.HALF_UP), ROW_KEY, category));
        return dataset;
    }
}
