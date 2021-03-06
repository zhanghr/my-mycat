package com.example.service.impl;

import com.example.common.dto.*;
import com.example.integration.dao.*;
import com.example.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;

@Service("com.example.service.CommonService")
public class CommonServiceImpl implements CommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    @Qualifier("com.example.integration.dao.impl.CourierDAO")
    private CourierDAO courierDAO;

    @Autowired
    @Qualifier("com.example.integration.dao.impl.OrdersDAO")
    private OrdersDAO ordersDAO;

    @Autowired
    @Qualifier("com.example.integration.dao.impl.OrderCargoDAO")
    private OrderCargoDAO orderCargoDAO;

    @Autowired
    @Qualifier("com.example.integration.dao.impl.OrderStatusInterceptionDAO")
    private OrderStatusInterceptionDAO orderStatusInterceptionDAO;

    @Autowired
    @Qualifier("com.example.integration.dao.impl.CustomerDAO")
    private CustomerDAO customerDAO;

    @Override
    public int addCourier(CourierDTO courierDTO) {
        int result = 0;
        try {
            result = courierDAO.add(courierDTO);
        } catch (SQLException e) {
            LOGGER.error("", e);
        }

        return result;
    }

    @Override
    public int addOrders(OrdersDTO ordersDTO) {
        int result = 0;
        try {
            result = ordersDAO.add(ordersDTO);
        } catch (SQLException e) {
            LOGGER.error("", e);
        }

        return result;
    }

    @Override
    public int addOrderCargo(OrderCargoDTO orderCargoDTO) {
        int result = 0;
        try {
            result = orderCargoDAO.add(orderCargoDTO);
        } catch (SQLException e) {
            LOGGER.error("", e);
        }

        return result;
    }

    @Override
    public int addOrderStatusInterception(OrderStatusInterceptionDTO orderStatusInterceptionDTO) {
        int result = 0;
        try {
            result = orderStatusInterceptionDAO.add(orderStatusInterceptionDTO);
        } catch (SQLException e) {
            LOGGER.error("", e);
        }

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void addOrderAndCargoInSingleDB() throws Exception {

        OrdersDTO ordersDTO = new OrdersDTO();
        OrderCargoDTO orderCargoDTO = new OrderCargoDTO();

        int loopIdx = 12;
        ordersDTO.setId(String.valueOf(loopIdx));
        ordersDTO.setInitialpoint(String.valueOf(loopIdx));
        ordersDTO.setEndpoint(String.valueOf(loopIdx));
        ordersDTO.setOrderStatus(String.valueOf(loopIdx));
        ordersDTO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        ordersDTO.setModifiedTime(new Timestamp(System.currentTimeMillis()));
        int result = ordersDAO.add(ordersDTO);
        if (1 == result) {
            System.out.println("addOrder success:" + loopIdx);

            orderCargoDTO.setCargoId(String.valueOf(loopIdx));
            orderCargoDTO.setOrderId(String.valueOf(loopIdx));
            result = orderCargoDAO.add(orderCargoDTO);
            if (result != 1) {
                System.out.println("addOrderAndCargo error:" + loopIdx);
            } else {
                System.out.println("addOrderAndCargo success:" + loopIdx);
            }
        }

        throw new Exception();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void addOrderAndCargoInMultiDB() throws Exception {

        OrdersDTO ordersDTO = new OrdersDTO();
        OrderCargoDTO orderCargoDTO = new OrderCargoDTO();

        for (int loopIdx = 15; loopIdx < 25; loopIdx++) {

            if (23 == loopIdx) {
                throw new Exception();
            }

            ordersDTO.setId(String.valueOf(loopIdx));
            ordersDTO.setInitialpoint(String.valueOf(loopIdx));
            ordersDTO.setEndpoint(String.valueOf(loopIdx));
            ordersDTO.setOrderStatus(String.valueOf(loopIdx));
            ordersDTO.setCreateTime(new Timestamp(System.currentTimeMillis()));
            ordersDTO.setModifiedTime(new Timestamp(System.currentTimeMillis()));
            int result = ordersDAO.add(ordersDTO);
            if (1 == result) {
                orderCargoDTO.setCargoId(String.valueOf(loopIdx));
                orderCargoDTO.setOrderId(String.valueOf(loopIdx));
                result = orderCargoDAO.add(orderCargoDTO);
                if (result != 1) {
                    System.out.println("addOrderAndCargo error:" + loopIdx);
                    throw new Exception();
                } else {
                    System.out.println("addOrderAndCargo success:" + loopIdx);
                }
            }
        }
    }

    @Override
    public int addCustomer(CustomerDTO customerDTO) {
        int result = 0;
        try {
            result = customerDAO.add(customerDTO);
        } catch (SQLException e) {
            LOGGER.error("", e);
        }

        return result;
    }
}
