package jpolanco.springbootapp.user.application.utils;

import java.awt.image.BufferedImage;

public record QRResult(
        String svg,
        BufferedImage image
)
{}
