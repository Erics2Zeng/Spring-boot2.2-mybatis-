package com.drawon.mall.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * This class corresponds to the database table user
 */
/**
 * user
 * @author DELL
 * @date 2020-03-28 10:51:49
 */
@Data
public class User implements Serializable {
    /**
     * Database Column Remarks:
     *
     *
     * This field corresponds to the database column user.open_id
     *
     * @mbg.generated
     */
    private String openId;

    /**
     * Database Column Remarks:
     *
     *
     * This field corresponds to the database column user.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     * Database Column Remarks:
     *   临时卡车型
     *
     * This field corresponds to the database column user.phone
     *
     * @mbg.generated
     */
    private String phone;

    /**
     *
     * This field corresponds to the database column user.country
     *
     * @mbg.generated
     */
    private String country;

    /**
     *
     * This field corresponds to the database column user.province
     *
     * @mbg.generated
     */
    private String province;

    /**
     *
     * This field corresponds to the database column user.city
     *
     * @mbg.generated
     */
    private String city;

    /**
     *
     * This field corresponds to the database column user.thumb_url
     *
     * @mbg.generated
     */
    private String thumbUrl;

    /**
     *
     * This field corresponds to the database column user.address_id
     *
     * @mbg.generated
     */
    private String addressId;

    /**
     *
     * This field corresponds to the database column user.expire_time
     *
     * @mbg.generated
     */
    private Date expireTime;

    /**
     *
     * This field corresponds to the database column user.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     *
     * This field corresponds to the database column user.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * This field corresponds to the database table user
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", openId=").append(openId);
        sb.append(", name=").append(name);
        sb.append(", phone=").append(phone);
        sb.append(", country=").append(country);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", thumbUrl=").append(thumbUrl);
        sb.append(", addressId=").append(addressId);
        sb.append(", expireTime=").append(expireTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}