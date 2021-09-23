package com.thebois.models.beings.roles;

/**
 * The builder constructs buildings.
 */
class BuilderRole extends AbstractRole {

    @Override
    public RoleType getType() {
        return RoleType.BUILDER;
    }

}
