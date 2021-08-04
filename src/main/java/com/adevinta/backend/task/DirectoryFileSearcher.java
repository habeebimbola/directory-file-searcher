package com.adevinta.backend.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author ANIMASHAUN HABEEB ABIMBOLA
 * @version 1.0
 * @date July 2021
 */
public class DirectoryFileSearcher implements FileSearcher {

    private String directoryToSearch;
    private final Scanner keyboard = new Scanner(System.in);
    private static final String CMD_PROMPT = "Search>";
    private static final String PROMPT_TERMINATOR = ":quit";
    private String searchInput;
    private File directory;
    private Map<String, List<String>> rankingMap = new TreeMap<>();

    public DirectoryFileSearcher(String directoryToSearch) {
        this.directoryToSearch = directoryToSearch;
        this.directory = new File(directoryToSearch);
    }

    public void searchDirectory() {
        if (getDirectory().exists() && getDirectory().isDirectory()) {
            Map<String, Integer> frequencyMap = new TreeMap<>();
            File[] files = this.getDirectory().listFiles();
            Map<String, List<String>> treeMap = inMemoryWordsList(files);

            long filesCount = Arrays.stream(files).filter(file -> file.isFile() && file.getName().endsWith(".txt")).count();
            System.out.println(String.format("%d files read in directory %s ", filesCount, this.getDirectory()));
            setSearchInput(displayPrompt());
            int foundCount = 0;

            while (!PROMPT_TERMINATOR.equalsIgnoreCase(getSearchInput())) {
                boolean notFound = true;

                String[] searchWords = getSearchInput().split("[^A-Za-z]+");

                for (String fileName : treeMap.keySet()) {
                    List<String> stringList = treeMap.get(fileName);
                    //Default search assumes case-sensitivity
                    //Todo: Can convert List Contents to a particular case for insensitive Search....

                    for (int index = 0; index < searchWords.length; index++) {
                        Collections.sort(stringList);
                        int searchIndex = Collections.binarySearch(stringList, searchWords[index]);

                        if (searchIndex > -1) {
                            ++foundCount;
                            notFound = false;
                        }
                    }
                    if (searchWords.length == foundCount) {
                        frequencyMap.put(fileName, 100);
                    }
                    if (foundCount == 0) {
                        frequencyMap.put(fileName, 0);
                    }
                    if ((foundCount < searchWords.length) && (foundCount > 0)) {
                        frequencyMap.put(fileName, (int) (((double) foundCount / searchWords.length) * 100));
                    }
                    foundCount = 0;
                }

                this.displaySearchResult(notFound, getSearchInput(), frequencyMap);
                frequencyMap.clear();

                setSearchInput(displayPrompt());
            }

        }

    }

    private String displayPrompt() {
        System.out.print(CMD_PROMPT);
        String input = this.keyboard.nextLine();
        return input;
    }

    private Map inMemoryWordsList(File[] files) {
        Arrays.stream(files).filter(file -> file.getName().endsWith(".txt") && file.isFile()).forEach(file -> {
            List<String> stringList = new ArrayList<>();
            try (Scanner fileReader = new Scanner(file)) {
                while (fileReader.hasNext()) {
                    stringList.add(fileReader.next().replaceAll("([^A-za-z]+)", ""));
                }
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            this.getRankingMap().put(file.getName(), stringList);

        });
        return this.getRankingMap();
    }
    private void displaySearchResult(boolean notFound, String searchInput, Map<String,Integer> resultsMap) {

        if (notFound) {
            System.out.println(String.format("%s \"%s\"", "No matches found for ", searchInput));
            return;
        }

        TreeMap<Integer, String> mapSortedRank = new TreeMap<>();

        for(String key : resultsMap.keySet())
        {
           mapSortedRank.put(resultsMap.get(key), key);
        }

        mapSortedRank.descendingMap().forEach( (freq, file) -> {
            System.out.println(String.format("%s %s %s %d ", "File:", file, "Rank", freq));
        });

    }

    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
    }

    public String getSearchInput() {
        return searchInput;
    }

    public File getDirectory() {
        return directory;
    }

    public Map<String, List<String>> getRankingMap() {
        return rankingMap;
    }
}
