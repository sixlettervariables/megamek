/*
 * MegaMek - Copyright (C) 2003,2004 Ben Mazur (bmazur@sev.org)
 * Copyright © 2013 Edward Cullen (eddy@obsessedcomputers.co.uk)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 */
package megamek.client.ui.swing.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;

import javax.accessibility.AccessibleContext;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import megamek.client.ui.Messages;
import megamek.client.ui.swing.GUIPreferences;
import megamek.common.Configuration;
import megamek.common.Entity;
import megamek.common.Infantry;
import megamek.common.options.IOption;
import megamek.common.options.IOptionGroup;
import megamek.common.options.OptionsConstants;
import megamek.common.util.fileUtils.MegaMekFile;

/**
 * Set of elements to represent pilot information in MechDisplay
 */
public class PilotMapSet {

    private static String STAR3 = "***"; //$NON-NLS-1$
    private static int N_ADV = 35;
    private JComponent comp;
    private JPanel content = new JPanel();
    private PMPicArea portraitArea;
    private JLabel nameL, nickL, pilotL, gunneryL, gunneryLL, gunneryML, gunneryBL, toughBL, initBL, commandBL;
    private JValueLabel pilotR, gunneryR, gunneryLR, gunneryMR, gunneryBR, toughBR, initBR, commandBR, hitsR;
    private JValueLabel[] advantagesR;
    private Vector<BackGroundDrawer> bgDrawers = new Vector<BackGroundDrawer>();
    private static final Font FONT_VALUE = new Font("SansSerif", Font.PLAIN, //$NON-NLS-1$
            GUIPreferences.getInstance().getInt("AdvancedMechDisplayLargeFontSize"));
    private static final Font FONT_TITLE = new Font("SansSerif", Font.ITALIC, //$NON-NLS-1$
            GUIPreferences.getInstance().getInt("AdvancedMechDisplayLargeFontSize"));
    private int yCoord = 1;

    /**
     * This constructor have to be called only from addNotify() method
     */
    public PilotMapSet(JComponent c) {
        comp = c;

        content.setOpaque(false);
        content.setBackground(null);
        content.setLayout(new GridLayout(0, 2));

        setAreas();
        setBackGround();
    }

    // These two methods are used to vertically position new labels on the
    // display.
    private int getYCoord() {
        return (yCoord * 15) - 5;
    }

    private int getNewYCoord() {
        yCoord++;
        return getYCoord();
    }

