package com.yoimiya.onlinemall.controller;

import com.yoimiya.onlinemall.common.Result;
import com.yoimiya.onlinemall.entity.Address;
import com.yoimiya.onlinemall.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public Result<List<Address>> list(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(addressService.getAddresses(userId));
    }

    @GetMapping("/{id}")
    public Result<Address> detail(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(addressService.getAddress(id, userId));
    }

    @PostMapping
    public Result<Address> add(@RequestBody Address address, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        address.setUserId(userId);
        return Result.success(addressService.addAddress(address));
    }

    @PutMapping("/{id}")
    public Result<Address> update(@PathVariable Long id, @RequestBody Address address, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        address.setId(id);
        address.setUserId(userId);
        return Result.success(addressService.updateAddress(address));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        addressService.deleteAddress(id, userId);
        return Result.success();
    }

    @PutMapping("/{id}/default")
    public Result<Void> setDefault(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        addressService.setDefault(id, userId);
        return Result.success();
    }
}
