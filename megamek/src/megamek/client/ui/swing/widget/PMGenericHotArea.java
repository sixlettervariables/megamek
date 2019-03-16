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
import javax.accessibility.AccessibleExtendedComponent;
import javax.accessibility.AccessibleKeyBinding;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import megamek.common.preference.PreferenceManager;

/**
 * Abstract class which defines common functionality for all hot areas such as
 * event handling and dispatching.
 */
public abstract class PMGenericHotArea extends JComponent implements PMHotArea, Accessible {

    private static final long serialVersionUID = 227946635057903366L;

    private ActionListener actionListener = null;
    private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
    private int mnemonic;

    protected AccessibleHotArea accessibleContext;

    protected PMGenericHotArea() {
        setFocusable(true);
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor c) {
        cursor = c;
    }

    public int getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(int m) {
        mnemonic = m;
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
            accessibleContext = new AccessibleHotArea();
        }
        return accessibleContext;
    }

    protected class AccessibleHotArea extends AccessibleJComponent implements AccessibleAction, AccessibleExtendedComponent {

        private static final long serialVersionUID = 4062089184365230374L;

        @Override
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        @Override
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PUSH_BUTTON;
        }

        @Override
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet states = new AccessibleStateSet();
            states.add(AccessibleState.ENABLED);
            return states;
        }

        @Override
        public int getAccessibleIndexInParent() {
            return -1;
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
            return PreferenceManager.getClientPreferences().getLocale();
        }

        @Override
        public int getAccessibleActionCount() {
            return 3;
        }

        @Override
        public String getAccessibleActionDescription(int i) {
            switch (i) {
            case 0:
                return PMHotArea.MOUSE_CLICK_LEFT;
            case 1:
                return PMHotArea.MOUSE_CLICK_RIGHT;
            case 2:
                return PMHotArea.MOUSE_DOUBLE_CLICK;
            default:
                return null;
            }
        }

        @Override
        public boolean doAccessibleAction(int i) {
            int modifiers = 0; // TODO: how do we support this?
            String command;
            ActionEvent ae;
            switch (i) {
            case 0:
                command = PMHotArea.MOUSE_CLICK_LEFT;
                ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, modifiers);
                PMGenericHotArea.this.dispatchEvent(ae);
                return true;
            case 1:
                command = PMHotArea.MOUSE_CLICK_RIGHT;
                ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, modifiers);
                PMGenericHotArea.this.dispatchEvent(ae);
                return true;
            case 2:
                command = PMHotArea.MOUSE_DOUBLE_CLICK;
                ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, modifiers);
                PMGenericHotArea.this.dispatchEvent(ae);
                return true;
            default:
                return false;
            }
        }

        /**
         * Returns key bindings associated with this object
         *
         * @return the key bindings, if supported, of the object;
         * otherwise, null
         * @see AccessibleKeyBinding
         */
        public AccessibleKeyBinding getAccessibleKeyBinding() {
            int mnemonic = PMGenericHotArea.this.getMnemonic();
            if (mnemonic == 0) {
                return null;
            }
            return new ButtonKeyBinding(mnemonic);
        }

        class ButtonKeyBinding implements AccessibleKeyBinding {
            int mnemonic;

            ButtonKeyBinding(int mnemonic) {
                this.mnemonic = mnemonic;
            }

            /**
             * Returns the number of key bindings for this object
             *
             * @return the zero-based number of key bindings for this object
             */
            public int getAccessibleKeyBindingCount() {
                return 1;
            }

            /**
             * Returns a key binding for this object.  The value returned is an
             * java.lang.Object which must be cast to appropriate type depending
             * on the underlying implementation of the key.  For example, if the
             * Object returned is a javax.swing.KeyStroke, the user of this
             * method should do the following:
             * <nf><code>
             * Component c = <get the component that has the key bindings>
             * AccessibleContext ac = c.getAccessibleContext();
             * AccessibleKeyBinding akb = ac.getAccessibleKeyBinding();
             * for (int i = 0; i < akb.getAccessibleKeyBindingCount(); i++) {
             *     Object o = akb.getAccessibleKeyBinding(i);
             *     if (o instanceof javax.swing.KeyStroke) {
             *         javax.swing.KeyStroke keyStroke = (javax.swing.KeyStroke)o;
             *         <do something with the key binding>
             *     }
             * }
             * </code></nf>
             *
             * @param i zero-based index of the key bindings
             * @return a javax.lang.Object which specifies the key binding
             * @exception IllegalArgumentException if the index is
             * out of bounds
             * @see #getAccessibleKeyBindingCount
             */
            public java.lang.Object getAccessibleKeyBinding(int i) {
                if (i != 0) {
                    throw new IllegalArgumentException();
                }
                return KeyStroke.getKeyStroke(mnemonic, 0);
            }
        }
    }
}
