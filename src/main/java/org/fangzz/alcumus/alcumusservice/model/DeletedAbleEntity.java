package org.fangzz.alcumus.alcumusservice.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DeletedAbleEntity extends BaseEntity {
    private boolean deleted = false;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
