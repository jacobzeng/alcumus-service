package org.fangzz.alcumus.alcumusservice.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface AbstractRepository<T> extends PagingAndSortingRepository<T, Integer>, JpaSpecificationExecutor {
}
