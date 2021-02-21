/**
 * MegaMek - Copyright (C) 2000,2001,2002,2004 Ben Mazur (bmazur@sev.org)
 * 
 *  This program is free software; you can redistribute it and/or modify it 
 *  under the terms of the GNU General Public License as published by the Free 
 *  Software Foundation; either version 2 of the License, or (at your option) 
 *  any later version.
 * 
 *  This program is distributed in the hope that it will be useful, but 
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 *  for more details.
 */

package megamek.client.ui.swing.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;

public class PMSimpleLabel extends JLabel implements PMLabel {

    private static final long serialVersionUID = 2980037123366497246L;

    // The descent of the label
    int descent;
    // Color to draw the label with.
    Color color;
    // Font and Fontmetrics for the label
    Font f;
    FontMetrics fm;

    /*
     * Create the label with the specified string, font and color
     */
    public PMSimpleLabel(String s, FontMetrics fm, Color c) {
        super(s);
        this.fm = fm;
        super.setSize(fm.stringWidth(s), fm.getHeight());
        descent = fm.getMaxDescent();
        color = c;
        setFocusable(true);
    }

    public void setString(String s) {
        setText(s);
        // The width use to just be the stringWidth, but this
        // sometimes caused cropping when setString was called.
        // The value of 140% was chosen by trial and error, and
        // may be incorrect. In fact, this whole fix is
        // basically a kludge, since I don't know why it
        // is needed.
        int width = (int) Math.ceil(fm.stringWidth(s) * 1.4);
        int height = fm.getHeight();
        setSize(width, height);
        descent = fm.getMaxDescent();
    }

    /*
     * Set the color of the label of the font.
     */
    public void setColor(Color c) {
        color = c;
    }

    /*
     * translate the coordinates of the label.
     */
    public void moveTo(int x, int y) {
        setLocation(x, y);
    }

    public void translate(int x, int y) {
        setLocation(getX() + x, getY() + y);
    }

    /*
     * Draw the label.
     */
    public void drawInto(Graphics g) {
        if (!isVisible())
            return;
        Font font = g.getFont();
        Color temp = g.getColor();
        g.setColor(color);
        g.setFont(fm.getFont());
        g.drawString(getText(), getX(), getY());
        g.setColor(temp);
        g.setFont(font);
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY() - getHeight() + descent, getWidth(), getHeight());
    }

    /*
     * Returns the descent of the label.
     */
    public int getDescent() {
        return descent;
    }
}
