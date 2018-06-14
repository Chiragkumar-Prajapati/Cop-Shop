package com.ctrlaltelite.copshop.objects;

public class SellerAccountObject extends AccountObject {
    private String organizationName;

    public SellerAccountObject(String id, String password, String email, String organizationName) {
        super(id, password, email);
        this.organizationName = organizationName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
