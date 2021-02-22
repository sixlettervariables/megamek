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

import java.awt.AWTEventMulticaster;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

/**
 * Simple rectangle hot are for PicMap component. Show single image when idle
 * and "hoghlite" image when mouse is over this area.
 */

public class PMPicArea extends JLabel implements PMHotArea {
    private Rectangle areaShape;
    private ActionListener actionListener = null;
    private Image idleImage;
    private Image activeImage;
    private boolean highlight = true;
    private boolean selected = false;

    public PMPicArea(Image idle, Image active) {
        this.idleImage = idle;
        this.activeImage = active;
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        areaShape = new Rectangle(getX(), getY(), idle.getWidth(null), idle.getHeight(null));
    }

    public PMPicArea(Image im) {
        this(im, null);
        highlight = false;
    }

    // PMElement interface methods
    public void translate(int x, int y) {
        areaShape.translate(x, y);
        setLocation(getX() + x, getY() + y);
    }

    public Rectangle getBounds() {
        return areaShape.getBounds();
    }

    public void drawInto(Graphics g) {
        if ((g == null) || (!isVisible()))
            return;
        if (selected) {
            g.drawImage(activeImage, getX(), getY(), null);
        } else {
            g.drawImage(idleImage, getX(), getY(), null);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        drawInto(g);
    }

    public void setIdleImage(Image idle) {
        this.idleImage = idle;
    }

    public synchronized void addActionListener(ActionListener l) {
        actionListener = AWTEventMulticaster.add(actionListener, l);
    }

    public synchronized void removeActionListener(ActionListener l) {
        actionListener = AWTEventMulticaster.remove(actionListener, l);
    }

    // PMHotArea interface methods
    public Shape getAreaShape() {
        return this.areaShape;
    }

    public void onMouseClick(MouseEvent e) {
        // Override
    }

    public void onMouseOver(MouseEvent e) {
        if (highlight)
            selected = true;
    }

    public void onMouseExit(MouseEvent e) {
        if (highlight)
            selected = false;
    }

    public void onMouseDown(MouseEvent e) {
        // Override
    }

    public void onMouseUp(MouseEvent e) {
        // Override
    }
}