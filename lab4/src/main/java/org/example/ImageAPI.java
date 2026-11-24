package org.example;

import java.util.Locale;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nu.pattern.OpenCV;

public class ImageAPI {

    private static final Logger log = LoggerFactory.getLogger(ImageAPI.class);

    public enum OSType {
        WINDOWS, MACOS, LINUX, OTHER
    }

    public OSType getOperatingSystemType() {
        String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if ((os.contains("mac")) || (os.contains("darwin"))) {
            return OSType.MACOS;
        } else if (os.contains("win")) {
            return OSType.WINDOWS;
        } else if (os.contains("nux")) {
            return OSType.LINUX;
        } else {
            return OSType.OTHER;
        }
    }

    public ImageAPI() {
        OpenCV.loadLocally();
        log.info("OpenCV library loaded successfully");
    }

    public void applyFilters(String imagePath, int kernelSize) {
        if (imagePath == null || imagePath.isEmpty()) {
            log.error("Image path is null or empty");
            return;
        }

        Mat src = Imgcodecs.imread(imagePath);
        if (src.empty()) {
            log.error("Image not found at path: " + imagePath);
            return;
        }

        Mat dst = new Mat();
        String outputDir = Config.getAbsolutePath("Images/1/");

        // Усредняющий фильтр
        Imgproc.blur(src, dst, new Size(kernelSize, kernelSize));
        Imgcodecs.imwrite(outputDir + "blur_" + kernelSize + ".jpg", dst);

        // Гауссовский фильтр
        Imgproc.GaussianBlur(src, dst, new Size(kernelSize, kernelSize), 0);
        Imgcodecs.imwrite(outputDir + "gaussian_" + kernelSize + ".jpg", dst);

        // Медианный фильтр
        Imgproc.medianBlur(src, dst, kernelSize);
        Imgcodecs.imwrite(outputDir + "median_" + kernelSize + ".jpg", dst);

        // Двусторонний фильтр
        Imgproc.bilateralFilter(src, dst, kernelSize, kernelSize * 2, kernelSize / 2);
        Imgcodecs.imwrite(outputDir + "bilateral_" + kernelSize + ".jpg", dst);
    }

    public void performMorphology(String imagePath, int[] kernelSizes, int[] morphTypes) {
        if (imagePath == null || imagePath.isEmpty()) {
            log.error("Image path is null or empty");
            return;
        }

        Mat src = Imgcodecs.imread(imagePath);
        if (src.empty()) {
            log.error("Image not found at path: " + imagePath);
            return;
        }

        String outputDir = Config.getAbsolutePath("Images/2/");

        for (int size : kernelSizes) {
            for (int type : morphTypes) {
                Mat element = Imgproc.getStructuringElement(type, new Size(size, size));
                Mat dst = new Mat();

                // Морфологическое сужение
                Imgproc.erode(src, dst, element);
                Imgcodecs.imwrite(outputDir + "erode_" + size + "_" + type + ".jpg", dst);

                // Морфологическое расширение
                Imgproc.dilate(src, dst, element);
                Imgcodecs.imwrite(outputDir + "dilate_" + size + "_" + type + ".jpg", dst);
            }
        }
    }
}