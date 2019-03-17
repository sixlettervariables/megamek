package megamek.client.ui.swing.unitDisplay;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.List;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.swing.JLayeredPane;

import megamek.client.ui.swing.accessibility.AccessibleMegaMekRole;
import megamek.client.ui.swing.widget.BackGroundDrawer;
import megamek.client.ui.swing.widget.GeneralInfoMapSet;
import megamek.client.ui.swing.widget.PicMap;
import megamek.common.Entity;

/**
 * The movement panel contains all the buttons, readouts and gizmos relating
 * to moving around on the battlefield.
 */
public class MovementPanel extends PicMap implements SelectablePanel {

    private static final long serialVersionUID = 8284603003897415518L;

    private GeneralInfoMapSet gi;

    private int minTopMargin = 8;
    private int minLeftMargin = 8;

    private boolean isSelected;

    MovementPanel() {
        gi = new GeneralInfoMapSet(this);

        // layout choice panel
        GridBagLayout gridbag;
        GridBagConstraints c;

        gridbag = new GridBagLayout();
        c = new GridBagConstraints();
        setLayout(gridbag);

        JLayeredPane layeredPane = new JLayeredPane();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        gridbag.setConstraints(layeredPane, c);
        add(layeredPane);

        int layerIndex = 0;
        for (List<Component> layer : gi.getLayers()) {
            for (Component component : layer) {
                layeredPane.add(component, layerIndex);
            }
            layerIndex++;
        }
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
        if (null != r) {
            int dx = Math.round(((w - r.width) / 2));
            if (dx < minLeftMargin) {
                dx = minLeftMargin;
            }
            int dy = minTopMargin;
            setContentMargins(dx, dy, dx, dy);
        }
    }

    /**
     * updates fields for the specified mech
     */
    public void displayMech(Entity en) {
        gi.setEntity(en);
        onResize();
        update();
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        if (accessibleContext != null) {
            if (selected) {
                accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
                    null, AccessibleState.SELECTED);
            } else {
                accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
                    AccessibleState.SELECTED, null);
            }
        }
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
            return AccessibleMegaMekRole.MOVEMENT_PANEL;
        }

        @Override
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet states = super.getAccessibleStateSet();
            states.add(AccessibleState.SELECTABLE);
            if (isSelected) {
                states.add(AccessibleState.SELECTED);
            }
            return states;
        }
    }
}
