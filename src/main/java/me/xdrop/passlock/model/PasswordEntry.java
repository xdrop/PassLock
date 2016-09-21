package me.xdrop.passlock.model;

import java.util.Date;

public class PasswordEntry<T extends EncryptionData> {

    private T encryptionData;
    private Date date;
    private String description;
    private String ref;
    private String id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getEncryptionData() {
        return encryptionData;
    }

    public void setEncryptionData(T encryptionData) {
        this.encryptionData = encryptionData;
    }
}
