package com.thebois.controllers;

import com.thebois.models.beings.roles.IRoleAllocator;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.views.RoleView;

public class RoleController {

    private final IRoleAllocator roleAllocator;
    private final RoleView roleView;

    public RoleController(final IRoleAllocator roleAllocator, final RoleView roleView) {
        this.roleAllocator = roleAllocator;
        this.roleView = roleView;

        roleView.updateRoles(RoleFactory.all());
    }

}
