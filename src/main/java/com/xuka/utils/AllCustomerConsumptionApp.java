package com.xuka.utils;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AllCustomerConsumptionApp extends Application {
    private static List<Map.Entry<String, Double>> customerConsumption;

    public static void setCustomerConsumption(List<Map.Entry<String, Double>> consumption) {
        customerConsumption = consumption;
    }

    @Override
    public void start(Stage stage) {
        // Create axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Customers");
        yAxis.setLabel("Total Consumption");

        // Create bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Total Consumption of All Customers");

        // Add data to the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Consumption");

        for (Map.Entry<String, Double> entry : customerConsumption) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().add(series);

        // Create a scene and display the chart
        Scene scene = new Scene(barChart, 800, 600);
        stage.setScene(scene);
        stage.setTitle("All Customers Consumption Chart");
        stage.show();

        // Save the chart as an image
        WritableImage image = scene.snapshot(null);
        File outputFile = new File("src/main/data/figure/all_customers_consumption_chart.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the application after saving the image
        stage.close();
    }
}