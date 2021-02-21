package megamek.client.ui.swing.unitDisplay;

import java.awt.Rectangle;
import java.util.Enumeration;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;

import megamek.client.ui.swing.widget.BackGroundDrawer;
import megamek.client.ui.swing.widget.GeneralInfoMapSet;
import megamek.client.ui.swing.widget.PicMap;
import megamek.common.Entity;

/**
 * The movement panel contains all the buttons, readouts and gizmos relating
 * to moving around on the battlefield.
 */
class MovementPanel extends PicMap {

    /**
     *
     */
    private static final long serialVersionUID = 8284603003897415518L;

    private GeneralInfoMapSet gi;

    private int minTopMargin = 8;
    private int minLeftMargin = 8;

    MovementPanel() {
        gi = new GeneralInfoMapSet(this);
        addElement(gi.getContentGroup());
        Enumeration<BackGroundDrawer> iter = gi.getBackgroundDrawers()
                                               .elements();
        while (iter.hasMoreElements()) {
            addBgDrawer(iter.nextElement());
        }
        onResize();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        update();
    }

    @Override
    public void onResize() {
        int w = getSize().width;
        Rectangle r = getContentBounds();
        int dx = Math.round(((w - r.width) / 2));
        if (dx < minLeftMargin) {
            dx = minLeftMargin;
        }
        int dy = minTopMargin;
        setContentMargins(dx, dy, dx, dy);
    }

    /**
     * updates fields for the specified mech
     */
    public void displayMech(Entity en) {
        gi.setEntity(en);
        onResize();
        update();
    }

    @Override
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleMovementPanel();
        }

        return accessibleContext;
    }

    protected class AccessibleMovementPanel extends AccessiblePicMap {
        @Override
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PAGE_TAB;
        }
    }

}