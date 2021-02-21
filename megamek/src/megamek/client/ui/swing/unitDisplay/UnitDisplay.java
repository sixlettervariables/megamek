/**
 * MegaMek - Copyright (C) 2000,2001,2002,2003,2004,2006 Ben Mazur (bmazur@sev.org)
 * Copyright © 2013 Edward Cullen (eddy@obsessedcomputers.co.uk)
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

package megamek.client.ui.swing.unitDisplay;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import megamek.client.event.MechDisplayEvent;
import megamek.client.event.MechDisplayListener;
import megamek.client.ui.swing.ClientGUI;
import megamek.client.ui.swing.util.CommandAction;
import megamek.client.ui.swing.util.KeyCommandBind;
import megamek.client.ui.swing.util.MegaMekController;
import megamek.client.ui.swing.widget.MechPanelTabStrip;
import megamek.client.ui.swing.widget.PicMap;
import megamek.common.Entity;
import megamek.common.annotations.Nullable;

/**
 * Displays the info for a mech. This is also a sort of interface for special
 * movement and firing actions.
 */
public class UnitDisplay extends JPanel {
    // buttons & gizmos for top level

    /**
     *
     */
    private static final long serialVersionUID = -2060993542227677984L;

    private MechPanelTabStrip tabStrip;

    private JPanel displayP;
    private MovementPanel mPan;
    private PilotPanel pPan;
    private ArmorPanel aPan;
    public WeaponPanel wPan;
    private SystemPanel sPan;
    private ExtraPanel ePan;
    private ClientGUI clientgui;

    private Entity currentlyDisplaying;
    private ArrayList<MechDisplayListener> eventListeners = new ArrayList<MechDisplayListener>();

    /**
     * Creates and lays out a new mech display.
     * 
     * @param clientgui
     *            The ClientGUI for the GUI that is creating this UnitDisplay.
     *            This could be null, if there is no ClientGUI, such as with
     *            MekWars.
     */
    public UnitDisplay(@Nullable ClientGUI clientgui) {
        this(clientgui, null);
    }
        
    public UnitDisplay(@Nullable ClientGUI clientgui,
            @Nullable MegaMekController controller) {
        super(new GridBagLayout());
        this.clientgui = clientgui;

        tabStrip = new MechPanelTabStrip(this);

        displayP = new JPanel(new CardLayout());
        mPan = new MovementPanel();
        mPan.getAccessibleContext().setAccessibleName("General");
        displayP.add("movement", mPan); //$NON-NLS-1$
        pPan = new PilotPanel(this);
        pPan.getAccessibleContext().setAccessibleName("Piloting");
        displayP.add("pilot", pPan); //$NON-NLS-1$
        aPan = new ArmorPanel(clientgui != null ? clientgui.getClient().getGame() : null, this);
        aPan.getAccessibleContext().setAccessibleName("Armor");
        displayP.add("armor", aPan); //$NON-NLS-1$
        wPan = new WeaponPanel(this);
        wPan.getAccessibleContext().setAccessibleName("Weapons");
        displayP.add("weapons", wPan); //$NON-NLS-1$
        sPan = new SystemPanel(this);
        sPan.getAccessibleContext().setAccessibleName("Systems");
        displayP.add("systems", sPan); //$NON-NLS-1$
        ePan = new ExtraPanel(this);
        ePan.getAccessibleContext().setAccessibleName("Extras");
        displayP.add("extras", ePan); //$NON-NLS-1$

        // layout main panel
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4, 1, 0, 1);

        c.weightx = 1.0;
        c.weighty = 0.0;

        c.gridwidth = GridBagConstraints.REMAINDER;
        addBag(tabStrip, c);
        c.insets = new Insets(0, 1, 1, 1);
        c.weighty = 1.0;
        addBag(displayP, c);

        ((CardLayout) displayP.getLayout()).show(displayP, "movement"); //$NON-NLS-1$
        setTab(0, false);
        
