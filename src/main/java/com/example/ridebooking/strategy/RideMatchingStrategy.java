package com.example.ridebooking.strategy;

import com.example.ridebooking.model.Driver;
import com.example.ridebooking.model.Rider;
import java.util.List;

public interface RideMatchingStrategy {

    Driver findDriver(Rider rider, List<Driver> drivers);



}
