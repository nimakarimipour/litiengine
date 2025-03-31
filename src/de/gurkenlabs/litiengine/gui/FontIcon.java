package de.gurkenlabs.litiengine.gui;

import de.gurkenlabs.litiengine.graphics.TextRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import javax.annotation.Nullable;

/** A fonticon is an class that represents a single character of an icon font. */
public class FontIcon {
  /** The font. */
  @Nullable private final Font font;

  /** The text. */
  private final String text;

  /**
   * Instantiates a new icon.
   *
   * @param font the font
   * @param text the text
   */
  public FontIcon(final Font font, final char text) {
    this.font = font;
    this.text = String.valueOf(text);
  }

  public FontIcon(@Nullable final Font font, final String unicode) {
    this.font = font;
    this.text = unicode;
  }

  /**
   * Gets the font.
   *
   * @return the font
   */
  @Nullable
  public Font getFont() {
    return this.font;
  }

  /**
   * Gets the text.
   *
   * @return the text
   */
  public String getText() {
    return this.text;
  }

  /**
   * Render.
   *
   * @param g the g
   * @param color the color
   * @param fontSize the font size
   * @param x the x
   * @param y the y
   * @param bold the bold
   */
  public void render(
      final Graphics2D g,
      final Color color,
      final float fontSize,
      final double x,
      final double y,
      final boolean bold) {
    final Color oldColor = g.getColor();
    final Font oldFont = g.getFont();
    g.setColor(color);
    Font currentFont = this.getFont(); // Assign to a local variable
    if (currentFont != null) { // Check if the font is not null
      if (bold) {
        g.setFont(currentFont.deriveFont(Font.BOLD, fontSize));
      } else {
        g.setFont(currentFont.deriveFont(fontSize));
      }
    } else { // Provide a default font in case currentFont is null
      g.setFont(new Font("default", bold ? Font.BOLD : Font.PLAIN, (int) fontSize));
    }
    TextRenderer.render(g, this.getText(), x, y);
    g.setColor(oldColor);
    g.setFont(oldFont);
  }
}
