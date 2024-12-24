package com.nerdery.ecommerce.persistence.entity.enums;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public enum Role {
    ROLE_ADMINISTRATOR(Arrays.asList(
            RolePermission.READ_ALL_PRODUCTS,
            RolePermission.READ_ALL_CATEGORIES,
            RolePermission.CREATE_ONE_PRODUCT,
            RolePermission.CREATE_ONE_CATEGORY,
            RolePermission.SHOW_MY_ORDERS,
            RolePermission.UPDATE_ONE_PRODUCT
            )),
    ROLE_CUSTOMER(Collections.singletonList(RolePermission.READ_MY_PROFILE));

    private final List<RolePermission> permissionList;

    Role(List<RolePermission> permissionList) {
        this.permissionList = permissionList;
    }
}
