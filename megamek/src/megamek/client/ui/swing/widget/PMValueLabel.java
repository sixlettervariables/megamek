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

import javax.accessibility.AccessibleContext;

/*
 * A class for showing centered labels with desired value.
 */

public class PMValueLabel extends PMSimpleLabel {

    /*
     * Create the label.
     */
    public PMValueLabel(FontMetrics fm, Color c) {
        super("", fm, c); //$NON-NLS-1$
    }

    /*
     * Set/change the value displayed in the label.
     */
    public void setValue(String v) {
        String oldAccessibleName = null;
        if (accessibleContext != null) {
            oldAccessibleName = accessibleContext.getAccessibleName();
        }

        string = v;
        width = fm.stringWidth(string);
        height = fm.getHeight();
        descent = fm.getMaxDescent();

        if ((accessibleContext != null)
            && (accessibleContext.getAccessibleName() != oldAccessibleName)) {
                accessibleContext.firePropertyChange(
                        AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY,
                        oldAccessibleName,
                        accessibleContext.getAccessibleName());
        }
    }

    /*
     * Draw the label.
     */
    @Override
    public void drawInto(Graphics g) {
        if (!visible)
            return;
        Color temp = g.getColor();
        g.setColor(color);
        g.drawString(string, x - width / 2, y - fm.getMaxDescent());
        g.setColor(temp);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x - width / 2, y - height, width, height + descent);
    }
}
