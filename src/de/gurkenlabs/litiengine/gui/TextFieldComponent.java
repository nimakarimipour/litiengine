package de.gurkenlabs.litiengine.gui;

import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.input.Input;
import edu.ucr.cs.riple.annotator.util.Nullability;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public class TextFieldComponent extends ImageComponent {
  public static final String DOUBLE_FORMAT = "[-+]?[0-9]*\\.?[0-9]*([eE][-+]?[0-9]*)?";
  public static final String INTEGER_FORMAT = "[0-9]{1,10}";
  private static final Logger log = Logger.getLogger(TextFieldComponent.class.getName());
  private final List<Consumer<String>> changeConfirmedConsumers;
  private boolean cursorVisible;
  private final int flickerDelay;
  @Nullable private String format;

  @Nullable private String fullText;
  private long lastToggled;
  private int maxLength = 0;

  public TextFieldComponent(
      final double x, final double y, final double width, final double height, final String text) {
    super(x, y, width, height, text);
    this.changeConfirmedConsumers = new CopyOnWriteArrayList<>();
    this.setText(text);
    this.flickerDelay = 100;
    Input.keyboard().onKeyTyped(this::handleTypedKey);
    this.onClicked(
        e -> {
          if (!this.isSelected()) {
            this.toggleSelection();
          }
        });

    Input.mouse()
        .onClicked(
            e -> {
              if (!this.getBoundingBox().contains(Input.mouse().getLocation())) {
                this.setSelected(false);
              }
            });

    this.setTextAlign(Align.LEFT);
  }

  @Nullable
  public String getFormat() {
    return this.format;
  }

  public int getMaxLength() {
    return this.maxLength;
  }

  @Nullable
  @Override
  public String getText() {
    return this.fullText;
  }

  public void handleTypedKey(final KeyEvent event) {
    if (this.isSuspended() || !this.isSelected() || !this.isVisible() || !this.isEnabled()) {
      return;
    }

    String currentText = this.getText() != null ? this.getText() : "";

    switch (event.getKeyCode()) {
      case KeyEvent.VK_BACK_SPACE:
        this.handleBackSpace();
        break;
      case KeyEvent.VK_SPACE:
        if (!currentText.equals("")) {
          this.setText(currentText + " ");
        }
        break;
      case KeyEvent.VK_ENTER:
        this.toggleSelection();
        this.changeConfirmedConsumers.forEach(c -> c.accept(currentText));

        log.log(
            Level.INFO,
            "{0} typed into TextField with ComponentID {1}",
            new Object[] {currentText, this.getComponentId()});
        break;
      default:
        this.handleNormalTyping(event);
        break;
    }
  }

  public void onChangeConfirmed(final Consumer<String> cons) {
    this.changeConfirmedConsumers.add(cons);
  }

  @Override
  public void render(final Graphics2D g) {
    super.render(g);
    g.setFont(this.getFont());
    final FontMetrics fm = g.getFontMetrics();

    if (this.isSelected() && Game.time().since(this.lastToggled) > this.flickerDelay) {
      this.cursorVisible = !this.cursorVisible;
      this.lastToggled = Game.time().now();
    }
    if (this.isSelected() && this.cursorVisible) {
      final Rectangle2D cursor =
          new Rectangle2D.Double(
              this.getX() + this.getTextX() + fm.stringWidth(this.getTextToRender(g)),
              this.getY() + this.getTextY(),
              this.getFont().getSize2D() * 3 / 5,
              this.getFont().getSize2D() * 1 / 5);
      g.setColor(this.getAppearance().getForeColor());
      g.fill(cursor);
    }
  }

  public void setFormat(final String format) {
    this.format = format;
  }

  public void setMaxLength(final int maxLength) {
    this.maxLength = maxLength;
  }

  @Override
  public void setText(@Nullable final String text) {
    this.fullText = text;
  }

  private void handleBackSpace() {
    String text = this.getText();
    if (text != null) {
      if (Input.keyboard().isPressed(KeyEvent.VK_SHIFT)) {
        while (Nullability.castToNonnull(this.getText(), "initially validated non-null").length()
                >= 1
            && text.charAt(text.length() - 1) == ' ') {
          text = text.substring(0, text.length() - 1);
          this.setText(text);
        }

        while (Nullability.castToNonnull(this.getText(), "initially validated non-null").length()
                >= 1
            && text.charAt(text.length() - 1) != ' ') {
          text = text.substring(0, text.length() - 1);
          this.setText(text);
        }
      } else if (text.length() >= 1) {
        text = text.substring(0, text.length() - 1);
        this.setText(text);
      }

      if (this.isKnownNumericFormat() && (text == null || text.isEmpty())) {
        this.setText("0");
      }
    }
  }

  private void handleNormalTyping(KeyEvent event) {
    String currentText = this.getText();

    if (currentText != null
        && this.getMaxLength() > 0
        && currentText.length() >= this.getMaxLength()) {
      return;
    }

    final char text = event.getKeyChar();
    if (text == KeyEvent.CHAR_UNDEFINED) {
      return;
    }

    if (this.getFormat() != null && !this.getFormat().isEmpty()) {
      final Pattern pat = Pattern.compile(this.getFormat());
      final Matcher mat = pat.matcher(currentText + text);
      if (!mat.matches()) {
        return;
      }
    }

    if (this.isKnownNumericFormat()
        && "0".equals(Nullability.castToNonnull(this.getText(), "checked for null"))) {
      this.setText("");
    }

    this.setText(currentText + text);
  }

  private boolean isKnownNumericFormat() {
    return this.getFormat() != null
        && (this.getFormat().equals(INTEGER_FORMAT) || this.getFormat().equals(DOUBLE_FORMAT));
  }
}
