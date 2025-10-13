package org.Exceptions;

public class PhoneNumberAlreadyExists extends RuntimeException {
    public PhoneNumberAlreadyExists() {
      super("Пользователь с таким номером телефона уже существует");
    }
}
