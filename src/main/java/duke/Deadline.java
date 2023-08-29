package duke;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + convertDateToString(this.by) + ")";
    }

    @Override
    public String lineToWriteFile() {
        return "D | " + super.lineToWriteFile() + " | "
                + convertDateToString(this.by);
    }

    public String convertDateToString(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("MMM dd yyyy hh:mma"));
    }
}