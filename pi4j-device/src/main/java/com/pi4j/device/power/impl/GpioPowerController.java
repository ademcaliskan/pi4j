package com.pi4j.device.power.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  GpioPowerController.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.pi4j.device.power.PowerControllerBase;
import com.pi4j.device.power.PowerState;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

public class GpioPowerController extends PowerControllerBase
{
    // internal class members
    GpioPinDigitalOutput pin = null;
    PinState onState = PinState.HIGH;
    PinState offState = PinState.LOW;
    
    /**
     * using this constructor requires that the consumer 
     *  define the POWER ON and POWER OFF pin states 
     *  
     * @param pin GPIO digital output pin
     * @param onState pin state to set when power is ON
     * @param offState pin state to set when power is OFF
     */
    public GpioPowerController(GpioPinDigitalOutput pin, PinState onState, PinState offState)
    {
        this.pin = pin;
        this.onState = onState;
        this.offState = offState;
    }

    /**
     * default constructor; using this constructor assumes that:
     *  (1) a pin state of HIGH is POWER ON
     *  (2) a pin state of LOW  is POWER OFF
     *  
     * @param pin GPIO digital output pin
     */
    public GpioPowerController(GpioPinDigitalOutput pin)
    {
        this.pin = pin;        
    }

    /**
     * Return the current power state based on the  
     * GPIO digital output pin state.
     *  
     * @return PowerState 
     */
    @Override
    public PowerState getState()
    {
        if(pin.isState(onState))
            return PowerState.ON;
        else if(pin.isState(offState))
            return PowerState.OFF;
        else
            return PowerState.UNKNOWN;
    }

    /**
     * Set the current GPIO digital output pin state
     * based on the supplied power state
     * 
     * @param state new power state to apply
     */
    @Override
    public void setState(PowerState state)
    {
        switch(state)
        {
            case OFF:
            {
                if(!isOff())
                    pin.setState(offState);
                break;
            }
            case ON:
            {
                if(!isOn())
                    pin.setState(onState);
                break;
            }
            default:
            {
                throw new UnsupportedOperationException("Cannot set power state: " + state.toString());
            }
        }
    }
}
