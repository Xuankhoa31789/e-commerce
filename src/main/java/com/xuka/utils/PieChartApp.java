package com.xuka.utils;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PieChartApp extends Application {
    private static int lessThan30;
    private static int between30And60;
    private static int greaterThan60;

    @Override
    public void start(Stage stage) {
        // Create PieChart data
        PieChart pieChart = new PieChart();
        pieChart.getData().add(new PieChart.Data("Less than 30%", lessThan30));
        pieChart.getData().add(new PieChart.Data("30% - 60%", between30And60));
        pieChart.getData().add(new PieChart.Data("Greater than 60%", greaterThan60));
        pieChart.setTitle("Discount Distribution");

        // Create a scene and display the chart
        Scene scene = new Scene(pieChart, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Discount Pie Chart");
        stage.show();

        // Save the chart as an image
        WritableImage image = scene.snapshot(null);
        File outputFile = new File("src/main/data/figure/discount_pie_chart.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the application after saving the image
        stage.close();
    }

    public static void setLessThan30(int lessThan30) {
        PieChartApp.lessThan30 = lessThan30;
    }

    public static void setBetween30And60(int between30And60) {
        PieChartApp.between30And60 = between30And60;
    }

    public static void setGreaterThan60(int greaterThan60) {
        PieChartApp.greaterThan60 = greaterThan60;
    }
}
