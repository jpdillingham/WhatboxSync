/****************************************************************************
 *
 * ConfigurationTest.java
 *
 * Tests the Configuration class.
 *
 ***************************************************************************
 *
 * Copyright (C) 2016 JP Dillingham (jp@dillingham.ws)
 *
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
 *
 ****************************************************************************/

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests the Configuration class.
 */
public class ConfigurationTest {
    /**
     * The logger for this class.
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * Configure the logger.
     */
    @Before
    public void ConfigureLogging() {
        ConsoleAppender console = new ConsoleAppender();
        console.setLayout(new PatternLayout("%d{yyyy-MM-dd' 'HH:mm:ss.SSS} [%-5p] [%c] - %m%n"));
        console.setThreshold(Level.INFO);
        console.activateOptions();
        org.apache.log4j.Logger.getRootLogger().addAppender(console);
    }

    /**
     * Constructs an instance of Configuration and tests all accessors.
     */
    @Test
    public void testConfiguration() {
        Configuration test = new Configuration("server", 1, "user", "password", 1, "remote", "local");

        assertEquals(test.getServer(), "server");
        assertEquals(test.getPort(), (Integer)1);
        assertEquals(test.getUsername(), "user");
        assertEquals(test.getPassword(), "password");
        assertEquals(test.getInterval(), (Integer)1);
        assertEquals(test.getRemoteDirectory(), "remote");
        assertEquals(test.getLocalDirectory(), "local");
        assertEquals(test.isValid(), true);
    }

    /**
     * Constructs an instance of Configuration with a blank server value.
     */
    @Test
    public void testBlankServer() {
        Configuration test = new Configuration("", 1, "user", "password", 1, "remote", "local");

        assertEquals(test.isValid(), false);
    }

    /**
     * Constructs an instance of Configuration with a missing server value.
     */
    @Test
    public void testMissingServer() {
        Configuration test = new Configuration(null, 1, "user", "password", 1, "remote", "local");

        assertEquals(test.isValid(), false);
    }

    /**
     * Constructs an instance of Configuration with an invalid server port.
     */
    @Test
    public void testInvalidPort() {
        Configuration test = new Configuration("server", 0, "user", "password", 1, "remote", "local");

        assertEquals(test.isValid(), false);
    }

    /**
     * Constructs an instance of Configuration with a missing server port.
     */
    @Test
    public void testMissingPort() {
        Configuration test = new Configuration("server", null, "user", "password", 1, "remote", "local");

        assertEquals(test.isValid(), false);
    }

    /**
     * Constructs an instance of Configuration with an invalid username.
     */
    @Test
    public void testInvalidUsername() {
        Configuration test = new Configuration("server", 1, "", "password", 1, "remote", "local");

        assertEquals(test.isValid(), false);
    }

    /**
     * Constructs an instance of Configuration with a missing username.
     */
    @Test
    public void testMissingUsername() {
        Configuration test = new Configuration("server", 1, null, "password", 1, "remote", "local");

        assertEquals(test.isValid(), false);
    }

    /**
     * Constructs an instance of Configuration with an invalid password.
     */
    @Test
    public void testInvalidPassword() {
        Configuration test = new Configuration("server", 0, "user", null, 1, "remote", "local");

        assertEquals(test.isValid(), false);
    }

    /**
     * Constructs an instance of Configuration with an invalid interval.
     */
    @Test
    public void testInvalidInterval() {
        Configuration test = new Configuration("server", 1, "user", "password", -1, "remote", "local");

        assertEquals(test.isValid(), false);
    }

    /**
     * Constructs an instance of Configuration with a missing interval.
     */
    @Test
    public void testMissingInterval() {
        Configuration test = new Configuration("server", 1, "user", "password", null, "remote", "local");

        assertEquals(test.isValid(), false);
    }

    /**
     * Constructs an instance of Configuration with an invalid remote directory.
     */
    @Test
    public void testInvalidRemoteDirectory() {
        Configuration test = new Configuration("server", 1, "user", "password", 1, "", "local");

        assertEquals(test.isValid(), false);
    }

    /**
     * Constructs an instance of Configuration with a missing remote directory.
     */
    @Test
    public void testMissingRemoteDirectory() {
        Configuration test = new Configuration("server", 1, "user", "password", 1, null, "local");

        assertEquals(test.isValid(), false);
    }

    /**
     * Constructs an instance of Configuration with an invalid local directory.
     */
    @Test
    public void testInvalidLocalDirectory() {
        Configuration test = new Configuration("server", 1, "user", "password", 1, "remote", "");

        assertEquals(test.isValid(), false);
    }

    /**
     * Constructs an instance of Configuration with a missing local directory.
     */
    @Test
    public void testMissingLocalDirectory() {
        Configuration test = new Configuration("server", 1, "user", "password", 1, "remote", null);

        assertEquals(test.isValid(), false);
    }
}
