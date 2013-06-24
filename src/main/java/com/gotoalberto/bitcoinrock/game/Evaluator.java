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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * @author gotoalberto
 */
public class Evaluator {

    private final Map<Choice, List<Choice>> winnerMap;

    public Evaluator() {
        this.winnerMap = new HashMap<Choice, List<Choice>>();
        for (Choice choice : Choice.values()) {
            List<Choice> winsTo = new ArrayList<Choice>();
            if (choice == Choice.LIZARD) {
                winsTo.add(Choice.SPOCK);
                winsTo.add(Choice.PAPER);
            } else if (choice == Choice.PAPER) {
                winsTo.add(Choice.ROCK);
                winsTo.add(Choice.SPOCK);
            } else if (choice == Choice.ROCK) {
                winsTo.add(Choice.SCISSORS);
                winsTo.add(Choice.LIZARD);
            } else if (choice == Choice.SCISSORS) {
                winsTo.add(Choice.LIZARD);
                winsTo.add(Choice.PAPER);
            } else if (choice == Choice.SPOCK) {
                winsTo.add(Choice.ROCK);
                winsTo.add(Choice.SCISSORS);
            }
            this.winnerMap.put(choice, winsTo);
        }
    }

    /**
     * @param first
     *            Choice
     * @param second
     *            Choice
     * @return Winner Choice or null when even
     */
    @Nullable
    public Choice winnerFrom(Choice first, Choice second) {
        if (this.winnerMap.get(first).contains(second)) {
            return first;
        } else if (this.winnerMap.get(second).contains(first)) {
            return second;
        } else {
            return null;
        }
    }
}
