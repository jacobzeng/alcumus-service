package org.fangzz.alcumus.alcumusservice.dto.param;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageQueryParameter {
    private int start = 0;
    private int limit = 20;
    private String[] attributesOrderByAsc;
    private String[] attributesOrderByDesc;

    public Sort genSort() {
        List<Sort.Order> orderList = new ArrayList<>();
        if (attributesOrderByAsc != null && attributesOrderByAsc.length > 0) {
            for (int ascInx = 0; ascInx < attributesOrderByAsc.length; ascInx++) {
                orderList.add(new Sort.Order(Sort.Direction.ASC, attributesOrderByAsc[ascInx]));
            }
        }
        if (attributesOrderByDesc != null && attributesOrderByDesc.length > 0) {
            for (int descInx = 0; descInx < attributesOrderByDesc.length; descInx++) {
                orderList.add(new Sort.Order(Sort.Direction.DESC, attributesOrderByDesc[descInx]));
            }
        }
        if (orderList.size() == 0) {
            return null;
        } else {
            return Sort.by(orderList);
        }
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String[] getAttributesOrderByAsc() {
        return attributesOrderByAsc;
    }

    public void setAttributesOrderByAsc(String[] attributesOrderByAsc) {
        this.attributesOrderByAsc = attributesOrderByAsc;
    }

    public String[] getAttributesOrderByDesc() {
        return attributesOrderByDesc;
    }

    public void setAttributesOrderByDesc(String[] attributesOrderByDesc) {
        this.attributesOrderByDesc = attributesOrderByDesc;
    }

    @Override
    public String toString() {
        return "PageQueryParameter{" +
                "start=" + start +
                ", limit=" + limit +
                ", attributesOrderByAsc=" + Arrays.toString(attributesOrderByAsc) +
                ", attributesOrderByDesc=" + Arrays.toString(attributesOrderByDesc) +
                '}';
    }
}
