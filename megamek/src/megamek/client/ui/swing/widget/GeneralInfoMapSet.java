/*
* MegaMek -
* Copyright (C) 2003, 2004 Ben Mazur (bmazur@sev.org)
* Copyright (C) 2013 Edward Cullen (eddy@obsessedcomputers.co.uk)
* Copyright (C) 2018 The MegaMek Team
*
* This program is free software; you can redistribute it and/or modify it under
* the terms of the GNU General Public License as published by the Free Software
* Foundation; either version 2 of the License, or (at your option) any later
* version.
*
* This program is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
* details.
*/

package megamek.client.ui.swing.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.util.Enumeration;
import java.util.Vector;

import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import megamek.client.ui.Messages;
import megamek.client.ui.swing.GUIPreferences;
import megamek.common.Configuration;
import megamek.common.Crew;
import megamek.common.Entity;
import megamek.common.EntityMovementMode;
import megamek.common.EntityMovementType;
import megamek.common.GunEmplacement;
import megamek.common.IAero;
import megamek.common.Infantry;
import megamek.common.Jumpship;
import megamek.common.LandAirMech;
import megamek.common.Mech;
import megamek.common.QuadVee;
import megamek.common.Sensor;
import megamek.common.Tank;
import megamek.common.Warship;
import megamek.common.options.IOption;
import megamek.common.options.IOptionGroup;
import megamek.common.options.IOptions;
import megamek.common.options.OptionsConstants;
import megamek.common.options.PilotOptions;
import megamek.common.util.fileUtils.MegaMekFile;

/**
 * Set of elements to represent general unit information in MechDisplay
 */

public class GeneralInfoMapSet {

    private static String STAR3 = "***";
    private JComponent comp;
    private JPanel content = new JPanel();
    private JLabel mechTypeL0, mechTypeL1, statusL, pilotL, playerL, teamL, weightL, bvL, mpL0, mpL1, mpL2, mpL3, mpL4,
            curMoveL, heatL, movementTypeL, ejectL, elevationL, fuelL, curSensorsL, visualRangeL;
    private JLabel statusR, pilotR, playerR, teamR, weightR, bvR, mpR0, mpR1, mpR2, mpR3, mpR4, curMoveR, heatR,
            movementTypeR, ejectR, elevationR, fuelR, curSensorsR, visualRangeR;
    private PMMultiLineLabel quirksAndPartReps;
    private Vector<BackGroundDrawer> bgDrawers = new Vector<BackGroundDrawer>();
    private static final Font FONT_VALUE = new Font("SansSerif", Font.PLAIN,
            GUIPreferences.getInstance().getInt("AdvancedMechDisplayLargeFontSize"));
    private static final Font FONT_TITLE = new Font("SansSerif", Font.ITALIC,
            GUIPreferences.getInstance().getInt("AdvancedMechDisplayLargeFontSize"));
    private int yCoord = 1;

    /**
     * This constructor have to be called anly from addNotify() method
     */
    public GeneralInfoMapSet(JComponent c) {
        comp = c;
        setAreas();
        setBackGround();
    }

