package com.adevinta.backend.task;

/**
 * @author ANIMASHAUN HABEEB ABIMBOLA
 * Entry Point To The Command Line Searcher On Files From A Directory...
 *
 */
public class FileSearcherMain
{
    public static void main( String[] args )
    {
        if(args.length == 0 )
        {
            throw new IllegalArgumentException("No Directory Path Specified.");
        }

        FileSearcher directoryFileSearcher = new DirectoryFileSearcher(args[0]);
        directoryFileSearcher.searchDirectory();
    }
}