    private void setAreas() {
        portraitArea = new PMPicArea(new BufferedImage(72, 72, BufferedImage.TYPE_BYTE_INDEXED));
        portraitArea.getAccessibleContext().setAccessibleDescription("A picture of the pilot.");
        content.add(portraitArea);

        yCoord = 6;
        Font fm = FONT_TITLE;
        nameL = createLabel(Messages.getString("GeneralInfoMapSet.LocOstLCT"), fm, 0, getYCoord()); //$NON-NLS-1$
        nameL.setForeground(Color.yellow);
        content.add(nameL);

        fm = FONT_VALUE;
        nickL = createLabel(Messages.getString("GeneralInfoMapSet.LocOstLCT"), fm, 0, getNewYCoord()); //$NON-NLS-1$
        nickL.getAccessibleContext().setAccessibleDescription("Nickname");
        content.add(nickL);

        hitsR = createValueLabel(STAR3, fm, 0, getNewYCoord());
        hitsR.setForeground(Color.RED);
        content.add(hitsR);
        getNewYCoord();

        pilotL = createLabel(Messages.getString("PilotMapSet.pilotLAntiMech"), fm, 0, getNewYCoord()); //$NON-NLS-1$
        content.add(pilotL);
        pilotR = createValueLabel(STAR3, fm, pilotL.getSize().width + 5, getYCoord());
        pilotL.setLabelFor(pilotR);
        content.add(pilotR);

        initBL = createLabel(Messages.getString("PilotMapSet.initBL"), fm, pilotL.getSize().width + 50, getYCoord()); //$NON-NLS-1$
        content.add(initBL);
        initBR = createValueLabel(STAR3, fm, pilotL.getSize().width + 50 + initBL.getSize().width + 25, getYCoord());
        initBL.setLabelFor(initBR);
        content.add(initBR);

        gunneryL = createLabel(Messages.getString("PilotMapSet.gunneryL"), fm, 0, getNewYCoord()); //$NON-NLS-1$
        content.add(gunneryL);
        gunneryR = createValueLabel(STAR3, fm, pilotL.getSize().width + 5, getYCoord());
        gunneryL.setLabelFor(gunneryR);
        content.add(gunneryR);

        commandBL = createLabel(Messages.getString("PilotMapSet.commandBL"), fm, pilotL.getSize().width + 50, //$NON-NLS-1$
                getYCoord());
        content.add(commandBL);
        commandBR = createValueLabel(STAR3, fm, pilotL.getSize().width + 50 + initBL.getSize().width + 25, getYCoord());
        commandBL.setLabelFor(commandBR);
        content.add(commandBR);

        gunneryLL = createLabel(Messages.getString("PilotMapSet.gunneryLL"), fm, 0, getYCoord()); //$NON-NLS-1$
        content.add(gunneryLL);
        gunneryLR = createValueLabel(STAR3, fm, pilotL.getSize().width + 25, getYCoord());
        gunneryLL.setLabelFor(gunneryLR);
        content.add(gunneryLR);

        gunneryML = createLabel(Messages.getString("PilotMapSet.gunneryML"), fm, 0, getNewYCoord()); //$NON-NLS-1$
        content.add(gunneryML);
        gunneryMR = createValueLabel(STAR3, fm, pilotL.getSize().width + 25, getYCoord());
        gunneryML.setLabelFor(gunneryMR);
        content.add(gunneryMR);

        toughBL = createLabel(Messages.getString("PilotMapSet.toughBL"), fm, pilotL.getSize().width + 50, getYCoord()); //$NON-NLS-1$
        content.add(toughBL);
        toughBR = createValueLabel(STAR3, fm, pilotL.getSize().width + 50 + initBL.getSize().width + 25, getYCoord());
        toughBL.setLabelFor(toughBR);
        content.add(toughBR);

        gunneryBL = createLabel(Messages.getString("PilotMapSet.gunneryBL"), fm, 0, getNewYCoord()); //$NON-NLS-1$
        content.add(gunneryBL);
        gunneryBR = createValueLabel(STAR3, fm, pilotL.getSize().width + 25, getYCoord());
        gunneryBL.setLabelFor(gunneryBR);
        content.add(gunneryBR);

        getNewYCoord();

        JPanel advantages = new JPanel();
        advantages.setOpaque(false);
        advantages.setBackground(null);
        content.add(advantages);

        advantagesR = new JValueLabel[N_ADV];
        for (int i = 0; i < advantagesR.length; i++) {
            advantagesR[i] = createValueLabel(Integer.toString(i), fm, 10, getNewYCoord());
            advantages.add(advantagesR[i]);
        }
        // DO NOT PLACE ANY MORE LABELS BELOW HERE. They will get
        // pushed off the bottom of the screen by the pilot advantage
        // labels. Why not just allocate the number of pilot advantage
        // labels required instead of a hard 24? Because we don't have
        // an entity at this point. Bleh.
    }

    /**
     * updates fields for the unit
     */
    public void setEntity(Entity en) {
        setEntity(en, 0);
    }
    
