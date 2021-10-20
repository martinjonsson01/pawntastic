package com.thebois.models.beings.roles;

/**
 * The builder constructs buildings.
 *
 * @author Martin
 */
class BuilderRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.BUILDER;
    }

}
