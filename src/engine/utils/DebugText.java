package engine.utils;

import java.awt.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class DebugText {

  private final static Font awtFont = new Font("Times New Roman", Font.BOLD,
      24); //name, style (PLAIN, BOLD, or ITALIC), size

  private final static TrueTypeFont font = new TrueTypeFont(awtFont,
      false); //base Font, anti-aliasing true/false


  public static void write(String string) {
    font.drawString(100, 50, string, Color.red); //x, y, string to draw, color
  }
}