    public void setEntity(Entity en, int slot) {
        if (en instanceof Infantry) {
            pilotL.setText(Messages.getString("PilotMapSet.pilotLAntiMech"));
        } else {
            pilotL.setText(Messages.getString("PilotMapSet.pilotL"));
        }
        if (en.getCrew().isMissing(slot)) {
            nameL.setText(Messages.getString("PilotMapSet.empty"));
            nickL.setText("");
            nickL.setVisible(false);
            pilotL.setVisible(false);
            pilotR.setVisible(false);
            gunneryL.setVisible(false);
            gunneryR.setVisible(false);
            gunneryLL.setVisible(false);
            gunneryLR.setVisible(false);
            gunneryML.setVisible(false);
            gunneryMR.setVisible(false);
            gunneryBL.setVisible(false);
            gunneryBR.setVisible(false);
        } else {
            nameL.setText(en.getCrew().getName(slot));
            nickL.setText(en.getCrew().getNickname(slot));
            pilotR.setText(Integer.toString(en.getCrew().getPiloting(slot)));
            gunneryR.setText(Integer.toString(en.getCrew().getGunnery(slot)));
            pilotL.setVisible(true);
            pilotR.setVisible(true);

            portraitArea.setIdleImage(en.getCrew().getPortrait(slot).getImage());

            if ((en.getGame() != null) && en.getGame().getOptions().booleanOption(OptionsConstants.RPG_RPG_GUNNERY)) {
                gunneryLR.setText(Integer.toString(en.getCrew().getGunneryL(slot)));
                gunneryMR.setText(Integer.toString(en.getCrew().getGunneryM(slot)));
                gunneryBR.setText(Integer.toString(en.getCrew().getGunneryB(slot)));
                gunneryL.setVisible(false);
                gunneryR.setVisible(false);
                gunneryLL.setVisible(true);
                gunneryLR.setVisible(true);
                gunneryML.setVisible(true);
                gunneryMR.setVisible(true);
                gunneryBL.setVisible(true);
                gunneryBR.setVisible(true);
            } else {
                gunneryLL.setVisible(false);
                gunneryLR.setVisible(false);
                gunneryML.setVisible(false);
                gunneryMR.setVisible(false);
                gunneryBL.setVisible(false);
                gunneryBR.setVisible(false);
                gunneryL.setVisible(true);
                gunneryR.setVisible(true);
            }
        }

        if ((en.getGame() != null)
                && en.getGame().getOptions().booleanOption(OptionsConstants.RPG_TOUGHNESS)
                && !en.getCrew().isMissing(slot)) {
            toughBR.setText(Integer.toString(en.getCrew().getToughness(slot)));
            toughBL.setVisible(true);
            toughBR.setVisible(true);
        } else {
            toughBL.setVisible(false);
            toughBR.setVisible(false);
        }
        if ((en.getGame() != null)
                && en.getGame().getOptions().booleanOption(OptionsConstants.RPG_INDIVIDUAL_INITIATIVE)
                && !en.getCrew().isMissing(slot)) {
            initBR.setText(Integer.toString(en.getCrew().getInitBonus()));
            initBL.setVisible(true);
            initBR.setVisible(true);
        } else {
            initBL.setVisible(false);
            initBR.setVisible(false);
        }
        if ((en.getGame() != null) && en.getGame().getOptions().booleanOption(OptionsConstants.RPG_COMMAND_INIT)
                && !en.getCrew().isMissing(slot)) {
            commandBR.setText(Integer.toString(en.getCrew().getCommandBonus()));
            commandBL.setVisible(true);
            commandBR.setVisible(true);
        } else {
            commandBL.setVisible(false);
            commandBR.setVisible(false);
        }
        if (en.getCrew().isMissing(slot)) {
            hitsR.setText("");
            hitsR.setVisible(false);
        } else {
            hitsR.setText(en.getCrew().getStatusDesc(slot));
            hitsR.setVisible(true);
        }
        for (int i = 0; i < advantagesR.length; i++) {
            advantagesR[i].setText("");
            advantagesR[i].setVisible(false);
        }
        int i = 0;
        for (Enumeration<IOptionGroup> advGroups = en.getCrew().getOptions().getGroups(); advGroups
                .hasMoreElements();) {
            if (i >= advantagesR.length - 1) {
                advantagesR[advantagesR.length - 1].setVisible(true);
                advantagesR[advantagesR.length - 1].setText(Messages.getString("PilotMapSet.more"));
                break;
            }
            IOptionGroup advGroup = advGroups.nextElement();
            if (en.getCrew().countOptions(advGroup.getKey()) > 0) {
                advantagesR[i].setVisible(true);
                advantagesR[i++].setText(advGroup.getDisplayableName());
                for (Enumeration<IOption> advs = advGroup.getOptions(); advs.hasMoreElements();) {
                    if (i >= advantagesR.length - 1) {
                        advantagesR[advantagesR.length - 1].setVisible(true);
                        advantagesR[advantagesR.length - 1].setText("  " + Messages.getString("PilotMapSet.more"));
                        return;
                    }
                    IOption adv = advs.nextElement();
                    if ((adv != null) && adv.booleanValue()) {
                        advantagesR[i].setVisible(true);
                        advantagesR[i++].setText("  " + adv.getDisplayableNameWithValue());
                    }
                }
            }
        }
    }

