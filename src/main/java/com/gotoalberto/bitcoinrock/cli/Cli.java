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
package com.gotoalberto.bitcoinrock.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.gotoalberto.bitcoinrock.flow.Game;

/**
 * @author gotoalberto
 */
public class Cli {

    private static final String CONFIG_FILE = "conf.properties";

    public static void main(String... args) throws Exception {
        buildGame().start();
    }

    private static final Game buildGame() {
        Properties prop = readProperties();
        return new Game(prop.getProperty("account"), Float.parseFloat(prop
                .getProperty("fee")), prop.getProperty("endpoint"),
                Integer.parseInt(prop.getProperty("port")),
                prop.getProperty("user"), prop.getProperty("password"));
    }

    private static final Properties readProperties() {
        Properties properties = new Properties();
        File file = new File(System.getProperty("java.class.path"));
        File dir = file.getAbsoluteFile().getParentFile();
        String propertiesPath = dir + System.getProperty("file.separator")
                + CONFIG_FILE;
        try {
            properties.load(new FileInputStream(propertiesPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format(
                    "File '%s' not found. Please create this file.",
                    propertiesPath));
        } catch (IOException e) {
            throw new RuntimeException(String.format(
                    "Error reading file '%s'.", propertiesPath));
        }
        return properties;
    }
}
