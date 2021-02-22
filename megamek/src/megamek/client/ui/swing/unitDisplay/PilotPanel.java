package megamek.client.ui.swing.unitDisplay;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import megamek.client.ui.Messages;
import megamek.client.ui.swing.widget.BackGroundDrawer;
import megamek.client.ui.swing.widget.PilotMapSet;
import megamek.common.CrewType;
import megamek.common.Entity;

/**
 * The pilot panel contains all the information about the pilot/crew of this
 * unit.
 */
class PilotPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 8284603003897415518L;

    private PilotMapSet pi;

    private int minTopMargin = 8;
    private int minLeftMargin = 8;
    private final JComboBox<String> cbCrewSlot = new JComboBox<>();
    private final JToggleButton btnSwapRoles = new JToggleButton();
    
    //We need to hold onto the entity in case the crew slot changes.
    private Entity entity;

    private transient Vector<BackGroundDrawer> bgDrawers;

    PilotPanel(final UnitDisplay unitDisplay) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(minTopMargin, minLeftMargin, minTopMargin, minLeftMargin);
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        add(cbCrewSlot, gbc);
        cbCrewSlot.addActionListener(e -> selectCrewSlot());
        
        btnSwapRoles.setToolTipText(Messages.getString("PilotMapSet.swapRoles.toolTip"));
        gbc.gridy = 1;
        add(btnSwapRoles, gbc);
        btnSwapRoles.addActionListener(e -> {
            if (null != entity) {
                entity.getCrew().setSwapConsoleRoles(btnSwapRoles.isSelected());
                unitDisplay.getClientGUI().getClient().sendUpdateEntity(entity);
                updateSwapButtonText();
            }
        });
        
        //Hack to keep controls at the top of the screen when the bottom one is not always visible.
        //There is probably a better way to do this.
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        add(new JLabel(), gbc);
        
        pi = new PilotMapSet(this);
        add(pi.getContent());

        bgDrawers = new Vector<>(pi.getBackgroundDrawers());
    }

    /**
     * updates fields for the specified mech
     */
    public void displayMech(Entity en) {
        entity = en;
        pi.setEntity(en);
        if (en.getCrew().getSlotCount() > 1) {
            cbCrewSlot.removeAllItems();
            for (int i = 0; i < en.getCrew().getSlotCount(); i++) {
                cbCrewSlot.addItem(en.getCrew().getCrewType().getRoleName(i));
            }
            cbCrewSlot.setVisible(true);
        } else {
            cbCrewSlot.setVisible(false);
        }
        if (entity.getCrew().getCrewType().equals(CrewType.COMMAND_CONSOLE)) {
            btnSwapRoles.setSelected(entity.getCrew().getSwapConsoleRoles());
            btnSwapRoles.setEnabled(entity.getCrew().isActive(0) && entity.getCrew().isActive(1));
            btnSwapRoles.setVisible(true);
            updateSwapButtonText();
        } else {
            btnSwapRoles.setVisible(false);
        }
    }
    
    private void selectCrewSlot() {
        if (null != entity && cbCrewSlot.getSelectedIndex() >= 0) {
            pi.setEntity(entity, cbCrewSlot.getSelectedIndex());
        }
    }
    
    private void updateSwapButtonText() {
        if (btnSwapRoles.isSelected()) {
            btnSwapRoles.setText(Messages.getString("PilotMapSet.keepRoles.text"));
        } else {
            btnSwapRoles.setText(Messages.getString("PilotMapSet.swapRoles.text"));
        }        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (BackGroundDrawer bgDrawer : bgDrawers) {
            bgDrawer.drawInto(g, getWidth(), getHeight());
        }
    }
    
}