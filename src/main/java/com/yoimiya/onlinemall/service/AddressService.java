package com.yoimiya.onlinemall.service;

import com.yoimiya.onlinemall.entity.Address;
import com.yoimiya.onlinemall.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    public List<Address> getAddresses(Long userId) {
        return addressMapper.findByUserId(userId);
    }

    public Address getAddress(Long id, Long userId) {
        return addressMapper.findByIdAndUserId(id, userId);
    }

    public Address addAddress(Address address) {
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            addressMapper.clearDefault(address.getUserId());
        }
        if (address.getIsDefault() == null) {
            address.setIsDefault(0);
        }
        // If this is the first address, set it as default
        List<Address> existing = addressMapper.findByUserId(address.getUserId());
        if (existing.isEmpty()) {
            address.setIsDefault(1);
        }
        addressMapper.insert(address);
        return address;
    }

    public Address updateAddress(Address address) {
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            addressMapper.clearDefault(address.getUserId());
        }
        addressMapper.update(address);
        return address;
    }

    public void deleteAddress(Long id, Long userId) {
        addressMapper.deleteByIdAndUserId(id, userId);
        // If deleted address was default, set first remaining as default
        Address defaultAddr = addressMapper.findDefaultByUserId(userId);
        if (defaultAddr == null) {
            List<Address> remaining = addressMapper.findByUserId(userId);
            if (!remaining.isEmpty()) {
                Address first = remaining.get(0);
                first.setIsDefault(1);
                addressMapper.update(first);
            }
        }
    }

    public void setDefault(Long id, Long userId) {
        Address address = addressMapper.findByIdAndUserId(id, userId);
        if (address != null) {
            addressMapper.clearDefault(userId);
            address.setIsDefault(1);
            addressMapper.update(address);
        }
    }
}
