package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import com.thebois.abstractions.IDeepClonable;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.actions.IActionSource;

/**
 * Represents an assignment to a specific work task.
 */
public abstract class AbstractRole implements IDeepClonable<AbstractRole> {

    private final Queue<IActionSource> tasks = new LinkedList<>();
    private IAction currentAction;

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
     * Calculates the next action to perform at the moment and returns it.
     *
     * @param performer The entity who will perform the obtained action.
     *
     * @return The current action that needs to be completed.
     */
    public IAction obtainNextAction(final IActionPerformer performer) {
        if (needsNewAction(performer)) {
            currentAction = getNextUncompletedAction(performer);
        }

        if (!currentAction.canPerform(performer)) {
            currentAction = RoleFactory.idle().obtainNextAction(performer);
        }

        return currentAction;
    }

    private boolean needsNewAction(final IActionPerformer performer) {
        return currentAction == null || currentAction.isCompleted(performer);
    }

    private IAction getNextUncompletedAction(final IActionPerformer performer) {
        // Take actions from queue until one is found that is not completed.
        IAction newAction;
        do {
            if (tasks.isEmpty()) tasks.addAll(getTaskGenerators());

            final IActionSource actionable = tasks.remove();
            newAction = actionable.generate(performer);
        } while (newAction.isCompleted(performer));
        return newAction;
    }

    /**
     * Gets a set of task generators that, when called, generate a task with up-to-date
     * information.
     * <p>
     * Task generators are used instead of actions directly, because the generated task may differ
     * depending on the moment in time when it is to be performed. E.g. a house needs to be
     * constructed, but once the role gets around to handing out that task the house is already
     * completed.
     * </p>
     *
     * @return Generators of actions to perform.
     */
    protected abstract Collection<IActionSource> getTaskGenerators();

}