        if (controller != null) {
            registerKeyboardCommands(this, controller);
        }
    }

    /**
     * Register the keyboard commands that the UnitDisplay should process
     *
     * @param ud
     * @param controller
     */
    private void registerKeyboardCommands(final UnitDisplay ud,
            final MegaMekController controller) {
        // Display General Tab
        controller.registerCommandAction(KeyCommandBind.UD_GENERAL.cmd,
                new CommandAction() {

                    @Override
                    public boolean shouldPerformAction() {
                        if (ud.isVisible()) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                    @Override
                    public void performAction() {
                        ((CardLayout) displayP.getLayout()).show(displayP,
                                "movement");
                        setTab(0, true);
                    }

                });

        // Display Pilot Tab
        controller.registerCommandAction(KeyCommandBind.UD_PILOT.cmd,
                new CommandAction() {

                    @Override
                    public boolean shouldPerformAction() {
                        if (ud.isVisible()) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                    @Override
                    public void performAction() {
                        ((CardLayout) displayP.getLayout()).show(displayP,
                                "pilot");
                        setTab(1, true);
                    }

                });

        // Display Armor Tab
        controller.registerCommandAction(KeyCommandBind.UD_ARMOR.cmd,
                new CommandAction() {

                    @Override
                    public boolean shouldPerformAction() {
                        if (ud.isVisible()) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                    @Override
                    public void performAction() {
                        ((CardLayout) displayP.getLayout()).show(displayP,
                                "armor");
                        setTab(2, true);
                    }

                });

        // Display Systems Tab
        controller.registerCommandAction(KeyCommandBind.UD_SYSTEMS.cmd,
                new CommandAction() {

                    @Override
                    public boolean shouldPerformAction() {
                        if (ud.isVisible()) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                    @Override
                    public void performAction() {
                        ((CardLayout) displayP.getLayout()).show(displayP,
                                "systems");
                        setTab(3, true);
                    }

                });

        // Display Weapons Tab
        controller.registerCommandAction(KeyCommandBind.UD_WEAPONS.cmd,
                new CommandAction() {

                    @Override
                    public boolean shouldPerformAction() {
                        if (ud.isVisible()) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                    @Override
                    public void performAction() {
                        ((CardLayout) displayP.getLayout()).show(displayP,
                                "weapons");
                        setTab(4, true);
                    }

                });

        // Display Extras Tab
        controller.registerCommandAction(KeyCommandBind.UD_EXTRAS.cmd,
                new CommandAction() {

                    @Override
                    public boolean shouldPerformAction() {
                        if (ud.isVisible()) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                    @Override
                    public void performAction() {
                        ((CardLayout) displayP.getLayout()).show(displayP,
                                "extras");
                        setTab(5, true);
                    }

                });
    }

    /**
     *
     */
    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
            int condition, boolean pressed) {
        if (!e.isConsumed()) {
            return super.processKeyBinding(ks, e, condition, pressed);
        } else {
            return true;
        }
    }

    /**
     *
     * @param comp
     * @param c
     */
    private void addBag(JComponent comp, GridBagConstraints c) {
        ((GridBagLayout) getLayout()).setConstraints(comp, c);
        add(comp);
    }

    /**
     * Displays the specified entity in the panel.
     */
    public void displayEntity(Entity en) {

        String enName = en.getShortName();
        switch (en.getDamageLevel()) {
            case Entity.DMG_CRIPPLED:
                enName += " [CRIPPLED]";
                break;
            case Entity.DMG_HEAVY:
                enName += " [HEAVY DMG]";
                break;
            case Entity.DMG_MODERATE:
                enName += " [MODERATE DMG]";
                break;
            case Entity.DMG_LIGHT:
                enName += " [LIGHT DMG]";
                break;
            default:
                enName += " [UNDAMAGED]";
        }
        if (clientgui != null) {
            clientgui.mechW.setTitle(enName);
        }

        currentlyDisplaying = en;

        mPan.displayMech(en);
        pPan.displayMech(en);
        aPan.displayMech(en);
        wPan.displayMech(en);
        sPan.displayMech(en);
        ePan.displayMech(en);
    }

    private int getTabCount() {
        return 6;
    }

    private PicMap getTab(int i) {
        switch (i) {
            case 0:
                return mPan;
            case 1:
                return pPan;
            case 2:
                return aPan;
            case 3:
                return wPan;
            case 4:
                return sPan;
            case 5:
                return ePan;
            default:
                return null;
        }
    }

    /**
     * Returns the entity we'return currently displaying
     */

    public Entity getCurrentEntity() {
        return currentlyDisplaying;
    }

    /**
     * Changes to the specified panel.
     */
    public void showPanel(String s) {
        ((CardLayout) displayP.getLayout()).show(displayP, s);
        if ("movement".equals(s)) { //$NON-NLS-1$
            setTab(0, true);
        } else if ("pilot".equals(s)) { //$NON-NLS-1$
            setTab(1, true);
        } else if ("armor".equals(s)) { //$NON-NLS-1$
            setTab(2, true);
        } else if ("weapons".equals(s)) { //$NON-NLS-1$
            setTab(4, true);
        } else if ("systems".equals(s)) { //$NON-NLS-1$
            setTab(3, true);
        } else if ("extras".equals(s)) { //$NON-NLS-1$
            setTab(5, true);
        }
    }
    
    /**
     * Used to force the display to the Systems tab, on a specific location
     * @param loc
     */
    public void showSpecificSystem(int loc) {
        ((CardLayout) displayP.getLayout()).show(displayP, "systems");
        setTab(3, true);
        sPan.selectLocation(loc);
    }

    /**
     * Adds the specified mech display listener to receive events from this
     * view.
     *
     * @param listener the listener.
     */
    public void addMechDisplayListener(MechDisplayListener listener) {
        eventListeners.add(listener);
    }

    /**
     * Notifies attached listeners of the event.
     *
     * @param event the mech display event.
     */
    void processMechDisplayEvent(MechDisplayEvent event) {
        for (int i = 0; i < eventListeners.size(); i++) {
            MechDisplayListener lis = eventListeners.get(i);
            switch (event.getType()) {
                case MechDisplayEvent.WEAPON_SELECTED:
                    lis.weaponSelected(event);
                    break;
                default:
                    System.err.println("unknown event " + event.getType()
                            + " in processMechDisplayEvent");
                    break;
            }
        }
    }

    /**
     * Returns the UnitDisplay's ClientGUI reference, which can be null.
     * @return
     */
    @Nullable
    public ClientGUI getClientGUI() {
        return clientgui;
    }
    
    private void setTab(int index, boolean doAccessibleChanges) {
        int oldIndex = tabStrip.getSelectedIndex();
        PicMap oldPage = null, newPage = null;
        String oldName = null;

        doAccessibleChanges = doAccessibleChanges && (oldIndex != index);

        if (doAccessibleChanges) {
            if (accessibleContext != null) {
                oldName = accessibleContext.getAccessibleName();
            }

            if (oldIndex >= 0) {
                oldPage = getTab(oldIndex);
            }

            if (index >= 0) {
                newPage = getTab(index);
            }
        }

        tabStrip.setTab(index);

        if (doAccessibleChanges) {
            changeAccessibleSelection(oldPage, oldName, newPage);
        }
    }

    private void changeAccessibleSelection(PicMap oldPage, String oldName, PicMap newPage) {
        if (accessibleContext == null) {
            return;
        }

        /* if (oldPage != null) {
            oldPage.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
                                       AccessibleState.SELECTED, null);
        }

        if (newPage != null) {
            newPage.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
                                       null, AccessibleState.SELECTED);
        } */

        accessibleContext.firePropertyChange(
            AccessibleContext.ACCESSIBLE_NAME_PROPERTY,
            oldName,
            accessibleContext.getAccessibleName());
    }

    @Override
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleUnitDisplay();
        }

        return accessibleContext;
    }

    protected class AccessibleUnitDisplay extends AccessibleJComponent implements AccessibleSelection {
        private static final long serialVersionUID = -7125915249048458628L;

        /**
         * Returns the accessible name of this object, or {@code null} if
         * there is no accessible name.
         *
         * @return the accessible name of this object, nor {@code null}.
         * @since 1.6
         */
        @Override
        public String getAccessibleName() {
            if (accessibleName != null) {
                return accessibleName;
            }

            String cp = (String)getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY);

            if (cp != null) {
                return cp;
            }

            int index = tabStrip.getSelectedIndex();

            if (index >= 0) {
                return getTab(index).getAccessibleContext().getAccessibleName();
            }

            return super.getAccessibleName();
        }

        /**
         * Get the role of this object.
         *
         * @return an instance of AccessibleRole describing the role of the object
         */
        @Override
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PAGE_TAB_LIST;
        }

        /**
         * Returns the number of accessible children in the object.
         *
         * @return the number of accessible children in the object.
         */
        @Override
        public int getAccessibleChildrenCount() {
            return getTabCount();
        }

        /**
         * Return the specified Accessible child of the object.
         *
         * @param i zero-based index of child
         * @return the Accessible child of the object
         * @exception IllegalArgumentException if index is out of bounds
         */
        @Override
        public Accessible getAccessibleChild(int i) {
            if (i < 0 || i >= getTabCount()) {
                return null;
            }
            return getTab(i);
        }

        /**
         * Gets the <code>AccessibleSelection</code> associated with
         * this object.  In the implementation of the Java
         * Accessibility API for this class,
         * returns this object, which is responsible for implementing the
         * <code>AccessibleSelection</code> interface on behalf of itself.
         *
         * @return this object
         */
        @Override
        public AccessibleSelection getAccessibleSelection() {
            return this;
        }

        public int getAccessibleSelectionCount() {
            return 1;
        }

        public Accessible getAccessibleSelection(int i) {
            int index = tabStrip.getSelectedIndex();
            if (index == -1) {
                return null;
            }
            return getTab(index);
        }

        public boolean isAccessibleChildSelected(int i) {
            return (i == tabStrip.getSelectedIndex());
        }

        public void addAccessibleSelection(int i) {
           tabStrip.setTab(i);
        }

        public void removeAccessibleSelection(int i) {
           // can't do
        }

        public void clearAccessibleSelection() {
           // can't do
        }

        public void selectAllAccessibleSelection() {
           // can't do
        }
    }
}
