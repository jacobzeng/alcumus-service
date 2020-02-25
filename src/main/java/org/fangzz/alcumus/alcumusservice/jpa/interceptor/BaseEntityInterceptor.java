package org.fangzz.alcumus.alcumusservice.jpa.interceptor;

import org.fangzz.alcumus.alcumusservice.model.BaseEntity;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
public class BaseEntityInterceptor extends EmptyInterceptor {
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) {
        if (!(entity instanceof BaseEntity)) {
            return false;
        }

        int createdAtIndex = findPropertyNameIndex(propertyNames, "createdAt");
        int modifiedAtIndex = findPropertyNameIndex(propertyNames, "modifiedAt");
        Date now = new Date();
        if (currentState[createdAtIndex] == null) {
            currentState[createdAtIndex] = now;
        }
        currentState[modifiedAtIndex] = now;
        return true;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (!(entity instanceof BaseEntity)) {
            return false;
        }

        int createdAtIndex = findPropertyNameIndex(propertyNames, "createdAt");
        int modifiedAtIndex = findPropertyNameIndex(propertyNames, "modifiedAt");
        Date now = new Date();
        if (state[createdAtIndex] == null) {
            state[createdAtIndex] = now;
        }
        state[modifiedAtIndex] = now;
        return true;
    }


    private int findPropertyNameIndex(String[] propertyNames, String targetPropertyName) {
        for (int i = 0; i < propertyNames.length; i++) {
            String item = propertyNames[i];
            if (item.equals(targetPropertyName)) {
                return i;
            }
        }
        return -1;
    }
}
