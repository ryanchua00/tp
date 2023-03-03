package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.task.Task;

public interface ReadOnlyTaskList {
    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Task> getTaskList();
}
