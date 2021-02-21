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

import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleStateSet;

/**
 * PMAreasGroup allows to group handle PicMap elements as single entity.
 */

public class PMAreasGroup implements PMElement {
    private Vector<PMElement> gr = new Vector<PMElement>();
    private AccessiblePMAreasGroup accessibleContext;

    /**
     * Adds area to group
     */
    public void addArea(PMElement ha) {
        gr.addElement(ha);
    }

    /**
     * Remoes area from group
     */

    public boolean removeArea(PMElement ag) {
        return gr.removeElement(ag);
    }

    /**
     * Removes all elements from group
     */

    public void removeAll() {
        gr.removeAllElements();
    }

    /**
     * Returns element at specific index.
     */

    public PMElement elementAt(int i) {
        return gr.elementAt(i);
    }

    /**
     * Returns enumeration of all elements in group.
     */
    public Enumeration<PMElement> elements() {
        return gr.elements();
    }

    /**
     * Translates all elements in group by x, y.
     */
    public void translate(int x, int y) {
        Enumeration<PMElement> iter = gr.elements();
        while (iter.hasMoreElements()) {
            PMElement pme = iter.nextElement();
            if (pme != null)
                pme.translate(x, y);
        }
    }

    /**
     * Sets bounding box of all elements in group at (x, y)
     */

    public void moveTo(int x, int y) {
        Rectangle r = getBounds();
        translate(x - r.x, y - r.y);
    }

    /**
     * Returns bounding box which includes all elements in group.
     */
    public Rectangle getBounds() {
        Rectangle bounds = null;
        Enumeration<PMElement> iter = gr.elements();
        while (iter.hasMoreElements()) {
            PMElement pme = iter.nextElement();
            if ((pme != null) && (pme.getBounds() != null)) {
                if (bounds == null) {
                    bounds = pme.getBounds();
                } else {
                    bounds = bounds.union(pme.getBounds());
                }
            }
        }
        return bounds;
    }

    /**
     * Draws all elements in group into specifyed Graphics
     */

    public void drawInto(Graphics g) {
        Enumeration<PMElement> iter = gr.elements();
        while (iter.hasMoreElements()) {
            PMElement pme = iter.nextElement();
            if (pme != null)
                pme.drawInto(g);
        }
    }

    /**
     * Sets visibility of all elements in roup to true or false.
     */

    public void setVisible(boolean v) {
        Enumeration<PMElement> iter = gr.elements();
        while (iter.hasMoreElements()) {
            PMElement pme = iter.nextElement();
            pme.setVisible(v);
        }
    }

    @Override
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext != null) {
            accessibleContext = new AccessiblePMAreasGroup();
        }

        return accessibleContext;
    }

    protected class AccessiblePMAreasGroup extends AccessibleContext {

        @Override
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PANEL;
        }

        @Override
        public AccessibleStateSet getAccessibleStateSet() {
            // TODO Auto-generated method stub
            return new AccessibleStateSet();
        }

        @Override
        public int getAccessibleIndexInParent() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getAccessibleChildrenCount() {
            return PMAreasGroup.this.gr.size();
        }

        @Override
        public Accessible getAccessibleChild(int i) {
            if (i < 0 || i >= PMAreasGroup.this.gr.size()) {
                return null;
            }

            return PMAreasGroup.this.gr.elementAt(i);
        }

        @Override
        public Locale getLocale() throws IllegalComponentStateException {
            return Locale.getDefault();
        }
    }
}
