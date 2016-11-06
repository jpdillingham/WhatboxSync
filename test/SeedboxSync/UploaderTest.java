/****************************************************************************
 *
 * UploaderTest.java
 *
 * Tests the Uploader class.
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

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests the Uploader class.
 */
public class UploaderTest {
    /**
     * The logger for this class.
     */
    private static Logger logger = LoggerFactory.getLogger(new Throwable().getStackTrace()[0].getClassName());

    /**
     * The temporary folder for the class.
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * Configure the logger.
     */
    @Before
    public void configureLogging() {
        ConsoleAppender console = new ConsoleAppender();
        console.setLayout(new PatternLayout("%d{yyyy-MM-dd' 'HH:mm:ss.SSS} [%-5p] [%c] - %m%n"));
        console.setThreshold(Level.INFO);
        console.activateOptions();
        org.apache.log4j.Logger.getRootLogger().addAppender(console);
        org.apache.log4j.Logger.getRootLogger().setAdditivity(false);
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        IServer server = mock(IServer.class);

        Uploader uploader = new Uploader(server, "local", "remote");
    }

    /**
     * Tests the queue.
     */
    @Test
    public void testQueue() {
        IServer server = mock(IServer.class);

        Uploader test = new Uploader(server, "local", "remote");

        // enqueue a string and confirm the size
        test.enqueue("hello world!");
        assertEquals(test.getQueue().size(), 1);

        // try to enqueue the same string.  it should be discarded.
        test.enqueue("hello world!");
        assertEquals(test.getQueue().size(), 1);

        // remove the string we enqueued
        test.dequeue("hello world!");
        assertEquals(test.getQueue().size(), 0);

        // try to remove the same string.  it shouldn't raise an error.
        test.dequeue("hello world!");
    }

    /**
     * Tests a typical process() call
     * @throws IOException
     */
    @Test
    public void testProcess() throws Exception {
        File uploadFolder = folder.newFolder("upload");
        File uploadFile = folder.newFile("upload/file.txt");

        IServer server = mock(IServer.class);
        Uploader uploader = new Uploader(server, uploadFolder.getAbsolutePath(), "");

        uploader.enqueue(uploadFile.getAbsolutePath());

        uploader.process();

        // assert that the file was removed from the queue
        assertEquals(uploader.getQueue().size(), 0);

        // assert that the original file is now missing, and that a file with
        // [Uploaded] prepended to the name exists.
        assertEquals(uploadFile.exists(), false);

        assertEquals(new File(uploadFile.getParent() + "/[Uploaded] file.txt").exists(), true);
    }

    /**
     * Tests process() with a known bad upload folder.
     * @throws IOException
     */
    @Test
    public void testMissingDirProcess() throws Exception {
        IServer server = mock(IServer.class);
        Uploader uploader = new Uploader(server, "bad folder", "");

        uploader.enqueue("test");

        uploader.process();
    }

    /**
     * Tests process() under all possible combinations of file/folders.
     * @throws IOException
     */
    @Test
    public void testAllFolderPossibilitiesProcess() throws Exception {
        File uploadFolder = folder.newFolder("upload");
        File childFolder = folder.newFolder("upload","test");
        File file = folder.newFile("upload/file.txt");
        File uploadedFile = folder.newFile("upload/[Uploaded] file2.txt");

        IServer server = mock(IServer.class);
        Uploader uploader = new Uploader(server, uploadFolder.getAbsolutePath(), "");

        uploader.process();
    }

    /**
     * Tests process() with an empty queue.
     * @throws IOException
     */
    @Test
    public void testEmptyQueueProcess() throws Exception {
        File uploadFolder = folder.newFolder("upload");

        IServer server = mock(IServer.class);
        Uploader uploader = new Uploader(server, uploadFolder.getAbsolutePath(), "");

        // assert that the queue is empty
        assertEquals(uploader.getQueue().size(), 0);
        uploader.process();
    }

    /**
     * Test the process() method with a known server exception
     * @throws Exception
     */
    @Test
    public void testProcessUploadException() throws Exception {
        IServer server = mock(IServer.class);

        Mockito.when(server.upload("local/one", "remote/one")).thenThrow(Exception.class);

        Uploader test = new Uploader(server, "local", "remote");

        test.enqueue("one");

        test.process();
    }
}