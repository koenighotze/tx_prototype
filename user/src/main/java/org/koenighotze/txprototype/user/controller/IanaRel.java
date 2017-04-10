package org.koenighotze.txprototype.user.controller;

public enum IanaRel {
    EDIT("edit"),
    COLLECTION("collection"),
    CREATE_FORM("create-form"),
    EDIT_FORM("edit-form");

    private final String rel;

    IanaRel(String rel) {
        this.rel = rel;
    }

    public String getRel() {
        return rel;
    }
}
