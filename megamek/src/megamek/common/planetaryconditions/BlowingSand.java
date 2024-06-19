/*
 * Copyright (c) 2024 - The MegaMek Team. All Rights Reserved.
 *
 * This file is part of MegaMek.
 *
 * MegaMek is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MegaMek is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MegaMek. If not, see <http://www.gnu.org/licenses/>.
 */
package megamek.common.planetaryconditions;

import megamek.common.Messages;

public enum BlowingSand {
    BLOWING_SAND_NONE("BLOWING_SAND_NONE", "PlanetaryConditions.DisplayableName.SandBlowing.false", "\uD83D\uDC41"),
    BLOWING_SAND("BLOWING_SAND", "PlanetaryConditions.DisplayableName.SandBlowing.true", "\uD83C\uDF2C");
    private final String externalId;
    private final String name;
    private final String indicator;

    BlowingSand(final String externalId, final String name, final String indicator) {
        this.externalId = externalId;
        this.name = name;
        this.indicator = indicator;
    }

    public String getIndicator() {
        return indicator;
    }

    public String getExternalId() {
        return externalId;
    }

    @Override
    public String toString() {
        return Messages.getString(name);
    }

    public boolean isBlowingSandNone() {
        return this == BLOWING_SAND_NONE;
    }

    public boolean isBlowingSand() {
        return this == BLOWING_SAND;
    }

    public static BlowingSand getBlowingSand(int i) {
        return BlowingSand.values()[i];
    }

    public static BlowingSand getBlowingSand(String s) {
        for (BlowingSand condition : BlowingSand.values()) {
            if (condition.getExternalId().equals(s)) {
                return condition;
            }
        }
        return BlowingSand.BLOWING_SAND_NONE;
    }
}
