/**
 * MegaMek - Copyright (C) 2000-2002 Ben Mazur (bmazur@sev.org)
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
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

/*
 * A class for showing centered labels with desired value.
 */

public class PMValueLabel extends PMSimpleLabel {

    /*
     * Create the label.
     */
    PMValueLabel(FontMetrics fm, Color c) {
        super("", fm, c); //$NON-NLS-1$
    }

    /*
     * Set/change the value displayed in the label.
     */
    void setValue(String v) {
        setText(v);
        setSize(fm.stringWidth(v), getHeight());
    }

    /*
     * Draw the label.
     */
    @Override
    public void drawInto(Graphics g) {
        if (!isVisible())
            return;
        Color temp = g.getColor();
        g.setColor(color);
        g.drawString(getText(), getX() - getWidth() / 2, getY() - fm.getMaxDescent());
        g.setColor(temp);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(getX() - getWidth() / 2, getY() - getHeight(), getWidth(), getHeight() + descent);
    }
}
