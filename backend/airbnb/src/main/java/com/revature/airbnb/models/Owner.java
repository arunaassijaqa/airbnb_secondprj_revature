package com.revature.airbnb.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "owner")
public class Owner extends User {

    @OneToMany(mappedBy = "owner")
    private List<Listing> listings;

    public Owner()
    {

    }
 
    public List<Listing> getListings() {
        return this.listings;
    }

    public void setListings(List<Listing> listings) {
        this.listings = listings;
    }

    public int getOwnerId() {
        return this.userId;
    }

    public void setOwnerId(int ownerId) {
        this.userId = ownerId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
