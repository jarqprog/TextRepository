package com.jarq.system.service.address;

public interface IAddressService {

    String createAddress(int userId, String postalCode, String city,
                         String street, String houseNo, String apartmentNo);

    String changePostalCode(int userId, String postalCode);

    String changeCity(int userId, String city);

    String changeStreet(int userId, String street);

    String changeHouseNo(int userId, String houseNo);

    String changeApartmentNo(int userId, String apartmentNo);

    String removeAddress(int userId);
}