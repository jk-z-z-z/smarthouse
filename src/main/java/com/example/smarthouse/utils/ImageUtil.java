package com.example.smarthouse.utils;

public class ImageUtil {

    public static String getMediaType(String imagePath) {
        if (imagePath.endsWith(".jpg") || imagePath.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (imagePath.endsWith(".png")) {
            return "image/png";
        } else if (imagePath.endsWith(".gif")) {
            return "image/gif";
        } else if (imagePath.endsWith(".bmp")) {
            return "image/bmp";
        } else {
            throw new IllegalArgumentException("Unsupported image type: " + imagePath);
        }
    }
    public static String getImageType(String imagePath) {
        if (imagePath.endsWith(".jpg") || imagePath.endsWith(".jpeg")) {
            return ".jpeg";
        } else if (imagePath.endsWith(".png")) {
            return ".png";
        } else if (imagePath.endsWith(".gif")) {
            return ".gif";
        } else if (imagePath.endsWith(".bmp")) {
            return ".bmp";
        } else {
            throw new IllegalArgumentException("Unsupported image type: " + imagePath);
        }
    }


}
