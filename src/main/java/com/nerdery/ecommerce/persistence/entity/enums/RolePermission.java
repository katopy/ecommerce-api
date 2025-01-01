package com.nerdery.ecommerce.persistence.entity.enums;

import lombok.Getter;

public enum RolePermission {
    READ_ALL_PRODUCTS,
    READ_ONE_PRODUCT,
    CREATE_ONE_PRODUCT,
    UPDATE_ONE_PRODUCT,
    DISABLE_ONE_PRODUCT,
    SHOW_ORDERS_PER_CLIENT,

    READ_ALL_CATEGORIES,
    READ_ONE_CATEGORY,
    CREATE_ONE_CATEGORY,
    UPDATE_ONE_CATEGORY,
    DISABLE_ONE_CATEGORY,

    READ_MY_PROFILE,
    SHOW_MY_ORDERS,

    ADD_ITEMS_CART,

    CREATE_PAYMENT_INTENT,
    ADD_PRODUCT_IMAGE,

    READ_ONE_ORDER;

}
