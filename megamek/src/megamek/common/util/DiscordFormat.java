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
package megamek.common.util;

public enum DiscordFormat {
    GRAY(30), RED(31), GREEN(32), YELLOW(33), BLUE(34), PINK(35), CYAN(36), WHITE(37),

    BOLD(1), UNDERLINE(4),

    RESET(0);

    private final int code;

    DiscordFormat(int code) {
        this.code = code;
    }

    public String format() {
        return "\u001b[" + code + 'm';
    }
}