    public JPanel getContent() {
        return content;
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
        FontMetrics fm = comp.getFontMetrics(FONT_TITLE);

        content.setLayout(new GridLayout(0, 2));

        mechTypeL0 = createLabel(
                Messages.getString("GeneralInfoMapSet.LocOstLCT"), fm, 0, getYCoord());
        mechTypeL0.setForeground(Color.yellow);
        content.add(mechTypeL0);

        mechTypeL1 = createLabel(STAR3, fm, 0, getNewYCoord());
        mechTypeL0.setLabelFor(mechTypeL1);
        mechTypeL1.setForeground(Color.yellow);
        content.add(mechTypeL1);

        fm = comp.getFontMetrics(FONT_VALUE);

        pilotL = createLabel(
                Messages.getString("GeneralInfoMapSet.pilotL"), fm, 0, getNewYCoord());
        content.add(pilotL);

        pilotR = createLabel(
                Messages.getString("GeneralInfoMapSet.playerR"), fm, pilotL.getSize().width + 10, getYCoord());
        pilotL.setLabelFor(pilotR);
        content.add(pilotR);

        playerL = createLabel(
                Messages.getString("GeneralInfoMapSet.playerL"), fm, 0, getNewYCoord());
        content.add(playerL);

        playerR = createLabel(
                Messages.getString("GeneralInfoMapSet.playerR"), fm, playerL.getSize().width + 10, getYCoord());
        playerL.setLabelFor(playerR);
        content.add(playerR);

        teamL = createLabel(
                Messages.getString("GeneralInfoMapSet.teamL"), fm, 0, getNewYCoord());
        content.add(teamL);

        teamR = createLabel(
                Messages.getString("GeneralInfoMapSet.teamR"), fm, teamL.getSize().width + 10, getYCoord());
        teamL.setLabelFor(teamR);
        content.add(teamR);

        statusL = createLabel(
                Messages.getString("GeneralInfoMapSet.statusL"), fm, 0, getNewYCoord());
        content.add(statusL);

        statusR = createLabel(STAR3, fm, statusL.getSize().width + 10,
                getYCoord());
        statusL.setLabelFor(statusR);
        content.add(statusR);

        weightL = createLabel(
                Messages.getString("GeneralInfoMapSet.weightL"), fm, 0, getNewYCoord());
        content.add(weightL);

        weightR = createLabel(STAR3, fm, weightL.getSize().width + 10,
                getYCoord());
        weightL.setLabelFor(weightR);
        content.add(weightR);

        bvL = createLabel(
                Messages.getString("GeneralInfoMapSet.bvL"), fm, 0, getNewYCoord());
        content.add(bvL);

        bvR = createLabel(STAR3, fm, bvL.getSize().width + 10, getYCoord());
        bvL.setLabelFor(bvR);
        content.add(bvR);

        mpL0 = createLabel(
                Messages.getString("GeneralInfoMapSet.mpL0"), fm, 0, getNewYCoord());
        content.add(mpL0);

        mpR0 = createLabel("", fm, mpL0.getSize().width + 10, getYCoord());
        mpL0.setLabelFor(mpR0);
        content.add(mpR0);

        mpL1 = createLabel(
                Messages.getString("GeneralInfoMapSet.mpL1"), fm, 0, getNewYCoord());
        mpL1.setLocation(mpL0.getSize().width - mpL1.getSize().width, getYCoord());
        content.add(mpL1);

        mpR1 = createLabel(STAR3, fm, mpL0.getSize().width + 10, getYCoord());
        mpL1.setLabelFor(mpR1);
        content.add(mpR1);

        mpL2 = createLabel(
                Messages.getString("GeneralInfoMapSet.mpL2"), fm, 0, getNewYCoord());
        mpL2.setLocation(mpL0.getSize().width - mpL2.getSize().width, getYCoord());
        content.add(mpL2);

        mpR2 = createLabel(STAR3, fm, mpL0.getSize().width + 10, getYCoord());
        mpL2.setLabelFor(mpR2);
        content.add(mpR2);

        mpL3 = createLabel(
                Messages.getString("GeneralInfoMapSet.mpL3"), fm, 0, getNewYCoord());
        mpL3.setLocation(mpL0.getSize().width - mpL3.getSize().width, getYCoord());
        content.add(mpL3);

        mpR3 = createLabel(STAR3, fm, mpL0.getSize().width + 10, getYCoord());
        mpL3.setLabelFor(mpR3);
        content.add(mpR3);

        mpL4 = createLabel(
                Messages.getString("GeneralInfoMapSet.mpL4"), fm, 0, getNewYCoord());
        mpL4.setLocation(mpL0.getSize().width - mpL3.getSize().width, getYCoord());
        content.add(mpL4);

        mpR4 = createLabel("", fm, mpL0.getSize().width + 10, getYCoord());
        mpL4.setLabelFor(mpR4);
        content.add(mpR4);

        curMoveL = createLabel(
                Messages.getString("GeneralInfoMapSet.curMoveL"), fm, 0, getNewYCoord());
        content.add(curMoveL);

        curMoveR = createLabel(STAR3, fm, curMoveL.getSize().width + 10,
                getYCoord());
        curMoveL.setLabelFor(curMoveR);
        content.add(curMoveR);

        heatL = createLabel(
                Messages.getString("GeneralInfoMapSet.heatL"), fm, 0, getNewYCoord());
        content.add(heatL);

        heatR = createLabel(STAR3, fm, heatL.getSize().width + 10, getYCoord());
        heatL.setLabelFor(heatR);
        content.add(heatR);

        fuelL = createLabel(
                Messages.getString("GeneralInfoMapSet.fuelL"), fm, 0, getNewYCoord());
        content.add(fuelL);
        fuelR = createLabel(STAR3, fm, fuelL.getSize().width + 10, getYCoord());
        fuelL.setLabelFor(fuelR);
        content.add(fuelR);

        movementTypeL = createLabel(
                Messages.getString("GeneralInfoMapSet.movementTypeL"), fm, 0, getNewYCoord());
        content.add(movementTypeL);
        movementTypeR = createLabel(STAR3, fm,
                movementTypeL.getSize().width + 10, getYCoord());
        movementTypeL.setLabelFor(movementTypeR);
        content.add(movementTypeR);

        ejectL = createLabel(
                Messages.getString("GeneralInfoMapSet.ejectL"), fm, 0, getNewYCoord());
        content.add(ejectL);
        ejectR = createLabel(STAR3, fm, ejectL.getSize().width + 10,
                getYCoord());
        ejectL.setLabelFor(ejectR);
        content.add(ejectR);

        elevationL = createLabel(
                Messages.getString("GeneralInfoMapSet.elevationL"), fm, 0, getNewYCoord());
        content.add(elevationL);
        elevationR = createLabel(STAR3, fm, elevationL.getSize().width + 10,
                getYCoord());
        elevationL.setLabelFor(elevationR);
        content.add(elevationR);

        curSensorsL = createLabel(
                Messages.getString("GeneralInfoMapSet.currentSensorsL"), fm, 0, getNewYCoord());
        content.add(curSensorsL);
        curSensorsR = createLabel(STAR3, fm, curSensorsL.getSize().width + 10,
                getYCoord());
        curSensorsL.setLabelFor(curSensorsR);
        content.add(curSensorsR);

        visualRangeL = createLabel(
                Messages.getString("GeneralInfoMapSet.visualRangeL"), fm, 0, getNewYCoord());
        content.add(visualRangeL);
        visualRangeR = createLabel(STAR3, fm,
                visualRangeL.getSize().width + 10, getYCoord());
        visualRangeL.setLabelFor(visualRangeR);
        content.add(visualRangeR);

        getNewYCoord(); // skip a line for readability

        quirksAndPartReps = new PMMultiLineLabel(FONT_VALUE, Color.white);
        quirksAndPartReps.setLocation(0, getNewYCoord());
        content.add(quirksAndPartReps);
    }

