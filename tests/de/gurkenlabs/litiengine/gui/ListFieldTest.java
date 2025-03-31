package de.gurkenlabs.litiengine.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.gurkenlabs.litiengine.Game;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SwingTestSuite.class)
class ListFieldTest {

  private final String[] content_1D = new String[] {"A", "B", "C", "D", "E", "F", "G"};
  private final int content_1D_shownRows = 4;

  private final String[][] content_2D =
      new String[][] {
        {"A", "B", "C", "D", "E", "F", "G"},
        {"H", "I", "J", "K", "L", "M", "N", "O"},
        {"P", "Q", "R", "S", "T", "U", "V"},
        {"W", "X", "Y", "Z"}
      };
  private final int content_2D_shownRows = 7;
  private final int content_2D_shownColumns = 3;

  @BeforeAll
  public static void initGame() {
    Game.init(Game.COMMADLINE_ARG_NOGUI);
  }

  @BeforeEach
  public void assertOnSwingThread() {
    assertTrue(SwingUtilities.isEventDispatchThread());
  }

  @Test
  void testInitialization() {
    ListField listField_1D = new ListField(0, 0, 100, 50, this.content_1D, content_1D_shownRows);
    ListField listField_2D =
        new ListField(
            0, 0, 100, 50, this.content_2D, content_2D_shownRows, content_2D_shownColumns);

    assertNotNull(listField_1D);
    assertNotNull(listField_2D);

    assertEquals(this.content_1D, listField_1D.getContent()[0]);
    assertEquals(this.content_2D, listField_2D.getContent());

    for (int i = 0; i < listField_1D.getListEntry(0).size(); i++) {
      assertEquals(this.content_1D[i], listField_1D.getListEntry(0).get(i).getText());
    }
    for (int i = 0; i < listField_2D.getNumberOfShownColumns(); i++) {
      for (int j = 0; j < listField_2D.getListEntry(i).size(); j++) {
        assertEquals(this.content_2D[i][j], listField_2D.getListEntry(i).get(j).getText());
      }
    }

    assertEquals(7, listField_1D.getMaxRows());
    assertEquals(8, listField_2D.getMaxRows());

    listField_1D.prepare();
    listField_2D.prepare();

    assertTrue(listField_1D.isVisible());
    assertTrue(listField_2D.isVisible());

    assertTrue(listField_1D.isEnabled());
    assertTrue(listField_2D.isEnabled());

    assertFalse(listField_1D.isArrowKeyNavigation());
    assertFalse(listField_2D.isArrowKeyNavigation());

    assertFalse(listField_1D.isEntireRowSelected());
    assertFalse(listField_2D.isEntireRowSelected());

    assertFalse(listField_1D.isSliderInside());
    assertFalse(listField_2D.isSliderInside());
  }

  @Test
  void initSliders_with2dSlider() {
    // arrange
    ListField listField =
        new ListField(
            0, 0, 100, 50, this.content_2D, content_2D_shownRows, content_2D_shownColumns);

    assertNotNull(listField.getHorizontalSlider());
    assertEquals(100, listField.getHorizontalSlider().getWidth());
    assertNotNull(listField.getVerticalSlider());
    assertEquals(50, listField.getVerticalSlider().getHeight());

    // act
    listField.setSliderInside(true);

    // assert
    assertNotNull(listField.getHorizontalSlider());
    assertEquals(90, listField.getHorizontalSlider().getWidth()); // 100 - (50/5)
    assertNotNull(listField.getVerticalSlider());
    assertEquals(40, listField.getVerticalSlider().getHeight()); // 50 - (50/5)
  }

  @Test
  void initSliders_nullHorizontalSlider() {
    // arrange
    ListField listField =
        new ListField(0, 0, 100, 50, this.content_2D, content_2D_shownRows, content_2D.length);

    assertNull(listField.getHorizontalSlider());
    assertNotNull(listField.getVerticalSlider());
    assertEquals(100, listField.getVerticalSlider().getX());

    // act
    listField.setSliderInside(true);

    // assert
    assertNull(listField.getHorizontalSlider());
    assertNotNull(listField.getVerticalSlider());
    assertEquals(90, listField.getVerticalSlider().getX()); // 100 - (50/5)
  }
}
