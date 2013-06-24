/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.gotoalberto.bitcoinrock.game;

/**
 * @author gotoalberto
 */
public enum Choice {

    ROCK("ROCK"), PAPER("PAPER"), SCISSORS("SCISSORS"), LIZARD("LIZARD"), SPOCK(
            "SPOCK");

    private String choice;

    private Choice(final String choice) {
        this.choice = choice;
    }

    @Override
    public String toString() {
        return this.choice;
    }

    public static final int largestNameLength() {
        int length = 0;
        for (Choice choice : Choice.values()) {
            if (length < choice.toString().length()) {
                length = choice.toString().length();
            }
        }
        return length;
    }
}
