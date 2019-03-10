package megamek.client.ui.swing.accessibility;

import java.util.Locale;

import javax.accessibility.AccessibleRole;

public class AccessibleMegaMekRole extends AccessibleRole {
    /**
     * An object that presents a unit's movement information.
     */
    public static final AccessibleRole MOVEMENT_PANEL = new AccessibleMegaMekRole("MovementPanelRole.description");

    /**
     * An object that presents a series of panels (or page tabs), one at a
     * time, through some mechanism provided by the object.  The most common
     * mechanism is a list of tabs at the top of the panel.  The children of
     * a panel tab list are all PicMap panels.
     */
    public static final AccessibleRole PANEL_TAB_LIST = new AccessibleMegaMekRole("PanelTabList.description");

    protected AccessibleMegaMekRole(String key) {
        super(key);
    }

    /**
     * Obtains the key as a localized string. If a localized string cannot be
     * found for the key, the locale independent key stored in the role will be
     * returned. This method is intended to be used only by subclasses so that
     * they can specify their own resource bundles which contain localized
     * strings for their keys.
     *
     * @param  resourceBundleName the name of the resource bundle to use for
     *         lookup
     * @param  locale the locale for which to obtain a localized string
     * @return a localized string for the key
     */
    @Override
    protected String toDisplayString(String resourceBundleName,
                                    Locale locale) {
        return AccessibilityMessages.getString(key);
    }
}
