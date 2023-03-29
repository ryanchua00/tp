package seedu.address.logic.commands.tank;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.fish.FishAddCommand.MESSAGE_MISSING_TANK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FEEDING_INTERVAL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAST_FED_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPECIES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TANK;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_FISHES;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TANKS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.fish.FeedingInterval;
import seedu.address.model.fish.Fish;
import seedu.address.model.fish.LastFedDate;
import seedu.address.model.fish.Name;
import seedu.address.model.fish.Species;
import seedu.address.model.tag.Tag;
import seedu.address.model.tank.Tank;
import seedu.address.model.tank.TankName;

/**
 * Edits the details of an existing fish in the address book.
 */
public class TankEditCommand extends TankCommand {

    public static final String TANK_COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + TANK_COMMAND_WORD
            + ": Edits the details of the tank identified "
            + "by the index number used in the displayed tank list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME]\n"
            + "Example: " + COMMAND_WORD + " " + TANK_COMMAND_WORD + " 1 "
            + PREFIX_NAME + "Glass Tank";

    public static final String MESSAGE_EDIT_TANK_SUCCESS = "Edited Tank: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TANK = "This tank already exists in the address book.";
    public static final String MESSAGE_USE_INDEX = "Use a numerical index of a tank eg. /tk 1";
    private final Index index;
    private final EditTankDescriptor editTankDescriptor;

    /**
     * @param index of the fish in the filtered fish list to edit
     * @param editTankDescriptor details to edit the fish with
     */
    public TankEditCommand(Index index, EditTankDescriptor editTankDescriptor) {
        requireNonNull(index);
        requireNonNull(editTankDescriptor);

        this.index = index;
        this.editTankDescriptor = new EditTankDescriptor(editTankDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Tank> lastShownList = model.getFilteredTankList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_FISH_DISPLAYED_INDEX);
        }

        Tank tankToEdit = lastShownList.get(index.getZeroBased());
        Tank editedTank = createEditedTank(tankToEdit, editTankDescriptor);


        model.getTankList().setTank(tankToEdit, editedTank);

        model.updateFilteredTankList(PREDICATE_SHOW_ALL_TANKS);
        return new CommandResult(String.format(MESSAGE_EDIT_TANK_SUCCESS, editedTank));
    }

    /**
     * Creates and returns a {@code Fish} with the details of {@code fishToEdit}
     * edited with {@code editFishDescriptor}.
     */
    private static Tank createEditedTank(Tank tankToEdit, EditTankDescriptor editTankDescriptor) {
        assert tankToEdit != null;

        TankName updatedName = editTankDescriptor.getName().orElse(tankToEdit.getTankName());
        AddressBook fishList = tankToEdit.getFishList();

        return new Tank(updatedName, fishList);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof seedu.address.logic.commands.fish.FishEditCommand)) {
            return false;
        }

        // state check
        TankEditCommand e = (TankEditCommand) other;
        return index.equals(e.index)
                && editTankDescriptor.equals(e.editTankDescriptor);
    }

    /**
     * Stores the details to edit the fish with. Each non-empty field value will replace the
     * corresponding field value of the fish.
     */
    public static class EditTankDescriptor {
        private TankName name;

        public EditTankDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditTankDescriptor(EditTankDescriptor toCopy) {
            setName(toCopy.name);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name);
        }

        public void setName(TankName name) {
            this.name = name;
        }

        public Optional<TankName> getName() {
            return Optional.ofNullable(name);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTankDescriptor)) {
                return false;
            }

            // state check
            EditTankDescriptor e = (EditTankDescriptor) other;

            return getName().equals(e.getName());
        }
    }
}

