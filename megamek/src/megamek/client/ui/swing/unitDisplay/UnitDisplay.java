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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import megamek.client.event.MechDisplayEvent;
import megamek.client.event.MechDisplayListener;
import megamek.client.ui.swing.ClientGUI;
import megamek.client.ui.swing.util.CommandAction;
import megamek.client.ui.swing.util.KeyCommandBind;
import megamek.client.ui.swing.util.MegaMekController;
import megamek.common.Entity;
import megamek.common.annotations.Nullable;

/**
 * Displays the info for a mech. This is also a sort of interface for special
 * movement and firing actions.
 */
public class UnitDisplay extends JPanel {
    // buttons & gizmos for top level

    private static final long serialVersionUID = -2060993542227677984L;

    private JTabbedPane tabStrip;
    private MovementPanel mPan;
    private PilotPanel pPan;
    private ArmorPanel aPan;
    public WeaponPanel wPan;
    private SystemPanel sPan;
    private ExtraPanel ePan;
    private ClientGUI clientgui;

    private Entity currentlyDisplaying;
    private transient ArrayList<MechDisplayListener> eventListeners = new ArrayList<MechDisplayListener>();

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

        tabStrip = new JTabbedPane();

        mPan = new MovementPanel();
        tabStrip.addTab("General", mPan);
        pPan = new PilotPanel(this);
        tabStrip.addTab("Piloting", pPan);
        aPan = new ArmorPanel(clientgui != null ? clientgui.getClient().getGame() : null, this);
        tabStrip.addTab("Armor", aPan);
        wPan = new WeaponPanel(this);
        tabStrip.addTab("Weapons", wPan);
        sPan = new SystemPanel(this);
        tabStrip.addTab("Systems", sPan);
        ePan = new ExtraPanel(this);
        tabStrip.addTab("Extras", ePan);

        // layout main panel
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.REMAINDER;
        c.insets = new Insets(1, 1, 1, 1);

        c.weightx = 1.0;
        c.weighty = 1.0;

        c.gridwidth = GridBagConstraints.REMAINDER;
        addBag(tabStrip, c);

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
                selectTabCommandAction(ud, 0));

        // Display Pilot Tab
        controller.registerCommandAction(KeyCommandBind.UD_PILOT.cmd,
                selectTabCommandAction(ud, 1));

        // Display Armor Tab
        controller.registerCommandAction(KeyCommandBind.UD_ARMOR.cmd,
                selectTabCommandAction(ud, 2));

        // Display Weapons Tab
        controller.registerCommandAction(KeyCommandBind.UD_WEAPONS.cmd,
                selectTabCommandAction(ud, 3));

        // Display Systems Tab
        controller.registerCommandAction(KeyCommandBind.UD_SYSTEMS.cmd,
                selectTabCommandAction(ud, 4));

        // Display Extras Tab
        controller.registerCommandAction(KeyCommandBind.UD_EXTRAS.cmd,
                selectTabCommandAction(ud, 5));
    }

    private CommandAction selectTabCommandAction(final UnitDisplay ud, int tabIndex) {
        return new CommandAction() {
            @Override
            public boolean shouldPerformAction() {
                return ud.isVisible();
            }

            @Override
            public void performAction() {
                ud.setTab(tabIndex);
            }
        };
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
        if ("movement".equals(s)) { //$NON-NLS-1$
            setTab(0);
        } else if ("pilot".equals(s)) { //$NON-NLS-1$
            setTab(1);
        } else if ("armor".equals(s)) { //$NON-NLS-1$
            setTab(2);
        } else if ("weapons".equals(s)) { //$NON-NLS-1$
            setTab(3);
        } else if ("systems".equals(s)) { //$NON-NLS-1$
            setTab(4);
        } else if ("extras".equals(s)) { //$NON-NLS-1$
            setTab(5);
        }
    }
    
    /**
     * Used to force the display to the Systems tab, on a specific location
     * @param loc
     */
    public void showSpecificSystem(int loc) {
        setTab(4);
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
    
    private void setTab(int index) {
        tabStrip.setSelectedIndex(index);
    }
}
