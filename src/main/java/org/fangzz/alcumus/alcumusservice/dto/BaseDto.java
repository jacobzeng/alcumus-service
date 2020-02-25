package org.fangzz.alcumus.alcumusservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.fangzz.alcumus.alcumusservice.model.BaseEntity;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class BaseDto {
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
    private Date modifiedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public static void convert(BaseEntity model, BaseDto dto) {
        BeanUtils.copyProperties(model, dto);
    }
}
