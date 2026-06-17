# Technical Documentation — Online Shop Orders Dashboard

## Purpose of the Application

A Java Swing desktop application that reads order data from an existing database
table, visualises it as a chart, and offers two exports:

1. The chart can be exported as a **PDF** file.
2. All order records can be exported as a **JSON** file.

When the application starts, it loads the data from the database, builds a bar
chart of **total revenue per product category**, and shows a window containing
the chart and two export buttons. Both exports are written to the project's
`output/` directory.

## Technologies and Libraries Used

| Concern             | Technology / Library                | Notes                                              |
|---------------------|-------------------------------------|----------------------------------------------------|
| Language / Runtime  | Java 25                             | Compiled with `--release 25`                       |
| GUI                 | Java Swing                          | `JFrame`, `ChartPanel`, `JButton`                  |
| ORM / DB access     | Hibernate ORM 6.6                   | Maps the table to a Java entity                    |
| Database driver     | Microsoft SQL Server JDBC 12.6      | Connects to SQL Server                             |
| Boilerplate removal | Lombok 1.18                         | Getters/setters/constructor on the entity         |
| Charting            | JFreeChart 1.5.5                    | Bar chart + on-screen `ChartPanel`                 |
| PDF export          | Apache PDFBox 3.0                   | Embeds the rendered chart image into a PDF         |
| JSON export         | Jackson 2.17 (+ JSR-310 module)     | Serialises the records to JSON                     |
| Logging             | SLF4J Simple                        | Shows Hibernate output on the console              |
| Build               | Apache Maven                        | Dependency management and build                    |
| Version control     | Git                                 | Multiple, traceable commits                        |

## Database

* **Engine:** Microsoft SQL Server
* **Database:** `turingdb`
* **Table:** `dbo.OnlineShopOrders` (≈200 records)

Connection settings live in [`src/main/resources/hibernate.cfg.xml`](src/main/resources/hibernate.cfg.xml).
The schema is treated as **read-only** (`hibernate.hbm2ddl.auto = none`), so the
application never modifies the table structure.

### Table → Class Mapping

The table is mapped to [`org.example.entity.Order`](src/main/java/org/example/entity/Order.java)
using JPA annotations. Each column maps to one field; Lombok generates the
accessors.

| Database column   | Java field        | Java type     |
|-------------------|-------------------|---------------|
| `OrderID` (PK)    | `orderId`         | `int`         |
| `CustomerName`    | `customerName`    | `String`      |
| `ProductCategory` | `productCategory` | `String`      |
| `ProductName`     | `productName`     | `String`      |
| `OrderDate`       | `orderDate`       | `LocalDate`   |
| `Quantity`        | `quantity`        | `Integer`     |
| `UnitPrice`       | `unitPrice`       | `BigDecimal`  |
| `PaymentMethod`   | `paymentMethod`   | `String`      |
| `ShippingCountry` | `shippingCountry` | `String`      |
| `OrderStatus`     | `orderStatus`     | `String`      |

The entity also exposes a derived `getRevenue()` (`quantity × unitPrice`) used by
both the chart and the JSON export.

> The JPA *entity name* is set to `ShopOrder` (`@Entity(name = "ShopOrder")`)
> because `ORDER` is a reserved word in HQL/JPQL.

## Structure of the Main Classes

```
org.example
├── Main                      → entry point: load data → build chart → show window
├── entity
│   └── Order                 → @Entity mapped to dbo.OnlineShopOrders (Lombok)
├── persistence
│   ├── HibernateUtil         → builds & holds the SessionFactory
│   └── OrderRepository       → findAll(): loads every order via HQL
├── chart
│   └── RevenueChartFactory   → aggregates revenue per category into a JFreeChart
├── export
│   ├── OutputDirectory       → resolves/creates the output/ directory
│   ├── PdfExporter           → chart → output/chart.pdf (PDFBox)
│   └── JsonExporter          → records → output/orders.json (Jackson)
└── ui
    └── MainWindow            → JFrame with the chart and the two export buttons
```

## Functionality of the Chart

`RevenueChartFactory` iterates over the loaded `Order` objects and sums
`quantity × unitPrice` per `productCategory` (using a `TreeMap` for stable,
alphabetical category order). The aggregated values are placed into a
`DefaultCategoryDataset` and rendered as a vertical **bar chart** titled
*"Revenue by Product Category"*. The chart is displayed inside a JFreeChart
`ChartPanel` in the centre of the window.

## Functionality of the PDF Export

The **"Export Chart as PDF"** button calls `PdfExporter.export(chart)`:

1. The chart is rendered to an in-memory `BufferedImage` (900 × 600).
2. A single-page `PDDocument` is created with PDFBox, the image is embedded via
   `LosslessFactory`, and drawn to fill the page.
3. The document is saved to **`output/chart.pdf`**.

A confirmation dialog shows the saved file path.

## Functionality of the JSON Export

The **"Export"** button calls `JsonExporter.export(orders)`:

1. A Jackson `ObjectMapper` (with the JavaTime module, ISO date formatting and
   pretty-printing) serialises the full list of `Order` objects.
2. The result is written to **`output/orders.json`**.

The JSON file contains all records and is intended as an exchange format for
neighbouring applications and systems.

## Instructions for Running the Application

### Prerequisites
* JDK 25
* Maven (or run from an IDE such as IntelliJ IDEA)
* Network access to the SQL Server database (configured in `hibernate.cfg.xml`)

### Run from the command line
```bash
# from the project directory (where pom.xml is)
mvn compile
mvn exec:java
```

### Run from an IDE
Run the `org.example.Main` class.

### Result
* A window opens showing the *Revenue by Product Category* bar chart.
* **Export Chart as PDF** writes `output/chart.pdf`.
* **Export** writes `output/orders.json`.
