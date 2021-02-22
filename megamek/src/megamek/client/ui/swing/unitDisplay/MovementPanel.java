package megamek.client.ui.swing.unitDisplay;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

import megamek.client.ui.swing.widget.BackGroundDrawer;
import megamek.client.ui.swing.widget.GeneralInfoMapSet;
import megamek.common.Entity;

/**
 * The movement panel contains all the buttons, readouts and gizmos relating
 * to moving around on the battlefield.
 */
class MovementPanel extends JPanel {

    private static final long serialVersionUID = 8284603003897415518L;

    private transient GeneralInfoMapSet gi;
    private transient Vector<BackGroundDrawer> bgDrawers;

    MovementPanel() {
        gi = new GeneralInfoMapSet(this);
        add(gi.getContent());
        bgDrawers = new Vector<>(gi.getBackgroundDrawers());

        setOpaque(false);
        setBackground(Color.BLACK);
    }

    /**
     * updates fields for the specified mech
     */
    public void displayMech(Entity en) {
        gi.setEntity(en);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (BackGroundDrawer bgDrawer : bgDrawers) {
            bgDrawer.drawInto(g, getWidth(), getHeight());
        }
    }

}