    /**
     * updates fields for the unit
     */
    public void setEntity(Entity en) {

        String s = en.getShortName();
        mechTypeL1.setVisible(false);

        if (s.length() > GUIPreferences.getInstance().getInt(
                "AdvancedMechDisplayWrapLength")) {
            mechTypeL1.setForeground(Color.yellow);
            int i = s
                    .lastIndexOf(
                            " ", GUIPreferences.getInstance().getInt("AdvancedMechDisplayWrapLength"));
            mechTypeL0.setText(s.substring(0, i));
            mechTypeL1.setText(s.substring(i).trim());
            mechTypeL1.setVisible(true);
        } else {
            mechTypeL0.setText(s);
            mechTypeL1.setText("");
        }

        if (!en.isDesignValid()) {
            // If this is the case, we will just overwrite the name-overflow
            // area, since this info is more important.
            mechTypeL1.setForeground(Color.red);
            mechTypeL1.setText(Messages
                    .getString("GeneralInfoMapSet.invalidDesign"));
            mechTypeL1.setVisible(true);
        }

        statusR.setText(en.isProne() ? Messages
                .getString("GeneralInfoMapSet.prone") : Messages.getString("GeneralInfoMapSet.normal")); //$NON-NLS-2$
        if (en.getOwner() != null) {
            playerR.setText(en.getOwner().getName());
            if (en.getOwner().getTeam() == 0) {
                teamL.setVisible(false);
                teamR.setVisible(false);
            } else {
                teamL.setVisible(true);
                teamR.setText(Messages.getString("GeneralInfoMapSet.Team") + en.getOwner().getTeam());
                teamR.setVisible(true);
            }
        }

        if (en.getCrew() != null) {
            Crew c = en.getCrew();
            String pilotString = c.getDesc(c.getCurrentPilotIndex()) + " (";
            pilotString += c.getSkillsAsString(
                    en.getGame().getOptions().booleanOption(OptionsConstants.RPG_RPG_GUNNERY));
            int crewAdvCount = c.countOptions(PilotOptions.LVL3_ADVANTAGES);
            if (crewAdvCount > 0) {
                pilotString += ", +" + crewAdvCount;
            }
            pilotString += ")";
            pilotR.setText(pilotString);
        } else {
            pilotR.setText("");
        }

        if (en instanceof Infantry) {
            weightR.setText(Double.toString(en.getWeight()));
        } else {
            weightR.setText(Integer.toString((int) en.getWeight()));
        }

        ejectR.setText(Messages.getString("GeneralInfoMapSet.NA"));
        if (en instanceof Mech) {
            if (((Mech) en).isAutoEject()) {
                ejectR.setText(Messages
                        .getString("GeneralInfoMapSet.Operational"));
            } else {
                ejectR.setText(Messages
                        .getString("GeneralInfoMapSet.Disabled"));
            }
        }
        elevationR.setText(Integer.toString(en.getElevation()));
        if (en.isAirborne()) {
            elevationL.setText(Messages
                    .getString("GeneralInfoMapSet.altitudeL"));
            elevationR.setText(Integer.toString(en.getAltitude()));
        } else {
            elevationL.setText(Messages
                    .getString("GeneralInfoMapSet.elevationL"));
        }

        quirksAndPartReps.clear();

        if ((null != en.getGame())
                && en.getGame().getOptions().booleanOption(OptionsConstants.ADVANCED_STRATOPS_QUIRKS)) {
            addOptionsToList(en.getQuirks(), quirksAndPartReps);
        }

        if ((null != en.getGame())
                && en.getGame().getOptions().booleanOption(OptionsConstants.ADVANCED_STRATOPS_PARTIALREPAIRS)) {
            // skip a line for readability
            quirksAndPartReps.addString("");

            addOptionsToList(en.getPartialRepairs(), quirksAndPartReps);
        }

        if (en.mpUsed > 0) {
            mpR0.setText("(" + en.mpUsed + " used)"); //$NON-NLS-2$
        } else {
            mpR0.setText("");
        }
        mpR1.setText(Integer.toString(en.getWalkMP()));
        mpR2.setText(en.getRunMPasString());

        if ((en instanceof Jumpship) && !(en instanceof Warship)) {
            mpR2.setText(en.getRunMPasString() + " ("
                    + Double.toString(((Jumpship) en).getAccumulatedThrust())
                    + ")");
        }

        mpR3.setText(Integer.toString(en.getJumpMPWithTerrain()));

        if (en.hasUMU()) {
            mpL4.setVisible(true);
            mpR4.setVisible(true);
            mpR4.setText(Integer.toString(en.getActiveUMUCount()));
        } else if (en instanceof LandAirMech
                && en.getMovementMode() == EntityMovementMode.WIGE) {
            mpL4.setVisible(true);
            mpR4.setVisible(true);
            mpR1.setText(Integer.toString(((LandAirMech)en).getAirMechWalkMP()));
            mpR2.setText(Integer.toString(((LandAirMech)en).getAirMechRunMP()));
            mpR3.setText(Integer.toString(((LandAirMech)en).getAirMechCruiseMP()));
            mpR4.setText(Integer.toString(((LandAirMech)en).getAirMechFlankMP()));
        } else {
            mpL4.setVisible(false);
            mpR4.setVisible(false);
        }

        if (en.isAero()) {
            IAero a = (IAero) en;
            curMoveR.setText(Integer.toString(a.getCurrentVelocity())
                    + Messages.getString("GeneralInfoMapSet.velocity"));
            int currentFuel = a.getCurrentFuel();
            int safeThrust = en.getWalkMP();
            fuelR.setText(Integer.toString(a.getCurrentFuel()));
            if (currentFuel < (5 * safeThrust)) {
                fuelR.setForeground(Color.red);
            } else if (currentFuel < (10 * safeThrust)) {
                fuelR.setForeground(Color.yellow);
            } else {
                fuelR.setForeground(Color.white);
            }
        } else {
            curMoveR.setText(en.getMovementString(en.moved)
                    + (en.moved == EntityMovementType.MOVE_NONE ? "" : " " + en.delta_distance)); //$NON-NLS-2$
        }

        int heatCap = en.getHeatCapacity();
        int heatCapWater = en.getHeatCapacityWithWater();
        if(en.getCoolantFailureAmount() > 0) {
            heatCap -= en.getCoolantFailureAmount();
            heatCapWater -= en.getCoolantFailureAmount();
        }
        String heatCapacityStr = Integer.toString(heatCap);

        if (heatCap < heatCapWater) {
            heatCapacityStr = heatCap + " [" + heatCapWater + "]"; //$NON-NLS-2$
        }

        heatR.setText(Integer.toString(en.heat)
                + " (" + heatCapacityStr + " " + Messages.getString("GeneralInfoMapSet.capacity") + ")"); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        if (en instanceof Mech) {
            heatL.setVisible(true);
            heatR.setVisible(true);
        } else {
            heatL.setVisible(false);
            heatR.setVisible(false);
        }

        if (en instanceof Tank) {
            movementTypeL.setText(Messages.getString("GeneralInfoMapSet.movementTypeL"));
            movementTypeL.setVisible(true);
            movementTypeR.setText(Messages.getString("MovementType."
                    + en.getMovementModeAsString()));
            movementTypeR.setVisible(true);
        } else if (en instanceof QuadVee || en instanceof LandAirMech
                || (en instanceof Mech && ((Mech)en).hasTracks())) {
            movementTypeL.setText(Messages.getString("GeneralInfoMapSet.movementModeL"));
            if (en.getMovementMode() == EntityMovementMode.AERODYNE) {
                //Show "Fighter/AirMech" instead of "Aerodyne/WiGE"
                movementTypeR.setText(Messages.getString("BoardView1.ConversionMode.AERODYNE"));
            } else if (en.getMovementMode() == EntityMovementMode.WIGE) {
                movementTypeR.setText(Messages.getString("BoardView1.ConversionMode.WIGE"));
            } else {
                movementTypeR.setText(Messages.getString("MovementType."
                        + en.getMovementModeAsString()));
            }
            movementTypeL.setVisible(true);
            movementTypeR.setVisible(true);
        } else {
            movementTypeL.setVisible(false);
            movementTypeR.setVisible(false);
        }

        if ((en.getGame() != null) && en.getGame().getOptions().booleanOption(OptionsConstants.ADVANCED_TACOPS_SENSORS)) {
            curSensorsR.setVisible(true);
            visualRangeR.setVisible(true);
            curSensorsL.setVisible(true);
            visualRangeL.setVisible(true);
            curSensorsR.setText(en.getSensorDesc());
            visualRangeR.setText(Integer.toString(en.getGame()
                    .getPlanetaryConditions().getVisualRange(en, false)));
            //If using sensors, update our visual range display to the automatic detection range of the current sensor
            if (en.isSpaceborne() && en.getGame().getOptions().booleanOption(OptionsConstants.ADVAERORULES_STRATOPS_ADVANCED_SENSORS)) {
                int autoVisualRange = 0;
                //For squadrons. Default to the passive thermal/optical value used by component fighters
                if (en.hasETypeFlag(Entity.ETYPE_FIGHTER_SQUADRON)) {
                    autoVisualRange = Sensor.ASF_OPTICAL_FIRING_SOLUTION_RANGE;
                }
                if (en.getActiveSensor() != null) {
                    if (en.getActiveSensor().getType() == Sensor.TYPE_AERO_SENSOR) {
                        //required because the return on this from the method below is for ground maps
                        autoVisualRange = Sensor.ASF_RADAR_AUTOSPOT_RANGE;
                    } else {
                        autoVisualRange = (int) Math.ceil(en.getActiveSensor().getRangeByBracket() / 10.0);
                    }
                }
                visualRangeR.setText(Integer.toString(autoVisualRange));
            }
        } else {
            curSensorsR.setVisible(false);
            visualRangeR.setVisible(false);
            curSensorsR.setVisible(false);
            visualRangeR.setVisible(false);
        }

        if (en instanceof GunEmplacement) {
            weightL.setVisible(false);
            weightR.setVisible(false);
            mpL0.setVisible(false);
            mpR0.setVisible(false);
            mpL1.setVisible(false);
            mpR1.setVisible(false);
            mpL2.setVisible(false);
            mpR2.setVisible(false);
            mpL3.setVisible(false);
            mpR3.setVisible(false);
            curMoveL.setVisible(false);
            curMoveR.setVisible(false);
        } else {
            weightL.setVisible(true);
            weightR.setVisible(true);
            mpL0.setVisible(true);
            mpR0.setVisible(true);
            mpL1.setVisible(true);
            mpR1.setVisible(true);
            mpL2.setVisible(true);
            mpR2.setVisible(true);
            mpL3.setVisible(true);
            mpR3.setVisible(true);
            curMoveL.setVisible(true);
            curMoveR.setVisible(true);
        }

        if (en.isAero()) {
            heatL.setVisible(true);
            heatR.setVisible(true);
            curMoveL.setVisible(true);
            curMoveR.setVisible(true);
            fuelL.setVisible(true);
            fuelR.setVisible(true);
            mpL0.setText(Messages.getString("GeneralInfoMapSet.thrust"));
            mpL1.setText(Messages.getString("GeneralInfoMapSet.safe"));
            mpL2.setText(Messages.getString("GeneralInfoMapSet.over"));
            if (en.getMovementMode() == EntityMovementMode.WHEELED) {
                mpR1.setText(Integer.toString(((IAero)en).getCurrentThrust()));
                mpR2.setText(Integer.toString((int)Math.ceil(((IAero)en).getCurrentThrust() * 1.5)));
                mpL3.setText(Messages.getString("GeneralInfoMapSet.vehicle.mpL1"));
                mpR3.setText(Integer.toString(en.getWalkMP()));
                mpR3.setVisible(true);
                mpL3.setVisible(true);
            } else {
                mpR3.setVisible(false);
                mpL3.setVisible(false);
            }
        } else if (en instanceof Tank
                || (en instanceof QuadVee && en.getConversionMode() == QuadVee.CONV_MODE_VEHICLE)) {
            mpL0.setText(Messages.getString("GeneralInfoMapSet.mpL0"));
            mpL1.setText(Messages.getString("GeneralInfoMapSet.vehicle.mpL1"));
            mpL2.setText(Messages.getString("GeneralInfoMapSet.vehicle.mpL2"));
            mpL3.setText(Messages.getString("GeneralInfoMapSet.mpL3"));
            fuelL.setVisible(false);
            fuelR.setVisible(false);
        } else if (en instanceof LandAirMech
                && en.getMovementMode() == EntityMovementMode.WIGE) {
            mpL0.setText(Messages.getString("GeneralInfoMapSet.mpL0"));
            mpL1.setText(Messages.getString("GeneralInfoMapSet.mpL1"));
            mpL2.setText(Messages.getString("GeneralInfoMapSet.mpL2"));
            mpL3.setText(Messages.getString("GeneralInfoMapSet.vehicle.mpL1"));
            mpL4.setText(Messages.getString("GeneralInfoMapSet.vehicle.mpL2"));
            fuelL.setVisible(false);
            fuelR.setVisible(false);
        } else {
            mpL0.setText(Messages.getString("GeneralInfoMapSet.mpL0"));
            mpL1.setText(Messages.getString("GeneralInfoMapSet.mpL1"));
            mpL2.setText(Messages.getString("GeneralInfoMapSet.mpL2"));
            mpL3.setText(Messages.getString("GeneralInfoMapSet.mpL3"));
            fuelL.setVisible(false);
            fuelR.setVisible(false);
            if (en instanceof LandAirMech
                    && en.getMovementMode() == EntityMovementMode.WIGE) {
                mpL3.setText(Messages.getString("GeneralInfoMapSet.vehicle.mpL1"));
                mpL4.setText(Messages.getString("GeneralInfoMapSet.vehicle.mpL2"));
            } else {
                mpL3.setText(Messages.getString("GeneralInfoMapSet.mpL3"));
            }
        }
        if ((en.getGame() != null) && en.getGame().getBoard().inSpace()) {
            elevationL.setVisible(false);
            elevationR.setVisible(false);
        }
        bvL.setVisible(true);
        bvR.setVisible(true);
        bvR.setText(Integer.toString(en.calculateBattleValue()));

    }

