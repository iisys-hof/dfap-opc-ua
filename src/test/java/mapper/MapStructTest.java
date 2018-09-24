/*
 * Copyright 2018 Thomas Winkler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mapper;

import mapper.testModels.*;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

class MapStructTest {

    @Test
    public void shouldMapCarToDTO() {
        Car car = new Car( "Morris", 5, CarType.SEDAN );

        CarDTO carDto = CarMapper.INSTANCE.carToCarDto( car );

        System.out.println(car);
        System.out.println(carDto);

        assertNotNull(carDto);
        assertEquals(carDto.getMake(), "Morris");
        assertEquals(carDto.getSeatCount(), 5);
        assertEquals(carDto.getType(), "SEDAN");

    }

    @Test
    public void shouldMapOrderToOrderDTO() {
        Order order = new Order();
        order.setBillingAddress(new Address("Hohenberg", "Regnitzlosau"));
        order.setCustomer(new Customer(new Name("Thomas", "Winkler")));

        OrderDTO orderDTO = OrderMapper.INSTANCE.orderToOrderDto(order);

        System.out.println(order);
        System.out.println(orderDTO);

        assertEquals(order.getCustomer().getName().getFirstName(), orderDTO.getCustomerFirstName());
        assertEquals(order.getCustomer().getName().getLastName(), orderDTO.getCustomerLastName());
        assertEquals(order.getBillingAddress().getStreet(), orderDTO.getBillingStreet());
        assertEquals(order.getBillingAddress().getCity(), orderDTO.getBillingCity());
    }
}