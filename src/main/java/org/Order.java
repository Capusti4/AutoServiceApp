package org;

import org.Exceptions.IncorrectOrderTypeId;
import org.bson.types.ObjectId;

public class Order {
    private final int typeID;
    private final String type;
    private final ObjectId customerId;
    private final String comment;

    public Order(int typeID, ObjectId customerId, String comment) throws IncorrectOrderTypeId {
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
                throw new IncorrectOrderTypeId();
        }
        this.typeID = typeID;
        this.customerId = customerId;
        this.comment = comment;
    }

    public Order(String type, ObjectId customerId, String comment) {
        this.typeID = 0;
        this.type = type;
        this.customerId = customerId;
        this.comment = comment;
    }

    public int getTypeID() { return typeID; }
    public String getType() { return type; }
    public String getComment() { return comment; }
    public ObjectId getCustomerId() { return customerId; }
}
