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
import java.awt.IllegalComponentStateException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Locale;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleStateSet;

/**
 * Abstract class which defines common functionality for all hot areas such as
 * event handling and dispatching.
 */

public abstract class PMGenericHotArea implements PMHotArea {

    private ActionListener actionListener = null;
    private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
    private AccessiblePMGenericHotArea accessibleContext;

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor c) {
        cursor = c;
    }

    public synchronized void addActionListener(ActionListener l) {
        actionListener = AWTEventMulticaster.add(actionListener, l);
    }

    public synchronized void removeActionListener(ActionListener l) {
        actionListener = AWTEventMulticaster.remove(actionListener, l);
    }

    public void onMouseClick(MouseEvent e) {
        int modifiers = e.getModifiers();
        String command = ""; //$NON-NLS-1$

        if (0 != (modifiers & InputEvent.BUTTON1_MASK)) {
            command = PMHotArea.MOUSE_CLICK_LEFT;
        } else if (0 != (modifiers & InputEvent.BUTTON2_MASK)) {
            command = PMHotArea.MOUSE_CLICK_RIGHT;
        }

        if (e.getClickCount() > 1)
            command = PMHotArea.MOUSE_DOUBLE_CLICK;

        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, modifiers);
        dispatchEvent(ae);
    }

    public void onMouseOver(MouseEvent e) {
        int modifiers = e.getModifiers();
        String command = PMHotArea.MOUSE_OVER;
        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, modifiers);
        dispatchEvent(ae);

    }

    public void onMouseExit(MouseEvent e) {
        int modifiers = e.getModifiers();
        String command = PMHotArea.MOUSE_EXIT;
        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, modifiers);
        dispatchEvent(ae);
    }

    public void onMouseDown(MouseEvent e) {
        int modifiers = e.getModifiers();
        String command = PMHotArea.MOUSE_DOWN;
        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, modifiers);
        dispatchEvent(ae);
    }

    public void onMouseUp(MouseEvent e) {
        int modifiers = e.getModifiers();
        String command = PMHotArea.MOUSE_UP;
        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, modifiers);
        dispatchEvent(ae);
    }

    private void dispatchEvent(ActionEvent ae) {
        if (actionListener != null) {
            actionListener.actionPerformed(ae);
        }
    }

    @Override
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessiblePMGenericHotArea();
        }

        return accessibleContext;
    }

    protected class AccessiblePMGenericHotArea extends AccessibleContext implements AccessibleAction {

        @Override
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.CANVAS;
        }

        @Override
        public AccessibleStateSet getAccessibleStateSet() {
            return new AccessibleStateSet();
        }

        @Override
        public int getAccessibleIndexInParent() {
            return 0;
        }

        @Override
        public int getAccessibleChildrenCount() {
            return 0;
        }

        @Override
        public Accessible getAccessibleChild(int i) {
            return null;
        }

        @Override
        public Locale getLocale() throws IllegalComponentStateException {
            return Locale.getDefault();
        }

        @Override
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        @Override
        public int getAccessibleActionCount() {
            return 7;
        }

        @Override
        public String getAccessibleActionDescription(int i) {
            switch (i) {
                case 0:
                    return "Left Click";
                case 1:
                    return "Right Click";
                case 2:
                    return "Double Click";
                case 3:
                    return "Mouse Over";
                case 4:
                    return "Mouse Exit";
                case 5:
                    return "Mouse Down";
                case 6:
                    return "Mouse Up";
                default:
                    return null;
            }
        }

        @Override
        public boolean doAccessibleAction(int i) {
            String command;
            switch (i) {
                case 0:
                    command = PMHotArea.MOUSE_CLICK_LEFT;
                    break;
                case 1:
                    command = PMHotArea.MOUSE_CLICK_RIGHT;
                    break;
                case 2:
                    command = PMHotArea.MOUSE_DOUBLE_CLICK;
                    break;
                case 3:
                    command = PMHotArea.MOUSE_OVER;
                    break;
                case 4:
                    command = PMHotArea.MOUSE_EXIT;
                    break;
                case 5:
                    command = PMHotArea.MOUSE_DOWN;
                    break;
                case 6:
                    command = PMHotArea.MOUSE_UP;
                    break;
                default:
                    return false;
            }

            dispatchEvent(new ActionEvent(PMGenericHotArea.this, ActionEvent.ACTION_PERFORMED, command, 0));
            return true;
        }
        
    }
}