package com.github.vtramo.turingmachine.ui;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class LatexFormulaPngGenerator {
    public Path generate(final String latex, final int renderingFontSize) {
        final TeXFormula formula = new TeXFormula(latex);

        final TeXIcon icon = formula.new TeXIconBuilder()
            .setStyle(TeXConstants.STYLE_DISPLAY)
            .setSize(renderingFontSize)
            .build();
        icon.setInsets(new Insets(0, 0, 0, 0));

        final BufferedImage image = new BufferedImage(
            icon.getIconWidth(),
            icon.getIconHeight(),
            BufferedImage.TYPE_INT_ARGB
        );

        final Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());

        final JLabel jl = new JLabel();
        jl.setForeground(new Color(0, 0, 0));

        icon.paintIcon(jl, g2, 0, 0);

        try {
            final File file = File.createTempFile("formula", ".png");
            ImageIO.write(image, "png", file.getAbsoluteFile());
            return file.toPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