    public JPanel getContent() {
        return content;
    }

    public Vector<BackGroundDrawer> getBackgroundDrawers() {
        return bgDrawers;
    }

    private void setBackGround() {
        UnitDisplaySkinSpecification udSpec = SkinXMLHandler.getUnitDisplaySkin();

        Image tile = comp.getToolkit()
                .getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getBackgroundTile()).toString());
        PMUtil.setImage(tile, comp);
        int b = BackGroundDrawer.TILING_BOTH;
        bgDrawers.addElement(new BackGroundDrawer(tile, b));

        b = BackGroundDrawer.TILING_HORIZONTAL | BackGroundDrawer.VALIGN_TOP;
        tile = comp.getToolkit().getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getTopLine()).toString());
        PMUtil.setImage(tile, comp);
        bgDrawers.addElement(new BackGroundDrawer(tile, b));

        b = BackGroundDrawer.TILING_HORIZONTAL | BackGroundDrawer.VALIGN_BOTTOM;
        tile = comp.getToolkit().getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getBottomLine()).toString());
        PMUtil.setImage(tile, comp);
        bgDrawers.addElement(new BackGroundDrawer(tile, b));

        b = BackGroundDrawer.TILING_VERTICAL | BackGroundDrawer.HALIGN_LEFT;
        tile = comp.getToolkit().getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getLeftLine()).toString());
        PMUtil.setImage(tile, comp);
        bgDrawers.addElement(new BackGroundDrawer(tile, b));

        b = BackGroundDrawer.TILING_VERTICAL | BackGroundDrawer.HALIGN_RIGHT;
        tile = comp.getToolkit().getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getRightLine()).toString());
        PMUtil.setImage(tile, comp);
        bgDrawers.addElement(new BackGroundDrawer(tile, b));

        b = BackGroundDrawer.NO_TILING | BackGroundDrawer.VALIGN_TOP | BackGroundDrawer.HALIGN_LEFT;
        tile = comp.getToolkit().getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getTopLeftCorner()).toString());
        PMUtil.setImage(tile, comp);
        bgDrawers.addElement(new BackGroundDrawer(tile, b));

        b = BackGroundDrawer.NO_TILING | BackGroundDrawer.VALIGN_BOTTOM | BackGroundDrawer.HALIGN_LEFT;
        tile = comp.getToolkit()
                .getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getBottomLeftCorner()).toString());
        PMUtil.setImage(tile, comp);
        bgDrawers.addElement(new BackGroundDrawer(tile, b));

        b = BackGroundDrawer.NO_TILING | BackGroundDrawer.VALIGN_TOP | BackGroundDrawer.HALIGN_RIGHT;
        tile = comp.getToolkit().getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getTopRightCorner()).toString());
        PMUtil.setImage(tile, comp);
        bgDrawers.addElement(new BackGroundDrawer(tile, b));

        b = BackGroundDrawer.NO_TILING | BackGroundDrawer.VALIGN_BOTTOM | BackGroundDrawer.HALIGN_RIGHT;
        tile = comp.getToolkit()
                .getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getBottomRightCorner()).toString());
        PMUtil.setImage(tile, comp);
        bgDrawers.addElement(new BackGroundDrawer(tile, b));
    }

    private JLabel createLabel(String s, Font f, int x, int y) {
        JLabel l = new JLabel(s);
        l.setFont(f);
        l.setForeground(Color.WHITE);
        l.setLocation(x, y);
        return l;
    }

    private JValueLabel createValueLabel(String s, Font f, int x, int y) {
        JValueLabel l = new JValueLabel(s);
        l.setFont(f);
        l.setForeground(Color.WHITE);
        l.setLocation(x, y);
        return l;
    }

    protected class JValueLabel extends JLabel {

        public JValueLabel(String s) {
            super(s);
            setFocusable(true);
        }

        @Override
        public AccessibleContext getAccessibleContext() {
            if (accessibleContext == null) {
                accessibleContext = new AccessibleJValueLabel();
            }

            return accessibleContext;
        }

        protected class AccessibleJValueLabel extends AccessibleJLabel {
            @Override
            public String getAccessibleName() {
                String name = super.getAccessibleName();
                return STAR3.equals(name) ? "N/A" : name;
            }
        }
    }

}