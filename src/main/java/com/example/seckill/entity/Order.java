package com.example.seckill.entity;

import java.util.Date;

public class Order {
    private Long order_id;
    private String order_no;
    private Integer order_status;
    private String user_id;
    private String recv_name;
    private String recv_address;
    private String recv_mobile;
    private Long amount;
    private Long postage;
    private Date create_time;

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public Integer getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Integer order_status) {
        this.order_status = order_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRecv_name() {
        return recv_name;
    }

    public void setRecv_name(String recv_name) {
        this.recv_name = recv_name;
    }

    public String getRecv_address() {
        return recv_address;
    }

    public void setRecv_address(String recv_address) {
        this.recv_address = recv_address;
    }

    public String getRecv_mobile() {
        return recv_mobile;
    }

    public void setRecv_mobile(String recv_mobile) {
        this.recv_mobile = recv_mobile;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getPostage() {
        return postage;
    }

    public void setPostage(Long postage) {
        this.postage = postage;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
