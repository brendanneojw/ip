import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Scanner;

public class Storage {

    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }
    private void createDirectory() {
        File f = new File("./data");
        if (!hasDirectory() && !f.mkdir()) {
            return;
        }
    }

    private static boolean hasDirectory() {
        File f = new File("./data");
        if (f.exists() && f.isDirectory()) {
            return true;
        }
        Ui.directoryNotFound();
        return false;
    }

    private Scanner createOrGetFile() throws DukeException {
        File f = new File(this.filePath);
        try {
            if (!f.createNewFile()) {
               Ui.savedFileFound();
            } else {
               Ui.savedFileNotFound();
            }
            return new Scanner(f);
        } catch (IOException e) {
            throw new DukeException("HEYHEY! Seems like I couldn't create the file for you. "
                    + "Please manually add the file!");
        }
    }

    private LocalDateTime undoDateFormatInputFile(String lineInFile) {
        return LocalDateTime.parse(lineInFile, DateTimeFormatter.ofPattern("MMM dd yyyy hh:mma"));
    }

    public ArrayList<Task> loadFile() {
        createDirectory();

        Scanner s = createOrGetFile();
        ArrayList<Task> taskArray = new ArrayList<>();

        while (s.hasNext()) {
            String[] line = s.nextLine().trim().split(" \\| ", 3);

            switch (line[0]) {
            case "T":
                taskArray.add(new Todo(line[2]));
                break;
            case "D":
                String[] remainLine = line[2].split(" \\| ", 2);
                taskArray.add(new Deadline(remainLine[0], undoDateFormatInputFile(remainLine[1])));
                break;
            case "E":
                String[] remainingLine = line[2].split(" \\| ", 2);
                String[] getDateTime = remainingLine[1].split(" to ");
                taskArray.add(new Event(remainingLine[0],
                        undoDateFormatInputFile(getDateTime[0]), undoDateFormatInputFile(getDateTime[1])));
                break;
            }

            if (line[1].equals("X")) {
                taskArray.get(taskArray.size() - 1).markDone();
            }
        }
        return taskArray;
    }

    public void addLineToFile(TaskList taskList) throws DukeException {
        try {
            FileWriter fw = new FileWriter(this.filePath, true);
            if (taskList.taskArray.size() == 1) {
                fw.write(taskList.taskArray.get(0).lineToWriteFile());
            } else {
                fw.write("\n" + taskList.taskArray.get(taskList.taskArray.size() - 1).lineToWriteFile());
            }
            fw.close();
        } catch (IOException e) {
            throw new DukeException("UHOH! Something went wrong when attempting to write to file!");
        }
    }

    public void rewriteFile(TaskList taskList) throws DukeException {
        try {
            FileWriter fw = new FileWriter(this.filePath);
            for (int i = 0; i < taskList.taskArray.size(); i++) {
                if (i != taskList.taskArray.size() - 1) {
                    fw.write(taskList.taskArray.get(i).lineToWriteFile() + "\n");
                } else {
                    fw.write(taskList.taskArray.get(i).lineToWriteFile());
                }
            }
            fw.close();
        } catch (IOException e) {
            throw new DukeException("UHOH! Something went wrong when attempting to write to file!");
        }
    }
}