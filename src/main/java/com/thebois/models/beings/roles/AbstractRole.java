package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import com.thebois.models.IDeepClonable;
import com.thebois.models.beings.tasks.ITaskGenerator;
import com.thebois.models.beings.tasks.ITask;

/**
 * Represents an assignment to a specific work task.
 */
public abstract class AbstractRole implements IDeepClonable<AbstractRole> {

    private final Queue<ITaskGenerator> tasks = new LinkedList<>();
    private ITask currentTask;

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass().equals(obj.getClass())) {
            final AbstractRole other = (AbstractRole) obj;
            return this.getType().equals(other.getType());
        }
        return false;
    }

    /**
     * Gets the occupation type.
     *
     * @return The occupation type.
     */
    public abstract RoleType getType();

    /**
     * Gets the title of the role occupation.
     *
     * @return The title of the role occupation.
     */
    public String getName() {
        final String className = this.getClass().getSimpleName();
        final int roleSuffixLength = 4;
        return className.substring(0, className.length() - roleSuffixLength);
    }

    @Override
    public AbstractRole deepClone() {
        return RoleFactory.fromType(getType());
    }

    /**
     * Calculates the next task to perform at the moment and returns it.
     *
     * @return The current task that needs to be completed.
     */
    public ITask obtainNextTask() {
        if (tasks.isEmpty()) tasks.addAll(getTaskGenerators());

        if (currentTask == null || currentTask.isCompleted()) {
            // Take tasks from queue until one is found that is not completed.
            ITask newTask;
            do {
                final ITaskGenerator actionable = tasks.remove();
                newTask = actionable.generate();
            } while (newTask.isCompleted());

            this.currentTask = newTask;
        }

        return currentTask;
    }

    /**
     * Gets a set of task generators that, when called, generate a task with up to date
     * information.
     * <p>
     * Task generators are used instead of tasks directly, because the generated task may differ
     * depending on the moment in time when it is to be performed. E.g. a house needs to be
     * constructed, but once the role gets around to handing out that task the house is already
     * completed.
     * </p>
     *
     * @return Generators of tasks to perform.
     */
    protected abstract Collection<ITaskGenerator> getTaskGenerators();

}
