package com.adevinta.backend.task;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Unit test for DirectoryFileSearcher.
 */
public class DiectoryFileSearcherTest
{
    FileSearcher fileSearcher;

    @DisplayName("Initializing Search Directory With Specific Directory")
    @BeforeEach
    void setupFileSearcherTest( )
    {
       Path filePath  = directoryToSearch.resolve("/users/home");

       fileSearcher = new DirectoryFileSearcher(filePath.toFile().getPath());

    }

    @TempDir
    Path directoryToSearch ;

    @DisplayName("Validating Search directory")
    @Test
    void directoryToSearchTest(){
        File file = directoryToSearch.toFile();

        assertEquals(directoryToSearch.toFile().isDirectory() && directoryToSearch.toFile().exists(),
                true,"Directory To Search Must Exist and Must a Valid Directory" );
    }
}
