package org.fangzz.alcumus.alcumusservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity extends IntegerIdentifier {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt; //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedAt; //最后的修改时间

    public static void onModify(BaseEntity target) {
        target.setModifiedAt(new Date());
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

    @Override
    public String toString() {
        return "BaseEntity{" +
                "createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
