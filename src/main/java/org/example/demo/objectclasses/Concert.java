package org.example.demo.objectclasses;

import java.time.LocalDate;

public class Concert {
    private int id;
    private LocalDate date;
    private String city;
    private String venue;
    private MusicGroup musicGroup;
    private String orgName;
    private String orgPhone;

    public Concert(LocalDate date, MusicGroup musicGroup, String city, String venue, String orgName, String orgPhone) {
        this.date = date;
        this.city = city;
        this.venue = venue;
        this.musicGroup = musicGroup;
        this.orgName = orgName;
        this.orgPhone = orgPhone;
    }

    // Геттеры и Сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public MusicGroup getMusicGroup() {
        return musicGroup;
    }

    public void setMusicGroup(MusicGroup musicGroup) {
        this.musicGroup = musicGroup;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgPhone() {
        return orgPhone;
    }

    public void setOrgPhone(String orgPhone) {
        this.orgPhone = orgPhone;
    }

    @Override
    public String toString() {
        return musicGroup.getName() + " " + date + " " + city + " " + venue;
    }
}
