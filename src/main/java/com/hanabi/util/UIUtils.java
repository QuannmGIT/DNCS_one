package com.hanabi.util;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.AbstractBorder;

public class UIUtils {

    private UIUtils() {}

    public static void setRounded(JComponent comp, int arc) {
        comp.putClientProperty(FlatClientProperties.STYLE,
                "arc:" + arc + ";focusWidth:0;innerFocusWidth:0");
    }

    public static void styleActionButton(JButton btn, int arc, Color bg, Color fg, Color hoverBg, String pressedHex) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE,
                "arc:" + arc + ";borderWidth:0;focusWidth:0;innerFocusWidth:0;" +
                "pressedBackground:#" + pressedHex);
        btn.putClientProperty("JButton.hoverBackground", hoverBg);
    }

    public static RoundedBorder createRoundedBorder(Color color, int thickness, int arc) {
        return new RoundedBorder(color, thickness, arc);
    }

    public static RoundedBorder createRoundedBorder(int arc) {
        return new RoundedBorder(new Color(154, 123, 90), 1, arc);
    }

    public static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int arc;

        public RoundedBorder(Color color, int thickness, int arc) {
            this.color = color;
            this.thickness = thickness;
            this.arc = arc;
        }

        public Color getColor() { return color; }
        public int getThickness() { return thickness; }
        public int getArc() { return arc; }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(
                x + thickness / 2,
                y + thickness / 2,
                width - thickness,
                height - thickness,
                arc, arc);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            int pad = thickness + 2;
            return new Insets(pad, pad, pad, pad);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            int pad = thickness + 2;
            insets.set(pad, pad, pad, pad);
            return insets;
        }
    }
}
