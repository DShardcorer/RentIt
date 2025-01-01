package com.greem.rentit.service;


import com.greem.rentit.dao.PropertyRepository;
import com.greem.rentit.entity.Property;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PropertyService {

    private PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }


    public Boolean isPropertyOfLandlord(String landlordEmail, int propertyId){
        Property property = propertyRepository.findById(propertyId).get();

        return property.getLandlordEmail().equals(landlordEmail);

    }

    public void deleteProperty(int id) {
        propertyRepository.deleteById(id);
    }
}
