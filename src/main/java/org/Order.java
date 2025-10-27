package org;

import com.google.gson.internal.LinkedTreeMap;
import org.Exceptions.IncorrectOrderID;

public class Order {
    private final int typeID;
    private final String type;
    private final String clientUsername;
    private final String clientFirstName;
    private final String clientLastName;
    private final String clientPhoneNum;
    private final String comment;

    public Order(int typeID, LinkedTreeMap<String, String> userInfo, String comment) {
        switch (typeID) {
            case 1:
                this.type = "Покраска авто";
                break;
            case 2:
                this.type = "Замена шин";
                break;
            case 3:
                this.type = "Замена внешней детали";
                break;
            case 4:
                this.type = "Замена внутренней детали";
                break;
            case 5:
                this.type = "Ремонт внешней детали";
                break;
            case 6:
                this.type = "Ремонт внутренней детали";
                break;
            default:
                throw new IncorrectOrderID();
        }
        this.typeID = typeID;
        this.clientUsername = userInfo.get("username");
        this.clientFirstName = userInfo.get("firstName");
        this.clientLastName = userInfo.get("LastName");
        this.clientPhoneNum = userInfo.get("phoneNum");
        this.comment = comment;
    }

    public Order(String type, LinkedTreeMap<String, String> userInfo, String comment) {
        this.typeID = 0;
        this.type = type;
        this.clientUsername = userInfo.get("username");
        this.clientFirstName = userInfo.get("firstName");
        this.clientLastName = userInfo.get("LastName");
        this.clientPhoneNum = userInfo.get("phoneNum");
        this.comment = comment;
    }

    public int getTypeID() { return typeID; }
    public String getClientUsername() { return clientUsername; }
    public String getType() { return type; }
    public String getComment() { return comment; }
    public String getClientFirstName() { return clientFirstName; }
    public String getClientLastName() { return clientLastName; }
    public String getClientPhoneNum() { return clientPhoneNum; }
}
