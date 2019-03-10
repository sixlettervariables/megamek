/*
 * Copyright © 2013 Edward Cullen (eddy@obsessedcomputers.co.uk)
 */
/*
 * Copyright © 2013 Edward Cullen (eddy@obsessedcomputers.co.uk)
 */
package megamek.client.ui.swing.widget;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;

import megamek.client.ui.swing.accessibility.AccessibleMegaMekRole;
import megamek.client.ui.swing.unitDisplay.UnitDisplay;
import megamek.common.Configuration;
import megamek.common.util.MegaMekFile;

public class MechPanelTabStrip extends PicMap implements Accessible {

    /**
     *
     */
    private static final long serialVersionUID = -1282343469769007184L;

    private PMPicPolygonalArea[] tabs = new PMPicPolygonalArea[6];
    private static final Image[] idleImage = new Image[6];
    private static final Image[] activeImage = new Image[6];
    private Image idleCorner, selectedCorner;
    private int activeTab = 0;
    UnitDisplay md;

    public MechPanelTabStrip(UnitDisplay md) {
        super();
        this.md = md;
    }

    public void setTab(int i) {
        if (i > 5) {
            i = 5;
        }
        int oldTab = activeTab;
        activeTab = i;
        redrawImages();
        update();
        if (accessibleContext != null) {
            changeAccessibleSelection(
                oldTab,
                tabs[oldTab].getAccessibleContext().getAccessibleName(), 
                activeTab);
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        setImages();
        setAreas();
        setListeners();
        setAccessibility();
        update();
    }

    private void setAccessibility() {
        tabs[0].getAccessibleContext().setAccessibleName("General");
        tabs[1].getAccessibleContext().setAccessibleName("Pilot");
        tabs[2].getAccessibleContext().setAccessibleName("Armor");
        tabs[3].getAccessibleContext().setAccessibleName("Systems");
        tabs[4].getAccessibleContext().setAccessibleName("Weapons");
        tabs[5].getAccessibleContext().setAccessibleName("Extras");
    }

    private void setImages() {
        UnitDisplaySkinSpecification udSpec = SkinXMLHandler
                .getUnitDisplaySkin();
        MediaTracker mt = new MediaTracker(this);
        Toolkit tk = getToolkit();
        idleImage[0] = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getGeneralTabIdle()).toString());
        idleImage[1] = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getPilotTabIdle()).toString());
        idleImage[2] = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getArmorTabIdle()).toString());
        idleImage[3] = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getSystemsTabIdle()).toString());
        idleImage[4] = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getWeaponsTabIdle()).toString());
        idleImage[5] = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getExtrasTabIdle()).toString());
        activeImage[0] = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getGeneralTabActive()).toString());
        activeImage[1] = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getPilotTabActive()).toString());
        activeImage[2] = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getArmorTabActive()).toString());
        activeImage[3] = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getSystemsTabActive()).toString());
        activeImage[4] = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getWeaponsTabActive()).toString());
        activeImage[5] = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getExtraTabActive()).toString());
        idleCorner = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getCornerIdle()).toString());
        selectedCorner = tk.getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getCornerActive()).toString());

        // If we don't flush, we might have stale data
        idleCorner.flush();
        selectedCorner.flush();

        for (int i = 0; i < 6; i++) {
            // If we don't flush, we might have stale data
            idleImage[i].flush();
            activeImage[i].flush();
            mt.addImage(idleImage[i], 0);
            mt.addImage(activeImage[i], 0);
        }
        mt.addImage(idleCorner, 0);
        mt.addImage(selectedCorner, 0);
        try {
            mt.waitForAll();
        } catch (InterruptedException e) {
            System.out.println("TabStrip: Error while image loading."); //$NON-NLS-1$
        }
        if (mt.isErrorID(0)) {
            System.out.println("TabStrip: Could Not load Image."); //$NON-NLS-1$
        }

        for (int i = 0; i < 6; i++) {
            if (idleImage[i].getWidth(null) != activeImage[i].getWidth(null)) {
                System.out.println("TabStrip Warning: idleImage and "
                        + "activeImage do not match widths for image " + i);
            }
            if (idleImage[i].getHeight(null) != activeImage[i].getHeight(null)) {
                System.out.println("TabStrip Warning: idleImage and "
                        + "activeImage do not match heights for image " + i);
            }
        }
        if (idleCorner.getWidth(null) != selectedCorner.getWidth(null)) {
            System.out.println("TabStrip Warning: idleCorner and "
                    + "selectedCorner do not match widths!");
        }
        if (idleCorner.getHeight(null) != selectedCorner.getHeight(null)) {
            System.out.println("TabStrip Warning: idleCorner and "
                    + "selectedCorner do not match heights!");
        }
    }

    private void setAreas() {
        int cornerWidth = idleCorner.getWidth(null);

        for (int i = 0; i < idleImage.length; i++) {
            int width = idleImage[i].getWidth(null);
            int height = idleImage[i].getHeight(null);
            int[] pointsX = new int[] { 0, width, width + cornerWidth, 0 };
            int[] pointsY = new int[] { 0, 0, height, height };
            tabs[i] = new PMPicPolygonalArea(new Polygon(pointsX, pointsY, 4),
                    createImage(width, height));
        }

        int cumWidth = 0;
        for (int i = 0; i < idleImage.length; i++) {
            drawIdleImage(i);
            tabs[i].translate(cumWidth, 0);
            addElement(tabs[i]);
            cumWidth += idleImage[i].getWidth(null);
        }
    }

    private void setListeners() {
        tabs[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == PMHotArea.MOUSE_CLICK_LEFT) {
                    md.showPanel("movement"); //$NON-NLS-1$
                }
            }
        });
        tabs[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == PMHotArea.MOUSE_CLICK_LEFT) {
                    md.showPanel("pilot"); //$NON-NLS-1$
                }
            }
        });
        tabs[2].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == PMHotArea.MOUSE_CLICK_LEFT) {
                    md.showPanel("armor"); //$NON-NLS-1$
                }
            }
        });
        tabs[3].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == PMHotArea.MOUSE_CLICK_LEFT) {
                    md.showPanel("systems"); //$NON-NLS-1$
                }
            }
        });
        tabs[4].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == PMHotArea.MOUSE_CLICK_LEFT) {
                    md.showPanel("weapons"); //$NON-NLS-1$
                }
            }
        });
        tabs[5].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == PMHotArea.MOUSE_CLICK_LEFT) {
                    md.showPanel("extras"); //$NON-NLS-1$
                }
            }
        });

    }

    private void redrawImages() {
        for (int i = 0; i < 6; i++) {
            drawIdleImage(i);
        }
    }

    private void drawIdleImage(int tab) {
        if (tabs[tab] == null) {
            // hmm, display not initialized yet...
            return;
        }
        Graphics g = tabs[tab].getIdleImage().getGraphics();

        if (activeTab == tab) {
            g.drawImage(activeImage[tab], 0, 0, null);
        } else {
            g.drawImage(idleImage[tab], 0, 0, null);
            if ((tab - activeTab) == 1) {
                g.drawImage(selectedCorner, 0, 4, null);
            } else if (tab > 0) {
                g.drawImage(idleCorner, 0, 4, null);
            }
        }
        g.dispose();
    }

    @Override
    public void onResize() {
        //ignore
    }

    private void changeAccessibleSelection(int oldTab, String oldName, int newTab) {
        if (accessibleContext == null) {
            return;
        }

        switch (oldTab) {
        case 0:
            md.getMovementPanel().setSelected(false);
            break;
        default:
            break;
        }

        switch (newTab) {
        case 0:
            md.getMovementPanel().setSelected(true);
            break;
        default:
            break;
        }

        accessibleContext.firePropertyChange(
            AccessibleContext.ACCESSIBLE_NAME_PROPERTY,
            oldName,
            accessibleContext.getAccessibleName());
    }

    @Override
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleMechPanelTabStrip();
        }
        return accessibleContext;
    }

    protected class AccessibleMechPanelTabStrip extends AccessiblePicMap implements AccessibleSelection {
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

        @Override
        public String getAccessibleName() {
            String name = accessibleName;
            if (activeTab != -1) {
                name = tabs[activeTab].getAccessibleContext().getAccessibleName();
            }
            return name;
        }

        @Override
        public AccessibleRole getAccessibleRole() {
            return AccessibleMegaMekRole.PANEL_TAB_LIST;
        }

        @Override
        public int getAccessibleChildrenCount() {
            return MechPanelTabStrip.this.tabs.length;
        }

        @Override
        public Accessible getAccessibleChild(int i) {
            if (i < 0 || i >= MechPanelTabStrip.this.tabs.length) {
                return null;
            }
            return tabs[i];
        }

        @Override
        public int getAccessibleSelectionCount() {
            // Always one selected
            return 1;
        }

        @Override
        public Accessible getAccessibleSelection(int i) {
            if (i != 0) {
                return null;
            }
            switch (activeTab) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return tabs[i];
            default:
                return null;
            }
        }

        @Override
        public boolean isAccessibleChildSelected(int i) {
            return activeTab == i;
        }

        @Override
        public void addAccessibleSelection(int i) {
            MechPanelTabStrip.this.setTab(i);
        }

        @Override
        public void removeAccessibleSelection(int i) {
            // No-op
        }

        @Override
        public void clearAccessibleSelection() {
            // No-op
        }

        @Override
        public void selectAllAccessibleSelection() {
            // No-op
        }
    }
}
