package com.example.microservicetwo.service;


import com.example.microservicetwo.entity.ServiceTwoEntity;
import com.example.microservicetwo.repo.ServiceTwoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceTwoServiceimpl implements ServiceTwoService {

    @Autowired
    private ServiceTwoRepo serviceTwoRepo;

    @Override
    public ServiceTwoEntity saveEntity(ServiceTwoEntity serviceTwoEntity) {
        return serviceTwoRepo.save(serviceTwoEntity);
    }
}