    /**
     * Add all options from the given IOptions instance into an array of PMSimpleLabel elements.
     * @param optionsInstance IOptions instance
     * @param quirksAndPartReps
     */
    public void addOptionsToList(IOptions optionsInstance, PMMultiLineLabel quirksAndPartReps) {
        for (Enumeration<IOptionGroup> optionGroups = optionsInstance.getGroups(); optionGroups.hasMoreElements();) {
            IOptionGroup group = optionGroups.nextElement();
            if (optionsInstance.count(group.getKey()) > 0) {
                quirksAndPartReps.addString(group.getDisplayableName());

                for (Enumeration<IOption> options = group.getOptions(); options.hasMoreElements();) {
                    IOption option = options.nextElement();
                    if (option != null && option.booleanValue()) {
                        quirksAndPartReps.addString("  " + option.getDisplayableNameWithValue());
                    }
                }
            }
        }
    }

    public Vector<BackGroundDrawer> getBackgroundDrawers() {
        return bgDrawers;
    }

    private void setBackGround() {
        content.setOpaque(false);
        content.setBackground(null);

        UnitDisplaySkinSpecification udSpec = SkinXMLHandler
                .getUnitDisplaySkin();

        Image tile = comp.getToolkit().getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getBackgroundTile()).toString());
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

        b = BackGroundDrawer.NO_TILING | BackGroundDrawer.VALIGN_TOP
                | BackGroundDrawer.HALIGN_LEFT;
        tile = comp.getToolkit().getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getTopLeftCorner()).toString());
        PMUtil.setImage(tile, comp);
        bgDrawers.addElement(new BackGroundDrawer(tile, b));

        b = BackGroundDrawer.NO_TILING | BackGroundDrawer.VALIGN_BOTTOM
                | BackGroundDrawer.HALIGN_LEFT;
        tile = comp.getToolkit().getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getBottomLeftCorner()).toString());
        PMUtil.setImage(tile, comp);
        bgDrawers.addElement(new BackGroundDrawer(tile, b));

        b = BackGroundDrawer.NO_TILING | BackGroundDrawer.VALIGN_TOP
                | BackGroundDrawer.HALIGN_RIGHT;
        tile = comp.getToolkit().getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getTopRightCorner()).toString());
        PMUtil.setImage(tile, comp);
        bgDrawers.addElement(new BackGroundDrawer(tile, b));

        b = BackGroundDrawer.NO_TILING | BackGroundDrawer.VALIGN_BOTTOM
                | BackGroundDrawer.HALIGN_RIGHT;
        tile = comp.getToolkit().getImage(new MegaMekFile(Configuration.widgetsDir(), udSpec.getBottomRightCorner()).toString());
        PMUtil.setImage(tile, comp);
        bgDrawers.addElement(new BackGroundDrawer(tile, b));

    }

    private JLabel createLabel(String s, FontMetrics fm, int x, int y) {
        JLabel l = new JLabel(s);
        l.setFocusable(true);
        l.setLocation(x, y);
        l.setForeground(Color.WHITE);
        return l;
    }

}
