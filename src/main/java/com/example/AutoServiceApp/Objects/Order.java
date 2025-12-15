package com.example.AutoServiceApp.Objects;

import com.example.AutoServiceApp.Exceptions.IncorrectOrderTypeId;
import org.bson.types.ObjectId;

public class Order {
    private final int _typeID;
    private String _type;
    private final ObjectId _customerId;
    private final String _comment;

    public Order(int typeID, ObjectId customerId, String comment) throws IncorrectOrderTypeId {
        _typeID = typeID;
        SwitchTypeId();
        _customerId = customerId;
        _comment = comment;
    }

    void SwitchTypeId() throws IncorrectOrderTypeId {
        switch (_typeID) {
            case 1:
                _type = "Покраска авто";
                break;
            case 2:
                _type = "Замена шин";
                break;
            case 3:
                _type = "Замена внешней детали";
                break;
            case 4:
                _type = "Замена внутренней детали";
                break;
            case 5:
                _type = "Ремонт внешней детали";
                break;
            case 6:
                _type = "Ремонт внутренней детали";
                break;
            default:
                throw new IncorrectOrderTypeId();
        }
    }

    public Order(String type, ObjectId customerId, String comment) {
        _typeID = 0;
        _type = type;
        _customerId = customerId;
        _comment = comment;
    }

    public int getTypeID() { return _typeID; }
    public String getType() { return _type; }
    public String getComment() { return _comment; }
    public ObjectId getCustomerId() { return _customerId; }
}
